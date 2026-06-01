package com.venkat.dsa.coding.medium.solutions;

/**
 * House Robber (LeetCode 198)
 *
 * Approach: Bottom-up DP with two rolling variables. Let rob1 = best amount
 * robbing up to two houses ago and rob2 = best amount robbing up to the previous
 * house. For each new house the decision is max(rob1 + current, rob2).
 *
 * Key insight: We never rob two adjacent houses, so the recurrence reduces to
 * a simple two-variable rolling maximum — no array needed.
 *
 * Time:  O(n)
 * Space: O(1)
 */
public class HouseRobber {

    public int rob(int[] nums) {
        int rob1 = 0; // max loot from houses ending two positions back
        int rob2 = 0; // max loot from houses ending one position back

        for (int num : nums) {
            int curr = Math.max(rob1 + num, rob2); // rob this house or skip it
            rob1 = rob2;
            rob2 = curr;
        }
        return rob2;
    }
}
