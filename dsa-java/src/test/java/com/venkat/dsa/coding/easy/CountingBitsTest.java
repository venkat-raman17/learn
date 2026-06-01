package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Disabled("practice — delete when you start")
public class CountingBitsTest {

    private final CountingBits solution = new CountingBits();

    @Test
    public void testN2() {
        assertArrayEquals(new int[]{0, 1, 1}, solution.countBits(2));
    }

    @Test
    public void testN5() {
        assertArrayEquals(new int[]{0, 1, 1, 2, 1, 2}, solution.countBits(5));
    }

    @Test
    public void testN0() {
        assertArrayEquals(new int[]{0}, solution.countBits(0));
    }
}
