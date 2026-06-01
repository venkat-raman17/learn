package com.venkat.dsa.coding.easy.solutions;

/**
 * Counting Bits (LeetCode #338)
 *
 * Approach: Dynamic programming using the recurrence dp[i] = dp[i >> 1] + (i & 1).
 * Shifting right by 1 removes the lowest bit; the popcount of i equals the
 * popcount of (i / 2) plus whether i is odd (has its lowest bit set).
 *
 * Key insight: Every integer i has the same bit pattern as i/2 but with one
 * extra bit potentially set at position 0, so we can build the answer table
 * for all integers 0..n in a single forward pass in O(n) time and space.
 *
 * Time complexity:  O(n) — one pass 1..n.
 * Space complexity: O(n) — the output array (no extra space beyond result).
 */
public class CountingBits {

    public int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i >> 1] + (i & 1); // popcount(i) = popcount(i/2) + lowest bit
        }
        return dp;
    }
}
