package com.venkat.dsa.coding.medium.solutions;

/**
 * Partition Equal Subset Sum (LeetCode 416)
 *
 * Approach: 0/1 knapsack DP. If the total sum is odd, it is impossible to
 * partition. Otherwise, we need to find a subset summing to total/2. We use a
 * boolean dp array where dp[s] = true means sum s is achievable. We iterate
 * backwards when including each number to avoid using it more than once.
 *
 * Key insight: Partition into two equal halves ↔ find one subset with sum =
 * total / 2. The backwards traversal of the dp array is the standard trick
 * that converts unbounded knapsack into 0/1 knapsack in-place.
 *
 * Time:  O(n * sum/2)
 * Space: O(sum/2)
 */
public class PartitionEqualSubsetSum {

    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int num : nums) total += num;

        if (total % 2 != 0) return false; // odd total cannot be split equally

        int target = total / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // sum 0 is always achievable

        for (int num : nums) {
            // iterate backwards to ensure each number is used at most once
            for (int s = target; s >= num; s--) {
                dp[s] = dp[s] || dp[s - num];
            }
        }
        return dp[target];
    }
}
