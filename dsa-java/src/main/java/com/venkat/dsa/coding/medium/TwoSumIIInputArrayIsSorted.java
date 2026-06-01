package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Two Sum II Input Array Is Sorted
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Two Pointers
 * <p>URL: https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
 *
 * <p>Given a 1-indexed array of integers {@code numbers} that is already sorted in non-decreasing
 * order, find two numbers that add up to a specific target number. Return their indices (1-indexed)
 * as an integer array {@code [index1, index2]} where {@code 1 <= index1 < index2 <= numbers.length}.
 *
 * <p>Constraints:
 * <ul>
 *   <li>2 &lt;= numbers.length &lt;= 3 * 10^4</li>
 *   <li>-1000 &lt;= numbers[i] &lt;= 1000</li>
 *   <li>numbers is sorted in non-decreasing order</li>
 *   <li>-1000 &lt;= target &lt;= 1000</li>
 *   <li>Exactly one solution exists</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   twoSum([2,7,11,15], 9) =&gt; [1,2]
 *   twoSum([2,3,4], 6)     =&gt; [1,3]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: Place one pointer at the start and one at the end; compare their sum to the target.
 * <p>Hint 2: If the sum is too large, move the right pointer left; if too small, move the left pointer right.
 */
public class TwoSumIIInputArrayIsSorted {

    public int[] twoSum(int[] numbers, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
