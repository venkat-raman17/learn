package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Redundant Connection
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/redundant-connection/
 *
 * <p>In a graph with n nodes (labeled 1..n) that started as a tree with one extra edge added,
 * return the edge that can be removed so the result is a tree. If multiple answers exist, return
 * the one that appears last in the input.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == edges.length</li>
 *   <li>3 <= n <= 1000</li>
 *   <li>edges[i].length == 2</li>
 *   <li>1 <= a, b <= n, a != b</li>
 *   <li>No repeated edges</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: edges = [[1,2],[1,3],[2,3]]
 * Output: [2,3]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
 * Output: [1,4]
 * </pre>
 *
 * <p>Target: Time O(n * alpha(n)) with path compression, Space O(n)
 *
 * <p>Hint 1: Use Union-Find (DSU) — process edges in order; the first edge whose two nodes already share a root is the answer.
 * <p>Hint 2: With path compression and union by rank, each find/union is nearly O(1).
 */
public class RedundantConnection {

    public int[] findRedundantConnection(int[][] edges) {
        throw new UnsupportedOperationException("implement me");
    }
}
