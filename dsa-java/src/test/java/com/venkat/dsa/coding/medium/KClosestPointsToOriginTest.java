package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Comparator;

@Disabled("practice — delete when you start")
public class KClosestPointsToOriginTest {

    private final KClosestPointsToOrigin solution = new KClosestPointsToOrigin();

    private int[][] sorted(int[][] pts) {
        int[][] copy = pts.clone();
        Arrays.sort(copy, Comparator.comparingInt(p -> p[0] * p[0] + p[1] * p[1]));
        return copy;
    }

    @Test
    void testExample1() {
        int[][] result = solution.kClosest(new int[][]{{1, 3}, {-2, 2}}, 1);
        assertArrayEquals(new int[][]{{-2, 2}}, sorted(result));
    }

    @Test
    void testExample2() {
        int[][] result = solution.kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2);
        int[][] expected = sorted(new int[][]{{3, 3}, {-2, 4}});
        assertArrayEquals(expected, sorted(result));
    }

    @Test
    void testSinglePoint() {
        int[][] result = solution.kClosest(new int[][]{{0, 0}}, 1);
        assertArrayEquals(new int[][]{{0, 0}}, result);
    }
}
