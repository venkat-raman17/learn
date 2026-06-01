package com.venkat.dsa.coding.hard.solutions;

/**
 * Regular Expression Matching (LeetCode 10)
 *
 * Approach: 2-D DP. dp[i][j] = true iff s[0..i-1] matches p[0..j-1].
 *   - p[j-1] is a literal or '.': dp[i][j] = dp[i-1][j-1] && match(s[i-1], p[j-1])
 *   - p[j-1] is '*':
 *       * Zero occurrences of p[j-2]: dp[i][j] = dp[i][j-2]
 *       * One+ occurrences: dp[i][j] |= dp[i-1][j] && match(s[i-1], p[j-2])
 * Base case: dp[0][0] = true; dp[0][j] handles patterns like "a*b*" matching empty s.
 *
 * Time:  O(m * n)  where m = s.length(), n = p.length()
 * Space: O(m * n)
 *
 * Key insight: the '*' case splits into "use zero copies" (skip the x* pair) or
 * "use one or more copies" (consuming one s character while keeping j the same).
 */
public class RegularExpressionMatching {

    public boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        // dp[i][j] = s[0..i-1] matches p[0..j-1]
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;

        // Pattern can match empty string if it consists of "x*" pairs
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2]; // zero occurrences of the preceding element
            }
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char pc = p.charAt(j - 1);
                if (pc == '*') {
                    // Zero occurrences: skip the "x*" pair
                    dp[i][j] = dp[i][j - 2];
                    // One+ occurrences: the preceding element p[j-2] matches s[i-1]
                    if (matches(s.charAt(i - 1), p.charAt(j - 2))) {
                        dp[i][j] |= dp[i - 1][j];
                    }
                } else {
                    // Literal or '.' match
                    dp[i][j] = dp[i - 1][j - 1] && matches(s.charAt(i - 1), pc);
                }
            }
        }
        return dp[m][n];
    }

    /** Returns true if pattern character pc matches string character sc. */
    private boolean matches(char sc, char pc) {
        return pc == '.' || pc == sc;
    }
}
