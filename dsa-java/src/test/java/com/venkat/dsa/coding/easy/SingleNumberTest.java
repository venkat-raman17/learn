package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class SingleNumberTest {

    private final SingleNumber solution = new SingleNumber();

    @Test
    public void testExample1() {
        assertEquals(1, solution.singleNumber(new int[]{2, 2, 1}));
    }

    @Test
    public void testExample2() {
        assertEquals(4, solution.singleNumber(new int[]{4, 1, 2, 1, 2}));
    }

    @Test
    public void testSingleElement() {
        assertEquals(1, solution.singleNumber(new int[]{1}));
    }
}
