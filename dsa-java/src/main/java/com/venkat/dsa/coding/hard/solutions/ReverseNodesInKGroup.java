package com.venkat.dsa.coding.hard.solutions;

/**
 * Reverse Nodes in k-Group (LeetCode #25)
 *
 * Approach: Iterative in-place reversal with a dummy sentinel head. For each
 * group, first check that at least k nodes remain (if not, leave them as-is).
 * Then reverse exactly k nodes in-place using the standard three-pointer
 * technique, reconnect the reversed segment to the previously processed tail
 * and the next group's start, and advance the tail pointer.
 *
 * Key insight: Tracking the "previous group's tail" pointer makes it possible
 * to stitch reversed segments together without a second pass. Checking group
 * size upfront (by walking k steps) avoids reversing a short tail that should
 * remain unchanged.
 *
 * Time complexity:  O(n) — each node is visited at most twice (once during the
 *                   k-check and once during reversal).
 * Space complexity: O(1) — iterative; no recursion stack.
 */
public class ReverseNodesInKGroup {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode groupPrev = dummy; // tail of the last reversed group

        while (true) {
            // Find the k-th node from groupPrev; if fewer than k remain, stop
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) break;

            ListNode groupNext = kth.next; // first node of the next group

            // Reverse the k nodes from groupPrev.next to kth
            ListNode prev = groupNext; // after reversal, kth.next will be groupNext
            ListNode cur  = groupPrev.next;
            while (cur != groupNext) {
                ListNode tmp = cur.next;
                cur.next = prev;
                prev = cur;
                cur = tmp;
            }

            // Reconnect: groupPrev -> (new head of reversed group = kth)
            ListNode tmp = groupPrev.next; // will become the new tail of this group
            groupPrev.next = kth;
            groupPrev = tmp; // advance groupPrev to the tail of the just-reversed group
        }

        return dummy.next;
    }

    /** Return the node k steps ahead of {@code curr}, or null if fewer than k nodes remain. */
    private ListNode getKth(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }
        return curr;
    }
}
