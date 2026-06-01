package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Target Sum
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/target-sum/
 *
 * <p>Given an integer array nums and an integer target, assign either '+' or '-' to each
 * element and return the number of different expressions that evaluate to target.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= nums.length <= 20</li>
 *   <li>0 <= nums[i] <= 1000</li>
 *   <li>0 <= sum(nums[i]) <= 1000</li>
 *   <li>-1000 <= target <= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   findTargetSumWays([1,1,1,1,1], 3) => 5
 *   findTargetSumWays([1], 1)         => 1
 * </pre>
 *
 * <p>Target Time: O(n * sum), Space: O(n * sum) or O(sum)
 *
 * <p>Hint 1: Use a DP map from current running sum to number of ways to reach it.
 * <p>Hint 2: Equivalently, reduce to subset-sum: find a subset S+ where
 * 2*S+ = total + target; count subsets summing to S+.
 */
public class TargetSum {

    public int findTargetSumWays(int[] nums, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
