package com.venkat.dsa.coding.easy.solutions;

/**
 * Missing Number (LeetCode #268)
 *
 * Approach: XOR every index 0..n with every value in nums. Indices and values
 * form pairs; only the missing number has no matching index partner and is left
 * as the sole survivor after all XOR cancellations.
 *
 * Key insight: For a complete range 0..n, XOR(all indices) ^ XOR(all values)
 * cancels every present number because x ^ x == 0, leaving the one missing
 * number that never got cancelled. This avoids any risk of integer overflow
 * that the arithmetic sum approach (expectedSum - actualSum) can face.
 *
 * Time complexity:  O(n) — single pass through nums.
 * Space complexity: O(1).
 */
public class MissingNumber {

    public int missingNumber(int[] nums) {
        int missing = nums.length; // start with n (the largest possible index)
        for (int i = 0; i < nums.length; i++) {
            missing ^= i ^ nums[i]; // XOR index and value; all present pairs cancel
        }
        return missing;
    }
}
