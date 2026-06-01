package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MergeIntervalsTest {

    private final MergeIntervals sol = new MergeIntervals();

    @Test
    void example1() {
        // [[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]]
        int[][] result = sol.merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
        assertArrayEquals(new int[][]{{1, 6}, {8, 10}, {15, 18}}, result);
    }

    @Test
    void example2() {
        // [[1,4],[4,5]] -> [[1,5]] (touching endpoints merge)
        int[][] result = sol.merge(new int[][]{{1, 4}, {4, 5}});
        assertArrayEquals(new int[][]{{1, 5}}, result);
    }

    @Test
    void single() {
        int[][] result = sol.merge(new int[][]{{1, 4}});
        assertArrayEquals(new int[][]{{1, 4}}, result);
    }

    @Test
    void allMergeIntoOne() {
        int[][] result = sol.merge(new int[][]{{1, 10}, {2, 6}, {3, 8}});
        assertArrayEquals(new int[][]{{1, 10}}, result);
    }

    @Test
    void noMerge() {
        int[][] result = sol.merge(new int[][]{{1, 2}, {3, 4}, {5, 6}});
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}, {5, 6}}, result);
    }

    @Test
    void unsortedInput() {
        // Unsorted input should still be handled correctly after sort
        int[][] result = sol.merge(new int[][]{{15, 18}, {1, 3}, {2, 6}, {8, 10}});
        assertArrayEquals(new int[][]{{1, 6}, {8, 10}, {15, 18}}, result);
    }

    @Test
    void containedInterval() {
        // [1,10] contains [2,5]
        int[][] result = sol.merge(new int[][]{{1, 10}, {2, 5}});
        assertArrayEquals(new int[][]{{1, 10}}, result);
    }
}
