package com.venkat.dsa.algorithms.sorting;

import java.util.Arrays;

/**
 * Merge Sort — a comparison-based, divide-and-conquer sorting algorithm.
 *
 * <p><b>Backing representation:</b> Operates on a plain {@code int[]} array.
 * A temporary auxiliary array of the same length is allocated once per sort
 * invocation and reused across all recursive calls to avoid repeated allocation.
 *
 * <p><b>Invariant:</b> After every merge step the sub-array {@code a[lo..hi]}
 * is sorted in non-decreasing order. The algorithm is stable: equal elements
 * preserve their original relative order.
 *
 * <h2>Operations</h2>
 * <pre>
 * Operation          Time (worst / avg / best)   Space
 * ─────────────────────────────────────────────────────
 * sort(int[])        O(n log n) / O(n log n) / O(n log n)   O(n) auxiliary
 * </pre>
 *
 * <p><b>When to use:</b> When stability is required and worst-case O(n log n)
 * is needed. Preferred over quicksort for linked lists or external sorting
 * (disk-based). For small arrays (≤ ~16 elements) insertion sort is faster in
 * practice; production implementations often use a hybrid (e.g., Timsort).
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Time complexity is O(n log n) in all cases — no worst-case degradation.</li>
 *   <li>Space complexity is O(n) due to the auxiliary array (not in-place).</li>
 *   <li>Stable sort: identical keys maintain original order.</li>
 *   <li>Top-down (recursive) approach shown here; bottom-up (iterative) is also common.</li>
 *   <li>Recurrence: T(n) = 2T(n/2) + O(n), solved by Master Theorem to O(n log n).</li>
 * </ul>
 */
public final class MergeSort {

    private MergeSort() {
        // utility class — no instances
    }

    /**
     * Sorts the given array in-place in non-decreasing order using top-down
     * stable merge sort.
     *
     * <p>Big-O: O(n log n) time, O(n) auxiliary space.
     *
     * @param a the array to sort; {@code null} is rejected
     * @throws IllegalArgumentException if {@code a} is {@code null}
     */
    public static void sort(int[] a) {
        if (a == null) {
            throw new IllegalArgumentException("Input array must not be null");
        }
        if (a.length < 2) {
            return; // zero or one element is already sorted
        }
        int[] aux = Arrays.copyOf(a, a.length);
        sortRange(a, aux, 0, a.length - 1);
    }

    /**
     * Recursively sorts {@code a[lo..hi]} (inclusive) using {@code aux} as
     * scratch space.
     *
     * <p>Big-O: O(n log n) time where n = hi - lo + 1.
     */
    private static void sortRange(int[] a, int[] aux, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sortRange(a, aux, lo, mid);
        sortRange(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    /**
     * Merges two adjacent sorted sub-arrays {@code a[lo..mid]} and
     * {@code a[mid+1..hi]} into a single sorted run, writing the result back
     * into {@code a[lo..hi]}.
     *
     * <p>Big-O: O(n) time and O(n) space where n = hi - lo + 1.
     */
    private static void merge(int[] a, int[] aux, int lo, int mid, int hi) {
        // Copy the relevant portion into aux before merging back into a.
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);

        int left = lo;        // pointer into aux left half
        int right = mid + 1;  // pointer into aux right half
        int dest = lo;         // write position in a

        while (left <= mid && right <= hi) {
            // Use <= for stability: take from left when equal.
            if (aux[left] <= aux[right]) {
                a[dest++] = aux[left++];
            } else {
                a[dest++] = aux[right++];
            }
        }

        // Copy any remaining elements from the left half.
        while (left <= mid) {
            a[dest++] = aux[left++];
        }

        // Remaining elements from the right half are already in place in a
        // (they were copied from a into aux and the original values still sit
        // at the correct positions when right half is exhausted first).
        while (right <= hi) {
            a[dest++] = aux[right++];
        }
    }
}
