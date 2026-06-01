package com.venkat.dsa.algorithms.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link MinimumSpanningTree} – Kruskal's and Prim's algorithms.
 *
 * <p>Graph used for the main "known weight" tests (n=5, 6 edges):</p>
 * <pre>
 *   0 --4-- 1
 *   |  \    |
 *   2   8   7
 *   |    \  |
 *   2 --9-- 3
 *    \     /
 *     3--10
 *       4
 * </pre>
 * Concrete edge list:
 *   {0,1,4}, {0,2,2}, {1,3,7}, {2,3,9}, {2,4,3}, {3,4,10}
 *
 * MST edges (by Kruskal trace):
 *   {0,2,2}  – add (cost 2)
 *   {2,4,3}  – add (cost 3)
 *   {0,1,4}  – add (cost 4)
 *   {1,3,7}  – add (cost 7)
 *   Total = 16
 */
class MinimumSpanningTreeTest {

    // -----------------------------------------------------------------------
    // Known-graph: both methods must agree and match expected total weight 16
    // -----------------------------------------------------------------------

    private static final int N = 5;
    private static final int[][] EDGES = {
        {0, 1, 4},
        {0, 2, 2},
        {1, 3, 7},
        {2, 3, 9},
        {2, 4, 3},
        {3, 4, 10}
    };

    @Test
    void kruskal_knownGraph_returnsCorrectTotalWeight() {
        assertEquals(16L, MinimumSpanningTree.kruskal(N, EDGES));
    }

    @Test
    void prim_knownGraph_returnsCorrectTotalWeight() {
        assertEquals(16L, MinimumSpanningTree.prim(N, EDGES));
    }

    @Test
    void kruskalAndPrim_agreeOnSameGraph() {
        long k = MinimumSpanningTree.kruskal(N, EDGES);
        long p = MinimumSpanningTree.prim(N, EDGES);
        assertEquals(k, p, "Kruskal and Prim must return the same MST weight");
    }

    // -----------------------------------------------------------------------
    // Minimal / trivial graphs
    // -----------------------------------------------------------------------

    @Test
    void kruskal_singleVertex_returnsZero() {
        assertEquals(0L, MinimumSpanningTree.kruskal(1, new int[0][0]));
    }

    @Test
    void prim_singleVertex_returnsZero() {
        assertEquals(0L, MinimumSpanningTree.prim(1, new int[0][0]));
    }

    @Test
    void kruskal_twoVerticesOneEdge_returnsThatWeight() {
        int[][] e = {{0, 1, 5}};
        assertEquals(5L, MinimumSpanningTree.kruskal(2, e));
    }

    @Test
    void prim_twoVerticesOneEdge_returnsThatWeight() {
        int[][] e = {{0, 1, 5}};
        assertEquals(5L, MinimumSpanningTree.prim(2, e));
    }

    @Test
    void kruskal_noEdges_returnsZero() {
        assertEquals(0L, MinimumSpanningTree.kruskal(4, new int[0][0]));
    }

    @Test
    void prim_noEdges_returnsZero() {
        assertEquals(0L, MinimumSpanningTree.prim(4, new int[0][0]));
    }

    // -----------------------------------------------------------------------
    // Self-loops are ignored
    // -----------------------------------------------------------------------

    @Test
    void kruskal_selfLoopIgnored_mstCorrect() {
        // Self-loop on vertex 0 must not be counted; only {0,1,3} matters
        int[][] e = {{0, 0, 1}, {0, 1, 3}};
        assertEquals(3L, MinimumSpanningTree.kruskal(2, e));
    }

    @Test
    void prim_selfLoopIgnored_mstCorrect() {
        int[][] e = {{0, 0, 1}, {0, 1, 3}};
        assertEquals(3L, MinimumSpanningTree.prim(2, e));
    }

    // -----------------------------------------------------------------------
    // Parallel edges – algorithm must pick the cheaper one
    // -----------------------------------------------------------------------

    @Test
    void kruskal_parallelEdges_picksCheaperEdge() {
        // Two edges between 0 and 1; MST should use weight 2
        int[][] e = {{0, 1, 10}, {0, 1, 2}};
        assertEquals(2L, MinimumSpanningTree.kruskal(2, e));
    }

    @Test
    void prim_parallelEdges_picksCheaperEdge() {
        int[][] e = {{0, 1, 10}, {0, 1, 2}};
        assertEquals(2L, MinimumSpanningTree.prim(2, e));
    }

    // -----------------------------------------------------------------------
    // Already-connected (complete) small graph
    // -----------------------------------------------------------------------

    @Test
    void kruskal_completeTriangle_picksTwoLowestEdges() {
        // Triangle: 0-1(1), 1-2(2), 0-2(5)  -> MST picks {0,1,1} and {1,2,2} = 3
        int[][] e = {{0, 1, 1}, {1, 2, 2}, {0, 2, 5}};
        assertEquals(3L, MinimumSpanningTree.kruskal(3, e));
    }

    @Test
    void prim_completeTriangle_picksTwoLowestEdges() {
        int[][] e = {{0, 1, 1}, {1, 2, 2}, {0, 2, 5}};
        assertEquals(3L, MinimumSpanningTree.prim(3, e));
    }

    // -----------------------------------------------------------------------
    // Zero-weight edges
    // -----------------------------------------------------------------------

    @Test
    void kruskal_zeroWeightEdges_handled() {
        int[][] e = {{0, 1, 0}, {1, 2, 0}, {0, 2, 5}};
        assertEquals(0L, MinimumSpanningTree.kruskal(3, e));
    }

    @Test
    void prim_zeroWeightEdges_handled() {
        int[][] e = {{0, 1, 0}, {1, 2, 0}, {0, 2, 5}};
        assertEquals(0L, MinimumSpanningTree.prim(3, e));
    }

    // -----------------------------------------------------------------------
    // Larger deterministic graph – both algorithms agree
    // -----------------------------------------------------------------------

    @Test
    void kruskalAndPrim_agreeOnLargerGraph() {
        // n=6, known MST weight = 1+2+3+4+5 = 15 (chain 0-1-2-3-4-5 with costs 1,2,3,4,5)
        // plus several extra heavier edges that should NOT be in MST
        int n = 6;
        int[][] e = {
            {0, 1, 1}, {1, 2, 2}, {2, 3, 3}, {3, 4, 4}, {4, 5, 5},
            {0, 5, 100}, {1, 4, 50}, {2, 5, 40}, {0, 3, 20}
        };
        long k = MinimumSpanningTree.kruskal(n, e);
        long p = MinimumSpanningTree.prim(n, e);
        assertEquals(15L, k);
        assertEquals(15L, p);
    }

    // -----------------------------------------------------------------------
    // Error conditions
    // -----------------------------------------------------------------------

    @Test
    void kruskal_negativeN_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.kruskal(-1, new int[0][0]));
    }

    @Test
    void prim_negativeN_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.prim(-1, new int[0][0]));
    }

    @Test
    void kruskal_nullEdges_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.kruskal(3, null));
    }

    @Test
    void prim_nullEdges_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.prim(3, null));
    }

    @Test
    void kruskal_edgeVertexOutOfRange_throwsIllegalArgument() {
        int[][] e = {{0, 5, 1}}; // vertex 5 is out of range for n=3
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.kruskal(3, e));
    }

    @Test
    void prim_edgeVertexOutOfRange_throwsIllegalArgument() {
        int[][] e = {{0, 5, 1}};
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.prim(3, e));
    }

    @Test
    void kruskal_malformedEdge_throwsIllegalArgument() {
        // Edge with only 2 elements instead of 3
        int[][] e = {{0, 1}};
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.kruskal(3, e));
    }

    @Test
    void prim_malformedEdge_throwsIllegalArgument() {
        int[][] e = {{0, 1}};
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.prim(3, e));
    }

    @Test
    void kruskal_negativeVertexInEdge_throwsIllegalArgument() {
        int[][] e = {{-1, 1, 5}};
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.kruskal(3, e));
    }

    @Test
    void prim_negativeVertexInEdge_throwsIllegalArgument() {
        int[][] e = {{-1, 1, 5}};
        assertThrows(IllegalArgumentException.class,
                () -> MinimumSpanningTree.prim(3, e));
    }
}
