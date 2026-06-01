package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Palindromic Substrings
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/palindromic-substrings/
 *
 * <p>Given a string {@code s}, return the number of substrings of {@code s} that are palindromes.
 * A single character is always a palindrome.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 1000</li>
 *   <li>{@code s} consists of lowercase English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: s = "abc"  -> Output: 3  ("a", "b", "c")
 * Input: s = "aaa"  -> Output: 6  ("a","a","a","aa","aa","aaa")
 * </pre>
 *
 * <p>Target: Time O(n^2), Space O(1) using expand-around-center
 *
 * <p>Hint 1: Use the same expand-around-center technique as Longest Palindromic Substring, but
 *            count every successful expansion instead of tracking the longest.
 * <p>Hint 2: Remember to handle both odd-length (single center) and even-length (two-center) expansions.
 */
public class PalindromicSubstrings {

    public int countSubstrings(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
