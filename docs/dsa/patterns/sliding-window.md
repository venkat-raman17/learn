# Sliding Window

## When to reach for it

If you see any of these, consider a sliding window before anything else:

- The problem asks for a **subarray or substring** that satisfies some constraint (max length, min length, exact count, etc.)
- The problem says "contiguous" explicitly
- You need to track a running aggregate (sum, distinct count, frequency map) over a range that grows and shrinks
- A brute-force O(n²) solution is obvious but the constraint screams for O(n)
- Keywords: *longest*, *shortest*, *minimum window*, *at most k*, *exactly k distinct*, *permutation in*

Do NOT reach for it when the target subarray is non-contiguous or when index order does not matter (use a map/sort instead).

---

## The idea

Maintain two pointers, `left` and `right`, that define a window `[left, right]` over the input. Expand `right` one step at a time to include new elements, and shrink `left` only when a constraint is violated. Because each element enters and leaves the window at most once, the total work is O(n).

Space is O(1) for sum-based windows; O(k) where k is alphabet size or distinct-element count for frequency-map windows. No auxiliary stack or recursion needed.

---

## Template

```java
// Generic variable-size sliding window (find longest valid window)
public int slidingWindow(int[] arr, /* constraint params */) {
    int left = 0;
    int result = 0;
    // State tracked inside the window — adjust type as needed
    int windowState = 0; // e.g. current sum, or use a Map<Character, Integer>

    for (int right = 0; right < arr.length; right++) {
        // 1. Expand: include arr[right] in the window
        windowState += arr[right]; // or map.merge(arr[right], 1, Integer::sum)

        // 2. Shrink: move left until the window is valid again
        while (windowIsInvalid(windowState /*, constraint */)) {
            windowState -= arr[left]; // or decrement map entry, remove if 0
            left++;
        }

        // 3. Update answer — window [left, right] is now valid
        result = Math.max(result, right - left + 1);
    }
    return result;
}

// Fixed-size window variant (window size = k)
public int fixedWindow(int[] arr, int k) {
    int windowSum = 0;
    int result = Integer.MIN_VALUE;

    for (int right = 0; right < arr.length; right++) {
        windowSum += arr[right];

        if (right >= k) {                     // evict the element falling off the left
            windowSum -= arr[right - k];
        }

        if (right >= k - 1) {                 // window is full
            result = Math.max(result, windowSum);
        }
    }
    return result;
}

// Two-pointer for a sorted or monotone condition (e.g. two-sum variant)
public int twoPointer(int[] sorted, int target) {
    int left = 0, right = sorted.length - 1;
    int result = 0;

    while (left < right) {
        int sum = sorted[left] + sorted[right];
        if (sum == target) { result++; left++; right--; }
        else if (sum < target) left++;
        else right--;
    }
    return result;
}
```

---

## Variations

| Variation | Key adjustment |
|---|---|
| **Fixed-size window** | Remove `while` shrink loop; evict `arr[right - k]` each step |
| **At most k distinct / k violations** | Shrink when `map.size() > k` or violation count exceeds threshold |
| **Exactly k distinct** | `atMost(k) - atMost(k - 1)` trick |
| **Minimum window** | Track `formed` counter; shrink aggressively, record minimum on valid window |
| **Monotonic deque (max/min in window)** | Maintain a `Deque<Integer>` of indices in decreasing order; front is current max |
| **Character replacement** | Track max frequency in window; shrink when `(windowSize - maxFreq) > k` |

---

## Pitfalls

- **Forgetting to clean up the map entry.** When shrinking, decrement the count and then `remove` the key if it hits zero — otherwise `map.size()` stays inflated and the valid-window check is wrong.
- **Off-by-one on window size.** Window length is `right - left + 1`, not `right - left`.
- **Using `if` instead of `while` to shrink.** A single `if` only evicts one element; a `while` is correct for variable-size windows because the window may need to shrink multiple steps.
- **Checking the answer before the window is full (fixed-size).** Guard with `if (right >= k - 1)` before recording the result.
- **Updating `maxFreq` but not recalculating it on shrink (character replacement).** You only need to track the max going forward — never decrease it — because a smaller window with a lower max is never a better answer than the current best.
- **Deque: not popping expired indices.** In the sliding-window maximum, always check `if (deque.peekFirst() < left) deque.pollFirst()` at the start of each iteration.

---

## Problems in this pattern

| Problem | Difficulty | Key insight |
|---|---|---|
| **Best Time to Buy and Sell Stock** | Easy | Two-pointer: track running min price as implicit left pointer; answer is `price - minSoFar` |
| **Longest Substring Without Repeating Characters** | Medium | Variable window; shrink when a duplicate enters — use `Map<char, lastIndex>` and jump `left` directly |
| **Longest Repeating Character Replacement** | Medium | Shrink when `(windowSize - maxFreq) > k`; never decrease `maxFreq` |
| **Permutation in String** | Medium | Fixed window of size `len(p)`; compare frequency arrays or use a `matches` counter |
| **Minimum Window Substring** | Hard | Variable window; expand until `formed == required`, then shrink for minimum; record when fully formed |
| **Sliding Window Maximum** | Hard | Monotonic deque of indices (decreasing values); front of deque is max of current window |
