package com.venkat.dsa.coding.easy.solutions;

/**
 * Reverse Bits (LeetCode #190)
 *
 * Approach: Iterate 32 times (one per bit). On each step, shift the result
 * left by 1, then OR in the lowest bit of n (extracted with n & 1), then
 * shift n right by 1 to process the next bit.
 *
 * Key insight: We consume n from LSB to MSB while building result from MSB
 * to LSB. After 32 iterations the bits are fully reversed. Using >>> ensures
 * an unsigned right shift so the sign bit of n is treated as data.
 *
 * Time complexity:  O(1) — always exactly 32 iterations.
 * Space complexity: O(1).
 */
public class ReverseBits {

    public int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1); // append lowest bit of n to result
            n >>>= 1;                          // unsigned right-shift (treat n as unsigned)
        }
        return result;
    }
}
