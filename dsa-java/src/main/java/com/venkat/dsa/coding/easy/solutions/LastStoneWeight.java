package com.venkat.dsa.coding.easy.solutions;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Last Stone Weight (LeetCode #1046)
 *
 * <p>Approach: Use a max-heap to always access the two heaviest stones in O(log n).
 * Repeatedly extract the two largest: if they are equal both are destroyed; otherwise
 * the difference is pushed back. Continue until at most one stone remains.
 *
 * <p>Key insight: Greedily smashing the two heaviest stones each round is optimal
 * because any smaller stone smashed earlier would leave the large stones untouched,
 * yielding a larger or equal final weight.
 *
 * <p>Time complexity:  O(n log n) — each of the n rounds does two polls + one offer.
 * Space complexity: O(n) — heap storage.
 */
public class LastStoneWeight {

    public int lastStoneWeight(int[] stones) {
        // Collections.reverseOrder() turns PriorityQueue into a max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int s : stones) {
            maxHeap.offer(s);
        }

        while (maxHeap.size() > 1) {
            int y = maxHeap.poll(); // heaviest
            int x = maxHeap.poll(); // second heaviest
            // If unequal, the difference survives
            if (x != y) {
                maxHeap.offer(y - x);
            }
        }

        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }
}
