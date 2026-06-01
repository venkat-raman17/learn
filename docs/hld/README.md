# High-Level Design (HLD) / System Design

Drive a system-design round end to end. Covers **backend distributed systems** and **frontend
system design**, plus the topics that became table-stakes in 2026. Hands-on practice uses
[`../../backend-service/`](../../backend-service/), [`../../infra/`](../../infra/), and
[`../../web/ssr-tanstack-start/`](../../web/ssr-tanstack-start/).

## The framework — ✅ written
- [x] [`framework.md`](framework.md) — the step-by-step round: requirements → estimates → API → data model → architecture → deep dives → bottlenecks → trade-offs (+ anti-patterns and the 2026 additions).

## Core concepts (classics) — ✅ written in [`core-concepts.md`](core-concepts.md)
- [ ] Scaling (vertical/horizontal), load balancing, statelessness
- [ ] Caching (strategies, eviction, CDN, edge)
- [ ] Databases: SQL vs NoSQL, indexing, sharding/partitioning, replication
- [ ] Consistency: CAP, PACELC, eventual consistency, quorum
- [ ] Messaging: queues, pub/sub, Kafka, back-pressure
- [ ] Consistent hashing, bloom filters, rate limiting, leader election

## 2026 must-knows (newer, now expected) — ✅ in [`core-concepts.md`](core-concepts.md)
- [ ] **Vector databases & RAG** (pgvector, embeddings, semantic search)
- [ ] **LLM system design** (serving, routing, token caching, cost/latency budgets)
- [ ] **Agentic systems** (tool gateways, safety boundaries, action-risk classification)
- [ ] **Idempotency** (dedupe, exactly-once-ish, payment retries)
- [ ] **Multi-region** (geo routing, replication, failover)
- [ ] **Observability & cost** (metrics/logs/traces, cost-per-request as a rubric item)

## Case studies — *filled in Phase 4*
- [ ] URL shortener · [ ] News feed · [ ] Chat/WhatsApp · [ ] Rideshare/Uber · [ ] Video streaming
- [ ] Drive/Dropbox · [ ] Typeahead/autocomplete · [ ] Distributed message queue · [ ] Unique ID generator
- [ ] Distributed rate limiter · [ ] Web crawler · [ ] Ad-click aggregator

## Frontend system design — *filled in Phase 4*
- [ ] Rendering strategies: CSR, SSR, SSG, ISR, streaming SSR, RSC, Partial Prerendering, islands
- [ ] Data fetching & caching (SWR/React Query, ISR, edge), state at scale
- [ ] Performance (Core Web Vitals, bundling, code-splitting), accessibility as architecture, micro-frontends

## Resources
- **Designing Data-Intensive Applications, 2e (2026)** — the distributed-systems bible
- [ByteByteGo](https://bytebytego.com/) (visual breadth) · [Hello Interview](https://www.hellointerview.com/) (mock practice) · [DesignGurus](https://www.designgurus.io/) (2026 LLM/agentic guides)
- [system-design-primer](https://github.com/donnemartin/system-design-primer) (free fundamentals)
- Frontend: [GreatFrontEnd](https://www.greatfrontend.com/front-end-system-design-playbook) · [Frontend Interview Handbook](https://www.frontendinterviewhandbook.com/front-end-system-design)
