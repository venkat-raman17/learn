package com.venkat.dsa.coding.medium.solutions;

import java.util.HashSet;
import java.util.Set;

/**
 * Longest Consecutive Sequence (LeetCode 128) — Medium
 *
 * <p>Approach: Load all numbers into a HashSet for O(1) lookups. Then iterate; for each
 * number {@code n}, check whether {@code n-1} is absent — if so, {@code n} is the start
 * of a potential sequence. Extend the sequence upward ({@code n+1, n+2, ...}) counting
 * its length and update the global maximum.
 *
 * <p><b>Time complexity:</b> O(n) — each number is visited at most twice (once in the
 * outer loop, once while extending a sequence it starts).<br>
 * <b>Space complexity:</b> O(n) — the HashSet stores all n elements.
 *
 * <p><b>Key insight:</b> Only process a number as a sequence start when {@code n-1}
 * is not in the set; this ensures each sequence is counted exactly once, giving the
 * O(n) guarantee despite the nested while-loop.
 */
public class LongestConsecutiveSequence {

    public int longestConsecutive(int[] nums) {
        Set<Integer> numSet = new HashSet<>();
        for (int n : nums) numSet.add(n);

        int longest = 0;
        for (int n : numSet) {
            // only start counting from the beginning of a sequence
            if (!numSet.contains(n - 1)) {
                int length = 1;
                while (numSet.contains(n + length)) {
                    length++;
                }
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }
}
