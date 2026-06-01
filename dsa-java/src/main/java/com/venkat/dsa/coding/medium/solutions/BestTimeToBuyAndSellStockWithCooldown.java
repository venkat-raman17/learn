package com.venkat.dsa.coding.medium.solutions;

/**
 * Best Time to Buy and Sell Stock with Cooldown (LeetCode 309)
 *
 * Approach: State-machine DP with three states per day:
 *   holding  — currently holding a stock (either bought today or already held)
 *   sold     — just sold today (will enter cooldown next day)
 *   cooldown — in cooldown or idle (not holding, not just sold)
 *
 * Transitions each day:
 *   holding  = max(prev_holding, prev_cooldown - price)  // keep holding OR buy from idle
 *   sold     = prev_holding + price                       // sell today
 *   cooldown = max(prev_cooldown, prev_sold)              // idle or just exited cooldown
 *
 * Time:  O(n)
 * Space: O(1)
 *
 * Key insight: tracking the "just sold" state naturally enforces the 1-day cooldown
 * without needing an explicit look-back.
 */
public class BestTimeToBuyAndSellStockWithCooldown {

    public int maxProfit(int[] prices) {
        int holding = Integer.MIN_VALUE; // max profit while holding a stock
        int sold = 0;                   // max profit on the day we just sold
        int cooldown = 0;               // max profit while on cooldown / idle

        for (int price : prices) {
            int prevHolding = holding;
            int prevSold = sold;
            int prevCooldown = cooldown;

            holding  = Math.max(prevHolding, prevCooldown - price); // keep or buy
            sold     = prevHolding + price;                         // sell today
            cooldown = Math.max(prevCooldown, prevSold);            // idle or exit cooldown
        }
        // The answer is the max of "just sold" or "cooldown/idle" (never want to hold at end)
        return Math.max(sold, cooldown);
    }
}
