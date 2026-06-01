package com.venkat.dsa.coding.easy.solutions;

import java.util.PriorityQueue;

/**
 * Kth Largest Element in a Stream (LeetCode #703)
 *
 * <p>Approach: Maintain a min-heap of size k. The smallest element in the heap
 * is always the k-th largest seen so far. On each {@code add}, push the new
 * value and, if the heap exceeds size k, remove the minimum. The heap's peek
 * is then the answer.
 *
 * <p>Key insight: A min-heap of exactly k elements keeps the k largest values;
 * the minimum of those k elements is the k-th largest overall.
 *
 * <p>Time complexity:  O(n log k) constructor (n initial elements), O(log k) per add.
 * Space complexity: O(k) — heap never exceeds k elements.
 */
public class KthLargestElementInAStream {

    private final int k;
    private final PriorityQueue<Integer> minHeap; // stores k largest values

    public KthLargestElementInAStream(int k, int[] nums) {
        this.k = k;
        this.minHeap = new PriorityQueue<>(); // natural order = min-heap
        for (int n : nums) {
            add(n);
        }
    }

    public int add(int val) {
        minHeap.offer(val);
        // Evict the smallest so we keep only the k largest
        if (minHeap.size() > k) {
            minHeap.poll();
        }
        // Top of min-heap is the k-th largest
        return minHeap.peek();
    }
}
