package com.venkat.dsa.coding.easy.solutions;

/**
 * Reverse Linked List (LeetCode #206)
 *
 * Approach: Iterative three-pointer reversal. Traverse the list once, at each
 * node flipping the {@code next} pointer to point to the previous node. A
 * {@code prev} pointer starts at null (the new tail's next) and advances with
 * the traversal until {@code curr} reaches null, at which point {@code prev}
 * is the new head.
 *
 * Key insight: Only three pointers are needed — prev, curr, and next — making
 * it possible to reverse in-place in a single pass without extra storage.
 *
 * Time complexity:  O(n) — single pass through the list.
 * Space complexity: O(1) — constant extra space.
 */
public class ReverseLinkedList {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next; // save next before overwriting
            curr.next = prev;          // flip the pointer
            prev = curr;               // advance prev
            curr = next;               // advance curr
        }
        return prev; // prev is now the new head
    }
}
