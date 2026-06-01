package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinCostConnectAllPointsTest {

    private final MinCostConnectAllPoints sol = new MinCostConnectAllPoints();

    @Test
    void testExample1() {
        // LeetCode example 1
        int[][] points = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        assertEquals(20, sol.minCostConnectPoints(points));
    }

    @Test
    void testExample2() {
        // LeetCode example 2: all edges cost 0
        int[][] points = {{3, 12}, {-2, 5}, {-4, 1}};
        assertEquals(18, sol.minCostConnectPoints(points));
    }

    @Test
    void testSinglePoint() {
        int[][] points = {{0, 0}};
        assertEquals(0, sol.minCostConnectPoints(points));
    }

    @Test
    void testTwoPoints() {
        int[][] points = {{0, 0}, {1, 1}};
        // Manhattan distance = |0-1| + |0-1| = 2
        assertEquals(2, sol.minCostConnectPoints(points));
    }

    @Test
    void testAllSamePoint() {
        // All points identical => all edge costs = 0
        int[][] points = {{1, 1}, {1, 1}, {1, 1}};
        assertEquals(0, sol.minCostConnectPoints(points));
    }

    @Test
    void testLinearPoints() {
        // Points on a line: (0,0),(1,0),(2,0),(3,0)
        // MST edges: 1+1+1 = 3
        int[][] points = {{0, 0}, {1, 0}, {2, 0}, {3, 0}};
        assertEquals(3, sol.minCostConnectPoints(points));
    }
}
