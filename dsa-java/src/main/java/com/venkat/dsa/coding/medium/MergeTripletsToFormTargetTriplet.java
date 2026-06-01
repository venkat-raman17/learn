package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Merge Triplets to Form Target Triplet
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/merge-triplets-to-form-target-triplet/
 *
 * <p>Given a 2D integer array {@code triplets} where {@code triplets[i] = [a, b, c]} and a target
 * triplet {@code target = [x, y, z]}, you may merge any two triplets by taking the element-wise
 * maximum. Return {@code true} if it is possible to obtain the target triplet as a merged result.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= triplets.length &lt;= 10^5</li>
 *   <li>triplets[i].length == target.length == 3</li>
 *   <li>1 &lt;= a, b, c, x, y, z &lt;= 1000</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: triplets = [[2,5,3],[1,8,4],[1,7,5]], target = [2,7,5]  Output: true
 * Input: triplets = [[3,4,5],[4,5,6]], target = [3,2,5]          Output: false
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space
 *
 * <p><b>Hint 1:</b> Discard any triplet that exceeds the target in any dimension — it can only hurt.
 * <p><b>Hint 2:</b> Among the remaining valid triplets, check whether the element-wise max equals
 * the target.
 */
public class MergeTripletsToFormTargetTriplet {

    /**
     * Returns true if the target triplet can be formed by merging a subset of the given triplets.
     *
     * @param triplets array of triplets [a, b, c]
     * @param target   desired triplet [x, y, z]
     * @return whether the target is achievable
     */
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        throw new UnsupportedOperationException("implement me");
    }
}
