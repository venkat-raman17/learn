package com.venkat.dsa.coding.medium;

import java.util.HashSet;

/**
 * NeetCode / LeetCode — Longest Consecutive Sequence
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/longest-consecutive-sequence/
 *
 * <p>Given an unsorted array of integers {@code nums}, return the length of the longest
 * consecutive elements sequence (e.g. [1,2,3,4]). You must write an algorithm that runs
 * in O(n) time.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>0 &lt;= nums.length &lt;= 10^5</li>
 *   <li>-10^9 &lt;= nums[i] &lt;= 10^9</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: [100,4,200,1,3,2]    →  Output: 4  (sequence: 1,2,3,4)
 *   Input: [0,3,7,2,5,8,4,6,0,1] →  Output: 9  (sequence: 0–8)
 * </pre>
 *
 * <p><b>Target:</b> Time O(n), Space O(n)
 *
 * <p><b>Hint 1:</b> Put all numbers into a HashSet. A number is the start of a sequence only if
 *   (num - 1) is NOT in the set.
 * <p><b>Hint 2:</b> For each sequence start, keep incrementing and checking the set to find its
 *   length — each number is visited at most twice overall, keeping the overall time O(n).
 */
public class LongestConsecutiveSequence {

    public int longestConsecutive(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
