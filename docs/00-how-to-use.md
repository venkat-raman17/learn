# How to use this knowledge base

`docs/` is the **reading** half of the workspace; the projects are the **doing** half.

## Start here (solo learners)
- [`../SETUP.md`](../SETUP.md) — one-time install (JDK 21 / Node / uv / Docker).
- [`how-to-study-solo.md`](how-to-study-solo.md) — how to learn from this repo: attempt-first, time-boxing, spaced repetition, a daily routine.
- [`rubrics.md`](rubrics.md) — grade your own coding / LLD / HLD / behavioral attempts (no interviewer needed).
- [`cheatsheets/`](cheatsheets/) — [Big-O](cheatsheets/big-o.md) · [pattern triggers](cheatsheets/pattern-triggers.md) · [system-design numbers](cheatsheets/system-design-numbers.md) · [checklists](cheatsheets/design-checklists.md).

## Read ↔ practice pairing

| Read (`docs/`) | Practice (project) |
| --- | --- |
| [`dsa/`](dsa/) — 18 [pattern notes](dsa/patterns/) + conventions + roadmap | `dsa-java/` — DS impls + coding stubs; **answers in `coding/<diff>/solutions/`** |
| [`lld/`](lld/) — OOP, SOLID, patterns, framework | `backend-service/.../lld` + `web/spa-react-vite/src/lld` — each kata has a `SOLUTION.md` |
| [`hld/`](hld/) — framework + core concepts + [12 case studies](hld/case-studies/) | `backend-service/`, `infra/`, `web/`, `agentic-python/` |
| [`behavioral/`](behavioral/), [`interview-process/`](interview-process/) | your own story bank + mock answers |
| [`mock-interviews/`](mock-interviews/) — timed self-run mocks (Phase 6) | all rounds, under time |
| [`../capstone/`](../capstone/) — the TinyLink full-stack build (Phase 6) | the whole stack |

## The hybrid loop
1. **Read** the reference doc / data-structure impl.
2. **Practice** the matching stub/spec until tests pass — *your* solution first (time-boxed).
3. **Compare** against the reference: coding answers in each difficulty's `solutions/` subpackage; LLD katas have a `SOLUTION.md`; HLD has the worked case study.
4. **Self-grade** with [`rubrics.md`](rubrics.md), then **review** on a spaced schedule.

## Conventions
- One concept per file. Reference material is fully worked; practice material is a spec + failing test.
- Each doc opens with **what it is**, **why it matters at Staff level**, and **how it's tested**.
- Curated external resources are linked inline (need internet) — the repo content itself is self-contained.

Follow [`../STUDY-PLAN.md`](../STUDY-PLAN.md) for the order; tick [`../PROGRESS.md`](../PROGRESS.md) as you go.
