package com.venkat.dsa.coding.medium.solutions;

/**
 * Merge Triplets to Form Target Triplet (LeetCode #1899)
 *
 * <p>A triplet is usable only if none of its values exceeds the corresponding target value
 * (merging via max would otherwise permanently raise a component above the target). Greedy: filter
 * out invalid triplets, then check if the component-wise OR (max) of all remaining triplets equals
 * the target.
 *
 * <p><b>Key insight:</b> Any triplet with a value exceeding the target in any component must be
 * discarded — it would corrupt the target. Valid triplets can always be freely merged in any order.
 *
 * <p><b>Time complexity:</b> O(n) — single pass through triplets.<br>
 * <b>Space complexity:</b> O(1) — three running max variables.
 */
public class MergeTripletsToFormTargetTriplet {

    public boolean mergeTriplets(int[][] triplets, int[] target) {
        int a = 0, b = 0, c = 0; // best achievable so far

        for (int[] t : triplets) {
            // Skip triplets that would force any component above the target
            if (t[0] > target[0] || t[1] > target[1] || t[2] > target[2]) {
                continue;
            }
            // Merge by taking the max in each component
            a = Math.max(a, t[0]);
            b = Math.max(b, t[1]);
            c = Math.max(c, t[2]);
        }

        return a == target[0] && b == target[1] && c == target[2];
    }
}
