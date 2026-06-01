package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Koko Eating Bananas
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Binary Search
 * <p>URL: https://leetcode.com/problems/koko-eating-bananas/
 *
 * <p>Koko can decide her bananas-per-hour eating speed {@code k}. Each hour she picks one pile
 * and eats up to {@code k} bananas from it. Given piles of bananas and {@code h} hours, return
 * the minimum integer {@code k} such that she can eat all bananas within {@code h} hours.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= piles.length &lt;= 10^4</li>
 *   <li>piles.length &lt;= h &lt;= 10^9</li>
 *   <li>1 &lt;= piles[i] &lt;= 10^9</li>
 * </ul>
 *
 * <p>Example 1: piles = [3,6,7,11], h = 8 → 4
 * <p>Example 2: piles = [30,11,23,4,20], h = 5 → 30
 *
 * <p>Target: O(n log(max(piles))) time, O(1) space.
 *
 * <p>Hint 1: Binary search on the answer {@code k} in range [1, max(piles)].
 * <p>Hint 2: For a candidate speed {@code k}, compute total hours as sum of ceil(pile/k) and check if it fits in h.
 */
public class KokoEatingBananas {

    public int minEatingSpeed(int[] piles, int h) {
        throw new UnsupportedOperationException("implement me");
    }
}
