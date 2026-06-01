package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Container With Most Water
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Two Pointers
 * <p>URL: https://leetcode.com/problems/container-with-most-water/
 *
 * <p>Given an integer array {@code height} of length {@code n} where each element represents the
 * height of a vertical line at position {@code i}, find two lines that together with the x-axis
 * form a container that holds the most water. Return the maximum amount of water the container can store.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == height.length</li>
 *   <li>2 &lt;= n &lt;= 10^5</li>
 *   <li>0 &lt;= height[i] &lt;= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   maxArea([1,8,6,2,5,4,8,3,7]) =&gt; 49
 *   maxArea([1,1])                =&gt; 1
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Use two pointers at both ends; the area is min(height[l], height[r]) * (r - l).
 * <p>Hint 2: Always move the pointer pointing to the shorter line inward to potentially find a taller one.
 */
public class ContainerWithMostWater {

    public int maxArea(int[] height) {
        throw new UnsupportedOperationException("implement me");
    }
}
