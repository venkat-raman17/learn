package com.venkat.dsa.coding.hard.solutions;

import java.util.PriorityQueue;

/**
 * Merge K Sorted Lists (LeetCode #23)
 *
 * Approach: Min-heap (priority queue). Pre-populate the heap with the head
 * node of each non-null list. Repeatedly poll the smallest node, append it to
 * the result list, and push its successor (if any) into the heap. This
 * guarantees the globally minimum available node is always chosen next.
 *
 * Key insight: The heap always holds at most k nodes — one per list — so each
 * of the N total nodes is inserted and removed from the heap exactly once,
 * giving O(N log k) overall.
 *
 * Time complexity:  O(N log k) — N = total nodes, k = number of lists.
 * Space complexity: O(k) — the heap holds at most k elements at any time.
 */
public class MergeKSortedLists {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode mergeKLists(ListNode[] lists) {
        // Min-heap ordered by node value
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);

        // Seed the heap with the head of every non-empty list
        for (ListNode node : lists) {
            if (node != null) heap.offer(node);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (!heap.isEmpty()) {
            ListNode node = heap.poll();     // take the smallest
            tail.next = node;
            tail = tail.next;
            if (node.next != null) heap.offer(node.next); // push its successor
        }

        return dummy.next;
    }
}
