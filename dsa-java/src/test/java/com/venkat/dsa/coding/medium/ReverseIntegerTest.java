package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class ReverseIntegerTest {

    private final ReverseInteger solution = new ReverseInteger();

    @Test
    public void testPositive() {
        assertEquals(321, solution.reverse(123));
    }

    @Test
    public void testNegative() {
        assertEquals(-321, solution.reverse(-123));
    }

    @Test
    public void testTrailingZero() {
        assertEquals(21, solution.reverse(120));
    }

    @Test
    public void testOverflow() {
        // Reversed value would exceed Integer.MAX_VALUE
        assertEquals(0, solution.reverse(1534236469));
    }
}
