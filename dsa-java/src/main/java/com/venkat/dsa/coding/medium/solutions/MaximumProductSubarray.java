package com.venkat.dsa.coding.medium.solutions;

/**
 * Maximum Product Subarray (LeetCode 152)
 *
 * Approach: Single-pass DP tracking both the current maximum and current
 * minimum product ending at the current position. A negative number flips
 * max and min, so we must maintain both. At each step:
 *   curMax = max(num, curMax * num, curMin * num)
 *   curMin = min(num, curMax * num, curMin * num)
 * We update the global result after each element.
 *
 * Key insight: A large negative minimum can become a large positive maximum
 * when multiplied by another negative — hence tracking both extremes is
 * necessary.
 *
 * Time:  O(n)
 * Space: O(1)
 */
public class MaximumProductSubarray {

    public int maxProduct(int[] nums) {
        int res = nums[0];
        int curMax = nums[0];
        int curMin = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];
            // must use temporaries because curMax is overwritten before curMin
            int tempMax = Math.max(num, Math.max(curMax * num, curMin * num));
            int tempMin = Math.min(num, Math.min(curMax * num, curMin * num));
            curMax = tempMax;
            curMin = tempMin;
            res = Math.max(res, curMax);
        }
        return res;
    }
}
