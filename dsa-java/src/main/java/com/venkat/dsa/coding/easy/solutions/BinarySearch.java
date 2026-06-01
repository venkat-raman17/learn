package com.venkat.dsa.coding.easy.solutions;

/**
 * Binary Search (LeetCode #704)
 *
 * Approach: Classic iterative binary search on a sorted array. Maintain low and
 * high pointers, compute mid each iteration, and narrow the search window based
 * on whether target is less than, greater than, or equal to nums[mid].
 *
 * Key insight: Halving the search space each step guarantees O(log n) time
 * regardless of array size; integer overflow in mid is avoided by computing
 * lo + (hi - lo) / 2 instead of (lo + hi) / 2.
 *
 * Time complexity:  O(log n) — search space halves each iteration.
 * Space complexity: O(1) — only a constant number of pointers used.
 */
public class BinarySearch {

    public int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2; // avoids integer overflow

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                lo = mid + 1; // target is in the right half
            } else {
                hi = mid - 1; // target is in the left half
            }
        }

        return -1; // target not found
    }
}
