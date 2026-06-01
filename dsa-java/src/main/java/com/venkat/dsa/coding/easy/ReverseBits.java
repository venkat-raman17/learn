package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Reverse Bits
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/reverse-bits/
 *
 * <p>Reverse the bits of a given 32-bit unsigned integer and return the resulting integer.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The input must be treated as an unsigned 32-bit integer.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input:  n = 43261596  (binary: 00000010100101000001111010011100)
 *   Output: 964176192     (binary: 00111001011110000010100101000000)
 *
 *   Input:  n = -3 (as unsigned: 4294967293, binary: 11111111111111111111111111111101)
 *   Output: -1073741825 (binary: 10111111111111111111111111111111)
 * </pre>
 *
 * <p>Target: Time O(1) — exactly 32 iterations, Space O(1)
 *
 * <p>Hint 1: Extract the LSB of n with (n &amp; 1), append it to a result by left-shifting result and OR-ing the bit.
 * <p>Hint 2: Right-shift n by 1 after each step; repeat 32 times total.
 */
public class ReverseBits {

    public int reverseBits(int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
