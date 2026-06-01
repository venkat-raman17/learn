package com.venkat.dsa.coding.easy.solutions;

/**
 * Best Time to Buy and Sell Stock (LeetCode 121) — Easy
 *
 * <p>Approach: Single-pass sliding window / greedy scan. Track the minimum price seen so
 * far ({@code minPrice}). At every index compute the profit if we sold today, and update
 * the global maximum. No need to store a window explicitly — the "left pointer" is the
 * position of {@code minPrice} and advances whenever we find a cheaper buy day.
 *
 * <p><b>Time complexity:</b> O(n) — one pass through the prices array.<br>
 * <b>Space complexity:</b> O(1) — only two scalar variables are maintained.
 *
 * <p><b>Key insight:</b> We can never improve profit by buying on a day after the current
 * sell day, so greedily keeping the running minimum buy price is always optimal.
 */
public class BestTimeToBuyAndSellStock {

    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {
            if (price < minPrice) {
                // Found a cheaper buy day — shift the "left pointer"
                minPrice = price;
            } else if (price - minPrice > maxProfit) {
                // Selling today beats our best profit so far
                maxProfit = price - minPrice;
            }
        }

        return maxProfit;
    }
}
