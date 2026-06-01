package com.venkat.dsa.coding.easy;

import java.util.HashMap;

/**
 * NeetCode / LeetCode — Two Sum
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/two-sum/
 *
 * <p>Given an array of integers {@code nums} and an integer {@code target}, return the indices
 * of the two numbers that add up to {@code target}. Each input has exactly one solution and
 * you may not use the same element twice.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>2 &lt;= nums.length &lt;= 10^4</li>
 *   <li>-10^9 &lt;= nums[i] &lt;= 10^9</li>
 *   <li>-10^9 &lt;= target &lt;= 10^9</li>
 *   <li>Only one valid answer exists.</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: nums = [2,7,11,15], target = 9  →  Output: [0,1]
 *   Input: nums = [3,2,4],     target = 6  →  Output: [1,2]
 * </pre>
 *
 * <p><b>Target:</b> Time O(n), Space O(n)
 *
 * <p><b>Hint 1:</b> Use a HashMap to store each number's index as you iterate.
 * <p><b>Hint 2:</b> For each element, check if its complement (target - nums[i]) already exists in the map.
 */
public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
