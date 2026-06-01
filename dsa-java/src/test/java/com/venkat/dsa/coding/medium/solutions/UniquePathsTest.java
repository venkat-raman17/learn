package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UniquePathsTest {

    private final UniquePaths sol = new UniquePaths();

    // LeetCode example 1: 3x7 grid -> 28
    @Test
    void example1() {
        assertEquals(28, sol.uniquePaths(3, 7));
    }

    // LeetCode example 2: 3x2 grid -> 3
    @Test
    void example2() {
        assertEquals(3, sol.uniquePaths(3, 2));
    }

    // Single row: only one path (go straight right)
    @Test
    void singleRow() {
        assertEquals(1, sol.uniquePaths(1, 10));
    }

    // Single column: only one path (go straight down)
    @Test
    void singleColumn() {
        assertEquals(1, sol.uniquePaths(7, 1));
    }

    // 1x1 grid: start == end
    @Test
    void oneByOne() {
        assertEquals(1, sol.uniquePaths(1, 1));
    }

    // 2x2 grid: 2 paths
    @Test
    void twoByTwo() {
        assertEquals(2, sol.uniquePaths(2, 2));
    }

    // 3x3 grid: 6 paths
    @Test
    void threeByThree() {
        assertEquals(6, sol.uniquePaths(3, 3));
    }
}
