# TinyLink — Reference Architecture (Design Answer Key)

> This file is the answer key. Open it AFTER you have committed your own design decisions.
> Divergences are fine; unjustified divergences are a flag.

---

## Components & Responsibilities

| Component | Responsibility |
|---|---|
| `LinkService` | Validate URL, generate base62 code, write to Postgres, write-through to Redis, publish `LinkCreated` event (optional) |
| `RedirectController` | Look up code in Redis; on miss, hit Postgres; return 302; publish `ClickEvent` to Kafka asynchronously |
| `AnalyticsConsumer` | `@KafkaListener` on `click-events`; upsert `click_aggregates` row for the current hour bucket |
| `AnalyticsController` | Read aggregated rows from Postgres; expose `GET /api/links/{code}/analytics` |
| Redis | Hot-path cache: `code → original_url` with TTL; idempotency key store |
| Kafka | Decouples redirect path from analytics writes; absorbs write bursts |
| Postgres | Source of truth for links and aggregated analytics |

---

## Data Model

```sql
-- links: one row per short code
CREATE TABLE links (
    code        VARCHAR(10)  PRIMARY KEY,
    original_url TEXT        NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    expires_at  TIMESTAMPTZ,
    owner_id    UUID                         -- nullable for anonymous
);

-- idempotency: deduplicate shorten requests
CREATE TABLE idempotency_keys (
    key         VARCHAR(128) PRIMARY KEY,
    code        VARCHAR(10)  NOT NULL REFERENCES links(code),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX ON idempotency_keys(created_at); -- for TTL cleanup job

-- click_aggregates: pre-aggregated by hour (avoids full table scan on dashboard)
CREATE TABLE click_aggregates (
    code        VARCHAR(10)  NOT NULL REFERENCES links(code),
    hour_bucket TIMESTAMPTZ  NOT NULL,       -- truncated to the hour
    count       BIGINT       NOT NULL DEFAULT 0,
    PRIMARY KEY (code, hour_bucket)
);
```

Key design choices:
- No raw `click_events` table — Kafka is the log; Postgres only stores aggregates (write amplification is acceptable; raw replay goes through Kafka if needed).
- `hour_bucket = date_trunc('hour', event_timestamp)` keeps cardinality bounded.
- `ON CONFLICT (code, hour_bucket) DO UPDATE SET count = click_aggregates.count + EXCLUDED.count` — idempotent upsert from consumer.

---

## REST API

```
POST /api/links
  Body:  { "url": "https://...", "idempotencyKey": "uuid" }
  201:   { "code": "aB3xZ9", "shortUrl": "https://tinylink.io/aB3xZ9" }
  409:   idempotency collision (same key, different URL)

GET /:code
  302 → original URL        (cache hit or DB hit)
  404 → code unknown
  410 → code expired

GET /api/links
  200: [ { "code", "originalUrl", "createdAt", "totalClicks" } ]

GET /api/links/{code}/analytics
  200: { "code", "totalClicks", "hourly": [ { "bucket": "2026-06-01T14:00Z", "count": 42 } ] }
```

Header convention: `X-Idempotency-Key` is also accepted as an alternative to the body field.

---

## Kafka Event Flow

### Topic layout

```
click-events  (3 partitions, key = short code)
```

Partitioning by code ensures all events for a given code land in the same partition, preserving
order and enabling per-partition aggregation without distributed coordination.

### ClickEvent schema (JSON, keep it small)

```json
{ "code": "aB3xZ9", "ts": 1748784000000, "referrer": "https://twitter.com", "ip": "1.2.3.4" }
```

### Sequence: redirect → aggregate

```
Client          RedirectController      Redis       Postgres      Kafka        AnalyticsConsumer
  |  GET /aB3xZ9      |                   |            |             |                |
  |─────────────────> |                   |            |             |                |
  |                   |── GET aB3xZ9 ──>  |            |             |                |
  |                   |<── HIT url ───────|            |             |                |
  |<── 302 url ───────|                   |            |             |                |
  |                   |── publish ClickEvent ─────────────────────> |                |
  |                   |                   |            |             |── consume ───> |
  |                   |                   |            |             |                |── UPSERT aggregate
```

The publish is fire-and-forget (`kafkaTemplate.send(...)` without blocking). If Kafka is
unavailable, the redirect still succeeds; events are lost (acceptable per eventual-consistency SLA).
For stricter guarantees, use an outbox table + Debezium CDC.

---

## Idempotency & Consistency Approach

**Shorten idempotency:**
1. On `POST /api/links`, look up `idempotency_keys` by `idempotencyKey`.
2. If found and URL matches → return existing code (200, not 201).
3. If found and URL differs → 409 Conflict.
4. If not found → generate code, insert both rows in a single transaction.

Redis shortcut: cache `idempotency:{key} → code` with a 24 h TTL to avoid the DB lookup on
hot re-submissions.

**Analytics eventual consistency:**
- Consumers use `enable.auto.commit=false`; commit offset only after successful upsert.
- On consumer restart, re-processing the same event is safe: the upsert is idempotent
  (`ON CONFLICT DO UPDATE`).
- Kafka retention = 7 days → replay window if consumer falls behind.

---

## Caching Strategy

| Operation | Cache action | TTL |
|---|---|---|
| Redirect (hit) | Read `code → url` from Redis | — |
| Redirect (miss) | Populate Redis after DB read | 1 h (configurable) |
| Shorten (write) | Write-through: set in Redis immediately | 1 h |
| Idempotency | `SET NX EX 86400 idempotency:{key} code` | 24 h |
| Expiry | `EXPIREAT code unix_ts` on links with `expires_at` | link TTL |

Redis eviction policy: `allkeys-lru`. If Redis is down, `RedirectController` catches
`RedisConnectionFailureException`, falls back to Postgres, and increments a `cache.miss.fallback`
Micrometer counter.

---

## Failure Modes

| Failure | Behaviour | Recovery |
|---|---|---|
| Redis down | Fallback to Postgres; metric incremented | Redis reconnects automatically (Lettuce retry) |
| Kafka down | Redirect succeeds; ClickEvent dropped; warn log | Events lost; acceptable per SLA |
| Postgres down | 503 on shorten + redirect | Circuit breaker (Resilience4j) opens after 5 failures |
| Consumer lag spikes | Analytics data is stale but not incorrect | Auto-scale consumer instances (same consumer group) |
| Duplicate ClickEvents | Consumer upsert is idempotent; count correct | No action needed |

---

## Base62 Key Generation (the Coding Skill)

```
ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

function toBase62(id: long): String
    sb = StringBuilder()
    while id > 0:
        sb.prepend(ALPHABET[id % 62])
        id /= 62
    return sb.padStart(6, '0')
```

`id` comes from a Postgres sequence (`links_id_seq`) or a distributed counter (Snowflake ID if
multi-region). 6 chars of base62 = 62^6 ≈ 56 billion unique codes — sufficient.

Collision risk with random generation: use DB unique constraint + retry loop (max 3 retries) as
safety net. Sequence-based generation has zero collision risk.

---

## Scaling Notes

- **Read-heavy** (100:1 read/write typical): Redis absorbs the redirect load; Postgres only
  takes cache misses and analytics reads.
- **Horizontal API scaling**: Spring is stateless; add instances behind a load balancer. Redis
  and Kafka are shared infrastructure.
- **Kafka consumer scaling**: up to 3 instances (one per partition); beyond that, increase
  partition count first.
- **Analytics at scale**: if hourly buckets are too coarse, switch to 5-min buckets or stream
  directly to a time-series DB (TimescaleDB extension on Postgres is a zero-infra-change upgrade).
- **Hot codes**: a single viral link saturates one Redis key — acceptable; Redis can handle
  ~1M ops/s on a single node. Cache stampede protection: use `SET NX` with a short lock TTL
  before the DB read.
