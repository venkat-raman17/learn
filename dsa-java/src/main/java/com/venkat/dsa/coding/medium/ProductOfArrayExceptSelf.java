package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Product of Array Except Self
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/product-of-array-except-self/
 *
 * <p>Given an integer array {@code nums}, return an array {@code answer} such that
 * {@code answer[i]} is the product of all elements of {@code nums} except {@code nums[i]}.
 * You must solve it without using division and in O(n) time.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>2 &lt;= nums.length &lt;= 10^5</li>
 *   <li>-30 &lt;= nums[i] &lt;= 30</li>
 *   <li>The product of any prefix or suffix of nums fits in a 32-bit integer.</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: [1,2,3,4]   →  Output: [24,12,8,6]
 *   Input: [-1,1,0,-3,3] →  Output: [0,0,9,0,0]
 * </pre>
 *
 * <p><b>Target:</b> Time O(n), Space O(1) extra (output array doesn't count)
 *
 * <p><b>Hint 1:</b> Build a prefix-product array (left pass) and a suffix-product array (right pass).
 * <p><b>Hint 2:</b> Combine them in a single output array: for index i, result[i] = prefix[i] * suffix[i].
 *   You can do it in O(1) extra space by reusing the output array for the prefix pass.
 */
public class ProductOfArrayExceptSelf {

    public int[] productExceptSelf(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
