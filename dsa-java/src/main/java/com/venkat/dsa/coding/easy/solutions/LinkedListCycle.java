package com.venkat.dsa.coding.easy.solutions;

/**
 * Linked List Cycle (LeetCode #141)
 *
 * Approach: Floyd's Tortoise and Hare cycle-detection algorithm. Two pointers
 * start at the head; the slow pointer advances one step at a time while the
 * fast pointer advances two steps. If there is a cycle, the fast pointer will
 * eventually lap the slow pointer and they will meet inside the cycle. If there
 * is no cycle, the fast pointer reaches null first.
 *
 * Key insight: In a cycle of length L, the hare gains one node per step on the
 * tortoise, so they must meet within at most L steps after the tortoise enters
 * the cycle — no extra memory needed.
 *
 * Time complexity:  O(n) — the fast pointer traverses at most 2n nodes.
 * Space complexity: O(1) — only two pointer variables.
 */
public class LinkedListCycle {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;       // advance one step
            fast = fast.next.next;  // advance two steps
            if (slow == fast) return true; // pointers met inside the cycle
        }
        return false; // fast reached null — no cycle
    }
}
