package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Longest Common Subsequence
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/longest-common-subsequence/
 *
 * <p>Given two strings text1 and text2, return the length of their longest common
 * subsequence. A subsequence is derived by deleting some (or no) characters without
 * changing the relative order of the remaining characters.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= text1.length, text2.length <= 1000</li>
 *   <li>text1 and text2 consist of only lowercase English characters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   longestCommonSubsequence("abcde", "ace") => 3   // "ace"
 *   longestCommonSubsequence("abc", "abc")   => 3
 *   longestCommonSubsequence("abc", "def")   => 0
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n)
 *
 * <p>Hint 1: Build dp[i][j] = LCS of text1[0..i-1] and text2[0..j-1]; if characters
 * match, dp[i][j] = dp[i-1][j-1] + 1, else max of the two neighbours.
 * <p>Hint 2: You can reduce space to O(min(m, n)) using two rolling rows.
 */
public class LongestCommonSubsequence {

    public int longestCommonSubsequence(String text1, String text2) {
        throw new UnsupportedOperationException("implement me");
    }
}
