package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Largest Rectangle in Histogram
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/largest-rectangle-in-histogram/
 *
 * <p>Given an array of integers representing bar heights in a histogram (each bar
 * has width 1), find the area of the largest rectangle that can be formed.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= heights.length <= 10^5</li>
 *   <li>0 <= heights[i] <= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   largestRectangleArea([2,1,5,6,2,3]) -> 10  // bars 5 and 6, width 2
 *   largestRectangleArea([2,4])         -> 4
 * </pre>
 *
 * <p>Target: O(n) time, O(n) space
 *
 * <p>Hint 1: Use a monotonic increasing stack of (index, height) pairs; when a shorter bar is encountered, pop and compute the rectangle width extending back.
 * <p>Hint 2: After iterating all bars, drain the remaining stack entries — each can extend to the right end of the array.
 */
public class LargestRectangleInHistogram {

    public int largestRectangleArea(int[] heights) {
        throw new UnsupportedOperationException("implement me");
    }
}
