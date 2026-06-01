package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CoinChangeIITest {

    private final CoinChangeII solution = new CoinChangeII();

    @Test
    void testExample1() {
        assertEquals(4, solution.change(5, new int[]{1, 2, 5}));
    }

    @Test
    void testNoWay() {
        assertEquals(0, solution.change(3, new int[]{2}));
    }

    @Test
    void testSingleCoin() {
        assertEquals(1, solution.change(10, new int[]{10}));
    }
}
