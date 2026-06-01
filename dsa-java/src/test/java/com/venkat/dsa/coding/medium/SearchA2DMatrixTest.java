package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SearchA2DMatrixTest {

    private final SearchA2DMatrix solution = new SearchA2DMatrix();

    @Test
    public void testTargetExists() {
        int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        assertTrue(solution.searchMatrix(matrix, 3));
    }

    @Test
    public void testTargetNotExists() {
        int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        assertFalse(solution.searchMatrix(matrix, 13));
    }

    @Test
    public void testSingleCell() {
        int[][] matrix = {{1}};
        assertTrue(solution.searchMatrix(matrix, 1));
    }
}
