package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Best Time to Buy and Sell Stock with Cooldown
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
 *
 * <p>Given an integer array prices where prices[i] is the price on day i, find the
 * maximum profit with unlimited transactions but a 1-day cooldown after every sell
 * (you cannot buy on the day immediately after a sell).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= prices.length <= 5000</li>
 *   <li>0 <= prices[i] <= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   maxProfit([1,2,3,0,2]) => 3   // buy day0, sell day1, cooldown day2, buy day3, sell day4
 *   maxProfit([1])         => 0
 * </pre>
 *
 * <p>Target Time: O(n), Space: O(n) or O(1)
 *
 * <p>Hint 1: Model three states per day: holding, sold (just sold), cooldown (idle after cooldown).
 * <p>Hint 2: Transitions: holding[i] = max(holding[i-1], cooldown[i-1] - price[i]);
 * sold[i] = holding[i-1] + price[i]; cooldown[i] = max(cooldown[i-1], sold[i-1]).
 */
public class BestTimeToBuyAndSellStockWithCooldown {

    public int maxProfit(int[] prices) {
        throw new UnsupportedOperationException("implement me");
    }
}
