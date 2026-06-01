package com.venkat.dsa.coding.medium.solutions;

/**
 * Palindromic Substrings (LeetCode 647)
 *
 * Approach: Expand-around-center. For each of the 2n-1 possible centers (n
 * odd-length centers + n-1 even-length centers) we expand outward as long as
 * characters match, incrementing a counter for every valid palindrome found.
 *
 * Key insight: Every palindrome has a unique center. Counting from all centers
 * is cleaner and faster in practice than an O(n^2) DP table.
 *
 * Time:  O(n^2)
 * Space: O(1)
 */
public class PalindromicSubstrings {

    public int countSubstrings(String s) {
        int count = 0;
        int n = s.length();

        for (int i = 0; i < n; i++) {
            count += countFromCenter(s, i, i);     // odd-length
            count += countFromCenter(s, i, i + 1); // even-length
        }
        return count;
    }

    /** Counts palindromes expanding outward from (left, right). */
    private int countFromCenter(String s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            count++;
            left--;
            right++;
        }
        return count;
    }
}
