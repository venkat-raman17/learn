package com.venkat.dsa.coding.easy.solutions;

/**
 * Single Number (LeetCode #136)
 *
 * Approach: XOR every element together. XOR is commutative and associative, and
 * x ^ x == 0 for any x, while x ^ 0 == x. Therefore all duplicate pairs cancel
 * out, leaving only the element that appears exactly once.
 *
 * Key insight: XOR is a bitwise no-carry addition; pairing identical numbers
 * always yields 0, so the single unpaired number survives as the accumulator.
 *
 * Time complexity:  O(n) — one pass through the array.
 * Space complexity: O(1) — only one integer accumulator.
 */
public class SingleNumber {

    public int singleNumber(int[] nums) {
        int result = 0;
        for (int n : nums) {
            result ^= n; // paired duplicates cancel; lone element remains
        }
        return result;
    }
}
