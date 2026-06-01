package com.venkat.dsa.coding.hard.solutions;

/**
 * Burst Balloons (LeetCode 312)
 *
 * Approach: Interval DP. Add sentinel balloons of value 1 at both ends.
 * dp[l][r] = maximum coins obtainable by bursting all balloons in the open interval
 * (l, r) (l and r are NOT burst in this sub-problem). We iterate over all possible
 * "last balloon to burst" k in (l, r): bursting k last means its left and right
 * neighbors are nums[l] and nums[r] (the sentinels of the sub-interval), giving
 * coins = nums[l]*nums[k]*nums[r] + dp[l][k] + dp[k][r].
 *
 * Time:  O(n^3)  — O(n^2) sub-problems, O(n) choices each
 * Space: O(n^2)
 *
 * Key insight: thinking about the LAST balloon to burst in an interval instead of
 * the first avoids the complexity of changing neighbors after each burst.
 */
public class BurstBalloons {

    public int maxCoins(int[] nums) {
        int n = nums.length;
        // Pad with sentinel 1s at both ends
        int[] arr = new int[n + 2];
        arr[0] = 1;
        arr[n + 1] = 1;
        for (int i = 0; i < n; i++) arr[i + 1] = nums[i];

        int len = n + 2;
        // dp[l][r] = max coins bursting all balloons strictly between l and r
        int[][] dp = new int[len][len];

        // Iterate by interval length (at least 2 apart means at least 1 balloon inside)
        for (int gap = 2; gap < len; gap++) {
            for (int l = 0; l + gap < len; l++) {
                int r = l + gap;
                for (int k = l + 1; k < r; k++) {
                    // k is the last balloon burst in (l, r)
                    int coins = arr[l] * arr[k] * arr[r] + dp[l][k] + dp[k][r];
                    dp[l][r] = Math.max(dp[l][r], coins);
                }
            }
        }
        return dp[0][len - 1];
    }
}
