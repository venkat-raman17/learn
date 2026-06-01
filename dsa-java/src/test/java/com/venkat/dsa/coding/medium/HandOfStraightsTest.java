package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class HandOfStraightsTest {

    private final HandOfStraights solution = new HandOfStraights();

    @Test
    void testExample1() {
        assertTrue(solution.isNStraightHand(new int[]{1, 2, 3, 6, 2, 3, 4, 7, 8}, 3));
    }

    @Test
    void testExample2() {
        assertFalse(solution.isNStraightHand(new int[]{1, 2, 3, 4, 5}, 4));
    }

    @Test
    void testGroupSizeOne() {
        assertTrue(solution.isNStraightHand(new int[]{5, 3, 1}, 1));
    }
}
