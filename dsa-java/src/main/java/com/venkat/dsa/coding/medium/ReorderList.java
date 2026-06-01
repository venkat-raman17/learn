package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Reorder List
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/reorder-list/
 *
 * <p>Given the head of a singly linked list L0 -> L1 -> ... -> Ln, reorder it in-place to
 * L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ... Do not return a new list; modify in-place.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the list is in the range [1, 5 * 10^4].</li>
 *   <li>1 <= Node.val <= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: head = [1,2,3,4]    ->  Output: [1,4,2,3]
 *   Input: head = [1,2,3,4,5]  ->  Output: [1,5,2,4,3]
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space
 *
 * <p>Hint 1: Find the middle with slow/fast pointers, reverse the second half, then merge the two halves.
 * <p>Hint 2: After splitting at the middle, interleave nodes from the front and the reversed back.
 */
public class ReorderList {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public void reorderList(ListNode head) {
        throw new UnsupportedOperationException("implement me");
    }
}
