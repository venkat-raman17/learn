package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Permutations (LeetCode 46)
 *
 * Approach: Backtracking with a boolean "used" array. At each position we try every
 * unused number, mark it used, recurse to fill the next position, then unmark it.
 * When the current list reaches length n, a full permutation is recorded.
 *
 * Key insight: tracking which elements have been used (rather than mutating the input
 * array) keeps the logic clear and avoids index arithmetic.
 *
 * Time:  O(n * n!) — n! permutations, each of length n.
 * Space: O(n) recursion depth (excluding output).
 */
public class Permutations {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used,
                           List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue; // skip already-placed elements

            used[i] = true;
            current.add(nums[i]);
            backtrack(nums, used, current, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
}
