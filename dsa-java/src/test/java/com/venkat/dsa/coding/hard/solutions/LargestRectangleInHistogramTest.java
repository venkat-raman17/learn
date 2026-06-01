package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LargestRectangleInHistogramTest {

    private final LargestRectangleInHistogram solution = new LargestRectangleInHistogram();

    // Official LeetCode examples
    @Test
    void example1() {
        // heights = [2,1,5,6,2,3]
        // Largest rectangle: bars at index 2,3 with height 5, width 2 -> area 10
        assertEquals(10, solution.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
    }

    @Test
    void example2_twoElements() {
        // heights = [2,4] -> max is 4 (single bar) vs 2*2=4 -> both give 4
        assertEquals(4, solution.largestRectangleArea(new int[]{2, 4}));
    }

    // Edge cases
    @Test
    void singleBar() {
        assertEquals(5, solution.largestRectangleArea(new int[]{5}));
    }

    @Test
    void singleBarHeightOne() {
        assertEquals(1, solution.largestRectangleArea(new int[]{1}));
    }

    @Test
    void allSameHeight() {
        // [3,3,3,3] -> 3*4=12
        assertEquals(12, solution.largestRectangleArea(new int[]{3, 3, 3, 3}));
    }

    @Test
    void increasingHeights() {
        // [1,2,3,4,5] -> largest is 1*5=5 or 2*4=8 or 3*3=9 -> 9
        assertEquals(9, solution.largestRectangleArea(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void decreasingHeights() {
        // [5,4,3,2,1] -> 1*5=5 or 2*4=8 or 3*3=9 -> 9
        assertEquals(9, solution.largestRectangleArea(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void mountainShape() {
        // [1,2,3,2,1] -> height=2 spans indices 1-3 (width=3) -> 6; height=1 spans all 5 -> 5; height=3 width=1 -> 3 -> max=6
        assertEquals(6, solution.largestRectangleArea(new int[]{1, 2, 3, 2, 1}));
    }

    @Test
    void singleBarHeightZero() {
        assertEquals(0, solution.largestRectangleArea(new int[]{0}));
    }

    @Test
    void largeRectangleSpanningAll() {
        // [6,6,6] -> 6*3=18
        assertEquals(18, solution.largestRectangleArea(new int[]{6, 6, 6}));
    }

    @Test
    void valleyInMiddle() {
        // [5,1,5] -> max is 1*3=3 or 5*1=5 -> 5
        assertEquals(5, solution.largestRectangleArea(new int[]{5, 1, 5}));
    }

    @Test
    void twoTallBarsSeparatedByZero() {
        // [5,0,5] -> max single bar = 5
        assertEquals(5, solution.largestRectangleArea(new int[]{5, 0, 5}));
    }
}
