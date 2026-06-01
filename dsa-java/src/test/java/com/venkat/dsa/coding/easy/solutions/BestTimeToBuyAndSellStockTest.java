package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BestTimeToBuyAndSellStockTest {

    private final BestTimeToBuyAndSellStock solution = new BestTimeToBuyAndSellStock();

    // Official LeetCode examples
    @Test
    void example1_profitExists() {
        // Buy day 1 (price=1), sell day 4 (price=6) → profit 5
        assertEquals(5, solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }

    @Test
    void example2_alwaysDecreasing() {
        // Prices only go down — best is to not trade, profit = 0
        assertEquals(0, solution.maxProfit(new int[]{7, 6, 4, 3, 1}));
    }

    // Edge cases
    @Test
    void singleDay() {
        assertEquals(0, solution.maxProfit(new int[]{5}));
    }

    @Test
    void twoDaysProfitable() {
        assertEquals(3, solution.maxProfit(new int[]{2, 5}));
    }

    @Test
    void twoDaysLoss() {
        assertEquals(0, solution.maxProfit(new int[]{5, 2}));
    }

    @Test
    void buyOnFirstSellOnLast() {
        // Best buy=1 (index 0), best sell=100 (index 4)
        assertEquals(99, solution.maxProfit(new int[]{1, 2, 3, 4, 100}));
    }

    @Test
    void peakInMiddle() {
        // Buy at 1, sell at 10
        assertEquals(9, solution.maxProfit(new int[]{3, 1, 10, 2, 5}));
    }

    @Test
    void allSamePrice() {
        assertEquals(0, solution.maxProfit(new int[]{4, 4, 4, 4}));
    }

    @Test
    void minPriceAfterPeak() {
        // [3,2,6,5,0,3]: buy at 2, sell at 6 → 4
        assertEquals(4, solution.maxProfit(new int[]{3, 2, 6, 5, 0, 3}));
    }

    @Test
    void largeSpread() {
        assertEquals(9999, solution.maxProfit(new int[]{1, 10000}));
    }
}
