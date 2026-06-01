package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Non Overlapping Intervals
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Intervals
 * <p>URL: https://leetcode.com/problems/non-overlapping-intervals/
 *
 * <p>Given an array of intervals, return the minimum number of intervals you need to
 * remove to make the rest of the intervals non-overlapping.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= intervals.length &lt;= 10^5</li>
 *   <li>intervals[i].length == 2</li>
 *   <li>-5 * 10^4 &lt;= start_i &lt; end_i &lt;= 5 * 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  intervals = [[1,2],[2,3],[3,4],[1,3]]
 *   Output: 1   (remove [1,3])
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  intervals = [[1,2],[1,2],[1,2]]
 *   Output: 2   (keep only one [1,2])
 * </pre>
 *
 * <p>Target: Time O(n log n), Space O(1).
 *
 * <p>Hint 1: Greedy — sort by end time and always keep the interval that ends earliest.
 * This is equivalent to the classic "activity selection" problem.
 * <p>Hint 2: Track the end of the last kept interval; whenever the next interval's start
 * is &lt; that end, increment removals (skip the interval with the later end).
 */
public class NonOverlappingIntervals {

    /**
     * Returns the minimum number of intervals to remove to eliminate all overlaps.
     *
     * @param intervals 2-D array of [start, end] pairs
     * @return minimum removals needed
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        throw new UnsupportedOperationException("implement me");
    }
}
