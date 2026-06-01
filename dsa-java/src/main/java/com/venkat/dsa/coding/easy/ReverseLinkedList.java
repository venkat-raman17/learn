package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Reverse Linked List
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/reverse-linked-list/
 *
 * <p>Given the head of a singly linked list, reverse the list and return the new head.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 5000].</li>
 *   <li>-5000 <= Node.val <= 5000</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: head = [1,2,3,4,5]  ->  Output: [5,4,3,2,1]
 *   Input: head = [1,2]        ->  Output: [2,1]
 * </pre>
 *
 * <p>Target: O(n) time, O(1) space (iterative) or O(n) space (recursive)
 *
 * <p>Hint 1: Use three pointers: prev, curr, next to iteratively reverse links.
 * <p>Hint 2: At each step, save curr.next, point curr.next to prev, then advance both pointers.
 */
public class ReverseLinkedList {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseList(ListNode head) {
        throw new UnsupportedOperationException("implement me");
    }
}
