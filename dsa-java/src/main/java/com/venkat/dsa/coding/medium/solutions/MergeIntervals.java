package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Merge Intervals (LeetCode 56)
 *
 * <p>Sort intervals by start time. Then iterate and greedily merge: keep a running
 * "current" interval and, whenever the next interval's start is at or before the
 * current end, extend the current end. Otherwise, commit the current interval and
 * start a new one.
 *
 * <p><b>Time complexity:</b> O(n log n) — sorting dominates.<br>
 * <b>Space complexity:</b> O(n) for the output list.
 *
 * <p><b>Key insight:</b> After sorting by start, only consecutive pairs can overlap,
 * so one linear pass over the sorted array is sufficient.
 */
public class MergeIntervals {

    public int[][] merge(int[][] intervals) {
        // Sort by start time
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                // Overlapping — extend current interval if needed
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                // No overlap — commit current and move on
                merged.add(current);
                current = intervals[i];
            }
        }
        merged.add(current); // commit last interval

        return merged.toArray(new int[merged.size()][]);
    }
}
