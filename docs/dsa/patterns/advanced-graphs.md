# Advanced Graphs

## When to reach for it

- You see a **weighted graph** and need shortest path from one node to all others → Dijkstra.
- You need the **minimum spanning tree** (MST) to connect all nodes at minimum total cost → Prim or Kruskal.
- The graph has **edge-count constraints** ("at most K stops") → Bellman-Ford or BFS with state `(node, hops)`.
- You must **reconstruct a path** that uses every edge exactly once → Eulerian path (Hierholzer's algorithm).
- Edges appear **incrementally** and you need to track connected components cheaply → Union-Find (Kruskal fits here too).
- You need **topological order** with dependencies encoded in an unusual domain (e.g., character ordering from sorted words) → BFS topo-sort (Kahn's) with in-degree tracking.
- The "cost" dimension is non-standard (water level, time, elevation) but the goal is still "cheapest path" → treat as Dijkstra with a custom comparator.

## The idea

Advanced graph problems share a core pattern: model the problem as a weighted or constrained graph, then apply the right traversal. Dijkstra greedily expands the cheapest-known node using a min-heap; Prim does the same but tracks the cheapest edge into the growing MST; Bellman-Ford relaxes every edge V-1 times to handle arbitrary weights or hop limits; Hierholzer follows edges depth-first then backstitches the path on return.

Typical complexity:
- Dijkstra: O((V + E) log V) with a binary heap.
- Prim: O(E log V).
- Kruskal: O(E log E) dominated by sorting.
- Bellman-Ford: O(V * E).
- Hierholzer: O(E).
- Kahn's topo-sort: O(V + E).

Space is O(V + E) for the adjacency list plus O(V) for auxiliary structures (dist[], visited, Union-Find).

## Template

```java
import java.util.*;

public class AdvancedGraphs {

    // ── Dijkstra: single-source shortest path ──────────────────────────────
    // adjList[u] = list of {v, weight}
    public int[] dijkstra(int n, List<int[]>[] adjList, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // min-heap: {cost, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0], u = cur[1];
            if (cost > dist[u]) continue;          // stale entry — skip

            for (int[] edge : adjList[u]) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    // ── Prim: minimum spanning tree ────────────────────────────────────────
    public int primMST(int n, List<int[]>[] adjList) {
        boolean[] inMST = new boolean[n];
        int totalCost = 0;
        // min-heap: {weight, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int w = cur[0], u = cur[1];
            if (inMST[u]) continue;
            inMST[u] = true;
            totalCost += w;
            for (int[] edge : adjList[u]) {
                if (!inMST[edge[0]])
                    pq.offer(new int[]{edge[1], edge[0]});
            }
        }
        return totalCost;
    }

    // ── Bellman-Ford with hop limit (Cheapest Flights K Stops) ────────────
    public int bellmanFordKHops(int n, int[][] flights, int src, int dst, int k) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int i = 0; i <= k; i++) {          // exactly k+1 rounds (k stops)
            int[] tmp = dist.clone();
            for (int[] f : flights) {
                int u = f[0], v = f[1], w = f[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < tmp[v])
                    tmp[v] = dist[u] + w;
            }
            dist = tmp;                          // snapshot prevents within-round chaining
        }
        return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
    }

    // ── Hierholzer: Eulerian path (e.g., Reconstruct Itinerary) ───────────
    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> t : tickets)
            graph.computeIfAbsent(t.get(0), x -> new PriorityQueue<>()).offer(t.get(1));

        LinkedList<String> result = new LinkedList<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push("JFK");
        while (!stack.isEmpty()) {
            String top = stack.peek();
            PriorityQueue<String> neighbors = graph.get(top);
            if (neighbors != null && !neighbors.isEmpty())
                stack.push(neighbors.poll());
            else
                result.addFirst(stack.pop());    // backtrack — prepend to build path
        }
        return result;
    }

    // ── Kahn's topo-sort (Alien Dictionary) ───────────────────────────────
    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        for (String w : words)
            for (char c : w.toCharArray()) { adj.putIfAbsent(c, new HashSet<>()); inDegree.putIfAbsent(c, 0); }

        for (int i = 0; i < words.length - 1; i++) {
            String a = words[i], b = words[i + 1];
            int len = Math.min(a.length(), b.length());
            if (a.length() > b.length() && a.startsWith(b)) return ""; // invalid
            for (int j = 0; j < len; j++) {
                if (a.charAt(j) != b.charAt(j)) {
                    if (!adj.get(a.charAt(j)).contains(b.charAt(j))) {
                        adj.get(a.charAt(j)).add(b.charAt(j));
                        inDegree.merge(b.charAt(j), 1, Integer::sum);
                    }
                    break;
                }
            }
        }
        Queue<Character> q = new LinkedList<>();
        for (char c : inDegree.keySet()) if (inDegree.get(c) == 0) q.offer(c);
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll();
            sb.append(c);
            for (char nei : adj.get(c)) { inDegree.merge(nei, -1, Integer::sum); if (inDegree.get(nei) == 0) q.offer(nei); }
        }
        return sb.length() == inDegree.size() ? sb.toString() : "";
    }
}
```

## Variations

- **Dijkstra with state**: add extra dimensions to the node (e.g., `(node, stops_used)`) to track constraints; used for "K Stops" if you prefer Dijkstra over Bellman-Ford.
- **Kruskal instead of Prim**: sort all edges, then greedily add the cheapest edge that doesn't form a cycle using Union-Find. Preferred when the edge list is already given and sparse.
- **0-1 BFS**: when edge weights are only 0 or 1, replace the min-heap with a deque (push-front for 0-weight, push-back for 1-weight). O(V + E) instead of O(E log V).
- **Binary search + BFS/DFS**: for "swim in rising water" style problems, binary search on the answer and check feasibility with BFS/DFS.
- **Multi-source Dijkstra**: insert all sources into the heap at cost 0 at start; useful when the source is a set of nodes.

## Pitfalls

- **Stale heap entries**: always check `if (cost > dist[u]) continue;` at the top of the Dijkstra loop. Skipping this causes O(E) extra relaxations and wrong results if costs are mutable.
- **Bellman-Ford snapshot**: copy `dist[]` before each relaxation round. Without the snapshot, a single round can chain updates across multiple hops, violating the "at most K stops" invariant.
- **Hierholzer prepend, not append**: the path is built by prepending dead-end nodes. Appending produces the reversed or corrupted itinerary.
- **Prim on disconnected graphs**: if the graph is disconnected, Prim silently returns a partial MST. Check that `inMST` is fully true; otherwise return -1 or handle accordingly.
- **Alien Dictionary — prefix check**: if word A is a prefix of word B but appears after B in the list, the ordering is impossible. Catch this before building edges.
- **Integer overflow**: when summing weights, use `long` or guard with `dist[u] != Integer.MAX_VALUE` before adding to prevent overflow wrapping to a negative number.
- **Topo-sort cycle detection**: if `sb.length() != totalNodes` after Kahn's, a cycle exists — return `""` for alien dictionary or signal impossibility.

## Problems in this pattern

| Problem | Key Algorithm |
|---|---|
| Network Delay Time | Dijkstra — single source, find max of shortest paths |
| Min Cost to Connect All Points | Prim or Kruskal MST on implicit complete graph |
| Cheapest Flights Within K Stops | Bellman-Ford (K+1 rounds) or Dijkstra with state `(cost, node, stops)` |
| Reconstruct Itinerary | Hierholzer's Eulerian path with lexicographic min-heap |
| Swim in Rising Water | Dijkstra (treat water level as edge cost) or binary search + BFS |
| Alien Dictionary | Kahn's topo-sort — derive character order from adjacent word pairs |
