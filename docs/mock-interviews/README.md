# Mock interviews — how to run them solo

This kit is designed for solo, offline prep. No interviewer, no network required.
Everything here is a repeatable ritual you run on yourself.

---

## Why mocks matter

Reading patterns and solving problems at your own pace builds knowledge.
Mocks build something different: the ability to perform under constraint.

- **Simulated pressure** — a hard countdown changes how your brain works. You will blank, spiral, and recover. That recovery muscle only develops under pressure.
- **Communication habit** — narrating forces you to externalise half-formed thoughts. Gaps in your explanation reveal gaps in understanding, not just gaps in memory.
- **Time calibration** — you learn whether you spend 8 minutes on clarification or 2. You cannot calibrate this by reading.
- **Failure rehearsal** — getting stuck in a mock costs nothing. Getting stuck in a real interview costs everything.

Do not skip mocks because you feel underprepared. Do them *because* you are underprepared.

---

## How to self-mock realistically

### Setup rules (non-negotiable)

| Rule | Why |
|---|---|
| Set a hard timer (45 min coding, 40 min system design, 30 min behavioural) | Mirrors real slots; trains pacing |
| Narrate out loud — or record yourself speaking | Forces externalisation; reveals gaps |
| Write on paper or a plain text editor with no autocomplete | Closest to a whiteboard or locked-down IDE |
| No peeking at the solution until the timer ends | Peeking resets all pressure; defeats the exercise |
| Grade yourself immediately after, while memory is fresh | Delayed grading loses detail |

### Narrating out loud

- State the problem back in your own words before touching code.
- Say your constraints aloud ("I'm assuming integers fit in 32 bits…").
- Announce your approach before you implement it.
- Talk through edge cases even if you do not handle all of them.
- If you are stuck, narrate the stuck state ("I need O(n) but my current approach is O(n²); let me think about what structure gives me O(1) lookup…").

Recording a voice memo and listening back once per week is high-signal: you will hear filler, long silences, and confidence drops you cannot perceive in the moment.

### Environment checklist

- [ ] Phone/computer timer visible, not buried in a tab
- [ ] Paper or a blank `.txt` / `.md` file open — nothing else
- [ ] Solutions folder closed or in a separate window you cannot see
- [ ] Notifications silenced

---

## Phase-6 week schedule (one mock per day)

This schedule rotates round types across six weeks to avoid rut-drilling.
One mock per day, five days per week. Use weekends for retros and catch-up.

See [../../STUDY-PLAN.md](../../STUDY-PLAN.md) for the broader phase breakdown this schedule sits inside.

| Week | Mon | Tue | Wed | Thu | Fri |
|---|---|---|---|---|---|
| 1 | Coding — arrays/strings | System design — storage | Behavioural | Coding — trees/graphs | System design — read-heavy API |
| 2 | Coding — dynamic prog. | Behavioural | Coding — heaps/queues | System design — real-time | Coding — mixed |
| 3 | System design — distributed | Coding — hard (LC-hard tier) | Behavioural | Coding — sliding window | System design — write-heavy |
| 4 | Coding — mixed | System design — search/index | Behavioural | Coding — graphs (advanced) | Full loop (coding + design back-to-back) |
| 5 | Coding — hard | System design — staff-level scope | Behavioural | Coding — hard | System design — staff-level scope |
| 6 | Full loop | Full loop | Behavioural (leadership-heavy) | Full loop | Full loop |

**Full loop** = 45 min coding + 10 min break + 40 min system design in a single sitting.
Week 6 is deliberately the hardest week. Do not save energy.

---

## Scoring every mock

Score immediately after the timer ends using the rubric in [../rubrics.md](../rubrics.md).

### Quick-score dimensions (rate each 1–4)

| Dimension | 1 — Missed | 2 — Partial | 3 — Solid | 4 — Strong |
|---|---|---|---|---|
| Problem clarification | Did not ask questions | Asked one surface question | Covered constraints + I/O | Covered edge cases, probed ambiguity |
| Approach before code | Jumped straight to code | Named approach only | Explained trade-offs | Named alternatives, justified choice |
| Correctness | Wrong or incomplete | Passes happy path | Handles main edge cases | All cases covered, proved correct |
| Efficiency | No analysis | Named complexity incorrectly | Correct complexity stated | Optimised + explained why |
| Communication clarity | Silent or chaotic | Occasional narration | Consistent narration | Clear, confident, no dead air |
| Time management | Ran out before coding | Coded but no time to test | Tested with 3–5 min left | Tested and iterated |

Sum = 6–24. Target 18+ before scheduling real interviews.

For system design rounds, use the design-specific rubric section in [../rubrics.md](../rubrics.md).

---

## Retro — what to fix

After scoring, write three lines in your retro log before closing the session.

```
Date: 2026-06-03 | Round: Coding — graphs
Score: 14/24
What broke: Spent 12 min on clarification, rushed implementation, missed the disconnected-graph edge case.
Fix: Cap clarification at 5 min. Add "disconnected / empty input" to pre-code checklist.
Next mock: Graphs again (repeat until 18+).
```

Keep retro notes in a single file: `retro-log.md` in this folder.
Do not move on from a round type until you score 18+ twice in a row.

---

## Round guides in this folder

Each file is a self-contained guide for that round type.

| File | Round |
|---|---|
| [coding-rounds.md](coding-rounds.md) | Algorithm and data-structure rounds |
| [system-design.md](system-design.md) | System design — component, capacity, trade-off walk |
| [behavioural.md](behavioural.md) | Leadership, conflict, impact — STAR structure |
| [staff-scope.md](staff-scope.md) | Staff-specific: ambiguity, org influence, multi-team problems |

Pattern notes live in [../../patterns/](../../patterns/) and case study write-ups in [../../case-studies/](../../case-studies/).
Reference them during prep, not during the mock itself.

---

## Optional peer and online options

These require an internet connection. Treat them as supplements, not replacements.

- **Pramp** (pramp.com) — free peer-to-peer mocks, scheduled in advance. Good for getting honest feedback from a stranger.
- **interviewing.io** (interviewing.io) — anonymous mocks with engineers from top companies. Paid tier for recorded replays.
- **Blind / Leetcode discuss** — post your approach after a mock and ask for critique. Async, not real-time pressure, but useful for design trade-off feedback.

All three are internet-dependent and require account setup ahead of time.
Do not block your prep on them. Solo mocks here come first.

---

## Quick-start checklist for your first mock

- [ ] Pick a problem from the relevant round guide (do not browse — pick one and commit)
- [ ] Set the timer
- [ ] Open a blank editor or grab paper
- [ ] Narrate from problem restatement through solution
- [ ] Stop when the timer ends, even mid-sentence
- [ ] Score using [../rubrics.md](../rubrics.md)
- [ ] Write the three-line retro
- [ ] Close the solutions file

That is the full loop. Repeat it tomorrow.
