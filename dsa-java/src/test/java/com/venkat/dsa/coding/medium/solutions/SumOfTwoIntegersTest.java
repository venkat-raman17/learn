package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SumOfTwoIntegersTest {

    private final SumOfTwoIntegers sol = new SumOfTwoIntegers();

    @Test
    void example1_officialExample() {
        // 1 + 2 = 3
        assertEquals(3, sol.getSum(1, 2));
    }

    @Test
    void example2_officialExample() {
        // 2 + 3 = 5
        assertEquals(5, sol.getSum(2, 3));
    }

    @Test
    void negativePlusPositive() {
        // -1 + 1 = 0
        assertEquals(0, sol.getSum(-1, 1));
    }

    @Test
    void bothNegative() {
        // -3 + -5 = -8
        assertEquals(-8, sol.getSum(-3, -5));
    }

    @Test
    void zeroAddend() {
        // 7 + 0 = 7
        assertEquals(7, sol.getSum(7, 0));
    }

    @Test
    void largeValues() {
        // 1000 + 999 = 1999
        assertEquals(1999, sol.getSum(1000, 999));
    }

    @Test
    void minInt() {
        // Integer.MIN_VALUE + 0 = Integer.MIN_VALUE
        assertEquals(Integer.MIN_VALUE, sol.getSum(Integer.MIN_VALUE, 0));
    }
}
