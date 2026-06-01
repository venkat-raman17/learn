package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Longest Increasing Subsequence (LeetCode 300)
 *
 * Approach: Patience-sorting / binary-search DP. We maintain a "tails" list
 * where tails[i] is the smallest tail element of all increasing subsequences
 * of length i+1 seen so far. For each number we binary-search for the first
 * tail >= num (lower bound) and replace it, or append if num is larger than
 * all tails. The answer is tails.size().
 *
 * Key insight: The tails list is always sorted, enabling O(log n) binary
 * search per element. The list length equals the LIS length at every step,
 * even though the list itself does not represent the actual LIS.
 *
 * Time:  O(n log n)
 * Space: O(n)
 */
public class LongestIncreasingSubsequence {

    public int lengthOfLIS(int[] nums) {
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            // binary search: find leftmost index where tails[idx] >= num
            int lo = 0, hi = tails.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (tails.get(mid) < num) lo = mid + 1;
                else hi = mid;
            }
            if (lo == tails.size()) {
                tails.add(num); // extend the subsequence
            } else {
                tails.set(lo, num); // replace to keep smallest possible tail
            }
        }
        return tails.size();
    }
}
