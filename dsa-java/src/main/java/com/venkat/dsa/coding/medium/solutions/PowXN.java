package com.venkat.dsa.coding.medium.solutions;

/**
 * Pow(x, n) (LeetCode 50)
 *
 * <p>Computes x raised to the power n using fast exponentiation (binary exponentiation / exponentiation
 * by squaring): if n is even, x^n = (x^2)^(n/2); if odd, x^n = x * x^(n-1). Handles negative n
 * by inverting x and negating n. Uses a long for n to safely handle Integer.MIN_VALUE negation.
 *
 * <p><b>Key insight:</b> Halving the exponent each step gives O(log n) multiplications instead of O(n).
 *
 * <p><b>Time complexity:</b> O(log n). <b>Space complexity:</b> O(1) (iterative).
 */
public class PowXN {

    /**
     * Returns x raised to the power n.
     *
     * @param x base (double)
     * @param n exponent (int, may be negative)
     * @return x^n
     */
    public double myPow(double x, int n) {
        long exp = n; // use long to avoid overflow on -Integer.MIN_VALUE
        if (exp < 0) {
            x = 1.0 / x;
            exp = -exp;
        }

        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) { // odd exponent: multiply current x into result
                result *= x;
            }
            x *= x;   // square the base
            exp >>= 1; // halve the exponent
        }
        return result;
    }
}
