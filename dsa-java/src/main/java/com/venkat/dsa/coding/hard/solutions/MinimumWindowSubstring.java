package com.venkat.dsa.coding.hard.solutions;

import java.util.HashMap;
import java.util.Map;

/**
 * Minimum Window Substring (LeetCode 76) — Hard
 *
 * <p>Approach: Variable-size sliding window using two {@link HashMap}s. {@code need} holds
 * the character frequencies required from {@code t}. {@code have} tracks the frequencies
 * in the current window. An integer {@code formed} counts how many distinct characters in
 * {@code need} are currently satisfied (frequency in window &gt;= frequency required).
 * When {@code formed == need.size()} the window is valid; we record it and contract from
 * the left until it becomes invalid, then expand again from the right.
 *
 * <p><b>Time complexity:</b> O(|s| + |t|) — each character in s is added and removed at
 * most once; building the need map is O(|t|).<br>
 * <b>Space complexity:</b> O(|s| + |t|) in the worst case for the two frequency maps,
 * though bounded by the alphabet size (O(1) for ASCII).
 *
 * <p><b>Key insight:</b> The {@code formed} counter avoids re-scanning all entries of
 * {@code need} on every window change, keeping each step O(1) amortised.
 */
public class MinimumWindowSubstring {

    public String minWindow(String s, String t) {
        if (s.isEmpty() || t.isEmpty()) return "";

        // Frequency map of characters required
        Map<Character, Integer> need = new HashMap<>();
        for (char ch : t.toCharArray()) {
            need.merge(ch, 1, Integer::sum);
        }

        Map<Character, Integer> have = new HashMap<>();
        int formed = 0;                  // distinct chars with satisfied frequency
        int required = need.size();

        int left = 0;
        int[] best = {-1, 0, 0};        // [windowLen, leftIndex, rightIndex]

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            have.merge(ch, 1, Integer::sum);

            // Check if this character's requirement is now fully met
            if (need.containsKey(ch) && have.get(ch).intValue() == need.get(ch).intValue()) {
                formed++;
            }

            // Contract the window from the left while it remains valid
            while (formed == required) {
                // Update best window if this one is smaller
                if (best[0] == -1 || right - left + 1 < best[0]) {
                    best[0] = right - left + 1;
                    best[1] = left;
                    best[2] = right;
                }

                char leftChar = s.charAt(left);
                have.merge(leftChar, -1, Integer::sum);
                // Check if removing leftChar breaks a requirement
                if (need.containsKey(leftChar) && have.get(leftChar) < need.get(leftChar)) {
                    formed--;
                }
                left++;
            }
        }

        return best[0] == -1 ? "" : s.substring(best[1], best[2] + 1);
    }
}
