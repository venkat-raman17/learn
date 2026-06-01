package com.venkat.dsa.coding.medium.solutions;

/**
 * Decode Ways (LeetCode 91)
 *
 * Approach: Bottom-up DP where dp[i] = number of ways to decode s[0..i-1].
 * A single character s[i-1] contributes dp[i-1] ways if it is non-zero.
 * A two-character group s[i-2..i-1] contributes dp[i-2] ways if it is in [10,26].
 * We only keep two rolling variables to avoid O(n) space.
 *
 * Key insight: Leading zeros are invalid ('0' cannot be decoded alone), so we
 * must check both single-digit and two-digit validity before adding.
 *
 * Time:  O(n)
 * Space: O(1)
 */
public class DecodeWays {

    public int numDecodings(String s) {
        int n = s.length();
        // dp2 = dp[i-2], dp1 = dp[i-1], both seeded for the empty prefix
        int dp2 = 0; // dp[i-2]: ways to decode empty string before any two-char lookahead
        int dp1 = 1; // dp[0]:  one way to decode the empty prefix

        for (int i = 1; i <= n; i++) {
            int curr = 0;

            // single-digit decode: s[i-1] must not be '0'
            if (s.charAt(i - 1) != '0') {
                curr += dp1;
            }

            // two-digit decode: s[i-2..i-1] must be in range [10, 26]
            if (i >= 2) {
                int twoDigit = Integer.parseInt(s.substring(i - 2, i));
                if (twoDigit >= 10 && twoDigit <= 26) {
                    curr += dp2;
                }
            }

            dp2 = dp1;
            dp1 = curr;
        }
        return dp1;
    }
}
