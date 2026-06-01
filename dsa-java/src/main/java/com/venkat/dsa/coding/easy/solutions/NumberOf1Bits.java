package com.venkat.dsa.coding.easy.solutions;

/**
 * Number of 1 Bits (LeetCode #191) — also called Hamming Weight
 *
 * Approach: Use the classic bit-manipulation trick n &= (n - 1) which clears
 * the lowest set bit of n on every iteration. Count how many iterations are
 * needed to reduce n to zero; that count equals the number of 1 bits.
 *
 * Key insight: n - 1 flips the lowest set bit and all trailing zeros of n,
 * so n & (n - 1) isolates and removes exactly one 1-bit per step, giving a
 * loop that runs exactly as many times as there are set bits.
 *
 * Time complexity:  O(k) where k is the number of set bits (at most 32).
 * Space complexity: O(1).
 */
public class NumberOf1Bits {

    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1); // clear lowest set bit
            count++;
        }
        return count;
    }
}
