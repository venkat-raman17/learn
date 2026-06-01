package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Palindrome Partitioning
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/palindrome-partitioning/
 *
 * <p>Given a string {@code s}, partition {@code s} such that every substring of the partition
 * is a palindrome. Return all possible palindrome partitioning of {@code s}.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 16</li>
 *   <li>s contains only lowercase English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: s = "aab"
 *   Output: [["a","a","b"],["aa","b"]]
 *
 *   Input: s = "a"
 *   Output: [["a"]]
 * </pre>
 *
 * <p>Target: Time O(n * 2^n), Space O(n)
 *
 * <p>Hint 1: At each position, try every possible end index for the next substring; only
 * recurse further if that substring is a palindrome.
 * <p>Hint 2: A simple helper isPalindrome(s, l, r) checking two pointers avoids allocating
 * new strings and speeds up the palindrome check.
 */
public class PalindromePartitioning {

    public List<List<String>> partition(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
