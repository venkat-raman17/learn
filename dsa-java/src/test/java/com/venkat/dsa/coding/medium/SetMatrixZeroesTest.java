package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SetMatrixZeroesTest {

    private final SetMatrixZeroes solution = new SetMatrixZeroes();

    @Test
    public void testSingleZero() {
        int[][] matrix = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
        solution.setZeroes(matrix);
        assertArrayEquals(new int[][]{{1, 0, 1}, {0, 0, 0}, {1, 0, 1}}, matrix);
    }

    @Test
    public void testMultipleZeroes() {
        int[][] matrix = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        solution.setZeroes(matrix);
        assertArrayEquals(new int[][]{{0, 0, 0, 0}, {0, 4, 5, 0}, {0, 3, 1, 0}}, matrix);
    }

    @Test
    public void testNoZeroes() {
        int[][] matrix = {{1, 2}, {3, 4}};
        solution.setZeroes(matrix);
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, matrix);
    }
}
