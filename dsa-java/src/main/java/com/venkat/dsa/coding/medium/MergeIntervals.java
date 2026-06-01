package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Merge Intervals
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Intervals
 * <p>URL: https://leetcode.com/problems/merge-intervals/
 *
 * <p>Given an array of intervals, merge all overlapping intervals and return an array
 * of the non-overlapping intervals that cover all the input intervals.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= intervals.length &lt;= 10^4</li>
 *   <li>intervals[i].length == 2</li>
 *   <li>0 &lt;= start_i &lt;= end_i &lt;= 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  intervals = [[1,3],[2,6],[8,10],[15,18]]
 *   Output: [[1,6],[8,10],[15,18]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  intervals = [[1,4],[4,5]]
 *   Output: [[1,5]]   (touching intervals are considered overlapping)
 * </pre>
 *
 * <p>Target: Time O(n log n), Space O(n).
 *
 * <p>Hint 1: Sort by start time, then iterate and extend the current merged interval
 * whenever the next interval's start is &lt;= current end.
 * <p>Hint 2: Keep a "current" interval; when no overlap is detected, commit it to the
 * result list and start a new current interval.
 */
public class MergeIntervals {

    /**
     * Merges all overlapping intervals.
     *
     * @param intervals 2-D array of [start, end] pairs (unsorted)
     * @return merged non-overlapping intervals as a 2-D array
     */
    public int[][] merge(int[][] intervals) {
        throw new UnsupportedOperationException("implement me");
    }
}
