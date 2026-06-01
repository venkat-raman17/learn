package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Find The Duplicate Number
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/find-the-duplicate-number/
 *
 * <p>Given an array of n + 1 integers where each integer is in the range [1, n] inclusive,
 * there is exactly one repeated number. Find and return that number without modifying the array
 * and using only O(1) extra space.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= n <= 10^5</li>
 *   <li>nums.length == n + 1</li>
 *   <li>1 <= nums[i] <= n</li>
 *   <li>All integers appear exactly once except for precisely one integer which appears two or more times.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: nums = [1,3,4,2,2]  ->  Output: 2
 *   Input: nums = [3,1,3,4,2]  ->  Output: 3
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space
 *
 * <p>Hint 1: Treat the array as a linked list where index i points to nums[i]; the duplicate
 *            creates a cycle — apply Floyd's cycle detection.
 * <p>Hint 2: After slow and fast meet inside the cycle, reset one pointer to index 0 and advance
 *            both one step at a time; they meet at the duplicate (cycle entrance).
 */
public class FindTheDuplicateNumber {

    public int findDuplicate(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
