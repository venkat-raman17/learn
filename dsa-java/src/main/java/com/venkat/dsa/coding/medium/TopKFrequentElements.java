package com.venkat.dsa.coding.medium;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * NeetCode / LeetCode — Top K Frequent Elements
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/top-k-frequent-elements/
 *
 * <p>Given an integer array {@code nums} and an integer {@code k}, return the {@code k} most
 * frequent elements. You may return the answer in any order.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10^5</li>
 *   <li>-10^4 &lt;= nums[i] &lt;= 10^4</li>
 *   <li>k is in the range [1, number of unique elements]</li>
 *   <li>The answer is guaranteed to be unique.</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: nums = [1,1,1,2,2,3], k = 2  →  Output: [1,2]
 *   Input: nums = [1], k = 1            →  Output: [1]
 * </pre>
 *
 * <p><b>Target:</b> Time O(n log k) with a min-heap, or O(n) with bucket sort
 *
 * <p><b>Hint 1:</b> Count frequencies with a HashMap, then use a min-heap of size k to keep the top k.
 * <p><b>Hint 2:</b> For O(n), use bucket sort: create buckets indexed by frequency (max index = n).
 */
public class TopKFrequentElements {

    public int[] topKFrequent(int[] nums, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
