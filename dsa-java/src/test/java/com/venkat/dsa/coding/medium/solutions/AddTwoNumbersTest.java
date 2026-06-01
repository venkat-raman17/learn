package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.AddTwoNumbers.ListNode;

class AddTwoNumbersTest {

    private final AddTwoNumbers sol = new AddTwoNumbers();

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
    void example1_342plus465() {
        // 342 + 465 = 807  =>  [7,0,8]
        ListNode result = sol.addTwoNumbers(build(2, 4, 3), build(5, 6, 4));
        assertArrayEquals(new int[]{7, 0, 8}, toArray(result));
    }

    @Test
    void example2_zeroPlusZero() {
        // 0 + 0 = 0  =>  [0]
        ListNode result = sol.addTwoNumbers(build(0), build(0));
        assertArrayEquals(new int[]{0}, toArray(result));
    }

    @Test
    void example3_largeNumbers() {
        // 9999999 + 9999 = 10009998
        // l1=[9,9,9,9,9,9,9], l2=[9,9,9,9]
        // result = [8,9,9,9,0,0,0,1]
        ListNode result = sol.addTwoNumbers(
            build(9, 9, 9, 9, 9, 9, 9),
            build(9, 9, 9, 9)
        );
        assertArrayEquals(new int[]{8, 9, 9, 9, 0, 0, 0, 1}, toArray(result));
    }

    @Test
    void carryAtEnd() {
        // 5 + 5 = 10  =>  [0,1]
        ListNode result = sol.addTwoNumbers(build(5), build(5));
        assertArrayEquals(new int[]{0, 1}, toArray(result));
    }

    @Test
    void differentLengths() {
        // 99 + 1 = 100  => [0,0,1]
        ListNode result = sol.addTwoNumbers(build(9, 9), build(1));
        assertArrayEquals(new int[]{0, 0, 1}, toArray(result));
    }
}
