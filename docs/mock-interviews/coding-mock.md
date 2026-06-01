# Coding mock

A self-run, timed coding interview simulation. No interviewer. No hints. Full exam conditions.

---

## How to run a 45-minute mock (2 problems)

### Setup (2 min before you start the clock)

- Close all browser tabs except a blank editor or your local IDE.
- Open a physical timer or `timer-tab.com`; set 45 minutes.
- Have a blank scratch sheet (paper or a second file) for diagrams.
- Do **not** open the problem's LeetCode discussion or any solution tab.

### The 45-minute protocol

| Time | Action |
|------|--------|
| 0–3 min | Read Problem 1. Clarify constraints to yourself out loud: input range, edge cases, expected output type. Write them as comments. |
| 3–7 min | **Narrate your approach before writing code.** Say (or write) the pattern name, time/space complexity, and why it fits. If stuck, list two approaches and pick the better one. |
| 7–20 min | Code Problem 1. Keep variable names clean; add a one-line comment per non-obvious block. |
| 20–22 min | Dry-run Problem 1 with one normal case and one edge case (empty input, single element, duplicates). Trace through the code by hand — do not just re-read it. |
| 22–24 min | Transition to Problem 2. Reset; start fresh. |
| 24–27 min | Read + clarify Problem 2 out loud. Write constraints. |
| 27–31 min | Narrate approach for Problem 2. |
| 31–43 min | Code Problem 2. |
| 43–45 min | Dry-run Problem 2. |

**Hard rule: do not open any solution, discussion, or hint until the timer ends.**

### After the timer

1. Open the solution or accepted LeetCode submission.
2. Score yourself using the Coding rubric in [../rubrics.md](../rubrics.md) — one row per problem.
3. Log misses in your running miss log (see [Logging misses](#logging-misses)).
4. If you scored ≤ 3 on Correctness or Optimal Complexity, mark that problem for a grind-first redo within 48 hours before running another mock.

---

## Narrate-before-you-code checklist

Use this mentally at the 3–7 min mark per problem:

- [ ] Named the **pattern** (e.g. "sliding window", "monotonic stack", "BFS on implicit graph").
- [ ] Stated **why** the pattern fits this problem's structure.
- [ ] Called out the **brute-force** and why you are rejecting it.
- [ ] Stated expected **time and space complexity** before writing a line.
- [ ] Identified at least **two edge cases** you will test in the dry-run.

---

## Dry-run checklist (final 2 min per problem)

- Trace with a **concrete example** — not the trivial one from the problem statement.
- Walk through your loop / recursion **by hand**, tracking variable values.
- Check the **termination condition** and any off-by-one boundaries explicitly.
- Ask: "What happens on an empty input? A single-element input? All-duplicate input?"

---

## Mock exam problem sets

These problems are for **fresh test conditions only** — do not use them as your primary grind list. Treat them as unseen. Work through grind problems by pattern first (see [../../STUDY-PLAN.md](../../STUDY-PLAN.md) and the pattern notes under `../dsa/patterns/`), then return here for mock sessions.

Each session = 2 problems, 45 minutes, no hints.

---

### Session A — Arrays / Hashing + Sliding Window

| # | Problem | Difficulty | Pattern |
|---|---------|------------|---------|
| A1 | Longest Consecutive Sequence | Medium | Arrays / Hashing |
| A2 | Minimum Window Substring | Hard | Sliding Window |

Pattern refs: [../dsa/patterns/arrays-hashing.md](../dsa/patterns/arrays-hashing.md), [../dsa/patterns/sliding-window.md](../dsa/patterns/sliding-window.md)

---

### Session B — Binary Search + Stack

| # | Problem | Difficulty | Pattern |
|---|---------|------------|---------|
| B1 | Search in Rotated Sorted Array II | Medium | Binary Search |
| B2 | Largest Rectangle in Histogram | Hard | Monotonic Stack |

Pattern refs: [../dsa/patterns/binary-search.md](../dsa/patterns/binary-search.md), [../dsa/patterns/stack.md](../dsa/patterns/stack.md)

---

### Session C — Trees + Heap

| # | Problem | Difficulty | Pattern |
|---|---------|------------|---------|
| C1 | Binary Tree Maximum Path Sum | Hard | Tree DFS / Post-order |
| C2 | Find Median from Data Stream | Hard | Two Heaps |

Pattern refs: [../dsa/patterns/trees.md](../dsa/patterns/trees.md), [../dsa/patterns/heap-priority-queue.md](../dsa/patterns/heap-priority-queue.md)

---

### Session D — Graphs + Dynamic Programming

| # | Problem | Difficulty | Pattern |
|---|---------|------------|---------|
| D1 | Pacific Atlantic Water Flow | Medium | Multi-source BFS/DFS |
| D2 | Edit Distance | Hard | 2-D DP |

Pattern refs: [../dsa/patterns/graphs.md](../dsa/patterns/graphs.md), [../dsa/patterns/dp-2d.md](../dsa/patterns/dp-2d.md)

---

### Session E — Backtracking + Greedy / Intervals

| # | Problem | Difficulty | Pattern |
|---|---------|------------|---------|
| E1 | Word Search II | Hard | Backtracking + Trie |
| E2 | Non-overlapping Intervals | Medium | Greedy / Intervals |

Pattern refs: [../dsa/patterns/backtracking.md](../dsa/patterns/backtracking.md), [../dsa/patterns/tries.md](../dsa/patterns/tries.md), [../dsa/patterns/intervals.md](../dsa/patterns/intervals.md)

---

## Self-scoring after each mock

Score each problem 1–5 on every dimension from [../rubrics.md](../rubrics.md):

| Dimension | Score (1–5) | Notes |
|-----------|-------------|-------|
| Correctness | | |
| Optimal complexity | | |
| Pattern recognition | | |
| Code clarity | | |
| Communication (narration) | | |

**Staff bar:** average 4+ with nothing at a 2.

---

## Logging misses

Maintain a file `miss-log.md` in this directory. One row per missed problem:

```
| Date | Problem | What I missed | Pattern to re-read | Status |
| --- | --- | --- | --- | --- |
| 2026-06-01 | Edit Distance | Forgot base-case row init | dp-2d.md | redo |
```

After two consecutive clean solves of a missed problem in a timed setting, mark it `done`.

---

## Recommended session cadence

- Run one mock session **per week** once your grind-first list is 60%+ covered.
- Do not run two mock sessions back-to-back; space them to let pattern review fill the gap.
- After all five sessions, rotate a new unseen problem set in from LeetCode contest problems or company-tagged problems — same protocol applies.

See [../how-to-study-solo.md](../how-to-study-solo.md) for the broader weekly schedule and pacing advice.
