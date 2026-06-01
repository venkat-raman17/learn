package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Partition Labels (LeetCode #763)
 *
 * <p>First pass: record the last occurrence index of every character. Second pass: scan left to
 * right maintaining the farthest last-occurrence seen in the current partition window. When the
 * current index reaches that boundary, the partition is complete — record its size and start a new
 * one.
 *
 * <p><b>Key insight:</b> A character must stay within the same partition as all of its other
 * occurrences; the farthest last-occurrence in the current window defines the earliest point at
 * which the partition can end.
 *
 * <p><b>Time complexity:</b> O(n) — two linear passes.<br>
 * <b>Space complexity:</b> O(1) — the last-occurrence array has at most 26 entries.
 */
public class PartitionLabels {

    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }

        List<Integer> result = new ArrayList<>();
        int start = 0;
        int end = 0; // farthest boundary for current partition

        for (int i = 0; i < s.length(); i++) {
            // Extend the partition boundary to include all occurrences of s[i]
            end = Math.max(end, last[s.charAt(i) - 'a']);

            // Current index reached the boundary — close partition
            if (i == end) {
                result.add(end - start + 1);
                start = i + 1;
            }
        }

        return result;
    }
}
