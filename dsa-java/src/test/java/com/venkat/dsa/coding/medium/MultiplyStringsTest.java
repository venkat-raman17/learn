package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MultiplyStringsTest {

    private final MultiplyStrings solution = new MultiplyStrings();

    @Test
    public void testSingleDigits() {
        assertEquals("6", solution.multiply("2", "3"));
    }

    @Test
    public void testLargerNumbers() {
        assertEquals("56088", solution.multiply("123", "456"));
    }

    @Test
    public void testMultiplyByZero() {
        assertEquals("0", solution.multiply("0", "12345"));
    }
}
