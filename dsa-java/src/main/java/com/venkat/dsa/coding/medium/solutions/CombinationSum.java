package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Combination Sum (LeetCode 39)
 *
 * Approach: Backtracking — iterate candidates starting from the current index;
 * the same candidate may be reused, so we recurse with the same index (not i+1).
 * We prune the branch whenever the running sum exceeds the target.
 *
 * Key insight: by always moving forward (start index), we naturally avoid generating
 * the same combination in different orders, keeping the solution space manageable.
 *
 * Time:  O(n^(t/m)) where t = target, m = min candidate value.
 * Space: O(t/m) recursion depth.
 */
public class CombinationSum {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
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
            if (candidates[i] > remaining) continue; // prune: no point going further

            current.add(candidates[i]);
            // reuse same index — unlimited usage per element
            backtrack(candidates, remaining - candidates[i], i, current, result);
            current.remove(current.size() - 1);
        }
    }
}
