package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TargetSumTest {

    private final TargetSum sol = new TargetSum();

    // LeetCode example 1: nums=[1,1,1,1,1], target=3 -> 5
    @Test
    void example1() {
        assertEquals(5, sol.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
    }

    // LeetCode example 2: nums=[1], target=1 -> 1
    @Test
    void example2() {
        assertEquals(1, sol.findTargetSumWays(new int[]{1}, 1));
    }

    // Single element, target=-1 -> 1 (assign -)
    @Test
    void singleNegative() {
        assertEquals(1, sol.findTargetSumWays(new int[]{1}, -1));
    }

    // Target unreachable (greater than sum)
    @Test
    void unreachable() {
        assertEquals(0, sol.findTargetSumWays(new int[]{1, 2}, 10));
    }

    // All zeros: every assignment gives 0, 2^n ways when target=0
    @Test
    void allZerosTargetZero() {
        assertEquals(4, sol.findTargetSumWays(new int[]{0, 0}, 0));
    }

    // Target=0 with balanced array
    @Test
    void targetZero() {
        assertEquals(2, sol.findTargetSumWays(new int[]{1, 1}, 0));
    }

    // Parity check: (target + total) must be even
    @Test
    void parityMismatch() {
        assertEquals(0, sol.findTargetSumWays(new int[]{1, 2}, 0));
        // total=3, target=0 -> (0+3)=3, odd -> 0 ways
    }
}
