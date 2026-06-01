package com.venkat.dsa.coding.medium.solutions;

import com.venkat.dsa.coding.medium.solutions.CloneGraph.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class CloneGraphTest {

    private final CloneGraph sol = new CloneGraph();

    /** BFS-collect all (node.val -> set-of-neighbor-vals) from the cloned graph. */
    private Map<Integer, Set<Integer>> collectAdjacency(Node node) {
        if (node == null) return Collections.emptyMap();
        Map<Integer, Set<Integer>> adj = new HashMap<>();
        Queue<Node> queue = new ArrayDeque<>();
        Set<Node> visited = new HashSet<>();
        queue.offer(node);
        visited.add(node);
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            Set<Integer> neighborVals = new HashSet<>();
            for (Node nb : curr.neighbors) {
                neighborVals.add(nb.val);
                if (visited.add(nb)) queue.offer(nb);
            }
            adj.put(curr.val, neighborVals);
        }
        return adj;
    }

    @Test
    void example1_fourNodeCycle() {
        // 1--2, 2--3, 3--4, 4--1 (undirected, each node has 2 neighbors)
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        n1.neighbors.addAll(Arrays.asList(n2, n4));
        n2.neighbors.addAll(Arrays.asList(n1, n3));
        n3.neighbors.addAll(Arrays.asList(n2, n4));
        n4.neighbors.addAll(Arrays.asList(n3, n1));

        Node cloned = sol.cloneGraph(n1);

        // Verify deep copy: same structure, different object references
        assertNotSame(n1, cloned);
        assertEquals(1, cloned.val);

        Map<Integer, Set<Integer>> origAdj = collectAdjacency(n1);
        Map<Integer, Set<Integer>> cloneAdj = collectAdjacency(cloned);
        assertEquals(origAdj, cloneAdj);

        // Ensure no shared objects between original and clone
        Set<Node> origNodes = new HashSet<>();
        bfsNodes(n1, origNodes);
        Set<Node> cloneNodes = new HashSet<>();
        bfsNodes(cloned, cloneNodes);
        for (Node o : origNodes) {
            assertFalse(cloneNodes.contains(o), "Clone shares node object with original");
        }
    }

    @Test
    void example2_singleNodeNoNeighbors() {
        Node n1 = new Node(1);
        Node cloned = sol.cloneGraph(n1);
        assertNotSame(n1, cloned);
        assertEquals(1, cloned.val);
        assertTrue(cloned.neighbors.isEmpty());
    }

    @Test
    void nullInput() {
        assertNull(sol.cloneGraph(null));
    }

    @Test
    void twoNodesMutuallyConnected() {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        n1.neighbors.add(n2);
        n2.neighbors.add(n1);

        Node cloned = sol.cloneGraph(n1);
        assertNotSame(n1, cloned);
        assertEquals(1, cloned.val);
        assertEquals(1, cloned.neighbors.size());
        assertEquals(2, cloned.neighbors.get(0).val);
        // cloned n2 points back to cloned n1 (not original n1)
        assertSame(cloned, cloned.neighbors.get(0).neighbors.get(0));
    }

    private void bfsNodes(Node start, Set<Node> result) {
        if (start == null) return;
        Queue<Node> q = new ArrayDeque<>();
        q.offer(start);
        result.add(start);
        while (!q.isEmpty()) {
            Node curr = q.poll();
            for (Node nb : curr.neighbors) {
                if (result.add(nb)) q.offer(nb);
            }
        }
    }
}
