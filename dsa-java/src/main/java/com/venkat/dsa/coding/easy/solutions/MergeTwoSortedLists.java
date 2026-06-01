package com.venkat.dsa.coding.easy.solutions;

/**
 * Merge Two Sorted Lists (LeetCode #21)
 *
 * Approach: Use a dummy sentinel head to simplify edge cases. Advance two
 * pointers through both lists simultaneously, always appending the smaller
 * node to the result list. After one list is exhausted, append the remainder
 * of the other (already sorted) list in O(1) by linking its head.
 *
 * Key insight: The dummy node avoids special-casing the very first node and
 * lets the main loop stay uniform throughout.
 *
 * Time complexity:  O(m + n) — each node is visited exactly once.
 * Space complexity: O(1) — nodes are relinked, no new nodes are created.
 */
public class MergeTwoSortedLists {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0); // sentinel so we never have a null tail
        ListNode tail = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                tail.next = list1;  // take from list1
                list1 = list1.next;
            } else {
                tail.next = list2;  // take from list2
                list2 = list2.next;
            }
            tail = tail.next;
        }
        // At most one list is non-null; link its remaining nodes directly
        tail.next = (list1 != null) ? list1 : list2;
        return dummy.next;
    }
}
