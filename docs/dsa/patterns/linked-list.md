# Linked List

## When to reach for it

- The problem gives you a `ListNode` head and asks you to rearrange, traverse, or detect something structural.
- You see phrases like "reverse", "cycle", "merge", "reorder", "nth from end", or "k-group".
- Random/next pointers must be deep-copied.
- You need O(1)-space in-place mutation of a sequence (no random access needed).
- Two-pointer tricks (fast/slow, left/right) on a sequence — linked list is the natural fit.
- The problem involves merging sorted sequences or a priority queue over list heads.

## The idea

Most linked-list problems reduce to one of three primitives: **pointer manipulation** (reverse, splice, reorder), **two-pointer traversal** (cycle detection, nth-from-end, middle), or **merge/sort** (merge two or k sorted lists). Work with a dummy head node to avoid null-checks at the front. Draw the pointer reassignment order before coding — one wrong sequence destroys references. Time is usually O(n) or O(n log k); space is O(1) for in-place and O(n) for copy/recursion problems.

## Template

```java
// Dummy-head + iterative skeleton (covers ~80% of problems)
class Solution {
    public ListNode solve(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prev = dummy;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next; // always save next before relinking

            // --- do work here ---
            // e.g., reverse:  curr.next = prev; prev = curr; curr = next;
            // e.g., skip:     prev.next = curr.next; curr = curr.next;
            // e.g., insert:   prev.next = newNode; newNode.next = curr; prev = newNode;

            prev = curr;
            curr = next;
        }

        return dummy.next;
    }
}

// Fast / slow pointer skeleton (cycle, middle, nth-from-end)
class TwoPointer {
    public ListNode findMiddle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow; // middle (or start of second half)
    }

    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    // nth from end: advance fast n steps, then move both until fast == null
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++) fast = fast.next;
        while (fast != null) { slow = slow.next; fast = fast.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }
}

// Merge two sorted lists
class Merge {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { cur.next = l1; l1 = l1.next; }
            else                  { cur.next = l2; l2 = l2.next; }
            cur = cur.next;
        }
        cur.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
}
```

## Variations

- **Reverse a sublist (k-group):** Identify group boundaries, reverse in-place, reconnect tail to next group head. Recurse or iterate.
- **Cycle entry point:** After slow/fast meet, reset one pointer to head and advance both one step at a time — they meet at the cycle entry (Floyd's algorithm).
- **Deep copy with random pointer:** HashMap `original -> copy`; first pass builds all nodes, second pass wires `.next` and `.random`.
- **Add two numbers:** Simulate grade-school addition with a carry variable; handle the extra carry digit after the loop.
- **Merge k sorted lists:** Use a `PriorityQueue<ListNode>` ordered by val; seed with all heads, poll-and-push greedily — O(n log k).
- **LRU Cache:** Doubly linked list (for O(1) removal) + HashMap (for O(1) lookup). Always maintain a dummy head and dummy tail to avoid edge-case null checks.
- **Find duplicate (pigeonhole / Floyd):** Treat array values as next-pointers; cycle entry = duplicate. O(n) time, O(1) space.

## Pitfalls

- **Losing the next pointer:** Always `ListNode next = curr.next` before relinking `curr.next`.
- **Off-by-one in fast pointer:** `fast != null && fast.next != null` — forgetting the second condition causes NPE on even-length lists.
- **Forgetting to disconnect the old tail:** After splitting a list (e.g., reorder), null out the tail of the first half or you get a cycle.
- **Returning `head` instead of `dummy.next`:** When the head can move (deletion, reverse), always return `dummy.next`.
- **Mutation order in reversal:** `curr.next = prev` must happen before `prev = curr`, and `next` must be saved before `curr.next = prev`.
- **LRU double-link bugs:** Remove a node by relinking both its prev and next neighbors; partial relinking leaves dangling pointers.
- **Recursion stack depth:** Recursive reversal is elegant but O(n) stack space — fine for NeetCode, but note it in interviews.

## Problems in this pattern

| Problem | Key technique |
|---|---|
| Reverse Linked List | Iterative pointer flip or recursion |
| Merge Two Sorted Lists | Dummy head + compare-and-advance |
| Linked List Cycle | Fast/slow pointers |
| Reorder List | Find middle, reverse second half, interleave |
| Remove Nth Node From End | Fast pointer n-step lead + slow pointer |
| Copy List With Random Pointer | HashMap original->copy, two-pass wiring |
| Add Two Numbers | Carry-based digit simulation |
| Find The Duplicate Number | Floyd cycle detection on implicit graph |
| LRU Cache | Doubly linked list + HashMap |
| Merge K Sorted Lists | Min-heap over list heads |
| Reverse Nodes In K Group | Group boundary tracking + in-place reversal |
