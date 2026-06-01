package com.venkat.dsa.algorithms.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Dijkstra#shortestPath(int, int[][], int)}.
 *
 * <p>Each test method is named after the behaviour it verifies.
 * All expected values have been derived by hand-tracing the algorithm.
 */
class DijkstraTest {

    // -----------------------------------------------------------------------
    // Helper constant
    // -----------------------------------------------------------------------
    private static final int INF = Integer.MAX_VALUE;

    // -----------------------------------------------------------------------
    // Basic correctness — known weighted directed graph
    //
    //   0 --1--> 1 --2--> 3
    //   |              ^
    //   4              |
    //   v              1
    //   2 ----3------> 3 (duplicate edge, longer path)
    //
    // Vertices: 0,1,2,3
    // Edges: 0->1 w=1, 0->2 w=4, 1->3 w=2, 2->3 w=1, 1->2 w=1
    //
    // From source=0:
    //   dist[0] = 0
    //   dist[1] = 1  (0->1)
    //   dist[2] = 2  (0->1->2)
    //   dist[3] = 3  (0->1->3)  or (0->1->2->3 = 1+1+1 = 3) — tie
    // -----------------------------------------------------------------------
    @Test
    void knownGraph_sourceZero_correctDistances() {
        int[][] edges = {
            {0, 1, 1},
            {0, 2, 4},
            {1, 3, 2},
            {2, 3, 1},
            {1, 2, 1}
        };
        int[] result = Dijkstra.shortestPath(4, edges, 0);

        assertEquals(0, result[0], "distance to source must be 0");
        assertEquals(1, result[1], "0->1 direct, weight 1");
        assertEquals(2, result[2], "0->1->2, weight 1+1=2");
        assertEquals(3, result[3], "0->1->3 or 0->1->2->3, weight 3");
    }

    @Test
    void knownGraph_sourceOne_correctDistances() {
        // Same graph, start from vertex 1
        // From 1: dist[1]=0, dist[2]=1, dist[3]=2, dist[0]=INF (no back edge)
        int[][] edges = {
            {0, 1, 1},
            {0, 2, 4},
            {1, 3, 2},
            {2, 3, 1},
            {1, 2, 1}
        };
        int[] result = Dijkstra.shortestPath(4, edges, 1);

        assertEquals(INF, result[0], "vertex 0 unreachable from 1");
        assertEquals(0,   result[1], "distance to source must be 0");
        assertEquals(1,   result[2], "1->2, weight 1");
        assertEquals(2,   result[3], "1->2->3, weight 1+1=2 (cheaper than 1->3 weight 2, tie)");
    }

    // -----------------------------------------------------------------------
    // Longer weighted graph: linear chain 0->1->2->3->4 with varying weights
    //
    // Edges: 0->1 w=2, 1->2 w=3, 2->3 w=1, 3->4 w=4
    // From 0: [0, 2, 5, 6, 10]
    // -----------------------------------------------------------------------
    @Test
    void linearChain_correctCumulativeDistances() {
        int[][] edges = {
            {0, 1, 2},
            {1, 2, 3},
            {2, 3, 1},
            {3, 4, 4}
        };
        int[] result = Dijkstra.shortestPath(5, edges, 0);

        assertArrayEquals(new int[]{0, 2, 5, 6, 10}, result);
    }

    // -----------------------------------------------------------------------
    // Unreachable node: vertex is present in the graph but has no incoming edges
    // from the source side.
    //
    // Graph: 0->1 w=1, 2->3 w=1  (two disconnected components)
    // From 0: dist[0]=0, dist[1]=1, dist[2]=INF, dist[3]=INF
    // -----------------------------------------------------------------------
    @Test
    void disconnectedGraph_unreachableNodesAreMaxValue() {
        int[][] edges = {
            {0, 1, 1},
            {2, 3, 1}
        };
        int[] result = Dijkstra.shortestPath(4, edges, 0);

        assertEquals(0,   result[0]);
        assertEquals(1,   result[1]);
        assertEquals(INF, result[2], "vertex 2 unreachable from 0");
        assertEquals(INF, result[3], "vertex 3 unreachable from 0");
    }

    // -----------------------------------------------------------------------
    // Single node — no edges
    // -----------------------------------------------------------------------
    @Test
    void singleNode_noEdges_distanceIsZero() {
        int[] result = Dijkstra.shortestPath(1, new int[0][0], 0);

        assertEquals(1, result.length);
        assertEquals(0, result[0], "distance from source to itself is always 0");
    }

    @Test
    void singleNode_nullEdges_distanceIsZero() {
        int[] result = Dijkstra.shortestPath(1, null, 0);

        assertEquals(1, result.length);
        assertEquals(0, result[0]);
    }

    // -----------------------------------------------------------------------
    // Source distance is always 0, regardless of incoming edges to source
    // -----------------------------------------------------------------------
    @Test
    void sourceDistanceIsAlwaysZero() {
        // Even with an edge pointing into the source, dist[source] stays 0
        int[][] edges = {
            {1, 0, 100},  // edge INTO source — should not affect dist[0]
            {0, 1, 1}
        };
        int[] result = Dijkstra.shortestPath(2, edges, 0);

        assertEquals(0, result[0], "source must always be distance 0");
        assertEquals(1, result[1]);
    }

    // -----------------------------------------------------------------------
    // Graph with a shorter path that must override an initially discovered longer path
    //
    // 0->2 w=10, 0->1 w=1, 1->2 w=2  => shortest to 2 is 3 (via 1), not 10
    // -----------------------------------------------------------------------
    @Test
    void relaxation_shorterPathOverridesLongerPath() {
        int[][] edges = {
            {0, 2, 10},
            {0, 1, 1},
            {1, 2, 2}
        };
        int[] result = Dijkstra.shortestPath(3, edges, 0);

        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
        assertEquals(3, result[2], "0->1->2 (cost 3) beats 0->2 (cost 10)");
    }

    // -----------------------------------------------------------------------
    // Parallel edges (multiple edges between same pair of vertices)
    // The algorithm must pick the minimum-weight one.
    // 0->1 w=5, 0->1 w=2, 0->1 w=8  => dist[1] = 2
    // -----------------------------------------------------------------------
    @Test
    void parallelEdges_minimumWeightChosen() {
        int[][] edges = {
            {0, 1, 5},
            {0, 1, 2},
            {0, 1, 8}
        };
        int[] result = Dijkstra.shortestPath(2, edges, 0);

        assertEquals(0, result[0]);
        assertEquals(2, result[1], "cheapest of three parallel edges must win");
    }

    // -----------------------------------------------------------------------
    // Zero-weight edges — a reachable node via w=0 edge must have dist=0
    // -----------------------------------------------------------------------
    @Test
    void zeroWeightEdge_reachableWithDistanceZero() {
        int[][] edges = {
            {0, 1, 0},
            {1, 2, 5}
        };
        int[] result = Dijkstra.shortestPath(3, edges, 0);

        assertEquals(0, result[0]);
        assertEquals(0, result[1], "edge weight 0 means vertex 1 is at distance 0");
        assertEquals(5, result[2]);
    }

    // -----------------------------------------------------------------------
    // Graph where source has no outgoing edges — all other nodes unreachable
    // -----------------------------------------------------------------------
    @Test
    void sourceWithNoOutgoingEdges_allOthersUnreachable() {
        // Only edge is from vertex 1 to vertex 2; source 0 has no edges out
        int[][] edges = {
            {1, 2, 3}
        };
        int[] result = Dijkstra.shortestPath(3, edges, 0);

        assertEquals(0,   result[0]);
        assertEquals(INF, result[1]);
        assertEquals(INF, result[2]);
    }

    // -----------------------------------------------------------------------
    // Fully connected graph (all pairs) — verify distances are correct
    //
    // 3 vertices, all-to-all:
    //   0->1 w=1, 0->2 w=4
    //   1->0 w=1, 1->2 w=2
    //   2->0 w=4, 2->1 w=2
    //
    // From 0: dist[0]=0, dist[1]=1, dist[2]=3 (0->1->2)
    // -----------------------------------------------------------------------
    @Test
    void fullyConnectedGraph_correctDistances() {
        int[][] edges = {
            {0, 1, 1}, {0, 2, 4},
            {1, 0, 1}, {1, 2, 2},
            {2, 0, 4}, {2, 1, 2}
        };
        int[] result = Dijkstra.shortestPath(3, edges, 0);

        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
        assertEquals(3, result[2], "0->1->2 (1+2=3) beats 0->2 (4)");
    }

    // -----------------------------------------------------------------------
    // Large-ish straight-line graph to confirm no integer overflow in relaxation
    // 0->1 w=MAX/2, 1->2 w=MAX/2 — these two added would overflow int
    // The impl uses (long) cast so dist[2] should still be correct
    // -----------------------------------------------------------------------
    @Test
    void largeWeights_noIntegerOverflow() {
        // dist[0]=0, dist[1]=MAX/2, dist[2]=MAX/2+(MAX/2)=MAX-1 (fits in int)
        int half = Integer.MAX_VALUE / 2; // 1_073_741_823
        int[][] edges = {
            {0, 1, half},
            {1, 2, half}
        };
        int[] result = Dijkstra.shortestPath(3, edges, 0);

        assertEquals(0,    result[0]);
        assertEquals(half, result[1]);
        assertEquals((long) half + half, (long) result[2],
                "sum of two MAX/2 weights must not overflow");
    }

    // -----------------------------------------------------------------------
    // Error conditions
    // -----------------------------------------------------------------------

    @Test
    void invalidN_zeroVertices_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(0, new int[0][0], 0));
    }

    @Test
    void invalidN_negativeVertices_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(-1, new int[0][0], 0));
    }

    @Test
    void invalidSource_tooLarge_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(3, new int[0][0], 3));
    }

    @Test
    void invalidSource_negative_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(3, new int[0][0], -1));
    }

    @Test
    void edgeWithNegativeWeight_throwsIllegalArgumentException() {
        int[][] edges = {{0, 1, -5}};
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(2, edges, 0));
    }

    @Test
    void edgeVertexOutOfRange_throwsIllegalArgumentException() {
        int[][] edges = {{0, 5, 1}};  // vertex 5 does not exist in n=3 graph
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(3, edges, 0));
    }

    @Test
    void edgeArrayWrongLength_throwsIllegalArgumentException() {
        int[][] edges = {{0, 1}};  // missing weight
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(2, edges, 0));
    }

    @Test
    void nullEdgeEntry_throwsIllegalArgumentException() {
        int[][] edges = {null};
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.shortestPath(2, edges, 0));
    }
}
