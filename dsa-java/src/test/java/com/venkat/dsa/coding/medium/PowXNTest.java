package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PowXNTest {

    private final PowXN solution = new PowXN();

    @Test
    public void testPositiveExponent() {
        assertEquals(1024.0, solution.myPow(2.0, 10), 1e-5);
    }

    @Test
    public void testNegativeExponent() {
        assertEquals(0.25, solution.myPow(2.0, -2), 1e-5);
    }

    @Test
    public void testDecimalBase() {
        assertEquals(9.261, solution.myPow(2.1, 3), 1e-3);
    }
}
