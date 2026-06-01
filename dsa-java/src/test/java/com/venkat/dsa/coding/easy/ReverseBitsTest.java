package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class ReverseBitsTest {

    private final ReverseBits solution = new ReverseBits();

    @Test
    public void testExample1() {
        // 00000010100101000001111010011100 -> 00111001011110000010100101000000
        assertEquals(964176192, solution.reverseBits(43261596));
    }

    @Test
    public void testExample2() {
        // 11111111111111111111111111111101 -> 10111111111111111111111111111111
        assertEquals(-1073741825, solution.reverseBits(-3));
    }

    @Test
    public void testAllOnes() {
        // 11111111111111111111111111111111 reversed is still all ones
        assertEquals(-1, solution.reverseBits(-1));
    }
}
