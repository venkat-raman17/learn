package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SetMatrixZeroesTest {

    private final SetMatrixZeroes sol = new SetMatrixZeroes();

    @Test
    void example1_3x3() {
        int[][] matrix = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
        };
        sol.setZeroes(matrix);
        int[][] expected = {
            {1, 0, 1},
            {0, 0, 0},
            {1, 0, 1}
        };
        assertArrayEquals(expected, matrix);
    }

    @Test
    void example2_3x4() {
        int[][] matrix = {
            {0, 1, 2, 0},
            {3, 4, 5, 2},
            {1, 3, 1, 5}
        };
        sol.setZeroes(matrix);
        int[][] expected = {
            {0, 0, 0, 0},
            {0, 4, 5, 0},
            {0, 3, 1, 0}
        };
        assertArrayEquals(expected, matrix);
    }

    @Test
    void noZeros() {
        int[][] matrix = {{1, 2}, {3, 4}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, matrix);
    }

    @Test
    void allZeros() {
        int[][] matrix = {{0, 0}, {0, 0}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{0, 0}, {0, 0}}, matrix);
    }

    @Test
    void zeroInFirstRow() {
        int[][] matrix = {{1, 0, 3}, {4, 5, 6}};
        sol.setZeroes(matrix);
        // col 1 and entire first row zeroed
        assertArrayEquals(new int[][]{{0, 0, 0}, {4, 0, 6}}, matrix);
    }

    @Test
    void zeroInFirstCol() {
        int[][] matrix = {{1, 2}, {0, 4}, {5, 6}};
        sol.setZeroes(matrix);
        // row 1 zeroed, col 0 zeroed
        assertArrayEquals(new int[][]{{0, 2}, {0, 0}, {0, 6}}, matrix);
    }

    @Test
    void singleCell_zero() {
        int[][] matrix = {{0}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{0}}, matrix);
    }

    @Test
    void singleCell_nonZero() {
        int[][] matrix = {{5}};
        sol.setZeroes(matrix);
        assertArrayEquals(new int[][]{{5}}, matrix);
    }
}
