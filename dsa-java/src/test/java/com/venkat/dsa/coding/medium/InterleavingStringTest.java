package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class InterleavingStringTest {

    private final InterleavingString solution = new InterleavingString();

    @Test
    void testExample1() {
        assertTrue(solution.isInterleave("aabcc", "dbbca", "aadbbcbcac"));
    }

    @Test
    void testExample2() {
        assertFalse(solution.isInterleave("aabcc", "dbbca", "aadbbbaccc"));
    }

    @Test
    void testEmptyStrings() {
        assertTrue(solution.isInterleave("", "", ""));
    }
}
