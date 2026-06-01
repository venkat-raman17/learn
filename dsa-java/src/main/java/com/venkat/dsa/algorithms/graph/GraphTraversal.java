package com.venkat.dsa.algorithms.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GraphTraversal — static BFS and DFS traversal algorithms over an adjacency-list graph.
 *
 * <p><b>Backing representation:</b> The graph is passed in as a
 * {@code Map<Integer, List<Integer>>} (adjacency list). Each key is a vertex; its value
 * is the ordered list of neighbours. This class owns no state itself.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>The adjacency map must not be {@code null}; individual neighbour lists must not be
 *       {@code null} (missing keys are treated as isolated vertices with no outgoing edges).</li>
 *   <li>Traversal starts at {@code start}; if {@code start} is absent from the map keys the
 *       returned list contains only {@code start} (it is still visited once).</li>
 *   <li>Only vertices reachable from {@code start} are included in the result.</li>
 *   <li>Deterministic: ties are broken by neighbour insertion order in the list.</li>
 * </ul>
 *
 * <p><b>Operations table:</b>
 * <pre>
 * ┌─────────────────────────┬───────────┬──────────┐
 * │ Operation               │ Time      │ Space    │
 * ├─────────────────────────┼───────────┼──────────┤
 * │ bfs(adj, start)         │ O(V + E)  │ O(V)     │
 * │ dfsIterative(adj,start) │ O(V + E)  │ O(V)     │
 * │ dfsRecursive(adj,start) │ O(V + E)  │ O(V)     │
 * └─────────────────────────┴───────────┴──────────┘
 * V = number of vertices reachable from start; E = number of edges among them.
 * </pre>
 *
 * <p><b>When to use:</b>
 * <ul>
 *   <li>BFS — shortest hop-count path, level-order processing, cycle detection in undirected
 *       graphs.</li>
 *   <li>DFS (iterative or recursive) — topological sort, cycle detection in directed graphs,
 *       connected-components, maze solving.</li>
 * </ul>
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>BFS uses a queue (FIFO); DFS uses a stack (LIFO).</li>
 *   <li>Iterative DFS pushes neighbours in <em>reverse</em> order so that the first neighbour
 *       is processed first, matching the recursive call order and producing the same visit
 *       sequence as recursive DFS.</li>
 *   <li>Recursive DFS may hit {@link StackOverflowError} on very deep graphs; prefer iterative
 *       for production code.</li>
 *   <li>A {@link LinkedHashSet} for {@code visited} gives O(1) membership tests while
 *       preserving insertion order (useful for debugging).</li>
 * </ul>
 *
 * @since Java 21
 */
public final class GraphTraversal {

    /** Utility class — no instances. */
    private GraphTraversal() {}

    /**
     * Breadth-first search starting from {@code start}.
     *
     * <p>Visits vertices level by level (closest neighbours first). Uses a FIFO queue
     * internally.
     *
     * <p><b>Time:</b> O(V + E) &nbsp; <b>Space:</b> O(V)
     *
     * @param adj   adjacency list; must not be {@code null}
     * @param start source vertex
     * @return list of visited vertices in BFS order (only vertices reachable from {@code start})
     * @throws IllegalArgumentException if {@code adj} is {@code null}
     */
    public static List<Integer> bfs(Map<Integer, List<Integer>> adj, int start) {
        if (adj == null) {
            throw new IllegalArgumentException("adj must not be null");
        }
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();

        visited.add(start);
        queue.addLast(start);

        while (!queue.isEmpty()) {
            int current = queue.removeFirst();
            order.add(current);

            List<Integer> neighbours = adj.getOrDefault(current, List.of());
            for (int neighbour : neighbours) {
                if (visited.add(neighbour)) {
                    queue.addLast(neighbour);
                }
            }
        }
        return order;
    }

    /**
     * Iterative depth-first search starting from {@code start}.
     *
     * <p>Uses an explicit stack. Neighbours are pushed in reverse insertion order so that
     * the first neighbour in the adjacency list is explored first, matching the visit order
     * of {@link #dfsRecursive}.
     *
     * <p><b>Time:</b> O(V + E) &nbsp; <b>Space:</b> O(V)
     *
     * @param adj   adjacency list; must not be {@code null}
     * @param start source vertex
     * @return list of visited vertices in DFS order (only vertices reachable from {@code start})
     * @throws IllegalArgumentException if {@code adj} is {@code null}
     */
    public static List<Integer> dfsIterative(Map<Integer, List<Integer>> adj, int start) {
        if (adj == null) {
            throw new IllegalArgumentException("adj must not be null");
        }
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (!visited.add(current)) {
                continue;
            }
            order.add(current);

            List<Integer> neighbours = adj.getOrDefault(current, List.of());
            // Push in reverse so that the first neighbour is on top of the stack.
            for (int i = neighbours.size() - 1; i >= 0; i--) {
                int neighbour = neighbours.get(i);
                if (!visited.contains(neighbour)) {
                    stack.push(neighbour);
                }
            }
        }
        return order;
    }

    /**
     * Recursive depth-first search starting from {@code start}.
     *
     * <p>Delegates to a private helper that carries the {@code visited} set and the
     * accumulating result list.
     *
     * <p><b>Time:</b> O(V + E) &nbsp; <b>Space:</b> O(V) (call-stack depth proportional to
     * longest path; iterative DFS is preferred for very deep graphs)
     *
     * @param adj   adjacency list; must not be {@code null}
     * @param start source vertex
     * @return list of visited vertices in DFS order (only vertices reachable from {@code start})
     * @throws IllegalArgumentException if {@code adj} is {@code null}
     */
    public static List<Integer> dfsRecursive(Map<Integer, List<Integer>> adj, int start) {
        if (adj == null) {
            throw new IllegalArgumentException("adj must not be null");
        }
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        dfsHelper(adj, start, visited, order);
        return order;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static void dfsHelper(
            Map<Integer, List<Integer>> adj,
            int vertex,
            Set<Integer> visited,
            List<Integer> order) {
        if (!visited.add(vertex)) {
            return;
        }
        order.add(vertex);
        List<Integer> neighbours = adj.getOrDefault(vertex, List.of());
        for (int neighbour : neighbours) {
            if (!visited.contains(neighbour)) {
                dfsHelper(adj, neighbour, visited, order);
            }
        }
    }
}
