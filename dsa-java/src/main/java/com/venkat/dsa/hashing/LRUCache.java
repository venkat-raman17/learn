package com.venkat.dsa.hashing;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU Cache (Least Recently Used Cache).
 *
 * <p><b>Definition:</b> A fixed-capacity cache that evicts the least recently used entry
 * when the capacity is exceeded.</p>
 *
 * <p><b>Backing Representation:</b> A {@link java.util.HashMap} for O(1) key-to-node
 * lookup, plus a hand-implemented doubly linked list that maintains recency order.
 * The head of the list is the Most Recently Used (MRU) node; the tail is the Least
 * Recently Used (LRU) node.</p>
 *
 * <p><b>Invariants:</b></p>
 * <ul>
 *   <li>The map and the doubly linked list are always in sync (same number of entries).</li>
 *   <li>{@code size() <= capacity()} at all times.</li>
 *   <li>The head sentinel's {@code prev} is {@code null}; the tail sentinel's {@code next}
 *       is {@code null}.</li>
 *   <li>Every live node is reachable from head via {@code next} links and every node's
 *       {@code prev} links back to its predecessor.</li>
 * </ul>
 *
 * <p><b>Operations:</b></p>
 * <pre>
 * Operation | Time  | Space (extra)
 * ----------|-------|---------------
 * get(key)  | O(1)  | O(1)
 * put(k,v)  | O(1)  | O(1) amortised
 * size()    | O(1)  | O(1)
 * capacity()| O(1)  | O(1)
 * </pre>
 *
 * <p><b>When to use:</b> Page replacement, browser history, database buffer pool, or any
 * scenario where you want O(1) access to frequently used items and automatic eviction of
 * stale ones under a memory cap.</p>
 *
 * <p><b>Interview notes:</b></p>
 * <ul>
 *   <li>The classic HashMap + doubly-linked-list design achieves O(1) for both get and put
 *       because the map gives direct node access (no list traversal needed).</li>
 *   <li>Sentinel head/tail nodes eliminate null checks on insert/remove.</li>
 *   <li>Java's {@link java.util.LinkedHashMap} provides a built-in LRU cache with
 *       {@code removeEldestEntry}; this implementation shows the mechanism explicitly.</li>
 *   <li>Thread safety requires external synchronisation or a {@code ConcurrentHashMap}
 *       variant with locks per bucket.</li>
 * </ul>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LRUCache<K, V> {

    // -------------------------------------------------------------------------
    // Internal doubly linked list node
    // -------------------------------------------------------------------------

    private class Node {
        K key;
        V value;
        Node prev;
        Node next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final int capacity;
    private final Map<K, Node> map;

    /** Sentinel head — node after head is MRU. */
    private final Node head;

    /** Sentinel tail — node before tail is LRU. */
    private final Node tail;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Creates an LRU cache with the given capacity.
     *
     * @param capacity maximum number of entries; must be &gt;= 1
     * @throws IllegalArgumentException if capacity &lt; 1
     */
    public LRUCache(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be >= 1, got: " + capacity);
        }
        this.capacity = capacity;
        this.map = new HashMap<>(capacity * 2);

        // Sentinel nodes — keys and values are never accessed
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Returns the value associated with {@code key}, or {@code null} if not present.
     * Accessing a key marks it as most recently used.
     *
     * <p><b>Time:</b> O(1)</p>
     *
     * @param key the key to look up
     * @return the associated value, or {@code null} if absent
     */
    public V get(K key) {
        Node node = map.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    /**
     * Inserts or updates the entry for {@code key} with {@code value}.
     * If the cache is at capacity and a new key is inserted, the least recently
     * used entry is evicted first.
     *
     * <p><b>Time:</b> O(1) amortised</p>
     *
     * @param key   the key (must not be {@code null})
     * @param value the value to associate with the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        Node existing = map.get(key);
        if (existing != null) {
            // Update value and refresh recency
            existing.value = value;
            moveToHead(existing);
            return;
        }

        // Evict LRU if at capacity
        if (map.size() == capacity) {
            Node lru = tail.prev; // node just before tail sentinel
            removeNode(lru);
            map.remove(lru.key);
        }

        // Insert new node at head (MRU position)
        Node newNode = new Node(key, value);
        insertAtHead(newNode);
        map.put(key, newNode);
    }

    /**
     * Returns the number of entries currently in the cache.
     *
     * <p><b>Time:</b> O(1)</p>
     *
     * @return current number of entries
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns the maximum number of entries this cache can hold.
     *
     * <p><b>Time:</b> O(1)</p>
     *
     * @return the fixed capacity
     */
    public int capacity() {
        return capacity;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /** Removes {@code node} from its current position in the doubly linked list. */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /** Inserts {@code node} immediately after the head sentinel (MRU position). */
    private void insertAtHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /** Moves an existing {@code node} to the MRU position (head). */
    private void moveToHead(Node node) {
        removeNode(node);
        insertAtHead(node);
    }
}
