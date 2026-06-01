package com.venkat.dsa.coding.medium.solutions;

/**
 * Product of Array Except Self (LeetCode 238) — Medium
 *
 * <p>Approach: Two-pass prefix/suffix product without using division. In the first pass,
 * store at {@code result[i]} the product of all elements to the left of {@code i}.
 * In the second (right-to-left) pass, multiply each position by a running suffix
 * product accumulated on the fly, avoiding a separate suffix array.
 *
 * <p><b>Time complexity:</b> O(n) — two linear passes.<br>
 * <b>Space complexity:</b> O(1) extra (the output array is not counted per problem rules).
 *
 * <p><b>Key insight:</b> The answer at index {@code i} is simply
 * {@code prefix[i] * suffix[i]}; by reusing the output array for the prefix and
 * maintaining a single suffix variable we need no extra arrays.
 */
public class ProductOfArrayExceptSelf {

    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // pass 1: result[i] = product of all elements to the LEFT of i
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        // pass 2: multiply by running suffix product from the RIGHT
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= suffix;  // combine prefix already stored with suffix
            suffix *= nums[i];    // extend suffix one step to the left
        }
        return result;
    }
}
