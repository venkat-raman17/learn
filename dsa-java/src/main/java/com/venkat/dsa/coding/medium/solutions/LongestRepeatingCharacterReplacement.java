package com.venkat.dsa.coding.medium.solutions;

/**
 * Longest Repeating Character Replacement (LeetCode 424) — Medium
 *
 * <p>Approach: Sliding window over the uppercase-letter string. Maintain a frequency
 * array {@code count[26]} for the current window. The window is valid when
 * {@code (windowSize - maxCount) <= k}, where {@code maxCount} is the frequency of the
 * most common character in the window. When the window becomes invalid, slide {@code left}
 * one step right (decrement the leaving character's count). We never shrink the window
 * below its historical maximum size, which means {@code maxCount} only needs to track
 * the running maximum — never recomputed on shrink.
 *
 * <p><b>Time complexity:</b> O(n) — both pointers advance at most n steps total.<br>
 * <b>Space complexity:</b> O(1) — fixed-size array of 26 entries.
 *
 * <p><b>Key insight:</b> Replacements needed = windowSize - maxFrequencyChar. If that
 * exceeds k, the window is invalid. We can afford not to recompute {@code maxCount} on
 * shrink because a smaller window with a lower {@code maxCount} would not extend our
 * answer — we only care about growing the window.
 */
public class LongestRepeatingCharacterReplacement {

    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int left = 0;
        int maxCount = 0; // highest frequency of any single char seen so far in the window

        for (int right = 0; right < s.length(); right++) {
            int idx = s.charAt(right) - 'A';
            count[idx]++;
            // maxCount only ever grows — shrinking won't help us find a longer window
            maxCount = Math.max(maxCount, count[idx]);

            // Window size = right - left + 1; replacements needed = windowSize - maxCount
            if ((right - left + 1) - maxCount > k) {
                // Window is invalid: slide left one step
                count[s.charAt(left) - 'A']--;
                left++;
            }
        }

        // The window never shrinks below its peak, so (s.length() - left) is the answer
        return s.length() - left;
    }
}
