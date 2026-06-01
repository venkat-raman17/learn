# `learn/` — Full-Stack Staff Engineer Prep

A single monorepo workspace to get interview-ready for a **full-stack Staff Software Engineer**
role, end to end: data structures & coding, low- and high-level design (backend **and**
frontend), hands-on event-driven/infra, modern agentic systems, and the human rounds
(behavioral, recruiter, hiring-manager, managerial, negotiation).

**👉 Start with [`STUDY-PLAN.md`](STUDY-PLAN.md)** — the recommended 12-week path and exactly
which project and file to open first. Track progress in [`PROGRESS.md`](PROGRESS.md).

## Projects

| Folder | Stack | Purpose | Status |
| --- | --- | --- | --- |
| [`dsa-java/`](dsa-java/) | Java 21, Maven, JUnit 5 | Data structures (reference) + coding (NeetCode 150) | ✅ Phase 1 (29 impls, 790 tests) · ✅ Phase 2 (150 stubs ready) |
| `backend-service/` | Spring Boot 4.0.6, Spring Kafka, JPA | Backend LLD/HLD, event-driven, DB design | 🟡 builds green · 8 LLD katas ready |
| [`web/`](web/README.md) | React: Vite SPA · TanStack Start · Next.js · React Router 7 · Astro | Frontend LLD/HLD — compare rendering paradigms (CSR / SSR / RSC / islands) | ✅ all 5 scaffolded (4 builds verified) |
| `expo-app/` | Expo / React Native | Native (iOS/Android) **and** web from one codebase | ✅ web build green |
| `agentic-python/` | Python 3.12+, uv, Claude Agent SDK | Modern AI-systems / agentic design | 🟡 scaffold |
| `infra/` | Docker Compose | Postgres, Redis, Kafka (KRaft) for hands-on HLD | 🟡 ready |
| [`docs/`](docs/) | Markdown | Study knowledge base (DSA, LLD, HLD, behavioral, process) | 🟡 DSA + LLD/HLD foundations + 12 HLD case studies; behavioral/process outlined |

Status: ✅ done · 🟡 in progress · ⬜ planned.

## The roadmap (full-stack Staff)

1. **DSA foundations** — data-structure reference impls (`dsa-java`)
2. **Coding patterns** — NeetCode 150 by pattern & difficulty (`dsa-java`)
3. **LLD** — OOP/SOLID/patterns + problems, backend (`backend-service`) **and** UI (`web`)
4. **HLD** — system design + frontend rendering + event-driven (`backend-service`, `infra`, `web`, `agentic-python`)
5. **Behavioral & process** — Staff bar, story bank, recruiter→offer (`docs`)
6. **Mock & capstone** — one feature across the full stack

## How it works

**Hybrid model:** data structures and reading docs are **reference to study**; coding and design
problems are **practice-first** — a stub/spec + tests you solve yourself, with a reference
solution provided on request *after* you attempt. One concept per file.

## Quick start

```powershell
# DSA (Java) — once built:
cd dsa-java;            .\mvnw.cmd test
# Backend service (Spring Boot) — once built:
cd backend-service;     .\mvnw.cmd test
# Frontend (web/: spa-react-vite, nextjs-app, react-router-app, astro-app, ssr-tanstack-start):
cd web/nextjs-app;      npm install; npm run dev
# Native + web (Expo):
cd expo-app;            npm install; npm run web   # or npm run android / ios
# Infra (databases, Kafka):
cd infra;               docker compose up -d
```

See each project's own `README.md` for details, and `docs/` for the study material.
