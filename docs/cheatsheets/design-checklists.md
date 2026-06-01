# Design & round checklists

Run these in the room so you don't forget a step under pressure.

## Coding round (UMPIRE)
1. **Understand** — restate the problem; ask about input ranges, dupes, empties, output format.
2. **Match** — what pattern is this? (see [pattern-triggers](pattern-triggers.md))
3. **Plan** — state the approach + complexity *before* coding; get a nod.
4. **Implement** — clean names, small helpers, talk as you go.
5. **Review** — dry-run a small example + an edge case line by line.
6. **Evaluate** — final time/space; can you do better? what breaks at scale?

Edge cases to always consider: empty · single element · duplicates · all-same · negative/zero · max size · already-sorted/reverse.

## LLD round
- [ ] Clarify functional + non-functional (concurrency? persistence? scale?); state assumptions.
- [ ] Pull out the **entities** (nouns) → classes/enums.
- [ ] Relationships + cardinality; **composition over inheritance**.
- [ ] Class diagram: fields + key methods; interfaces for the **varying** parts.
- [ ] Define the **public API** (method signatures).
- [ ] Apply patterns deliberately (Strategy/Factory/State/Observer…) — justify each.
- [ ] **Concurrency** + nasty edge cases (the race for the last resource).
- [ ] **Extensibility**: "to add X, I only touch…" (Open/Closed).

## HLD round
- [ ] Requirements: functional + **non-functional** (scale, read:write, latency, consistency, durability).
- [ ] **Estimate** QPS / storage / bandwidth (back-of-envelope).
- [ ] **API** (key endpoints) + **data model** (SQL vs NoSQL from access patterns).
- [ ] High-level architecture: draw it; show the **read path and write path**.
- [ ] Deep dives on the genuinely hard parts; weigh ≥2 options.
- [ ] Find the **bottleneck**; address it (cache / shard / replicate / queue / CDN) — name the cost.
- [ ] **Don't forget:** idempotency · observability (metrics/logs/traces) · failure modes · cost · multi-region.
- [ ] State the **2–3 biggest trade-offs** explicitly (the top Staff signal).

## Behavioral round
- [ ] **STARL**: Situation, Task, Action, Result, Learning — ~2–3 min, no rambling.
- [ ] Lead with **scope + quantified impact**; foreground *your* decisions (I, not we).
- [ ] Show **influence without authority** and a real **learning**.
- [ ] Map each answer to a [Staff signal](../behavioral/staff-signals.md) + a [story](../behavioral/story-bank.md).
