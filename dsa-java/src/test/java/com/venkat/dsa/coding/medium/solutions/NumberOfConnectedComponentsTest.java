package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumberOfConnectedComponentsTest {

    private final NumberOfConnectedComponents sol = new NumberOfConnectedComponents();

    @Test
    void example1_twoComponents() {
        // n=5, [[0,1],[1,2],[3,4]] -> components: {0,1,2}, {3,4} -> 2
        assertEquals(2, sol.countComponents(5, new int[][]{{0,1},{1,2},{3,4}}));
    }

    @Test
    void example2_oneComponent() {
        // n=5, [[0,1],[1,2],[2,3],[3,4]] -> all connected -> 1
        assertEquals(1, sol.countComponents(5, new int[][]{{0,1},{1,2},{2,3},{3,4}}));
    }

    @Test
    void noEdges_allIsolated() {
        assertEquals(4, sol.countComponents(4, new int[0][]));
    }

    @Test
    void singleNode() {
        assertEquals(1, sol.countComponents(1, new int[0][]));
    }

    @Test
    void fullyConnected_triangle() {
        // n=3, 0-1, 1-2, 0-2 -> 1 component
        assertEquals(1, sol.countComponents(3, new int[][]{{0,1},{1,2},{0,2}}));
    }

    @Test
    void twoDisjointPairs() {
        // n=4: {0,1}, {2,3} -> 2 components
        assertEquals(2, sol.countComponents(4, new int[][]{{0,1},{2,3}}));
    }

    @Test
    void starGraph() {
        // n=5: 0 connected to all others -> 1 component
        assertEquals(1, sol.countComponents(5, new int[][]{{0,1},{0,2},{0,3},{0,4}}));
    }

    @Test
    void threeIsolatedPlusOneConnected() {
        // n=6: {0,1,2} fully connected, nodes 3,4,5 isolated -> 4 components
        assertEquals(4, sol.countComponents(6, new int[][]{{0,1},{1,2},{0,2}}));
    }
}
