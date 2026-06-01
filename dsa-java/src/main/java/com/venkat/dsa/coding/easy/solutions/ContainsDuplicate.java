package com.venkat.dsa.coding.easy.solutions;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains Duplicate (LeetCode 217) — Easy
 *
 * <p>Approach: Stream the array into a {@link HashSet}. The moment {@code add} returns
 * {@code false} we know the element already exists, so we return {@code true} immediately.
 * A final fall-through returns {@code false} if every element was unique.
 *
 * <p><b>Time complexity:</b> O(n) — one pass through the array.<br>
 * <b>Space complexity:</b> O(n) — at most n elements in the set.
 *
 * <p><b>Key insight:</b> A HashSet gives O(1) average-case membership checks, turning
 * an otherwise O(n²) brute-force into a single linear scan.
 */
public class ContainsDuplicate {

    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int n : nums) {
            // add returns false when the element is already present
            if (!seen.add(n)) {
                return true;
            }
        }
        return false;
    }
}
