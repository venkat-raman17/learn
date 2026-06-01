package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class GraphValidTreeTest {

    private final GraphValidTree solution = new GraphValidTree();

    @Test
    public void testValidTree() {
        assertTrue(solution.validTree(5, new int[][]{{0,1},{0,2},{0,3},{1,4}}));
    }

    @Test
    public void testCycleInGraph() {
        assertFalse(solution.validTree(5, new int[][]{{0,1},{1,2},{2,3},{1,3},{1,4}}));
    }

    @Test
    public void testSingleNode() {
        assertTrue(solution.validTree(1, new int[][]{}));
    }
}
