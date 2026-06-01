package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — 3Sum
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Two Pointers
 * <p>URL: https://leetcode.com/problems/3sum/
 *
 * <p>Given an integer array {@code nums}, return all the triplets
 * {@code [nums[i], nums[j], nums[k]]} such that {@code i != j}, {@code i != k}, {@code j != k},
 * and {@code nums[i] + nums[j] + nums[k] == 0}. The solution set must not contain duplicate triplets.
 *
 * <p>Constraints:
 * <ul>
 *   <li>3 &lt;= nums.length &lt;= 3000</li>
 *   <li>-10^5 &lt;= nums[i] &lt;= 10^5</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   threeSum([-1,0,1,2,-1,-4]) =&gt; [[-1,-1,2],[-1,0,1]]
 *   threeSum([0,1,1])          =&gt; []
 * </pre>
 *
 * <p>Target: Time O(n^2), Space O(1) auxiliary (output excluded)
 *
 * <p>Hint 1: Sort the array first; then for each element use a two-pointer sweep on the rest.
 * <p>Hint 2: Skip duplicate values at both the outer loop and the two pointers to avoid duplicate triplets.
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
