package com.venkat.dsa.coding.medium.solutions;

/**
 * Search in Rotated Sorted Array (LeetCode #33)
 *
 * Approach: Standard binary search modified to account for the rotation.
 * Each iteration one of the two halves [lo..mid] or [mid+1..hi] is always
 * fully sorted. Determine which half is sorted, then check whether the target
 * lies within that sorted half; if yes, narrow to that half, otherwise to the
 * other half.
 *
 * Key insight: At least one of the two halves produced by any mid is
 * guaranteed to be sorted (no duplicates). That sorted half can be range-
 * checked in O(1), steering the search correctly with a single binary search.
 *
 * Time complexity:  O(log n) — standard binary search cost.
 * Space complexity: O(1) — only pointer variables.
 */
public class SearchInRotatedSortedArray {

    public int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (nums[mid] == target) return mid;

            // Determine which half is sorted
            if (nums[lo] <= nums[mid]) {
                // Left half [lo..mid] is sorted
                if (nums[lo] <= target && target < nums[mid]) {
                    hi = mid - 1; // target is in the sorted left half
                } else {
                    lo = mid + 1; // target is in the right half
                }
            } else {
                // Right half [mid..hi] is sorted
                if (nums[mid] < target && target <= nums[hi]) {
                    lo = mid + 1; // target is in the sorted right half
                } else {
                    hi = mid - 1; // target is in the left half
                }
            }
        }

        return -1; // target not found
    }
}
