# Greedy

## When to reach for it

If you see any of these, consider greedy first:

- "maximize" or "minimize" some global quantity by making local choices (profit, coverage, jumps).
- The problem has **optimal substructure** and a **greedy choice property**: the locally best pick at each step never invalidates future optimal picks.
- You need to **schedule**, **cover an interval**, **assign resources**, or **partition** elements.
- A DP solution exists but feels like it only carries one or two values forward — often reducible to greedy.
- The array elements represent "reach", "fuel", "balance", or "capacity" that you accumulate and spend.
- You can sort the input and the answer falls out from a single left-to-right (or two-pointer) sweep.

## The idea

At each step, commit to the locally optimal choice without reconsidering earlier decisions, trusting that this sequence of local optima yields a globally optimal result. The correctness argument usually takes the form of an **exchange argument**: swapping any greedy pick with a non-greedy one can only make the solution worse or equal. Greedy problems typically run in **O(n log n)** time (dominated by a sort) or **O(n)** with a single pass, and use **O(1)** to **O(n)** extra space.

## Template

```java
import java.util.Arrays;

public class GreedyTemplate {

    /**
     * Generic greedy skeleton.
     * Adapt sort key, accumulator type, and decision predicate per problem.
     */
    public int solve(int[] nums) {
        // 1. Sort (if order matters for the greedy choice).
        //    Some problems skip this step entirely.
        Arrays.sort(nums); // or custom comparator

        // 2. Initialize accumulator(s).
        int result = 0;          // answer being built up
        int carry  = 0;          // running value (reach, fuel, balance, …)

        // 3. Single pass — make the greedy choice at each step.
        for (int i = 0; i < nums.length; i++) {
            carry += nums[i];    // consume / extend

            // Greedy decision: reset, flush, or record.
            if (carry < 0) {     // example: Kadane reset
                carry = 0;
            }

            result = Math.max(result, carry); // or min, or count, etc.
        }

        return result;
    }

    // --- Two-accumulator variant (e.g., range tracking) ---
    public boolean solveRange(String s) {
        int lo = 0, hi = 0;      // min and max possible "balance"

        for (char c : s.toCharArray()) {
            // Tighten or expand the feasible range.
            lo += (c == '(' ? 1 : -1);   // pessimistic update
            hi += (c != ')' ? 1 : -1);   // optimistic update

            if (hi < 0) return false;    // impossible to recover
            lo = Math.max(lo, 0);        // clamp: lo can't go negative
        }
        return lo == 0;
    }
}
```

## Variations

- **Interval / scheduling greedy** — sort by end time, greedily pick the earliest-ending non-overlapping interval. Used for interval partitioning, meeting rooms, coverage.
- **Kadane's running max** — maintain a running sum; reset to 0 (or current element) when it goes negative. Extends to circular arrays via complement trick.
- **Reach / jump greedy** — track the farthest index reachable; increment a "steps" counter each time you exhaust the current window. Jump Game I & II.
- **Circular / rotation greedy** — when a global constraint (total gas, net balance) is non-negative, find the starting index where the running sum never dips below zero.
- **Frequency / grouping greedy** — sort or use a frequency map; greedily consume runs of k consecutive elements. Hand of Straights.
- **Two-variable tracking** — maintain a `lo` and `hi` feasible range instead of a single value to handle wildcard characters or uncertainty. Valid Parenthesis String.

## Pitfalls

- **Missing the exchange argument** — confirm the greedy choice is safe before coding; a locally optimal choice that invalidates a future better pick is a DP problem, not greedy.
- **Wrong sort key** — sorting by start instead of end (interval scheduling) or by value instead of frequency (grouping) breaks correctness.
- **Off-by-one on window boundaries** — in jump-game style problems, forgetting to update the current window end before incrementing steps.
- **Ignoring infeasibility early** — not returning `false` the moment a constraint is violated (e.g., `hi < 0` in the parenthesis-string range tracker).
- **Circular wrap-around** — for problems like Gas Station, checking only one rotation is insufficient; track the deficit separately and verify total feasibility first.
- **Integer overflow** — accumulating sums over large arrays; use `long` where needed.
- **Mutating input before it is safe to sort** — some problems require the original index; use index arrays or pairs when necessary.

## Problems in this pattern

| Problem | Key greedy idea |
|---|---|
| **Maximum Subarray** | Kadane: reset running sum to 0 when it goes negative. |
| **Jump Game** | Track max reachable index; return true if it reaches the last index. |
| **Jump Game II** | Sweep left-to-right tracking current window end; increment jumps at window boundary. |
| **Gas Station** | Total gas >= total cost => solution exists. Find start where prefix sum never goes negative. |
| **Hand of Straights** | Sort; greedily consume each group of k consecutive from the smallest remaining card. |
| **Merge Triplets to Form Target** | Discard triplets with any component exceeding the target; check if remaining triplets can supply each target component. |
| **Partition Labels** | Record last occurrence of each character; extend partition end as you scan; cut when index == end. |
| **Valid Parenthesis String** | Track lo/hi range of possible open-count; prune impossible states; valid if lo == 0 at end. |
