package com.venkat.dsa.coding.hard.solutions;

/**
 * Median of Two Sorted Arrays (LeetCode #4)
 *
 * Approach: Binary search on the shorter array to find a partition point i such
 * that the left half of the merged virtual array contains exactly (m+n+1)/2
 * elements. For every candidate partition (i in nums1, j in nums2), verify that
 * the maximum of the left sides <= minimum of the right sides. When the condition
 * holds, compute the median from the four boundary elements.
 *
 * Key insight: A valid partition exists where maxLeft1 <= minRight2 AND
 * maxLeft2 <= minRight1. Binary search on i (the smaller array) finds this
 * partition in O(log(min(m,n))) without merging or extra memory.
 *
 * Time complexity:  O(log(min(m, n))) — binary search on the shorter array.
 * Space complexity: O(1) — only scalar variables.
 */
public class MedianOfTwoSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // Ensure nums1 is the shorter array to minimise binary-search range
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;
        int halfLen = (m + n + 1) / 2; // size of the desired left partition

        int lo = 0, hi = m;

        while (lo <= hi) {
            int i = lo + (hi - lo) / 2; // partition index in nums1
            int j = halfLen - i;        // partition index in nums2

            // Boundary values (use +/-INF for out-of-bounds)
            int maxLeft1  = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            int minRight1 = (i == m) ? Integer.MAX_VALUE : nums1[i];
            int maxLeft2  = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            int minRight2 = (j == n) ? Integer.MAX_VALUE : nums2[j];

            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                // Found the correct partition
                int maxLeft  = Math.max(maxLeft1, maxLeft2);
                int minRight = Math.min(minRight1, minRight2);

                if ((m + n) % 2 == 1) {
                    return maxLeft; // odd total: median is the max of left side
                } else {
                    return (maxLeft + minRight) / 2.0; // even total: average the two middle elements
                }
            } else if (maxLeft1 > minRight2) {
                hi = i - 1; // move partition left in nums1
            } else {
                lo = i + 1; // move partition right in nums1
            }
        }

        // Should never reach here for valid sorted input
        throw new IllegalArgumentException("Input arrays are not sorted.");
    }
}
