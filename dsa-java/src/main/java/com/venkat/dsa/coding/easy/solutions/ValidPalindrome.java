package com.venkat.dsa.coding.easy.solutions;

/**
 * Valid Palindrome (LeetCode #125)
 *
 * Approach: Use two pointers starting at both ends of the string, skipping
 * non-alphanumeric characters and comparing the remaining characters
 * case-insensitively. If all matching pairs are equal the string is a
 * palindrome.
 *
 * Key insight: By advancing past non-alphanumeric characters in-place we avoid
 * allocating a cleaned copy of the string, keeping extra space to O(1).
 *
 * Time complexity:  O(n) — each character is visited at most once.
 * Space complexity: O(1) — no auxiliary data structure.
 */
public class ValidPalindrome {

    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            // Advance left pointer past non-alphanumeric characters
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            // Advance right pointer past non-alphanumeric characters
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            // Compare the two boundary characters (case-insensitive)
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
