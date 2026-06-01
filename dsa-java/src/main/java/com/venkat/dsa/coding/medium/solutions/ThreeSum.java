package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 3Sum (LeetCode #15)
 *
 * Approach: Sort the array then, for each index i, run a classic two-pointer
 * search on the subarray to the right of i looking for pairs that sum to
 * -nums[i]. Duplicate values at each of the three positions are skipped to
 * avoid repeating the same triplet in the output.
 *
 * Key insight: Sorting is the prerequisite that lets two pointers converge
 * correctly; the duplicate-skip logic (advance past equal neighbours after
 * recording a hit, and skip equal values of the outer loop) ensures each
 * unique triplet is emitted exactly once without a hash set.
 *
 * Time complexity:  O(n^2) — O(n log n) sort + O(n) two-pointer per element.
 * Space complexity: O(1) extra (not counting output); sort uses O(log n) stack.
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicate values for the outer pointer
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            // Optimisation: smallest possible sum is already > 0 — no point continuing
            if (nums[i] > 0) break;

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    // Skip duplicates for left and right pointers
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}
