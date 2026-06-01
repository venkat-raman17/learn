package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.hard.solutions.MergeKSortedLists.ListNode;

class MergeKSortedListsTest {

    private final MergeKSortedLists sol = new MergeKSortedLists();

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
    void example1_threeListsMerge() {
        // [[1,4,5],[1,3,4],[2,6]] => [1,1,2,3,4,4,5,6]
        ListNode[] lists = {build(1, 4, 5), build(1, 3, 4), build(2, 6)};
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, toArray(sol.mergeKLists(lists)));
    }

    @Test
    void example2_emptyInput() {
        assertNull(sol.mergeKLists(new ListNode[]{}));
    }

    @Test
    void example3_singleEmptyList() {
        assertNull(sol.mergeKLists(new ListNode[]{null}));
    }

    @Test
    void allListsNull() {
        assertNull(sol.mergeKLists(new ListNode[]{null, null, null}));
    }

    @Test
    void singleList() {
        ListNode[] lists = {build(1, 2, 3)};
        assertArrayEquals(new int[]{1, 2, 3}, toArray(sol.mergeKLists(lists)));
    }

    @Test
    void mixedNullAndNonNull() {
        ListNode[] lists = {null, build(1, 3), null, build(2, 4)};
        assertArrayEquals(new int[]{1, 2, 3, 4}, toArray(sol.mergeKLists(lists)));
    }

    @Test
    void singleElementLists() {
        ListNode[] lists = {build(3), build(1), build(2)};
        assertArrayEquals(new int[]{1, 2, 3}, toArray(sol.mergeKLists(lists)));
    }
}
