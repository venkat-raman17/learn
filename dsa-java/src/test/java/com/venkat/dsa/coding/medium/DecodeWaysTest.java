package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class DecodeWaysTest {

    private final DecodeWays solution = new DecodeWays();

    @Test
    public void testTwelve() {
        assertEquals(2, solution.numDecodings("12"));
    }

    @Test
    public void testTwoTwoSix() {
        assertEquals(3, solution.numDecodings("226"));
    }

    @Test
    public void testLeadingZero() {
        assertEquals(0, solution.numDecodings("06"));
    }
}
