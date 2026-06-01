package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchA2DMatrixTest {

    private final SearchA2DMatrix sol = new SearchA2DMatrix();

    // --- Official LeetCode examples ---

    @Test
    void example1_targetFound() {
        // matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3 => true
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };
        assertTrue(sol.searchMatrix(matrix, 3));
    }

    @Test
    void example2_targetNotFound() {
        // matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13 => false
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };
        assertFalse(sol.searchMatrix(matrix, 13));
    }

    // --- Edge cases ---

    @Test
    void singleCell_found() {
        assertTrue(sol.searchMatrix(new int[][]{{5}}, 5));
    }

    @Test
    void singleCell_notFound() {
        assertFalse(sol.searchMatrix(new int[][]{{5}}, 3));
    }

    @Test
    void targetAtTopLeft() {
        int[][] matrix = {{1, 2}, {3, 4}};
        assertTrue(sol.searchMatrix(matrix, 1));
    }

    @Test
    void targetAtBottomRight() {
        int[][] matrix = {{1, 2}, {3, 4}};
        assertTrue(sol.searchMatrix(matrix, 4));
    }

    @Test
    void targetSmallerThanMin() {
        int[][] matrix = {{2, 4}, {6, 8}};
        assertFalse(sol.searchMatrix(matrix, 1));
    }

    @Test
    void targetLargerThanMax() {
        int[][] matrix = {{2, 4}, {6, 8}};
        assertFalse(sol.searchMatrix(matrix, 9));
    }

    @Test
    void singleRow() {
        int[][] matrix = {{1, 3, 5, 7, 9}};
        assertTrue(sol.searchMatrix(matrix, 7));
        assertFalse(sol.searchMatrix(matrix, 4));
    }

    @Test
    void singleColumn() {
        int[][] matrix = {{1}, {3}, {5}, {7}};
        assertTrue(sol.searchMatrix(matrix, 5));
        assertFalse(sol.searchMatrix(matrix, 2));
    }
}
