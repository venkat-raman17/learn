package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.CloneGraph.Node;

@Disabled("practice — delete when you start")
public class CloneGraphTest {

    private final CloneGraph solution = new CloneGraph();

    @Test
    public void testNullGraph() {
        assertNull(solution.cloneGraph(null));
    }

    @Test
    public void testSingleNode() {
        Node node = new Node(1);
        Node cloned = solution.cloneGraph(node);
        assertNotNull(cloned);
        assertNotSame(node, cloned);
        assertEquals(1, cloned.val);
        assertTrue(cloned.neighbors.isEmpty());
    }

    @Test
    public void testTwoConnectedNodes() {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        n1.neighbors.add(n2);
        n2.neighbors.add(n1);

        Node cloned = solution.cloneGraph(n1);
        assertNotNull(cloned);
        assertNotSame(n1, cloned);
        assertEquals(1, cloned.val);
        assertEquals(1, cloned.neighbors.size());
        assertEquals(2, cloned.neighbors.get(0).val);
        assertNotSame(n2, cloned.neighbors.get(0));
    }
}
