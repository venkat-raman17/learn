package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BestTimeToBuyAndSellStockWithCooldownTest {

    private final BestTimeToBuyAndSellStockWithCooldown solution =
            new BestTimeToBuyAndSellStockWithCooldown();

    @Test
    void testExample1() {
        assertEquals(3, solution.maxProfit(new int[]{1, 2, 3, 0, 2}));
    }

    @Test
    void testSinglePrice() {
        assertEquals(0, solution.maxProfit(new int[]{1}));
    }

    @Test
    void testDecreasingPrices() {
        assertEquals(0, solution.maxProfit(new int[]{5, 4, 3, 2, 1}));
    }
}
