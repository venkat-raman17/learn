package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MergeKSortedListsTest {

    private MergeKSortedLists.ListNode buildList(int... vals) {
        MergeKSortedLists.ListNode dummy = new MergeKSortedLists.ListNode(0);
        MergeKSortedLists.ListNode cur = dummy;
        for (int v : vals) { cur.next = new MergeKSortedLists.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(MergeKSortedLists.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testThreeLists() {
        MergeKSortedLists sol = new MergeKSortedLists();
        MergeKSortedLists.ListNode[] lists = {
                buildList(1, 4, 5),
                buildList(1, 3, 4),
                buildList(2, 6)
        };
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, toArray(sol.mergeKLists(lists)));
    }

    @Test
    void testEmptyArray() {
        MergeKSortedLists sol = new MergeKSortedLists();
        assertNull(sol.mergeKLists(new MergeKSortedLists.ListNode[]{}));
    }

    @Test
    void testSingleEmptyList() {
        MergeKSortedLists sol = new MergeKSortedLists();
        assertNull(sol.mergeKLists(new MergeKSortedLists.ListNode[]{null}));
    }
}
