package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedundantConnectionTest {

    private final RedundantConnection sol = new RedundantConnection();

    @Test
    void example1() {
        // [[1,2],[1,3],[2,3]] -> [2,3] creates the cycle (last edge forming 1-2-3 triangle)
        assertArrayEquals(new int[]{2, 3},
                sol.findRedundantConnection(new int[][]{{1,2},{1,3},{2,3}}));
    }

    @Test
    void example2() {
        // [[1,2],[2,3],[3,4],[1,4],[1,5]] -> [1,4] is redundant
        assertArrayEquals(new int[]{1, 4},
                sol.findRedundantConnection(new int[][]{{1,2},{2,3},{3,4},{1,4},{1,5}}));
    }

    @Test
    void simpleThreeNodeCycle() {
        // Triangle: [1,2],[2,3],[1,3] -> last edge to complete triangle is [1,3]
        assertArrayEquals(new int[]{1, 3},
                sol.findRedundantConnection(new int[][]{{1,2},{2,3},{1,3}}));
    }

    @Test
    void directLoop_sameNodeTwice() {
        // [1,2],[2,3],[3,1]: cycle 1->2->3->1; last edge [3,1] is redundant
        assertArrayEquals(new int[]{3, 1},
                sol.findRedundantConnection(new int[][]{{1,2},{2,3},{3,1}}));
    }

    @Test
    void linearThenClose() {
        // [1,2],[2,3],[3,4],[4,5],[5,1]: last edge [5,1] closes the cycle
        assertArrayEquals(new int[]{5, 1},
                sol.findRedundantConnection(new int[][]{{1,2},{2,3},{3,4},{4,5},{5,1}}));
    }

    @Test
    void twoNodesRedundant() {
        // [1,2],[2,1] — but problem constraints say it won't have duplicate edges;
        // still: nodes 1 and 2, edge [2,1] is the redundant one
        // (problem guarantees no multi-edges, but let's verify DSU handles same-root detection)
        assertArrayEquals(new int[]{2, 1},
                sol.findRedundantConnection(new int[][]{{1,2},{2,1}}));
    }
}
