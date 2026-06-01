package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PlusOneTest {

    private final PlusOne solution = new PlusOne();

    @Test
    public void testSimpleIncrement() {
        assertArrayEquals(new int[]{1, 2, 4}, solution.plusOne(new int[]{1, 2, 3}));
    }

    @Test
    public void testAllNines() {
        assertArrayEquals(new int[]{1, 0, 0, 0}, solution.plusOne(new int[]{9, 9, 9}));
    }

    @Test
    public void testSingleDigit() {
        assertArrayEquals(new int[]{1, 0}, solution.plusOne(new int[]{9}));
    }
}
