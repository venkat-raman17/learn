package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpiralMatrixTest {

    private final SpiralMatrix sol = new SpiralMatrix();

    @Test
    void example1_3x3() {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        assertEquals(Arrays.asList(1, 2, 3, 6, 9, 8, 7, 4, 5), sol.spiralOrder(matrix));
    }

    @Test
    void example2_3x4() {
        int[][] matrix = {
            {1,  2,  3,  4},
            {5,  6,  7,  8},
            {9, 10, 11, 12}
        };
        assertEquals(Arrays.asList(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7), sol.spiralOrder(matrix));
    }

    @Test
    void singleRow() {
        int[][] matrix = {{1, 2, 3, 4}};
        assertEquals(Arrays.asList(1, 2, 3, 4), sol.spiralOrder(matrix));
    }

    @Test
    void singleColumn() {
        int[][] matrix = {{1}, {2}, {3}};
        assertEquals(Arrays.asList(1, 2, 3), sol.spiralOrder(matrix));
    }

    @Test
    void singleElement() {
        int[][] matrix = {{7}};
        assertEquals(List.of(7), sol.spiralOrder(matrix));
    }

    @Test
    void twoByTwo() {
        int[][] matrix = {{1, 2}, {3, 4}};
        assertEquals(Arrays.asList(1, 2, 4, 3), sol.spiralOrder(matrix));
    }

    @Test
    void tallMatrix_4x1() {
        int[][] matrix = {{1}, {2}, {3}, {4}};
        assertEquals(Arrays.asList(1, 2, 3, 4), sol.spiralOrder(matrix));
    }
}
