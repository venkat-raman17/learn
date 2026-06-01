package com.venkat.dsa.coding.medium.solutions;

/**
 * Longest Common Subsequence (LeetCode 1143)
 *
 * Approach: Classic 2-D DP. dp[i][j] = length of LCS of text1[0..i-1] and text2[0..j-1].
 * If text1[i-1] == text2[j-1], we extend the LCS by 1: dp[i][j] = dp[i-1][j-1] + 1.
 * Otherwise we take the best of skipping one character from either string:
 * dp[i][j] = max(dp[i-1][j], dp[i][j-1]).
 *
 * Time:  O(m * n)  where m = text1.length(), n = text2.length()
 * Space: O(m * n)  — full DP table (can be reduced to O(min(m,n)) with rolling rows)
 *
 * Key insight: matching characters contribute +1 diagonal; mismatches propagate
 * the best answer from either prefix.
 */
public class LongestCommonSubsequence {

    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        // dp[i][j] = LCS length for text1[0..i-1] and text2[0..j-1]
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1; // characters match — extend diagonal
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]); // skip one char
                }
            }
        }
        return dp[m][n];
    }
}
