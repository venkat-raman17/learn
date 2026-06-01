package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert Interval (LeetCode 57)
 *
 * <p>The input array is already sorted and non-overlapping. Walk through it in three phases:
 * (1) add all intervals that end before the new interval starts unchanged,
 * (2) merge all intervals that overlap the new interval into one expanded interval,
 * (3) add the remaining intervals unchanged.
 *
 * <p><b>Time complexity:</b> O(n) — single pass.<br>
 * <b>Space complexity:</b> O(n) for the result list.
 *
 * <p><b>Key insight:</b> Overlap between [a,b] and [c,d] iff a <= d AND c <= b; the merged
 * interval is [min(a,c), max(b,d)].
 */
public class InsertInterval {

    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;

        // Phase 1: intervals that come entirely before newInterval
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        // Phase 2: merge all overlapping intervals into newInterval
        while (i < n && intervals[i][0] <= newInterval[1]) {
            // Expand newInterval to cover this overlapping interval
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);

        // Phase 3: intervals that come entirely after newInterval
        while (i < n) {
            result.add(intervals[i]);
            i++;
        }

        return result.toArray(new int[result.size()][]);
    }
}
