package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Maximum Product Subarray
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/maximum-product-subarray/
 *
 * <p>Given an integer array {@code nums}, find the contiguous subarray that has the largest product
 * and return that product.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 2 * 10^4</li>
 *   <li>-10 &lt;= nums[i] &lt;= 10</li>
 *   <li>The product of any subarray fits in a 32-bit integer.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: nums = [2,3,-2,4]   -> Output: 6   (subarray [2,3])
 * Input: nums = [-2,0,-1]    -> Output: 0   (subarray [0])
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Track both the running maximum AND minimum product — a negative times a negative
 *            can become the new maximum.
 * <p>Hint 2: When you encounter a zero, the subarray resets; the current element itself becomes
 *            the new min/max candidate.
 */
public class MaximumProductSubarray {

    public int maxProduct(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
