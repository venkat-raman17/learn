package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — House Robber
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/house-robber/
 *
 * <p>Given an integer array {@code nums} representing the amount of money in each house, return
 * the maximum amount you can rob without robbing two adjacent houses (which would trigger the alarm).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 100</li>
 *   <li>0 &lt;= nums[i] &lt;= 400</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: nums = [1,2,3,1]  -> Output: 4  (rob house 0 and 2: 1+3=4)
 * Input: nums = [2,7,9,3,1]  -> Output: 12  (rob house 0, 2, 4: 2+9+1=12)
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: At each house you choose: rob it (add to prev-prev) or skip it (keep prev). Take the max.
 * <p>Hint 2: Maintain two variables — the best result up to two houses ago and one house ago.
 */
public class HouseRobber {

    public int rob(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
