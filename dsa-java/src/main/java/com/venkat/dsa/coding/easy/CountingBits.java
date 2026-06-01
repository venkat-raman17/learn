package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Counting Bits
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/counting-bits/
 *
 * <p>Given an integer n, return an array ans of length n + 1 such that for each i (0 &lt;= i &lt;= n),
 * ans[i] is the number of 1's in the binary representation of i.
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 &lt;= n &lt;= 10^5</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: n = 2  Output: [0, 1, 1]
 *   Input: n = 5  Output: [0, 1, 1, 2, 1, 2]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(n) (excluding output)
 *
 * <p>Hint 1: The number of 1-bits in i equals the number of 1-bits in i &gt;&gt; 1, plus the lowest bit (i &amp; 1).
 * <p>Hint 2: Use dynamic programming: dp[i] = dp[i &gt;&gt; 1] + (i &amp; 1).
 */
public class CountingBits {

    public int[] countBits(int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
