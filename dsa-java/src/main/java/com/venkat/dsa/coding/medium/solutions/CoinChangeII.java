package com.venkat.dsa.coding.medium.solutions;

/**
 * Coin Change II (LeetCode 518)
 *
 * Approach: Unbounded knapsack DP. dp[a] = number of distinct combinations that
 * sum to amount a. We iterate over each coin denomination and update the DP array
 * left-to-right (unbounded: same coin can be reused unlimited times). Processing
 * one coin at a time ensures combinations, not permutations, are counted.
 *
 * Time:  O(coins.length * amount)
 * Space: O(amount)
 *
 * Key insight: iterating coins in the outer loop and amounts in the inner loop
 * avoids counting the same combination multiple times in different orders.
 */
public class CoinChangeII {

    public int change(int amount, int[] coins) {
        // dp[a] = number of ways to make exactly amount a
        int[] dp = new int[amount + 1];
        dp[0] = 1; // one way to make 0: use no coins

        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) {
                // add ways that use this coin at least once (dp[a - coin] already uses coin)
                dp[a] += dp[a - coin];
            }
        }
        return dp[amount];
    }
}
