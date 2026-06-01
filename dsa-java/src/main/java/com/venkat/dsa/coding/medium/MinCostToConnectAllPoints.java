package com.venkat.dsa.coding.medium;

import java.util.*;

/**
 * NeetCode / LeetCode — Min Cost to Connect All Points
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Advanced Graphs
 * <p>URL: https://leetcode.com/problems/min-cost-to-connect-all-points/
 *
 * <p>Given an array of {@code points} where {@code points[i] = [xi, yi]} represents
 * a point on a 2-D plane, return the minimum cost to connect all points. The cost
 * of connecting two points is the Manhattan distance between them. You may only
 * connect two points directly; no intermediate points are needed.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= points.length &lt;= 1000</li>
 *   <li>-10^6 &lt;= xi, yi &lt;= 10^6</li>
 *   <li>All pairs (xi, yi) are distinct.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
 *   Output: 20
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  points = [[3,12],[-2,5],[-4,1]]
 *   Output: 18
 * </pre>
 *
 * <p>Target Time: O(N^2 log N) &nbsp; Space: O(N^2)
 *
 * <p>Hint 1: Build a complete graph where edge weight = Manhattan distance,
 *            then find the Minimum Spanning Tree (MST).
 * <p>Hint 2: Prim's algorithm with a min-heap, or Kruskal's with union-find,
 *            both work; Prim's is simpler here since the graph is dense.
 */
public class MinCostToConnectAllPoints {

    /**
     * Returns the minimum cost (sum of Manhattan distances) to connect all points
     * into a single connected component.
     *
     * @param points array of [x, y] coordinates
     * @return minimum total cost
     */
    public int minCostConnectPoints(int[][] points) {
        throw new UnsupportedOperationException("implement me");
    }
}
