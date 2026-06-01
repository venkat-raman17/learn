package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Subsets (LeetCode 78)
 *
 * Approach: Backtracking — at each index we decide whether to include the current
 * element or not, recursing through all choices. When we reach the end of the array
 * every path (including partial prefixes captured at each call) forms one unique subset.
 *
 * Key insight: capture the current path as a subset at every recursive call (not just
 * at the leaves), then iterate forward from the current index to avoid duplicates.
 *
 * Time:  O(n * 2^n) — 2^n subsets, each copied in O(n).
 * Space: O(n) recursion depth + O(n * 2^n) for the output list.
 */
public class Subsets {

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        // Every call represents a valid subset — capture it immediately
        result.add(new ArrayList<>(current));

        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);          // choose
            backtrack(nums, i + 1, current, result); // explore
            current.remove(current.size() - 1); // un-choose
        }
    }
}
