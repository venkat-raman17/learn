package com.venkat.dsa.hashing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generic Hash Set implemented with separate chaining (linked lists per bucket).
 *
 * <p><b>Definition:</b> An unordered collection of unique elements that supports
 * constant-time average add, remove, and contains operations.</p>
 *
 * <p><b>Backing representation:</b> An array ({@code Object[]}) of {@code List<Node<T>>}
 * buckets. Each bucket is a singly-linked chain of {@code Node} wrappers.
 * The number of buckets is always a power of two so that the index computation
 * {@code hash & (capacity - 1)} avoids the modulo operator.</p>
 *
 * <p><b>Invariants:</b></p>
 * <ul>
 *   <li>No duplicate elements (equality determined by {@code equals}/{@code hashCode}).</li>
 *   <li>{@code size} always equals the true number of stored elements.</li>
 *   <li>{@code loadFactor = size / capacity}. When {@code loadFactor > LOAD_FACTOR_THRESHOLD}
 *       after an insertion, the table is resized to {@code 2 * capacity} and all elements
 *       are rehashed.</li>
 * </ul>
 *
 * <p><b>Operations table:</b></p>
 * <pre>
 * Operation  | Average Time | Worst-case Time | Space
 * -----------|--------------|-----------------|------
 * add        | O(1)         | O(n)            | O(1) amortised
 * contains   | O(1)         | O(n)            |  —
 * remove     | O(1)         | O(n)            |  —
 * size       | O(1)         | O(1)            |  —
 * isEmpty    | O(1)         | O(1)            |  —
 * resize     | O(n)         | O(n)            | O(n)
 * Overall space: O(n + capacity)
 * </pre>
 * <p>Worst-case O(n) occurs when all elements hash to the same bucket (degenerate).</p>
 *
 * <p><b>When to use:</b> When you need fast membership testing and uniqueness enforcement
 * and do not require ordering or sorted iteration. Prefer over a sorted set when order
 * does not matter and the key set is large.</p>
 *
 * <p><b>Interview notes:</b></p>
 * <ul>
 *   <li>Separate chaining vs open addressing: chaining is simpler and tolerates load
 *       factors above 1.0; open addressing has better cache locality.</li>
 *   <li>Resize threshold: Java's {@code HashMap} uses 0.75; higher saves memory but
 *       increases collision probability.</li>
 *   <li>Power-of-two capacity lets you replace expensive modulo with bitwise AND.</li>
 *   <li>Always override both {@code hashCode} and {@code equals} in element types.</li>
 * </ul>
 *
 * @param <T> the type of elements maintained by this set
 */
public class HashSet<T> {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Default initial capacity (must be a power of two). */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * Load-factor threshold. A resize is triggered after any insertion that causes
     * {@code size / capacity} to exceed this value.
     */
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    // -------------------------------------------------------------------------
    // Internal node
    // -------------------------------------------------------------------------

    /**
     * A singly-linked node that stores one element in a bucket chain.
     *
     * @param <E> element type mirroring the outer class type parameter
     */
    private static final class Node<E> {
        final E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** Bucket array. Each slot is either {@code null} (empty) or the head of a chain. */
    @SuppressWarnings("unchecked")
    private Node<T>[] buckets;

    /** Number of elements currently stored. */
    private int size;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructs an empty HashSet with the default initial capacity (16)
     * and a load-factor threshold of 0.75.
     *
     * <p>Time: O(capacity)  Space: O(capacity)</p>
     */
    @SuppressWarnings("unchecked")
    public HashSet() {
        buckets = (Node<T>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Constructs an empty HashSet with the specified initial capacity.
     * The provided value is rounded up to the next power of two.
     *
     * <p>Time: O(capacity)  Space: O(capacity)</p>
     *
     * @param initialCapacity desired initial capacity; must be positive
     * @throws IllegalArgumentException if {@code initialCapacity < 1}
     */
    @SuppressWarnings("unchecked")
    public HashSet(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException(
                    "initialCapacity must be >= 1, got: " + initialCapacity);
        }
        int cap = nextPowerOfTwo(initialCapacity);
        buckets = (Node<T>[]) new Node[cap];
        size = 0;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Adds {@code value} to this set if it is not already present.
     *
     * <p>Time: O(1) average, O(n) worst  Space: O(1) amortised</p>
     *
     * @param value the element to add; must not be {@code null}
     * @return {@code true} if the element was added; {@code false} if it was already present
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public boolean add(T value) {
        Objects.requireNonNull(value, "null elements are not permitted");
        int idx = bucketIndex(value);
        Node<T> head = buckets[idx];

        // Walk the chain — reject duplicates
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            if (cur.value.equals(value)) {
                return false;
            }
        }

        // Prepend new node (O(1))
        buckets[idx] = new Node<>(value, head);
        size++;

        // Resize if load factor exceeded
        if ((double) size / buckets.length > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        return true;
    }

    /**
     * Returns {@code true} if this set contains {@code value}.
     *
     * <p>Time: O(1) average, O(n) worst  Space: O(1)</p>
     *
     * @param value element to search for; must not be {@code null}
     * @return {@code true} if found, {@code false} otherwise
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public boolean contains(T value) {
        Objects.requireNonNull(value, "null elements are not permitted");
        int idx = bucketIndex(value);
        for (Node<T> cur = buckets[idx]; cur != null; cur = cur.next) {
            if (cur.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes {@code value} from this set if it is present.
     *
     * <p>Time: O(1) average, O(n) worst  Space: O(1)</p>
     *
     * @param value element to remove; must not be {@code null}
     * @return {@code true} if the element was removed; {@code false} if not found
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public boolean remove(T value) {
        Objects.requireNonNull(value, "null elements are not permitted");
        int idx = bucketIndex(value);
        Node<T> cur = buckets[idx];
        Node<T> prev = null;

        while (cur != null) {
            if (cur.value.equals(value)) {
                if (prev == null) {
                    buckets[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    /**
     * Returns the number of elements in this set.
     *
     * <p>Time: O(1)  Space: O(1)</p>
     *
     * @return element count
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this set contains no elements.
     *
     * <p>Time: O(1)  Space: O(1)</p>
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Spreads the hash code (Wang/Jenkins mix) and masks it to a valid bucket index.
     * Using power-of-two capacity allows a fast bitwise AND instead of modulo.
     */
    private int bucketIndex(T value) {
        int h = value.hashCode();
        // Secondary hash to reduce clustering (similar to HashMap's internal spread)
        h ^= (h >>> 16);
        return h & (buckets.length - 1);
    }

    /**
     * Doubles the bucket array and rehashes all existing elements.
     * Called when the load factor exceeds {@value #LOAD_FACTOR_THRESHOLD}.
     *
     * <p>Time: O(n + new capacity)  Space: O(n + new capacity)</p>
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<T>[] newBuckets = (Node<T>[]) new Node[newCapacity];

        for (Node<T> head : buckets) {
            for (Node<T> cur = head; cur != null; cur = cur.next) {
                // Recompute index in new table
                int h = cur.value.hashCode();
                h ^= (h >>> 16);
                int newIdx = h & (newCapacity - 1);
                // Prepend into new bucket
                newBuckets[newIdx] = new Node<>(cur.value, newBuckets[newIdx]);
            }
        }
        buckets = newBuckets;
    }

    /**
     * Returns the smallest power of two that is &gt;= {@code n}.
     */
    private static int nextPowerOfTwo(int n) {
        if (n <= 1) return 1;
        int p = 1;
        while (p < n) {
            p <<= 1;
        }
        return p;
    }
}
