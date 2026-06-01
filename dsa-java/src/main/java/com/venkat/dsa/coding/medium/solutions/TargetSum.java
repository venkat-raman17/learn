package com.venkat.dsa.coding.medium.solutions;

/**
 * Target Sum (LeetCode 494)
 *
 * Approach: 2-D DP (subset sum variant). dp[i][sum+offset] = number of ways to assign
 * +/- to nums[0..i-1] to reach the given running sum. We use an offset equal to the
 * total sum so the index is always non-negative. For each number we branch: add (+) or
 * subtract (-).
 *
 * Alternatively viewed as a knapsack: partition nums into two subsets P (positive) and
 * N (negative) such that P - N = target. Combined with P + N = totalSum, we get
 * P = (target + totalSum) / 2 — count subsets summing to P (only if divisible).
 *
 * We use the direct DP here for clarity.
 *
 * Time:  O(n * totalSum)
 * Space: O(totalSum)  — single rolling array
 *
 * Key insight: reframe as counting subsets that sum to (target + total) / 2,
 * reducing to an unbounded 0/1 knapsack count problem.
 */
public class TargetSum {

    public int findTargetSumWays(int[] nums, int target) {
        int total = 0;
        for (int n : nums) total += n;

        // Quick feasibility check
        if (Math.abs(target) > total || (target + total) % 2 != 0) return 0;

        int subsetSum = (target + total) / 2; // count subsets summing to subsetSum
        // dp[s] = number of subsets that sum to exactly s
        int[] dp = new int[subsetSum + 1];
        dp[0] = 1; // empty subset sums to 0

        for (int num : nums) {
            // Traverse right-to-left to prevent using the same element twice (0/1 knapsack)
            for (int s = subsetSum; s >= num; s--) {
                dp[s] += dp[s - num];
            }
        }
        return dp[subsetSum];
    }
}
