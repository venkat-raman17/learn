package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BestTimeToBuyAndSellStockWithCooldownTest {

    private final BestTimeToBuyAndSellStockWithCooldown sol =
            new BestTimeToBuyAndSellStockWithCooldown();

    // LeetCode example 1: [1,2,3,0,2] -> 3
    // Buy@1, sell@3, cooldown, buy@0, sell@2 = 2+2 = 4? No:
    // Optimal: buy@1(day0), sell@3(day2), cooldown(day3), buy@0(day3)... wait
    // LeetCode says answer is 3: buy@1, sell@2 (+1), cooldown, buy@0, sell@2 (+2) = 3
    @Test
    void example1() {
        assertEquals(3, sol.maxProfit(new int[]{1, 2, 3, 0, 2}));
    }

    // Single price: no transaction possible
    @Test
    void singlePrice() {
        assertEquals(0, sol.maxProfit(new int[]{1}));
    }

    // Always decreasing: no profit
    @Test
    void alwaysDecreasing() {
        assertEquals(0, sol.maxProfit(new int[]{5, 4, 3, 2, 1}));
    }

    // Two prices, increasing: buy then sell
    @Test
    void twoPricesProfit() {
        assertEquals(1, sol.maxProfit(new int[]{1, 2}));
    }

    // Two prices, decreasing
    @Test
    void twoPricesNoProfit() {
        assertEquals(0, sol.maxProfit(new int[]{2, 1}));
    }

    // [6,1,3,2,4,7] -> 6: buy@1,sell@3(+2),cooldown,buy@2,sell@7(+5)?
    // cooldown constraint: sell day2, cooldown day3, buy day4, sell day5 => 2+5=7?
    // Let's verify: buy@idx1(price=1), sell@idx2(price=3) profit=2, cooldown@idx3,
    // buy@idx4(price=4), sell@idx5(price=7) profit=3. Total=5.
    // Or buy@idx1(1),sell@idx5(7) profit=6. Total=6.
    @Test
    void longerSequence() {
        assertEquals(6, sol.maxProfit(new int[]{6, 1, 3, 2, 4, 7}));
    }

    // All same price: 0 profit
    @Test
    void allSame() {
        assertEquals(0, sol.maxProfit(new int[]{3, 3, 3, 3}));
    }
}
