package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class NumberOf1BitsTest {

    private final NumberOf1Bits solution = new NumberOf1Bits();

    @Test
    public void testEleven() {
        // 11 in binary = 1011 → three 1-bits
        assertEquals(3, solution.hammingWeight(11));
    }

    @Test
    public void testOneTwentyEight() {
        // 128 in binary = 10000000 → one 1-bit
        assertEquals(1, solution.hammingWeight(128));
    }

    @Test
    public void testMaxInt() {
        // 2147483645 = 0111 1111 1111 1111 1111 1111 1111 1101 → 30 ones
        assertEquals(30, solution.hammingWeight(2147483645));
    }
}
