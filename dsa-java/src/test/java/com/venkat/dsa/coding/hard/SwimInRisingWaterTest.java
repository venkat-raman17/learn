package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SwimInRisingWaterTest {

    private final SwimInRisingWater solution = new SwimInRisingWater();

    @Test
    public void testExample1() {
        int[][] grid = {{0, 2}, {1, 3}};
        assertEquals(3, solution.swimInWater(grid));
    }

    @Test
    public void testExample2() {
        int[][] grid = {
            {0,  1,  2,  3,  4},
            {24, 23, 22, 21, 5},
            {12, 13, 14, 15, 16},
            {11, 17, 18, 19, 20},
            {10, 9,  8,  7,  6}
        };
        assertEquals(16, solution.swimInWater(grid));
    }

    @Test
    public void testSingleCell() {
        int[][] grid = {{0}};
        assertEquals(0, solution.swimInWater(grid));
    }
}
