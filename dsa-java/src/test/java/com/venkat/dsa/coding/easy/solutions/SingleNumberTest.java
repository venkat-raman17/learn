package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SingleNumberTest {

    private final SingleNumber sol = new SingleNumber();

    @Test
    void example1_officialExample() {
        // [2,2,1] -> 1
        assertEquals(1, sol.singleNumber(new int[]{2, 2, 1}));
    }

    @Test
    void example2_officialExample() {
        // [4,1,2,1,2] -> 4
        assertEquals(4, sol.singleNumber(new int[]{4, 1, 2, 1, 2}));
    }

    @Test
    void example3_officialExample() {
        // [1] -> 1
        assertEquals(1, sol.singleNumber(new int[]{1}));
    }

    @Test
    void negativeNumbers() {
        // [-3, -3, -7] -> -7
        assertEquals(-7, sol.singleNumber(new int[]{-3, -3, -7}));
    }

    @Test
    void largerArray() {
        // [5,3,5,4,3] -> 4
        assertEquals(4, sol.singleNumber(new int[]{5, 3, 5, 4, 3}));
    }
}
