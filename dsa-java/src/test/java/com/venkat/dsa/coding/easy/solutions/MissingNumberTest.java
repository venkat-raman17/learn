package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MissingNumberTest {

    private final MissingNumber sol = new MissingNumber();

    @Test
    void example1_officialExample() {
        // [3,0,1] -> missing 2
        assertEquals(2, sol.missingNumber(new int[]{3, 0, 1}));
    }

    @Test
    void example2_officialExample() {
        // [0,1] -> missing 2
        assertEquals(2, sol.missingNumber(new int[]{0, 1}));
    }

    @Test
    void example3_officialExample() {
        // [9,6,4,2,3,5,7,0,1] -> missing 8
        assertEquals(8, sol.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
    }

    @Test
    void missingZero() {
        // [1] -> missing 0
        assertEquals(0, sol.missingNumber(new int[]{1}));
    }

    @Test
    void missingN() {
        // [0] -> missing 1
        assertEquals(1, sol.missingNumber(new int[]{0}));
    }

    @Test
    void sortedArray() {
        // [0,1,2,4] -> missing 3
        assertEquals(3, sol.missingNumber(new int[]{0, 1, 2, 4}));
    }
}
