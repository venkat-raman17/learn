package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Permutation in String
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Sliding Window
 * <p>URL: https://leetcode.com/problems/permutation-in-string/
 *
 * <p>Given two strings {@code s1} and {@code s2}, return {@code true} if any permutation of
 * {@code s1} is a substring of {@code s2}; otherwise return {@code false}.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s1.length, s2.length &lt;= 10^4</li>
 *   <li>s1 and s2 consist of lowercase English letters.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  s1 = "ab", s2 = "eidbaooo"
 *   Output: true   ("ba" is a permutation of "ab" and appears in s2)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  s1 = "ab", s2 = "eidboaoo"
 *   Output: false
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space (fixed 26-element arrays)
 *
 * <p>Hint 1: Slide a fixed-size window of length s1.length() over s2 and compare frequency arrays.
 * <p>Hint 2: Instead of comparing full arrays each step, maintain a "matches" counter that tracks how many of the 26 character counts are equal.
 */
public class PermutationInString {

    public boolean checkInclusion(String s1, String s2) {
        throw new UnsupportedOperationException("implement me");
    }
}
