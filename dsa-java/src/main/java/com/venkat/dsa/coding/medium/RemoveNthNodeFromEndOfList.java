package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Remove Nth Node From End of List
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 *
 * <p>Given the head of a linked list, remove the n-th node from the end of the list
 * and return the head of the modified list.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the list is sz.</li>
 *   <li>1 <= sz <= 30</li>
 *   <li>0 <= Node.val <= 100</li>
 *   <li>1 <= n <= sz</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: head = [1,2,3,4,5], n = 2  ->  Output: [1,2,3,5]
 *   Input: head = [1], n = 1          ->  Output: []
 *   Input: head = [1,2], n = 1        ->  Output: [1]
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space (one pass)
 *
 * <p>Hint 1: Use two pointers separated by n steps; when the fast pointer reaches the end,
 *            slow is just before the node to remove.
 * <p>Hint 2: A dummy node before head simplifies removal of the first node.
 */
public class RemoveNthNodeFromEndOfList {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
