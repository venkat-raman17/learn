package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Distinct Subsequences
 *
 * <p>Difficulty: HARD
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/distinct-subsequences/
 *
 * <p>Given two strings s and t, return the number of distinct subsequences of s which
 * equals t.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= s.length <= 1000</li>
 *   <li>1 <= t.length <= 1000</li>
 *   <li>s and t consist of English letters.</li>
 *   <li>The answer fits in a 32-bit integer.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   numDistinct("rabbbit", "rabbit") => 3
 *   numDistinct("babgbag", "bag")    => 5
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n) or O(n)
 *
 * <p>Hint 1: dp[i][j] = number of ways to form t[0..j-1] using s[0..i-1];
 * if s[i-1] == t[j-1], dp[i][j] = dp[i-1][j-1] + dp[i-1][j], else dp[i-1][j].
 * <p>Hint 2: Base cases: dp[i][0] = 1 for all i (empty t is always achievable);
 * dp[0][j] = 0 for j > 0.
 */
public class DistinctSubsequences {

    public int numDistinct(String s, String t) {
        throw new UnsupportedOperationException("implement me");
    }
}
