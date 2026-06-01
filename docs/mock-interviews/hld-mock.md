# HLD / system-design mock

Self-run kit for a solo 60-min session — no interviewer, just you, blank paper, and a timer.
Keep this tab open; run a prompt from the list below; grade yourself at the end.

See also: [rubrics](../rubrics.md) · [HLD framework](../hld/framework.md) · [study plan](../../STUDY-PLAN.md)

---

## How to run a 60-min session

### Setup (2 min)

- Pick one prompt from the bank below.
- Clear your desk. Paper and pen only — no IDE, no Google.
- Set six timer alarms at: 8, 13, 18, 23, 38, 55, 60 min.

### The six phases (draw on paper throughout)

| Phase | Clock target | What to produce |
|---|---|---|
| **1. Requirements** | 0 → 8 min | Two columns: Functional (3–5 bullets) + Non-functional (scale, latency SLA, consistency, durability). State assumptions aloud. |
| **2. Estimates** | 8 → 13 min | QPS = DAU × actions / 86 400 × peak factor. Storage/day = writes × avg object size. Bandwidth. Memory for hot cache. One rough number per line. |
| **3. API design** | 13 → 18 min | 3–5 core endpoints with method, path, key params. Note pagination style, idempotency keys, auth mechanism. |
| **4. Data model** | 18 → 23 min | Core entities, key fields, primary + secondary access patterns. Choose store type (SQL / NoSQL / blob / time-series) and justify from the access pattern, not from taste. |
| **5. Architecture** | 23 → 38 min | Box diagram: clients → LB → services → cache → DB(s) → queues → async workers. Draw the **write path** and **read path** as two separate arrows. |
| **6. Deep dives + trade-offs** | 38 → 55 min | Pick the 2–3 hardest sub-problems for this prompt. Go deep: sketch two approaches, state the constraint that drives the choice, name the cost. Close with a 1-paragraph verbal recap of the big trades. |
| **Wrap + self-score** | 55 → 60 min | Score the rubric below while your reasoning is still fresh. Write the one thing to fix. |

### Drawing discipline

- Use a fresh A4 sheet per phase; label each.
- Boxes = services. Cylinders = stores. Arrows = data flow (annotate with payload name).
- Never erase — cross out and redraw. Real interviews are messy; train messy.

---

## Prompt bank (8 fresh systems)

These do not overlap with the 12 worked case studies
(`../hld/case-studies/`: ad-click-aggregator, chat, distributed-message-queue,
distributed-rate-limiter, file-storage, news-feed, rideshare, typeahead, unique-id-generator,
url-shortener, video-streaming, web-crawler).

| # | Prompt | Crux — the one hardest sub-problem |
|---|---|---|
| 1 | **Stock exchange / matching engine** | The order book must be a deterministic, single-threaded in-memory structure for microsecond matching; durability and fan-out of fills are asynchronous concerns. |
| 2 | **Ticketmaster (event ticketing)** | Inventory reservation under a thundering herd: you need a short-lived hold (e.g., 10-min lock) without overselling, which means optimistic-locking or a seat-claim queue rather than naive decrement. |
| 3 | **Distributed job scheduler / cron** | Exactly-once execution across workers: the hardest part is leader election for the scheduler and idempotent, fencing-token-guarded dispatch so a network partition cannot double-fire a job. |
| 4 | **Payment system** | Every step in the payment saga (auth → capture → settle → notify) must be idempotent and retryable; the core pattern is an outbox table + exactly-once delivery, not optimistic HTTP retries. |
| 5 | **Google Docs collaborative editor** | Real-time convergence: concurrent edits from N clients must merge without data loss, which forces either Operational Transformation or CRDTs; the choice defines latency, conflict complexity, and server statefulness. |
| 6 | **Online code judge (LeetCode-style)** | Secure, resource-bounded, multi-language execution: sandboxing (container per submission) with hard CPU/mem/time limits, followed by async diff-against-expected-output grading at scale. |
| 7 | **Price-alert system** | Matching millions of user-defined threshold rules against a high-frequency price tick stream without per-tick full-table scans: invert the index (price bucket → alert list) or use a sorted set. |
| 8 | **Multiplayer game server** | State synchronisation at low latency: authoritative server with client-side prediction and server reconciliation (rollback); pick UDP + custom reliability or WebSocket; regional server placement drives the latency budget. |

---

## Per-prompt starter questions to ask yourself

Before you draw anything, spend 30 seconds on these — they unlock the right decisions:

1. What is the **read:write ratio** and does it skew the storage/caching choice?
2. What is the **consistency requirement** — can users see stale data, or is a stale read a business bug?
3. Where is the **hot path** — a single entity (celebrity, trending event, a particular stock symbol) that could overwhelm a naive design?
4. What does **failure** look like and what is the blast radius? (A failed job vs. a double-charged payment are very different.)
5. What is the **latency SLA** and does it rule out synchronous cross-DC calls?

---

## Self-score (HLD rubric)

Score each dimension 1–5 immediately after the session. Staff bar = average 4+, nothing at 2.
Full rubric detail lives in [../rubrics.md](../rubrics.md).

| Dimension | 1–2 (miss) | 3 (competent / Senior) | 4–5 (strong / Staff) | Your score |
|---|---|---|---|---|
| **Requirements & scoping** | Dived into design; missed NFRs | Covered functional; vague on scale/latency | Pinned DAU, QPS, latency SLA, consistency model before drawing | /5 |
| **Estimates** | Skipped or wildly off | Rough numbers, no derivation | Derived QPS from DAU × actions; used numbers to justify cache size or DB choice | /5 |
| **API design** | No API discussed | Endpoints listed, no pagination/idempotency | Idempotency key on writes, cursor pagination, auth noted; gRPC vs REST justified | /5 |
| **Data model** | No entities; store chosen by feel | Entities listed; store unjustified | Access patterns drove store choice; sharding/partition key named | /5 |
| **Architecture** | One magic box | Components present; paths vague | Separate read/write paths drawn; async workers + queue shown; failure paths named | /5 |
| **Deep dives** | Surface-level; no options weighed | One approach explored | Two options compared; limiting constraint stated; choice justified with cost | /5 |
| **Trade-offs** | No trade-offs stated | One trade-off mentioned | 2–3 explicit trades stated (e.g., "write amplification for read speed"); cost of each named | /5 |
| **2026 topics** | None | One mentioned | Idempotency + observability + multi-region woven in naturally where relevant | /5 |

**Total: /40**

After scoring, write one sentence:

> "The single most important thing to improve next session: ___"

Log it in `../../PROGRESS.md` under today's date.

---

## Anti-patterns to watch for

- Jumping to a box diagram before finishing requirements — restart the clock if you catch yourself.
- Saying "we'll use Kafka" without stating *why* (async decoupling? fan-out? durability?).
- Picking a datastore by name-recognition rather than access pattern.
- Finishing without a monitoring/alerting box and no mention of cost-per-request.
- Treating "add a cache" as a free lunch — always name the invalidation strategy and the staleness window.

---

## Recommended rotation

Run one prompt per week. Pair with the pattern notes in [`../hld/core-concepts.md`](../hld/core-concepts.md)
for the relevant patterns (e.g., run Prompt 4 the same week you review idempotency + outbox;
run Prompt 5 the same week you review CRDT / OT). Track which prompts you have attempted in
`../../PROGRESS.md` so you do not repeat before you have done all eight.
