package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class BestTimeToBuyAndSellStockTest {

    private final BestTimeToBuyAndSellStock solution = new BestTimeToBuyAndSellStock();

    @Test
    void example1_profitExists() {
        assertEquals(5, solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }

    @Test
    void example2_decreasingPrices() {
        assertEquals(0, solution.maxProfit(new int[]{7, 6, 4, 3, 1}));
    }

    @Test
    void singleDay_noProfit() {
        assertEquals(0, solution.maxProfit(new int[]{5}));
    }
}
