package com.venkat.backend.lld.cache;

import java.util.Optional;

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

/**
 * Immutable snapshot of cache statistics at a point in time.
 *
 * @param hits       total number of successful {@link Cache#get} calls
 * @param misses     total number of unsuccessful {@link Cache#get} calls
 * @param evictions  total number of entries removed by the eviction policy
 * @param size       current number of entries in the cache
 */
record CacheStats(long hits, long misses, long evictions, int size) {}

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

/**
 * Identifies the eviction algorithm to be created by {@link CacheFactory}.
 */
enum PolicyType {
    /** Least-Recently-Used: evict the entry accessed furthest in the past. */
    LRU,
    /** Least-Frequently-Used: evict the entry with the lowest access frequency. */
    LFU
}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

/**
 * Strategy that the cache delegates all eviction decisions to.
 *
 * <p>The cache must call these callbacks at the appropriate points so the
 * policy's internal bookkeeping stays consistent:
 * <ul>
 *   <li>{@link #recordInsert} — immediately after a new key is inserted into
 *       the cache's primary data store.</li>
 *   <li>{@link #recordAccess} — immediately after a cache <em>hit</em> (a
 *       successful {@link Cache#get}).</li>
 *   <li>{@link #remove} — whenever a key is removed from the cache for any
 *       reason <em>other than</em> eviction (i.e. explicit
 *       {@link Cache#remove} or {@link Cache#clear}).</li>
 *   <li>{@link #evict} — when the cache needs to free one slot; the policy
 *       selects the candidate, removes it from its own bookkeeping, and
 *       returns the key so the cache can remove it from the primary store.</li>
 * </ul>
 *
 * @param <K> key type
 */
interface EvictionPolicy<K> {

    /**
     * Notify the policy that {@code key} was just read (cache hit).
     * Implementations should update recency / frequency metadata here.
     *
     * @param key the key that was accessed; never {@code null}
     */
    void recordAccess(K key);

    /**
     * Notify the policy that {@code key} was just inserted for the first time.
     *
     * @param key the newly inserted key; never {@code null}
     */
    void recordInsert(K key);

    /**
     * Notify the policy that {@code key} was explicitly removed from the cache
     * and must also be removed from the policy's internal bookkeeping.
     *
     * @param key the removed key; never {@code null}
     */
    void remove(K key);

    /**
     * Select and remove one key to evict according to the policy's algorithm.
     *
     * @return the evicted key, or {@link Optional#empty()} if there is nothing
     *         to evict (policy tracking is empty)
     */
    Optional<K> evict();
}

// ---------------------------------------------------------------------------
// Core cache interface
// ---------------------------------------------------------------------------

/**
 * Generic fixed-capacity in-memory cache with a pluggable eviction policy.
 *
 * <p>All implementations must be safe for concurrent use from multiple threads
 * unless explicitly documented otherwise.
 *
 * @param <K> key type — must be non-null
 * @param <V> value type — {@code null} values are permitted (null is a valid
 *            cached result; it is distinct from a cache miss)
 */
interface Cache<K, V> {

    /**
     * Return the value associated with {@code key}, updating the eviction
     * policy's access metadata on a hit.
     *
     * @param key the lookup key; must not be {@code null}
     * @return the cached value wrapped in {@link Optional}, or
     *         {@link Optional#empty()} on a miss
     * @throws NullPointerException if {@code key} is {@code null}
     */
    Optional<V> get(K key);

    /**
     * Associate {@code key} with {@code value} in the cache.  If the cache is
     * at capacity, one entry is evicted by the active policy before the new
     * entry is inserted.  If {@code key} already exists, its value is updated
     * and its eviction metadata is refreshed (treat as a new access).
     *
     * @param key   the key; must not be {@code null}
     * @param value the value to cache; {@code null} is permitted
     * @throws NullPointerException if {@code key} is {@code null}
     */
    void put(K key, V value);

    /**
     * Explicitly remove an entry.  If {@code key} is not present this is a
     * no-op.
     *
     * @param key the key to remove; must not be {@code null}
     */
    void remove(K key);

    /**
     * Force the eviction policy to evict one entry immediately, regardless of
     * the current size.
     *
     * @return the evicted key, or {@link Optional#empty()} if the cache is
     *         empty
     */
    Optional<K> evict();

    /**
     * Return a point-in-time snapshot of cache statistics.  This method must
     * not modify any cache or policy state.
     *
     * @return an immutable {@link CacheStats}
     */
    CacheStats stats();

    /**
     * Remove all entries and reset all statistics counters to zero.
     */
    void clear();

    /**
     * Return the maximum number of entries this cache can hold.  Immutable
     * after construction.
     *
     * @return capacity &ge; 1
     */
    int capacity();
}

// ---------------------------------------------------------------------------
// Factory — assembles the correct Cache + EvictionPolicy pair
// ---------------------------------------------------------------------------

/**
 * Factory that constructs a {@link Cache} wired with the appropriate
 * {@link EvictionPolicy} for the requested {@link PolicyType}.
 *
 * <p>Example:
 * <pre>{@code
 *     Cache<String, byte[]> lru = CacheFactory.create(PolicyType.LRU, 128);
 *     Cache<Integer, String> lfu = CacheFactory.create(PolicyType.LFU, 64);
 * }</pre>
 */
final class CacheFactory {

    private CacheFactory() {}

    /**
     * Create a new cache of the given {@code policyType} and {@code capacity}.
     *
     * @param policyType the eviction algorithm to use; must not be {@code null}
     * @param capacity   maximum number of entries; must be &ge; 1
     * @param <K>        key type
     * @param <V>        value type
     * @return a fully initialised, empty {@link Cache}
     * @throws NullPointerException     if {@code policyType} is {@code null}
     * @throws IllegalArgumentException if {@code capacity} &lt; 1
     */
    public static <K, V> Cache<K, V> create(PolicyType policyType, int capacity) {
        throw new UnsupportedOperationException("implement me");
    }
}

// ---------------------------------------------------------------------------
// Skeleton concrete classes — learner fills in the internals
// ---------------------------------------------------------------------------

/**
 * LRU cache skeleton.
 *
 * <p>Target: O(1) {@code get} and {@code put} using a doubly-linked list
 * paired with a {@link java.util.HashMap}.
 *
 * <p><strong>Do not implement yet</strong> — design the fields and the
 * internal node structure first.  The {@link EvictionPolicy} contract must be
 * honoured: the eviction logic lives in {@code LruEvictionPolicy}, not here.
 *
 * @param <K> key type
 * @param <V> value type
 */
class LruCache<K, V> implements Cache<K, V> {

    private final int capacity;
    // TODO: add fields — primary data store, policy reference, stats counters

    LruCache(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        this.capacity = capacity;
        // TODO: initialise fields
    }

    @Override
    public Optional<V> get(K key) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public void put(K key, V value) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public void remove(K key) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public Optional<K> evict() {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public CacheStats stats() {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public int capacity() {
        return capacity;
    }
}

/**
 * LFU cache skeleton.
 *
 * <p>Target: O(1) {@code get} and {@code put}.  Hint: maintain a
 * frequency-to-bucket mapping where each bucket is an ordered set of keys
 * sharing the same access count; track the minimum frequency separately.
 *
 * <p><strong>Do not implement yet</strong> — sketch the data structures on
 * paper before writing any code.
 *
 * @param <K> key type
 * @param <V> value type
 */
class LfuCache<K, V> implements Cache<K, V> {

    private final int capacity;
    // TODO: add fields — value store, frequency store, frequency buckets,
    //       min-frequency tracker, policy reference, stats counters

    LfuCache(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        this.capacity = capacity;
        // TODO: initialise fields
    }

    @Override
    public Optional<V> get(K key) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public void put(K key, V value) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public void remove(K key) {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public Optional<K> evict() {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public CacheStats stats() {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("implement me");
    }

    @Override
    public int capacity() {
        return capacity;
    }
}
