package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Partition Equal Subset Sum
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/partition-equal-subset-sum/
 *
 * <p>Given an integer array {@code nums}, return {@code true} if the array can be partitioned
 * into two subsets such that the sum of elements in both subsets is equal.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 200</li>
 *   <li>1 &lt;= nums[i] &lt;= 100</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: nums = [1,5,11,5]  -> Output: true   ([1,5,5] and [11])
 * Input: nums = [1,2,3,5]   -> Output: false
 * </pre>
 *
 * <p>Target: Time O(n * sum), Space O(sum)
 *
 * <p>Hint 1: If the total sum is odd, return false immediately. Otherwise the problem reduces to
 *            a 0/1 knapsack: can we reach target = sum/2 using a subset of nums?
 * <p>Hint 2: Use a boolean dp array of size target+1; iterate nums in the outer loop and targets
 *            downward in the inner loop to avoid reusing the same element.
 */
public class PartitionEqualSubsetSum {

    public boolean canPartition(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
