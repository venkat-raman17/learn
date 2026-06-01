package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Subsets II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/subsets-ii/
 *
 * <p>Given an integer array {@code nums} that may contain duplicates, return all possible subsets
 * (the power set). The solution set must not contain duplicate subsets.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10</li>
 *   <li>-10 &lt;= nums[i] &lt;= 10</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: nums = [1,2,2]
 *   Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
 *
 *   Input: nums = [0]
 *   Output: [[],[0]]
 * </pre>
 *
 * <p>Target: Time O(n * 2^n), Space O(n * 2^n)
 *
 * <p>Hint 1: Sort the array first so that duplicate values are adjacent, making them easy to skip.
 * <p>Hint 2: When iterating at the same recursion depth, skip an element if it equals the previous
 * element at that same depth (i.e., i > start and nums[i] == nums[i-1]).
 */
public class SubsetsII {

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
