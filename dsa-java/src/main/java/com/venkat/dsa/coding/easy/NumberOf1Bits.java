package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Number of 1 Bits
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/number-of-1-bits/
 *
 * <p>Given an integer n, return the number of set bits (1s) in its binary representation
 * (also known as the Hamming weight).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= n &lt;= 2^31 - 1</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: n = 11  (binary: 00000000000000000000000000001011)  Output: 3
 *   Input: n = 128 (binary: 00000000000000000000000010000000) Output: 1
 * </pre>
 *
 * <p>Target: Time O(1) — at most 32 iterations, Space O(1)
 *
 * <p>Hint 1: Use n &amp; 1 to check the least significant bit, then right-shift n by 1.
 * <p>Hint 2: The trick n = n &amp; (n - 1) clears the lowest set bit each iteration — loop until n == 0.
 */
public class NumberOf1Bits {

    public int hammingWeight(int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
