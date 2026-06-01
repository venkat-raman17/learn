# Intervals

## When to reach for it

If you see any of the following, think Intervals:

- Input is a list of `[start, end]` pairs (meetings, tasks, ranges).
- Asked to merge, insert, or remove overlapping ranges.
- Asked to count concurrent events or find the minimum number of "rooms" / "resources".
- Asked to find gaps, covers, or which intervals contain a point/query.
- Constraint involves "non-overlapping" or "minimum removals to make non-overlapping".

Key tell: the problem makes no sense without knowing both endpoints of each range.

---

## The idea

Sort intervals by start time (sometimes by end time for greedy removal problems). Then make a single left-to-right pass, comparing the current interval's start against the previous interval's end to decide whether they overlap (`cur.start <= prev.end`). For merge-style problems, extend the previous end; for counting-style problems (Meeting Rooms II), use a min-heap tracking the earliest-ending active interval and grow/shrink a pool. Sorting is O(n log n); the sweep is O(n), giving **O(n log n) time, O(n) or O(1) extra space** depending on variant.

---

## Template

```java
import java.util.*;

public class IntervalsTemplate {

    // --- Merge overlapping intervals ---
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // sort by start
        List<int[]> result = new ArrayList<>();

        for (int[] cur : intervals) {
            if (result.isEmpty() || result.get(result.size() - 1)[1] < cur[0]) {
                // no overlap: gap between prev.end and cur.start
                result.add(cur);
            } else {
                // overlap: extend prev.end if needed
                result.get(result.size() - 1)[1] =
                    Math.max(result.get(result.size() - 1)[1], cur[1]);
            }
        }
        return result.toArray(new int[0][]);
    }

    // --- Count minimum rooms (concurrent intervals) via min-heap ---
    public int minRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // sort by start
        PriorityQueue<Integer> endTimes = new PriorityQueue<>(); // min-heap of end times

        for (int[] cur : intervals) {
            if (!endTimes.isEmpty() && endTimes.peek() <= cur[0]) {
                endTimes.poll(); // reuse the room that freed up earliest
            }
            endTimes.offer(cur[1]);
        }
        return endTimes.size(); // rooms still occupied = answer
    }

    // --- Insert interval into sorted, non-overlapping list ---
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;

        // 1. Add all intervals that end before newInterval starts
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i++]);
        }
        // 2. Merge all overlapping intervals into newInterval
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);
        // 3. Append remaining intervals
        while (i < n) result.add(intervals[i++]);

        return result.toArray(new int[0][]);
    }

    // --- Minimum removals to make non-overlapping (sort by END, greedy keep) ---
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]); // sort by end
        int removals = 0;
        int prevEnd = Integer.MIN_VALUE;

        for (int[] cur : intervals) {
            if (cur[0] >= prevEnd) {
                prevEnd = cur[1]; // keep this interval
            } else {
                removals++;       // discard current (it overlaps and ends later)
            }
        }
        return removals;
    }
}
```

---

## Variations

| Twist | Adjustment |
|---|---|
| **Sort by end instead of start** | Greedy "keep as many as possible" — pick the interval that ends earliest and never conflicts (Non Overlapping Intervals). |
| **Query point/range against intervals** | Sort intervals + binary search, or use a min-heap ordered by end time and sweep queries in sorted order (Minimum Interval to Include Each Query). |
| **Insert into live sorted list** | Three-phase linear scan: copy left non-overlapping, merge overlapping, copy right (Insert Interval). |
| **Count overlapping at a single point** | Separate starts and ends into two sorted arrays; sweep with a counter (+1 on start, -1 on end). |
| **Interval covering / minimum intervals to cover range** | Greedy: at each position pick the interval that starts <= current position and reaches farthest. |

---

## Pitfalls

- **Forgetting to sort first.** Every interval algorithm assumes sorted order; skipping this produces wrong answers silently.
- **Off-by-one on overlap check.** `[1,3]` and `[3,5]` — do they overlap? Depends on whether endpoints are inclusive. In most LC problems they do (`cur.start <= prev.end` is the overlap condition).
- **Sorting by wrong key.** Merge and insert: sort by start. Non-overlapping greedy: sort by end. Mixing these up gives wrong answers.
- **Mutating the input array in-place carelessly.** Especially in Insert Interval — safer to build a new list.
- **Heap not polled before offer in Meeting Rooms II.** Always check `peek() <= cur.start` and poll before offering; otherwise room count inflates.
- **Integer overflow.** If endpoints are large (e.g., `10^9`), comparator `(a, b) -> a[0] - b[0]` can overflow. Use `Integer.compare(a[0], b[0])` instead.

---

## Problems in this pattern

| Problem | Key insight |
|---|---|
| **Insert Interval** | Three-phase linear scan; no sort needed (input already sorted). |
| **Merge Intervals** | Sort by start, greedy extend. |
| **Non Overlapping Intervals** | Sort by end, count intervals that must be removed to keep greedy max-keep set. |
| **Meeting Rooms** | Sort by start, check if any consecutive pair overlaps — return boolean. |
| **Meeting Rooms II** | Sort by start, min-heap of end times tracks concurrent rooms needed. |
| **Minimum Interval to Include Each Query** | Sort intervals by length (smallest first) + sort queries; sweep with a min-heap keyed by interval end, evict stale entries per query. |
