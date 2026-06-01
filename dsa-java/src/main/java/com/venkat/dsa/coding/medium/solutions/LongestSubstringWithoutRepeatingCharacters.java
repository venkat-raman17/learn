package com.venkat.dsa.coding.medium.solutions;

import java.util.HashMap;
import java.util.Map;

/**
 * Longest Substring Without Repeating Characters (LeetCode 3) — Medium
 *
 * <p>Approach: Sliding window with a {@link HashMap} that maps each character to the
 * index <em>after</em> its last occurrence. Two pointers {@code left} and {@code right}
 * define the current window. When {@code right} encounters a character already inside
 * the window, {@code left} jumps to {@code map.get(ch)} (one past the previous position)
 * so the duplicate is expelled without rescanning.
 *
 * <p><b>Time complexity:</b> O(n) — each character is visited at most twice (once by
 * {@code right}, once implicitly when {@code left} skips past it).<br>
 * <b>Space complexity:</b> O(min(n, |alphabet|)) — at most 128 entries for ASCII.
 *
 * <p><b>Key insight:</b> Storing the <em>next valid left boundary</em> rather than a
 * boolean "seen" flag lets us skip {@code left} forward in O(1) instead of shrinking
 * the window character by character.
 */
public class LongestSubstringWithoutRepeatingCharacters {

    public int lengthOfLongestSubstring(String s) {
        // char → index immediately after its last seen position
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLen = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            // If ch is inside the current window, advance left past the duplicate
            if (lastSeen.containsKey(ch) && lastSeen.get(ch) > left) {
                left = lastSeen.get(ch);
            }
            // Record the next valid start position if ch repeats later
            lastSeen.put(ch, right + 1);
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
