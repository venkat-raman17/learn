package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Burst Balloons
 *
 * <p>Difficulty: HARD
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/burst-balloons/
 *
 * <p>Given n balloons indexed from 0 to n-1, each with a number on it, burst all
 * balloons to maximise coins. Bursting balloon i yields nums[i-1] * nums[i] * nums[i+1]
 * coins; treat out-of-bounds as 1.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == nums.length</li>
 *   <li>1 <= n <= 300</li>
 *   <li>0 <= nums[i] <= 100</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   maxCoins([3,1,5,8]) => 167   // burst 1 -> 3 -> 5 -> 8
 *   maxCoins([1,5])     => 10
 * </pre>
 *
 * <p>Target Time: O(n^3), Space: O(n^2)
 *
 * <p>Hint 1: Think of k as the LAST balloon to burst in the open interval (i, j);
 * dp[i][j] = max coins from bursting all balloons strictly between i and j.
 * <p>Hint 2: dp[i][j] = max over k in (i,j) of: dp[i][k] + nums[i]*nums[k]*nums[j] + dp[k][j].
 * Pad nums with 1s at both ends.
 */
public class BurstBalloons {

    public int maxCoins(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
