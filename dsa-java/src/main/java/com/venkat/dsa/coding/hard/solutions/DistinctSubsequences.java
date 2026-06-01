package com.venkat.dsa.coding.hard.solutions;

/**
 * Distinct Subsequences (LeetCode 115)
 *
 * Approach: 2-D DP. dp[i][j] = number of distinct subsequences of s[0..i-1] that
 * equal t[0..j-1]. If s[i-1] == t[j-1], we can either use s[i-1] to match t[j-1]
 * (dp[i-1][j-1]) or skip s[i-1] (dp[i-1][j]); otherwise we can only skip s[i-1].
 * Base case: dp[i][0] = 1 for all i (empty t is always a subsequence).
 *
 * Optimised to a rolling 1-D array traversed right-to-left to avoid overwriting
 * the dp[i-1][j-1] diagonal value.
 *
 * Time:  O(m * n)
 * Space: O(n)
 *
 * Key insight: matching characters give a choice — use or skip — which adds both
 * the diagonal and the value above, while mismatches only propagate the "skip" count.
 */
public class DistinctSubsequences {

    public int numDistinct(String s, String t) {
        int m = s.length(), n = t.length();
        // dp[j] = number of ways to match t[0..j-1] using s[0..i-1]
        int[] dp = new int[n + 1];
        dp[0] = 1; // empty t matched by any prefix of s in exactly 1 way

        for (int i = 1; i <= m; i++) {
            // traverse right-to-left to preserve dp[j-1] as the diagonal (i-1, j-1) value
            for (int j = n; j >= 1; j--) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] += dp[j - 1]; // use s[i-1] to match t[j-1] + skip option already in dp[j]
                }
                // if no match, dp[j] unchanged (carry forward the skip count)
            }
        }
        return dp[n];
    }
}
