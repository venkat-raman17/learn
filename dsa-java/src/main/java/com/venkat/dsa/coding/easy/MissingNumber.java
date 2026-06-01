package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Missing Number
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Bit Manipulation
 * <p>URL: https://leetcode.com/problems/missing-number/
 *
 * <p>Given an array nums containing n distinct numbers in the range [0, n], return the one
 * number in the range that is missing from the array.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == nums.length</li>
 *   <li>1 &lt;= n &lt;= 10^4</li>
 *   <li>0 &lt;= nums[i] &lt;= n</li>
 *   <li>All numbers in nums are unique.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: nums = [3, 0, 1]     Output: 2
 *   Input: nums = [0, 1]        Output: 2
 *   Input: nums = [9,6,4,2,3,5,7,0,1] Output: 8
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: XOR all indices 0..n with all elements; duplicates cancel, leaving the missing number.
 * <p>Hint 2: Alternatively, the expected sum of 0..n is n*(n+1)/2; subtract the actual sum to find the gap.
 */
public class MissingNumber {

    public int missingNumber(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
