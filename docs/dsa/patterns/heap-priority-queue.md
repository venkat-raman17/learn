# Heap / Priority Queue

## When to reach for it

If you see any of these, consider a heap:

- "Find the **k-th largest / smallest**" element in a stream or array.
- "Return the **top-k** elements" by some comparator.
- "Process items in **priority order**" (highest urgency first, earliest deadline first).
- "Running **median**" — need both the low half and high half of a sorted stream efficiently.
- "Greedily pick the best available** option at each step" where the pool of candidates grows/shrinks dynamically.
- You see the word **"stream"** combined with an order statistic query.
- A greedy solution needs an `O(log n)` insert + `O(log n)` extract-min/max instead of a full sort each round.

---

## The idea

A heap gives you `O(log n)` insert and `O(log n)` extract of the minimum (min-heap) or maximum (max-heap), with `O(1)` peek. The canonical trick is a **size-k min-heap to track the k largest elements**: every new element either evicts the current minimum or is itself discarded, so after one pass you have exactly the k largest in `O(n log k)` time. A **two-heap split** (max-heap for the lower half, min-heap for the upper half) solves running-median problems in `O(log n)` per insertion.

**Typical complexity:** `O(n log k)` time, `O(k)` space for fixed-window problems; `O(n log n)` time, `O(n)` space when the full set lives in the heap.

---

## Template

```java
import java.util.PriorityQueue;

// ── Min-heap (default in Java) ──────────────────────────────────────────────
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// ── Max-heap ─────────────────────────────────────────────────────────────────
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// ── Heap of custom objects, sorted by a field ─────────────────────────────
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]); // sort by index 0

// ── Core operations ───────────────────────────────────────────────────────
pq.offer(val);      // insert  — O(log n)
pq.poll();          // remove + return top — O(log n); returns null if empty
pq.peek();          // view top — O(1); returns null if empty
pq.size();
pq.isEmpty();

// ── Pattern: keep a size-k min-heap of the k LARGEST elements ─────────────
PriorityQueue<Integer> kLargest = new PriorityQueue<>(); // min-heap
for (int num : nums) {
    kLargest.offer(num);
    if (kLargest.size() > k) {
        kLargest.poll(); // evict the smallest of the k+1 candidates
    }
}
// kLargest.peek() == kth largest element

// ── Pattern: two-heap running median ─────────────────────────────────────
PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
PriorityQueue<Integer> hi = new PriorityQueue<>();                            // min-heap

void addNum(int num) {
    lo.offer(num);
    hi.offer(lo.poll());          // balance: push lo's max into hi
    if (hi.size() > lo.size()) {  // keep lo.size() >= hi.size()
        lo.offer(hi.poll());
    }
}

double getMedian() {
    return lo.size() == hi.size()
        ? (lo.peek() + hi.peek()) / 2.0
        : lo.peek();
}

// ── Pattern: task scheduling / greedy with heap ───────────────────────────
// 1. Build frequency map.
// 2. Push all frequencies into a max-heap.
// 3. Each round: pop up to `n+1` tasks, decrement counts, re-add non-zero.
```

---

## Variations

- **K Closest Points** — heap on Euclidean distance; no need to sort the full array. Use a size-k max-heap and evict the farthest, or a min-heap + poll k times.
- **Merge k sorted lists** — min-heap of `(value, listIndex, nodeIndex)` triples; pop and push the next node from the same list.
- **Task Scheduler** — max-heap of task frequencies + a cooldown queue (or a deque acting as a wait buffer). Greedy: always schedule the most-frequent available task.
- **Design Twitter** — per-user tweet lists stored in descending time order; merge-k-sorted-lists pattern to produce a feed.
- **Dijkstra / Prim** — min-heap on `(cost, node)`; standard graph shortest-path building block.
- **Sliding-window max/min** — a monotonic deque is often better here; a heap works but requires lazy deletion.

---

## Pitfalls

1. **Min vs max confusion.** Java's `PriorityQueue` is a **min-heap** by default. For max-heap you must pass `Collections.reverseOrder()` or `(a, b) -> b - a`. Getting this backwards silently produces wrong answers.

2. **Integer overflow in comparators.** `(a, b) -> a - b` overflows for large negatives. Prefer `Integer.compare(a, b)` or `(a, b) -> a[0] - b[0]` only when values are known to be bounded.

3. **Heap doesn't support O(1) arbitrary removal.** If you need to remove an arbitrary element (e.g., sliding-window problems), you either use `remove(obj)` in `O(n)` or implement lazy deletion with a "tombstone" counter.

4. **Forgetting to re-offer after modification.** Java's heap does not re-heapify when you mutate an element inside the queue. Remove, modify, re-insert.

5. **Off-by-one on k.** "Kth largest" means after inserting k elements, the heap minimum is your answer. Inserting `k+1` elements and polling once is the clean idiom.

6. **Two-heap imbalance.** The invariant must be `lo.size() == hi.size()` (even count) or `lo.size() == hi.size() + 1` (odd count). Rebalance after every insertion, not just when the size difference is > 1.

---

## Problems in this pattern

| Problem | Key insight |
|---|---|
| **Kth Largest in a Stream** | Size-k min-heap; `peek()` is always the answer after each add. |
| **Last Stone Weight** | Max-heap; smash top two, re-insert difference if non-zero. |
| **K Closest Points to Origin** | Size-k max-heap on distance; evict farthest, poll remaining k. |
| **Kth Largest in an Array** | Same as stream but single pass; or `QuickSelect` for O(n) avg. |
| **Task Scheduler** | Max-heap of frequencies + cooling period; count idle slots. |
| **Design Twitter** | Per-user tweet deques + merge-k-sorted at `getNewsFeed` time. |
| **Find Median from Data Stream** | Two-heap split: max-heap (lo) + min-heap (hi), rebalance each add. |
