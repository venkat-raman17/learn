package com.venkat.dsa.coding.medium.solutions;

import java.util.Arrays;

/**
 * Non-Overlapping Intervals (LeetCode 435)
 *
 * <p>Sort intervals by their end time (the classic "activity selection" greedy). Greedily
 * keep the interval with the earliest end; whenever an interval overlaps the last kept
 * interval, discard it (count one removal). The greedy choice of keeping the earliest-
 * ending interval always leaves the most room for future intervals.
 *
 * <p><b>Time complexity:</b> O(n log n) — dominated by sorting.<br>
 * <b>Space complexity:</b> O(1) extra.
 *
 * <p><b>Key insight:</b> Sorting by end time and always keeping the interval with the
 * smallest end maximises the number of non-overlapping intervals we can retain, so the
 * number of removals = n − (maximum non-overlapping kept).
 */
public class NonOverlappingIntervals {

    public int eraseOverlapIntervals(int[][] intervals) {
        // Sort by end time — greedy activity selection
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);

        int removals = 0;
        int prevEnd = intervals[0][1]; // end time of last kept interval

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) {
                // Overlap — remove current interval (keep the one with smaller end)
                removals++;
                // prevEnd stays the same (the kept interval still has the earlier end)
            } else {
                // No overlap — keep this interval
                prevEnd = intervals[i][1];
            }
        }
        return removals;
    }
}
