package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrappingRainWaterTest {

    private final TrappingRainWater sol = new TrappingRainWater();

    // Official LeetCode example 1: [0,1,0,2,1,0,1,3,2,1,2,1] -> 6
    @Test
    void officialExample1() {
        assertEquals(6, sol.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }

    // Official LeetCode example 2: [4,2,0,3,2,5] -> 9
    @Test
    void officialExample2() {
        assertEquals(9, sol.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }

    @Test
    void emptyArray() {
        assertEquals(0, sol.trap(new int[]{}));
    }

    @Test
    void singleElement() {
        assertEquals(0, sol.trap(new int[]{5}));
    }

    @Test
    void twoElements() {
        assertEquals(0, sol.trap(new int[]{3, 4}));
    }

    @Test
    void flatArray() {
        // All same height — no water
        assertEquals(0, sol.trap(new int[]{3, 3, 3, 3}));
    }

    @Test
    void strictlyIncreasing() {
        // [1,2,3,4] — no valleys, no water
        assertEquals(0, sol.trap(new int[]{1, 2, 3, 4}));
    }

    @Test
    void strictlyDecreasing() {
        // [4,3,2,1] — no valleys, no water
        assertEquals(0, sol.trap(new int[]{4, 3, 2, 1}));
    }

    @Test
    void simpleValley() {
        // [3,0,3] -> water = 3
        assertEquals(3, sol.trap(new int[]{3, 0, 3}));
    }

    @Test
    void wideValley() {
        // [5,0,0,0,5] -> water = 5+5+5 = 15
        assertEquals(15, sol.trap(new int[]{5, 0, 0, 0, 5}));
    }

    @Test
    void asymmetricWalls() {
        // [3,0,5] -> min(3,5)-0 = 3 units of water
        assertEquals(3, sol.trap(new int[]{3, 0, 5}));
    }

    @Test
    void multipleValleys() {
        // [2,0,2,0,2] -> 2 (first valley) + 2 (second valley) = 4
        assertEquals(4, sol.trap(new int[]{2, 0, 2, 0, 2}));
    }

    @Test
    void pyramidShape() {
        // [0,1,2,3,2,1,0] -> no water (strictly goes up then down)
        assertEquals(0, sol.trap(new int[]{0, 1, 2, 3, 2, 1, 0}));
    }

    @Test
    void innerPlatform() {
        // [4,1,1,1,4] -> (4-1)*3 = 9
        assertEquals(9, sol.trap(new int[]{4, 1, 1, 1, 4}));
    }
}
