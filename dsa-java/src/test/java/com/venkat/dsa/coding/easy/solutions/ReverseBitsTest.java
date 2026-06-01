package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReverseBitsTest {

    private final ReverseBits sol = new ReverseBits();

    @Test
    void example1_officialExample() {
        // Input:  00000010100101000001111010011100 = 43261596
        // Output: 00111001011110000010100101000000 = 964176192
        assertEquals(964176192, sol.reverseBits(43261596));
    }

    @Test
    void example2_officialExample() {
        // Input:  11111111111111111111111111111101 = -3 (signed) / 4294967293 unsigned
        // Reversed: 10111111111111111111111111111111 = -1073741825 (signed)
        assertEquals(0xBFFFFFFF, sol.reverseBits(0xFFFFFFFD));
    }

    @Test
    void zero() {
        // All bits zero -> still zero
        assertEquals(0, sol.reverseBits(0));
    }

    @Test
    void allOnes() {
        // 0xFFFFFFFF reversed is still 0xFFFFFFFF
        assertEquals(0xFFFFFFFF, sol.reverseBits(0xFFFFFFFF));
    }

    @Test
    void onlyMsb() {
        // 0x80000000 reversed -> 0x00000001
        assertEquals(1, sol.reverseBits(0x80000000));
    }

    @Test
    void onlyLsb() {
        // 1 reversed -> 0x80000000
        assertEquals(0x80000000, sol.reverseBits(1));
    }
}
