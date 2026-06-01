# Study Plan — Full-Stack Staff Engineer

Your single source of truth: **what to study, in what order, and exactly which file to open
first.** Self-contained — clone the repo and follow it from any machine. Track status in
[`PROGRESS.md`](PROGRESS.md).

> Built around a **~12-week steady pace** (a few focused hours most days) and a **FAANG-style
> bar**, extended to full-stack (frontend system design included). Adjust freely — the *order*
> matters more than the calendar.

---

## 0. How to use this plan

- **Hybrid loop.** *Read* the reference (data structures, design docs) → *practice* the stubs
  (coding & design problems) → read the reference solution (in `solutions/`) only **after** you attempt.
- **One concept per file.** Reference impls are fully documented with Big-O; practice files are a
  spec + failing test you drive to green.
- **Spaced repetition.** Re-implement a data structure from scratch a few days later; redo a
  coding problem after a week. Retention beats coverage.
- **Build in public-ish.** Each project is runnable and pushable — your repo *is* your portfolio.

## 1. 🚦 START HERE

0. **One-time setup:** [`SETUP.md`](SETUP.md) (install JDK 21 / Node / uv / Docker, run one build). Studying solo? Read [`docs/how-to-study-solo.md`](docs/how-to-study-solo.md) — the attempt-first loop, time-boxing, and self-grading with [`docs/rubrics.md`](docs/rubrics.md).
1. Read [`docs/00-how-to-use.md`](docs/00-how-to-use.md) and [`docs/dsa/README.md`](docs/dsa/README.md) (5 min).
2. Open **`dsa-java/`** → read [`docs/dsa/CONVENTIONS.md`](docs/dsa/CONVENTIONS.md).
3. Your first file: **`dsa-java/src/main/java/com/venkat/dsa/linear/DynamicArray.java`** — read it,
   then close it and re-implement it from memory; run `\.mvnw.cmd test`.
4. Continue through `linear/` in the order listed in Phase 1, ticking [`PROGRESS.md`](PROGRESS.md).

## 2. The 12-week map

| Phase | Weeks | Focus | Primary project(s) |
| --- | --- | --- | --- |
| 1 DSA foundations | 1–2 | data-structure reference impls | `dsa-java`, `docs/dsa` |
| 2 Coding patterns | 3–5 | NeetCode 150 by pattern/difficulty | `dsa-java/coding` |
| 3 LLD | 5–7 | OOP/SOLID/patterns + problems (backend + UI) | `backend-service`, `web/spa-react-vite`, `docs/lld` |
| 4 HLD | 7–10 | system design + rendering + event-driven | `backend-service`, `infra`, `web/ssr-tanstack-start`, `agentic-python`, `docs/hld` |
| 5 Behavioral & process | 10–11 | story bank + recruiter→offer | `docs/behavioral`, `docs/interview-process` |
| 6 Mock & capstone | 12 | mocks + one feature across the stack | all |

Behavioral (Phase 5) and the agentic track run as **ongoing side-threads** from week 1 — don't
leave stories to the end.

---

## 3. Phase detail

### Phase 1 — DSA foundations (Weeks 1–2) · `dsa-java`
- **Goal:** rebuild the mechanical fluency to implement any core structure from scratch and state
  its Big-O cold.
- **Open first:** `linear/DynamicArray.java`.
- **Order:** `linear/` (DynamicArray → linked lists → stack → queue → deque → ring buffer) →
  `hashing/` (hash map chaining → open addressing → set → LRU) → `trees/` (binary tree → BST →
  AVL → heap → trie → segment/Fenwick) → `graphs/` (graph reps → union-find) →
  `algorithms/` (sorting → searching → graph algorithms).
- **Loop:** read the documented impl → re-implement from memory → green tests → note the Big-O.
- **Track:** [`PROGRESS.md`](PROGRESS.md) DSA section.

### Phase 2 — Coding patterns (Weeks 3–5) · `dsa-java/coding`
- **Goal:** recognize the pattern within ~2 minutes; code the optimal approach and explain the
  trade-off. (2026 reality: at senior/staff the *reasoning* is graded more than the typing.)
- **Backbone:** **NeetCode 150**, organized by the **18 patterns**, foldered by difficulty. Anchor
  on the 150; NeetCode 250 is optional depth.
- **Loop:** read the pattern note in `docs/dsa/patterns/` → solve 3–5 problems from that group
  (stub → failing test → green) → read the reference solution in `solutions/` to compare → redo a week later.
- **Order:** Arrays & Hashing → Two Pointers → Sliding Window → Stack → Binary Search →
  Linked List → Trees → Heap/PQ → Backtracking → Tries → Graphs → Advanced Graphs → 1-D DP →
  2-D DP → Greedy → Intervals → Math & Geometry → Bit Manipulation.
- **Resources:** [neetcode.io/practice](https://neetcode.io/practice).

### Phase 3 — Low-Level Design (Weeks 5–7) · `backend-service` + `web/spa-react-vite`
- **Goal:** turn a vague prompt into clean classes/APIs with the right patterns, concurrency, and
  extensibility — and do the **frontend** equivalent (component architecture, state, a11y).
- **Read:** `docs/lld/` — OOP refresher, SOLID, the design-pattern catalog, and the LLD framework.
- **Practice (backend, Java):** parking lot, rate limiter, LRU/LFU cache, splitwise, elevator,
  vending machine, notification service, … (spec + tests in `backend-service`).
- **Practice (UI, React):** design a reusable data-table, autocomplete/typeahead, a design-system
  component, a modal/toast system (in `web/spa-react-vite`).
- **Resources:** *Head First Design Patterns*, refactoring.guru.

### Phase 4 — High-Level Design (Weeks 7–10) · `backend-service` + `infra` + `web/ssr-tanstack-start` + `agentic-python`
- **Goal:** drive a system-design round end to end — requirements → estimates → API → data model
  → architecture → deep dives → bottlenecks → trade-offs — for **backend and frontend**.
- **Read:** `docs/hld/` — the framework, core concepts (scaling, caching, sharding, replication,
  CAP/PACELC, queues/Kafka, consistent hashing) **and the 2026 must-knows**: vector DBs/RAG, LLM
  & agentic system design, idempotency, multi-region, observability & cost-per-request.
- **Hands-on:** bring up `infra` (Postgres/Redis/Kafka KRaft) and build a small event-driven slice
  in `backend-service` (producer/consumer + DLQ + tracing). Compare rendering strategies by building
  one feature across the `web/` apps — TanStack Start (client-first), Next.js (RSC-first), React
  Router 7 (loader/action), Astro (islands) — plus `expo-app` for native + web. Use `agentic-python`
  to reason about LLM/agentic systems.
- **Resources:** **Designing Data-Intensive Applications, 2e (2026)**,
  [ByteByteGo](https://bytebytego.com/), [Hello Interview](https://www.hellointerview.com/),
  [DesignGurus](https://www.designgurus.io/). Frontend:
  [GreatFrontEnd](https://www.greatfrontend.com/front-end-system-design-playbook),
  [Frontend Interview Handbook](https://www.frontendinterviewhandbook.com/front-end-system-design).

### Phase 5 — Behavioral & process (Weeks 10–11, but start week 1) · `docs/`
- **Goal:** tell crisp Staff-level stories and navigate the non-technical rounds with intent.
- **Read:** `docs/behavioral/` (Staff signals: direction-setting, influence without authority,
  navigating ambiguity, the multiplier effect) and `docs/interview-process/` (recruiter screen,
  hiring manager, managerial round, negotiation, leveling, team-matching).
- **Do:** fill the **story bank** with 3–4 high-water-mark stories (STARL), each flexing across
  multiple themes; rehearse out loud.
- **Resources:** [StaffEng.com](https://staffeng.com/), *The Staff Engineer's Path* (Tanya Reilly),
  [Levels.fyi](https://www.levels.fyi/) (comp/negotiation),
  [Pragmatic Engineer](https://newsletter.pragmaticengineer.com/).

### Phase 6 — Mock & capstone (Week 12) · all
- Mock interviews (coding, LLD, HLD, behavioral) under time pressure.
- **Capstone:** one feature across the full stack — React UI → Spring backend → Kafka event →
  Postgres — demonstrating full-stack + system-design fluency. Write it up in `docs/hld/`.

---

## 4. Weekly cadence (suggested)

- **Mon–Fri:** 1 reference impl or 3–5 coding problems (≈60–90 min) + 15 min reading a design/behavioral doc.
- **Sat:** one LLD or HLD problem end-to-end (write it up).
- **Sun:** spaced review (redo 2 old problems, re-implement 1 structure) + add/refine one story.

Done well, that's the 12-week arc with room to breathe. Update [`PROGRESS.md`](PROGRESS.md) as you go.
