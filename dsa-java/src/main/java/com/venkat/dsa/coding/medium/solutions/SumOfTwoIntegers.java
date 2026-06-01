package com.venkat.dsa.coding.medium.solutions;

/**
 * Sum of Two Integers (LeetCode #371)
 *
 * Approach: Simulate binary addition without using the + or - operators.
 * The sum of bits without carry is a ^ b (XOR). The carry is (a & b) << 1
 * (AND shifted left). Repeat with a = sum-without-carry and b = carry until
 * there is no carry remaining.
 *
 * Key insight: At each iteration XOR handles the partial sum and AND captures
 * the carry bits; iterating until carry is zero correctly simulates full
 * binary addition using only bitwise operations.
 *
 * Time complexity:  O(1) — at most 32 iterations for 32-bit integers.
 * Space complexity: O(1).
 */
public class SumOfTwoIntegers {

    public int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1; // compute carry bits before modifying a
            a = a ^ b;                // sum without carry
            b = carry;                // carry becomes the next addend
        }
        return a;
    }
}
