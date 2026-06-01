package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Number of Connected Components in an Undirected Graph
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/
 *
 * <p>Given n nodes and a list of undirected edges, return the number of connected components in the graph.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= n <= 2000</li>
 *   <li>0 <= edges.length <= 5000</li>
 *   <li>edges[i].length == 2</li>
 *   <li>0 <= a, b < n, a != b</li>
 *   <li>No repeated edges</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: n = 5, edges = [[0,1],[1,2],[3,4]]
 * Output: 2
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]
 * Output: 1
 * </pre>
 *
 * <p>Target: Time O(V+E), Space O(V+E)
 *
 * <p>Hint 1: Union-Find: start with n components, decrement each time two different components are merged.
 * <p>Hint 2: Alternatively, DFS/BFS from each unvisited node, counting the number of traversal starts.
 */
public class NumberOfConnectedComponents {

    public int countComponents(int n, int[][] edges) {
        throw new UnsupportedOperationException("implement me");
    }
}
