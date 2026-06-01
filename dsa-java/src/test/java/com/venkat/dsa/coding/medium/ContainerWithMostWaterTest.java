package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ContainerWithMostWaterTest {

    private final ContainerWithMostWater solution = new ContainerWithMostWater();

    @Test
    void testStandardCase() {
        assertEquals(49, solution.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
    }

    @Test
    void testTwoEqualLines() {
        assertEquals(1, solution.maxArea(new int[]{1, 1}));
    }

    @Test
    void testIncreasingHeights() {
        assertEquals(8, solution.maxArea(new int[]{1, 2, 4, 8}));
    }
}
