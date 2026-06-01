package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Maximum Subarray
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/maximum-subarray/
 *
 * <p>Given an integer array {@code nums}, find the contiguous subarray (containing at least one
 * number) which has the largest sum and return its sum.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10^5</li>
 *   <li>-10^4 &lt;= nums[i] &lt;= 10^4</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]  Output: 6  (subarray [4,-1,2,1])
 * Input: nums = [1]                        Output: 1
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space
 *
 * <p><b>Hint 1:</b> Track the running subarray sum; if it drops below 0, reset it to 0 (Kadane's algorithm).
 * <p><b>Hint 2:</b> Keep a global maximum updated at each step.
 */
public class MaximumSubarray {

    /**
     * Returns the sum of the contiguous subarray with the largest sum.
     *
     * @param nums input array of integers
     * @return maximum subarray sum
     */
    public int maxSubArray(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
