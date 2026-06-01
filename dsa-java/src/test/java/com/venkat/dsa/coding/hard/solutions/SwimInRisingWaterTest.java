package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SwimInRisingWaterTest {

    private final SwimInRisingWater sol = new SwimInRisingWater();

    @Test
    void testExample1() {
        // LeetCode example 1: 2x2 grid [[0,2],[1,3]] -> answer 3
        int[][] grid = {{0, 2}, {1, 3}};
        assertEquals(3, sol.swimInWater(grid));
    }

    @Test
    void testExample2() {
        // LeetCode example 2: 5x5 grid
        int[][] grid = {
            {0,  1,  2,  3,  4},
            {24, 23, 22, 21, 5},
            {12, 13, 14, 15, 16},
            {11, 17, 18, 19, 20},
            {10,  9,  8,  7,  6}
        };
        assertEquals(16, sol.swimInWater(grid));
    }

    @Test
    void testSingleCell() {
        int[][] grid = {{7}};
        assertEquals(7, sol.swimInWater(grid));
    }

    @Test
    void testMonotonicallyIncreasingPath() {
        // Path along top row + right column: max = grid[1][1]=3
        // grid = [[0,1],[2,3]]
        // Paths: (0,0)->(0,1)->(1,1): max(0,1,3)=3
        //        (0,0)->(1,0)->(1,1): max(0,2,3)=3
        int[][] grid = {{0, 1}, {2, 3}};
        assertEquals(3, sol.swimInWater(grid));
    }

    @Test
    void testChooseLowerBottleneck() {
        // 3x3: two routes, choose the one with lower max elevation
        // Grid:
        // 0 9 1
        // 9 9 2
        // 3 4 5
        // Path via right column: (0,0)->(0,2): need to go through (0,1)=9 — no direct jump
        // From (0,0) neighbors: right=(0,1)=9, down=(1,0)=9
        // Going down column: (0,0)->(1,0)=9 bottleneck=9, or going right: bottleneck=9
        // All paths touch 9 except: (0,0)->(0,1)? no, we need a connected path
        // Best path: (0,0)->(0,1)=9 OR (0,0)->(1,0)=9, either way bottleneck includes 9
        // Actually any path from (0,0) to (2,2) must traverse the grid...
        // Let's use a simpler verifiable case:
        // 0 3
        // 1 2
        // Paths: right then down: max(0,3,2)=3; down then right: max(0,1,2)=2
        int[][] grid = {{0, 3}, {1, 2}};
        assertEquals(2, sol.swimInWater(grid));
    }

    @Test
    void testLargerGrid() {
        // 3x3 grid where the minimax path avoids a high peak
        // 0 5 1
        // 4 6 2
        // 7 8 3
        // Path right-down-right: (0,0)->5->1->2->3 = max 5
        // Path down-down-right-right: (0,0)->4->7->8->3 = max 8
        // Path right-right-down-down: (0,0)->5->1->2->3 = max 5
        // Actually shortest bottleneck: (0,0)->0, (0,2)->1, (1,2)->2, (2,2)->3 but need connected
        // (0,0)=0 -> (0,1)=5 -> (0,2)=1 -> (1,2)=2 -> (2,2)=3: bottleneck = max(0,5,1,2,3) = 5
        // (0,0)=0 -> (1,0)=4 -> (1,1)=6 -> (1,2)=2 -> (2,2)=3: bottleneck = max(0,4,6,2,3) = 6
        // (0,0)=0 -> (1,0)=4 -> (2,0)=7 -> (2,1)=8 -> (2,2)=3: bottleneck = 8
        // Best: 5 via top row then right column
        int[][] grid = {
            {0, 5, 1},
            {4, 6, 2},
            {7, 8, 3}
        };
        assertEquals(5, sol.swimInWater(grid));
    }
}
