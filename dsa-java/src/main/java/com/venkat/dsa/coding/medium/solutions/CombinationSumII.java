package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Combination Sum II (LeetCode 40)
 *
 * Approach: Sort candidates so duplicates are adjacent, then backtrack similarly to
 * CombinationSum but advance the index by 1 each call (each element used at most once).
 * Skip duplicate siblings at each depth level with the same i > start guard.
 *
 * Key insight: the same duplicate-skip idiom used in Subsets II applies here; sorting
 * ensures identical candidates sit next to each other so the skip condition is O(1).
 *
 * Time:  O(2^n) combinations to explore.
 * Space: O(n) recursion depth.
 */
public class CombinationSumII {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
                           List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) break; // sorted: no point continuing

            // skip duplicate at the same recursive depth
            if (i > start && candidates[i] == candidates[i - 1]) continue;

            current.add(candidates[i]);
            backtrack(candidates, remaining - candidates[i], i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
