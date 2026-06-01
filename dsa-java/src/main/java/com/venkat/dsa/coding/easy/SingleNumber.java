package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Single Number
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/single-number/
 *
 * <p>Given a non-empty array of integers where every element appears twice except for one,
 * find and return that single element. The solution must run in linear time and use only
 * constant extra space.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 3 * 10^4</li>
 *   <li>-3 * 10^4 &lt;= nums[i] &lt;= 3 * 10^4</li>
 *   <li>Each element in the array appears twice except for one element which appears exactly once.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: nums = [2, 2, 1]       Output: 1
 *   Input: nums = [4, 1, 2, 1, 2] Output: 4
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: XOR of a number with itself is 0; XOR of a number with 0 is itself.
 * <p>Hint 2: XOR all elements together — pairs cancel out, leaving the unique element.
 */
public class SingleNumber {

    public int singleNumber(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
