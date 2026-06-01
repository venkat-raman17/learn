package com.venkat.dsa.coding.medium.solutions;

/**
 * Find the Duplicate Number (LeetCode #287)
 *
 * Approach: Floyd's Cycle Detection (tortoise and hare). Treat array indices
 * as a linked list where node i points to node nums[i]. Because nums contains
 * a duplicate, two indices map to the same next-index, forming a cycle. The
 * duplicate value is the entry point of that cycle.
 *
 * Phase 1 finds the meeting point inside the cycle using slow (1 step) and
 * fast (2 steps) pointers. Phase 2 resets one pointer to index 0; both then
 * advance one step at a time and meet exactly at the cycle entrance (the
 * duplicate number).
 *
 * Key insight: The implicit linked-list view lets us apply cycle detection to
 * an array without modifying it, satisfying the O(1) extra space constraint.
 *
 * Time complexity:  O(n) — both phases are linear.
 * Space complexity: O(1) — only pointer variables.
 */
public class FindTheDuplicateNumber {

    public int findDuplicate(int[] nums) {
        // Phase 1: detect meeting point inside the cycle
        int slow = nums[0];
        int fast = nums[0];
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        // Phase 2: find the cycle entrance (the duplicate)
        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow; // cycle entrance == duplicate value
    }
}
