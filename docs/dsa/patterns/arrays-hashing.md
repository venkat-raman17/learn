# Arrays & Hashing

## When to reach for it

- You see "find duplicates", "check if two collections are equivalent", or "group elements by some property".
- The problem asks for **O(n)** or better on an unsorted array where a sort-first approach would cost O(n log n).
- You need fast frequency counts: "top K", "most common", "at least K times".
- You need to encode/decode a variable-length stream without a delimiter collision.
- You need the **longest consecutive run** without sorting.
- Any "for each element, is its complement/pair already seen?" pattern (Two Sum style).

## The idea

Trade space for time by stuffing elements (or derived keys) into a `HashMap` or `HashSet`. A single linear pass lets you answer lookup, frequency, and membership queries in O(1) average time. Group elements by a computed key (sorted string, frequency tuple, etc.) to bucket equivalent items together. Overall cost is typically **O(n) time, O(n) space**; the constant factor depends on key construction.

## Template

```java
import java.util.*;

public class ArraysHashingTemplate {

    // --- frequency map skeleton ---
    public void frequencyMap(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) {
            freq.merge(n, 1, Integer::sum);   // freq.put(n, freq.getOrDefault(n,0)+1)
        }
        // iterate freq.entrySet() for downstream logic
    }

    // --- seen-set / two-pass complement skeleton ---
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value -> index
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{};
    }

    // --- group-by-key skeleton (e.g. Group Anagrams) ---
    public List<List<String>> groupByKey(String[] words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : words) {
            String key = buildKey(w);               // derive canonical key
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(groups.values());
    }

    private String buildKey(String w) {
        char[] chars = w.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
        // alternative: int[26] count array -> Arrays.toString(count)
    }

    // --- top-K by frequency (bucket sort O(n)) ---
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new List[nums.length + 1];
        freq.forEach((val, cnt) -> {
            if (buckets[cnt] == null) buckets[cnt] = new ArrayList<>();
            buckets[cnt].add(val);
        });

        int[] result = new int[k];
        int idx = 0;
        for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
            if (buckets[i] != null) {
                for (int val : buckets[i]) {
                    result[idx++] = val;
                    if (idx == k) break;
                }
            }
        }
        return result;
    }

    // --- longest consecutive sequence O(n) ---
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int best = 0;
        for (int n : set) {
            if (!set.contains(n - 1)) {       // only start a chain at its head
                int len = 1;
                while (set.contains(n + len)) len++;
                best = Math.max(best, len);
            }
        }
        return best;
    }
}
```

## Variations

- **Sorted-char key vs. count-array key** — `Arrays.sort` is O(k log k) per word; a 26-element count array stringified is O(k). Prefer the count array when k is large or strings are long.
- **Bucket sort for top-K** — avoids a heap; O(n) instead of O(n log k). Use when you only need rank, not a sorted order.
- **Rolling hash / encode-decode** — prefix each token with its length and a delimiter (`"4#word"`) so you never collide on the delimiter character inside the token.
- **Prefix-sum + HashMap** — for subarray sum problems (e.g. subarray sum equals k), store cumulative sum -> count to answer in one pass.
- **2D board as HashMap key** — Valid Sudoku encodes each cell's value with its row, column, and 3x3 box index as three separate keys in a single set.

## Pitfalls

- **Forgetting `getOrDefault` / using `merge`** — `freq.get(k)` returns `null`, not 0; a NullPointerException on auto-unboxing is the classic bug.
- **Mutating the map while iterating `entrySet()`** — will throw `ConcurrentModificationException`; collect changes and apply after.
- **Using sorted string as anagram key without handling Unicode** — `char[]` sort works for ASCII/Latin; use `codePoints` or a proper locale-aware approach otherwise.
- **Starting consecutive chains at every element** — quadratic without the `!set.contains(n - 1)` guard.
- **Off-by-one in bucket array size** — frequency can equal `nums.length` (all same element); size the bucket array `nums.length + 1`.
- **HashMap vs. HashSet confusion** — use `HashSet` when you only care about membership; `HashMap` when you need the associated value (index, count, etc.).
- **Integer key equality** — `Integer` objects cached only for [-128, 127]; above that range `==` compares references. Always use `.equals()` or let `containsKey` handle it.

## Problems in this pattern

| # | Problem | Key insight |
|---|---------|-------------|
| 1 | Contains Duplicate | HashSet; first repeated element triggers early return |
| 2 | Valid Anagram | Frequency map on s, decrement on t; all zeros = anagram |
| 3 | Two Sum | HashMap complement lookup in one pass |
| 4 | Group Anagrams | sorted-char string (or count array) as grouping key |
| 5 | Top K Frequent Elements | frequency map + bucket sort by count |
| 6 | Encode and Decode Strings | length-prefix encoding: `len + "#" + word` |
| 7 | Product of Array Except Self | prefix + suffix products; no division, no extra array needed |
| 8 | Valid Sudoku | encode seen values as `"r0:5"`, `"c3:5"`, `"b11:5"` in one HashSet |
| 9 | Longest Consecutive Sequence | HashSet + chain from sequence head only |
