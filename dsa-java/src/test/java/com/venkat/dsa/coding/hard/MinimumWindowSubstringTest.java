package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class MinimumWindowSubstringTest {

    private final MinimumWindowSubstring solution = new MinimumWindowSubstring();

    @Test
    void example1_typicalCase() {
        assertEquals("BANC", solution.minWindow("ADOBECODEBANC", "ABC"));
    }

    @Test
    void example2_singleChar() {
        assertEquals("a", solution.minWindow("a", "a"));
    }

    @Test
    void noValidWindow() {
        assertEquals("", solution.minWindow("a", "aa"));
    }
}
