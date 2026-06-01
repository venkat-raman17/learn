# 1-D Dynamic Programming

## When to reach for it

- The problem asks for an **optimal value** (min/max/count/boolean) over a sequence.
- Each element's answer depends only on a **fixed look-back window** (previous 1-3 elements) or on all previous elements in a predictable way.
- You see phrases like: "minimum cost", "number of ways", "can you partition", "longest subsequence", "maximum product".
- Brute-force recursion has **overlapping subproblems** — you'd recompute the same index multiple times.
- The state space is **one-dimensional**: position in array, remaining capacity, current index.
- Contrast with 2-D DP: if the state needs two independent variables (row + col, two strings, index + remaining budget), you need a 2-D table.

## The idea

Define `dp[i]` as the answer to the subproblem ending at (or considering up to) index `i`. Build the table bottom-up from base cases, using a recurrence that expresses `dp[i]` in terms of earlier entries. Final answer is typically `dp[n-1]`, `dp[n]`, or a scan of the whole array.

**Time:** O(n) or O(n^2) depending on look-back width.
**Space:** O(n) for the full table; often reducible to O(1) or O(k) with rolling variables when recurrence only needs the last k entries.

## Template

```java
// Generic 1-D DP — adapt recurrence and base cases per problem.
public int solve(int[] nums) {
    int n = nums.length;
    if (n == 0) return 0;

    // --- Option A: full table (when you need all past values) ---
    int[] dp = new int[n + 1];   // +1 for 1-indexed convenience; adjust as needed

    // Base cases
    dp[0] = <base0>;
    dp[1] = <base1>;

    // Fill
    for (int i = 2; i <= n; i++) {
        dp[i] = <recurrence using dp[i-1], dp[i-2], nums[i-1], ...>;
    }

    return dp[n];

    // --- Option B: rolling variables (constant-window look-back) ---
    // int prev2 = <base0>, prev1 = <base1>;
    // for (int i = 2; i <= n; i++) {
    //     int cur = <recurrence(prev1, prev2, nums[i-1])>;
    //     prev2 = prev1;
    //     prev1 = cur;
    // }
    // return prev1;
}
```

## Variations

**Fixed look-back (k steps).** Climbing Stairs / House Robber: `dp[i] = f(dp[i-1], dp[i-2])`. Collapse to rolling variables for O(1) space.

**Circular array.** House Robber II: split into two linear passes — `[0..n-2]` and `[1..n-1]` — and take the max. Avoids the index-wrap edge case.

**Substring / subarray (O(n^2)).** Longest Palindromic Substring, Palindromic Substrings: expand-around-center instead of DP table (same complexity, less code). When using a table, `dp[i][j]` is 2-D but the outer driver is one index.

**Unbounded choices per step.** Coin Change, Word Break: inner loop iterates over choices at each index; `dp[i] = min/or over all valid dp[i - choice]`.

**Tracking sign flips.** Maximum Product Subarray: maintain both `maxSoFar` and `minSoFar` at each index because a negative times a negative flips back to a max.

**Boolean reachability.** Word Break, Decode Ways: `dp[i]` is boolean or count; initialize `dp[0] = true/1` as the empty-string base case.

**Subsequence (non-contiguous).** Longest Increasing Subsequence: `dp[i] = 1 + max(dp[j] for j < i if nums[j] < nums[i])` — O(n^2). Patience-sort binary-search trick gives O(n log n).

**Subset / knapsack.** Partition Equal Subset Sum: `dp[s]` = can we reach sum `s`. Iterate sums in reverse to avoid reusing an element twice (0-1 knapsack pattern).

## Pitfalls

- **Off-by-one on array sizing.** Using `dp[n]` with 1-indexed logic on a 0-indexed input array. Write the index mapping out explicitly before coding.
- **Wrong base cases.** Forgetting `dp[0]` or setting it to `0` when it should be `1` (e.g., empty string is one valid decoding). Test manually on n=0 and n=1.
- **Circular problem — treating it as linear.** House Robber II: including both index 0 and index n-1 in the same pass violates the circle constraint.
- **Reusing elements in 0-1 knapsack.** Iterating sums forward lets you pick the same item twice. Always iterate from `target` down to `cost` for 0-1 knapsack.
- **Not considering both signs in product subarray.** Resetting to `1` on zero but forgetting to track `minProduct` means you miss negative-pair flips.
- **Returning dp[n-1] vs dp[n].** When the table is sized `n+1` with a dummy `dp[0]` base, the answer is at `dp[n]`, not `dp[n-1]`.
- **LIS: reusing the patience-sort array as actual LIS.** The binary-search array gives the correct length but not the actual subsequence — reconstruct separately if the sequence itself is needed.

## Problems in this pattern

| Problem | Key recurrence / trick |
|---|---|
| Climbing Stairs | `dp[i] = dp[i-1] + dp[i-2]` |
| Min Cost Climbing Stairs | `dp[i] = cost[i] + min(dp[i-1], dp[i-2])` |
| House Robber | `dp[i] = max(dp[i-1], dp[i-2] + nums[i])` |
| House Robber II | Two linear passes over circular array |
| Longest Palindromic Substring | Expand-around-center, track max window |
| Palindromic Substrings | Count expansions at each center |
| Decode Ways | `dp[i] += dp[i-1]` (1-digit) + `dp[i-2]` (2-digit) if valid |
| Coin Change | `dp[i] = 1 + min(dp[i - c] for each coin c)` |
| Maximum Product Subarray | Track `maxProd` and `minProd`; reset on zero |
| Word Break | `dp[i] = any(dp[i-len] && word match)` |
| Longest Increasing Subsequence | O(n^2) DP or O(n log n) patience sort |
| Partition Equal Subset Sum | 0-1 knapsack on boolean `dp[sum]`, iterate reverse |
