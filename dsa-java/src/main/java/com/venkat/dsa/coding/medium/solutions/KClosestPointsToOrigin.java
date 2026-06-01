package com.venkat.dsa.coding.medium.solutions;

import java.util.PriorityQueue;

/**
 * K Closest Points to Origin (LeetCode #973)
 *
 * <p>Approach: Use a max-heap of size k keyed on squared Euclidean distance
 * (x^2 + y^2 — no need for sqrt). For each point, add it to the heap; if the
 * heap grows beyond k, remove the farthest point. After processing all points,
 * the heap contains exactly the k closest.
 *
 * <p>Key insight: Maintaining a max-heap of size k ensures we always discard the
 * worst candidate seen so far, keeping only the k smallest distances.
 * Comparing squared distances avoids floating-point arithmetic.
 *
 * <p>Time complexity:  O(n log k) — n inserts each costing O(log k).
 * Space complexity: O(k) — heap never exceeds k entries.
 */
public class KClosestPointsToOrigin {

    public int[][] kClosest(int[][] points, int k) {
        // Max-heap ordered by squared distance (largest distance on top)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> (b[0] * b[0] + b[1] * b[1]) - (a[0] * a[0] + a[1] * a[1])
        );

        for (int[] point : points) {
            maxHeap.offer(point);
            // Evict the farthest point to keep only k closest
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        int[][] result = new int[k][2];
        int i = 0;
        for (int[] point : maxHeap) {
            result[i++] = point;
        }
        return result;
    }
}
