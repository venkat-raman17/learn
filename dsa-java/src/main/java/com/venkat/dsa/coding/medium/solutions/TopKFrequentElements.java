package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Top K Frequent Elements (LeetCode 347) — Medium
 *
 * <p>Approach: Bucket sort by frequency. After building a frequency map, create an
 * array of lists where index {@code i} holds all numbers that appear exactly {@code i}
 * times. Then walk the buckets from highest to lowest, collecting elements until we
 * have {@code k} results.
 *
 * <p><b>Time complexity:</b> O(n) — frequency map + bucket fill + bucket scan are all linear.<br>
 * <b>Space complexity:</b> O(n) — frequency map and bucket array each hold at most n entries.
 *
 * <p><b>Key insight:</b> The maximum possible frequency is n (all elements identical),
 * so we can use the input length as the bucket-array size and avoid any comparison-based
 * sorting, beating the O(n log n) heap/sort approach.
 */
public class TopKFrequentElements {

    public int[] topKFrequent(int[] nums, int k) {
        // step 1: count frequencies
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) {
            freq.merge(n, 1, Integer::sum);
        }

        // step 2: bucket by frequency (index = frequency, 1-based)
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new List[nums.length + 1];
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            int f = entry.getValue();
            if (buckets[f] == null) buckets[f] = new ArrayList<>();
            buckets[f].add(entry.getKey());
        }

        // step 3: collect top-k from highest-frequency buckets
        int[] result = new int[k];
        int idx = 0;
        for (int f = buckets.length - 1; f >= 1 && idx < k; f--) {
            if (buckets[f] == null) continue;
            for (int num : buckets[f]) {
                result[idx++] = num;
                if (idx == k) break;
            }
        }
        return result;
    }
}
