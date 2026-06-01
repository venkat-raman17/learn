package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Sum of Two Integers
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/sum-of-two-integers/
 *
 * <p>Given two integers a and b, return their sum without using the operators + or -.
 *
 * <p>Constraints:
 * <ul>
 *   <li>-1000 &lt;= a, b &lt;= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: a = 1, b = 2  Output: 3
 *   Input: a = 2, b = 3  Output: 5
 * </pre>
 *
 * <p>Target: Time O(1) — at most 32 iterations, Space O(1)
 *
 * <p>Hint 1: XOR gives the sum without carries; AND followed by left-shift gives only the carries.
 * <p>Hint 2: Repeat: a = a ^ b (partial sum), b = (a_orig &amp; b) &lt;&lt; 1 (carry); stop when b == 0.
 */
public class SumOfTwoIntegers {

    public int getSum(int a, int b) {
        throw new UnsupportedOperationException("implement me");
    }
}
