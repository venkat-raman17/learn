package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Pow(x, n)
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Math & Geometry
 * <p>URL: https://leetcode.com/problems/powx-n/
 *
 * <p>Implement pow(x, n), which calculates x raised to the power n (i.e., x^n).
 *
 * <p>Constraints:
 * <ul>
 *   <li>-100.0 < x < 100.0</li>
 *   <li>-2^31 <= n <= 2^31 - 1</li>
 *   <li>n is an integer</li>
 *   <li>Either x is not zero or n > 0</li>
 *   <li>-10^4 <= x^n <= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   myPow(2.00000, 10)  -> 1024.00000
 *   myPow(2.10000, 3)   -> 9.26100
 *   myPow(2.00000, -2)  -> 0.25000
 * </pre>
 *
 * <p>Target: Time O(log n), Space O(log n) recursive / O(1) iterative.
 *
 * <p>Hint 1: Use fast exponentiation: x^n = (x^(n/2))^2 for even n, x * x^(n-1) for odd n.
 * <p>Hint 2: Handle negative exponents by computing 1 / myPow(x, -n); be careful of Integer.MIN_VALUE overflow.
 */
public class PowXN {

    public double myPow(double x, int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
