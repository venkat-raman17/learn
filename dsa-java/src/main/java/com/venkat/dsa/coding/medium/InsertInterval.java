package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Insert Interval
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Intervals
 * <p>URL: https://leetcode.com/problems/insert-interval/
 *
 * <p>Given an array of non-overlapping intervals sorted by start time and a new interval,
 * insert the new interval and merge if necessary so the result remains sorted and non-overlapping.
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 &lt;= intervals.length &lt;= 10^4</li>
 *   <li>intervals[i].length == 2, newInterval.length == 2</li>
 *   <li>0 &lt;= start_i &lt;= end_i &lt;= 10^5</li>
 *   <li>intervals is sorted in ascending order by start_i</li>
 *   <li>start_i are unique (no duplicates in existing intervals)</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  intervals = [[1,3],[6,9]], newInterval = [2,5]
 *   Output: [[1,5],[6,9]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
 *   Output: [[1,2],[3,10],[12,16]]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(n).
 *
 * <p>Hint 1: Process in three phases: add all intervals that end before newInterval starts,
 * then merge all overlapping ones into newInterval, then add the rest.
 * <p>Hint 2: Two intervals overlap when one starts before the other ends:
 * intervals[i][0] &lt;= newInterval[1] and newInterval[0] &lt;= intervals[i][1].
 */
public class InsertInterval {

    /**
     * Inserts newInterval into intervals and merges any overlapping intervals.
     *
     * @param intervals  sorted, non-overlapping intervals
     * @param newInterval the interval to insert
     * @return merged result as a 2-D int array
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        throw new UnsupportedOperationException("implement me");
    }
}
