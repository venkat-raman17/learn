package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Plus One
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Math & Geometry
 * <p>URL: https://leetcode.com/problems/plus-one/
 *
 * <p>Given a large integer represented as an array of digits (most significant digit first),
 * increment the integer by one and return the resulting array of digits.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= digits.length <= 100</li>
 *   <li>0 <= digits[i] <= 9</li>
 *   <li>digits does not contain leading zeros (except for the number 0 itself)</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   plusOne([1,2,3]) -> [1,2,4]
 *   plusOne([9,9,9]) -> [1,0,0,0]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1) amortised (O(n) in the all-9s edge case for new array).
 *
 * <p>Hint 1: Traverse from the least significant digit, propagating carry.
 * <p>Hint 2: If carry remains after the loop, prepend a 1 (all digits were 9).
 */
public class PlusOne {

    public int[] plusOne(int[] digits) {
        throw new UnsupportedOperationException("implement me");
    }
}
