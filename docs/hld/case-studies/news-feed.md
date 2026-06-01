# News Feed (Twitter / Facebook)

**Prompt:** Design a home timeline aggregating posts from accounts a user follows.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you must ask

| Question | Why it matters |
|---|---|
| DAU and peak multiplier? | Drives QPS for every tier |
| Average followees per user? | Determines fan-out depth |
| What latency target for feed load? | Decides sync vs async fan-out |
| Eventual vs strong consistency on feed? | Allows read-your-own-writes to be a special case |
| Media (images / video) in scope? | Separates object store from metadata store |
| Chronological only, or ranked / algorithmic? | Ranking adds an ML inference hop |
| Geo-distribution (single region or global)? | Multi-region replication strategy |

### Locked assumptions

- **500 M DAU**, peak 3× average (so ~1.7 M concurrent active users at peak)
- **150 M tweets/day** written; **15 B feed reads/day** — **read:write ≈ 100:1**
- **P99 feed load ≤ 500 ms** (mobile cold start included)
- Eventual consistency is acceptable; stale feed up to ~10 s is fine
- Average follow graph: **300 followees** per user; celebrity defined as > 1 M followers
- Chronological feed with lightweight engagement-score boost (not full ML ranking)
- Two regions (US-East primary, EU secondary) with async replication; failover RTO < 60 s
- Tweets: text ≤ 280 chars + media refs; no media byte storage designed here

### Functional requirements

1. Post a tweet (text + optional media reference)
2. Fetch home timeline (paginated, newest-first)
3. Follow / unfollow a user
4. Like / retweet (write-path only; no counters in HLD scope)

### Non-functional requirements

- Feed P99 < 500 ms; post write P99 < 200 ms
- 99.99% availability for reads; 99.9% for writes
- Data durability 11-nines (replication + WAL archival)
- Idempotent writes (duplicate tweet submissions must be deduplicated)
- Observability: per-endpoint latency, error rate, cost-per-request tracked in real-time

---

## 2. Capacity Estimates

### Write QPS

```
150 M tweets/day ÷ 86,400 s ≈ 1,740 writes/s average
Peak (3×)                   ≈ 5,200 writes/s
```

### Read QPS

```
15 B feed reads/day ÷ 86,400 s ≈ 174,000 reads/s average
Peak (3×)                       ≈ 520,000 reads/s
```

### Fan-out writes (push model, partial)

```
1,740 tweets/s × 300 followees avg = 522,000 cache writes/s average
Peak                               ≈ 1.56 M cache writes/s
```

Celebrities (> 1 M followers) are **excluded** from push fan-out — handled on read (see §6).

### Storage per day

```
Tweet row: ~500 bytes (text + metadata)
150 M tweets × 500 B = 75 GB/day raw
With 3× replication    = 225 GB/day
1 year retention       ≈ 82 TB
```

### Cache (hot-set) size

```
Top 10% of users account for ~80% of feed reads (power-law).
Active user feeds cached: 50 M users × 200 tweet IDs × 8 bytes = 80 GB per region
Add 2× headroom for serialization overhead             ≈ 160 GB RAM
```

Redis cluster of 8 × 32 GB nodes (256 GB total) covers the hot set with room for growth.

### Bandwidth

```
Feed response: 200 tweet IDs × 8 B + metadata ≈ 4 KB per request
520,000 reads/s × 4 KB = ~2 GB/s outbound (peak)
CDN absorbs static assets; core API bandwidth ≈ 500 MB/s after caching
```

---

## 3. API Design

All endpoints use **REST over HTTPS**; internal service calls use **gRPC**. Auth via JWT (Bearer). Pagination uses opaque cursor (see §6).

```
POST   /v1/tweets
  Body:  { text: string, media_ids: [string], idempotency_key: uuid }
  Returns: 201 { tweet_id, created_at }
  Notes: idempotency_key stored 24 h; duplicate returns 200 with original tweet.

GET    /v1/feed
  Query: cursor=<opaque>, limit=20 (max 100)
  Returns: 200 { tweets: [TweetView], next_cursor, has_more }

POST   /v1/follows
  Body:  { target_user_id: uuid }
  Returns: 201 {}

DELETE /v1/follows/{target_user_id}
  Returns: 204 {}

GET    /v1/tweets/{tweet_id}
  Returns: 200 TweetView   (used by hydration service)
```

`TweetView` is a denormalized projection (author handle, avatar URL, text, like_count, retweet_count, created_at) assembled by the **Hydration Service** to avoid N+1 DB calls.

---

## 4. Data Model

### Core entities

**User** — `user_id (UUID PK), handle, display_name, avatar_url, follower_count, followee_count, created_at`

**Tweet** — `tweet_id (UUID PK, time-sortable: UUIDv7), author_id (FK), text, media_refs[], created_at, is_deleted`

**Follow** — `(follower_id, followee_id) composite PK, created_at` — no other columns needed

**FeedItem** (materialized, denormalized) — `(user_id, tweet_id) PK, score FLOAT, created_at` — lives in Redis sorted set, **not** a durable table

### Store choices

| Entity | Store | Justification |
|---|---|---|
| User, Tweet | **PostgreSQL** (sharded by user_id) | Relational integrity, strong consistency for source-of-truth; UUID sharding avoids hotspots |
| Follow graph | **Cassandra** (wide-row: follower_id → [followee_ids]) | Write-heavy, wide rows, no joins needed; fan-out query is a single partition scan |
| Feed cache | **Redis Sorted Set** keyed by `feed:{user_id}` | O(log N) inserts, O(1) range reads, natural TTL eviction |
| Tweet metadata for hydration | **Redis hash** keyed by `tweet:{tweet_id}` | Read-amplification avoidance; short TTL (1 h) with DB fallback |
| Media objects | **S3-compatible object store** | Not in tweet DB; tweet stores only the object key |

---

## 5. High-Level Architecture

```
                          ┌────────────────────────────────────────┐
Client (mobile/web)       │               CDN / Edge               │
        │                 │  (static assets, public profile pages)  │
        │ HTTPS           └────────────────────────────────────────┘
        ▼
 ┌─────────────┐
 │  API Gateway│  (rate limiting, auth JWT validation, routing)
 └──────┬──────┘
        │ gRPC fan-out
   ┌────┴─────────────────────────┐
   │          Service Mesh        │
   ├──────────────┬───────────────┤
   │  Tweet Svc   │   Feed Svc    │
   │  (write)     │   (read)      │
   └──────┬───────┴───────┬───────┘
          │               │
   ┌──────▼──────┐  ┌─────▼──────────────┐
   │  Kafka      │  │  Redis Cluster     │
   │  (fan-out   │  │  feed:{user_id}    │
   │   topic)    │  │  tweet:{tweet_id}  │
   └──────┬──────┘  └─────┬──────────────┘
          │               │ cache miss
   ┌──────▼──────┐  ┌─────▼──────┐
   │  Fan-out    │  │ Hydration  │
   │  Workers    │  │ Service    │
   └──────┬──────┘  └─────┬──────┘
          │               │
   ┌──────▼──────┐  ┌─────▼──────┐
   │  Cassandra  │  │  Postgres  │
   │  (follows)  │  │  (tweets,  │
   └─────────────┘  │   users)   │
                    └────────────┘
```

**Write path:** Client → API Gateway → Tweet Service (writes to Postgres, emits `tweet.created` event to Kafka) → Fan-out Workers consume event, read followee list from Cassandra, push tweet_id + score into Redis sorted set `feed:{follower_id}` for each non-celebrity follower.

**Read path:** Client → API Gateway → Feed Service → Redis `ZREVRANGE feed:{user_id} 0 19` → Hydration Service fetches `tweet:{tweet_id}` from Redis hash (or Postgres on miss) → merges celebrity tweets on-the-fly (pull model) → assembles `TweetView[]` → returns with cursor.

---

## 6. Deep Dives

### 6.1 Fan-out on Write vs Fan-out on Read

**Fan-out on write (push model):** When a tweet is posted, workers immediately write the tweet_id into every follower's feed cache. Feed reads become a single `ZREVRANGE` — extremely fast.

Downsides: Write amplification proportional to follower count. A user with 10 M followers triggers 10 M Redis writes per tweet. At 5,200 tweets/s peak that is 5.2 B Redis writes/s — impossible to sustain universally.

**Fan-out on read (pull model):** Feed reads fetch the list of followees, query each followee's recent tweets, merge and sort. No write amplification. Downside: a user following 300 accounts means 300 DB queries per feed load — 300× read amplification, blowing the 500 ms latency budget.

**Decision: Hybrid (industry standard)**

- **Regular users (< 10 K followers):** fan-out on write. Workers push tweet_ids to follower feed caches asynchronously via Kafka. Median user (~300 followers) costs 300 Redis writes per tweet — acceptable at scale.
- **Celebrity users (> 1 M followers):** fan-out on read at serve time. Feed Service merges celebrity tweets from a per-celebrity sorted set (`celebrity_feed:{user_id}`) into the caller's pre-built feed. This merging step adds ~20 ms (one Redis read per followed celebrity, parallelized).
- **Threshold is dynamic:** a background job re-classifies users crossing 10 K/1 M boundaries and drains or fills their push queues accordingly.

### 6.2 The Celebrity / Hot-Key Problem

Celebrity feeds are themselves hot keys: millions of followers all reading `celebrity_feed:{celeb_id}` simultaneously.

Mitigations:
1. **Read replicas in Redis** — Redis Cluster with 3 read replicas per shard; celebrity keys land on a designated shard, replicas absorb read traffic.
2. **Local in-process cache in Feed Service pods** — a 5-second TTL Caffeine/Guava cache per pod holds the last 200 celebrity tweet IDs. At 500 Feed Service pods, this reduces Redis celebrity reads by ~99% during hot windows (e.g., celebrity tweets during a live event).
3. **Key rehashing / virtual shards** — consistent hashing spreads celebrity keys across more shards by appending a suffix (`celebrity_feed:{id}:0`, `:1`, ...) and reading all shards in parallel — useful if one celebrity dominates a single shard.

### 6.3 Feed Ranking

Pure chronological is a degenerate case: insert by `created_at` as sort score. Lightweight boost adds engagement signals:

```
score = created_at_unix + engagement_boost
engagement_boost = log(1 + likes_1h) * w1 + log(1 + retweets_1h) * w2
```

Weights `w1`, `w2` are tuned offline and deployed as a feature flag. Score is computed once in the Fan-out Worker at write time and stored in the Redis sorted set. This avoids per-request scoring (no ML inference on the read path). For full algorithmic ranking, an ML inference service sits between Feed Service and the client, re-ranks the pre-fetched candidate set — adds ~30–50 ms but stays within budget.

### 6.4 Cursor Pagination

Offset-based pagination (`?page=3`) is broken at scale: rows shift as new tweets arrive between requests, and large offsets require DB scans.

**Keyset / cursor pagination:**

Cursor encodes the last-seen `(tweet_id, score)` tuple, base64-encoded and opaque to the client:

```
cursor = base64({ tweet_id: "01HXY...", score: 1748777600.42 })
```

Feed Service executes: `ZREVRANGEBYSCORE feed:{user_id} (score -inf LIMIT 20` (exclusive lower bound). This is O(log N + M) in Redis regardless of page depth. On resume after app reopen, the client sends the stored cursor; the server returns the next page anchored exactly at the last seen item — no duplicates, no gaps. Cursors expire after 7 days (TTL in the cursor store, or rely on the feed cache eviction).

---

## 7. Bottlenecks & Scaling

| Bottleneck | Symptom | Fix | Cost |
|---|---|---|---|
| Redis fan-out write throughput | Fan-out lag > 5 s at peak | Increase Kafka consumer parallelism; add Redis shards | Kafka partition increase is free; Redis shards add ~$500/shard/month |
| Feed Service read QPS | P99 latency spike, CPU saturation | Horizontal scale (stateless pods behind LB); in-process celebrity cache | Linear infra cost; cache adds GC pressure |
| Postgres write throughput | Tweet insert lag, replication lag | Write to a single shard-leader; add PgBouncer connection pooling; async replicas for reads | PgBouncer is free; read replicas ~$200/node/month |
| Hydration N+1 reads | Cache miss storm on viral tweet | Multi-get (`MGET`), batch hydration; prefill tweet cache on fan-out | Engineering cost; no infra cost |
| Follow-graph scan (Cassandra) | High fan-out worker latency for power users | Pre-paginate followee lists into 1 K-ID chunks stored in Cassandra rows | Storage doubles for large graphs |
| Cross-region replication lag | EU users see stale US-origin tweets | Async Kafka topic mirroring (MirrorMaker 2); read-your-own-writes via sticky routing to primary for 30 s post-write | Additional Kafka cluster ~$2 K/month |

**Observability & cost-per-request (2026 must):** Every service emits `service, endpoint, p50, p99, error_rate, upstream_cost_usd` as structured logs → forwarded to a time-series store (Prometheus + Grafana). Alerts fire at p99 > 400 ms or error_rate > 0.1%. Cost-per-request is tracked by tagging Redis, Postgres, and Kafka calls with the originating endpoint — allows identifying that, e.g., `/v1/feed` costs $0.0003 per call in infrastructure, used for capacity planning and SLO budgeting.

---

## 8. Trade-offs & Summary

| Decision | What you gain | What you give up |
|---|---|---|
| **Hybrid fan-out (push normal, pull celebrity)** | Sub-10 ms feed reads for 99% of users | Added complexity: two code paths, dynamic user reclassification, potential inconsistency window when a user crosses the threshold |
| **Pre-materialized feed in Redis (denormalized)** | Feed read is one `ZREVRANGE` call; no join at read time | Write amplification; cache invalidation complexity; stale data up to fan-out lag (~2–5 s); Redis infra cost (~$4 K/month for 160 GB) |
| **Keyset cursor pagination over offset** | Stable pages as feed grows; O(log N) performance at any depth | Cursors are opaque (no "jump to page 50"); users cannot deep-link to an arbitrary page; cursor storage adds a small lookup layer |

---

## Key Takeaways

- **Read:write ratio dominates architecture.** A 100:1 read-heavy system justifies heavy write-time denormalization (materialized feeds) that would be wasteful in a write-heavy system.
- **Fan-out amplification is the core tension** in any social feed; the hybrid push/pull pattern (with a dynamic celebrity threshold) is the canonical industry resolution — know it cold.
- **Hot-key mitigation is layered:** Redis replicas handle steady-state; in-process caches absorb flash-crowd spikes; key rehashing handles single-shard dominance. Apply all three together.
- **Opaque cursors are strictly superior to offset pagination** for any feed that changes in real time — this generalizes to notifications, search results, audit logs.
- **Idempotency keys on writes** (tweet creation) prevent duplicate posts under client retries — a 2026 baseline expectation in any distributed write path.
- **Observability as a first-class concern:** cost-per-request tagging lets you make capacity decisions based on real economics, not guesswork — expected in staff-level discussions.
- **Multi-region is not free:** async replication introduces lag; read-your-own-writes requires sticky routing or version tokens; fail-over requires DNS TTL discipline and health-check tuning.
