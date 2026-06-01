package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class AddTwoNumbersTest {

    private AddTwoNumbers.ListNode buildList(int... vals) {
        AddTwoNumbers.ListNode dummy = new AddTwoNumbers.ListNode(0);
        AddTwoNumbers.ListNode cur = dummy;
        for (int v : vals) { cur.next = new AddTwoNumbers.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(AddTwoNumbers.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testBasicAddition() {
        // 342 + 465 = 807
        AddTwoNumbers sol = new AddTwoNumbers();
        assertArrayEquals(new int[]{7, 0, 8},
                toArray(sol.addTwoNumbers(buildList(2, 4, 3), buildList(5, 6, 4))));
    }

    @Test
    void testZeroPlusZero() {
        AddTwoNumbers sol = new AddTwoNumbers();
        assertArrayEquals(new int[]{0},
                toArray(sol.addTwoNumbers(buildList(0), buildList(0))));
    }

    @Test
    void testCarryPropagation() {
        // 9999999 + 9999 = 10009998
        AddTwoNumbers sol = new AddTwoNumbers();
        assertArrayEquals(new int[]{8, 9, 9, 9, 0, 0, 0, 1},
                toArray(sol.addTwoNumbers(
                        buildList(9, 9, 9, 9, 9, 9, 9),
                        buildList(9, 9, 9, 9))));
    }
}
