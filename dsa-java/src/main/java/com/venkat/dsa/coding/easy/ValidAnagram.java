package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Valid Anagram
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/valid-anagram/
 *
 * <p>Given two strings {@code s} and {@code t}, return {@code true} if {@code t} is an anagram
 * of {@code s}, and {@code false} otherwise. An anagram uses the same characters in the same
 * frequency, just rearranged.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= s.length, t.length &lt;= 5 * 10^4</li>
 *   <li>s and t consist of lowercase English letters</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: s = "anagram", t = "nagaram"  →  Output: true
 *   Input: s = "rat", t = "car"          →  Output: false
 * </pre>
 *
 * <p><b>Target:</b> Time O(n), Space O(1) (fixed 26-letter alphabet)
 *
 * <p><b>Hint 1:</b> Count character frequencies in both strings using an int[26] array.
 * <p><b>Hint 2:</b> The strings are anagrams if and only if both frequency arrays are identical.
 */
public class ValidAnagram {

    public boolean isAnagram(String s, String t) {
        throw new UnsupportedOperationException("implement me");
    }
}
