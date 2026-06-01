package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class TrappingRainWaterTest {

    private final TrappingRainWater solution = new TrappingRainWater();

    @Test
    void testStandardCase() {
        assertEquals(6, solution.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }

    @Test
    void testSecondExample() {
        assertEquals(9, solution.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }

    @Test
    void testFlatSurface() {
        assertEquals(0, solution.trap(new int[]{1, 1, 1, 1}));
    }
}
