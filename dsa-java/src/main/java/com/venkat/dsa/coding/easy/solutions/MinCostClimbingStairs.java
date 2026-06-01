package com.venkat.dsa.coding.easy.solutions;

/**
 * Min Cost Climbing Stairs (LeetCode 746)
 *
 * Approach: Bottom-up DP where dp[i] is the minimum cost to reach step i.
 * From step i you can move to i+1 or i+2, so dp[i] = cost[i] + min(dp[i-1], dp[i-2]).
 * The answer is min(dp[n-1], dp[n-2]) because you can start from index 0 or 1
 * and finish by stepping one or two beyond the last index.
 *
 * Key insight: Reuse the cost array in-place (or two variables) to avoid extra space.
 *
 * Time:  O(n)
 * Space: O(1)
 */
public class MinCostClimbingStairs {

    public int minCostClimbingStairs(int[] cost) {
        int n = cost.length;

        // dp[i] = minimum total cost to step off from stair i
        // We compute this in-place starting from index 2
        for (int i = 2; i < n; i++) {
            cost[i] += Math.min(cost[i - 1], cost[i - 2]);
        }

        // reach the top (beyond index n-1) from either of the last two stairs
        return Math.min(cost[n - 1], cost[n - 2]);
    }
}
