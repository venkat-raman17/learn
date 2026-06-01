package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Search in Rotated Sorted Array
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Binary Search
 * <p>URL: https://leetcode.com/problems/search-in-rotated-sorted-array/
 *
 * <p>Given an integer array {@code nums} sorted in ascending order and possibly rotated at an
 * unknown pivot, and an integer {@code target}, return the index of {@code target} if it is in
 * {@code nums}, or {@code -1} if it is not. You must write an algorithm with O(log n) runtime.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 5000</li>
 *   <li>-10^4 &lt;= nums[i], target &lt;= 10^4</li>
 *   <li>All values in {@code nums} are unique.</li>
 * </ul>
 *
 * <p>Example 1: nums = [4,5,6,7,0,1,2], target = 0 → 4
 * <p>Example 2: nums = [4,5,6,7,0,1,2], target = 3 → -1
 *
 * <p>Target: O(log n) time, O(1) space.
 *
 * <p>Hint 1: At each mid, determine which half is sorted by comparing nums[lo] to nums[mid].
 * <p>Hint 2: Once you know which half is sorted, check if target falls within it; if so, search that half, otherwise search the other.
 */
public class SearchInRotatedSortedArray {

    public int search(int[] nums, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
