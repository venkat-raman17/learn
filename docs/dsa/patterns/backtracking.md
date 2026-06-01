# Backtracking

## When to reach for it

If you see any of these, backtracking is almost certainly the right tool:

- "Find **all** valid combinations / subsets / permutations"
- The answer space is a tree of choices where invalid branches can be pruned early
- You need to build a solution **incrementally** and undo choices that violate constraints
- The problem says "generate all…", "list every…", or "count distinct arrangements…"
- Input size is small enough that exponential is acceptable (n <= ~20 for subsets, n <= ~8 for permutations/N-Queens)
- A grid traversal where you mark cells visited and must unmark them after

## The idea

Backtracking is depth-first search on a decision tree: at each node you try one candidate, recurse into it, then undo (backtrack) and try the next candidate. The key insight is that pruning invalid candidates early avoids exploring dead branches. Time complexity is typically O(2^n) for subset-style problems and O(n!) for permutation-style; space is O(n) for the call stack plus O(output size) for results.

## Template

```java
// Generic backtracking skeleton — adapt choices() and isValid() per problem.
class Solution {
    List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> solve(int[] nums) {
        backtrack(nums, 0, new ArrayList<>());
        return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> current) {
        // 1. Base case: record a complete solution
        if (isComplete(current, nums)) {
            result.add(new ArrayList<>(current)); // snapshot — never add the live list
            return;                                // may or may not return here depending on problem
        }

        // 2. Iterate over candidates for the current position
        for (int i = start; i < nums.length; i++) {

            // 3. Pruning: skip invalid or duplicate candidates before recursing
            if (!isValid(nums, i, current)) continue;

            // 4. Choose
            current.add(nums[i]);

            // 5. Explore (advance start to i+1 for combinations, keep i for repetition)
            backtrack(nums, i + 1, current);

            // 6. Unchoose (backtrack)
            current.remove(current.size() - 1);
        }
    }

    // Stubs — implement per problem
    private boolean isComplete(List<Integer> current, int[] nums) { return false; }
    private boolean isValid(int[] nums, int i, List<Integer> current) { return true; }
}
```

## Variations

| Variation | Key adjustment |
|---|---|
| **Subsets (no duplicates)** | Add `current` to result at every call, not just at base case; pass `i + 1` |
| **Subsets II / Combo Sum II (duplicates in input)** | Sort first; skip `if (i > start && nums[i] == nums[i-1])` |
| **Combination Sum (unlimited reuse)** | Pass `i` (not `i + 1`) into the recursive call |
| **Permutations** | Use a `boolean[] used` array; do not pass `start`; iterate from index 0 each time |
| **Grid/Word Search** | Mark cell visited before recursing (`board[r][c] = '#'`), restore after |
| **Constraint satisfaction (N-Queens)** | Carry bitmasks or sets for row/col/diagonal conflicts; prune before placing |
| **Palindrome partitioning** | Before adding a substring, gate on `isPalindrome(s, l, r)` |

## Pitfalls

1. **Snapshot, not reference.** `result.add(current)` stores a live mutable list. Always do `result.add(new ArrayList<>(current))`.

2. **Forgetting to backtrack.** Every mutation before the recursive call must be undone after it returns. Missing an undo corrupts the shared state for sibling branches.

3. **Wrong `start` index.** Combinations advance `start` to `i + 1`. Permutations restart from 0 with a `used[]` guard. Mixing these up generates wrong output silently.

4. **Duplicate handling without sorting.** The `nums[i] == nums[i-1]` skip only works if the array is sorted first. If you forget `Arrays.sort(nums)`, you will get duplicate result sets.

5. **Off-by-one on base case.** Recording results only at the leaf misses intermediate subsets; recording at every node works for subsets but double-counts for fixed-length combination problems. Match the base-case placement to what "complete" means for the problem.

6. **Grid: overwriting the original.** After restoring `board[r][c]`, make sure you are writing back the **original character**, not a hardcoded sentinel. Store it in a local variable before clobbering.

7. **Pruning too late.** Move constraint checks *before* recursing (not inside the recursive call's first line) to keep the tree small.

## Problems in this pattern

| Problem | Notes |
|---|---|
| **Subsets** | Record result at every node; pass `i + 1` |
| **Combination Sum** | Unlimited reuse — pass `i` instead of `i + 1`; prune when `remaining < 0` |
| **Permutations** | `boolean[] used`; restart from 0 each level |
| **Subsets II** | Sort + skip duplicate siblings (`i > start && nums[i] == nums[i-1]`) |
| **Combination Sum II** | Sort + skip duplicate siblings + advance `i + 1` (no reuse) |
| **Word Search** | DFS on grid; mark/unmark visited cell in-place |
| **Palindrome Partitioning** | Gate recursion on palindrome check; precompute DP table for large inputs |
| **Letter Combinations of a Phone Number** | Map digit -> chars; base case when `index == digits.length()` |
| **N Queens** | Track `cols`, `diag1`, `diag2` sets; prune before placing queen |
