package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MultiplyStringsTest {

    private final MultiplyStrings sol = new MultiplyStrings();

    @Test
    void example1_2x3() {
        assertEquals("6", sol.multiply("2", "3"));
    }

    @Test
    void example2_123x456() {
        assertEquals("56088", sol.multiply("123", "456"));
    }

    @Test
    void multiplyByZero_left() {
        assertEquals("0", sol.multiply("0", "12345"));
    }

    @Test
    void multiplyByZero_right() {
        assertEquals("0", sol.multiply("99", "0"));
    }

    @Test
    void bothZero() {
        assertEquals("0", sol.multiply("0", "0"));
    }

    @Test
    void singleDigits_9x9() {
        assertEquals("81", sol.multiply("9", "9"));
    }

    @Test
    void largeNumbers() {
        // 999 * 999 = 998001
        assertEquals("998001", sol.multiply("999", "999"));
    }

    @Test
    void asymmetric_1x999() {
        assertEquals("999", sol.multiply("1", "999"));
    }

    @Test
    void multiDigitResult_99x99() {
        // 99 * 99 = 9801
        assertEquals("9801", sol.multiply("99", "99"));
    }

    @Test
    void veryLargeNumbers() {
        // Use known result: 123456789 * 987654321 = 121932631112635269
        assertEquals("121932631112635269", sol.multiply("123456789", "987654321"));
    }
}
