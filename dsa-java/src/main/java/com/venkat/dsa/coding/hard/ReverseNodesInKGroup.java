package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Reverse Nodes In K Group
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/reverse-nodes-in-k-group/
 *
 * <p>Given the head of a linked list, reverse the nodes of the list k at a time and return the
 * modified list. If the number of remaining nodes is not a multiple of k, leave them as-is.
 * You may not alter the values in the nodes; only the nodes themselves may be changed.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the list is n.</li>
 *   <li>1 <= k <= n <= 5000</li>
 *   <li>0 <= Node.val <= 1000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: head = [1,2,3,4,5], k = 2  ->  Output: [2,1,4,3,5]
 *   Input: head = [1,2,3,4,5], k = 3  ->  Output: [3,2,1,4,5]
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space (iterative) or O(n/k) space (recursive)
 *
 * <p>Hint 1: First check that at least k nodes remain; if not, return the head unchanged.
 * <p>Hint 2: Reverse the current group of k nodes in-place, then recursively (or iteratively)
 *            process the rest and stitch the segments together.
 */
public class ReverseNodesInKGroup {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
