package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoinChangeIITest {

    private final CoinChangeII sol = new CoinChangeII();

    // LeetCode example 1: amount=5, coins=[1,2,5] -> 4
    // Combinations: [1,1,1,1,1],[1,1,1,2],[1,2,2],[5]
    @Test
    void example1() {
        assertEquals(4, sol.change(5, new int[]{1, 2, 5}));
    }

    // LeetCode example 2: amount=3, coins=[2] -> 0 (can't make 3 with only 2s)
    @Test
    void example2() {
        assertEquals(0, sol.change(3, new int[]{2}));
    }

    // LeetCode example 3: amount=10, coins=[10] -> 1
    @Test
    void example3() {
        assertEquals(1, sol.change(10, new int[]{10}));
    }

    // Amount 0: exactly one way (use no coins)
    @Test
    void amountZero() {
        assertEquals(1, sol.change(0, new int[]{1, 2, 3}));
    }

    // No coins: can only make amount 0 (handled above); amount>0 -> 0
    @Test
    void noCoins() {
        assertEquals(0, sol.change(5, new int[]{}));
    }

    // amount=1, coins=[1] -> 1
    @Test
    void single() {
        assertEquals(1, sol.change(1, new int[]{1}));
    }
}
