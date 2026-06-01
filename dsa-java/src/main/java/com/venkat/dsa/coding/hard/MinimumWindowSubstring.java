package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Minimum Window Substring
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Sliding Window
 * <p>URL: https://leetcode.com/problems/minimum-window-substring/
 *
 * <p>Given strings {@code s} and {@code t}, return the minimum window substring of {@code s}
 * that contains every character in {@code t} (including duplicates). If no such window exists,
 * return the empty string {@code ""}.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length, t.length &lt;= 10^5</li>
 *   <li>s and t consist of uppercase and lowercase English letters.</li>
 *   <li>The answer is unique when it exists.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  s = "ADOBECODEBANC", t = "ABC"
 *   Output: "BANC"
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  s = "a", t = "a"
 *   Output: "a"
 * </pre>
 *
 * <p>Target: O(n + m) time, O(n + m) space
 *
 * <p>Hint 1: Expand the right pointer until the window contains all characters of t, then shrink from the left to minimize it.
 * <p>Hint 2: Track a "formed" counter that increments only when a character's frequency in the window meets the required frequency from t.
 */
public class MinimumWindowSubstring {

    public String minWindow(String s, String t) {
        throw new UnsupportedOperationException("implement me");
    }
}
