package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ProductOfArrayExceptSelfTest {

    private final ProductOfArrayExceptSelf solution = new ProductOfArrayExceptSelf();

    @Test
    public void testBasicCase() {
        assertArrayEquals(new int[]{24, 12, 8, 6}, solution.productExceptSelf(new int[]{1, 2, 3, 4}));
    }

    @Test
    public void testWithZero() {
        assertArrayEquals(new int[]{0, 0, 9, 0, 0}, solution.productExceptSelf(new int[]{-1, 1, 0, -3, 3}));
    }

    @Test
    public void testTwoElements() {
        assertArrayEquals(new int[]{2, 1}, solution.productExceptSelf(new int[]{1, 2}));
    }
}
