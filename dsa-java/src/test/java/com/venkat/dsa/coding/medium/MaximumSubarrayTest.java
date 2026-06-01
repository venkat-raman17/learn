package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MaximumSubarrayTest {

    private final MaximumSubarray solution = new MaximumSubarray();

    @Test
    void testExample1() {
        assertEquals(6, solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    @Test
    void testSingleElement() {
        assertEquals(1, solution.maxSubArray(new int[]{1}));
    }

    @Test
    void testAllNegative() {
        assertEquals(-1, solution.maxSubArray(new int[]{-3, -1, -2}));
    }
}
