package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class SumOfTwoIntegersTest {

    private final SumOfTwoIntegers solution = new SumOfTwoIntegers();

    @Test
    public void testExample1() {
        assertEquals(3, solution.getSum(1, 2));
    }

    @Test
    public void testExample2() {
        assertEquals(5, solution.getSum(2, 3));
    }

    @Test
    public void testNegative() {
        assertEquals(-1, solution.getSum(1, -2));
    }
}
