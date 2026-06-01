package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SpiralMatrixTest {

    private final SpiralMatrix solution = new SpiralMatrix();

    @Test
    public void testSpiral3x3() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> expected = Arrays.asList(1, 2, 3, 6, 9, 8, 7, 4, 5);
        assertEquals(expected, solution.spiralOrder(matrix));
    }

    @Test
    public void testSpiral3x4() {
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7);
        assertEquals(expected, solution.spiralOrder(matrix));
    }

    @Test
    public void testSpiralSingleRow() {
        int[][] matrix = {{1, 2, 3}};
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, solution.spiralOrder(matrix));
    }
}
