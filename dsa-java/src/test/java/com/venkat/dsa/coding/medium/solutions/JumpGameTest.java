package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JumpGameTest {

    private final JumpGame solution = new JumpGame();

    @Test
    void example1_canReach() {
        // [2,3,1,1,4]: from 0 jump 2->index2, then 1->3, then 1->4 (last)
        assertTrue(solution.canJump(new int[]{2, 3, 1, 1, 4}));
    }

    @Test
    void example2_cannotReach() {
        // [3,2,1,0,4]: always land on index 3 which has 0, can't proceed
        assertFalse(solution.canJump(new int[]{3, 2, 1, 0, 4}));
    }

    @Test
    void singleElement() {
        // Already at last index
        assertTrue(solution.canJump(new int[]{0}));
    }

    @Test
    void twoElementsZeroFirst() {
        // Can't jump from 0
        assertFalse(solution.canJump(new int[]{0, 1}));
    }

    @Test
    void twoElementsOneFirst() {
        assertTrue(solution.canJump(new int[]{1, 0}));
    }

    @Test
    void allOnes() {
        assertTrue(solution.canJump(new int[]{1, 1, 1, 1}));
    }

    @Test
    void largeJumpAtStart() {
        // Jump 10 from start covers entire array of length 5
        assertTrue(solution.canJump(new int[]{10, 0, 0, 0, 1}));
    }

    @Test
    void zeroInMiddleBlocking() {
        assertFalse(solution.canJump(new int[]{1, 0, 1, 0}));
    }

    @Test
    void exactReach() {
        // [1,1,1] each step moves exactly one
        assertTrue(solution.canJump(new int[]{1, 1, 1}));
    }
}
