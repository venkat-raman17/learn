package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JumpGameIITest {

    private final JumpGameII solution = new JumpGameII();

    @Test
    void example1() {
        // [2,3,1,1,4]: jump index0->1 (reach 1 or 2), then 1->4, total 2 jumps
        assertEquals(2, solution.jump(new int[]{2, 3, 1, 1, 4}));
    }

    @Test
    void example2() {
        // [2,3,0,1,4]: jump 0->1, then 1->4, total 2 jumps
        assertEquals(2, solution.jump(new int[]{2, 3, 0, 1, 4}));
    }

    @Test
    void singleElement() {
        // Already at destination, 0 jumps needed
        assertEquals(0, solution.jump(new int[]{0}));
    }

    @Test
    void twoElements() {
        // One jump always sufficient
        assertEquals(1, solution.jump(new int[]{1, 0}));
    }

    @Test
    void allOnes() {
        // [1,1,1,1] -> must jump every step, 3 jumps
        assertEquals(3, solution.jump(new int[]{1, 1, 1, 1}));
    }

    @Test
    void largeJumpAtStart() {
        // [5,1,1,1,1,1]: one jump from index 0 reaches end
        assertEquals(1, solution.jump(new int[]{5, 1, 1, 1, 1, 1}));
    }

    @Test
    void threeJumps() {
        // [1,1,1,1]: 3 steps of 1 each
        assertEquals(3, solution.jump(new int[]{1, 1, 1, 1}));
    }

    @Test
    void greedyChoiceMatters() {
        // [3,2,2,0,4]: from 0 can reach 1,2,3; from 1 can reach 3; from 2 can reach 4
        // Greedy: jump 0->2, then 2->4, 2 jumps
        assertEquals(2, solution.jump(new int[]{3, 2, 2, 0, 4}));
    }
}
