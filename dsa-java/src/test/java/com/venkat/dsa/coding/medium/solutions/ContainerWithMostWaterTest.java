package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContainerWithMostWaterTest {

    private final ContainerWithMostWater sol = new ContainerWithMostWater();

    // Official LeetCode example 1: [1,8,6,2,5,4,8,3,7] -> 49
    @Test
    void officialExample1() {
        assertEquals(49, sol.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
    }

    // Official LeetCode example 2: [1,1] -> 1
    @Test
    void officialExample2() {
        assertEquals(1, sol.maxArea(new int[]{1, 1}));
    }

    @Test
    void twoElementsDifferentHeights() {
        // [3,5] -> min(3,5) * 1 = 3
        assertEquals(3, sol.maxArea(new int[]{3, 5}));
    }

    @Test
    void increasingHeights() {
        // [1,2,3,4,5]: best is indices 1 and 4 -> min(2,5)*3 = 6
        assertEquals(6, sol.maxArea(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void decreasingHeights() {
        // [5,4,3,2,1]: best is indices 0 and 3 -> min(5,2)*3 = 6
        assertEquals(6, sol.maxArea(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void allSameHeight() {
        // [4,4,4,4]: best = 4*3 = 12
        assertEquals(12, sol.maxArea(new int[]{4, 4, 4, 4}));
    }

    @Test
    void tallWallsAtEdgesWithLowMiddle() {
        // [10,1,1,1,10]: best = min(10,10)*4 = 40
        assertEquals(40, sol.maxArea(new int[]{10, 1, 1, 1, 10}));
    }

    @Test
    void singlePeakInMiddle() {
        // [1,100,1]: best = min(1,1)*2=2 or min(1,100)*1=1 -> 2
        assertEquals(2, sol.maxArea(new int[]{1, 100, 1}));
    }

    @Test
    void zeroBetweenTallWalls() {
        // [5,0,0,5]: best = min(5,5)*3 = 15
        assertEquals(15, sol.maxArea(new int[]{5, 0, 0, 5}));
    }
}
