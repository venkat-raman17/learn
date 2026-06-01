package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — K Closest Points to Origin
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/k-closest-points-to-origin/
 *
 * <p>Given an array of points on a 2D plane and an integer k, return the k closest points
 * to the origin (0, 0). The distance is the Euclidean distance. The answer may be returned
 * in any order.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= k &lt;= points.length &lt;= 10^4</li>
 *   <li>-10^4 &lt;= points[i][0], points[i][1] &lt;= 10^4</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  points = [[1,3],[-2,2]], k = 1
 *   Output: [[-2,2]]
 *   Explanation: dist(1,3)=sqrt(10), dist(-2,2)=sqrt(8); [-2,2] is closer.
 *
 * Example 2:
 *   Input:  points = [[3,3],[5,-1],[-2,4]], k = 2
 *   Output: [[3,3],[-2,4]]
 * </pre>
 *
 * <p>Target: Time O(n log k), Space O(k)
 *
 * <p>Hint 1: Maintain a max-heap of size k keyed on squared distance (avoid sqrt); when size exceeds k, evict the farthest.
 * <p>Hint 2: Squared distance x^2 + y^2 avoids floating-point and preserves ordering.
 */
public class KClosestPointsToOrigin {

    public int[][] kClosest(int[][] points, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
