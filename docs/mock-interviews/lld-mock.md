# LLD mock

Self-run, no interviewer. Run this kit to simulate a real 45–60 min LLD round.
See [../rubrics.md](../rubrics.md) for scoring and [../../STUDY-PLAN.md](../../STUDY-PLAN.md)
for where mocks fit (Phase 6, week 12).

---

## How to run a solo mock

### Setup (2 min before you start)
- Pick one prompt from the list below. Do NOT look at it more than 5 seconds before starting the timer.
- Open a blank doc or whiteboard. Have a timer visible.
- Say everything out loud — narrate every decision as if explaining to a panel.

### The 45–60 min arc

| Phase | Time | What to do out loud |
|---|---|---|
| **1. Clarify** | 0–5 min | Ask the 3–4 questions you'd ask an interviewer. State functional reqs, non-functional (concurrency? persistence? scale?), and explicit scope boundaries. Write them down. |
| **2. Entities** | 5–12 min | List the core nouns. Name each class/interface/enum. Decide is-a vs has-a. Prefer composition. Call out what *varies* — those become interfaces. |
| **3. Class diagram** | 12–25 min | Sketch fields, method signatures, visibility. Draw relationships (1-to-many, composition arrows). Use interfaces/abstract classes for every extension point. |
| **4. Key code** | 25–38 min | Implement 1–2 non-trivial methods in full. Omit boilerplate — write the algorithm, not getters. Show the pattern you chose (name it). |
| **5. Concurrency** | 38–45 min | Identify every piece of shared mutable state. State how it is guarded — lock, ConcurrentHashMap, atomic CAS, compare-and-swap, etc. Describe the race condition that would occur without it. |
| **6. Extensibility** | 45–52 min | Predict and answer 1–2 follow-up extensions ("now add X"). Show that adding X means a new class, not editing existing code (Open/Closed). Summarise your design in 3 sentences. |

**Strict time-boxing:** use a phone timer. Move forward even if a phase feels incomplete — that is
the real interview condition.

### After the mock
1. Score yourself on every LLD dimension in [../rubrics.md](../rubrics.md). Be honest.
2. Write one sentence: the single most important thing to improve.
3. Re-read [../lld/framework.md](../lld/framework.md) for any step that scored ≤ 3.
4. Log it in `PROGRESS.md`.

---

## Fresh LLD prompts

These 8 problems are **not in the existing practice katas** (parkinglot, ratelimiter, cache, vendingmachine,
elevator, splitwise, notification, logger). See [../lld/README.md](../lld/README.md) for the katas.

| # | Prompt | What makes it interesting |
|---|---|---|
| 1 | **Chess** | Deep state machine per piece type; polymorphic `isValidMove`; turn management; check/checkmate detection couples the whole board — hard to isolate. |
| 2 | **Library Management System** | Multiple actor roles (member, librarian); fine calculation requires a Clock abstraction; reservation queue needs a fair scheduler; good test of SRP across `Book`, `Copy`, `Loan`, `Member`. |
| 3 | **In-memory Key-Value DB** | Forces you to design a clean API (`get/set/delete/expire`) and then layer TTL eviction, optional transactions, and CAS — concurrency is the whole exercise. |
| 4 | **Snake & Ladder** | Deceptively simple: the interesting move is separating `Board` (static), `Game` (state machine), and `Cell` decoration (snakes/ladders as a Strategy/Map), plus making it testable with a mock dice. |
| 5 | **Meeting-room Scheduler** | Interval-overlap detection is the core algorithm; the design must expose a clean booking API, handle double-booking atomically, and support cancellation without corrupting the calendar. |
| 6 | **Food-delivery Platform** | Rich entity graph (Restaurant → Menu → Item, Order, DeliveryAgent); status transitions via State pattern; real-time matching of orders to agents; notification fan-out with Observer. |
| 7 | **ATM** | Classic State machine (Idle → CardInserted → PINVerified → SelectTransaction → Dispensing); cash-dispenser uses Chain of Responsibility for denominations; network timeout & retry handling. |
| 8 | **Online Auction** | Concurrent bid acceptance (CAS or optimistic locking); auction lifecycle as a State machine; extensibility for English vs Dutch auctions via Strategy; notification to losing bidders with Observer. |

### Quick start for any prompt
1. Treat the one-liner above as your only input — you must clarify the rest.
2. Run the full 6-phase arc.
3. After the mock, skim [../lld/design-patterns.md](../lld/design-patterns.md) to check whether you
   chose the right patterns and named them correctly.

---

## Self-score with the LLD rubric

Paste this table into your notes after each mock and fill in scores (1–5).

| Dimension | Score | Note |
|---|---|---|
| Requirements & scope | | Did you clarify FRs + NFRs, state assumptions, bound scope before designing? |
| Class model | | Cohesive classes, composition > inheritance, low coupling? |
| Patterns (deliberate) | | Named the right pattern(s) and justified each? Not pattern soup? |
| Extensibility | | "Add new type/rule" = new class, no edits to existing code? |
| Concurrency & edges | | Found shared mutable state, described the guard, named the race condition? |
| API design | | Minimal, intention-revealing public surface, internals hidden? |

Full rubric detail and the Staff bar (avg 4+, nothing at 2): [../rubrics.md](../rubrics.md).

---

## Common failure modes to watch for

- **Jumping to code in phase 2** — you cannot sketch a diagram until you have finished naming entities.
- **Every class is concrete** — if nothing is an interface, nothing is extensible.
- **Concurrency as an afterthought** — say "I'll come back to thread-safety" and then run out of time.
- **Pattern soup** — applying 4 patterns to look thorough; be able to remove any one and justify why each stays.
- **Passive narration** — describing what you are drawing instead of *why*. "I'm making Bid an interface *because* auction types extend differently" beats "here is Bid."
