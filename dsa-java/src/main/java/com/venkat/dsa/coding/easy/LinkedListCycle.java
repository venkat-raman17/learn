package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Linked List Cycle
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/linked-list-cycle/
 *
 * <p>Given the head of a linked list, determine if the linked list has a cycle in it.
 * A cycle exists if some node can be reached again by continuously following the next pointer.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 10^4].</li>
 *   <li>-10^5 <= Node.val <= 10^5</li>
 *   <li>pos is -1 or a valid index in the linked list (pos is not passed to the method).</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: head = [3,2,0,-4], pos = 1  ->  Output: true  (tail connects to index 1)
 *   Input: head = [1,2], pos = 0       ->  Output: true
 *   Input: head = [1], pos = -1        ->  Output: false
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space
 *
 * <p>Hint 1: Use Floyd's two-pointer (slow/fast) algorithm — if there is a cycle they will eventually meet.
 * <p>Hint 2: Move slow one step and fast two steps; if fast or fast.next becomes null, no cycle exists.
 */
public class LinkedListCycle {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public boolean hasCycle(ListNode head) {
        throw new UnsupportedOperationException("implement me");
    }
}
