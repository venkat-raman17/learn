package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumberOfIslandsTest {

    private final NumberOfIslands sol = new NumberOfIslands();

    // Helper to deep-copy a char[][] so the in-place modification doesn't affect other tests
    private char[][] copy(char[][] grid) {
        char[][] copy = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) copy[i] = grid[i].clone();
        return copy;
    }

    @Test
    void example1_fourIslands() {
        char[][] grid = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        assertEquals(3, sol.numIslands(copy(grid)));
    }

    @Test
    void example2_oneIsland() {
        char[][] grid = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        assertEquals(1, sol.numIslands(copy(grid)));
    }

    @Test
    void allWater() {
        char[][] grid = {
            {'0','0'},
            {'0','0'}
        };
        assertEquals(0, sol.numIslands(copy(grid)));
    }

    @Test
    void allLand() {
        char[][] grid = {
            {'1','1'},
            {'1','1'}
        };
        assertEquals(1, sol.numIslands(copy(grid)));
    }

    @Test
    void singleCell_land() {
        assertEquals(1, sol.numIslands(new char[][]{{'1'}}));
    }

    @Test
    void singleCell_water() {
        assertEquals(0, sol.numIslands(new char[][]{{'0'}}));
    }

    @Test
    void diagonalIslandsNotConnected() {
        // Diagonal adjacency does NOT count; each '1' is a separate island
        char[][] grid = {
            {'1','0','1'},
            {'0','1','0'},
            {'1','0','1'}
        };
        assertEquals(5, sol.numIslands(copy(grid)));
    }

    @Test
    void singleRow() {
        char[][] grid = {{'1','0','1','1','0','1'}};
        assertEquals(3, sol.numIslands(copy(grid)));
    }
}
