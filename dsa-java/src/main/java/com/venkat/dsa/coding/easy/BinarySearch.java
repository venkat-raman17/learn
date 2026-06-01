package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Binary Search
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Binary Search
 * <p>URL: https://leetcode.com/problems/binary-search/
 *
 * <p>Given an array of integers {@code nums} sorted in ascending order, and an integer
 * {@code target}, return the index of {@code target} if it exists, or {@code -1} if it does not.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10^4</li>
 *   <li>-10^4 &lt;= nums[i], target &lt;= 10^4</li>
 *   <li>All integers in {@code nums} are unique.</li>
 *   <li>{@code nums} is sorted in ascending order.</li>
 * </ul>
 *
 * <p>Example 1: nums = [-1,0,3,5,9,12], target = 9 → 4
 * <p>Example 2: nums = [-1,0,3,5,9,12], target = 2 → -1
 *
 * <p>Target: O(log n) time, O(1) space.
 *
 * <p>Hint 1: Maintain lo and hi pointers; compare mid element to target to decide which half to search.
 * <p>Hint 2: Avoid overflow when computing mid: use lo + (hi - lo) / 2.
 */
public class BinarySearch {

    public int search(int[] nums, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
