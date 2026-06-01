package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Regular Expression Matching
 *
 * <p>Difficulty: HARD
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/regular-expression-matching/
 *
 * <p>Given an input string s and a pattern p, implement regular expression matching
 * supporting '.' (matches any single character) and '*' (matches zero or more of the
 * preceding element). The matching must cover the entire input string.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= s.length <= 20</li>
 *   <li>1 <= p.length <= 30</li>
 *   <li>s contains only lowercase English letters.</li>
 *   <li>p contains only lowercase English letters, '.', and '*'.</li>
 *   <li>It is guaranteed for each '*' occurrence there is a previous valid character.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   isMatch("aa", "a")    => false
 *   isMatch("aa", "a*")   => true
 *   isMatch("ab", ".*")   => true
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n)
 *
 * <p>Hint 1: dp[i][j] = true if s[0..i-1] matches p[0..j-1]; handle '*' by either
 * ignoring the preceding element (dp[i][j-2]) or consuming one more character of s
 * (dp[i-1][j], when characters match or p[j-2] == '.').
 * <p>Hint 2: Base case dp[0][0] = true; handle patterns like "a*b*" matching empty string
 * with dp[0][j] = dp[0][j-2] when p[j-1] == '*'.
 */
public class RegularExpressionMatching {

    public boolean isMatch(String s, String p) {
        throw new UnsupportedOperationException("implement me");
    }
}
