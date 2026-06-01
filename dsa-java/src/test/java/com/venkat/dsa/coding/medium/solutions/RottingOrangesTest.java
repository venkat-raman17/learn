package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RottingOrangesTest {

    private final RottingOranges sol = new RottingOranges();

    @Test
    void example1_returns4() {
        // LeetCode example: [[2,1,1],[1,1,0],[0,1,1]] -> 4
        int[][] grid = {
            {2,1,1},
            {1,1,0},
            {0,1,1}
        };
        assertEquals(4, sol.orangesRotting(grid));
    }

    @Test
    void example2_returns_negative1() {
        // [[2,1,1],[0,1,1],[1,0,1]] -> -1 (bottom-left fresh orange isolated)
        int[][] grid = {
            {2,1,1},
            {0,1,1},
            {1,0,1}
        };
        assertEquals(-1, sol.orangesRotting(grid));
    }

    @Test
    void example3_noFreshOranges() {
        // [[0,2]] -> 0
        int[][] grid = {{0, 2}};
        assertEquals(0, sol.orangesRotting(grid));
    }

    @Test
    void allFresh_noRotten() {
        int[][] grid = {{1,1},{1,1}};
        assertEquals(-1, sol.orangesRotting(grid));
    }

    @Test
    void allRotten() {
        int[][] grid = {{2,2},{2,2}};
        assertEquals(0, sol.orangesRotting(grid));
    }

    @Test
    void singleFreshOrange_noRotten() {
        assertEquals(-1, sol.orangesRotting(new int[][]{{1}}));
    }

    @Test
    void singleRottenOrange() {
        assertEquals(0, sol.orangesRotting(new int[][]{{2}}));
    }

    @Test
    void linearSpread() {
        // [2,1,1,1,1] -> 4 minutes
        int[][] grid = {{2,1,1,1,1}};
        assertEquals(4, sol.orangesRotting(grid));
    }

    @Test
    void multipleRottenSources() {
        // [2,1,1,1,2] -> 2 minutes (spreading from both ends)
        int[][] grid = {{2,1,1,1,2}};
        assertEquals(2, sol.orangesRotting(grid));
    }
}
