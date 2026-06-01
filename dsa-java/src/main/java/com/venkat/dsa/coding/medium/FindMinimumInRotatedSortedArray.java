package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Find Minimum in Rotated Sorted Array
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Binary Search
 * <p>URL: https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
 *
 * <p>Given a sorted array of unique integers that has been rotated between 1 and n times,
 * find and return the minimum element. You must write an algorithm that runs in O(log n) time.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == nums.length</li>
 *   <li>1 &lt;= n &lt;= 5000</li>
 *   <li>-5000 &lt;= nums[i] &lt;= 5000</li>
 *   <li>All integers in {@code nums} are unique.</li>
 * </ul>
 *
 * <p>Example 1: nums = [3,4,5,1,2] → 1
 * <p>Example 2: nums = [4,5,6,7,0,1,2] → 0
 *
 * <p>Target: O(log n) time, O(1) space.
 *
 * <p>Hint 1: Compare nums[mid] to nums[hi]; if nums[mid] &gt; nums[hi] the minimum is in the right half.
 * <p>Hint 2: If nums[lo] &lt;= nums[hi] the current window is already sorted — nums[lo] is the minimum.
 */
public class FindMinimumInRotatedSortedArray {

    public int findMin(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
