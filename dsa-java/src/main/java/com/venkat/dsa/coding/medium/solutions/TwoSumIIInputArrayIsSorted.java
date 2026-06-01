package com.venkat.dsa.coding.medium.solutions;

/**
 * Two Sum II – Input Array Is Sorted (LeetCode #167)
 *
 * Approach: Use two pointers initialised at the two ends of the sorted array.
 * If the current sum equals the target we are done; if it is too small advance
 * the left pointer to increase the sum; if it is too large retreat the right
 * pointer to decrease the sum.
 *
 * Key insight: The sorted property guarantees that moving left right only
 * increases the sum and moving right left only decreases it, so the two
 * pointers converge to the unique solution in O(n) time without extra space.
 *
 * Time complexity:  O(n) — pointers traverse the array at most once.
 * Space complexity: O(1) — only two pointer variables.
 */
public class TwoSumIIInputArrayIsSorted {

    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                // Problem uses 1-indexed result
                return new int[]{left + 1, right + 1};
            } else if (sum < target) {
                left++;  // need a larger sum
            } else {
                right--; // need a smaller sum
            }
        }
        // Guaranteed by problem constraints that exactly one solution exists
        throw new IllegalArgumentException("No solution found");
    }
}
