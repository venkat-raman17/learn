package com.venkat.dsa.algorithms.sorting;

import java.util.Arrays;

/**
 * Counting Sort — a non-comparison, integer sorting algorithm.
 *
 * <p><b>Backing representation:</b> A frequency/count array of size
 * {@code (max - min + 1)} is built over the input values; prefix sums
 * convert counts to starting positions, and a second pass fills an
 * output array in stable order.</p>
 *
 * <p><b>Invariant:</b> After {@link #sort(int[])}, {@code output[i] <= output[i+1]}
 * for all valid {@code i}, and every value present in the input appears the
 * same number of times in the output (multiset equality).</p>
 *
 * <h2>Operations</h2>
 * <pre>
 * ┌──────────────────┬──────────────┬───────────────┐
 * │ Operation        │ Time         │ Space         │
 * ├──────────────────┼──────────────┼───────────────┤
 * │ sort(int[])      │ O(n + k)     │ O(n + k)      │
 * └──────────────────┴──────────────┴───────────────┘
 * n = number of elements, k = range (max - min + 1)
 * </pre>
 *
 * <p><b>When to use:</b> When the input is a large array of integers with a
 * known, bounded range (k) that is not astronomically larger than n.
 * Ideal for sorting ages, scores, character codes, or any dense integer
 * domain. Prefer comparison-based sorts (e.g. Timsort) when k >> n or
 * the range is unknown.</p>
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Counting Sort is <em>stable</em>: equal elements retain their
 *       original relative order (achieved by traversing the input
 *       right-to-left during output placement).</li>
 *   <li>It is <em>not</em> in-place — O(n + k) auxiliary space is required.</li>
 *   <li>Negative values are handled via an offset equal to {@code min},
 *       mapping every value into the non-negative index space
 *       {@code [0, k-1]}.</li>
 *   <li>Time complexity beats the O(n log n) lower bound for
 *       comparison-based algorithms because it exploits the integer
 *       structure of keys.</li>
 * </ul>
 * </p>
 */
public final class CountingSort {

    /** Utility class — no instances. */
    private CountingSort() {}

    /**
     * Sorts an array of integers in ascending order using Counting Sort.
     *
     * <p>Negative values are fully supported: the minimum element is used
     * as an offset so that all keys map to non-negative indices in the
     * count array.  The sort is <em>stable</em>.</p>
     *
     * <p><b>Time:</b> O(n + k) where k = max − min + 1.<br>
     * <b>Space:</b> O(n + k) auxiliary.</p>
     *
     * @param arr the array to sort; may contain negative integers.
     *            {@code null} throws {@link IllegalArgumentException}.
     *            An empty or single-element array is returned as-is (a
     *            fresh copy).
     * @return a new int[] containing the elements of {@code arr} sorted
     *         in non-decreasing order.
     * @throws IllegalArgumentException if {@code arr} is {@code null}.
     */
    public static int[] sort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Input array must not be null.");
        }

        int n = arr.length;
        if (n <= 1) {
            return Arrays.copyOf(arr, n);
        }

        // --- Step 1: find range ---
        int min = arr[0];
        int max = arr[0];
        for (int v : arr) {
            if (v < min) min = v;
            if (v > max) max = v;
        }

        int range = max - min + 1;   // k

        // --- Step 2: build frequency array ---
        int[] count = new int[range];
        for (int v : arr) {
            count[v - min]++;
        }

        // --- Step 3: prefix-sum (cumulative counts) ---
        // count[i] now holds the number of elements <= (i + min).
        // After this step count[i] is the exclusive upper bound of the
        // position range for key (i + min), i.e. the *next* available
        // write index for that key.
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        // --- Step 4: build output array (right-to-left for stability) ---
        int[] output = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int idx = arr[i] - min;
            output[--count[idx]] = arr[i];
        }

        return output;
    }
}
