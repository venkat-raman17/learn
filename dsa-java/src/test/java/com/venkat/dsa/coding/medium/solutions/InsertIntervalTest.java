package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsertIntervalTest {

    private final InsertInterval sol = new InsertInterval();

    @Test
    void example1() {
        // intervals=[[1,3],[6,9]], newInterval=[2,5] -> [[1,5],[6,9]]
        int[][] result = sol.insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5});
        assertArrayEquals(new int[][]{{1, 5}, {6, 9}}, result);
    }

    @Test
    void example2() {
        // intervals=[[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval=[4,8]
        // -> [[1,2],[3,10],[12,16]]
        int[][] result = sol.insert(
                new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}},
                new int[]{4, 8});
        assertArrayEquals(new int[][]{{1, 2}, {3, 10}, {12, 16}}, result);
    }

    @Test
    void insertAtBeginning() {
        int[][] result = sol.insert(new int[][]{{3, 5}, {6, 9}}, new int[]{1, 2});
        assertArrayEquals(new int[][]{{1, 2}, {3, 5}, {6, 9}}, result);
    }

    @Test
    void insertAtEnd() {
        int[][] result = sol.insert(new int[][]{{1, 3}, {6, 9}}, new int[]{10, 12});
        assertArrayEquals(new int[][]{{1, 3}, {6, 9}, {10, 12}}, result);
    }

    @Test
    void mergeAll() {
        int[][] result = sol.insert(new int[][]{{1, 3}, {4, 6}}, new int[]{2, 5});
        assertArrayEquals(new int[][]{{1, 6}}, result);
    }

    @Test
    void emptyIntervals() {
        int[][] result = sol.insert(new int[][]{}, new int[]{5, 7});
        assertArrayEquals(new int[][]{{5, 7}}, result);
    }

    @Test
    void noOverlapExactAdjacent() {
        // newInterval ends exactly where existing starts — no overlap
        int[][] result = sol.insert(new int[][]{{3, 5}}, new int[]{1, 2});
        assertArrayEquals(new int[][]{{1, 2}, {3, 5}}, result);
    }

    @Test
    void newIntervalCoversAll() {
        int[][] result = sol.insert(new int[][]{{1, 2}, {3, 4}, {5, 6}}, new int[]{0, 10});
        assertArrayEquals(new int[][]{{0, 10}}, result);
    }
}
