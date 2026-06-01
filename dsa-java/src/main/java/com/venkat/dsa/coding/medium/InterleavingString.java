package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Interleaving String
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/interleaving-string/
 *
 * <p>Given strings s1, s2, and s3, determine whether s3 is formed by an interleaving
 * of s1 and s2 (preserving the relative order of characters from each string).
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 <= s1.length, s2.length <= 100</li>
 *   <li>0 <= s3.length <= 200</li>
 *   <li>s1, s2, s3 consist of lowercase English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   isInterleave("aabcc", "dbbca", "aadbbcbcac") => true
 *   isInterleave("aabcc", "dbbca", "aadbbbaccc") => false
 *   isInterleave("", "", "")                     => true
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n) or O(n)
 *
 * <p>Hint 1: Build dp[i][j] = true if s3[0..i+j-1] can be formed from s1[0..i-1] and s2[0..j-1].
 * <p>Hint 2: dp[i][j] = (dp[i-1][j] && s1[i-1]==s3[i+j-1]) || (dp[i][j-1] && s2[j-1]==s3[i+j-1]).
 */
public class InterleavingString {

    public boolean isInterleave(String s1, String s2, String s3) {
        throw new UnsupportedOperationException("implement me");
    }
}
