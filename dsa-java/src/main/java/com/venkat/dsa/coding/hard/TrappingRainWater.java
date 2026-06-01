package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Trapping Rain Water
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Two Pointers
 * <p>URL: https://leetcode.com/problems/trapping-rain-water/
 *
 * <p>Given {@code n} non-negative integers representing an elevation map where the width of each
 * bar is 1, compute how much water it can trap after raining.
 * Water above position {@code i} equals {@code min(maxLeft, maxRight) - height[i]}.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == height.length</li>
 *   <li>1 &lt;= n &lt;= 2 * 10^4</li>
 *   <li>0 &lt;= height[i] &lt;= 10^5</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   trap([0,1,0,2,1,0,1,3,2,1,2,1]) =&gt; 6
 *   trap([4,2,0,3,2,5])             =&gt; 9
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Use two pointers with a running maxLeft and maxRight; process the side with the smaller max.
 * <p>Hint 2: At each step, water trapped at the current pointer = currentMax - height[pointer]; advance the pointer.
 */
public class TrappingRainWater {

    public int trap(int[] height) {
        throw new UnsupportedOperationException("implement me");
    }
}
