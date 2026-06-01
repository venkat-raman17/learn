package com.venkat.dsa.coding.medium.solutions;

/**
 * Permutation in String (LeetCode 567) — Medium
 *
 * <p>Approach: Fixed-size sliding window of length {@code s1.length()} over {@code s2}.
 * Maintain two frequency arrays ({@code need} for {@code s1}, {@code have} for the
 * current window) and a counter {@code matches} that tracks how many of the 26 letters
 * have equal frequency in both arrays. When {@code matches == 26} the current window is
 * a permutation of {@code s1}.
 *
 * <p><b>Time complexity:</b> O(n) where n = {@code s2.length()} — each character enters
 * and leaves the window exactly once; alphabet size is constant (26).<br>
 * <b>Space complexity:</b> O(1) — two fixed-size arrays of 26 integers.
 *
 * <p><b>Key insight:</b> Instead of sorting or hashing the window contents on every
 * slide, track an integer {@code matches} that is incremented/decremented in O(1) as
 * the window shifts, making each step amortised constant work.
 */
public class PermutationInString {

    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] need = new int[26];
        int[] have = new int[26];

        // Populate frequency arrays for s1 and the first window in s2
        for (int i = 0; i < s1.length(); i++) {
            need[s1.charAt(i) - 'a']++;
            have[s2.charAt(i) - 'a']++;
        }

        // Count how many characters already match between need and have
        int matches = 0;
        for (int i = 0; i < 26; i++) {
            if (need[i] == have[i]) matches++;
        }

        int winLen = s1.length();

        for (int right = winLen; right < s2.length(); right++) {
            if (matches == 26) return true;

            // Add the incoming character (right)
            int in = s2.charAt(right) - 'a';
            have[in]++;
            if (have[in] == need[in]) {
                matches++;
            } else if (have[in] - 1 == need[in]) {
                // Was matching before the increment, now over-counted
                matches--;
            }

            // Remove the outgoing character (right - winLen)
            int out = s2.charAt(right - winLen) - 'a';
            have[out]--;
            if (have[out] == need[out]) {
                matches++;
            } else if (have[out] + 1 == need[out]) {
                // Was matching before the decrement, now under-counted
                matches--;
            }
        }

        return matches == 26;
    }
}
