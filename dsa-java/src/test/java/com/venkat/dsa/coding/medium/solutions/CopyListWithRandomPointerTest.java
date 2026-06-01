package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.CopyListWithRandomPointer.Node;

class CopyListWithRandomPointerTest {

    private final CopyListWithRandomPointer sol = new CopyListWithRandomPointer();

    @Test
    void example1_threeNodes() {
        // [[7,null],[13,0],[11,4],[10,2],[1,0]]
        // Build: 7->13->11->10->1
        Node n1 = new Node(7);
        Node n2 = new Node(13);
        Node n3 = new Node(11);
        Node n4 = new Node(10);
        Node n5 = new Node(1);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n5;
        n1.random = null;
        n2.random = n1;
        n3.random = n5;
        n4.random = n3;
        n5.random = n1;

        Node copy = sol.copyRandomList(n1);

        // Verify values
        assertEquals(7,  copy.val);
        assertEquals(13, copy.next.val);
        assertEquals(11, copy.next.next.val);
        assertEquals(10, copy.next.next.next.val);
        assertEquals(1,  copy.next.next.next.next.val);

        // Verify it is a deep copy (different node objects)
        assertNotSame(n1, copy);
        assertNotSame(n2, copy.next);

        // Verify random pointers point to clones, not originals
        assertNull(copy.random);
        assertSame(copy, copy.next.random);               // 13's random -> 7 (clone)
        assertSame(copy.next.next.next.next, copy.next.next.random);  // 11's random -> 1 (clone)
        assertSame(copy.next.next, copy.next.next.next.random);       // 10's random -> 11 (clone)
        assertSame(copy, copy.next.next.next.next.random);            // 1's random -> 7 (clone)
    }

    @Test
    void example2_twoNodes() {
        // [[1,1],[2,1]]
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        n1.next = n2;
        n1.random = n2;
        n2.random = n2;

        Node copy = sol.copyRandomList(n1);

        assertEquals(1, copy.val);
        assertEquals(2, copy.next.val);
        assertSame(copy.next, copy.random);         // 1's random -> 2 (clone)
        assertSame(copy.next, copy.next.random);    // 2's random -> 2 (clone, self-loop)
        assertNotSame(n2, copy.next);               // truly a new node
    }

    @Test
    void nullHead() {
        assertNull(sol.copyRandomList(null));
    }

    @Test
    void singleNodeNullRandom() {
        Node n = new Node(42);
        Node copy = sol.copyRandomList(n);
        assertEquals(42, copy.val);
        assertNull(copy.next);
        assertNull(copy.random);
        assertNotSame(n, copy);
    }

    @Test
    void singleNodeSelfRandom() {
        Node n = new Node(5);
        n.random = n;
        Node copy = sol.copyRandomList(n);
        assertEquals(5, copy.val);
        assertSame(copy, copy.random); // self-loop preserved in clone
        assertNotSame(n, copy);
    }
}
