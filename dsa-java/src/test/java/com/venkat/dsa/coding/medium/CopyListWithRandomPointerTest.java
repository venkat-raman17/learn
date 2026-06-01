package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CopyListWithRandomPointerTest {

    @Test
    void testNullInput() {
        CopyListWithRandomPointer sol = new CopyListWithRandomPointer();
        assertNull(sol.copyRandomList(null));
    }

    @Test
    void testTwoNodes() {
        // [[1,1],[2,1]]  -> node0.random = node1, node1.random = node1
        CopyListWithRandomPointer.Node n0 = new CopyListWithRandomPointer.Node(1);
        CopyListWithRandomPointer.Node n1 = new CopyListWithRandomPointer.Node(2);
        n0.next = n1;
        n0.random = n1;
        n1.random = n1;

        CopyListWithRandomPointer sol = new CopyListWithRandomPointer();
        CopyListWithRandomPointer.Node copy = sol.copyRandomList(n0);

        assertNotNull(copy);
        assertNotSame(n0, copy);
        assertEquals(1, copy.val);
        assertNotNull(copy.next);
        assertEquals(2, copy.next.val);
        assertSame(copy.next, copy.random);        // random of copy[0] should point to copy[1]
        assertSame(copy.next, copy.next.random);   // random of copy[1] should point to copy[1]
    }

    @Test
    void testSingleNodeNoRandom() {
        CopyListWithRandomPointer.Node n = new CopyListWithRandomPointer.Node(7);
        CopyListWithRandomPointer sol = new CopyListWithRandomPointer();
        CopyListWithRandomPointer.Node copy = sol.copyRandomList(n);
        assertNotNull(copy);
        assertNotSame(n, copy);
        assertEquals(7, copy.val);
        assertNull(copy.random);
    }
}
