package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — House Robber II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/house-robber-ii/
 *
 * <p>All houses are arranged in a circle (the first and last house are adjacent).
 * Return the maximum amount you can rob without robbing two adjacent houses.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 100</li>
 *   <li>0 &lt;= nums[i] &lt;= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: nums = [2,3,2]  -> Output: 3
 * Input: nums = [1,2,3,1]  -> Output: 4
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Because first and last are adjacent, run House Robber I twice: once on nums[0..n-2]
 *            and once on nums[1..n-1], then take the maximum.
 * <p>Hint 2: Extract a helper that runs the linear DP on any subarray to avoid duplicating code.
 */
public class HouseRobberII {

    public int rob(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
