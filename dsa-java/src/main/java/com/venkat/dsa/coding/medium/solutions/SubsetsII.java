package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Subsets II (LeetCode 90)
 *
 * Approach: Sort the array first so that duplicates are adjacent, then use the same
 * backtracking as Subsets. When iterating at a given depth, skip an element if it is
 * the same as the previous sibling at the same level (i > start and nums[i] == nums[i-1]).
 *
 * Key insight: sorting + sibling-level duplicate skip is the canonical pattern for all
 * "subsets/combinations with duplicates" problems. Duplicates within the same branch
 * (going deeper) are fine — only duplicates across sibling branches must be pruned.
 *
 * Time:  O(n * 2^n).
 * Space: O(n) recursion depth (excluding output).
 */
public class SubsetsII {

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums); // group duplicates
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));

        for (int i = start; i < nums.length; i++) {
            // skip duplicate sibling: same value chosen again at this depth level
            if (i > start && nums[i] == nums[i - 1]) continue;

            current.add(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
