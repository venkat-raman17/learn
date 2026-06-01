package com.venkat.dsa.coding.medium.solutions;

/**
 * Interleaving String (LeetCode 97)
 *
 * Approach: 2-D DP. dp[i][j] = true iff s1[0..i-1] and s2[0..j-1] can form s3[0..i+j-1]
 * as an interleaving. Transitions:
 *   dp[i][j] = (dp[i-1][j] && s1[i-1]==s3[i+j-1])   // extend from s1
 *            | (dp[i][j-1] && s2[j-1]==s3[i+j-1])    // extend from s2
 * Base case: dp[0][0] = true; first row/column check prefix matches.
 *
 * Time:  O(m * n)  where m = s1.length(), n = s2.length()
 * Space: O(n)      — rolling 1-D array
 *
 * Key insight: at each cell we decide whether the next character in s3 came from
 * s1 or s2, and carry forward validity from the previous state.
 */
public class InterleavingString {

    public boolean isInterleave(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;

        // dp[j] = can s1[0..i-1] and s2[0..j-1] form s3[0..i+j-1]
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        // Initialise first row (i=0): only s2 used
        for (int j = 1; j <= n; j++) {
            dp[j] = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            // First column: only s1 used
            dp[0] = dp[0] && s1.charAt(i - 1) == s3.charAt(i - 1);

            for (int j = 1; j <= n; j++) {
                char c3 = s3.charAt(i + j - 1);
                // came from s1 (dp[j] still holds row i-1 value) OR from s2 (dp[j-1] already updated)
                dp[j] = (dp[j] && s1.charAt(i - 1) == c3)
                      | (dp[j - 1] && s2.charAt(j - 1) == c3);
            }
        }
        return dp[n];
    }
}
