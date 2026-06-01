package com.venkat.dsa.coding.hard;

import java.util.PriorityQueue;

/**
 * NeetCode / LeetCode — Minimum Interval to Include Each Query
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Intervals
 * <p>URL: https://leetcode.com/problems/minimum-interval-to-include-each-query/
 *
 * <p>You are given a 2-D array of intervals and an array of queries. For each query q,
 * find the size (end - start + 1) of the smallest interval that contains q
 * (i.e., start &lt;= q &lt;= end). If no such interval exists, return -1 for that query.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= intervals.length &lt;= 10^5</li>
 *   <li>1 &lt;= queries.length &lt;= 10^5</li>
 *   <li>intervals[i].length == 2, 1 &lt;= start_i &lt;= end_i &lt;= 10^7</li>
 *   <li>1 &lt;= queries[i] &lt;= 10^7</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  intervals = [[1,4],[2,4],[3,6],[4,4]], queries = [2,3,4,5]
 *   Output: [3,3,1,4]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  intervals = [[2,3],[2,5],[1,8],[20,25]], queries = [2,19,5,22]
 *   Output: [2,-1,4,6]
 * </pre>
 *
 * <p>Target: Time O((n + q) log n), Space O(n + q).
 *
 * <p>Hint 1: Sort both intervals (by start) and queries (by value, keeping original indices).
 * Use a min-heap keyed by interval size to track all intervals whose start &lt;= current query.
 * <p>Hint 2: For each query in sorted order, add all intervals that start &lt;= query to the heap,
 * then pop off intervals from the heap whose end &lt; query. The heap top gives the smallest
 * valid interval for that query.
 */
public class MinimumIntervalToIncludeEachQuery {

    /**
     * Returns an array where result[i] is the size of the smallest interval containing
     * queries[i], or -1 if no interval contains it.
     *
     * @param intervals 2-D array of [start, end] intervals
     * @param queries   array of query points
     * @return answer array of same length as queries
     */
    public int[] minInterval(int[][] intervals, int[] queries) {
        throw new UnsupportedOperationException("implement me");
    }
}
