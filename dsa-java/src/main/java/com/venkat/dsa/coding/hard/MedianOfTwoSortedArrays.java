package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Median of Two Sorted Arrays
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Binary Search
 * <p>URL: https://leetcode.com/problems/median-of-two-sorted-arrays/
 *
 * <p>Given two sorted arrays {@code nums1} and {@code nums2} of sizes m and n respectively,
 * return the median of the two sorted arrays combined. The overall run time complexity must
 * be O(log(m + n)).
 *
 * <p>Constraints:
 * <ul>
 *   <li>nums1.length == m, nums2.length == n</li>
 *   <li>0 &lt;= m, n &lt;= 1000</li>
 *   <li>1 &lt;= m + n &lt;= 2000</li>
 *   <li>-10^6 &lt;= nums1[i], nums2[i] &lt;= 10^6</li>
 * </ul>
 *
 * <p>Example 1: nums1 = [1,3], nums2 = [2] → 2.00000
 * <p>Example 2: nums1 = [1,2], nums2 = [3,4] → 2.50000
 *
 * <p>Target: O(log(min(m,n))) time, O(1) space.
 *
 * <p>Hint 1: Binary search on the partition point of the smaller array; the correct partition
 *   satisfies maxLeft1 &lt;= minRight2 and maxLeft2 &lt;= minRight1.
 * <p>Hint 2: Always binary search on the shorter array to keep the partition of the longer array
 *   fully determined; use Integer.MIN_VALUE / MAX_VALUE as sentinels for empty left/right halves.
 */
public class MedianOfTwoSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        throw new UnsupportedOperationException("implement me");
    }
}
