package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecodeWaysTest {

    private final DecodeWays solution = new DecodeWays();

    @Test
    void example1() {
        // "12" -> "AB"(1,2) or "L"(12) -> 2
        assertEquals(2, solution.numDecodings("12"));
    }

    @Test
    void example2() {
        // "226" -> "BBF"(2,2,6) / "BZ"(2,26) / "VF"(22,6) -> 3
        assertEquals(3, solution.numDecodings("226"));
    }

    @Test
    void example3_leadingZero() {
        // "06" -> '0' cannot be decoded alone, no valid decode -> 0
        assertEquals(0, solution.numDecodings("06"));
    }

    @Test
    void singleDigit() {
        // "5" -> only "E" -> 1
        assertEquals(1, solution.numDecodings("5"));
    }

    @Test
    void zero_alone() {
        // "0" -> 0 ways
        assertEquals(0, solution.numDecodings("0"));
    }

    @Test
    void zeroAfterValidTwoDigit() {
        // "10" -> "J"(10) -> 1
        assertEquals(1, solution.numDecodings("10"));
    }

    @Test
    void consecutiveZeros() {
        // "100" -> "10" is valid, then "0" alone is invalid -> 0
        assertEquals(0, solution.numDecodings("100"));
    }

    @Test
    void allOnes() {
        // "111" -> "AAA","KA","AK" -> 3
        assertEquals(3, solution.numDecodings("111"));
    }

    @Test
    void boundary27() {
        // "27" -> only "BG" (2,7); 27 > 26 so cannot be treated as one letter -> 1
        assertEquals(1, solution.numDecodings("27"));
    }
}
