package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MergeTwoSortedListsTest {

    private MergeTwoSortedLists.ListNode buildList(int... vals) {
        MergeTwoSortedLists.ListNode dummy = new MergeTwoSortedLists.ListNode(0);
        MergeTwoSortedLists.ListNode cur = dummy;
        for (int v : vals) { cur.next = new MergeTwoSortedLists.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(MergeTwoSortedLists.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testMergeTwoNonEmpty() {
        MergeTwoSortedLists sol = new MergeTwoSortedLists();
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4},
                toArray(sol.mergeTwoLists(buildList(1, 2, 4), buildList(1, 3, 4))));
    }

    @Test
    void testBothEmpty() {
        MergeTwoSortedLists sol = new MergeTwoSortedLists();
        assertNull(sol.mergeTwoLists(null, null));
    }

    @Test
    void testOneEmptyOneNonEmpty() {
        MergeTwoSortedLists sol = new MergeTwoSortedLists();
        assertArrayEquals(new int[]{0},
                toArray(sol.mergeTwoLists(null, buildList(0))));
    }
}
