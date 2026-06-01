package com.venkat.dsa.coding.hard.solutions;

/**
 * Trapping Rain Water (LeetCode #42)
 *
 * Approach: Two-pointer technique with running maximums. Maintain a left and
 * right pointer together with the maximum height seen so far from each side.
 * At each step the side with the smaller maximum constrains the water level;
 * process that side by adding (maxSoFar - height[pointer]) to the total and
 * advance the pointer inward.
 *
 * Key insight: The water above any column equals the minimum of the tallest
 * bar to its left and the tallest bar to its right minus the column's own
 * height. The two-pointer approach computes this in O(1) extra space by
 * exploiting the fact that the smaller of the two running maxima is always
 * the true limiting side for the column being processed.
 *
 * Time complexity:  O(n) — each element is visited exactly once.
 * Space complexity: O(1) — only four scalar variables.
 */
public class TrappingRainWater {

    public int trap(int[] height) {
        if (height == null || height.length < 3) return 0;

        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;   // tallest bar seen from the left up to (and including) `left`
        int rightMax = 0;  // tallest bar seen from the right up to (and including) `right`
        int water = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                // Left side is the bottleneck; process left column
                if (height[left] >= leftMax) {
                    leftMax = height[left]; // update running max, no water trapped here
                } else {
                    water += leftMax - height[left]; // leftMax is the effective ceiling
                }
                left++;
            } else {
                // Right side is the bottleneck; process right column
                if (height[right] >= rightMax) {
                    rightMax = height[right]; // update running max, no water trapped here
                } else {
                    water += rightMax - height[right]; // rightMax is the effective ceiling
                }
                right--;
            }
        }
        return water;
    }
}
