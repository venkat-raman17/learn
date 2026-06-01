package com.venkat.dsa.coding.medium.solutions;

/**
 * Reorder List (LeetCode #143)
 *
 * Approach: Three-phase in-place algorithm.
 * 1. Find the middle of the list using the slow/fast pointer technique.
 * 2. Reverse the second half in-place.
 * 3. Interleave nodes from the first half and the reversed second half.
 *
 * Key insight: Splitting + reversing the second half turns the problem into a
 * simple two-pointer merge, avoiding any extra space or data structure.
 *
 * Time complexity:  O(n) — three linear passes.
 * Space complexity: O(1) — all operations are in-place pointer manipulations.
 */
public class ReorderList {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        // Phase 1: find the middle (slow stops at mid for odd, mid-left for even)
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Phase 2: reverse the second half starting from slow.next
        ListNode second = slow.next;
        slow.next = null; // cut the list in half
        ListNode prev = null;
        while (second != null) {
            ListNode tmp = second.next;
            second.next = prev;
            prev = second;
            second = tmp;
        }
        // prev is now the head of the reversed second half

        // Phase 3: interleave first half and reversed second half
        ListNode first = head;
        second = prev;
        while (second != null) {
            ListNode tmp1 = first.next;
            ListNode tmp2 = second.next;
            first.next = second;  // insert second-half node after current first-half node
            second.next = tmp1;   // link back to next first-half node
            first = tmp1;
            second = tmp2;
        }
    }
}
