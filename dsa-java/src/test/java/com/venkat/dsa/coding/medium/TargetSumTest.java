package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class TargetSumTest {

    private final TargetSum solution = new TargetSum();

    @Test
    void testExample1() {
        assertEquals(5, solution.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
    }

    @Test
    void testSingleElement() {
        assertEquals(1, solution.findTargetSumWays(new int[]{1}, 1));
    }

    @Test
    void testNoWays() {
        assertEquals(0, solution.findTargetSumWays(new int[]{1}, 2));
    }
}
