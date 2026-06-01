# Trees

## When to reach for it

- You see a `TreeNode` (or similar) with `.left` / `.right` fields.
- The problem asks about paths, depths, ancestors, subtrees, or ordering within a binary tree.
- You need to visit every node exactly once and combine results bottom-up or top-down.
- The tree is a BST and you need sorted-order traversal or range queries.
- The problem mentions "level-by-level" or "row" — reach for BFS immediately.
- You see "serialize/deserialize" or "reconstruct from traversals" — tree construction variant.

---

## The idea

Most binary tree problems reduce to a recursive function that asks each node: "what can I compute from my left subtree, my right subtree, and myself?" The recursion bottoms out at `null` (return a base value), and the call stack implicitly tracks state. Alternatively, BFS with a queue handles level-order work. BST problems exploit the invariant `left < node < right` to prune halves, making O(h) queries possible.

**Typical complexity:** O(n) time, O(h) space (call stack), where h = O(log n) balanced, O(n) worst case.

---

## Template

```java
// ── DFS skeleton (post-order: children first, then current) ──────────────────
class Solution {
    public int solve(TreeNode root) {
        return dfs(root);
    }

    private int dfs(TreeNode node) {
        if (node == null) return 0; // base case — define the "empty" return

        int left  = dfs(node.left);
        int right = dfs(node.right);

        // combine left, right, node.val → update any outer state if needed
        return Math.max(left, right) + 1; // example: max depth
    }
}

// ── BFS skeleton (level-order) ───────────────────────────────────────────────
class SolutionBFS {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size(); // snapshot — critical, q grows inside loop
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                level.add(node.val);
                if (node.left  != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }
}

// ── BST in-order (gives sorted sequence) ─────────────────────────────────────
private void inorder(TreeNode node, List<Integer> out) {
    if (node == null) return;
    inorder(node.left, out);
    out.add(node.val);
    inorder(node.right, out);
}
```

---

## Variations

- **Global state via instance field** — when your recursive helper needs to report a value that doesn't bubble up cleanly (max path sum, diameter). Declare `int ans` at class level; update it inside `dfs`.
- **Two-pointer / two-value return** — return an array or int[] from `dfs` when you need multiple facts per node (e.g., `[isBalanced, height]`).
- **LCA pattern** — return the node itself (not a count). If both subtrees return non-null, the current node is the LCA.
- **BST search / validate** — pass `[min, max]` bounds down instead of traversing both subtrees.
- **Construct from traversals** — preorder first element is always root; use a `HashMap` of inorder indices for O(1) split.
- **Serialize / deserialize** — preorder DFS with null markers; use a `Queue<String>` when deserializing so consumed tokens advance automatically.

---

## Pitfalls

- **Forgetting the `null` base case** — always handle `node == null` first; every DFS branch reaches it.
- **Snapshotting queue size in BFS** — `int size = q.size()` must be captured before the inner loop; not recalculated each iteration.
- **Conflating "height" and "depth"** — height is bottom-up (leaf = 0 or 1 depending on convention); depth is top-down. Pick one and be consistent.
- **Max-path-sum double dip** — a path can only go through a node in one direction (left or right, not both) when returning to the parent. You update `ans` with both arms; you return only one arm.
- **BST validation with local comparison** — comparing only `node.val` vs. `node.left.val` misses violations deeper in the tree. Pass explicit `[min, max]` bounds.
- **Integer overflow in path sums** — node values can be negative; initialize the running max to `Integer.MIN_VALUE`, not 0.
- **Off-by-one in Kth smallest** — use a `[count, result]` pair or pass a `int[]` counter to mutate in-place; return early once found.

---

## Problems in this pattern

| Problem | Key technique |
|---|---|
| Invert Binary Tree | Post-order swap: swap children after recursing |
| Max Depth | Post-order max: `max(left, right) + 1` |
| Diameter | Global `ans = max(ans, left + right)`; return `max(left, right) + 1` |
| Balanced Binary Tree | Return `-1` as a sentinel for "unbalanced"; check early |
| Same Tree | Pre-order structural compare: check values, recurse |
| Subtree of Another Tree | `isSameTree` helper called at every node of the outer tree |
| LCA of a BST | Navigate left/right using BST property; no recursion into both sides |
| Level Order Traversal | BFS with snapshot size |
| Right Side View | BFS: last node polled per level; or DFS tracking depth |
| Count Good Nodes | DFS passing `maxSoFar` top-down; increment count when `node.val >= max` |
| Validate BST | DFS with `(long min, long max)` bounds; use `Long.MIN_VALUE / MAX_VALUE` |
| Kth Smallest in a BST | In-order traversal; decrement counter; capture when it hits 0 |
| Construct from Preorder and Inorder | Preorder index pointer + inorder HashMap for O(n) build |
| Binary Tree Max Path Sum | Global `ans`; per-node return is `node.val + max(0, left, right)` |
| Serialize and Deserialize Binary Tree | Preorder + null markers; `Queue<String>` for clean deserialization |
