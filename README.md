# `learn/` — Full-Stack Staff Engineer Prep

A single monorepo workspace to get interview-ready for a **full-stack Staff Software Engineer**
role, end to end: data structures & coding, low- and high-level design (backend **and**
frontend), hands-on event-driven/infra, modern agentic systems, and the human rounds
(behavioral, recruiter, hiring-manager, managerial, negotiation).

**👉 Start with [`STUDY-PLAN.md`](STUDY-PLAN.md)** — the recommended 12-week path and exactly
which project and file to open first. Track progress in [`PROGRESS.md`](PROGRESS.md).

**Studying solo / just unzipped this?** Do [`SETUP.md`](SETUP.md) once, then read
[`docs/how-to-study-solo.md`](docs/how-to-study-solo.md). Self-check with the verified `solutions/`,
the [cheat sheets](docs/cheatsheets/), and the [self-grading rubrics](docs/rubrics.md).

## Projects

| Folder | Stack | Purpose | Status |
| --- | --- | --- | --- |
| [`dsa-java/`](dsa-java/) | Java 21, Maven, JUnit 5 | Data structures (reference) + coding (NeetCode 150) | ✅ 29 DS impls · 150 stubs + verified solutions · 940+ tests green |
| `backend-service/` | Spring Boot 4.0.6, WebFlux, R2DBC, MongoDB Reactive | Backend LLD katas · ops layer (Actuator/Micrometer/ECS logging/graceful shutdown) · observability kata · SQL + NoSQL reactive demo | ✅ 73 tests green · 11 LLD katas · observability kata · R2DBC + MongoDB demo |
| [`web/`](web/README.md) | React: Vite SPA · TanStack Start · Next.js · React Router 7 · Astro | Frontend LLD/HLD — compare rendering paradigms (CSR / SSR / RSC / islands) + front-end observability layer | ✅ all 5 scaffolded · spa-react-vite has analytics/crash/vitals + Vitest |
| `expo-app/` | Expo / React Native | Native (iOS/Android) **and** web from one codebase + mobile observability (crash, screen-view, global handler) | ✅ web build green · observability sink + ErrorBoundary + jest-expo tests |
| [`graphql-layer/`](graphql-layer/README.md) | Apollo Server 4 · Apollo Client 3 · React · Vite · TypeScript | GraphQL full-stack: schema-first API, `useQuery` / `useMutation` / `useLazyQuery`, cache normalisation, N+1 / DataLoader notes | ✅ server + client · tsc clean |
| [`agentic-python/`](agentic-python/) | Python 3.11+, uv, LangGraph | Modern AI-systems / agentic — **Corrective RAG** state machine over MCP (keyless FakeLLM, swappable to Claude) | ✅ LangGraph graph + MCP client · 10 tests |
| [`mcp-server-py/`](mcp-server-py/) | Python 3.11+, uv, FastMCP, NumPy | **MCP server** exposing a from-scratch RAG knowledge base (TF-IDF + cosine, reservoir sampling) | ✅ stdio server · 7 tests |
| `infra/` | Docker Compose | Postgres · Redis · Kafka (KRaft) · MongoDB · Elasticsearch 8 — all with health checks | ✅ 7 services · connection strings in `infra/README.md` |
| [`docs/`](docs/) | Markdown | Study knowledge base: DSA · LLD · HLD (12 case studies + databases + Elasticsearch + GraphQL + frontend observability) · behavioral/process · cheat sheets · rubrics · mock-interview kit | ✅ complete |
| [`capstone/`](capstone/) | Full-stack build | Phase 6 capstone — build *TinyLink* (URL shortener + real-time analytics) across the stack | ✅ blueprint (spec + reference design) |

Status: ✅ done · 🟡 in progress · ⬜ planned.

## The roadmap (full-stack Staff)

1. **DSA foundations** — data-structure reference impls (`dsa-java`)
2. **Coding patterns** — NeetCode 150 by pattern & difficulty (`dsa-java`)
3. **LLD** — OOP/SOLID/patterns + problems, backend (`backend-service`) **and** UI (`web`)
4. **HLD** — system design + frontend rendering + event-driven + agentic RAG/MCP (`backend-service`, `infra`, `web`, `agentic-python`, `mcp-server-py`) + GraphQL layer (`graphql-layer`)
5. **Behavioral & process** — Staff bar, story bank, recruiter→offer (`docs`)
6. **Mock & capstone** — self-run mock kit (`docs/mock-interviews/`) + build the `capstone/`

## How it works

**Hybrid model:** data structures and reading docs are **reference to study**; coding and design
problems are **practice-first** — a stub/spec + tests you solve yourself, with a reference
solution provided in the repo (`solutions/`), to read *after* you attempt. One concept per file.

## Quick start

```bash
# DSA (Java) — once built:
cd dsa-java && ./mvnw test

# Backend service (Spring Boot) — no infra required:
cd backend-service && ./mvnw test
# With Postgres + MongoDB (docker compose up -d first):
./mvnw spring-boot:run -Dspring-boot.run.profiles=datastore

# Frontend (web/: spa-react-vite, nextjs-app, react-router-app, astro-app, ssr-tanstack-start):
cd web/nextjs-app && npm install && npm run dev

# GraphQL layer (Apollo Server + React client):
cd graphql-layer/server && npm install && npm run dev   # → http://localhost:4000
cd graphql-layer/client && npm install && npm run dev   # → http://localhost:5174

# Native + web (Expo):
cd expo-app && npm install && npm run web

# Agentic RAG (LangGraph + MCP):
cd agentic-python && uv sync && uv run python -m agentic.main "what is corrective rag?"

# Infra (all backing services):
cd infra && docker compose up -d
```

See each project's own `README.md` for details, and `docs/` for the study material.
