package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Subsets
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/subsets/
 *
 * <p>Given an integer array {@code nums} of unique elements, return all possible subsets
 * (the power set). The solution set must not contain duplicate subsets, and the order
 * of the output can be any order.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10</li>
 *   <li>-10 &lt;= nums[i] &lt;= 10</li>
 *   <li>All the numbers of nums are unique.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: nums = [1,2,3]
 *   Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 *
 *   Input: nums = [0]
 *   Output: [[],[0]]
 * </pre>
 *
 * <p>Target: Time O(n * 2^n), Space O(n * 2^n)
 *
 * <p>Hint 1: At each index, decide to include or exclude the current element, then recurse.
 * <p>Hint 2: Use a start index to avoid revisiting earlier elements; add the current subset
 * to the result list at every recursive call (not just at base cases).
 */
public class Subsets {

    public List<List<Integer>> subsets(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
