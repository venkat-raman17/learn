package com.venkat.dsa.coding.easy.solutions;

import java.util.HashMap;
import java.util.Map;

/**
 * Two Sum (LeetCode 1) — Easy
 *
 * <p>Approach: Maintain a HashMap from value to index while iterating. For each element,
 * compute its complement ({@code target - nums[i]}) and look it up in the map. If found,
 * we have our answer; otherwise store the current value and index for future lookups.
 *
 * <p><b>Time complexity:</b> O(n) — single pass with O(1) map operations.<br>
 * <b>Space complexity:</b> O(n) — the map holds at most n entries.
 *
 * <p><b>Key insight:</b> Instead of searching for the complement after building the full
 * map, we search as we build, which naturally avoids using the same index twice.
 */
public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        // maps each value seen so far to its index
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        // problem guarantees exactly one solution; this line is never reached
        throw new IllegalArgumentException("No two-sum solution");
    }
}
