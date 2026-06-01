package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class FindTheDuplicateNumberTest {

    @Test
    void testDuplicateIsTwo() {
        FindTheDuplicateNumber sol = new FindTheDuplicateNumber();
        assertEquals(2, sol.findDuplicate(new int[]{1, 3, 4, 2, 2}));
    }

    @Test
    void testDuplicateIsThree() {
        FindTheDuplicateNumber sol = new FindTheDuplicateNumber();
        assertEquals(3, sol.findDuplicate(new int[]{3, 1, 3, 4, 2}));
    }

    @Test
    void testDuplicateAtStart() {
        FindTheDuplicateNumber sol = new FindTheDuplicateNumber();
        assertEquals(1, sol.findDuplicate(new int[]{1, 1}));
    }
}
