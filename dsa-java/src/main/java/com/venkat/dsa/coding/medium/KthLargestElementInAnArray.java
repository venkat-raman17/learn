package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Kth Largest Element in an Array
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/kth-largest-element-in-an-array/
 *
 * <p>Given an integer array nums and an integer k, return the k-th largest element in the array.
 * Note that it is the k-th largest in sorted order, not the k-th distinct element. You must
 * solve it without sorting (aim for O(n) average with QuickSelect, or O(n log k) with a heap).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= k &lt;= nums.length &lt;= 10^5</li>
 *   <li>-10^4 &lt;= nums[i] &lt;= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  nums = [3, 2, 1, 5, 6, 4], k = 2
 *   Output: 5
 *
 * Example 2:
 *   Input:  nums = [3, 2, 3, 1, 2, 4, 5, 5, 6], k = 4
 *   Output: 4
 * </pre>
 *
 * <p>Target: Time O(n log k) heap / O(n) average QuickSelect, Space O(k)
 *
 * <p>Hint 1: A min-heap of size k always holds the k largest elements; its root is the answer.
 * <p>Hint 2: QuickSelect partitions around a pivot: if the pivot index equals n-k you found the answer; otherwise recurse on one side.
 */
public class KthLargestElementInAnArray {

    public int findKthLargest(int[] nums, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
