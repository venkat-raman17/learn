package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LastStoneWeightTest {

    private final LastStoneWeight solution = new LastStoneWeight();

    @Test
    void testExample1() {
        assertEquals(1, solution.lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1}));
    }

    @Test
    void testSingleStone() {
        assertEquals(1, solution.lastStoneWeight(new int[]{1}));
    }

    @Test
    void testAllEqual() {
        // [2, 2] -> smash -> [] -> 0
        assertEquals(0, solution.lastStoneWeight(new int[]{2, 2}));
    }
}
