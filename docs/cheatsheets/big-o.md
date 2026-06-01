# Big-O cheat sheet

`*` = amortized / average; worst case noted separately. `n` = #elements, `V`/`E` = vertices/edges.

## Data-structure operations

| Structure | Access | Search | Insert | Delete | Space |
| --- | --- | --- | --- | --- | --- |
| Array (static) | O(1) | O(n) | O(n) | O(n) | O(n) |
| Dynamic array | O(1) | O(n) | O(1)* / O(n) worst | O(n) | O(n) |
| Singly/Doubly linked list | O(n) | O(n) | O(1) at ends | O(1) at ends | O(n) |
| Stack / Queue / Deque | — | O(n) | O(1) | O(1) | O(n) |
| Hash table | — | O(1)* / O(n) | O(1)* | O(1)* | O(n) |
| BST (balanced) | O(log n) | O(log n) | O(log n) | O(log n) | O(n) |
| BST (worst, skewed) | O(n) | O(n) | O(n) | O(n) | O(n) |
| Binary heap | — | O(n) | O(log n) | O(log n) (pop) | O(n) |
| Trie | — | O(L) | O(L) | O(L) | O(Σ·N·L) |
| Segment / Fenwick tree | — | O(log n) query | O(log n) update | — | O(n) |

`L` = key length, `Σ` = alphabet. Heap `peek` is O(1).

## Sorting

| Algorithm | Best | Average | Worst | Space | Stable? |
| --- | --- | --- | --- | --- | --- |
| Merge sort | O(n log n) | O(n log n) | O(n log n) | O(n) | yes |
| Quick sort | O(n log n) | O(n log n) | O(n²) | O(log n) | no |
| Heap sort | O(n log n) | O(n log n) | O(n log n) | O(1) | no |
| Insertion sort | O(n) | O(n²) | O(n²) | O(1) | yes |
| Counting / Radix | O(n+k) | O(n+k) | O(n·k) | O(n+k) | yes |

## Graph algorithms

| Algorithm | Time | Space |
| --- | --- | --- |
| BFS / DFS | O(V + E) | O(V) |
| Dijkstra (binary heap) | O((V + E) log V) | O(V) |
| Bellman-Ford | O(V·E) | O(V) |
| Floyd-Warshall | O(V³) | O(V²) |
| Topological sort | O(V + E) | O(V) |
| Kruskal / Prim (MST) | O(E log V) | O(V) |
| Union-Find (op, ~amortized) | O(α(n)) ≈ O(1) | O(n) |

## The ladder (know where your solution sits)

O(1) < O(log n) < O(n) < O(n log n) < O(n²) < O(2ⁿ) < O(n!)

- `log n` → halving each step (binary search, balanced trees).
- `n log n` → sort, or "n items each doing a log-n op".
- `2ⁿ` / `n!` → subsets / permutations → look for DP or pruning.

## Rules of thumb
- ~10⁸ simple ops/second is a rough budget; n ≤ 10⁴ tolerates O(n²), n ≤ 10⁶ wants O(n log n), n ≥ 10⁹ needs O(log n) or O(1).
- Hash map turns an O(n²) double-loop into O(n) — the most common optimization.
- Recursion depth counts toward space (call stack).
