package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CoinChangeTest {

    private final CoinChange solution = new CoinChange();

    @Test
    public void testExample1() {
        assertEquals(3, solution.coinChange(new int[]{1, 2, 5}, 11));
    }

    @Test
    public void testImpossible() {
        assertEquals(-1, solution.coinChange(new int[]{2}, 3));
    }

    @Test
    public void testAmountZero() {
        assertEquals(0, solution.coinChange(new int[]{1}, 0));
    }
}
