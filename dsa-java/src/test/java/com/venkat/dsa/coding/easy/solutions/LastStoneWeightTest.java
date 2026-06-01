package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LastStoneWeightTest {

    private final LastStoneWeight sol = new LastStoneWeight();

    // Official example 1: [2,7,4,1,8,1]
    // Round 1: smash 8,7 -> 1  remaining: [2,4,1,1,1]
    // Round 2: smash 4,2 -> 2  remaining: [2,1,1,1]
    // Round 3: smash 2,2 -> 0  remaining: [1,1,1]  (both destroyed)
    // Wait: after round 2 we have stones [2,1,1,1] (four stones).
    // Let me retrace carefully:
    // Start: [1,1,2,4,7,8]
    // Step 1: 8 vs 7 -> diff=1  -> [1,1,1,2,4]
    // Step 2: 4 vs 2 -> diff=2  -> [1,1,1,2]
    // Step 3: 2 vs 1 -> diff=1  -> [1,1,1]
    // Step 4: 1 vs 1 -> equal, both gone -> [1]
    // Step 5: one stone left -> return 1
    @Test
    void testOfficialExample1() {
        assertEquals(1, sol.lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1}));
    }

    // Official example 2: single stone
    @Test
    void testSingleStone() {
        assertEquals(1, sol.lastStoneWeight(new int[]{1}));
    }

    // All equal: every collision destroys both -> 0 (even count) or 1 stone (odd count)
    @Test
    void testAllEqual_EvenCount() {
        // [3,3,3,3]: 3vs3=0, 3vs3=0 -> result 0
        assertEquals(0, sol.lastStoneWeight(new int[]{3, 3, 3, 3}));
    }

    @Test
    void testAllEqual_OddCount() {
        // [5,5,5]: 5vs5=0, one 5 left -> result 5
        assertEquals(5, sol.lastStoneWeight(new int[]{5, 5, 5}));
    }

    // Two stones, unequal
    @Test
    void testTwoStones() {
        assertEquals(3, sol.lastStoneWeight(new int[]{4, 7}));
    }

    // Two equal stones
    @Test
    void testTwoEqualStones() {
        assertEquals(0, sol.lastStoneWeight(new int[]{6, 6}));
    }

    // Large distinct values
    @Test
    void testLargeValues() {
        // [100, 1]: 100 vs 1 -> 99
        assertEquals(99, sol.lastStoneWeight(new int[]{100, 1}));
    }
}
