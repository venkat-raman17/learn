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

## Case studies — ✅ written in [`case-studies/`](case-studies/)
Worked designs following the framework; each opens with a try-it-yourself prompt, then applies
requirements → estimates → API → data model → architecture → deep dives → bottlenecks → trade-offs.
- [URL shortener](case-studies/url-shortener.md) · [News feed](case-studies/news-feed.md) · [Chat](case-studies/chat.md) · [Rideshare](case-studies/rideshare.md)
- [Video streaming](case-studies/video-streaming.md) · [File storage (Drive/Dropbox)](case-studies/file-storage.md) · [Typeahead](case-studies/typeahead.md)
- [Distributed message queue](case-studies/distributed-message-queue.md) · [Unique ID generator](case-studies/unique-id-generator.md) · [Distributed rate limiter](case-studies/distributed-rate-limiter.md)
- [Web crawler](case-studies/web-crawler.md) · [Ad-click aggregator](case-studies/ad-click-aggregator.md)

## Frontend system design — ✅ [`frontend-system-design.md`](frontend-system-design.md)
- [ ] Rendering strategies: CSR, SSR, SSG, ISR, streaming SSR, RSC, Partial Prerendering, islands
- [ ] Data fetching & caching (SWR/React Query, ISR, edge), state at scale
- [ ] Performance (Core Web Vitals, bundling, code-splitting), accessibility as architecture, micro-frontends

## Front-end observability — ✅ [`frontend-observability.md`](frontend-observability.md)
- [ ] Analytics (product + RUM), error/crash reporting, Core Web Vitals; the vendor-agnostic seam
- [ ] Trace propagation into the browser (W3C `traceparent`), session replay, privacy/consent, mobile crashlytics
- Worked samples: [`web/spa-react-vite/src/observability/`](../../web/spa-react-vite/src/observability/) (web) · [`expo-app/src/observability/`](../../expo-app/src/observability/) (mobile)

## Databases & search — ✅ written
- [x] [`databases.md`](databases.md) — SQL vs NoSQL decision matrix, R2DBC vs JDBC, MongoDB reactive patterns, CAP theorem, schema management, interview talking points
- [x] [`elasticsearch.md`](elasticsearch.md) — inverted index, BM25 ranking, kNN vector search, hybrid search (BM25 + kNN), Spring Data ES wiring, when to choose ES vs Postgres FTS vs Pinecone

## Security — ✅ [`security.md`](security.md)
- [ ] OWASP Top 10 (2021): Broken Access Control, Cryptographic Failures, Injection, Insecure Design, Security Misconfiguration, Vulnerable Components, Auth Failures, Integrity Failures, Logging Failures, SSRF
- [ ] OWASP API Security Top 10 (2023) quick-reference
- [ ] Frontend-specific: XSS (stored/reflected/DOM), CSRF, clickjacking, open redirect, localStorage vs HttpOnly cookies
- [ ] Spring Boot security: CORS config, security headers, JWT pitfalls, actuator exposure
- [ ] Secrets management, security headers cheat sheet, threat modelling (STRIDE)
- Worked examples from this repo's own code (TokenService, R2DBC parameterised queries, MemorySink, WebFilter)

## CI/CD — ✅ [`cicd.md`](cicd.md)
- [ ] CI vs CD vs Continuous Deployment; GitHub Actions anatomy (workflow / job / step / runner)
- [ ] Path filtering, caching, matrix builds, concurrency groups, fail-fast
- [ ] Docker service containers for integration tests; multi-stage Dockerfile
- [ ] Deployment strategies: blue-green, canary, rolling update, feature flags
- [ ] GitOps, DORA metrics, CI/CD security (pinned SHAs, minimum permissions, secrets hygiene)
- Working workflows: [`.github/workflows/`](../../../.github/workflows/) — 4 workflows covering Java, Python, Web, and security scans

## Resources
- **Designing Data-Intensive Applications, 2e (2026)** — the distributed-systems bible
- [ByteByteGo](https://bytebytego.com/) (visual breadth) · [Hello Interview](https://www.hellointerview.com/) (mock practice) · [DesignGurus](https://www.designgurus.io/) (2026 LLM/agentic guides)
- [system-design-primer](https://github.com/donnemartin/system-design-primer) (free fundamentals)
- Frontend: [GreatFrontEnd](https://www.greatfrontend.com/front-end-system-design-playbook) · [Frontend Interview Handbook](https://www.frontendinterviewhandbook.com/front-end-system-design)
