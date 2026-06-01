package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlusOneTest {

    private final PlusOne sol = new PlusOne();

    @Test
    void example1_123() {
        assertArrayEquals(new int[]{1, 2, 4}, sol.plusOne(new int[]{1, 2, 3}));
    }

    @Test
    void example2_4321() {
        // LeetCode example: [4,3,2,1] -> [4,3,2,2]
        assertArrayEquals(new int[]{4, 3, 2, 2}, sol.plusOne(new int[]{4, 3, 2, 1}));
    }

    @Test
    void example3_9() {
        assertArrayEquals(new int[]{1, 0}, sol.plusOne(new int[]{9}));
    }

    @Test
    void allNines_999() {
        assertArrayEquals(new int[]{1, 0, 0, 0}, sol.plusOne(new int[]{9, 9, 9}));
    }

    @Test
    void singleZero() {
        assertArrayEquals(new int[]{1}, sol.plusOne(new int[]{0}));
    }

    @Test
    void trailingNine_199() {
        // 1,9,9 -> 2,0,0
        assertArrayEquals(new int[]{2, 0, 0}, sol.plusOne(new int[]{1, 9, 9}));
    }

    @Test
    void singleOne() {
        assertArrayEquals(new int[]{2}, sol.plusOne(new int[]{1}));
    }
}
