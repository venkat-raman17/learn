package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LongestIncreasingPathInAMatrixTest {

    private final LongestIncreasingPathInAMatrix sol = new LongestIncreasingPathInAMatrix();

    // LeetCode example 1:
    // [[9,9,4],[6,6,8],[2,1,1]] -> 4 (path: 1->2->6->9)
    @Test
    void example1() {
        int[][] matrix = {{9,9,4},{6,6,8},{2,1,1}};
        assertEquals(4, sol.longestIncreasingPath(matrix));
    }

    // LeetCode example 2:
    // [[3,4,5],[3,2,6],[2,2,1]] -> 4 (path: 3->4->5->6)
    @Test
    void example2() {
        int[][] matrix = {{3,4,5},{3,2,6},{2,2,1}};
        assertEquals(4, sol.longestIncreasingPath(matrix));
    }

    // LeetCode example 3: [[1]] -> 1
    @Test
    void singleCell() {
        assertEquals(1, sol.longestIncreasingPath(new int[][]{{1}}));
    }

    // All same values: no increasing path beyond 1
    @Test
    void allSame() {
        int[][] matrix = {{3,3},{3,3}};
        assertEquals(1, sol.longestIncreasingPath(matrix));
    }

    // Strictly increasing row: [1,2,3,4] -> 4
    @Test
    void singleRow() {
        int[][] matrix = {{1,2,3,4}};
        assertEquals(4, sol.longestIncreasingPath(matrix));
    }

    // Strictly increasing column: [[1],[2],[3]] -> 3
    @Test
    void singleColumn() {
        int[][] matrix = {{1},{2},{3}};
        assertEquals(3, sol.longestIncreasingPath(matrix));
    }

    // 2x2 distinct ascending spiral: [[1,2],[4,3]] -> 4 (1->2->3->4)
    @Test
    void twoByTwo() {
        int[][] matrix = {{1,2},{4,3}};
        assertEquals(4, sol.longestIncreasingPath(matrix));
    }
}
