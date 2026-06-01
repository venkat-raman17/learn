# Binary Search

## When to reach for it

Reach for binary search when you see any of these signals:

- The input array is **sorted** (or can be treated as logically sorted).
- The problem asks for a **target value, boundary, or minimum/maximum** in a monotone space.
- The problem says "find the smallest X such that condition(X) is true" — that is a binary search on the answer.
- Naive O(n) or O(n^2) is too slow and the search space has a **monotone predicate** (once false, always false — or vice versa).
- Keywords: "sorted array", "rotated sorted", "minimum", "first/last occurrence", "koko", "capacity", "feasible".

## The idea

Binary search eliminates half the remaining search space on each step by evaluating a predicate at the midpoint. The key invariant is: maintain `[lo, hi]` such that the answer always lies within that window, then shrink it until `lo == hi`. Time complexity is O(log n) for a space of size n; space is O(1) for iterative implementations.

For "binary search on answer" problems, the array is never explicitly sorted — instead you define a predicate over a numeric range (e.g., `canFinish(speed)`) and binary search that range.

**Time:** O(log n) | **Space:** O(1)

## Template

```java
// --- Classic: find exact target ---
int binarySearch(int[] nums, int target) {
    int lo = 0, hi = nums.length - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2; // avoids overflow vs (lo+hi)/2
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) lo = mid + 1;
        else hi = mid - 1;
    }
    return -1; // not found
}

// --- Left boundary: first index where nums[i] >= target ---
int lowerBound(int[] nums, int target) {
    int lo = 0, hi = nums.length; // hi = length (open right)
    while (lo < hi) {             // strict '<', not '<='
        int mid = lo + (hi - lo) / 2;
        if (nums[mid] < target) lo = mid + 1;
        else hi = mid;            // mid stays as a candidate
    }
    return lo; // lo == hi, insertion point
}

// --- Binary search on answer ---
// Replace feasible() with your monotone predicate.
// Returns the minimum value in [lo, hi] where feasible(mid) is true.
int searchOnAnswer(int lo, int hi) {
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (feasible(mid)) hi = mid;   // mid could be the answer; keep it
        else lo = mid + 1;
    }
    return lo;
}
```

## Variations

**Left / right boundary (first / last occurrence)**
Use the `lowerBound` pattern above. For last occurrence, flip the condition: when `nums[mid] <= target` move `lo = mid + 1`, else `hi = mid`; answer is `lo - 1`.

**Rotated sorted array**
One half is always fully sorted. Check which half by comparing `nums[mid]` to `nums[lo]`. Apply target range check against the sorted half to decide which side to discard.

**Search in 2D matrix**
Treat the matrix as a flattened sorted array of length `m * n`. Map `mid` back to `(mid / n, mid % n)`.

**Binary search on answer (parametric search)**
Define `feasible(k)` as a boolean. The search range is the domain of `k` (e.g., `[1, max_val]`). Classic examples: Koko eating bananas, ship packages within D days.

**Find peak element**
Not a sorted search — compare `nums[mid]` with `nums[mid+1]` to decide which slope to follow. O(log n) still applies.

## Pitfalls

- **Off-by-one on `hi`:** For exact search use `hi = len - 1` and `lo <= hi`. For boundary search use `hi = len` (open) and `lo < hi`. Mixing these causes infinite loops or missed answers.
- **Overflow in `mid`:** Always write `mid = lo + (hi - lo) / 2`, never `(lo + hi) / 2`.
- **Infinite loop with `mid = lo`:** When `lo + 1 == hi`, `mid == lo`. If your update is `lo = mid` (not `mid + 1`), the loop never terminates. Ensure at least one side strictly shrinks.
- **Wrong predicate direction:** For "minimum feasible" use `if feasible: hi = mid`, `else: lo = mid + 1`. Flipping these finds the maximum infeasible instead.
- **Rotated array edge case:** When the array has duplicates (variant of the problem), the two-pointer approach can degrade to O(n); note this in interviews.
- **Forgetting to validate `lo` after the loop:** In boundary searches the loop exits with `lo` pointing to the insertion point. Always check `lo < nums.length && nums[lo] == target` before returning success.

## Problems in this pattern

| Problem | Key insight |
|---|---|
| **Binary Search** | Textbook exact-match; use the classic `lo <= hi` template. |
| **Search a 2D Matrix** | Flatten to 1D index; `row = mid / cols`, `col = mid % cols`. |
| **Koko Eating Bananas** | Search on answer: `feasible(speed)` = can finish all piles in `h` hours. Range `[1, max(piles)]`. |
| **Find Minimum in Rotated Sorted Array** | Binary search for the inflection point; the unsorted half always contains the minimum. |
| **Search in Rotated Sorted Array** | Identify which half is sorted, check if target falls in it, discard the other. |
| **Time Based Key-Value Store** | `get()` does a binary search (or `TreeMap.floorKey`) over timestamps stored per key. |
| **Median of Two Sorted Arrays** | Binary search on the partition of the shorter array; hard — ensure left-max <= right-min on both sides. |
