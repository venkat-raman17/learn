package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LargestRectangleInHistogramTest {

    private final LargestRectangleInHistogram solution = new LargestRectangleInHistogram();

    @Test
    public void testExample1() {
        assertEquals(10, solution.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
    }

    @Test
    public void testTwoBars() {
        assertEquals(4, solution.largestRectangleArea(new int[]{2, 4}));
    }

    @Test
    public void testSingleBar() {
        assertEquals(5, solution.largestRectangleArea(new int[]{5}));
    }
}
