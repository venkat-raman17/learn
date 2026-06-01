package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Best Time to Buy and Sell Stock
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Sliding Window
 * <p>URL: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 *
 * <p>Given an array {@code prices} where {@code prices[i]} is the price of a stock on day {@code i},
 * return the maximum profit achievable by buying on one day and selling on a later day.
 * If no profit is possible, return 0.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= prices.length &lt;= 10^5</li>
 *   <li>0 &lt;= prices[i] &lt;= 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  prices = [7,1,5,3,6,4]
 *   Output: 5   (buy day 2 at 1, sell day 5 at 6)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  prices = [7,6,4,3,1]
 *   Output: 0   (prices only decrease, no profit possible)
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space
 *
 * <p>Hint 1: Track the minimum price seen so far as you scan left to right.
 * <p>Hint 2: At each step compute profit = currentPrice - minPrice and update the running maximum.
 */
public class BestTimeToBuyAndSellStock {

    public int maxProfit(int[] prices) {
        throw new UnsupportedOperationException("implement me");
    }
}
