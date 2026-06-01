# TinyLink Capstone — Staff-Prep Build

## Overview & Why This Capstone

You have studied the URL-shortener HLD case study (`docs/hld/case-studies/url-shortener.md`).
Now you build it. TinyLink is the culminating project that forces every skill to work together:

- **LLD**: model a `Link` entity, choose base62 encoding, design the Postgres schema.
- **HLD**: reason about read/write ratios, cache placement, Kafka fanout, consistency trade-offs.
- **Coding drills**: base62 key generation, idempotency tokens, Bloom-filter de-duplication.
- **Event-driven**: Kafka ClickEvent publish/consume pipeline feeding analytics aggregates.
- **Full-stack**: React SPA talks to a Spring REST API backed by Redis, Kafka, and Postgres.

A staff-level candidate should be able to whiteboard this system in 20 minutes and implement
the core in a day. This capstone is the proof.

---

## Functional Requirements

1. **Shorten** — POST a long URL; receive a unique 6-8 char short code (e.g. `tinylink.io/aB3xZ9`).
2. **Redirect** — GET `/:code` returns HTTP 301/302 to the original URL; records a click event.
3. **Analytics** — Dashboard shows total clicks per link, clicks over time (hourly buckets), top referrers.
4. **Idempotency** — POSTing the same URL + same idempotency-key returns the existing short code, not a duplicate.
5. **Expiry (stretch)** — Optional TTL on short links; expired codes return 410 Gone.

---

## Non-Functional Requirements

| Concern | Target |
|---|---|
| Redirect latency (p99) | < 20 ms (cache hit), < 100 ms (cache miss) |
| Throughput | 5 000 redirects/sec on a single node |
| Analytics lag | < 30 s eventual consistency (Kafka consumer) |
| Availability | Short-circuit to DB if Redis is down; no 5xx |
| Observability | Micrometer metrics on redirect latency + Kafka lag |

---

## Architecture at a Glance

```
Browser / React SPA
       |
       | REST (JSON)
       v
  Spring Boot API  ──(cache lookup)──>  Redis
       |  |
       |  └──(cache miss / write)──>  Postgres
       |
       └──(on redirect)──> Kafka topic: click-events
                                   |
                            Analytics Consumer
                                   |
                               Postgres
                            (click_aggregates)
```

---

## Tech Mapping

| Layer | Project in repo | Notes |
|---|---|---|
| React SPA | `web/spa-react-vite` or `nextjs-app` | Vite + React Query; two routes: `/` (shorten form) and `/dashboard` |
| Spring API | `backend-service` | Spring Boot 3, Spring Data JPA, Spring Kafka |
| Cache | `infra/docker-compose` — Redis 7 | `SETEX code url 3600`; evict on expiry |
| Message bus | `infra/docker-compose` — Kafka + Zookeeper | Topic `click-events`, 3 partitions |
| Primary DB | `infra/docker-compose` — Postgres 16 | Tables: `links`, `click_aggregates` |
| Analytics consumer | second Spring `@KafkaListener` bean (same service or sidecar module) | Upserts hourly bucket rows |

---

## Milestones

### M0 — Infra up
- [ ] `docker-compose up` starts Postgres, Redis, Kafka, Zookeeper cleanly.
- [ ] Health checks pass; Kafka topic `click-events` auto-created with 3 partitions.
- [ ] Flyway baseline migration runs; `links` and `click_aggregates` tables exist.

### M1 — Shorten + Redirect (REST + Postgres)
- [ ] `POST /api/links` accepts `{ "url": "...", "idempotencyKey": "..." }` → `{ "code": "aB3xZ9", "shortUrl": "..." }`.
- [ ] Base62 key generation function passes unit tests (see `coding/` exercises).
- [ ] `GET /:code` redirects with 302; 404 on unknown code.
- [ ] Integration test: shorten then redirect round-trip hits Postgres only (no cache yet).

### M2 — Redis Cache
- [ ] On redirect: check Redis first; on miss, read Postgres and populate cache with TTL.
- [ ] On shorten: write-through to cache immediately after DB insert.
- [ ] Unit test: second redirect call does NOT hit the DB (verify with mock or metrics counter).
- [ ] Graceful degradation: if Redis is unreachable, redirects still work via DB fallback.

### M3 — Kafka ClickEvent + Analytics Consumer
- [ ] On each successful redirect, publish `ClickEvent { code, timestamp, referrer, ip }` to `click-events`.
- [ ] `AnalyticsConsumer` reads events; upserts into `click_aggregates(code, hour_bucket, count)`.
- [ ] `GET /api/links/{code}/analytics` returns aggregated data from Postgres.
- [ ] End-to-end test: redirect 10 times, wait 5 s, assert aggregate count == 10.

### M4 — React Dashboard
- [ ] Shorten form: input field + submit → calls `POST /api/links` → shows short URL.
- [ ] Dashboard page: lists all links with total click count.
- [ ] Click on a link → shows hourly click chart (recharts or similar).
- [ ] Polling or SSE refreshes analytics every 10 s without full-page reload.

### M5 — Polish: Idempotency, Metrics, Tests
- [ ] Idempotency: same `idempotencyKey` within 24 h returns cached response (Redis or DB lookup).
- [ ] Micrometer: `tinylink.redirect.latency` histogram; `tinylink.kafka.publish.errors` counter.
- [ ] `/actuator/prometheus` endpoint enabled and scrapeable.
- [ ] Unit test coverage >= 80 % on service layer; at least one contract test on `POST /api/links`.
- [ ] README `curl` smoke test documented and passing.

---

## Acceptance Criteria

A completed capstone satisfies ALL of the following:

1. `docker-compose up && ./smoke-test.sh` passes with zero errors.
2. `GET /:code` p99 latency < 20 ms under `wrk -t4 -c100 -d30s` (measure with Redis warm).
3. Posting the same `idempotencyKey` twice returns the **same** short code both times.
4. Killing the Redis container does not cause redirects to return 5xx (graceful fallback).
5. After 50 redirects, `GET /api/links/{code}/analytics` reports total >= 50 within 30 s.
6. The React dashboard renders without console errors and shows live click data.
7. `./mvnw test` passes with coverage report showing >= 80 % on `service` package.

---

## How to Self-Assess

- Re-read `docs/hld/case-studies/url-shortener.md`. Can you explain every decision you made
  that differs from that reference? Good — that means you reasoned, not copied.
- Whiteboard session: cover the system in 20 min, explain the Kafka fanout and Redis TTL choice.
- Open `DESIGN.md` in this folder. Compare your implementation to the reference architecture.
  For each divergence, write a one-line justification. If you cannot justify it, revisit.
- Rubric: M0–M3 = senior engineer. M4 = senior + product thinking. M5 = staff-level polish.

---

## Alternative Capstones

**Event-driven order pipeline** — An e-commerce order service (place order → payment-service Kafka
consumer → inventory-service consumer → notification-service consumer). Exercises saga pattern,
compensating transactions, and dead-letter topics. Same infra stack, different domain.

**Real-time leaderboard** — A gaming leaderboard backed by Redis sorted sets (`ZADD`, `ZRANK`,
`ZRANGE`). A score-ingest Kafka topic feeds a Spring consumer that updates Redis; the React UI
polls the top-10 via SSE. Exercises Redis data structures and high-frequency write patterns.
