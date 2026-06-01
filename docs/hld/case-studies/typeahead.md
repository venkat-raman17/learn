# Search Autocomplete (Typeahead)

**Prompt:** Design a search-as-you-type suggestion service.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions

1. What is the corpus? Web search queries, product names, user handles? (Drives size and update cadence.)
2. How many suggestions per keystroke? (Typically 5–10.)
3. Should results be personalized, or is a single global ranking enough?
4. What latency SLA at the 99th percentile?
5. How fast must new/trending terms appear — seconds or hours?
6. Do we need prefix-only matching, or substring / fuzzy too?
7. Multi-language / Unicode normalization needed?

### Locked assumptions

| Dimension | Value | Reasoning |
|---|---|---|
| DAU | 500 M | Google-scale; scale down freely |
| Avg queries/user/day | 10 | Mix of power users and casual |
| Avg prefix keystrokes per query | 4 | User types ~4 chars before picking |
| Read:write ratio | ~1000:1 | Suggestions are read-heavy |
| Suggestion count | 10 per request | Standard UX |
| Latency target | p99 < 100 ms end-to-end | Above that, UX degrades |
| Consistency | Eventual | Stale by minutes is fine |
| Trending lag | ~15 min for new hot queries | Real-time is nice-to-have |
| Corpus size | 10 B unique query strings | Web-scale |
| Matching | Prefix only | Simplifies the data structure |
| Personalization | Global ranking only (v1) | Avoids per-user state at query time |

---

## 2. Capacity Estimates

### QPS

```
DAU = 500 M
actions/user/day = 10 queries × 4 keystrokes = 40 autocomplete requests
raw QPS = 500 M × 40 / 86,400 ≈ 231,000 RPS
peak multiplier = 3×  →  peak ≈ 700,000 RPS
```

This is read-only; write path (index updates) is ~700 RPS (1/1000 ratio).

### Storage

```
10 B query strings × 50 bytes avg = 500 GB raw
top-k metadata (score, count) per prefix: ~2–3× overhead → ~1.5 TB total trie/index
popularity counters: 10 B queries/day × 8 bytes = 80 GB/day raw logs (aggregated to ~5 GB/day)
```

### Bandwidth

```
Request: avg query prefix = 10 chars → ~50 bytes/request
Response: 10 suggestions × 30 chars avg = ~300 bytes/response
Outbound: 700,000 RPS × 300 B ≈ 210 MB/s ≈ 1.7 Gbps
```

A CDN edge layer absorbs most of this; origin sees a fraction.

### Cache (hot-set)

```
80/20 rule: top 20% of prefixes absorb 80% of traffic
Distinct prefix requests: ~10 M active prefixes/day
Hot set: 2 M prefixes × 300 B response = 600 MB → fits in a single Redis node, easily
With 1 KB headroom per key: 2 GB RAM for the hot set
```

---

## 3. API Design

### Suggestion endpoint (read path)

```
GET /v1/suggest?q=<prefix>&limit=10&locale=en-US
```

| Field | Details |
|---|---|
| `q` | URL-encoded prefix string (max 100 chars) |
| `limit` | 1–20, default 10 |
| `locale` | BCP-47 tag for language-aware normalization |
| Auth | None (public); rate-limited by IP/client-id via gateway |

**Response (200 OK)**
```json
{
  "suggestions": [
    { "text": "apple iphone 15", "score": 9821034 },
    { "text": "apple watch", "score": 7402891 }
  ],
  "served_from": "edge-cache",
  "request_id": "req_abc123"
}
```

The `served_from` field is observability sugar — tracks cache hit ratio per tier in dashboards.

### Ingest endpoint (write path, internal)

```
POST /internal/v1/ingest
Content-Type: application/json
Idempotency-Key: <client-generated UUID>

{ "query": "apple iphone 15", "count_delta": 1, "timestamp_ms": 1748789200000 }
```

Idempotency key prevents double-counting on retry. Gateway rejects duplicates for 24 hours.

---

## 4. Data Model

### Core entities

**QueryCount** — the source of truth for popularity ranking.

| Field | Type | Notes |
|---|---|---|
| `query_normalized` | string (PK) | lowercased, unicode-normalized |
| `count_7d` | int64 | rolling 7-day weighted count |
| `count_1d` | int64 | last 24h (for trending detection) |
| `updated_at` | timestamp | for incremental reindex |

**TrieNode / PrefixIndex** — the serving index, not a normalized table.

| Field | Type | Notes |
|---|---|---|
| `prefix` | string (PK) | e.g., `"app"` |
| `top_k` | json array | pre-computed top-10 `{text, score}` tuples |
| `version` | int | optimistic concurrency on update |

### Store choice

| Layer | Store | Justification |
|---|---|---|
| Raw event log | **Kafka** → S3/GCS | Append-only, durable, replay-able for recomputes |
| Popularity counts | **Redis** (atomic `ZINCRBY`) + periodic flush to Postgres | Sub-millisecond increments; Postgres as durable ledger |
| Serving index | **Redis Cluster** (hash by prefix) | All reads are point lookups by prefix key; no joins; O(1) GET dominates |
| Trie bulk storage | **Cassandra / DynamoDB** | Wide-row per prefix, horizontal scale, eventual consistency fine |

SQL is a poor fit here: the access pattern is almost entirely `key → top-k list` with no multi-table joins. NoSQL wins on this shape.

---

## 5. High-Level Architecture

```
                      ┌─────────────────────────────────────────────────────┐
                      │                  READ PATH                          │
  Browser/App         │                                                     │
      │               │  ┌────────┐    ┌──────────────┐   ┌─────────────┐  │
      │──GET /suggest─┼─►│  CDN   │───►│  API Gateway │──►│  Suggest    │  │
      │               │  │ (edge) │    │  (rate-limit)│   │  Service    │  │
      │               │  └────────┘    └──────────────┘   │  (stateless)│  │
      │               │   cache hit?         │             └──────┬──────┘  │
      │               │   → return early     │                    │         │
      │               │                      │             ┌──────▼──────┐  │
      │               │                      │             │ Redis Cluster│  │
      │               │                      │             │ (prefix→topk)│  │
      │               │                      │             └─────────────┘  │
      │               └─────────────────────────────────────────────────────┘
      │
      │               ┌─────────────────────────────────────────────────────┐
      │               │                  WRITE PATH                         │
      │               │                                                     │
      ├──search submit─┼─► API Gateway ──► Ingest Service ──► Kafka topic   │
      │               │                                          │          │
      │               │                              ┌──────────▼────────┐  │
      │               │                              │  Stream Processor │  │
      │               │                              │  (Flink / Spark   │  │
      │               │                              │   Streaming)      │  │
      │               │                              └──────────┬────────┘  │
      │               │                                         │           │
      │               │                              ┌──────────▼────────┐  │
      │               │                              │  Count Store      │  │
      │               │                              │  (Redis ZINCRBY + │  │
      │               │                              │   Postgres ledger)│  │
      │               │                              └──────────┬────────┘  │
      │               │                                         │           │
      │               │                  Batch reindex (hourly)│           │
      │               │                              ┌──────────▼────────┐  │
      │               │                              │  Trie Builder     │  │
      │               │                              │  (Spark batch job)│  │
      │               │                              └──────────┬────────┘  │
      │               │                                         │           │
      │               │                              ┌──────────▼────────┐  │
      │               │                              │  Redis Cluster    │  │
      │               │                              │  (atomic swap     │  │
      │               │                              │   new top-k keys) │  │
      │               └─────────────────────────────────────────────────────┘
```

**Read path:** Browser → CDN edge (check cache, TTL 60s) → API Gateway → Suggest Service → Redis Cluster `GET prefix` → return top-k.

**Write path:** User submits query → Ingest Service publishes to Kafka → Stream Processor increments Redis sorted-set counts → Hourly Spark batch rebuilds prefix→top-k map → atomic bulk-load into Redis Cluster.

---

## 6. Deep Dives

### 6.1 Trie + top-k per prefix

A classic trie stores every prefix and, at each node, the top-k completions by score. Naively this is enormous (10 B strings × avg 15 prefix nodes = 150 B nodes). Two practical approaches:

**Option A — In-memory compressed trie per shard.**
Use a radix trie (Patricia trie) to collapse single-child chains. Store only the top-k at each node (not all descendants). Memory: ~1.5 TB total; shard by first 2 chars (26² = 676 shards). Each shard holds ~2 GB in RAM — fits a single machine. Reads are O(len(prefix)) pointer hops, then return the cached list. Pro: blazing fast, no disk I/O. Con: expensive memory, rebuild on restart.

**Option B — Flat hash map of prefix → serialized top-k list (chosen).**
Skip the pointer-based trie entirely. Pre-compute, for every prefix up to length N (say 15), the top-k results and store them as a serialized blob keyed by the prefix string in Redis. `GET "app"` returns the blob. Pro: O(1) lookup, trivially shardable, Redis already in the stack. Con: storage grows as O(corpus × avg_prefix_len) but is bounded in practice (~1.5 TB across cluster, well within Redis Cluster capacity at 2026 hardware prices). Update requires re-emitting all prefixes of a changed query, but that is handled by the batch job.

**Chosen: Option B.** The simplicity and operational familiarity of Redis outweigh the memory "waste" of a flat map. The trie's O(len) advantage over O(1) is irrelevant at prefix lengths ≤ 15.

**Top-k maintenance.** A Redis sorted set (`ZADD prefix score query`) supports `ZREVRANGE prefix 0 9` for top-10 in O(log N). For the batch path, Spark computes exact top-k per prefix with a heap reduce; for the streaming path, a Count-Min Sketch + small heap approximates top-k with bounded error and O(1) updates per event.

### 6.2 Popularity ranking

Raw query count is a bad signal — "flu symptoms" spikes in winter, old queries stay high. Use a **time-decayed score**:

```
score(q, t) = Σ count_in_window_i × decay_factor^i
```

In practice: maintain a rolling 7-day count (heavier weight) and 1-day count (lighter but fresher) in two Redis sorted sets; blend them: `score = 0.7 × count_7d + 0.3 × count_1d × 7`. Recompute hourly in the batch job. For trending detection, flag queries where `count_1d / count_7d × 7 > 3×` — these get a prominence boost.

**Alternative: Exponential moving average (EMA).** `new_score = α × current_count + (1-α) × old_score`. Easier to stream-update but harder to explain to stakeholders. The blended-window approach wins here for interpretability and tuneability.

### 6.3 Sub-100ms latency

Budget allocation (p99, end-to-end):

| Hop | Budget |
|---|---|
| Network (client → CDN PoP) | 15 ms |
| CDN → origin (cache miss) | 10 ms |
| API Gateway overhead | 5 ms |
| Redis GET (in-region) | 2 ms |
| Serialization + response | 3 ms |
| **Total (cache miss)** | **35 ms** |

Cache hit at CDN: ~15 ms total — well under budget. On a cache miss the Redis GET at 2 ms is the dominant compute step; the rest is network. Key techniques:

- **CDN caching** with 60s TTL covers the long tail of common prefixes. Cache-key is `(prefix, locale)`.
- **Client-side debounce**: fire request only after 100ms keystroke idle. Halves actual RPS.
- **Prefetching**: after user types 3 chars, speculatively fetch top-10 for chars 4–6 in the background.
- **Coalescing**: deduplicate in-flight requests for the same prefix at the API Gateway layer.
- **Connection keep-alive + HTTP/2 multiplexing** between CDN and origin.

### 6.4 Real-time vs batch index updates

Two extremes:

| Approach | Latency to index | Complexity | Cost |
|---|---|---|---|
| Pure batch (Spark hourly) | ~60 min | Low | Low |
| Pure streaming (Flink, update Redis directly per event) | ~30 s | High | High |
| Hybrid (chosen) | ~15 min | Medium | Medium |

**Hybrid:** Stream processor (Flink) updates raw counts in Redis sorted sets continuously (near-real-time). A lightweight "mini-batch" job runs every 15 minutes, reads the dirty sorted sets, recomputes top-k for affected prefixes only, and swaps them in Redis atomically. Full Spark rebuild runs nightly to reconcile any drift. This gives 15-minute freshness at moderate cost without the full complexity of per-event trie updates.

**Atomic swap strategy:** Build new prefix→top-k entries under a `shadow:` key namespace, then pipeline `RENAME shadow:app app` for each changed prefix. Prevents readers from seeing a half-written state.

### 6.5 Caching strategy

Three-tier cache:

1. **Browser/client cache:** Cache last N prefix responses in memory (LRU, N=50). If user backtracks (deletes a char), serve instantly.
2. **CDN edge (Cloudflare/Fastly):** TTL 60s. Varies on `(q, locale)`. Purge on index rebuild via Cache-Tag headers (`tag: prefix-app`).
3. **Redis Cluster (origin cache):** TTL 5 min (fallback if CDN miss). Hot set ~2 GB, cold long-tail on Cassandra as fallback. Redis eviction policy: `allkeys-lru`.

Cache invalidation: after the 15-min mini-batch, push a CDN purge for the ~10,000 most-changed prefix tags. Don't purge everything — surgical invalidation preserves hit ratio.

---

## 7. Bottlenecks & Scaling

| Limiting resource | Symptom | Fix | Cost |
|---|---|---|---|
| Redis read throughput | p99 latency climbs on hot prefixes | Read replicas per shard (Redis Cluster replica) | 2× memory cost |
| Hot key ("a", "th", "in") | Single shard CPU saturated | Replicate hot-key shards × 10; route reads randomly across replicas | Operational complexity |
| CDN bandwidth | Egress bill spikes | Increase CDN TTL from 60s → 300s; accept slightly staler results | Freshness degrades |
| Kafka consumer lag | Index freshness degrades | Scale Flink parallelism; partition Kafka topic by `hash(prefix[0])` for locality | Engineering time |
| Batch job duration | Mini-batch takes > 15 min on large dirty set | Incremental Spark job — only process prefixes touched since last run (via Kafka offset watermark) | Spark complexity |
| Multi-region | Single-region failure = global outage | Active-active: replicate Redis Cluster to 3 regions (us-east, eu-west, ap-southeast); geo-route users to nearest; accept that index may be 1–2 mini-batch cycles behind across regions | 3× infra cost; conflict-free because writes are count increments, not CAS |

**Observability & cost-per-request (2026 must-have):**
- Emit `cache_tier` (client/cdn/redis/cassandra) on every response; dashboard shows hit ratio per tier.
- Tag each Redis key with `shard_id`; alert when any shard exceeds 80% CPU.
- Cost-per-request = `(CDN egress cost + Redis CPU cost + Kafka ingest cost) / RPS`. Target < $0.000010/request. Review monthly; a CDN TTL change from 60s → 120s can halve origin cost.
- Distributed tracing (OpenTelemetry) from browser to Redis; p99 waterfall reveals which hop regresses first.

---

## 8. Trade-offs & Summary

| Decision | What we chose | What we gave up |
|---|---|---|
| Flat prefix hash map vs pointer trie | Flat map in Redis — O(1) lookup, simple ops | Pointer trie would use 30–50% less memory but adds rebuild complexity and requires specialized in-process serving |
| Hybrid (15-min) index freshness vs pure streaming | Hybrid — simpler, cheaper | Pure streaming gives ~30s freshness; worth it only if "trending NOW" is a core product requirement |
| Global ranking vs personalization | Global (v1) — no per-user state at query time, massive cache hit ratio | Personalization lifts CTR ~10–15% (industry data) but requires user-context at serve time, killing CDN cacheability and adding privacy complexity |

The 15-min lag and global ranking are the two biggest conscious simplifications. A v2 that adds personalization would add a re-ranking step *after* the global top-k fetch, using a lightweight per-user model served from a separate low-latency store — preserving the CDN-cached global suggestions as the base.

---

## Key Takeaways

1. **Pre-compute aggressively at write time, not read time.** The entire read path is a single Redis GET. Every complexity lives in the offline/batch pipeline where latency doesn't matter to the user.

2. **Flat hash map beats elegant tree structures when the access pattern is pure point lookup.** Don't reach for a trie just because textbooks say "typeahead = trie." Benchmark both; the simpler one usually wins operationally.

3. **Three-tier caching (client → CDN → origin) is the real scaling lever.** At 700K RPS, even a 90% CDN hit ratio drops origin load to 70K RPS — a 10× reduction without changing any backend.

4. **Hybrid real-time + batch is the pragmatic middle ground** for freshness vs. cost. Pure streaming is seductive but expensive; pure batch is cheap but stale. The 15-min mini-batch covers 95% of "trending" use cases.

5. **Multi-region is additive here** because the write path is count increments (commutative, conflict-free). This is a rare case where active-active replication is straightforward — exploit it.

6. **Observability and cost-per-request are not afterthoughts.** Cache hit ratio is the single most important operational metric for this system. A TTL change can change your infra bill by 2×; you need to see it in real time.
