package com.venkat.dsa.coding.medium.solutions;

/**
 * Longest Palindromic Substring (LeetCode 5)
 *
 * Approach: Expand-around-center. For every index we try to expand outward
 * treating it as the center of an odd-length palindrome, and for every pair
 * of adjacent equal characters we try even-length expansion. We track the
 * start and maximum length of the best palindrome found.
 *
 * Key insight: Any palindrome has a center (one character for odd length, two
 * for even). There are O(n) centers and each expansion is O(n) in the worst
 * case, giving O(n^2) overall — simpler than Manacher's O(n) but sufficient.
 *
 * Time:  O(n^2)
 * Space: O(1)
 */
public class LongestPalindromicSubstring {

    public String longestPalindrome(String s) {
        int n = s.length();
        int start = 0;
        int maxLen = 1;

        for (int i = 0; i < n; i++) {
            // odd-length palindromes centered at i
            int len1 = expand(s, i, i);
            // even-length palindromes centered between i and i+1
            int len2 = expand(s, i, i + 1);

            int len = Math.max(len1, len2);
            if (len > maxLen) {
                maxLen = len;
                // re-derive start from center i and length
                start = i - (len - 1) / 2;
            }
        }
        return s.substring(start, start + maxLen);
    }

    /** Returns the length of the longest palindrome centered at (left, right). */
    private int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // after the loop, left and right are one position outside the palindrome
        return right - left - 1;
    }
}
