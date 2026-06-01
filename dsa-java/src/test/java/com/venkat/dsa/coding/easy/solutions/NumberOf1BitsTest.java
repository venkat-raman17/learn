package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumberOf1BitsTest {

    private final NumberOf1Bits sol = new NumberOf1Bits();

    @Test
    void example1_officialExample() {
        // 00000000000000000000000000001011 -> 3
        assertEquals(3, sol.hammingWeight(0b00000000000000000000000000001011));
    }

    @Test
    void example2_officialExample() {
        // 00000000000000000000000010000000 -> 1
        assertEquals(1, sol.hammingWeight(0b00000000000000000000000010000000));
    }

    @Test
    void example3_officialExample() {
        // 11111111111111111111111111111101 -> 31  (all bits set except bit 1)
        // In Java: 0xFFFFFFFD as int = -3
        assertEquals(31, sol.hammingWeight(0xFFFFFFFD));
    }

    @Test
    void zero() {
        assertEquals(0, sol.hammingWeight(0));
    }

    @Test
    void allBitsSet() {
        // 0xFFFFFFFF = -1 in signed int, but all 32 bits are 1
        assertEquals(32, sol.hammingWeight(0xFFFFFFFF));
    }

    @Test
    void powerOfTwo() {
        // 16 = 0b10000 -> 1 bit
        assertEquals(1, sol.hammingWeight(16));
    }
}
