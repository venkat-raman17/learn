package com.venkat.dsa.graphs;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Graph}.
 *
 * <p>Covers: undirected and directed construction, addVertex, addEdge, neighbors,
 * vertices, hasEdge, vertexCount, edgeCount, and all documented edge cases.
 */
class GraphTest {

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

    @Test
    void newGraph_isEmpty() {
        Graph g = new Graph(false);
        assertEquals(0, g.vertexCount());
        assertEquals(0, g.edgeCount());
        assertTrue(g.vertices().isEmpty());
    }

    // ------------------------------------------------------------------
    // addVertex
    // ------------------------------------------------------------------

    @Test
    void addVertex_singleVertex_increasesCount() {
        Graph g = new Graph(false);
        g.addVertex(1);
        assertEquals(1, g.vertexCount());
    }

    @Test
    void addVertex_duplicate_doesNotIncreaseCount() {
        Graph g = new Graph(false);
        g.addVertex(5);
        g.addVertex(5);
        assertEquals(1, g.vertexCount());
    }

    @Test
    void addVertex_multipleDistinct_allPresent() {
        Graph g = new Graph(true);
        g.addVertex(10);
        g.addVertex(20);
        g.addVertex(30);
        assertEquals(3, g.vertexCount());
        assertTrue(g.vertices().containsAll(Set.of(10, 20, 30)));
    }

    // ------------------------------------------------------------------
    // addEdge — undirected
    // ------------------------------------------------------------------

    @Test
    void addEdge_undirected_edgeStoredBothWays() {
        Graph g = new Graph(false);
        g.addEdge(1, 2);
        assertTrue(g.hasEdge(1, 2), "1->2 should exist");
        assertTrue(g.hasEdge(2, 1), "2->1 should exist for undirected");
    }

    @Test
    void addEdge_undirected_edgeCountIsOne() {
        Graph g = new Graph(false);
        g.addEdge(1, 2);
        assertEquals(1, g.edgeCount());
    }

    @Test
    void addEdge_undirected_implicitlyCreatesVertices() {
        Graph g = new Graph(false);
        g.addEdge(3, 7);
        assertEquals(2, g.vertexCount());
        assertTrue(g.vertices().containsAll(Set.of(3, 7)));
    }

    @Test
    void addEdge_undirected_multipleEdges_countsCorrectly() {
        Graph g = new Graph(false);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(1, 3);
        assertEquals(3, g.edgeCount());
    }

    @Test
    void addEdge_undirected_selfLoop_storedAndCounted() {
        Graph g = new Graph(false);
        g.addEdge(4, 4);
        assertTrue(g.hasEdge(4, 4));
        assertEquals(1, g.edgeCount());
    }

    // ------------------------------------------------------------------
    // addEdge — directed
    // ------------------------------------------------------------------

    @Test
    void addEdge_directed_onlyForwardDirection() {
        Graph g = new Graph(true);
        g.addEdge(1, 2);
        assertTrue(g.hasEdge(1, 2), "1->2 should exist");
        assertFalse(g.hasEdge(2, 1), "2->1 should NOT exist for directed");
    }

    @Test
    void addEdge_directed_edgeCountIsOne() {
        Graph g = new Graph(true);
        g.addEdge(1, 2);
        assertEquals(1, g.edgeCount());
    }

    @Test
    void addEdge_directed_multipleEdges_countsCorrectly() {
        Graph g = new Graph(true);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1);
        assertEquals(3, g.edgeCount());
    }

    @Test
    void addEdge_directed_implicitlyCreatesVertices() {
        Graph g = new Graph(true);
        g.addEdge(9, 11);
        assertEquals(2, g.vertexCount());
        assertTrue(g.vertices().containsAll(Set.of(9, 11)));
    }

    // ------------------------------------------------------------------
    // neighbors
    // ------------------------------------------------------------------

    @Test
    void neighbors_undirected_containsBothEndpoints() {
        Graph g = new Graph(false);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        List<Integer> n = g.neighbors(1);
        assertEquals(2, n.size());
        assertTrue(n.contains(2));
        assertTrue(n.contains(3));
    }

    @Test
    void neighbors_directed_onlyOutgoing() {
        Graph g = new Graph(true);
        g.addEdge(1, 2);
        g.addEdge(3, 1);
        List<Integer> n1 = g.neighbors(1);
        assertEquals(1, n1.size());
        assertTrue(n1.contains(2));
        assertFalse(n1.contains(3));
    }

    @Test
    void neighbors_vertexWithNoEdges_emptyList() {
        Graph g = new Graph(false);
        g.addVertex(99);
        List<Integer> n = g.neighbors(99);
        assertNotNull(n);
        assertTrue(n.isEmpty());
    }

    @Test
    void neighbors_absentVertex_throwsIllegalArgumentException() {
        Graph g = new Graph(false);
        assertThrows(IllegalArgumentException.class, () -> g.neighbors(42));
    }

    @Test
    void neighbors_returnedList_isUnmodifiable() {
        Graph g = new Graph(false);
        g.addEdge(1, 2);
        List<Integer> n = g.neighbors(1);
        assertThrows(UnsupportedOperationException.class, () -> n.add(99));
    }

    // ------------------------------------------------------------------
    // vertices
    // ------------------------------------------------------------------

    @Test
    void vertices_returnsAllAddedVertices() {
        Graph g = new Graph(false);
        g.addVertex(5);
        g.addVertex(10);
        g.addEdge(20, 30);
        Set<Integer> v = g.vertices();
        assertTrue(v.containsAll(Set.of(5, 10, 20, 30)));
        assertEquals(4, v.size());
    }

    @Test
    void vertices_returnedSet_isUnmodifiable() {
        Graph g = new Graph(false);
        g.addVertex(1);
        Set<Integer> v = g.vertices();
        assertThrows(UnsupportedOperationException.class, () -> v.add(99));
    }

    // ------------------------------------------------------------------
    // hasEdge
    // ------------------------------------------------------------------

    @Test
    void hasEdge_absentSourceVertex_returnsFalse() {
        Graph g = new Graph(false);
        assertFalse(g.hasEdge(1, 2));
    }

    @Test
    void hasEdge_sourceExistsButNoEdge_returnsFalse() {
        Graph g = new Graph(false);
        g.addVertex(1);
        g.addVertex(2);
        assertFalse(g.hasEdge(1, 2));
    }

    @Test
    void hasEdge_undirected_bothDirectionsTrue() {
        Graph g = new Graph(false);
        g.addEdge(5, 6);
        assertTrue(g.hasEdge(5, 6));
        assertTrue(g.hasEdge(6, 5));
    }

    @Test
    void hasEdge_directed_reverseDirectionFalse() {
        Graph g = new Graph(true);
        g.addEdge(5, 6);
        assertTrue(g.hasEdge(5, 6));
        assertFalse(g.hasEdge(6, 5));
    }

    // ------------------------------------------------------------------
    // vertexCount / edgeCount
    // ------------------------------------------------------------------

    @Test
    void vertexCount_reflectsImplicitAndExplicitAdditions() {
        Graph g = new Graph(true);
        g.addVertex(1);
        g.addEdge(2, 3); // implicit
        assertEquals(3, g.vertexCount());
    }

    @Test
    void edgeCount_undirected_growsWithEachAddEdge() {
        Graph g = new Graph(false);
        assertEquals(0, g.edgeCount());
        g.addEdge(1, 2);
        assertEquals(1, g.edgeCount());
        g.addEdge(1, 3);
        assertEquals(2, g.edgeCount());
        g.addEdge(2, 3);
        assertEquals(3, g.edgeCount());
    }

    @Test
    void edgeCount_directed_growsWithEachAddEdge() {
        Graph g = new Graph(true);
        g.addEdge(1, 2);
        g.addEdge(2, 1); // reverse — separate logical edge
        assertEquals(2, g.edgeCount());
    }

    // ------------------------------------------------------------------
    // Mixed / integration
    // ------------------------------------------------------------------

    @Test
    void undirected_triangle_allEdgesPresent() {
        Graph g = new Graph(false);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(1, 3);
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(2, 1));
        assertTrue(g.hasEdge(2, 3));
        assertTrue(g.hasEdge(3, 2));
        assertTrue(g.hasEdge(1, 3));
        assertTrue(g.hasEdge(3, 1));
        assertEquals(3, g.edgeCount());
        assertEquals(3, g.vertexCount());
    }

    @Test
    void directed_chain_onlyForwardEdgesExist() {
        Graph g = new Graph(true);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(2, 3));
        assertTrue(g.hasEdge(3, 4));
        assertFalse(g.hasEdge(4, 3));
        assertFalse(g.hasEdge(3, 2));
        assertFalse(g.hasEdge(2, 1));
        assertEquals(3, g.edgeCount());
        assertEquals(4, g.vertexCount());
    }
}
