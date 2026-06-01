package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.ReverseLinkedList.ListNode;

class ReverseLinkedListTest {

    private final ReverseLinkedList sol = new ReverseLinkedList();

    // Helper: array -> linked list
    private ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    // Helper: linked list -> array representation as string for easy comparison
    private int[] toArray(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void example1_fiveElements() {
        // 1->2->3->4->5  =>  5->4->3->2->1
        ListNode result = sol.reverseList(build(1, 2, 3, 4, 5));
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(result));
    }

    @Test
    void example2_twoElements() {
        // 1->2  =>  2->1
        ListNode result = sol.reverseList(build(1, 2));
        assertArrayEquals(new int[]{2, 1}, toArray(result));
    }

    @Test
    void singleElement() {
        ListNode result = sol.reverseList(build(42));
        assertArrayEquals(new int[]{42}, toArray(result));
    }

    @Test
    void emptyList() {
        assertNull(sol.reverseList(null));
    }

    @Test
    void allSameValues() {
        // 3->3->3  =>  3->3->3
        ListNode result = sol.reverseList(build(3, 3, 3));
        assertArrayEquals(new int[]{3, 3, 3}, toArray(result));
    }
}
