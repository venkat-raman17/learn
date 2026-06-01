package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Longest Substring Without Repeating Characters
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Sliding Window
 * <p>URL: https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 * <p>Given a string {@code s}, find the length of the longest substring that contains no
 * repeating characters.
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 &lt;= s.length &lt;= 5 * 10^4</li>
 *   <li>{@code s} consists of English letters, digits, symbols, and spaces.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  s = "abcabcbb"
 *   Output: 3   (substring "abc")
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  s = "bbbbb"
 *   Output: 1   (substring "b")
 * </pre>
 *
 * <p>Target: O(n) time, O(min(n, alphabet)) space
 *
 * <p>Hint 1: Use a sliding window with a HashSet to track characters in the current window.
 * <p>Hint 2: When a duplicate is found, shrink the window from the left until the duplicate is removed.
 */
public class LongestSubstringWithoutRepeatingCharacters {

    public int lengthOfLongestSubstring(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
