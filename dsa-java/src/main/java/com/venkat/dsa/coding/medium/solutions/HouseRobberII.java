package com.venkat.dsa.coding.medium.solutions;

/**
 * House Robber II (LeetCode 213)
 *
 * Approach: Because the houses are arranged in a circle, the first and last
 * houses are adjacent, so we cannot rob both. We split the problem into two
 * linear sub-problems: (1) houses[0..n-2] and (2) houses[1..n-1], then take
 * the maximum of both results using the standard House Robber I DP.
 *
 * Key insight: Circular constraint → two independent linear passes, each
 * excluding one of the two "adjacent-to-both" endpoints.
 *
 * Time:  O(n)
 * Space: O(1)
 */
public class HouseRobberII {

    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);

        // rob houses[start..end] (inclusive) using the linear DP
        return Math.max(robRange(nums, 0, n - 2), robRange(nums, 1, n - 1));
    }

    private int robRange(int[] nums, int start, int end) {
        int rob1 = 0;
        int rob2 = 0;
        for (int i = start; i <= end; i++) {
            int curr = Math.max(rob1 + nums[i], rob2);
            rob1 = rob2;
            rob2 = curr;
        }
        return rob2;
    }
}
