package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountingBitsTest {

    private final CountingBits sol = new CountingBits();

    @Test
    void example1_n2() {
        // n=2 -> [0,1,1]
        assertArrayEquals(new int[]{0, 1, 1}, sol.countBits(2));
    }

    @Test
    void example2_n5() {
        // n=5 -> [0,1,1,2,1,2]
        assertArrayEquals(new int[]{0, 1, 1, 2, 1, 2}, sol.countBits(5));
    }

    @Test
    void zeroOnly() {
        // n=0 -> [0]
        assertArrayEquals(new int[]{0}, sol.countBits(0));
    }

    @Test
    void n1() {
        // n=1 -> [0,1]
        assertArrayEquals(new int[]{0, 1}, sol.countBits(1));
    }

    @Test
    void n8() {
        // 8 = 0b1000 -> 1 set bit
        // [0,1,1,2,1,2,2,3,1]
        assertArrayEquals(new int[]{0, 1, 1, 2, 1, 2, 2, 3, 1}, sol.countBits(8));
    }
}
