package com.venkat.dsa.algorithms.sorting;

import java.util.Random;

/**
 * QuickSort — a divide-and-conquer, comparison-based, in-place sorting algorithm.
 *
 * <p><b>Backing representation:</b> Operates directly on a primitive {@code int[]} array;
 * no auxiliary array is allocated beyond the call stack.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>After partitioning, every element to the left of the pivot is &le; pivot and
 *       every element to the right is &ge; pivot (for Lomuto) or the partition index
 *       satisfies the same range property (for Hoare).</li>
 *   <li>The public {@code sort} method leaves the input array fully sorted in
 *       non-decreasing order.</li>
 * </ul>
 *
 * <p><b>Operations table:</b>
 * <pre>
 * Operation          | Time (avg) | Time (worst) | Space (aux)
 * -------------------|------------|--------------|------------
 * sort(int[])        | O(n log n) | O(n²)        | O(log n) stack
 * lomutoPartition    | O(n)       | O(n)         | O(1)
 * hoarePartition     | O(n)       | O(n)         | O(1)
 * </pre>
 *
 * <p><b>When to use:</b> QuickSort is the practical default for in-place, cache-friendly
 * sorting of mutable arrays when average-case performance matters more than
 * worst-case guarantees. Prefer MergeSort when stability is required, or TimSort
 * (Java's {@code Arrays.sort} for objects) in production code.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Average O(n log n), worst O(n²) — the worst case is avoided with randomized or
 *       median-of-three pivot selection.</li>
 *   <li>Not stable — equal elements may change relative order.</li>
 *   <li>Typically faster in practice than MergeSort due to better cache locality and
 *       lower constant factors.</li>
 *   <li>Two classic partition schemes: Lomuto (simpler, slightly more swaps) and
 *       Hoare (fewer swaps, original scheme).</li>
 *   <li>The public {@code sort} uses <b>Lomuto partition with a randomized pivot</b>
 *       to guard against adversarial or already-sorted input.</li>
 * </ul>
 */
public final class QuickSort {

    private static final Random RANDOM = new Random();

    /** Utility class — no instantiation. */
    private QuickSort() {}

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Sorts {@code arr} in-place in non-decreasing order using QuickSort with
     * <b>Lomuto partition and a randomized pivot</b>.
     *
     * <p>Time: O(n log n) average, O(n²) worst (extremely unlikely with random pivot).
     * Space: O(log n) call-stack depth on average.
     *
     * @param arr the array to sort; must not be {@code null}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static void sort(int[] arr) {
        if (arr == null) {
            throw new NullPointerException("Input array must not be null");
        }
        sortLomuto(arr, 0, arr.length - 1);
    }

    /**
     * Sorts {@code arr} in-place using QuickSort with the <b>Lomuto partition scheme</b>
     * and a randomized pivot. Delegates to the private recursive helper.
     *
     * <p>Time: O(n log n) average. Space: O(log n) stack.
     *
     * @param arr the array to sort; must not be {@code null}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static void sortWithLomuto(int[] arr) {
        if (arr == null) {
            throw new NullPointerException("Input array must not be null");
        }
        sortLomuto(arr, 0, arr.length - 1);
    }

    /**
     * Sorts {@code arr} in-place using QuickSort with the <b>Hoare partition scheme</b>
     * and a randomized pivot. Delegates to the private recursive helper.
     *
     * <p>Time: O(n log n) average. Space: O(log n) stack.
     *
     * @param arr the array to sort; must not be {@code null}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static void sortWithHoare(int[] arr) {
        if (arr == null) {
            throw new NullPointerException("Input array must not be null");
        }
        sortHoare(arr, 0, arr.length - 1);
    }

    // -------------------------------------------------------------------------
    // Lomuto partition — private recursive implementation
    // -------------------------------------------------------------------------

    /**
     * Recursive driver for Lomuto-based QuickSort on {@code arr[lo..hi]} inclusive.
     */
    private static void sortLomuto(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int pivotIdx = lomutoPartition(arr, lo, hi);
        sortLomuto(arr, lo, pivotIdx - 1);
        sortLomuto(arr, pivotIdx + 1, hi);
    }

    /**
     * Lomuto partition: picks a random pivot, moves it to {@code arr[hi]}, then
     * rearranges {@code arr[lo..hi]} so that elements &le; pivot come before it
     * and elements &gt; pivot come after it. Returns the final pivot index.
     *
     * <p>All elements with index &lt; returned value are &le; pivot;
     * all with index &gt; returned value are &gt; pivot.
     */
    private static int lomutoPartition(int[] arr, int lo, int hi) {
        // Randomized pivot: swap a random element into the last position
        int randIdx = lo + RANDOM.nextInt(hi - lo + 1);
        swap(arr, randIdx, hi);

        int pivot = arr[hi];
        int i = lo - 1; // index of the last element confirmed <= pivot

        for (int j = lo; j < hi; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, hi); // place pivot in its correct position
        return i + 1;
    }

    // -------------------------------------------------------------------------
    // Hoare partition — private recursive implementation
    // -------------------------------------------------------------------------

    /**
     * Recursive driver for Hoare-based QuickSort on {@code arr[lo..hi]} inclusive.
     */
    private static void sortHoare(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int p = hoarePartition(arr, lo, hi);
        // Hoare returns the index of the RIGHT boundary of the left partition;
        // both sub-arrays must INCLUDE p, hence p (not p-1) for left call.
        sortHoare(arr, lo, p);
        sortHoare(arr, p + 1, hi);
    }

    /**
     * Hoare partition: uses two converging indices. Picks a random pivot (swapped to
     * {@code arr[lo]} for simplicity), then returns an index {@code p} such that
     * every element in {@code arr[lo..p]} is &le; pivot and every element in
     * {@code arr[p+1..hi]} is &ge; pivot.
     *
     * <p>Note: the pivot is NOT necessarily at index {@code p} after this call —
     * that is the key difference from Lomuto.
     */
    private static int hoarePartition(int[] arr, int lo, int hi) {
        // Randomized pivot: swap a random element to lo
        int randIdx = lo + RANDOM.nextInt(hi - lo + 1);
        swap(arr, randIdx, lo);

        int pivot = arr[lo];
        int i = lo - 1;
        int j = hi + 1;

        while (true) {
            do { i++; } while (arr[i] < pivot);
            do { j--; } while (arr[j] > pivot);
            if (i >= j) return j;
            swap(arr, i, j);
        }
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }
}
