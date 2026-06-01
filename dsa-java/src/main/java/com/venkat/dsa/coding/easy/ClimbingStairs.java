package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Climbing Stairs
 *
 * <p>Difficulty: EASY
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/climbing-stairs/
 *
 * <p>You are climbing a staircase with {@code n} steps. Each time you can climb 1 or 2 steps.
 * Return the number of distinct ways to reach the top.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= n &lt;= 45</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: n = 2  -> Output: 2  (1+1, 2)
 * Input: n = 3  -> Output: 3  (1+1+1, 1+2, 2+1)
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Ways to reach step i = ways to reach step i-1 + ways to reach step i-2.
 * <p>Hint 2: This is exactly the Fibonacci sequence — you only need two variables.
 */
public class ClimbingStairs {

    public int climbStairs(int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
