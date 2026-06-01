package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class MissingNumberTest {

    private final MissingNumber solution = new MissingNumber();

    @Test
    public void testExample1() {
        assertEquals(2, solution.missingNumber(new int[]{3, 0, 1}));
    }

    @Test
    public void testExample2() {
        assertEquals(2, solution.missingNumber(new int[]{0, 1}));
    }

    @Test
    public void testExample3() {
        assertEquals(8, solution.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
    }
}
