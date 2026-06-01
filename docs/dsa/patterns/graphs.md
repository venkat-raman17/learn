# Graphs

## When to reach for it

- The problem says "grid", "island", "region", "connected", or "path" and cells influence each other.
- You see an explicit edge list, adjacency list, or adjacency matrix.
- The problem asks for reachability, number of components, or cycle detection.
- Dependency ordering appears: "finish course A before B" — think directed graph + topological sort.
- "Shortest path in an unweighted graph" — BFS gives it in O(V+E).
- You need to find if a graph is a valid tree (connected + no cycle, so exactly V-1 edges).

## The idea

Model the domain as nodes and edges, then traverse with BFS (level-order, shortest path) or DFS (exhaustive search, cycle detection, topological order). Mark visited nodes to avoid re-processing. For grids, each cell is a node with up to four neighbors. Time is O(V+E) for both traversals; space is O(V) for the visited set plus the call stack (DFS) or queue (BFS).

## Template

```java
// ── BFS (shortest path / level-order) ──────────────────────────────────────
int bfs(int start, int target, List<List<Integer>> adj, int n) {
    boolean[] visited = new boolean[n];
    Queue<Integer> q = new ArrayDeque<>();
    q.offer(start);
    visited[start] = true;
    int steps = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            int node = q.poll();
            if (node == target) return steps;
            for (int nei : adj.get(node)) {
                if (!visited[nei]) {
                    visited[nei] = true;
                    q.offer(nei);
                }
            }
        }
        steps++;
    }
    return -1; // unreachable
}

// ── DFS (iterative, exhaustive) ────────────────────────────────────────────
void dfs(int node, List<List<Integer>> adj, boolean[] visited) {
    visited[node] = true;
    for (int nei : adj.get(node)) {
        if (!visited[nei]) dfs(nei, adj, visited);
    }
}

// ── Grid BFS (4-directional) ───────────────────────────────────────────────
int[] dr = {0, 0, 1, -1};
int[] dc = {1, -1, 0, 0};

void gridBfs(int r, int c, char[][] grid, boolean[][] visited) {
    int ROWS = grid.length, COLS = grid[0].length;
    Queue<int[]> q = new ArrayDeque<>();
    q.offer(new int[]{r, c});
    visited[r][c] = true;
    while (!q.isEmpty()) {
        int[] cur = q.poll();
        for (int d = 0; d < 4; d++) {
            int nr = cur[0] + dr[d], nc = cur[1] + dc[d];
            if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) continue;
            if (visited[nr][nc] || grid[nr][nc] == '0') continue;
            visited[nr][nc] = true;
            q.offer(new int[]{nr, nc});
        }
    }
}

// ── Topological Sort (Kahn's BFS, for directed acyclic graphs) ─────────────
List<Integer> topoSort(int n, List<List<Integer>> adj) {
    int[] indegree = new int[n];
    for (int u = 0; u < n; u++)
        for (int v : adj.get(u)) indegree[v]++;

    Queue<Integer> q = new ArrayDeque<>();
    for (int i = 0; i < n; i++) if (indegree[i] == 0) q.offer(i);

    List<Integer> order = new ArrayList<>();
    while (!q.isEmpty()) {
        int node = q.poll();
        order.add(node);
        for (int nei : adj.get(node))
            if (--indegree[nei] == 0) q.offer(nei);
    }
    return order.size() == n ? order : Collections.emptyList(); // empty = cycle
}

// ── Union-Find (connected components / cycle detection in undirected) ───────
int[] parent, rank;

int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]); // path compression
    return parent[x];
}

boolean union(int x, int y) {
    int px = find(x), py = find(y);
    if (px == py) return false; // already connected — edge is redundant
    if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
    parent[py] = px;
    if (rank[px] == rank[py]) rank[px]++;
    return true;
}
```

## Variations

- **Multi-source BFS** — seed the queue with all sources at once (Rotting Oranges, Walls and Gates). Distance from the nearest source falls out naturally.
- **DFS cycle detection in directed graph** — maintain a `state[]` array: 0=unvisited, 1=in-stack, 2=done. Seeing state=1 means a back edge (cycle).
- **Word Ladder** — build an implicit graph: each word is a node, edges connect words differing by one letter. BFS from begin word to end word.
- **Pacific Atlantic** — two separate BFS/DFS from each ocean's border; answer is the intersection of reachable cells.
- **Surrounded Regions** — DFS from all border 'O' cells to mark safe cells, then flip everything else.

## Pitfalls

- **Forgetting to mark visited before enqueuing** (not after dequeuing) — causes duplicate processing and can blow up on dense graphs.
- **Mutating the grid in-place without restoring** — acceptable when the grid is consumed, dangerous when you need the original later (Clone Graph).
- **Off-by-one on step count in BFS** — increment steps after processing a full level, not per-node.
- **Building an undirected edge list as directed** — for undirected graphs add both `adj[u].add(v)` and `adj[v].add(u)`.
- **Topological sort on a cyclic graph** — Kahn's returns fewer nodes than n; check this to detect the cycle rather than silently returning a partial order.
- **Union-Find without path compression** — degrades to O(n) per find; always compress.

## Problems in this pattern

| Problem | Key technique |
|---|---|
| Number of Islands | Grid DFS/BFS, count components |
| Max Area of Island | Grid DFS/BFS, track size during traversal |
| Clone Graph | DFS + HashMap node-to-clone |
| Walls and Gates | Multi-source BFS from all gates |
| Rotting Oranges | Multi-source BFS from all rotten oranges, track time |
| Pacific Atlantic Water Flow | Two BFS from borders, intersect reachable sets |
| Surrounded Regions | DFS from border 'O' cells to mark safe, then flip |
| Course Schedule | Directed cycle detection (DFS state or Kahn's) |
| Course Schedule II | Topological sort (Kahn's BFS), return order |
| Graph Valid Tree | Union-Find or DFS: connected + no cycle + V-1 edges |
| Number of Connected Components | Union-Find or DFS, count roots |
| Redundant Connection | Union-Find: first edge where union returns false |
| Word Ladder | BFS on implicit word graph, wildcard bucketing for neighbors |
