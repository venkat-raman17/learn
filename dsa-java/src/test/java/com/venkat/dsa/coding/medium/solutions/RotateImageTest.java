package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RotateImageTest {

    private final RotateImage sol = new RotateImage();

    @Test
    void example1_3x3() {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        sol.rotate(matrix);
        int[][] expected = {
            {7, 4, 1},
            {8, 5, 2},
            {9, 6, 3}
        };
        assertArrayEquals(expected, matrix);
    }

    @Test
    void example2_4x4() {
        int[][] matrix = {
            { 5,  1,  9, 11},
            { 2,  4,  8, 10},
            {13,  3,  6,  7},
            {15, 14, 12, 16}
        };
        sol.rotate(matrix);
        int[][] expected = {
            {15, 13,  2,  5},
            {14,  3,  4,  1},
            {12,  6,  8,  9},
            {16,  7, 10, 11}
        };
        assertArrayEquals(expected, matrix);
    }

    @Test
    void singleElement() {
        int[][] matrix = {{42}};
        sol.rotate(matrix);
        assertArrayEquals(new int[][]{{42}}, matrix);
    }

    @Test
    void twoByTwo() {
        int[][] matrix = {{1, 2}, {3, 4}};
        sol.rotate(matrix);
        // After transpose: [[1,3],[2,4]], after row-reverse: [[3,1],[4,2]]
        assertArrayEquals(new int[][]{{3, 1}, {4, 2}}, matrix);
    }
}
