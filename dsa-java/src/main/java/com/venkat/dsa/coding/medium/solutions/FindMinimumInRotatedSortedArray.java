package com.venkat.dsa.coding.medium.solutions;

/**
 * Find Minimum in Rotated Sorted Array (LeetCode #153)
 *
 * Approach: Binary search maintaining the invariant that the minimum is always
 * within [lo, hi]. If nums[mid] > nums[hi], the minimum is in the right half
 * (mid+1..hi); otherwise it is in the left half (lo..mid), because mid itself
 * could be the minimum.
 *
 * Key insight: Comparing nums[mid] to nums[hi] (not nums[lo]) cleanly identifies
 * which half is unsorted — the minimum always sits at the start of the rotated
 * (unsorted) half. No duplicates means strict comparison suffices.
 *
 * Time complexity:  O(log n) — binary search halves the range each iteration.
 * Space complexity: O(1) — only pointer variables.
 */
public class FindMinimumInRotatedSortedArray {

    public int findMin(int[] nums) {
        int lo = 0, hi = nums.length - 1;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (nums[mid] > nums[hi]) {
                // Mid is in the larger left portion; minimum is to the right
                lo = mid + 1;
            } else {
                // Mid is in the smaller right portion (or mid IS the minimum)
                hi = mid;
            }
        }

        return nums[lo]; // lo == hi: the minimum element
    }
}
