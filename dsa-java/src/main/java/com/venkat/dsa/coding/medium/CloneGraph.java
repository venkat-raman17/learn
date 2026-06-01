package com.venkat.dsa.coding.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * NeetCode / LeetCode — Clone Graph
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/clone-graph/
 *
 * <p>Given a reference to a node in a connected undirected graph, return a deep copy (clone) of the graph.
 * Each node contains a value and a list of its neighbors.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes is in the range [0, 100]</li>
 *   <li>1 <= Node.val <= 100</li>
 *   <li>Node.val is unique for each node</li>
 *   <li>There are no repeated edges and no self-loops</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 * Output: [[2,4],[1,3],[2,4],[1,3]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: adjList = [[]]
 * Output: [[]]
 * </pre>
 *
 * <p>Target: Time O(V+E), Space O(V)
 *
 * <p>Hint 1: Use a HashMap to map original nodes to their clones, preventing infinite loops.
 * <p>Hint 2: DFS/BFS over the graph — if a neighbor's clone already exists in the map, reuse it.
 */
public class CloneGraph {

    public static class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }

        public Node(int val) {
            this.val = val;
            neighbors = new ArrayList<>();
        }

        public Node(int val, List<Node> neighbors) {
            this.val = val;
            this.neighbors = neighbors;
        }
    }

    public Node cloneGraph(Node node) {
        throw new UnsupportedOperationException("implement me");
    }
}
