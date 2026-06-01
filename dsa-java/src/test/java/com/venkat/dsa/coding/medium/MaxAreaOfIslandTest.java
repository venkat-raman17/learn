package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MaxAreaOfIslandTest {

    private final MaxAreaOfIsland solution = new MaxAreaOfIsland();

    @Test
    public void testMaxAreaSix() {
        int[][] grid = {
            {0,0,1,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,0,0,1,1,1,0,0,0},
            {0,1,1,0,1,0,0,0,0,0,0,0,0},
            {0,1,0,0,1,1,0,0,1,0,1,0,0},
            {0,1,0,0,1,1,0,0,1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,1,0,0},
            {0,0,0,0,0,0,0,1,1,1,0,0,0},
            {0,0,0,0,0,0,0,1,1,0,0,0,0}
        };
        assertEquals(6, solution.maxAreaOfIsland(grid));
    }

    @Test
    public void testNoIsland() {
        int[][] grid = {{0,0,0,0,0,0,0,0}};
        assertEquals(0, solution.maxAreaOfIsland(grid));
    }

    @Test
    public void testSingleCell() {
        int[][] grid = {{1}};
        assertEquals(1, solution.maxAreaOfIsland(grid));
    }
}
