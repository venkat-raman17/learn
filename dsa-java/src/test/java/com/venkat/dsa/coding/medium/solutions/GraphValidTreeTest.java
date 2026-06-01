package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphValidTreeTest {

    private final GraphValidTree sol = new GraphValidTree();

    @Test
    void example1_validTree() {
        // n=5, edges=[[0,1],[0,2],[0,3],[1,4]] -> true
        assertTrue(sol.validTree(5, new int[][]{{0,1},{0,2},{0,3},{1,4}}));
    }

    @Test
    void example2_hasCycle() {
        // n=5, edges=[[0,1],[1,2],[2,3],[1,3],[1,4]] -> false (cycle 1-2-3-1)
        assertFalse(sol.validTree(5, new int[][]{{0,1},{1,2},{2,3},{1,3},{1,4}}));
    }

    @Test
    void singleNode_noEdges() {
        assertTrue(sol.validTree(1, new int[0][]));
    }

    @Test
    void twoNodes_oneEdge() {
        assertTrue(sol.validTree(2, new int[][]{{0,1}}));
    }

    @Test
    void twoNodes_noEdges_disconnected() {
        // n=2, 0 edges -> two separate nodes, not connected -> not a tree
        assertFalse(sol.validTree(2, new int[0][]));
    }

    @Test
    void twoNodes_twoEdges_extraEdge() {
        // n=2, 2 edges -> extra edge means not n-1 edges, fails immediately
        assertFalse(sol.validTree(2, new int[][]{{0,1},{0,1}}));
    }

    @Test
    void linearChain_validTree() {
        // 0-1-2-3 -> valid tree
        assertTrue(sol.validTree(4, new int[][]{{0,1},{1,2},{2,3}}));
    }

    @Test
    void disconnectedGraph_enoughEdges() {
        // n=4, edges: [0-1],[2-3] -> 2 edges but 2 components, not a tree
        assertFalse(sol.validTree(4, new int[][]{{0,1},{2,3}}));
    }
}
