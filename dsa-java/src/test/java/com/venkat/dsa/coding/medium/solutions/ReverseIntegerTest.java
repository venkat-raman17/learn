package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReverseIntegerTest {

    private final ReverseInteger sol = new ReverseInteger();

    @Test
    void example1_officialExample() {
        // 123 -> 321
        assertEquals(321, sol.reverse(123));
    }

    @Test
    void example2_officialExample() {
        // -123 -> -321
        assertEquals(-321, sol.reverse(-123));
    }

    @Test
    void example3_officialExample() {
        // 120 -> 21  (trailing zeros drop)
        assertEquals(21, sol.reverse(120));
    }

    @Test
    void zero() {
        assertEquals(0, sol.reverse(0));
    }

    @Test
    void positiveOverflow() {
        // 1534236469 reversed = 9646324351 > Integer.MAX_VALUE -> 0
        assertEquals(0, sol.reverse(1534236469));
    }

    @Test
    void negativeOverflow() {
        // -1534236469 reversed = -9646324351 < Integer.MIN_VALUE -> 0
        assertEquals(0, sol.reverse(-1534236469));
    }

    @Test
    void singleDigit() {
        assertEquals(5, sol.reverse(5));
        assertEquals(-7, sol.reverse(-7));
    }

    @Test
    void reverseMaxInt_overflows() {
        // Integer.MAX_VALUE = 2147483647, reversed = 7463847412 -> overflow -> 0
        assertEquals(0, sol.reverse(Integer.MAX_VALUE));
    }

    @Test
    void reverseMinInt_overflows() {
        // Integer.MIN_VALUE = -2147483648, reversed = -8463847412 -> overflow -> 0
        assertEquals(0, sol.reverse(Integer.MIN_VALUE));
    }

    @Test
    void palindromicNumber() {
        // 1221 reversed = 1221
        assertEquals(1221, sol.reverse(1221));
    }
}
