# URL Shortener (TinyURL)

**Prompt:** Design a service that shortens long URLs and redirects visitors to the original URL.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you must ask

| Question | Why it matters |
|---|---|
| DAU and total URLs created per day? | Determines QPS floor and storage growth rate. |
| Read:write ratio? | Drives caching strategy completely. |
| Custom aliases (vanity slugs)? | Adds collision/uniqueness concerns and a separate write path. |
| Click analytics — real-time or eventual? | Real-time analytics forces synchronous writes; eventual allows async queues. |
| URL expiry (TTL)? | Affects storage, key recycling, and cache invalidation. |
| Multi-region or single DC? | Determines if you need geo-replication and consistency trade-offs. |
| Authenticated users only, or public? | Affects rate-limiting and spam/abuse controls. |

### Functional requirements (locked)

- `POST /shorten` — accept a long URL, return a short code (7 chars).
- `GET /{code}` — resolve code to original URL and issue HTTP 301/302 redirect.
- Optional: custom alias, expiry date, per-link click analytics.

### Non-functional requirements & assumptions

- **Scale:** 100 M DAU; users create ~1 URL/day on average → **1 M writes/day**.
  Reads dominate heavily: assume **100:1** read:write → **100 M redirects/day**.
- **Peak:** 3× average for writes = ~35 writes/s; reads = ~3,500 reads/s.
- **Latency:** redirect p99 < 50 ms (user perceives it as "instant"). Write < 200 ms.
- **Availability:** 99.99 % (four nines); URLs must never silently lose their mapping.
- **Consistency:** eventual is fine for analytics; the redirect mapping itself must be
  **read-after-write consistent** (at least in the same region).
- **URL lifetime:** default 5 years unless TTL is set; expired URLs return 410 Gone.
- **Short code length:** 7 alphanumeric chars (base-62) → 62^7 ≈ **3.5 trillion** unique codes.

---

## 2. Capacity Estimates

### Write path

```
1 M writes/day ÷ 86,400 s ≈ 12 writes/s average; peak 35 writes/s
Storage per record ≈ 500 B (long URL avg 200 B + code + metadata)
Storage/day = 1 M × 500 B = 500 MB/day
5-year total = 500 MB × 365 × 5 ≈ 900 GB → round to ~1 TB total
```

### Read path

```
100 M reads/day ÷ 86,400 s ≈ 1,160 reads/s average; peak ~3,500 reads/s
Bandwidth/read ≈ 0.5 KB response → 3,500 × 500 B ≈ 1.75 MB/s outbound (trivial)
```

### Cache sizing (hot-set)

Zipf distribution: 20 % of URLs account for 80 % of traffic.

```
Hot URLs = 20% × 1 M/day × 365 days active ≈ 73 M records
Memory per record ≈ 500 B → 73 M × 500 B ≈ 36 GB
```

A single large Redis node (64 GB) holds the entire hot set comfortably.
Cache hit rate target: **> 95 %** (eliminates DB read for nearly all traffic).

### Key insight

This is an **extremely read-heavy, small-record** system. The entire working set fits in memory.
Design around cache, not DB.

---

## 3. API Design

### Public endpoints

```
POST   /api/v1/urls
  Body:    { "url": "https://...", "alias": "my-slug", "ttl_days": 365 }
  Returns: 201 { "short_url": "https://sho.rt/aB3xY7z", "code": "aB3xY7z", "expires_at": "..." }
  Headers: Idempotency-Key: <client-uuid>   ← dedupe retries

GET    /{code}
  Returns: 301/302 Location: <original_url>
           410 Gone (expired)
           404 Not Found

GET    /api/v1/urls/{code}/stats
  Returns: { "clicks": 4821, "last_clicked": "...", "top_countries": [...] }

DELETE /api/v1/urls/{code}           (owner only, JWT auth)
  Returns: 204 No Content
```

### Design decisions

- **301 vs 302:** Use **302 Found** (temporary) so browsers do NOT cache the redirect locally —
  this preserves your analytics and lets you update or expire URLs. Use 301 only for permanent
  aliases you never need to change.
- **Idempotency-Key** on POST: if the client retries after a timeout, the server returns the same
  short code without creating a duplicate. Key stored in Redis with a 24-hour TTL.
- **Rate limiting:** 100 creates/min per IP (unauthenticated), 1,000/min per authenticated user.

---

## 4. Data Model

### Core entities

**`urls` table** (primary store)

| Column | Type | Notes |
|---|---|---|
| `code` | CHAR(7) PK | The short slug; indexed, unique |
| `original_url` | TEXT | Up to 2 KB |
| `owner_id` | UUID FK | nullable for anonymous |
| `created_at` | TIMESTAMPTZ | |
| `expires_at` | TIMESTAMPTZ | nullable |
| `is_custom` | BOOL | true = user-supplied alias |

**`clicks` table** (analytics, append-only)

| Column | Type | Notes |
|---|---|---|
| `id` | BIGSERIAL PK | |
| `code` | CHAR(7) | FK → urls |
| `clicked_at` | TIMESTAMPTZ | |
| `country` | CHAR(2) | from IP geo-lookup |
| `referrer` | TEXT | |

### Store choice

**Primary mapping (`code → url`):** Use a **key-value store** (Redis as primary + disk-backed
persistence, or DynamoDB/Cassandra). The sole access pattern is point-lookup by code — there are
no joins, no range scans, no transactions. A relational DB works but adds unnecessary overhead.

In practice: **DynamoDB** (or Cassandra) for the URL table — infinite horizontal scale,
single-digit ms reads, no schema migrations, global tables for multi-region. `code` as the
partition key gives perfect distribution.

**Analytics (`clicks`):** Append-only writes are the bottleneck, not reads. Use **Apache Kafka**
as the ingest bus, then drain to **ClickHouse** or **BigQuery** for OLAP queries. Avoid writing
clicks synchronously to the primary DB — it creates write contention on a hot-path.

---

## 5. High-Level Architecture

```
                        ┌──────────────────────────────────────────┐
                        │              DNS / CDN (CloudFront)       │
                        │   geo-routes, caches 301s at edge         │
                        └─────────────────┬────────────────────────┘
                                          │
                              ┌───────────▼───────────┐
                              │   Load Balancer (L7)  │
                              │   (rate-limit, TLS)   │
                              └─────┬──────────┬──────┘
                                    │          │
                        ┌───────────▼──┐  ┌────▼──────────┐
                        │  Redirect    │  │  Write API     │
                        │  Service     │  │  Service       │
                        │  (stateless) │  │  (stateless)   │
                        └──────┬───────┘  └──────┬─────────┘
                               │                 │
                    ┌──────────▼──────┐   ┌──────▼─────────────┐
                    │  Redis Cache    │   │  Key Generator      │
                    │  (hot set,      │   │  (counter-based,    │
                    │   look-aside)   │   │   ZooKeeper coord.) │
                    └──────┬──────────┘   └──────┬─────────────┘
                           │ miss                │
                    ┌──────▼──────────────────────▼─────────────┐
                    │          DynamoDB (URL store)              │
                    │      Global Tables (us-east / eu-west)     │
                    └────────────────────────────────────────────┘
                                          │ async click event
                              ┌───────────▼──────────┐
                              │    Kafka topic        │
                              │    (click_events)     │
                              └───────────┬───────────┘
                                          │
                              ┌───────────▼──────────┐
                              │  ClickHouse / BQ      │
                              │  (analytics, OLAP)    │
                              └──────────────────────┘
```

### Read path (redirect)

1. Request hits CDN — if the short code was recently resolved and the edge has it, return 302
   immediately (sub-5 ms).
2. Cache miss → LB → Redirect Service → **Redis look-aside** (< 1 ms).
3. Redis miss (cold start or TTL expired) → DynamoDB point-read (5–10 ms single-digit p99).
4. Populate Redis; return 302 to client.
5. Fire a **fire-and-forget** click event onto Kafka (async; does NOT block the redirect).

### Write path (shorten)

1. Validate URL (scheme allowlist, SSRF blocklist, max length).
2. Check **Idempotency-Key** in Redis — return cached response if exists.
3. Request a new code from the Key Generator Service.
4. Write `{ code, original_url, metadata }` to DynamoDB (conditional write — fails if code
   already exists, which triggers a retry with a new code).
5. Populate Redis immediately (write-through) so the first redirect is cache-warm.
6. Return 201 to client.

---

## 6. Deep Dives

### 6.1 Key generation: counter + base-62 vs hashing

**Option A — Counter + base-62 encoding**

A global atomic counter (ZooKeeper, or a dedicated counter service with range pre-allocation)
increments for each URL. Convert the integer to base-62 (a-z A-Z 0-9).

- Pros: guaranteed uniqueness, no collision detection, predictable 7-char length, sortable.
- Cons: sequential codes are guessable (privacy risk); single counter is a bottleneck if not
  pre-allocated in ranges.
- Mitigation: each Write API instance pre-fetches a range of 10,000 IDs from the counter
  service (batch allocation). The counter service is behind ZooKeeper leader election.

**Option B — Hashing (MD5 / SHA-256, take first 7 chars)**

Hash the original URL; take the first 7 base-62 chars.

- Pros: stateless, no coordinator needed; same URL always produces the same code.
- Cons: **collision probability** is non-trivial at scale (birthday paradox — with 1 B codes the
  chance of a 7-char collision is ~1/62^7 × n^2/2 ≈ 1 in 7 M for n = 1 M). Requires collision
  detection on every write: read-then-write with retry is expensive.

**Decision: use counter + base-62 with range pre-allocation.**
It eliminates collision detection entirely, scales to multi-instance with a thin coordinator,
and the guessability risk is acceptable (add a per-URL HMAC token for sensitive use-cases).
Shuffle the base-62 alphabet to make sequential codes non-obvious.

### 6.2 Redirect read path + caching

The redirect path is the **hot path** at 3,500 rps peak. Every millisecond matters.

- **Two-layer caching:** CDN edge cache (for the most popular codes globally) + Redis
  in-region. Serve 95 %+ of traffic without touching DynamoDB.
- **Cache TTL:** Redis TTL = `min(URL_expiry - now, 24 h)`. CDN TTL = 60 s (shorter so
  expired URLs propagate quickly).
- **Cache eviction:** LRU in Redis. The hot set (36 GB estimate) fits in a single 64 GB node,
  so eviction is rare — size the cluster to hold 2× the hot set.
- **Thundering herd:** On a cache miss for a suddenly viral URL, many requests race to DynamoDB.
  Use a **Redis lock** (SETNX) or probabilistic early expiration (re-cache at 90 % of TTL) to
  absorb the stampede.
- **301 vs 302 revisited:** Serving 301 moves caching responsibility to the browser (outside
  your control). Use 302 + CDN so you control both invalidation and analytics.

### 6.3 Custom aliases

Users supply a vanity slug (e.g., `sho.rt/product-launch`).

- **Uniqueness check:** A conditional write to DynamoDB (`ConditionExpression: attribute_not_exists(code)`)
  atomically reserves the alias. No separate read needed; the DB enforces uniqueness.
- **Namespace conflict:** Reserve a blocklist of system paths (`/api`, `/health`, `/admin`, etc.)
  as a static allowlist checked before the DB write.
- **Length & character rules:** 3–50 chars, alphanumeric + hyphen only. Validate server-side.
- **Pricing tier:** Custom aliases are typically a paid feature; gate behind auth + entitlement
  check to prevent squatting.

### 6.4 Click analytics

At 100 M clicks/day (~1,160/s average, 3,500/s peak), writing each click synchronously to a
relational DB would saturate connections immediately.

- **Async via Kafka:** The Redirect Service publishes a tiny event `{ code, ts, country, ua }`
  to a Kafka topic. The redirect itself returns immediately. A Flink or Spark Structured
  Streaming job consumes the topic and writes aggregates to ClickHouse.
- **Pre-aggregation:** Increment a Redis counter `clicks:{code}` atomically on every redirect
  (O(1), in-memory). Flush counters to ClickHouse every 60 s. This gives near-real-time click
  counts without a full DB write per event.
- **Observability cost-per-request:** Each redirect emits one Kafka event (~100 B). At 3,500/s
  that is ~350 KB/s on Kafka — negligible. Track p50/p99 latency and error rate per code as
  custom metrics; alert on anomaly (sudden 10× click spike = possible abuse or viral event).

### 6.5 Collision handling

With counter-based generation, hash collisions are impossible — but **concurrent range exhaustion**
and **retry storms** under coordinator failure must be handled.

- Each Write API pod pre-fetches a range of IDs. If the coordinator is down, pods use their
  in-memory buffer for up to 10,000 more writes before blocking. This gives several minutes of
  runway to restore the coordinator.
- For hashing-based generation (not chosen, but for completeness): detect collision via the
  conditional DynamoDB write; retry with a salt appended to the input (`url + attempt_number`).
  Cap at 5 retries; alert if retries exceed 0.01 % of writes (indicates bucket exhaustion).

---

## 7. Bottlenecks & Scaling

| Bottleneck | Limiting resource | Fix | Cost |
|---|---|---|---|
| Redirect latency | Redis single node | Redis Cluster (sharded by code) | Operational complexity; consistent hashing for key distribution |
| Write throughput | Key generator coordinator | Range pre-allocation per pod; no round-trip per write | Risk: gaps in ID space if pod crashes mid-range (acceptable) |
| DynamoDB hot partitions | Single partition for viral codes | DynamoDB auto-scaling + Adaptive Capacity handles skew automatically | Minimal; monitor consumed RCUs |
| Click analytics write | Relational DB write throughput | Kafka + ClickHouse async pipeline | Eventual consistency on click counts (~60 s lag) |
| Global latency | Single-region DynamoDB | DynamoDB Global Tables (multi-master, async replication) + geo-DNS routing | ~$0.10/M replicated writes; added replication lag (< 1 s) |
| Abuse / spam | Open write endpoint | Rate limiting (token bucket per IP/user) + URL reputation API (Google Safe Browsing) | Extra latency (~5 ms) per write; false-positive risk |
| CDN cache invalidation | Propagation delay on URL deletion/expiry | Short CDN TTL (60 s) + CDN cache purge API on explicit delete | Cost: more CDN misses for low-traffic URLs |

### Multi-region (2026 consideration)

Deploy in two regions (e.g., us-east-1, eu-west-1). DynamoDB Global Tables gives active-active
multi-master with last-write-wins conflict resolution — safe here because URL mappings are
write-once (conflicts only arise if two users create the same custom alias simultaneously, which
the conditional write prevents). Route traffic by geo-DNS (latency-based routing). Redirect
Services are fully stateless so they scale horizontally in each region with no coordination.

---

## 8. Trade-offs & Summary

### Decision 1 — Counter + base-62 vs hashing for key generation

**Chose:** Counter with range pre-allocation.
**Traded:** Slight operational complexity (ZooKeeper/coordinator) for **zero collision risk and
zero per-write read overhead**. Hashing is simpler to deploy but requires a read-before-write
on every create at scale — that's 35 extra DynamoDB reads/s peak, plus retry logic.

### Decision 2 — 302 (temporary) redirects over 301 (permanent)

**Chose:** 302 with CDN caching.
**Traded:** Slightly higher redirect latency (always hits CDN, not browser cache) in exchange for
**full analytics fidelity and the ability to update or expire links**. A 301 cached in the browser
is invisible to your analytics and impossible to revoke without user action.

### Decision 3 — Async analytics via Kafka over synchronous DB writes

**Chose:** Fire-and-forget Kafka event + Redis counter, drained to ClickHouse.
**Traded:** Up to 60-second lag on click counts for **zero added latency to the redirect hot path**
and linear write scalability. Synchronous analytics writes would halve the effective redirect
throughput and create a single point of failure (the analytics DB becoming the critical path).

---

## Key Takeaways

- **Read:write ratio drives everything.** 100:1 means you optimize entirely for the read path
  (multi-layer cache, CDN) and treat writes as the easy part. Identify this ratio in every
  design question.
- **Separate hot path from analytic path.** Never let an OLAP workload (click counts, dashboards)
  sit on the same DB as your sub-50 ms SLA hot path. Use a queue to decouple them.
- **Conditional writes are your distributed lock.** `ConditionExpression: attribute_not_exists`
  in DynamoDB (or `INSERT ... ON CONFLICT` in Postgres) replaces a read-then-write + external
  lock for uniqueness enforcement. It is atomic, cheap, and correct.
- **Cache invalidation is a product decision.** The choice between 301 and 302 is not just HTTP
  trivia — it determines who controls the cache (browser vs your CDN), which determines your
  ability to iterate on the product (link updates, expiry, A/B experiments).
- **Idempotency keys on writes.** Any distributed write that can be retried needs a client-supplied
  deduplication key. Store it in Redis with a short TTL; return the original response on replay.
  This pattern applies universally: payments, URL creation, order placement.
- **Cost-per-request visibility.** At 100 M redirects/day, a 0.1 ms average DynamoDB read costs
  ~$0.025/M reads ≈ $2.50/day. A cache hit costs ~$0.001/M ops. 95 % cache hit rate reduces
  DB cost by 20×. Always model the unit economics of your caching strategy.
