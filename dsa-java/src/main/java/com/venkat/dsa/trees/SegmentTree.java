package com.venkat.dsa.trees;

import java.util.Arrays;

/**
 * Segment Tree over an {@code int[]} supporting range sum queries and point updates.
 *
 * <p><b>Definition:</b> A binary tree where each node stores the aggregate (here: sum)
 * of a contiguous subarray of the original array.
 *
 * <p><b>Backing representation:</b> A 1-indexed {@code int[]} of size {@code 4 * n},
 * stored in a flat array where the left child of node {@code i} is {@code 2*i} and the
 * right child is {@code 2*i+1}. The root is at index 1.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>Every internal node holds the sum of its two children.</li>
 *   <li>Every leaf node holds the value of the corresponding element in the original array.</li>
 *   <li>The underlying array length {@code n} is fixed at construction time.</li>
 * </ul>
 *
 * <p><b>Operations:</b>
 * <table border="1">
 *   <tr><th>Operation</th><th>Time</th><th>Space (extra)</th></tr>
 *   <tr><td>Build (constructor)</td><td>O(n)</td><td>O(n)</td></tr>
 *   <tr><td>query(l, r)</td><td>O(log n)</td><td>O(log n) stack</td></tr>
 *   <tr><td>update(index, value)</td><td>O(log n)</td><td>O(log n) stack</td></tr>
 * </table>
 *
 * <p><b>When to use:</b> Use a segment tree when you need repeated range queries
 * (sum, min, max, GCD) and point or range updates on a mutable array. It is
 * superior to a prefix-sum array when updates must be interleaved with queries.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Build is O(n), not O(n log n), because each node is visited exactly once.</li>
 *   <li>The flat-array representation with 4*n nodes avoids pointer overhead and
 *       guarantees no out-of-bounds even for non-power-of-two sizes.</li>
 *   <li>A Fenwick (BIT) tree uses O(n) space and O(log n) per op but supports only
 *       prefix queries, while a segment tree supports arbitrary range queries.</li>
 *   <li>Lazy propagation extends the tree to O(log n) range updates.</li>
 * </ul>
 */
public class SegmentTree {

    private final int[] tree;
    private final int n;

    /**
     * Builds a segment tree from the given array.
     *
     * <p>Time: O(n) — each of the ~2n nodes is filled exactly once.
     * Space: O(n) — internal tree array of size 4*n.
     *
     * @param arr the source array; must not be {@code null}
     * @throws IllegalArgumentException if {@code arr} is empty
     */
    public SegmentTree(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Input array must not be null");
        }
        if (arr.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty");
        }
        this.n = arr.length;
        // 4*n is a safe upper bound for any n
        this.tree = new int[4 * n];
        build(arr, 1, 0, n - 1);
    }

    /**
     * Returns the sum of elements in the inclusive range [l, r] of the original array.
     *
     * <p>Time: O(log n).
     *
     * @param l left boundary (0-based, inclusive)
     * @param r right boundary (0-based, inclusive)
     * @return the range sum
     * @throws IllegalArgumentException if indices are out of bounds or l &gt; r
     */
    public int query(int l, int r) {
        if (l < 0 || r >= n || l > r) {
            throw new IllegalArgumentException(
                    "Invalid range [" + l + ", " + r + "] for array of length " + n);
        }
        return queryInternal(1, 0, n - 1, l, r);
    }

    /**
     * Updates the element at {@code index} to {@code value} and adjusts all
     * ancestor sums accordingly.
     *
     * <p>Time: O(log n).
     *
     * @param index 0-based position to update
     * @param value new value to assign
     * @throws IllegalArgumentException if {@code index} is out of bounds
     */
    public void update(int index, int value) {
        if (index < 0 || index >= n) {
            throw new IllegalArgumentException(
                    "Index " + index + " out of bounds for array of length " + n);
        }
        updateInternal(1, 0, n - 1, index, value);
    }

    /**
     * Returns the number of elements in the original array.
     *
     * <p>Time: O(1).
     *
     * @return size of the original array
     */
    public int size() {
        return n;
    }

    // -------------------------------------------------------------------------
    // Private recursive helpers
    // -------------------------------------------------------------------------

    /**
     * Recursively builds the tree. Node {@code node} covers the range
     * [{@code start}, {@code end}] of the source array.
     */
    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, 2 * node, start, mid);
            build(arr, 2 * node + 1, mid + 1, end);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }

    /**
     * Recursively queries the sum over [{@code l}, {@code r}].
     * Node {@code node} is responsible for the segment [{@code start}, {@code end}].
     */
    private int queryInternal(int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            // Completely outside the query range
            return 0;
        }
        if (l <= start && end <= r) {
            // Completely inside the query range
            return tree[node];
        }
        int mid = (start + end) / 2;
        int leftSum  = queryInternal(2 * node,     start,   mid, l, r);
        int rightSum = queryInternal(2 * node + 1, mid + 1, end, l, r);
        return leftSum + rightSum;
    }

    /**
     * Recursively updates the value at {@code idx} and propagates sums upward.
     * Node {@code node} covers [{@code start}, {@code end}].
     */
    private void updateInternal(int node, int start, int end, int idx, int value) {
        if (start == end) {
            tree[node] = value;
        } else {
            int mid = (start + end) / 2;
            if (idx <= mid) {
                updateInternal(2 * node,     start,   mid, idx, value);
            } else {
                updateInternal(2 * node + 1, mid + 1, end, idx, value);
            }
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }
}
