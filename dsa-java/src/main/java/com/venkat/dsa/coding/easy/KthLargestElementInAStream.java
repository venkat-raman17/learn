package com.venkat.dsa.coding.easy;

import java.util.PriorityQueue;

/**
 * NeetCode / LeetCode — Kth Largest Element in a Stream
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/kth-largest-element-in-a-stream/
 *
 * <p>Design a class to find the k-th largest element in a stream. Note that it is the
 * k-th largest element in the sorted order, not the k-th distinct element. Implement
 * KthLargest with a constructor that accepts k and an initial array of numbers, and
 * an add(val) method that adds a new value and returns the current k-th largest.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= k &lt;= 10^4</li>
 *   <li>0 &lt;= nums.length &lt;= 10^4</li>
 *   <li>-10^4 &lt;= nums[i] &lt;= 10^4</li>
 *   <li>-10^4 &lt;= val &lt;= 10^4</li>
 *   <li>At most 10^4 calls will be made to add.</li>
 *   <li>It is guaranteed that there will be at least k elements in the array when you search for the k-th element.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   k = 3, nums = [4, 5, 8, 2]
 *   add(3)  -> 4
 *   add(5)  -> 5
 *   add(10) -> 5
 *   add(9)  -> 8
 *   add(4)  -> 8
 *
 * Example 2:
 *   k = 1, nums = []
 *   add(3) -> 3
 * </pre>
 *
 * <p>Target: Time O(log k) per add, Space O(k)
 *
 * <p>Hint 1: Use a min-heap of size k — the root is always the k-th largest seen so far.
 * <p>Hint 2: When adding a value, push it onto the heap; if size exceeds k, pop the minimum.
 */
public class KthLargestElementInAStream {

    private final int k;
    private final PriorityQueue<Integer> minHeap;

    public KthLargestElementInAStream(int k, int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }

    public int add(int val) {
        throw new UnsupportedOperationException("implement me");
    }
}
