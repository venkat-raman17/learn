package com.venkat.dsa.algorithms.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link TopologicalSort}.
 *
 * <p>Correctness of a topological order is verified by checking that every directed
 * edge u → v satisfies {@code indexOf(u) < indexOf(v)} in the returned list — the
 * only invariant a topological sort must satisfy.
 */
class TopologicalSortTest {

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    /**
     * Asserts that {@code order} is a valid topological ordering for the given edges
     * and contains exactly {@code n} distinct vertices in [0, n).
     */
    private static void assertValidTopologicalOrder(int n, List<int[]> edges, List<Integer> order) {
        assertNotNull(order, "Result must not be null");
        assertEquals(n, order.size(), "Result must contain exactly n vertices");

        // Build position map
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            assertTrue(v >= 0 && v < n, "Vertex out of range: " + v);
            assertFalse(pos.containsKey(v), "Duplicate vertex in result: " + v);
            pos.put(v, i);
        }

        // Every edge u -> v must have pos(u) < pos(v)
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            assertTrue(pos.get(u) < pos.get(v),
                    "Order violated for edge " + u + " -> " + v
                            + ": pos(" + u + ")=" + pos.get(u)
                            + " pos(" + v + ")=" + pos.get(v));
        }
    }

    // -------------------------------------------------------------------------
    // kahn — valid DAGs
    // -------------------------------------------------------------------------

    @Test
    void kahnOnEmptyGraph_returnsEmptyList() {
        List<Integer> result = TopologicalSort.kahn(0, List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void kahnSingleVertex_noEdges_returnsSingleVertex() {
        List<Integer> result = TopologicalSort.kahn(1, List.of());
        assertEquals(List.of(0), result);
    }

    @Test
    void kahnLinearChain_respectsOrder() {
        // 0 -> 1 -> 2 -> 3
        List<int[]> edges = List.of(
                new int[]{0, 1},
                new int[]{1, 2},
                new int[]{2, 3}
        );
        List<Integer> result = TopologicalSort.kahn(4, edges);
        assertValidTopologicalOrder(4, edges, result);
        // For a strict chain there is exactly one valid order
        assertEquals(List.of(0, 1, 2, 3), result);
    }

    @Test
    void kahnDiamondDag_respectsAllEdges() {
        //     0
        //    / \
        //   1   2
        //    \ /
        //     3
        List<int[]> edges = List.of(
                new int[]{0, 1},
                new int[]{0, 2},
                new int[]{1, 3},
                new int[]{2, 3}
        );
        List<Integer> result = TopologicalSort.kahn(4, edges);
        assertValidTopologicalOrder(4, edges, result);
    }

    @Test
    void kahnMultipleRoots_allVerticesPresent() {
        // 0 -> 2, 1 -> 2, 2 -> 3
        List<int[]> edges = List.of(
                new int[]{0, 2},
                new int[]{1, 2},
                new int[]{2, 3}
        );
        List<Integer> result = TopologicalSort.kahn(4, edges);
        assertValidTopologicalOrder(4, edges, result);
    }

    @Test
    void kahnIsolatedVertices_allAppearInResult() {
        // 5 vertices, only edge 0 -> 1; vertices 2,3,4 are isolated
        List<int[]> edges = List.of(new int[]{0, 1});
        List<Integer> result = TopologicalSort.kahn(5, edges);
        assertValidTopologicalOrder(5, edges, result);
    }

    @Test
    void kahnLargerDag_respectsAllEdges() {
        // Build a DAG:  5->0, 5->2, 4->0, 4->1, 2->3, 3->1
        int n = 6;
        List<int[]> edges = List.of(
                new int[]{5, 0},
                new int[]{5, 2},
                new int[]{4, 0},
                new int[]{4, 1},
                new int[]{2, 3},
                new int[]{3, 1}
        );
        List<Integer> result = TopologicalSort.kahn(n, edges);
        assertValidTopologicalOrder(n, edges, result);
    }

    @Test
    void kahnNoEdges_allVerticesPresentInAnyOrder() {
        int n = 5;
        List<Integer> result = TopologicalSort.kahn(n, List.of());
        assertEquals(n, result.size());
        // All vertices 0..n-1 must appear
        for (int i = 0; i < n; i++) {
            assertTrue(result.contains(i), "Missing vertex: " + i);
        }
    }

    @Test
    void kahnResultIsImmutable() {
        List<Integer> result = TopologicalSort.kahn(3, List.of(new int[]{0, 1}, new int[]{1, 2}));
        assertThrows(UnsupportedOperationException.class, () -> result.add(99));
    }

    // -------------------------------------------------------------------------
    // kahn — cycle detection
    // -------------------------------------------------------------------------

    @Test
    void kahnSelfLoop_throwsIllegalArgumentException() {
        // Self-loop: 0 -> 0
        List<int[]> edges = List.of(new int[]{0, 0});
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(1, edges));
    }

    @Test
    void kahnTwoNodeCycle_throwsIllegalArgumentException() {
        // 0 -> 1 -> 0
        List<int[]> edges = List.of(new int[]{0, 1}, new int[]{1, 0});
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(2, edges));
    }

    @Test
    void kahnThreeNodeCycle_throwsIllegalArgumentException() {
        // 0 -> 1 -> 2 -> 0
        List<int[]> edges = List.of(
                new int[]{0, 1},
                new int[]{1, 2},
                new int[]{2, 0}
        );
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(3, edges));
    }

    @Test
    void kahnPartialCycle_throwsIllegalArgumentException() {
        // Nodes 0,1 are a valid chain; nodes 2,3,4 form a cycle
        // 0->1, 2->3->4->2
        List<int[]> edges = List.of(
                new int[]{0, 1},
                new int[]{2, 3},
                new int[]{3, 4},
                new int[]{4, 2}
        );
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(5, edges));
    }

    // -------------------------------------------------------------------------
    // kahn — invalid arguments
    // -------------------------------------------------------------------------

    @Test
    void kahnNegativeN_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(-1, List.of()));
    }

    @Test
    void kahnNullEdgeList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(3, null));
    }

    @Test
    void kahnEdgeEndpointOutOfRange_throwsIllegalArgumentException() {
        // n=3, edge references vertex 5
        List<int[]> edges = List.of(new int[]{0, 5});
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(3, edges));
    }

    @Test
    void kahnNegativeEdgeEndpoint_throwsIllegalArgumentException() {
        List<int[]> edges = List.of(new int[]{-1, 1});
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(3, edges));
    }

    @Test
    void kahnNullEdgeEntry_throwsIllegalArgumentException() {
        List<int[]> edges = new ArrayList<>();
        edges.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(2, edges));
    }

    @Test
    void kahnEdgeTooShort_throwsIllegalArgumentException() {
        List<int[]> edges = List.of(new int[]{0});   // needs [u, v]
        assertThrows(IllegalArgumentException.class,
                () -> TopologicalSort.kahn(2, edges));
    }
}
