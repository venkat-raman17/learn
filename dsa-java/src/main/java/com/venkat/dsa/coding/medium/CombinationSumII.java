package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Combination Sum II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/combination-sum-ii/
 *
 * <p>Given a collection of candidate numbers {@code candidates} (which may contain duplicates)
 * and a target number {@code target}, find all unique combinations where the candidate numbers
 * sum to target. Each number in candidates may only be used once in the combination.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= candidates.length &lt;= 100</li>
 *   <li>1 &lt;= candidates[i] &lt;= 50</li>
 *   <li>1 &lt;= target &lt;= 30</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: candidates = [10,1,2,7,6,1,5], target = 8
 *   Output: [[1,1,6],[1,2,5],[1,7],[2,6]]
 *
 *   Input: candidates = [2,5,2,1,2], target = 5
 *   Output: [[1,2,2],[5]]
 * </pre>
 *
 * <p>Target: Time O(2^n), Space O(n)
 *
 * <p>Hint 1: Sort the candidates array first; then at each recursion depth skip duplicates
 * by checking if candidates[i] == candidates[i-1] when i > start.
 * <p>Hint 2: Unlike Combination Sum I, advance the start index by 1 after choosing an element
 * (each element can only be used once).
 */
public class CombinationSumII {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
