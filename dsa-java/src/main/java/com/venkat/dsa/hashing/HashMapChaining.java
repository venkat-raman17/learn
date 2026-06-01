package com.venkat.dsa.hashing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hash map using separate chaining (array of linked-list buckets).
 *
 * <p><b>Definition:</b> A key-value store that maps keys to values using a hash
 * function to distribute entries across an array of buckets, resolving collisions
 * by chaining entries in a linked list at each bucket index.
 *
 * <p><b>Backing representation:</b> An array ({@code Node<K,V>[]}) of singly-linked
 * list heads. Each slot holds a chain of {@code Node} objects containing the key,
 * value, and next pointer.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>Load factor = size / capacity is always kept {@code <= 0.75} after a put.</li>
 *   <li>Capacity is always a positive power of two (starts at 16).</li>
 *   <li>Each key appears in at most one node across all buckets.</li>
 * </ul>
 *
 * <p><b>Operations:</b>
 * <pre>
 * Operation     Average   Worst    Space
 * ----------    -------   -----    -----
 * put           O(1)      O(n)     O(1) amortised
 * get           O(1)      O(n)     O(1)
 * remove        O(1)      O(n)     O(1)
 * containsKey   O(1)      O(n)     O(1)
 * keySet        O(n)      O(n)     O(n)
 * rehash        O(n)      O(n)     O(n)
 * </pre>
 * Worst case occurs when all keys collide into one bucket.
 *
 * <p><b>When to use:</b> When you need O(1) average-case lookups and insertions,
 * keys are not naturally sorted, and memory is not severely constrained.
 * Separate chaining degrades more gracefully than open addressing under high load.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Spread hash: multiply by a prime (31 works well) and XOR with the upper
 *       bits to reduce clustering caused by poor {@code hashCode} implementations.</li>
 *   <li>Use {@code Math.floorMod} instead of {@code %} so that negative hash codes
 *       still produce a non-negative index.</li>
 *   <li>Load factor threshold of 0.75 is the value used by {@link java.util.HashMap}.</li>
 *   <li>Doubling capacity keeps amortised rehash cost O(1) per insertion.</li>
 * </ul>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class HashMapChaining<K, V> {

    // ------------------------------------------------------------------ node --

    private static final class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // ---------------------------------------------------------------- fields --

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    @SuppressWarnings("unchecked")
    private Node<K, V>[] buckets = new Node[INITIAL_CAPACITY];
    private int size = 0;

    // --------------------------------------------------------- hash helpers --

    /**
     * Spreads the key's hash code to reduce bucket clustering.
     *
     * <p>Multiplies by a large prime and XORs the upper 16 bits down so that
     * small differences in the upper bits affect the index even with a small
     * capacity. {@code Math.floorMod} guarantees a non-negative result.
     *
     * @param key      the key (may be {@code null})
     * @param capacity the current bucket array length
     * @return a valid array index in {@code [0, capacity)}
     */
    private int indexFor(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        // Spread: mix with upper bits using a prime multiplier
        h = h ^ (h >>> 16);
        h = h * 0x45d9f3b;
        h = h ^ (h >>> 16);
        return Math.floorMod(h, capacity);
    }

    // --------------------------------------------------------- public API --

    /**
     * Associates the specified value with the specified key.
     * If the key already exists its value is updated and the old value is returned.
     *
     * <p>Rehashes when the load factor would exceed 0.75 after insertion.
     *
     * <p>Time: O(1) average, O(n) worst.  Space: O(1) amortised (O(n) during rehash).
     *
     * @param key   the key (may be {@code null})
     * @param value the value to associate
     * @return the previous value for {@code key}, or {@code null} if absent
     */
    public V put(K key, V value) {
        int idx = indexFor(key, buckets.length);
        Node<K, V> cur = buckets[idx];

        // Update if key already present
        while (cur != null) {
            if (keysEqual(cur.key, key)) {
                V old = cur.value;
                cur.value = value;
                return old;
            }
            cur = cur.next;
        }

        // Insert at head of chain
        buckets[idx] = new Node<>(key, value, buckets[idx]);
        size++;

        if ((double) size / buckets.length > LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
        return null;
    }

    /**
     * Returns the value mapped to {@code key}, or {@code null} if absent.
     *
     * <p>Time: O(1) average, O(n) worst.  Space: O(1).
     *
     * @param key the key to look up (may be {@code null})
     * @return the associated value, or {@code null}
     */
    public V get(K key) {
        int idx = indexFor(key, buckets.length);
        Node<K, V> cur = buckets[idx];
        while (cur != null) {
            if (keysEqual(cur.key, key)) {
                return cur.value;
            }
            cur = cur.next;
        }
        return null;
    }

    /**
     * Removes the entry for {@code key} and returns its value, or {@code null} if absent.
     *
     * <p>Time: O(1) average, O(n) worst.  Space: O(1).
     *
     * @param key the key to remove (may be {@code null})
     * @return the removed value, or {@code null}
     */
    public V remove(K key) {
        int idx = indexFor(key, buckets.length);
        Node<K, V> prev = null;
        Node<K, V> cur = buckets[idx];

        while (cur != null) {
            if (keysEqual(cur.key, key)) {
                if (prev == null) {
                    buckets[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return cur.value;
            }
            prev = cur;
            cur = cur.next;
        }
        return null;
    }

    /**
     * Returns {@code true} if this map contains a mapping for {@code key}.
     *
     * <p>Time: O(1) average, O(n) worst.  Space: O(1).
     *
     * @param key the key to test (may be {@code null})
     * @return {@code true} if present
     */
    public boolean containsKey(K key) {
        int idx = indexFor(key, buckets.length);
        Node<K, V> cur = buckets[idx];
        while (cur != null) {
            if (keysEqual(cur.key, key)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * <p>Time: O(1).  Space: O(1).
     *
     * @return the number of entries
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if the map contains no entries.
     *
     * <p>Time: O(1).  Space: O(1).
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns a snapshot {@link Set} of all keys currently in the map.
     * Modifying the returned set does not affect the map.
     *
     * <p>Time: O(n + capacity).  Space: O(n).
     *
     * @return set of keys
     */
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>(size * 2);
        for (Node<K, V> head : buckets) {
            Node<K, V> cur = head;
            while (cur != null) {
                keys.add(cur.key);
                cur = cur.next;
            }
        }
        return keys;
    }

    // ------------------------------------------------------- internal helpers --

    /** Null-safe key equality using {@code equals}. */
    private boolean keysEqual(K a, K b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * Doubles the bucket array capacity and re-inserts all existing entries.
     *
     * <p>Time: O(n + old_capacity).  Space: O(n).
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];

        for (Node<K, V> head : buckets) {
            Node<K, V> cur = head;
            while (cur != null) {
                Node<K, V> next = cur.next;
                int newIdx = indexFor(cur.key, newCapacity);
                cur.next = newBuckets[newIdx];
                newBuckets[newIdx] = cur;
                cur = next;
            }
        }
        buckets = newBuckets;
    }

    /**
     * Returns the current internal capacity (number of buckets).
     * Exposed for white-box testing of rehash behaviour.
     *
     * <p>Time: O(1).  Space: O(1).
     *
     * @return the bucket array length
     */
    public int capacity() {
        return buckets.length;
    }
}
