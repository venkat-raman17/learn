package com.venkat.dsa.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph — a collection of vertices connected by edges, represented as an adjacency list.
 *
 * <p><b>Backing representation:</b> {@code Map<Integer, List<Integer>>} where each key is a
 * vertex and its value is the list of adjacent vertices (neighbours).
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>Every vertex that has ever been added via {@code addVertex} or implicitly via
 *       {@code addEdge} is present as a key in the adjacency map.</li>
 *   <li>For an undirected graph, if {@code v} appears in {@code adj(u)} then {@code u} appears
 *       in {@code adj(v)}.</li>
 *   <li>Self-loops are permitted; parallel (duplicate) edges are permitted.</li>
 * </ul>
 *
 * <p><b>Operations — time complexity (V = vertices, E = edges, deg = degree of queried vertex):</b>
 * <pre>
 * Operation      Time      Space
 * -----------    ------    ------
 * addVertex      O(1)      O(1)
 * addEdge        O(1)      O(1)
 * neighbors      O(1)*     O(deg)  (* map lookup; returns live list view)
 * vertices       O(1)      O(V)
 * hasEdge        O(deg)    O(1)
 * vertexCount    O(1)      O(1)
 * edgeCount      O(1)      O(1)
 * </pre>
 *
 * <p><b>When to use:</b> Sparse graphs where the edge-to-vertex ratio is low. Adjacency lists
 * use O(V + E) space, compared with O(V²) for a matrix, making them practical for most
 * real-world graphs (social networks, road maps, dependency trees).
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>BFS / DFS both run in O(V + E) on an adjacency-list graph.</li>
 *   <li>Directed vs undirected: undirected edges are stored twice (once per endpoint).</li>
 *   <li>{@code edgeCount} for an undirected graph counts each logical edge once.</li>
 *   <li>Common follow-ups: cycle detection, topological sort, connected components,
 *       shortest path (Dijkstra / BFS).</li>
 * </ul>
 */
public class Graph {

    private final Map<Integer, List<Integer>> adjacency;
    private final boolean directed;
    private int edgeCount;

    /**
     * Creates an empty graph.
     *
     * @param directed {@code true} for a directed graph (digraph); {@code false} for undirected.
     * <b>O(1)</b>
     */
    public Graph(boolean directed) {
        this.adjacency = new HashMap<>();
        this.directed  = directed;
        this.edgeCount = 0;
    }

    /**
     * Adds a vertex to the graph if it is not already present.
     *
     * <p><b>O(1)</b>
     *
     * @param v the vertex identifier
     */
    public void addVertex(int v) {
        adjacency.putIfAbsent(v, new ArrayList<>());
    }

    /**
     * Adds an edge from vertex {@code u} to vertex {@code v}, implicitly creating either vertex
     * if it does not already exist.  For an undirected graph the reverse edge is also added.
     *
     * <p><b>O(1)</b>
     *
     * @param u source vertex
     * @param v destination vertex
     */
    public void addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        adjacency.get(u).add(v);
        if (!directed) {
            adjacency.get(v).add(u);
        }
        edgeCount++;
    }

    /**
     * Returns an unmodifiable view of the neighbours of vertex {@code v}.
     *
     * <p><b>O(1)</b> for the lookup; iterating the returned list is O(deg(v)).
     *
     * @param v the vertex to query
     * @return list of adjacent vertices
     * @throws IllegalArgumentException if {@code v} is not present in the graph
     */
    public List<Integer> neighbors(int v) {
        if (!adjacency.containsKey(v)) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist.");
        }
        return Collections.unmodifiableList(adjacency.get(v));
    }

    /**
     * Returns an unmodifiable set of all vertices in the graph.
     *
     * <p><b>O(1)</b>
     *
     * @return the set of vertex identifiers
     */
    public Set<Integer> vertices() {
        return Collections.unmodifiableSet(adjacency.keySet());
    }

    /**
     * Returns {@code true} if an edge from {@code u} to {@code v} exists.
     *
     * <p><b>O(deg(u))</b>
     *
     * @param u source vertex
     * @param v destination vertex
     * @return {@code true} if the edge exists; {@code false} otherwise (including when either
     *         vertex is absent)
     */
    public boolean hasEdge(int u, int v) {
        List<Integer> neighbours = adjacency.get(u);
        if (neighbours == null) {
            return false;
        }
        return neighbours.contains(v);
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * <p><b>O(1)</b>
     *
     * @return vertex count
     */
    public int vertexCount() {
        return adjacency.size();
    }

    /**
     * Returns the number of logical edges in the graph.  For an undirected graph each edge is
     * counted once even though it is stored in two adjacency lists.
     *
     * <p><b>O(1)</b>
     *
     * @return edge count
     */
    public int edgeCount() {
        return edgeCount;
    }
}
