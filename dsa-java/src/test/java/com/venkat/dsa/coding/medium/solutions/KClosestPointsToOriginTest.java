package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class KClosestPointsToOriginTest {

    private final KClosestPointsToOrigin sol = new KClosestPointsToOrigin();

    // Helper: sort result rows so order-independent comparison works
    private int[][] sortPoints(int[][] pts) {
        Arrays.sort(pts, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        return pts;
    }

    // Official example 1: [[1,3],[-2,2]], k=1 -> [[-2,2]]
    // distances^2: (1+9)=10, (4+4)=8 -> closest is [-2,2]
    @Test
    void testOfficialExample1() {
        int[][] result = sol.kClosest(new int[][]{{1, 3}, {-2, 2}}, 1);
        assertArrayEquals(new int[]{-2, 2}, result[0]);
    }

    // Official example 2: [[3,3],[5,-1],[-2,4]], k=2 -> [[3,3],[-2,4]]
    // distances^2: 18, 26, 20 -> two closest: 18([3,3]) and 20([-2,4])
    @Test
    void testOfficialExample2() {
        int[][] result = sortPoints(sol.kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2));
        assertEquals(2, result.length);
        // Expected after sort: [-2,4] and [3,3]
        assertArrayEquals(new int[]{-2, 4}, result[0]);
        assertArrayEquals(new int[]{3, 3}, result[1]);
    }

    // k equals total points -> return all
    @Test
    void testKEqualsLength() {
        int[][] points = {{1, 1}, {2, 2}};
        int[][] result = sol.kClosest(points, 2);
        assertEquals(2, result.length);
    }

    // Origin point included
    @Test
    void testOriginPoint() {
        int[][] result = sol.kClosest(new int[][]{{0, 0}, {1, 1}, {2, 2}}, 1);
        assertArrayEquals(new int[]{0, 0}, result[0]);
    }

    // Negative coordinates
    @Test
    void testNegativeCoordinates() {
        int[][] result = sol.kClosest(new int[][]{{-1, -1}, {-5, -5}}, 1);
        assertArrayEquals(new int[]{-1, -1}, result[0]);
    }

    // Single point
    @Test
    void testSinglePoint() {
        int[][] result = sol.kClosest(new int[][]{{3, 4}}, 1);
        assertArrayEquals(new int[]{3, 4}, result[0]);
    }
}
