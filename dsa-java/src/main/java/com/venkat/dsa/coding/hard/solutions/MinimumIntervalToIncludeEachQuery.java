package com.venkat.dsa.coding.hard.solutions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Minimum Interval to Include Each Query (LeetCode 1851)
 *
 * <p>Sort both the intervals and the queries. Use a sweep-line with a min-heap ordered by
 * interval size (right-left+1). Process queries in increasing order: for each query value,
 * add all intervals whose left endpoint is <= query to the heap. Then evict from the heap
 * all intervals whose right endpoint is < query (they can no longer cover this or future
 * queries). The heap top is then the smallest interval that covers the current query.
 * Because queries are reordered, answers are stored in a map and re-indexed at the end.
 *
 * <p><b>Time complexity:</b> O((n + q) log n) — each interval is pushed/popped at most
 * once; each query triggers one heap poll in the worst case.<br>
 * <b>Space complexity:</b> O(n + q).
 *
 * <p><b>Key insight:</b> Sweeping queries left-to-right means intervals added to the heap
 * are monotonically eligible; stale (too-short) intervals are lazily evicted so the heap
 * top always represents the tightest valid cover.
 */
public class MinimumIntervalToIncludeEachQuery {

    public int[] minInterval(int[][] intervals, int[] queries) {
        // Sort intervals by start
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // Sort queries but remember original indices
        int q = queries.length;
        Integer[] queryIdx = new Integer[q];
        for (int i = 0; i < q; i++) queryIdx[i] = i;
        Arrays.sort(queryIdx, (a, b) -> queries[a] - queries[b]);

        // Min-heap: [intervalSize, intervalRight]
        // — smallest size first; we only need right to detect staleness
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        int[] result = new int[q];
        int i = 0; // pointer into sorted intervals

        for (int qi = 0; qi < q; qi++) {
            int origIdx = queryIdx[qi];
            int val = queries[origIdx];

            // Add all intervals that start at or before this query value
            while (i < intervals.length && intervals[i][0] <= val) {
                int size = intervals[i][1] - intervals[i][0] + 1;
                heap.offer(new int[]{size, intervals[i][1]});
                i++;
            }

            // Evict intervals whose right endpoint is before this query value
            while (!heap.isEmpty() && heap.peek()[1] < val) {
                heap.poll();
            }

            // The smallest remaining interval covers this query (or none does)
            result[origIdx] = heap.isEmpty() ? -1 : heap.peek()[0];
        }

        return result;
    }
}
