package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.MergeTwoSortedLists.ListNode;

class MergeTwoSortedListsTest {

    private final MergeTwoSortedLists sol = new MergeTwoSortedLists();

    private ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void example1_interleavedMerge() {
        // [1,2,4] + [1,3,4] => [1,1,2,3,4,4]
        ListNode result = sol.mergeTwoLists(build(1, 2, 4), build(1, 3, 4));
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4}, toArray(result));
    }

    @Test
    void example2_bothEmpty() {
        assertNull(sol.mergeTwoLists(null, null));
    }

    @Test
    void example3_oneEmpty() {
        // [] + [0] => [0]
        ListNode result = sol.mergeTwoLists(null, build(0));
        assertArrayEquals(new int[]{0}, toArray(result));
    }

    @Test
    void list1Empty() {
        ListNode result = sol.mergeTwoLists(null, build(1, 2, 3));
        assertArrayEquals(new int[]{1, 2, 3}, toArray(result));
    }

    @Test
    void list2Empty() {
        ListNode result = sol.mergeTwoLists(build(1, 2, 3), null);
        assertArrayEquals(new int[]{1, 2, 3}, toArray(result));
    }

    @Test
    void allElementsFromList1First() {
        // [1,2,3] + [4,5,6] => [1,2,3,4,5,6]
        ListNode result = sol.mergeTwoLists(build(1, 2, 3), build(4, 5, 6));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, toArray(result));
    }

    @Test
    void duplicateValues() {
        // [2,2] + [2,2] => [2,2,2,2]
        ListNode result = sol.mergeTwoLists(build(2, 2), build(2, 2));
        assertArrayEquals(new int[]{2, 2, 2, 2}, toArray(result));
    }
}
