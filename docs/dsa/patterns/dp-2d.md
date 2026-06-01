# 2-D Dynamic Programming

## When to reach for it

If you see any of these, suspect 2-D DP:

- Two sequences (strings, arrays) compared against each other — "common", "edit", "interleaving", "matching".
- A single sequence but with two moving indices — start/end windows, left/right boundaries (burst balloons).
- A 2-D grid where you need cumulative counts or costs — paths, reachability.
- A knapsack-style problem with two axes of state: items × capacity, coins × target, etc.
- "How many ways…" or "minimum cost…" that depends on choosing from both dimensions simultaneously.
- Regex / wildcard matching where pattern position and string position both advance independently.

## The idea

Define `dp[i][j]` to represent the answer to a subproblem parameterized by two indices (often `i` = prefix of input A, `j` = prefix of input B, or row/col in a grid). Fill the table bottom-up using a recurrence that expresses `dp[i][j]` in terms of already-computed cells. Most problems fill in O(m*n) time and O(m*n) space; space is often reducible to O(min(m,n)) by keeping only the previous row.

## Template

```java
// Generic 2-D DP skeleton — adapt dp meaning and recurrence per problem.
// Inputs: A (length m), B (length n), or a single input with two axes.
int solve(int[] A, int[] B) {
    int m = A.length, n = B.length;

    // dp[i][j] = answer for A[0..i-1] and B[0..j-1]
    int[][] dp = new int[m + 1][n + 1];

    // --- Base cases ---
    // dp[0][j] and dp[i][0] are typically 0, 1, or j/i depending on problem.
    for (int j = 0; j <= n; j++) dp[0][j] = /* base */ 0;
    for (int i = 0; i <= m; i++) dp[i][0] = /* base */ 0;

    // --- Fill ---
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (/* A[i-1] matches / compatible with B[j-1] */) {
                dp[i][j] = /* diagonal or derived */ dp[i - 1][j - 1] + /* delta */;
            } else {
                dp[i][j] = /* combine */ Math.min(dp[i - 1][j], dp[i][j - 1]) + /* cost */;
                // or Math.max, or dp[i-1][j] + dp[i][j-1], etc.
            }
        }
    }

    return dp[m][n];
}

// --- Space-optimized variant (rolling array, when only prev row needed) ---
int solveSpaceOpt(int[] A, int[] B) {
    int m = A.length, n = B.length;
    int[] prev = new int[n + 1];
    int[] curr = new int[n + 1];

    for (int i = 1; i <= m; i++) {
        // reset curr base case for this row if needed
        curr[0] = /* base for row i */;
        for (int j = 1; j <= n; j++) {
            if (/* match condition */) {
                curr[j] = prev[j - 1] + /* delta */;
            } else {
                curr[j] = Math.min(prev[j], curr[j - 1]) + /* cost */;
            }
        }
        int[] tmp = prev; prev = curr; curr = tmp; // swap
    }
    return prev[n];
}
```

## Variations

- **Interval DP** (`dp[i][j]` = answer for subarray `A[i..j]`): fill by increasing interval length. Used in Burst Balloons. Outer loop is length, inner loops are `i` and `k` (split point).
- **Grid path DP** (`dp[r][c]` = ways/cost to reach cell): only right/down moves means `dp[r][c] = dp[r-1][c] + dp[r][c-1]`. Unique Paths is the canonical example.
- **Memoized DFS instead of bottom-up**: when the state space is sparse or recurrence is hard to topologically order (e.g., Longest Increasing Path in a Matrix where direction depends on values). Use `int[][] memo` initialized to -1; DFS from each cell.
- **1-D compression**: when `dp[i][j]` only depends on `dp[i-1][*]` and `dp[i][j-1]`, a single row suffices. Iterate `j` left-to-right (unbounded knapsack style) or right-to-left (0-1 knapsack style) depending on whether reuse is allowed.
- **With an extra state axis**: Buy/Sell Stock With Cooldown adds a "holding" boolean — becomes `dp[i][0/1]` (2-D over index × state).

## Pitfalls

- **Off-by-one in indexing**: `dp[i][j]` representing `A[0..i-1]` means `A[i-1]` (not `A[i]`) inside the loop. Sketch the meaning before coding.
- **Wrong base cases**: a missing or incorrect `dp[0][j]` or `dp[i][0]` corrupts the entire table. For Edit Distance `dp[i][0] = i` (delete all), `dp[0][j] = j` (insert all) — easy to forget.
- **Bottom-up order**: ensure every cell you read is already filled. For interval DP, iterate by length, not by `i`.
- **Integer overflow**: counting problems (Distinct Subsequences, Unique Paths with large grids) can overflow `int`; use `long`.
- **Memoized DFS without a visited guard**: in grid problems (Longest Increasing Path), you must not revisit; the strictly-increasing constraint guarantees no cycles, but confirm this before skipping a visited check.
- **Greedy temptation**: "local best = global best" is rarely true when two sequences interact. If you find yourself writing a greedy and it fails edge cases, switch to 2-D DP.
- **Confusing "at most" vs "exactly"**: Coin Change II counts exact-sum combinations; accidentally allowing overcounting by iterating in the wrong direction gives wrong answers.

## Problems in this pattern

| Problem | Key state | Recurrence hint |
|---|---|---|
| Unique Paths | `dp[r][c]` = paths to cell | `dp[r-1][c] + dp[r][c-1]` |
| Longest Common Subsequence | `dp[i][j]` = LCS of prefixes | match → `dp[i-1][j-1]+1`; else `max(dp[i-1][j], dp[i][j-1])` |
| Buy/Sell Stock With Cooldown | `dp[i][holding]` | 3-state: hold, sold, rest |
| Coin Change II | `dp[i][j]` = ways using coins `0..i` summing to `j` | `dp[i-1][j] + dp[i][j-coin]` |
| Target Sum | `dp[i][sum+offset]` | add or subtract `nums[i]` |
| Interleaving String | `dp[i][j]` = can interleave `s1[0..i-1]`, `s2[0..j-1]` → `s3[0..i+j-1]` | OR of two branches |
| Edit Distance | `dp[i][j]` = edits to convert prefixes | match → `dp[i-1][j-1]`; else `1 + min(3 neighbors)` |
| Longest Increasing Path in a Matrix | `memo[r][c]` = LIP from cell | memoized DFS, 4-dir, strictly increasing |
| Distinct Subsequences | `dp[i][j]` = ways `s[0..i-1]` contains `t[0..j-1]` | match → `dp[i-1][j-1] + dp[i-1][j]`; else `dp[i-1][j]` |
| Burst Balloons | `dp[i][j]` = max coins bursting `balloons[i..j]` | interval DP, try each `k` as last burst |
| Regular Expression Matching | `dp[i][j]` = `s[0..i-1]` matches `p[0..j-1]` | handle `*` by zero or more of preceding char |
