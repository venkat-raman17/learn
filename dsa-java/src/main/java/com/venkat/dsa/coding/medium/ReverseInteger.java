package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Reverse Integer
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/reverse-integer/
 *
 * <p>Given a signed 32-bit integer x, return x with its digits reversed. If reversing x
 * causes the value to go outside the signed 32-bit integer range [-2^31, 2^31 - 1], return 0.
 *
 * <p>Constraints:
 * <ul>
 *   <li>-2^31 &lt;= x &lt;= 2^31 - 1</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: x = 123   Output: 321
 *   Input: x = -123  Output: -321
 *   Input: x = 120   Output: 21
 * </pre>
 *
 * <p>Target: Time O(log x) — proportional to digit count, Space O(1)
 *
 * <p>Hint 1: Extract digits one at a time using x % 10 and x / 10; build the reversed number digit by digit.
 * <p>Hint 2: Before appending a digit, check whether result would overflow: if result &gt; Integer.MAX_VALUE / 10 (or &lt; Integer.MIN_VALUE / 10), return 0.
 */
public class ReverseInteger {

    public int reverse(int x) {
        throw new UnsupportedOperationException("implement me");
    }
}
