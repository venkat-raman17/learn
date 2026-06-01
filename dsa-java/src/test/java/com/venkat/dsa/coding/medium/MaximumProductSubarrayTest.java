package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MaximumProductSubarrayTest {

    private final MaximumProductSubarray solution = new MaximumProductSubarray();

    @Test
    public void testExample1() {
        assertEquals(6, solution.maxProduct(new int[]{2, 3, -2, 4}));
    }

    @Test
    public void testWithZero() {
        assertEquals(0, solution.maxProduct(new int[]{-2, 0, -1}));
    }

    @Test
    public void testAllNegative() {
        assertEquals(6, solution.maxProduct(new int[]{-2, 3, -4}));
    }
}
