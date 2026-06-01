package com.venkat.dsa.coding.easy.solutions;

/**
 * Valid Anagram (LeetCode 242) — Easy
 *
 * <p>Approach: Use a fixed-size int array of length 26 as a frequency counter for
 * lower-case English letters. Increment counts for each character in {@code s} and
 * decrement for each character in {@code t}. If all counts are zero afterward, the
 * strings are anagrams.
 *
 * <p><b>Time complexity:</b> O(n) — two linear passes (one per string).<br>
 * <b>Space complexity:</b> O(1) — the 26-element array is constant size regardless of input.
 *
 * <p><b>Key insight:</b> For lowercase-only input an array indexed by
 * {@code ch - 'a'} is faster and uses less memory than a HashMap.
 */
public class ValidAnagram {

    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] freq = new int[26];
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;   // count characters in s
            freq[t.charAt(i) - 'a']--;   // subtract characters in t
        }
        // every count must be zero for a valid anagram
        for (int count : freq) {
            if (count != 0) return false;
        }
        return true;
    }
}
