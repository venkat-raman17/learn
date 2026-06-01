package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Palindrome Partitioning (LeetCode 131)
 *
 * Approach: Backtracking — at each position try all substrings starting there;
 * if the substring is a palindrome, add it to the current path and recurse from
 * the character after the substring. When the start index reaches the end of the
 * string, a complete valid partition has been found.
 *
 * Key insight: checking palindromes inline is fine given the small input size;
 * for very large strings a DP palindrome pre-check table would reduce palindrome
 * checks from O(n) to O(1), but the overall backtracking complexity stays O(n * 2^n).
 *
 * Time:  O(n * 2^n).
 * Space: O(n) recursion depth.
 */
public class PalindromePartitioning {

    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(String s, int start, List<String> current, List<List<String>> result) {
        if (start == s.length()) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int end = start + 1; end <= s.length(); end++) {
            String sub = s.substring(start, end);
            if (isPalindrome(sub)) {
                current.add(sub);
                backtrack(s, end, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }
}
