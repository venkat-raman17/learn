package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PowXNTest {

    private static final double EPS = 1e-5;
    private final PowXN sol = new PowXN();

    @Test
    void example1_2pow10() {
        assertEquals(1024.0, sol.myPow(2.0, 10), EPS);
    }

    @Test
    void example2_2_1pow_3() {
        // 2.1^3 = 9.261
        assertEquals(9.261, sol.myPow(2.1, 3), EPS);
    }

    @Test
    void example3_2powNeg2() {
        // 2^-2 = 0.25
        assertEquals(0.25, sol.myPow(2.0, -2), EPS);
    }

    @Test
    void xPow0() {
        assertEquals(1.0, sol.myPow(5.0, 0), EPS);
    }

    @Test
    void zeroPow0() {
        // Conventionally 0^0 = 1
        assertEquals(1.0, sol.myPow(0.0, 0), EPS);
    }

    @Test
    void pow1() {
        assertEquals(3.14159, sol.myPow(3.14159, 1), EPS);
    }

    @Test
    void negativeBase_evenExp() {
        // (-2)^4 = 16
        assertEquals(16.0, sol.myPow(-2.0, 4), EPS);
    }

    @Test
    void negativeBase_oddExp() {
        // (-2)^3 = -8
        assertEquals(-8.0, sol.myPow(-2.0, 3), EPS);
    }

    @Test
    void intMinExponent() {
        // x=1, n=Integer.MIN_VALUE => 1
        assertEquals(1.0, sol.myPow(1.0, Integer.MIN_VALUE), EPS);
    }

    @Test
    void largeNegativeExponent() {
        // 2^-10 = 1/1024
        assertEquals(1.0 / 1024.0, sol.myPow(2.0, -10), EPS);
    }
}
