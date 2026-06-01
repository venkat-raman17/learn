package com.venkat.dsa.algorithms.searching;

/**
 * Binary Search — a family of divide-and-conquer search algorithms on sorted arrays.
 *
 * <p><b>Backing representation:</b> sorted {@code int[]} passed by the caller; no internal
 * state is stored (all methods are static).
 *
 * <p><b>Invariant:</b> every method that accepts {@code arr} requires the array to be sorted
 * in non-decreasing order, except {@link #searchRotated(int[], int)} which requires a
 * once-rotated sorted array (a sorted array whose prefix has been moved to the end).
 *
 * <h2>Operations</h2>
 * <pre>
 * +-----------------+------------+--------+
 * | Operation       | Time       | Space  |
 * +-----------------+------------+--------+
 * | search          | O(log n)   | O(1)   |
 * | lowerBound      | O(log n)   | O(1)   |
 * | upperBound      | O(log n)   | O(1)   |
 * | searchRotated   | O(log n)   | O(1)   |
 * +-----------------+------------+--------+
 * </pre>
 *
 * <p><b>When to use:</b> use binary search whenever random-access into a sorted collection is
 * needed and O(log n) lookup is acceptable; ideal for large, rarely-modified datasets where
 * linear scan would be too slow.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Always use {@code mid = lo + (hi - lo) / 2} to avoid integer overflow.</li>
 *   <li>{@code lowerBound} and {@code upperBound} are the idiomatic C++ {@code lower_bound} /
 *       {@code upper_bound} equivalents; together they give a range {@code [lb, ub)} for all
 *       occurrences of a value.</li>
 *   <li>For rotated arrays the key insight is that at least one half around {@code mid} is
 *       always sorted; binary decisions flow from that observation.</li>
 *   <li>Off-by-one errors are the #1 bug — be explicit about whether loop bounds are inclusive
 *       or exclusive.</li>
 * </ul>
 */
public final class BinarySearch {

    private BinarySearch() {
        // utility class — no instances
    }

    /**
     * Returns the index of {@code target} in the sorted array {@code arr}, or {@code -1} if
     * not present. When duplicates exist, any matching index may be returned.
     *
     * <p><b>Time:</b> O(log n) &nbsp;|&nbsp; <b>Space:</b> O(1)
     *
     * @param arr    sorted (non-decreasing) array; must not be {@code null}
     * @param target value to find
     * @return index of {@code target} in {@code arr}, or {@code -1}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static int search(int[] arr, int target) {
        if (arr == null) throw new NullPointerException("arr must not be null");
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) lo = mid + 1;
            else                   hi = mid - 1;
        }
        return -1;
    }

    /**
     * Returns the index of the first element that is <em>greater than or equal to</em>
     * {@code target} (i.e., the insertion point that keeps the array sorted).
     * Returns {@code arr.length} when all elements are less than {@code target}.
     *
     * <p><b>Time:</b> O(log n) &nbsp;|&nbsp; <b>Space:</b> O(1)
     *
     * @param arr    sorted (non-decreasing) array; must not be {@code null}
     * @param target lower threshold (inclusive)
     * @return first index {@code i} such that {@code arr[i] >= target}, or {@code arr.length}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static int lowerBound(int[] arr, int target) {
        if (arr == null) throw new NullPointerException("arr must not be null");
        int lo = 0, hi = arr.length; // hi is exclusive
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < target) lo = mid + 1;
            else                   hi = mid;
        }
        return lo;
    }

    /**
     * Returns the index of the first element that is <em>strictly greater than</em>
     * {@code target}.
     * Returns {@code arr.length} when all elements are less than or equal to {@code target}.
     *
     * <p><b>Time:</b> O(log n) &nbsp;|&nbsp; <b>Space:</b> O(1)
     *
     * @param arr    sorted (non-decreasing) array; must not be {@code null}
     * @param target upper threshold (exclusive)
     * @return first index {@code i} such that {@code arr[i] > target}, or {@code arr.length}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static int upperBound(int[] arr, int target) {
        if (arr == null) throw new NullPointerException("arr must not be null");
        int lo = 0, hi = arr.length; // hi is exclusive
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= target) lo = mid + 1;
            else                    hi = mid;
        }
        return lo;
    }

    /**
     * Searches for {@code target} in a <em>rotated</em> sorted array — a sorted array that has
     * been right-rotated by an unknown pivot (e.g., {@code [4,5,6,7,0,1,2]} is
     * {@code [0,1,2,4,5,6,7]} rotated at index 4). All values are assumed distinct.
     *
     * <p><b>Time:</b> O(log n) &nbsp;|&nbsp; <b>Space:</b> O(1)
     *
     * @param arr    once-rotated sorted array with distinct elements; must not be {@code null}
     * @param target value to find
     * @return index of {@code target} in {@code arr}, or {@code -1}
     * @throws NullPointerException if {@code arr} is {@code null}
     */
    public static int searchRotated(int[] arr, int target) {
        if (arr == null) throw new NullPointerException("arr must not be null");
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] == target) return mid;
            // Determine which half is sorted
            if (arr[lo] <= arr[mid]) {
                // Left half [lo..mid] is sorted
                if (arr[lo] <= target && target < arr[mid]) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            } else {
                // Right half [mid..hi] is sorted
                if (arr[mid] < target && target <= arr[hi]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
        }
        return -1;
    }
}
