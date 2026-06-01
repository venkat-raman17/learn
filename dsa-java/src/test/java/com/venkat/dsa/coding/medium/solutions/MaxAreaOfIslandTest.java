package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaxAreaOfIslandTest {

    private final MaxAreaOfIsland sol = new MaxAreaOfIsland();

    private int[][] copy(int[][] g) {
        int[][] c = new int[g.length][];
        for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
        return c;
    }

    @Test
    void example1() {
        // LeetCode example: max island area = 6
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
        assertEquals(6, sol.maxAreaOfIsland(copy(grid)));
    }

    @Test
    void example2_allWater() {
        int[][] grid = {{0,0,0,0,0,0,0,0}};
        assertEquals(0, sol.maxAreaOfIsland(copy(grid)));
    }

    @Test
    void singleLandCell() {
        assertEquals(1, sol.maxAreaOfIsland(new int[][]{{0,1,0}}));
    }

    @Test
    void fullGridLand() {
        int[][] grid = {
            {1,1},
            {1,1}
        };
        assertEquals(4, sol.maxAreaOfIsland(copy(grid)));
    }

    @Test
    void twoIslands_differentSizes() {
        int[][] grid = {
            {1,1,0,0,1},
            {1,0,0,0,1},
            {0,0,0,0,1}
        };
        // left island: 3 cells; right island: 3 cells — tie; answer = 3
        assertEquals(3, sol.maxAreaOfIsland(copy(grid)));
    }

    @Test
    void largerRightIsland() {
        int[][] grid = {
            {1,0,1},
            {0,0,1},
            {0,0,1}
        };
        // right column: 3 cells; top-left: 1 cell
        assertEquals(3, sol.maxAreaOfIsland(copy(grid)));
    }
}
