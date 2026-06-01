package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MinCostToConnectAllPointsTest {

    private final MinCostToConnectAllPoints solution = new MinCostToConnectAllPoints();

    @Test
    public void testExample1() {
        int[][] points = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        assertEquals(20, solution.minCostConnectPoints(points));
    }

    @Test
    public void testExample2() {
        int[][] points = {{3, 12}, {-2, 5}, {-4, 1}};
        assertEquals(18, solution.minCostConnectPoints(points));
    }

    @Test
    public void testSinglePoint() {
        int[][] points = {{0, 0}};
        assertEquals(0, solution.minCostConnectPoints(points));
    }
}
