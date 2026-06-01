package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Longest Repeating Character Replacement
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Sliding Window
 * <p>URL: https://leetcode.com/problems/longest-repeating-character-replacement/
 *
 * <p>Given a string {@code s} and an integer {@code k}, you can replace at most {@code k}
 * characters in the string. Return the length of the longest substring containing the same
 * letter after performing those replacements.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 10^5</li>
 *   <li>s consists of only uppercase English letters.</li>
 *   <li>0 &lt;= k &lt;= s.length</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  s = "ABAB", k = 2
 *   Output: 4   (replace both 'A' or both 'B' to get "AAAA" or "BBBB")
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  s = "AABABBA", k = 1
 *   Output: 4
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space (26-char frequency array)
 *
 * <p>Hint 1: The window is valid when (windowSize - maxFreqInWindow) &lt;= k.
 * <p>Hint 2: Keep a frequency count of characters in the window; track the maximum frequency seen so far to avoid re-scanning.
 */
public class LongestRepeatingCharacterReplacement {

    public int characterReplacement(String s, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
