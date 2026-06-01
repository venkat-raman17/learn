# Pattern triggers — "if you see X, reach for Y"

Recognition is half the battle. Skim this before a problem set; aim to name the pattern in ~2 min.

| If you see… | Reach for | Why |
| --- | --- | --- |
| Sorted array; pair/triplet with a target sum; palindrome check | **Two Pointers** | Converge from both ends, O(n) instead of O(n²). |
| Contiguous subarray/substring with a constraint (longest/shortest/at most k) | **Sliding Window** | Expand right, contract left; O(n). |
| "Find / threshold / minimize the max / maximize the min" over a **sorted** or monotonic space | **Binary Search** (incl. on the answer) | Halve the search space each step. |
| Frequency counts, dedup, "have I seen this", group by key | **Hashing** | O(1) lookup; turns O(n²) into O(n). |
| Top-k, k-th largest/smallest, merge k lists, running median | **Heap / Priority Queue** | Maintain the k best in O(log k). |
| Matching pairs, nesting, "most recent", monotonic next-greater | **Stack** (often **monotonic**) | LIFO; next-greater/smaller in O(n). |
| All subsets / permutations / combinations; "find all paths"; constraint satisfaction | **Backtracking** | Choose → recurse → undo; prune early. |
| Shortest path in an **unweighted** graph / grid; level-by-level | **BFS** | Queue explores nearest first. |
| Connectivity, cycle detection, flood fill, "regions" | **DFS / Union-Find** | Traverse components; DSU for dynamic connectivity. |
| Weighted shortest path (non-negative) | **Dijkstra** | Greedy + min-heap. |
| Ordering with prerequisites / dependencies (DAG) | **Topological Sort** | Kahn (indegree) or DFS post-order. |
| Prefix lookups, autocomplete, word dictionary | **Trie** | O(L) per word. |
| "Count the ways", "min/max over a sequence of choices", overlapping subproblems | **Dynamic Programming** | Define state + transition; memoize or tabulate. |
| Overlapping/merge ranges; "can attend all meetings" | **Intervals** | Sort by start; sweep / merge. |
| Local optimal → global optimal; "minimum number of …" | **Greedy** | Prove the exchange argument; sort + pick. |
| Range sum/min with updates | **Segment / Fenwick tree** | O(log n) query + update. |
| XOR tricks, "single number", bit counting, subsets via bitmask | **Bit Manipulation** | XOR cancels pairs; bitmask enumerates subsets. |
| Detect cycle in linked list / find duplicate / cycle length | **Fast & Slow Pointers** | Floyd's tortoise-and-hare. |

## Quick DP state hints
- 1-D over an array/string → `dp[i]` = answer ending at / using first `i`.
- Two sequences → 2-D `dp[i][j]` (LCS, edit distance).
- Subset/knapsack → `dp[i][capacity]` (or rolling 1-D).
- Grid → `dp[r][c]` from neighbors.
- Intervals → `dp[i][j]` over a range (burst balloons, MCM).
