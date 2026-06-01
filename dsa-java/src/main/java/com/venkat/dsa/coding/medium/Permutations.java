package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Permutations
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/permutations/
 *
 * <p>Given an array {@code nums} of distinct integers, return all the possible permutations.
 * You can return the answer in any order.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 6</li>
 *   <li>-10 &lt;= nums[i] &lt;= 10</li>
 *   <li>All the integers of nums are unique.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: nums = [1,2,3]
 *   Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 *   Input: nums = [0,1]
 *   Output: [[0,1],[1,0]]
 * </pre>
 *
 * <p>Target: Time O(n! * n), Space O(n)
 *
 * <p>Hint 1: Track which elements have already been placed in the current permutation using
 * a boolean "used" array or by swapping in-place.
 * <p>Hint 2: When the current permutation reaches length n, add a copy of it to the results.
 */
public class Permutations {

    public List<List<Integer>> permute(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
