package com.venkat.dsa.algorithms.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GraphTraversal}.
 *
 * <p>All graphs are built deterministically so expected visit orders are fixed.
 * Naming convention: {@code <method>_<scenario>_<expected behaviour>}.
 */
class GraphTraversalTest {

    // -------------------------------------------------------------------------
    // Helper to build an adjacency list quickly
    // -------------------------------------------------------------------------

    /**
     * Builds an adjacency map. Pairs are interpreted as directed edges: adj[pairs[i]] -> pairs[i+1].
     * For undirected graphs, add edges in both directions explicitly.
     */
    private static Map<Integer, List<Integer>> adj(int... pairs) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.computeIfAbsent(pairs[i], k -> new ArrayList<>()).add(pairs[i + 1]);
        }
        return map;
    }

    /**
     * Returns a small undirected graph with 5 nodes and the following edges:
     * 0-1, 0-2, 1-3, 2-4.
     *
     * <pre>
     *     0
     *    / \
     *   1   2
     *   |   |
     *   3   4
     * </pre>
     *
     * Adjacency lists (insertion order):
     *   0 -> [1, 2]
     *   1 -> [0, 3]
     *   2 -> [0, 4]
     *   3 -> [1]
     *   4 -> [2]
     */
    private static Map<Integer, List<Integer>> treeGraph() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        // 0 -> 1, 2
        g.put(0, list(1, 2));
        // 1 -> 0, 3
        g.put(1, list(0, 3));
        // 2 -> 0, 4
        g.put(2, list(0, 4));
        // 3 -> 1
        g.put(3, list(1));
        // 4 -> 2
        g.put(4, list(2));
        return g;
    }

    private static List<Integer> list(int... values) {
        List<Integer> l = new ArrayList<>();
        for (int v : values) l.add(v);
        return l;
    }

    // =========================================================================
    // BFS tests
    // =========================================================================

    @Test
    void bfs_treeGraph_returnsLevelOrder() {
        // BFS from 0: 0, then level-1 = 1,2, then level-2 = 3,4
        List<Integer> result = GraphTraversal.bfs(treeGraph(), 0);
        assertEquals(List.of(0, 1, 2, 3, 4), result);
    }

    @Test
    void bfs_singleIsolatedNode_returnsOnlyThatNode() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(42, new ArrayList<>());
        List<Integer> result = GraphTraversal.bfs(g, 42);
        assertEquals(List.of(42), result);
    }

    @Test
    void bfs_startNodeNotInMap_returnsOnlyStartNode() {
        // Node 99 has no entry in the adjacency map — treat as isolated.
        Map<Integer, List<Integer>> g = new HashMap<>();
        List<Integer> result = GraphTraversal.bfs(g, 99);
        assertEquals(List.of(99), result);
    }

    @Test
    void bfs_linearChain_returnsChainOrder() {
        // Directed chain: 0 -> 1 -> 2 -> 3
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, list(3));
        g.put(3, new ArrayList<>());
        List<Integer> result = GraphTraversal.bfs(g, 0);
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void bfs_cycleGraph_doesNotLoopInfinitely() {
        // Directed cycle: 0 -> 1 -> 2 -> 0
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, list(0));
        List<Integer> result = GraphTraversal.bfs(g, 0);
        assertEquals(List.of(0, 1, 2), result);
    }

    @Test
    void bfs_startFromMiddle_onlyReachableNodes() {
        // Directed: 0 -> 1 -> 2; starting from 1 should NOT visit 0
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, new ArrayList<>());
        List<Integer> result = GraphTraversal.bfs(g, 1);
        assertEquals(List.of(1, 2), result);
    }

    @Test
    void bfs_selfLoop_nodeVisitedOnce() {
        // Node with a self-loop: 0 -> 0
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(0));
        List<Integer> result = GraphTraversal.bfs(g, 0);
        assertEquals(List.of(0), result);
    }

    @Test
    void bfs_nullAdj_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> GraphTraversal.bfs(null, 0));
    }

    @Test
    void bfs_diamondGraph_returnsCorrectLevelOrder() {
        // Diamond: 0 -> 1, 0 -> 2; 1 -> 3; 2 -> 3
        // BFS from 0: 0, 1, 2, 3
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1, 2));
        g.put(1, list(3));
        g.put(2, list(3));
        g.put(3, new ArrayList<>());
        List<Integer> result = GraphTraversal.bfs(g, 0);
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    // =========================================================================
    // DFS iterative tests
    // =========================================================================

    @Test
    void dfsIterative_treeGraph_returnsDepthFirstOrder() {
        // Expected: 0, 1, 3, 2, 4  (pre-order, first neighbour first)
        List<Integer> result = GraphTraversal.dfsIterative(treeGraph(), 0);
        assertEquals(List.of(0, 1, 3, 2, 4), result);
    }

    @Test
    void dfsIterative_singleIsolatedNode_returnsOnlyThatNode() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(7, new ArrayList<>());
        List<Integer> result = GraphTraversal.dfsIterative(g, 7);
        assertEquals(List.of(7), result);
    }

    @Test
    void dfsIterative_startNodeNotInMap_returnsOnlyStartNode() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        List<Integer> result = GraphTraversal.dfsIterative(g, 5);
        assertEquals(List.of(5), result);
    }

    @Test
    void dfsIterative_linearChain_returnsChainOrder() {
        // Directed chain: 0 -> 1 -> 2 -> 3
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, list(3));
        g.put(3, new ArrayList<>());
        List<Integer> result = GraphTraversal.dfsIterative(g, 0);
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void dfsIterative_cycleGraph_doesNotLoopInfinitely() {
        // Directed cycle: 0 -> 1 -> 2 -> 0
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, list(0));
        List<Integer> result = GraphTraversal.dfsIterative(g, 0);
        assertEquals(List.of(0, 1, 2), result);
    }

    @Test
    void dfsIterative_selfLoop_nodeVisitedOnce() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(0));
        List<Integer> result = GraphTraversal.dfsIterative(g, 0);
        assertEquals(List.of(0), result);
    }

    @Test
    void dfsIterative_nullAdj_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> GraphTraversal.dfsIterative(null, 0));
    }

    @Test
    void dfsIterative_diamondGraph_returnsDepthFirstOrder() {
        // Diamond: 0 -> 1, 0 -> 2; 1 -> 3; 2 -> 3
        // Iterative DFS from 0:
        //   push 0. pop 0 (visit), push 2,1 in reverse order → push 2 then 1, top=1
        //   pop 1 (visit), push 3 (only unvisited neighbour). top=3
        //   pop 3 (visit), no unvisited neighbours. stack=[2]
        //   pop 2 (visit), neighbour 3 already visited.
        // Order: [0, 1, 3, 2]
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1, 2));
        g.put(1, list(3));
        g.put(2, list(3));
        g.put(3, new ArrayList<>());
        List<Integer> result = GraphTraversal.dfsIterative(g, 0);
        assertEquals(List.of(0, 1, 3, 2), result);
    }

    // =========================================================================
    // DFS recursive tests
    // =========================================================================

    @Test
    void dfsRecursive_treeGraph_returnsDepthFirstOrder() {
        // Same expected order as iterative: 0, 1, 3, 2, 4
        List<Integer> result = GraphTraversal.dfsRecursive(treeGraph(), 0);
        assertEquals(List.of(0, 1, 3, 2, 4), result);
    }

    @Test
    void dfsRecursive_singleIsolatedNode_returnsOnlyThatNode() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(9, new ArrayList<>());
        List<Integer> result = GraphTraversal.dfsRecursive(g, 9);
        assertEquals(List.of(9), result);
    }

    @Test
    void dfsRecursive_startNodeNotInMap_returnsOnlyStartNode() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        List<Integer> result = GraphTraversal.dfsRecursive(g, 100);
        assertEquals(List.of(100), result);
    }

    @Test
    void dfsRecursive_linearChain_returnsChainOrder() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, list(3));
        g.put(3, new ArrayList<>());
        List<Integer> result = GraphTraversal.dfsRecursive(g, 0);
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void dfsRecursive_cycleGraph_doesNotLoopInfinitely() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1));
        g.put(1, list(2));
        g.put(2, list(0));
        List<Integer> result = GraphTraversal.dfsRecursive(g, 0);
        assertEquals(List.of(0, 1, 2), result);
    }

    @Test
    void dfsRecursive_selfLoop_nodeVisitedOnce() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(0));
        List<Integer> result = GraphTraversal.dfsRecursive(g, 0);
        assertEquals(List.of(0), result);
    }

    @Test
    void dfsRecursive_nullAdj_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> GraphTraversal.dfsRecursive(null, 0));
    }

    @Test
    void dfsRecursive_diamondGraph_returnsDepthFirstOrder() {
        // Same expected order as iterative: [0, 1, 3, 2]
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, list(1, 2));
        g.put(1, list(3));
        g.put(2, list(3));
        g.put(3, new ArrayList<>());
        List<Integer> result = GraphTraversal.dfsRecursive(g, 0);
        assertEquals(List.of(0, 1, 3, 2), result);
    }

    // =========================================================================
    // Cross-method consistency tests
    // =========================================================================

    @Test
    void dfsIterativeAndRecursive_treeGraph_produceSameOrder() {
        Map<Integer, List<Integer>> g = treeGraph();
        assertEquals(
            GraphTraversal.dfsRecursive(g, 0),
            GraphTraversal.dfsIterative(g, 0)
        );
    }

    @Test
    void allThreeMethods_singleNode_allReturnSingletonList() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, new ArrayList<>());
        assertEquals(List.of(0), GraphTraversal.bfs(g, 0));
        assertEquals(List.of(0), GraphTraversal.dfsIterative(g, 0));
        assertEquals(List.of(0), GraphTraversal.dfsRecursive(g, 0));
    }
}
