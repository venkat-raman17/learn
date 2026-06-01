package com.venkat.dsa.coding.medium.solutions;

import java.util.PriorityQueue;

/**
 * Kth Largest Element in an Array (LeetCode #215)
 *
 * <p>Approach: Use a min-heap of exactly size k. Iterate through the array; add
 * each element and, if the heap exceeds k entries, remove the minimum. After
 * the full pass the heap's minimum is the k-th largest element overall.
 *
 * <p>Key insight: A min-heap of k elements always holds the k largest values seen
 * so far; its peek (the minimum of those k values) is therefore the k-th largest.
 *
 * <p>Time complexity:  O(n log k) — n heap operations each O(log k).
 * Space complexity: O(k) — heap is bounded to k elements.
 */
public class KthLargestElementInAnArray {

    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min-heap by default

        for (int num : nums) {
            minHeap.offer(num);
            // Keep only the k largest; discard the new minimum when over capacity
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // The smallest of the k largest = k-th largest overall
        return minHeap.peek();
    }
}
