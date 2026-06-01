package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Longest Palindromic Substring
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/longest-palindromic-substring/
 *
 * <p>Given a string {@code s}, return the longest substring of {@code s} that is a palindrome.
 * If there are multiple answers of the same length, return any one.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 1000</li>
 *   <li>{@code s} consists of only digits and English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: s = "babad"  -> Output: "bab"  (or "aba")
 * Input: s = "cbbd"   -> Output: "bb"
 * </pre>
 *
 * <p>Target: Time O(n^2), Space O(1) using expand-around-center
 *
 * <p>Hint 1: For each character (and each pair of adjacent characters), try expanding outward
 *            as long as the characters match — this covers both odd and even-length palindromes.
 * <p>Hint 2: Track the start index and maximum length seen so far, updating whenever a longer
 *            palindrome is found.
 */
public class LongestPalindromicSubstring {

    public String longestPalindrome(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
