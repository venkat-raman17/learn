package com.venkat.dsa.coding.easy.solutions;

/**
 * Plus One (LeetCode 66)
 *
 * <p>Given a large integer represented as an array of digits (most-significant first), add one
 * to the integer and return the resulting digit array. Traverse from the least-significant digit,
 * propagating carry; if carry still exists after the loop (all 9s), prepend a new leading 1.
 *
 * <p><b>Key insight:</b> Only need a new array in the all-nines case; otherwise mutate in place.
 *
 * <p><b>Time complexity:</b> O(n). <b>Space complexity:</b> O(n) worst case (all nines), O(1) otherwise.
 */
public class PlusOne {

    /**
     * Increments the integer represented by {@code digits} by one.
     *
     * @param digits non-empty array of digits [0..9], no leading zeros except single digit 0
     * @return digit array of the incremented integer
     */
    public int[] plusOne(int[] digits) {
        // Walk from right to left, add carry
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;   // no carry — done
                return digits;
            }
            digits[i] = 0;    // digit was 9, becomes 0, carry propagates
        }
        // All digits were 9 (e.g. 999 -> 1000)
        int[] result = new int[digits.length + 1];
        result[0] = 1; // remaining positions are already 0
        return result;
    }
}
