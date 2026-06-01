package com.venkat.dsa.hashing;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic hash map using open addressing with linear probing and tombstone deletion.
 *
 * <p><b>Definition:</b> A hash table that resolves collisions by probing successive
 * slots in the underlying array (linear probing), storing all entries in a single
 * flat array rather than linked chains.</p>
 *
 * <p><b>Backing representation:</b> A fixed-length array of {@code Entry<K,V>} objects.
 * A special sentinel {@code TOMBSTONE} entry marks slots that held a deleted key,
 * allowing continued probing through deleted positions without breaking lookup chains.</p>
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>Capacity is always a power of two (simplifies modulo to bitwise AND).</li>
 *   <li>Load factor (live entries / capacity) never exceeds 0.5 before insertion;
 *       the table doubles and rehashes when this threshold is crossed.</li>
 *   <li>Tombstones are not counted toward the load factor but are reclaimed on resize.</li>
 *   <li>Every key stored satisfies {@code key != null} (null keys are rejected).</li>
 * </ul>
 * </p>
 *
 * <p><b>Operations (n = live entries, m = capacity):</b>
 * <pre>
 * Operation     Average    Worst      Space
 * ----------    -------    -----      -----
 * put           O(1)       O(n)       O(1) amortised per insert
 * get           O(1)       O(n)       O(1)
 * remove        O(1)       O(n)       O(1)
 * containsKey   O(1)       O(n)       O(1)
 * size          O(1)       O(1)       O(1)
 * resize        O(n)       O(n)       O(m) new array
 * </pre>
 * Worst-case O(n) arises when every key hashes to the same slot (full cluster).</p>
 *
 * <p><b>When to use:</b> When you need a fast in-memory key-value store and want
 * cache-friendly memory layout (better than chaining for small-to-medium tables),
 * and can tolerate amortised O(1) inserts with occasional O(n) rehash pauses.</p>
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Linear probing suffers from <em>primary clustering</em>; quadratic probing
 *       and double hashing reduce clustering at the cost of complexity.</li>
 *   <li>Tombstones are necessary so that a {@code get} probe does not stop early
 *       at a gap left by a deletion.</li>
 *   <li>Load factor must be kept low (typically ≤ 0.5–0.75) to preserve O(1) average.</li>
 *   <li>Java's {@link java.util.HashMap} uses separate chaining, not open addressing.</li>
 * </ul>
 * </p>
 *
 * @param <K> the type of keys; must not be {@code null}
 * @param <V> the type of values; may be {@code null}
 */
@SuppressWarnings("unchecked")
public class HashMapOpenAddressing<K, V> {

    // -----------------------------------------------------------------------
    // Internal types
    // -----------------------------------------------------------------------

    private static final class Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Sentinel object used to mark deleted slots.
    private static final Entry<?, ?> TOMBSTONE = new Entry<>(null, null);

    // -----------------------------------------------------------------------
    // Constants & fields
    // -----------------------------------------------------------------------

    private static final int DEFAULT_CAPACITY = 16;   // must be power of two
    private static final double MAX_LOAD_FACTOR = 0.5;

    private Entry<K, V>[] table;
    private int size;          // number of live (non-tombstone) entries
    private int capacity;      // current array length (power of two)

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Creates a hash map with the default initial capacity (16).
     * <p>O(1).</p>
     */
    public HashMapOpenAddressing() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates a hash map with at least {@code initialCapacity} slots, rounded up
     * to the next power of two.
     * <p>O(1).</p>
     *
     * @param initialCapacity desired minimum number of slots; must be positive
     * @throws IllegalArgumentException if {@code initialCapacity} is not positive
     */
    public HashMapOpenAddressing(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        this.capacity = nextPowerOfTwo(initialCapacity);
        this.table = (Entry<K, V>[]) new Entry[this.capacity];
        this.size = 0;
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains a mapping for the key, the value is updated.
     * Triggers a resize when the load factor would exceed {@value #MAX_LOAD_FACTOR}.
     *
     * <p>O(1) average, O(n) worst case per operation; O(n) amortised over all puts.</p>
     *
     * @param key   key with which to associate the value; must not be {@code null}
     * @param value value to be associated with the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(K key, V value) {
        requireNonNull(key);
        // Resize before inserting so load factor stays within bounds.
        if ((double) (size + 1) / capacity > MAX_LOAD_FACTOR) {
            resize();
        }
        int idx = probeForWrite(key);
        if (table[idx] == null || table[idx] == TOMBSTONE) {
            table[idx] = new Entry<>(key, value);
            size++;
        } else {
            // Key already present — update value.
            table[idx].value = value;
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key.
     *
     * <p>O(1) average, O(n) worst case.</p>
     *
     * @param key key whose associated value is to be returned; must not be {@code null}
     * @return the associated value, or {@code null} if absent
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public V get(K key) {
        requireNonNull(key);
        int idx = probeForRead(key);
        if (idx < 0) {
            return null;
        }
        return table[idx].value;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * The slot is replaced with a tombstone so that subsequent probes remain correct.
     *
     * <p>O(1) average, O(n) worst case.</p>
     *
     * @param key key whose mapping is to be removed; must not be {@code null}
     * @return the previous value associated with the key, or {@code null} if absent
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public V remove(K key) {
        requireNonNull(key);
        int idx = probeForRead(key);
        if (idx < 0) {
            return null;
        }
        V old = table[idx].value;
        table[idx] = (Entry<K, V>) TOMBSTONE;
        size--;
        return old;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * <p>O(1) average, O(n) worst case.</p>
     *
     * @param key key whose presence in this map is to be tested; must not be {@code null}
     * @return {@code true} if the key is present
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean containsKey(K key) {
        requireNonNull(key);
        return probeForRead(key) >= 0;
    }

    /**
     * Returns the number of live key-value mappings in this map.
     *
     * <p>O(1).</p>
     *
     * @return the number of entries
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this map contains no entries.
     *
     * <p>O(1).</p>
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns all live keys in an unordered list.
     *
     * <p>O(m) where m is the current capacity.</p>
     *
     * @return list of keys
     */
    public List<K> keys() {
        List<K> result = new ArrayList<>(size);
        for (Entry<K, V> entry : table) {
            if (entry != null && entry != TOMBSTONE) {
                result.add(entry.key);
            }
        }
        return result;
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Linear-probing search that returns the index of the live entry for {@code key},
     * or {@code -1} if no such entry exists.
     */
    private int probeForRead(K key) {
        int hash = hash(key);
        for (int i = 0; i < capacity; i++) {
            int idx = (hash + i) & (capacity - 1);
            Entry<K, V> entry = table[idx];
            if (entry == null) {
                return -1;                          // empty slot: key absent
            }
            if (entry != TOMBSTONE && entry.key.equals(key)) {
                return idx;
            }
        }
        return -1;
    }

    /**
     * Linear-probing search that returns the index where {@code key} should be written.
     * If the key already exists, returns its current index (for update).
     * Otherwise returns the first available slot (null or tombstone).
     */
    private int probeForWrite(K key) {
        int hash = hash(key);
        int firstTombstone = -1;
        for (int i = 0; i < capacity; i++) {
            int idx = (hash + i) & (capacity - 1);
            Entry<K, V> entry = table[idx];
            if (entry == null) {
                // Prefer the first tombstone seen; fall back to this empty slot.
                return (firstTombstone >= 0) ? firstTombstone : idx;
            }
            if (entry == TOMBSTONE) {
                if (firstTombstone < 0) {
                    firstTombstone = idx;
                }
            } else if (entry.key.equals(key)) {
                return idx;                         // existing key: will update
            }
        }
        // Table is full of tombstones (shouldn't happen if resize works correctly).
        return firstTombstone;
    }

    /**
     * Doubles the capacity and rehashes all live entries into the new table.
     */
    private void resize() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] newTable = (Entry<K, V>[]) new Entry[newCapacity];
        int newCapMask = newCapacity - 1;
        for (Entry<K, V> entry : table) {
            if (entry == null || entry == TOMBSTONE) {
                continue;
            }
            int hash = hash(entry.key);
            // Re-probe into new table (no tombstones in fresh table).
            for (int i = 0; i < newCapacity; i++) {
                int idx = (hash + i) & newCapMask;
                if (newTable[idx] == null) {
                    newTable[idx] = entry;
                    break;
                }
            }
        }
        table = newTable;
        capacity = newCapacity;
        // size (live entry count) is unchanged; tombstones are discarded.
    }

    /**
     * Spreads the key's hash code to reduce clustering. Must NOT mask with {@code capacity}:
     * every caller already masks with the (current) capacity, and {@code resize()} rehashes with
     * the new capacity. If this baked in the old capacity, entries would be placed during resize
     * at one slot and looked up afterward at another, making them unreachable.
     */
    private int hash(K key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    /** Round {@code n} up to the nearest power of two >= 1. */
    private static int nextPowerOfTwo(int n) {
        if (n <= 1) return 1;
        int p = 1;
        while (p < n) {
            p <<= 1;
        }
        return p;
    }

    private static void requireNonNull(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }
}
