package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class DistinctSubsequencesTest {

    private final DistinctSubsequences solution = new DistinctSubsequences();

    @Test
    void testExample1() {
        assertEquals(3, solution.numDistinct("rabbbit", "rabbit"));
    }

    @Test
    void testExample2() {
        assertEquals(5, solution.numDistinct("babgbag", "bag"));
    }

    @Test
    void testNoMatch() {
        assertEquals(0, solution.numDistinct("abc", "xyz"));
    }
}
