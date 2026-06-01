package com.venkat.dsa.trees;

import java.util.Arrays;

/**
 * Fenwick Tree (Binary Indexed Tree / BIT) for efficient prefix-sum queries and point updates.
 *
 * <p><b>Definition:</b> A Fenwick Tree is an array-based data structure that supports prefix-sum
 * queries and point updates in O(log n) time, using a compact 1-based internal array representation
 * that leverages the lowest-set-bit (LSB) trick for index traversal.
 *
 * <p><b>Backing Representation:</b> A 1-indexed {@code long[]} of length {@code n+1}, where
 * {@code tree[i]} stores the sum of a specific range of elements determined by the LSB of {@code i}.
 * Index 0 is unused; public API is 0-based and is translated to 1-based internally.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code tree[i]} holds the sum of elements from index {@code i - lowBit(i) + 1} to {@code i}
 *       (1-based), where {@code lowBit(i) = i & (-i)}.</li>
 *   <li>The logical size {@code n} is fixed at construction; indices must satisfy
 *       {@code 0 <= index < n} for all public methods.</li>
 * </ul>
 *
 * <p><b>Operations Table:</b>
 * <pre>
 * Operation              Time         Space (extra)
 * ─────────────────────────────────────────────────
 * Construction (size)    O(n)         O(n)
 * Construction (array)   O(n log n)   O(n)
 * update(index, delta)   O(log n)     O(1)
 * prefixSum(index)       O(log n)     O(1)
 * rangeSum(l, r)         O(log n)     O(1)
 * </pre>
 *
 * <p><b>When to use:</b> Use a Fenwick Tree when you need repeated prefix-sum queries with
 * intermixed point updates on a mutable array, and you want lower constant factors and simpler
 * implementation compared to a Segment Tree. Prefer a Segment Tree for range-update / range-query
 * or non-invertible functions (e.g., range-max without lazy propagation).
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Key insight: {@code i & (-i)} isolates the lowest set bit, driving both parent and child
 *       traversal in O(log n) steps.</li>
 *   <li>Space is O(n) — no pointers, no nodes, just an array.</li>
 *   <li>Supports any group-invertible aggregation (sum, XOR); not suitable for max/min without
 *       additional tricks.</li>
 *   <li>Classic interview problems: "Count of smaller numbers after self", "Number of Range Sums",
 *       online 2-D grid sum queries.</li>
 * </ul>
 */
public class FenwickTree {

    private final long[] tree;
    private final int n;

    /**
     * Constructs a Fenwick Tree of the given size, with all values initialised to zero.
     *
     * <p>Time: O(n) — fills the backing array with zeros.
     *
     * @param size the number of logical elements ({@code size >= 1})
     * @throws IllegalArgumentException if {@code size < 1}
     */
    public FenwickTree(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be >= 1, got: " + size);
        }
        this.n = size;
        this.tree = new long[n + 1]; // index 0 unused
    }

    /**
     * Constructs a Fenwick Tree pre-populated with the values in {@code values}.
     * The 0-based array is loaded element by element via {@link #update(int, long)}.
     *
     * <p>Time: O(n log n) — calls {@code update} for each element.
     *
     * @param values source array; must be non-null and non-empty
     * @throws IllegalArgumentException if {@code values} is null or empty
     */
    public FenwickTree(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values array must be non-null and non-empty");
        }
        this.n = values.length;
        this.tree = new long[n + 1];
        for (int i = 0; i < n; i++) {
            update(i, values[i]);
        }
    }

    /**
     * Adds {@code delta} to the element at 0-based {@code index}.
     *
     * <p>Translates to 1-based internally, then propagates the update upward by repeatedly
     * adding {@code lowBit(i)} to {@code i}.
     *
     * <p>Time: O(log n).
     *
     * @param index 0-based position in {@code [0, n)}
     * @param delta value to add (may be negative, enabling logical "set" via prior subtraction)
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     */
    public void update(int index, long delta) {
        checkIndex(index);
        for (int i = index + 1; i <= n; i += i & (-i)) {
            tree[i] += delta;
        }
    }

    /**
     * Returns the prefix sum of elements {@code [0, index]} (inclusive) in O(log n).
     *
     * <p>Translates to 1-based internally, then accumulates sums downward by repeatedly
     * subtracting {@code lowBit(i)} from {@code i}.
     *
     * <p>Time: O(log n).
     *
     * @param index 0-based upper bound (inclusive), must be in {@code [0, n)}
     * @return sum of elements at positions {@code 0..index}
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     */
    public long prefixSum(int index) {
        checkIndex(index);
        long sum = 0;
        for (int i = index + 1; i > 0; i -= i & (-i)) {
            sum += tree[i];
        }
        return sum;
    }

    /**
     * Returns the sum of elements in the closed 0-based range {@code [l, r]}.
     *
     * <p>Computed as {@code prefixSum(r) - prefixSum(l - 1)}, or just {@code prefixSum(r)}
     * when {@code l == 0}.
     *
     * <p>Time: O(log n).
     *
     * @param l 0-based left bound (inclusive)
     * @param r 0-based right bound (inclusive)
     * @return sum of elements at positions {@code l..r}
     * @throws IndexOutOfBoundsException if {@code l} or {@code r} are out of {@code [0, n)}
     * @throws IllegalArgumentException  if {@code l > r}
     */
    public long rangeSum(int l, int r) {
        checkIndex(l);
        checkIndex(r);
        if (l > r) {
            throw new IllegalArgumentException(
                    "Left bound (" + l + ") must be <= right bound (" + r + ")");
        }
        if (l == 0) {
            return prefixSum(r);
        }
        return prefixSum(r) - prefixSum(l - 1);
    }

    /**
     * Returns the logical size of this Fenwick Tree (number of elements).
     *
     * <p>Time: O(1).
     *
     * @return the number of elements
     */
    public int size() {
        return n;
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private void checkIndex(int index) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + n);
        }
    }
}
