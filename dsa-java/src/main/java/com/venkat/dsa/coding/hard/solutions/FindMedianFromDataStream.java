package com.venkat.dsa.coding.hard.solutions;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Find Median from Data Stream (LeetCode #295)
 *
 * <p>Approach: Maintain two heaps: a max-heap {@code lo} for the lower half of
 * numbers and a min-heap {@code hi} for the upper half. After every insertion
 * the invariants are: (1) every element in {@code lo} ≤ every element in
 * {@code hi}; (2) their sizes differ by at most 1. The median is then either
 * the top of the larger heap or the average of both tops.
 *
 * <p>Key insight: Partitioning the stream into two balanced halves at a
 * dynamically maintained boundary gives O(1) median reads with only O(log n)
 * insertion cost, far better than re-sorting on each query.
 *
 * <p>Time complexity:  addNum O(log n); findMedian O(1).
 * Space complexity: O(n) — all elements stored across both heaps.
 */
public class FindMedianFromDataStream {

    private final PriorityQueue<Integer> lo; // max-heap — lower half
    private final PriorityQueue<Integer> hi; // min-heap — upper half

    public FindMedianFromDataStream() {
        lo = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
        hi = new PriorityQueue<>();                           // min-heap
    }

    public void addNum(int num) {
        // Always add to lo first; then balance by pushing lo's max to hi
        lo.offer(num);
        hi.offer(lo.poll()); // ensures every lo element <= every hi element

        // Rebalance sizes: lo is allowed to be one larger than hi
        if (hi.size() > lo.size()) {
            lo.offer(hi.poll());
        }
    }

    public double findMedian() {
        if (lo.size() > hi.size()) {
            // Odd total — lo holds the middle element
            return lo.peek();
        }
        // Even total — median is average of the two middle elements
        return (lo.peek() + (double) hi.peek()) / 2.0;
    }
}
