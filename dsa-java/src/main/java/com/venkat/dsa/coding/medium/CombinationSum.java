package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Combination Sum
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/combination-sum/
 *
 * <p>Given an array of distinct integers {@code candidates} and a target integer {@code target},
 * return a list of all unique combinations of candidates where the chosen numbers sum to target.
 * The same number may be chosen from candidates an unlimited number of times.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= candidates.length &lt;= 30</li>
 *   <li>2 &lt;= candidates[i] &lt;= 40</li>
 *   <li>All elements of candidates are distinct.</li>
 *   <li>1 &lt;= target &lt;= 40</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: candidates = [2,3,6,7], target = 7
 *   Output: [[2,2,3],[7]]
 *
 *   Input: candidates = [2,3,5], target = 8
 *   Output: [[2,2,2,2],[2,3,3],[3,5]]
 * </pre>
 *
 * <p>Target: Time O(n^(t/m)) where t=target, m=min candidate, Space O(t/m)
 *
 * <p>Hint 1: Use a start index to avoid generating duplicate combinations; recurse without
 * incrementing start to allow reuse of the same candidate.
 * <p>Hint 2: Prune the recursion early when the remaining target goes below zero.
 */
public class CombinationSum {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
