package com.venkat.dsa.coding.medium.solutions;

/**
 * Edit Distance (LeetCode 72)
 *
 * Approach: Classic 2-D DP (Levenshtein distance). dp[i][j] = minimum number of
 * operations (insert, delete, replace) to convert word1[0..i-1] into word2[0..j-1].
 *   - If characters match: dp[i][j] = dp[i-1][j-1]  (no operation needed)
 *   - Otherwise:           dp[i][j] = 1 + min(dp[i-1][j-1],   // replace
 *                                              dp[i-1][j],      // delete from word1
 *                                              dp[i][j-1])      // insert into word1
 * Base cases: converting to/from empty string costs i or j operations.
 *
 * Time:  O(m * n)
 * Space: O(n)  — rolling 1-D array with a single prev variable
 *
 * Key insight: three DP neighbors (diagonal, top, left) correspond directly to the
 * three edit operations.
 */
public class EditDistance {

    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        // dp[j] = edit distance for word1[0..i-1] vs word2[0..j-1]
        int[] dp = new int[n + 1];

        // Base case: empty word1 -> insert j chars
        for (int j = 0; j <= n; j++) dp[j] = j;

        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; // dp[i-1][j-1] diagonal value
            dp[0] = i;        // base case: empty word2 -> delete i chars

            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // save before overwrite (will become prev for next j)
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev; // characters match — take diagonal, no cost
                } else {
                    dp[j] = 1 + Math.min(prev,              // replace
                                Math.min(dp[j],              // delete
                                         dp[j - 1]));        // insert
                }
                prev = temp;
            }
        }
        return dp[n];
    }
}
