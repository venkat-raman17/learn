package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Coin Change
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/coin-change/
 *
 * <p>Given an array of coin denominations {@code coins} and a target {@code amount}, return the
 * fewest number of coins needed to make up that amount. Return -1 if it is impossible.
 * You have an unlimited supply of each coin.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= coins.length &lt;= 12</li>
 *   <li>1 &lt;= coins[i] &lt;= 2^31 - 1</li>
 *   <li>0 &lt;= amount &lt;= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: coins = [1,2,5], amount = 11  -> Output: 3  (5+5+1)
 * Input: coins = [2],     amount = 3   -> Output: -1
 * Input: coins = [1],     amount = 0   -> Output: 0
 * </pre>
 *
 * <p>Target: Time O(amount * coins.length), Space O(amount)
 *
 * <p>Hint 1: Build a dp array of size amount+1 initialized to amount+1 (infinity); dp[0] = 0.
 *            For each amount a and each coin c, dp[a] = min(dp[a], dp[a-c] + 1).
 * <p>Hint 2: If dp[amount] is still greater than amount at the end, return -1.
 */
public class CoinChange {

    public int coinChange(int[] coins, int amount) {
        throw new UnsupportedOperationException("implement me");
    }
}
