package com.venkat.dsa.coding.medium;

import java.util.*;

/**
 * NeetCode / LeetCode — Network Delay Time
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Advanced Graphs
 * <p>URL: https://leetcode.com/problems/network-delay-time/
 *
 * <p>Given a network of {@code n} nodes labeled 1 to n, and a list of travel times
 * as directed edges {@code times[i] = [u, v, w]}, find the minimum time for all
 * nodes to receive a signal sent from node {@code k}. Return -1 if it is impossible.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= k &lt;= n &lt;= 100</li>
 *   <li>1 &lt;= times.length &lt;= 6000</li>
 *   <li>times[i].length == 3</li>
 *   <li>1 &lt;= u, v &lt;= n; u != v</li>
 *   <li>0 &lt;= w &lt;= 100</li>
 *   <li>All (u, v) pairs are unique.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 *   Output: 2
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  times = [[1,2,1]], n = 2, k = 1
 *   Output: 1
 * </pre>
 *
 * <p>Target Time: O((E + V) log V) &nbsp; Space: O(V + E)
 *
 * <p>Hint 1: Model as a shortest-path problem — Dijkstra from source {@code k}.
 * <p>Hint 2: The answer is the maximum distance in the shortest-path array;
 *            if any node is unreachable (distance still infinity), return -1.
 */
public class NetworkDelayTime {

    /**
     * Returns the minimum time for all n nodes to receive the signal from node k,
     * or -1 if not all nodes can be reached.
     *
     * @param times directed edges [u, v, w]
     * @param n     total number of nodes (labeled 1..n)
     * @param k     source node
     * @return minimum delay time, or -1
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
