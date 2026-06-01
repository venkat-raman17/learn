package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HandOfStraightsTest {

    private final HandOfStraights solution = new HandOfStraights();

    @Test
    void example1_possible() {
        // hand=[1,2,3,6,2,3,4,7,8] groupSize=3 -> [1,2,3],[2,3,4],[6,7,8]
        assertTrue(solution.isNStraightHand(new int[]{1, 2, 3, 6, 2, 3, 4, 7, 8}, 3));
    }

    @Test
    void example2_impossible() {
        // hand=[1,2,3,4,5] groupSize=4 -> 5 % 4 != 0
        assertFalse(solution.isNStraightHand(new int[]{1, 2, 3, 4, 5}, 4));
    }

    @Test
    void groupSizeOne() {
        // Every single card is a valid group of 1
        assertTrue(solution.isNStraightHand(new int[]{5, 3, 1}, 1));
    }

    @Test
    void singleGroupExact() {
        assertTrue(solution.isNStraightHand(new int[]{1, 2, 3}, 3));
    }

    @Test
    void gapInSequence() {
        // [1,2,4,5] groupSize=2 -> [1,2],[4,5] ok
        assertTrue(solution.isNStraightHand(new int[]{1, 2, 4, 5}, 2));
    }

    @Test
    void notEnoughConsecutive() {
        // [1,2,5] groupSize=3 -> need 1,2,3 but 3 is missing
        assertFalse(solution.isNStraightHand(new int[]{1, 2, 5}, 3));
    }

    @Test
    void duplicateCards_possible() {
        // [1,1,2,2,3,3] groupSize=3 -> [1,2,3],[1,2,3]
        assertTrue(solution.isNStraightHand(new int[]{1, 1, 2, 2, 3, 3}, 3));
    }

    @Test
    void duplicateCards_impossible() {
        // [1,1,2,3] groupSize=3 -> need two groups but only 4 cards and 4%3!=0
        assertFalse(solution.isNStraightHand(new int[]{1, 1, 2, 3}, 3));
    }

    @Test
    void lengthNotDivisible() {
        assertFalse(solution.isNStraightHand(new int[]{1, 2, 3, 4}, 3));
    }
}
