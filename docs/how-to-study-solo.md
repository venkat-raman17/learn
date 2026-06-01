# How to study this repo solo (in ~12 weeks)

You're doing this **without a mentor and offline** — so the repo is built to check your own work.
This guide is how to extract the most from it. Pair it with [`../STUDY-PLAN.md`](../STUDY-PLAN.md)
(the week-by-week schedule) and [`../PROGRESS.md`](../PROGRESS.md) (your tracker).

## The one rule: attempt before you read

Every practice artifact has an answer key. The learning happens in the **gap** between your
attempt and the reference — not in reading the reference. So for everything: **struggle first
(time-boxed), then compare.** Reading a solution you didn't attempt feels productive and teaches
almost nothing.

## The loop per artifact

| Artifact | Attempt | Self-check | Learn |
| --- | --- | --- | --- |
| **Data structure** (`dsa-java/.../{linear,trees,…}`) | Read the class once, close it, re-implement from memory | Run the sibling test (`.\mvnw.cmd test`) | Diff your version against the reference; note what you missed |
| **Coding problem** (`coding/{easy,medium,hard}`) | Implement the stub; delete `@Disabled` on its test | `.\mvnw.cmd test` goes green | Read `solutions/<Problem>.java` — compare approach + complexity, even if yours passed |
| **LLD kata** (`backend-service/.../lld`, `web/.../lld`) | Design classes / build the component to satisfy the spec + test | Make the `@Disabled` test pass (backend) / it compiles & behaves (UI) | Read the kata's `SOLUTION.md` and grade yourself with the [rubric](rubrics.md) |
| **HLD case study** (`hld/case-studies`) | Read only the **Prompt**; design on paper for 30–40 min | — | Read the worked study; grade with the [rubric](rubrics.md) |
| **Behavioral** (`behavioral/`) | Draft your STARL stories; answer questions out loud | — | Compare shape against `staff-signals.md` / `question-banks.md` |

## Time-boxing (so you don't rabbit-hole)

- Easy coding: 15 min, then read the solution. Medium: 30–35 min. Hard: 45 min.
- LLD kata: 45–60 min. HLD design: 40 min on paper before reading.
- If you're stuck past the box: read **only the next hint** (in the Javadoc), reset the timer once.
  Still stuck → read the solution, then **redo it from scratch the next day**.

## Spaced repetition (this is what makes it stick)

- Re-implement each **data structure** from a blank file 2–3 days after first learning it.
- **Redo** every coding problem you needed the solution for after ~1 week (mark it in PROGRESS).
- Keep an **error log**: one line per miss ("forgot to handle empty input on X"). Re-read weekly.
- Cycle the **pattern** notes (`dsa/patterns/`): before a problem set, re-read the pattern's trigger.

## Grade your own work

You have no interviewer, so use the [self-assessment rubrics](rubrics.md). After an LLD/HLD/
behavioral attempt, score yourself honestly against the rubric and write the one thing to improve.
A 7/10 you diagnosed beats a 9/10 you assumed.

## A realistic day (~90 min)

1. **Warm-up (10 min):** re-read yesterday's error log + one pattern note.
2. **Core (60 min):** the day's main block from `STUDY-PLAN.md` (e.g., 2 coding problems, or one LLD kata, or one HLD design).
3. **Review (20 min):** compare against the reference, update PROGRESS + error log, jot the one lesson.

On a light day, do only the warm-up + one problem. Consistency beats intensity — a missed day is
fine; a missed week compounds.

## Solo-study traps to avoid

- **Passive reading** — scrolling solutions ≠ studying. Always attempt first.
- **No time-box** — an hour lost on one easy problem is a bad trade; box it and move on.
- **Skipping the redo** — the problem you struggled with is the one worth repeating.
- **Coverage theater** — 150 problems half-understood < 80 problems you can re-derive cold.
- **Ignoring the human rounds** — behavioral/process is ~40% of a Staff loop; don't leave it to week 12.

## If you fall behind

Prioritize, in order: **coding patterns (Phase 2) → HLD framework + 4–5 case studies → behavioral
story bank → LLD core (parking lot, rate limiter, cache) → the rest.** Depth on the essentials
beats a shallow pass over everything.
