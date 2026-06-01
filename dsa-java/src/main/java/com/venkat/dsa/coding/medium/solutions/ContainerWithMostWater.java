package com.venkat.dsa.coding.medium.solutions;

/**
 * Container With Most Water (LeetCode #11)
 *
 * Approach: Place two pointers at the leftmost and rightmost lines. The water
 * held between the two pointers is limited by the shorter line; to maximise
 * area we greedily discard the shorter line by advancing its pointer inward,
 * since keeping it can only decrease or maintain the width while the height
 * cannot improve (it is still capped by the shorter line).
 *
 * Key insight: Moving the taller line inward can never increase area (width
 * shrinks, height stays capped by the shorter line), so always advance the
 * pointer pointing to the shorter line.
 *
 * Time complexity:  O(n) — each pointer moves at most n/2 steps.
 * Space complexity: O(1).
 */
public class ContainerWithMostWater {

    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int width = right - left;
            int currentArea = width * Math.min(height[left], height[right]);
            maxWater = Math.max(maxWater, currentArea);

            // Advance the pointer with the shorter line
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;
    }
}
