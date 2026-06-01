package com.venkat.dsa.algorithms.sorting;

/**
 * Heap Sort — comparison-based sorting algorithm that uses a binary max-heap.
 *
 * <p><b>Backing representation:</b> The input array is sorted in-place; no auxiliary
 * storage beyond a few local variables is required. A virtual max-heap is maintained
 * within the array itself (index arithmetic, not pointer links).
 *
 * <p><b>Invariant:</b> After the build-heap phase, {@code arr[0]} is always the largest
 * element in the current heap region. During extraction, the heap region shrinks from
 * the right while the sorted region grows from the right, so the subarray to the right
 * of the current heap boundary is always fully sorted.
 *
 * <p><b>Operations table:</b>
 * <pre>
 * Operation        | Time (best) | Time (avg)  | Time (worst) | Space
 * -----------------|-------------|-------------|--------------|-------
 * sort(int[])      | O(n log n)  | O(n log n)  | O(n log n)   | O(1)
 * buildMaxHeap     | O(n)        | O(n)        | O(n)         | O(1)
 * heapify (sift)   | O(log n)    | O(log n)    | O(log n)     | O(1)
 * </pre>
 *
 * <p><b>When to use:</b> Prefer Heap Sort when you need a guaranteed O(n log n) worst-case
 * with O(1) extra space and an unstable sort is acceptable. It is rarely the fastest in
 * practice due to cache-unfriendly access patterns, but it shines in embedded or
 * memory-constrained environments.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Not stable — equal elements may be reordered.</li>
 *   <li>In-place — O(1) auxiliary space (iterative heapify; no recursion stack needed
 *       if heapify is written iteratively).</li>
 *   <li>The build-heap step is O(n) because lower levels of the heap do less work;
 *       the total cost telescopes to O(n).</li>
 *   <li>Extraction phase is O(n log n): n extractions each costing O(log n).</li>
 *   <li>Unlike Quick Sort, the worst case is still O(n log n).</li>
 * </ul>
 */
public final class HeapSort {

    /** Prevent instantiation — all methods are static. */
    private HeapSort() {}

    /**
     * Sorts the given array in ascending order in-place using Heap Sort.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Build a max-heap from the entire array in O(n) time.</li>
     *   <li>Repeatedly swap the root (maximum) with the last element of the heap,
     *       shrink the heap by one, and restore the heap property — O(n log n) total.</li>
     * </ol>
     *
     * <p><b>Time complexity:</b> O(n log n) best / average / worst.<br>
     * <b>Space complexity:</b> O(1) auxiliary.
     *
     * @param arr the array to sort; {@code null} is treated as a no-op, as is an
     *            array of length 0 or 1
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        int n = arr.length;

        // Phase 1 — Build max-heap (Floyd's algorithm, O(n)).
        // Start from the last non-leaf node and sift each down.
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Phase 2 — Extract elements from the heap one by one.
        for (int i = n - 1; i > 0; i--) {
            // Move current root (maximum) to the sorted region at index i.
            swap(arr, 0, i);
            // Restore max-heap property for the reduced heap [0 .. i-1].
            heapify(arr, i, 0);
        }
    }

    /**
     * Restores the max-heap property for the subtree rooted at index {@code root}
     * within the virtual heap of size {@code heapSize}.
     *
     * <p>Iterative to avoid recursion stack overhead.
     *
     * <p><b>Time complexity:</b> O(log heapSize).
     *
     * @param arr      the underlying array
     * @param heapSize the number of elements that form the current heap
     * @param root     index of the subtree root to sift down
     */
    private static void heapify(int[] arr, int heapSize, int root) {
        int current = root;

        while (true) {
            int largest = current;
            int left  = 2 * current + 1;
            int right = 2 * current + 2;

            if (left < heapSize && arr[left] > arr[largest]) {
                largest = left;
            }
            if (right < heapSize && arr[right] > arr[largest]) {
                largest = right;
            }

            if (largest == current) {
                break; // Heap property satisfied.
            }

            swap(arr, current, largest);
            current = largest;
        }
    }

    /**
     * Swaps the elements at indices {@code i} and {@code j} in {@code arr}.
     *
     * <p><b>Time complexity:</b> O(1).
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i]  = arr[j];
        arr[j]  = tmp;
    }
}
