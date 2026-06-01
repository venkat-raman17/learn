# Self-assessment rubrics

No interviewer? Grade yourself. After each LLD / HLD / behavioral / coding attempt, score each
dimension **1–5**, then write the **single most important thing to improve** — that line is the
whole point. Be honest; inflating scores only fools you. Staff bar ≈ averaging **4+** with nothing
at a 2.

> 1 = missed it · 2 = shaky · 3 = competent (Senior) · 4 = strong (Staff) · 5 = exceptional (Principal)

---

## Coding (per problem)

| Dimension | What 4–5 looks like |
| --- | --- |
| **Correctness** | Passes all cases including edges (empty, single, dup, overflow, boundaries) on the first serious attempt. |
| **Optimal complexity** | You reach the expected time/space, and can state both and *why* — not just "it works". |
| **Pattern recognition** | You named the pattern within ~2 min and knew why it applied. |
| **Code clarity** | Clean names, small helpers, no dead branches; readable under interview pressure. |
| **Communication** | You narrated the approach and trade-offs before coding, not after. |

**Redo trigger:** any problem where you needed the solution, or scored ≤3 on correctness/complexity.

---

## LLD (per kata)

| Dimension | What 4–5 looks like |
| --- | --- |
| **Requirements & scope** | Clarified functional + non-functional, stated assumptions, bounded scope before designing. |
| **Class model** | Cohesive classes, composition over inheritance, low coupling; the model reads like the domain. |
| **Patterns (deliberate)** | Used the right pattern(s) for the *varying* parts and can justify each — not pattern soup. |
| **Extensibility** | "Add a new type/rule" = a new class, no edits to existing code (Open/Closed). You predicted the follow-up. |
| **Concurrency & edges** | Identified shared mutable state and guarded it; handled the nasty edge cases. |
| **API design** | Minimal, intention-revealing public API; internals hidden. |

Compare against the kata's `SOLUTION.md` and the [LLD framework](lld/framework.md).

---

## HLD (per case study)

| Dimension | What 4–5 looks like |
| --- | --- |
| **Requirements & scoping** | Separated functional/non-functional, pinned scale/latency/consistency, asked the clarifying questions. |
| **Estimates** | Back-of-envelope QPS/storage/bandwidth with the arithmetic; used them to drive decisions. |
| **Architecture** | Clear components, explicit read + write paths; nothing hand-waved. |
| **Deep dives** | Went deep on this system's genuinely hard parts; weighed ≥2 options and chose with reasons. |
| **Bottlenecks & scaling** | Found the limiting resource and addressed it (cache/shard/replicate/queue/CDN) with the cost named. |
| **Trade-offs** | Stated the 2–3 biggest decisions and what each trades — the #1 Staff signal. |
| **2026 topics** | Wove in idempotency, observability/cost, multi-region (and vector-DB/LLM/agentic when relevant). |

Compare against the worked study and the [HLD framework](hld/framework.md).

---

## Behavioral (per story / answer)

| Dimension | What 4–5 looks like |
| --- | --- |
| **Structure (STARL)** | Crisp Situation→Task→Action→Result→Learning; no rambling; ~2–3 min. |
| **Scope & impact** | Org/company-level scope, quantified impact ("cut p99 40%", "aligned 4 teams") — not a solo feature. |
| **Your role** | Clear *I*-not-*we* on the decisions you owned; the hard call is visible. |
| **Influence** | Shows influence without authority — you moved people who initially disagreed. |
| **Reflection** | A genuine learning, ideally one you later applied. |

Map every answer to a [Staff signal](behavioral/staff-signals.md) and a story in your
[story bank](behavioral/story-bank.md).

---

## Weekly self-review (10 min, Sundays)

1. Average your rubric scores for the week — which **dimension** is lowest across artifacts?
2. That dimension is next week's focus. Write it at the top of `PROGRESS.md`.
3. Re-read your error log; pick 2 items to redo.
