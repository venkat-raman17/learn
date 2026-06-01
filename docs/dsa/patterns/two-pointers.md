# Two Pointers

## When to reach for it

Reach for two pointers if you see any of these:

- Array or string is **sorted** (or can be sorted without breaking the problem).
- You need pairs, triplets, or subarrays that satisfy a **sum or distance constraint**.
- The problem asks you to compare elements from **both ends** moving inward (palindrome, container width).
- A brute-force O(n^2) nested loop feels natural — two pointers often cuts it to O(n).
- The words "in-place", "no extra space", or "linear time" appear in the constraints.
- You are sliding a window where both boundaries move in the **same direction** (fast/slow variant).

## The idea

Place one pointer at each end (or both at the start for fast/slow) and advance them toward
each other based on a comparison against the target. Because the array is sorted (or the
structure is monotone), each step provably eliminates at least one candidate, giving you a
single linear pass. Time: **O(n)** after any sort; space: **O(1)** extra (in-place).

## Template

```java
// Classic opposite-end template (sorted array, pair sum, palindrome check, etc.)
int left = 0, right = arr.length - 1;

while (left < right) {
    int val = arr[left] + arr[right]; // or some comparison

    if (val == target) {
        // process result
        left++;
        right--;
        // skip duplicates if needed:
        // while (left < right && arr[left] == arr[left - 1]) left++;
        // while (left < right && arr[right] == arr[right + 1]) right--;
    } else if (val < target) {
        left++;
    } else {
        right--;
    }
}

// Fast / slow pointer template (cycle detection, middle of list, etc.)
int slow = 0, fast = 0;

while (fast < arr.length && fast + 1 < arr.length) {
    slow++;
    fast += 2;
    // process arr[slow] and arr[fast]
}
```

## Variations

**Opposite ends — pair sum**
Classic sorted array. Advance `left` when sum is too small, retreat `right` when too large.

**Opposite ends — shrink/expand**
Container With Most Water: move the pointer at the *shorter* wall inward to have any chance
of finding a taller one.

**Three pointers (3Sum)**
Fix one element with an outer loop, then run the standard two-pointer pair-sum on the
remaining subarray. Sort first; skip duplicates at all three levels.

**Prefix + suffix arrays (Trapping Rain Water)**
Pre-compute `maxLeft[i]` and `maxRight[i]`, then water at `i` = `min(maxLeft[i], maxRight[i]) - height[i]`.
Alternatively, run the two-pointer scan and maintain running max on each side.

**Fast / slow (Floyd's)**
Both pointers start at index 0 (or head). Fast moves 2x. Used for cycle detection,
finding the midpoint, or removing the nth node from the end.

**Partition / in-place write**
One pointer marks the "write position" (slow); the other scans forward (fast).
Used in Remove Duplicates, Move Zeroes.

## Pitfalls

- **Forgetting to sort.** Two pointers on an unsorted array is usually wrong. Always verify
  the pre-condition or sort explicitly (and account for O(n log n) in complexity).
- **Off-by-one on the loop guard.** Use `left < right`, not `left <= right`, for opposite-end
  problems. Crossing pointers means you have already processed every pair.
- **Skipping duplicate handling in 3Sum.** You will return duplicate triplets. After advancing
  both pointers on a match, skip while the value equals the previous value at each pointer.
- **Wrong pointer to move in container/water problems.** Always move the smaller side; moving
  the larger side can only decrease area further.
- **Integer overflow.** `arr[left] + arr[right]` can overflow `int` for large values. Cast to
  `long` or subtract from target: `target - arr[left]` compared to `arr[right]`.
- **Mutating the array mid-pass without tracking the sorted order.** If you re-order in the
  loop, the monotone property breaks.

## Problems in this pattern

| Problem | Key insight |
|---|---|
| **Valid Palindrome** | Opposite ends; skip non-alphanumeric with `Character.isLetterOrDigit`. |
| **Two Sum II** | Sorted input guarantees O(n); advance left or retreat right based on sum vs. target. |
| **3Sum** | Sort + fix outer index + two-pointer on the rest; three levels of duplicate skipping. |
| **Container With Most Water** | Move the shorter wall inward; width shrinks but height might grow. |
| **Trapping Rain Water** | Two-pointer with running `maxLeft` / `maxRight`; water = min(max sides) - height. |
