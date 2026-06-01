package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Min Cost Climbing Stairs
 *
 * <p>Difficulty: EASY
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/min-cost-climbing-stairs/
 *
 * <p>Given an integer array {@code cost} where {@code cost[i]} is the cost of the i-th step,
 * return the minimum cost to reach the top (beyond the last index). You can start from index 0
 * or 1 and climb 1 or 2 steps at a time.
 *
 * <p>Constraints:
 * <ul>
 *   <li>2 &lt;= cost.length &lt;= 1000</li>
 *   <li>0 &lt;= cost[i] &lt;= 999</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: cost = [10,15,20]  -> Output: 15  (start at index 1, pay 15, jump to top)
 * Input: cost = [1,100,1,1,1,100,1,1,100,1]  -> Output: 6
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Define dp[i] as the minimum cost to reach step i; dp[i] = cost[i] + min(dp[i-1], dp[i-2]).
 * <p>Hint 2: You only need to track the previous two values, not the entire array.
 */
public class MinCostClimbingStairs {

    public int minCostClimbingStairs(int[] cost) {
        throw new UnsupportedOperationException("implement me");
    }
}
