package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Last Stone Weight
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/last-stone-weight/
 *
 * <p>You have an array of stones, each with a positive integer weight. Each turn, pick the two
 * heaviest stones and smash them together. If they are equal both are destroyed; otherwise the
 * smaller is destroyed and the larger's weight is reduced by the smaller's weight. Return the
 * weight of the last remaining stone, or 0 if none remain.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= stones.length &lt;= 30</li>
 *   <li>1 &lt;= stones[i] &lt;= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  stones = [2, 7, 4, 1, 8, 1]
 *   Output: 1
 *
 * Example 2:
 *   Input:  stones = [1]
 *   Output: 1
 * </pre>
 *
 * <p>Target: Time O(n log n), Space O(n)
 *
 * <p>Hint 1: Use a max-heap (negate values in a Java PriorityQueue) to always access the two heaviest stones.
 * <p>Hint 2: Each iteration poll twice, compute the difference, and push it back only if non-zero.
 */
public class LastStoneWeight {

    public int lastStoneWeight(int[] stones) {
        throw new UnsupportedOperationException("implement me");
    }
}
