package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Longest Increasing Subsequence
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/longest-increasing-subsequence/
 *
 * <p>Given an integer array {@code nums}, return the length of the longest strictly increasing
 * subsequence (elements need not be contiguous).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 2500</li>
 *   <li>-10^4 &lt;= nums[i] &lt;= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: nums = [10,9,2,5,3,7,101,18]  -> Output: 4  ([2,3,7,101])
 * Input: nums = [0,1,0,3,2,3]          -> Output: 4
 * Input: nums = [7,7,7,7]              -> Output: 1
 * </pre>
 *
 * <p>Target: Time O(n^2) DP or O(n log n) with patience sort, Space O(n)
 *
 * <p>Hint 1: dp[i] = length of LIS ending at index i. For each i, scan all j &lt; i where
 *            nums[j] &lt; nums[i] and take dp[i] = max(dp[j] + 1).
 * <p>Hint 2: For O(n log n), maintain a "tails" array and binary-search for the insertion position
 *            of each element.
 */
public class LongestIncreasingSubsequence {

    public int lengthOfLIS(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
