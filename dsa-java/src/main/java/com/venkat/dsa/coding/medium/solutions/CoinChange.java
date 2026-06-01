package com.venkat.dsa.coding.medium.solutions;

import java.util.Arrays;

/**
 * Coin Change (LeetCode 322)
 *
 * Approach: Bottom-up DP (unbounded knapsack). Let dp[a] = minimum number of
 * coins needed to make amount a. Initialise dp[0] = 0 and all others to
 * amount + 1 (a sentinel "infinity"). For each amount from 1 to target we try
 * every coin: dp[a] = min(dp[a], dp[a - coin] + 1).
 *
 * Key insight: Because each coin can be reused, we fill the dp table in
 * ascending order of amount — classic unbounded knapsack direction.
 *
 * Time:  O(amount * len(coins))
 * Space: O(amount)
 */
public class CoinChange {

    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // sentinel: impossible
        dp[0] = 0;

        for (int a = 1; a <= amount; a++) {
            for (int coin : coins) {
                if (coin <= a) {
                    dp[a] = Math.min(dp[a], dp[a - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
