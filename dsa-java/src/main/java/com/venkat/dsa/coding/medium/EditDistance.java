package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Edit Distance
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/edit-distance/
 *
 * <p>Given two strings word1 and word2, return the minimum number of operations
 * (insert, delete, replace a character) to convert word1 to word2.
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 <= word1.length, word2.length <= 500</li>
 *   <li>word1 and word2 consist of lowercase English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   minDistance("horse", "ros") => 3
 *   minDistance("intention", "execution") => 5
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n) or O(n)
 *
 * <p>Hint 1: dp[i][j] = min edit distance for word1[0..i-1] to word2[0..j-1].
 * If characters match: dp[i][j] = dp[i-1][j-1]; otherwise 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]).
 * <p>Hint 2: Base cases: dp[i][0] = i (delete all), dp[0][j] = j (insert all).
 */
public class EditDistance {

    public int minDistance(String word1, String word2) {
        throw new UnsupportedOperationException("implement me");
    }
}
