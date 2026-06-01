package com.venkat.backend.lld.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

record CacheStats(long hits, long misses, long evictions, int size) {}

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

enum PolicyType {
    LRU,
    LFU
}

// ---------------------------------------------------------------------------
// Strategy interface — eviction policy callbacks
// ---------------------------------------------------------------------------

interface EvictionPolicy<K> {
    void recordAccess(K key);
    void recordInsert(K key);
    void remove(K key);
    Optional<K> evict();
}

// ---------------------------------------------------------------------------
// Core cache interface
// ---------------------------------------------------------------------------

interface Cache<K, V> {
    Optional<V> get(K key);
    void put(K key, V value);
    void remove(K key);
    Optional<K> evict();
    CacheStats stats();
    void clear();
    int capacity();
}

// ---------------------------------------------------------------------------
// Factory
// ---------------------------------------------------------------------------

final class CacheFactory {

    private CacheFactory() {}

    public static <K, V> Cache<K, V> create(PolicyType policyType, int capacity) {
        Objects.requireNonNull(policyType, "policyType must not be null");
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        return switch (policyType) {
            case LRU -> new LruCache<>(capacity);
            case LFU -> new LfuCache<>(capacity);
        };
    }
}

// ---------------------------------------------------------------------------
// LRU Cache — O(1) get/put via doubly-linked list + HashMap
// ---------------------------------------------------------------------------

class LruCache<K, V> implements Cache<K, V> {

    // Doubly-linked list node
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node<K, V>> map = new HashMap<>();
    // Sentinel head (MRU side) and tail (LRU side)
    private final Node<K, V> head = new Node<>(null, null);
    private final Node<K, V> tail = new Node<>(null, null);
    private final ReentrantLock lock = new ReentrantLock();

    // Stats
    private long hits;
    private long misses;
    private long evictions;

    LruCache(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    // -- Linked-list helpers (must be called under lock) --

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertAfterHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        insertAfterHead(node);
    }

    @Override
    public Optional<V> get(K key) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            Node<K, V> node = map.get(key);
            if (node == null) { misses++; return Optional.empty(); }
            hits++;
            moveToHead(node);
            return Optional.ofNullable(node.value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            Node<K, V> existing = map.get(key);
            if (existing != null) {
                existing.value = value;
                moveToHead(existing);
                return;
            }
            if (map.size() == capacity) {
                // Evict LRU — the node just before tail
                Node<K, V> lru = tail.prev;
                removeNode(lru);
                map.remove(lru.key);
                evictions++;
            }
            Node<K, V> node = new Node<>(key, value);
            insertAfterHead(node);
            map.put(key, node);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(K key) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            Node<K, V> node = map.remove(key);
            if (node != null) removeNode(node);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<K> evict() {
        lock.lock();
        try {
            if (map.isEmpty()) return Optional.empty();
            Node<K, V> lru = tail.prev;
            removeNode(lru);
            map.remove(lru.key);
            evictions++;
            return Optional.of(lru.key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public CacheStats stats() {
        lock.lock();
        try {
            return new CacheStats(hits, misses, evictions, map.size());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            map.clear();
            head.next = tail;
            tail.prev = head;
            hits = misses = evictions = 0;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int capacity() { return capacity; }
}

// ---------------------------------------------------------------------------
// LFU Cache — O(1) get/put via freq-bucket map + min-freq tracker
// ---------------------------------------------------------------------------

class LfuCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, V> values = new HashMap<>();              // key → value
    private final Map<K, Integer> freqs = new HashMap<>();         // key → frequency
    private final Map<Integer, LinkedHashSet<K>> buckets = new HashMap<>(); // freq → ordered keys
    private int minFreq = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private long hits;
    private long misses;
    private long evictions;

    LfuCache(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        this.capacity = capacity;
    }

    private void incrementFreq(K key) {
        int f = freqs.get(key);
        freqs.put(key, f + 1);
        buckets.get(f).remove(key);
        if (buckets.get(f).isEmpty()) {
            buckets.remove(f);
            if (minFreq == f) minFreq = f + 1;
        }
        buckets.computeIfAbsent(f + 1, k -> new LinkedHashSet<>()).add(key);
    }

    @Override
    public Optional<V> get(K key) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            if (!values.containsKey(key)) { misses++; return Optional.empty(); }
            hits++;
            incrementFreq(key);
            return Optional.ofNullable(values.get(key));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            if (values.containsKey(key)) {
                values.put(key, value);
                incrementFreq(key);
                return;
            }
            if (values.size() == capacity) {
                // Evict the LFU entry (oldest insertion order within minFreq bucket)
                LinkedHashSet<K> minBucket = buckets.get(minFreq);
                K evicted = minBucket.iterator().next();
                minBucket.remove(evicted);
                if (minBucket.isEmpty()) buckets.remove(minFreq);
                values.remove(evicted);
                freqs.remove(evicted);
                evictions++;
            }
            values.put(key, value);
            freqs.put(key, 1);
            buckets.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFreq = 1;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(K key) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            if (!values.containsKey(key)) return;
            int f = freqs.remove(key);
            values.remove(key);
            LinkedHashSet<K> bucket = buckets.get(f);
            if (bucket != null) {
                bucket.remove(key);
                if (bucket.isEmpty()) buckets.remove(f);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<K> evict() {
        lock.lock();
        try {
            if (values.isEmpty()) return Optional.empty();
            LinkedHashSet<K> minBucket = buckets.get(minFreq);
            K evicted = minBucket.iterator().next();
            minBucket.remove(evicted);
            if (minBucket.isEmpty()) buckets.remove(minFreq);
            values.remove(evicted);
            freqs.remove(evicted);
            evictions++;
            return Optional.of(evicted);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public CacheStats stats() {
        lock.lock();
        try {
            return new CacheStats(hits, misses, evictions, values.size());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            values.clear();
            freqs.clear();
            buckets.clear();
            minFreq = 0;
            hits = misses = evictions = 0;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int capacity() { return capacity; }
}
