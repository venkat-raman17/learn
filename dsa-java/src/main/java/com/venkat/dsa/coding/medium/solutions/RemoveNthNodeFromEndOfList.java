package com.venkat.dsa.coding.medium.solutions;

/**
 * Remove Nth Node From End of List (LeetCode #19)
 *
 * Approach: Two-pointer one-pass technique with a dummy sentinel head. Advance
 * the fast pointer n+1 steps ahead of the slow pointer. Then advance both
 * together until fast reaches null; slow will be sitting on the node just
 * before the one to remove, making the deletion a single pointer reassignment.
 *
 * Key insight: Keeping fast exactly n+1 nodes ahead ensures that when fast
 * hits null, slow is at the predecessor of the target — enabling O(1) deletion
 * with one forward pass.
 *
 * Time complexity:  O(L) — single pass through the list.
 * Space complexity: O(1) — only two pointer variables.
 */
public class RemoveNthNodeFromEndOfList {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head); // sentinel avoids null-check for head removal
        ListNode fast = dummy;
        ListNode slow = dummy;

        // Advance fast n+1 steps so the gap between slow and fast is exactly n+1
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }

        // Move both together until fast reaches null
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }

        // slow.next is the node to remove
        slow.next = slow.next.next;
        return dummy.next;
    }
}
