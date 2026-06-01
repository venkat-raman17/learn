package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinChangeTest {

    private final CoinChange solution = new CoinChange();

    @Test
    void example1() {
        // coins=[1,2,5], amount=11 -> 5+5+1 = 3 coins
        assertEquals(3, solution.coinChange(new int[]{1, 2, 5}, 11));
    }

    @Test
    void example2_impossible() {
        // coins=[2], amount=3 -> impossible -> -1
        assertEquals(-1, solution.coinChange(new int[]{2}, 3));
    }

    @Test
    void example3_zeroAmount() {
        // amount=0 -> 0 coins always
        assertEquals(0, solution.coinChange(new int[]{1}, 0));
    }

    @Test
    void singleCoinExactMatch() {
        assertEquals(1, solution.coinChange(new int[]{5}, 5));
    }

    @Test
    void singleCoinMultiple() {
        // coins=[3], amount=9 -> 3 coins
        assertEquals(3, solution.coinChange(new int[]{3}, 9));
    }

    @Test
    void largerAmount() {
        // coins=[1,5,10,25], amount=36 -> 25+10+1 = 3 coins
        assertEquals(3, solution.coinChange(new int[]{1, 5, 10, 25}, 36));
    }

    @Test
    void exactOneCoin() {
        assertEquals(1, solution.coinChange(new int[]{1, 2, 5}, 5));
    }
}
