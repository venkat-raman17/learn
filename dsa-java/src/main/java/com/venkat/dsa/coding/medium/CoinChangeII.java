package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Coin Change II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/coin-change-ii/
 *
 * <p>Given an integer array coins and an integer amount, return the number of
 * combinations (not permutations) that make up that amount using the coins
 * (each coin may be used an unlimited number of times).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= coins.length <= 300</li>
 *   <li>1 <= coins[i] <= 5000</li>
 *   <li>0 <= amount <= 5000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   change(5, [1,2,5]) => 4   // 5; 2+2+1; 2+1+1+1; 1+1+1+1+1
 *   change(3, [2])     => 0
 *   change(10, [10])   => 1
 * </pre>
 *
 * <p>Target Time: O(amount * coins.length), Space: O(amount)
 *
 * <p>Hint 1: Use a 1-D dp array where dp[a] = number of ways to reach amount a; iterate
 * coins in the outer loop to avoid counting permutations.
 * <p>Hint 2: For each coin, iterate amounts from coin to amount and add dp[a - coin] to dp[a].
 */
public class CoinChangeII {

    public int change(int amount, int[] coins) {
        throw new UnsupportedOperationException("implement me");
    }
}
