# Distributed Rate Limiter

**Prompt:** Design a rate limiter shared across many API servers.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you would ask

1. Who is the subject being limited — user, API key, IP, tenant?
2. What granularity of window — per second, per minute, per day?
3. Hard reject (429) or soft throttle (queue / degrade)?
4. Do we need per-endpoint limits or a single global quota?
5. What happens on Redis failure — fail open or fail closed?
6. Multi-region deployment? Do limits need to be globally consistent or per-region?

### Functional requirements (locked)

- Enforce a configurable request-per-window quota per `(client_id, endpoint)` pair.
- Return `429 Too Many Requests` with a `Retry-After` header when the limit is exceeded.
- Expose a management API to create/update/delete limit rules.
- Limits are enforced in real time — not best-effort async.

### Non-functional requirements (locked assumptions)

| Attribute | Target |
|---|---|
| Scale | 500 K API servers; 50 B requests / day |
| Peak QPS | ~2 M req/s (10x diurnal average) |
| Read:write ratio | Every request is both a read and a write (check-and-increment) |
| Latency budget | Rate-limit decision in < 5 ms p99 (not on the critical-path hot loop) |
| Consistency | Single-region: strong (no double-spend). Multi-region: eventually consistent with bounded drift (< 1 % over-admission acceptable). |
| Availability | 99.99 % — rate limiter failure must not block traffic (fail open with local fallback) |
| Durability | Counter state is ephemeral — losing a few seconds of counts on crash is acceptable |

---

## 2. Capacity Estimates

### Request volume

```
DAU = 50 M active clients
Average requests per client per day = 1 000
Daily total = 50 M × 1 000 = 50 B requests
Average QPS = 50 B / 86 400 ≈ 580 K req/s
Peak QPS (3x burst) ≈ 1.7 M req/s  →  round to 2 M req/s
```

### Redis counter storage

Each counter = `(client_id:endpoint:window_bucket)` → a 64-bit integer.

```
Unique (client, endpoint) pairs = 50 M clients × 20 endpoints = 1 B keys
Each Redis key ≈ 60 bytes key + 8 bytes value + ~50 bytes overhead = ~120 bytes
Hot set (active in last 1 min window) ≈ 5 % = 50 M keys
Hot-set memory = 50 M × 120 B ≈ 6 GB   →  one r7g.2xlarge node (64 GB) is ample
```

### Bandwidth to Redis

```
Each rate-limit call = one EVALSHA (Lua) round-trip ≈ 200 bytes on wire
2 M req/s × 200 B = 400 MB/s to Redis cluster
Spread across 6-shard cluster → ~67 MB/s per shard  (well within 10 Gbps NICs)
```

### Rule storage (relational)

```
Rules table: ~5 M rows × 500 B = 2.5 GB  → trivially fits in a small PostgreSQL instance
```

---

## 3. API Design

### Rate-limit check (internal, called by API gateway middleware)

```
POST /v1/ratelimit/check
Content-Type: application/json
Idempotency-Key: <uuid>          # idempotent so retries on network blip don't double-count

{
  "client_id": "usr_abc123",
  "endpoint":  "POST /v2/messages",
  "timestamp": 1748780000        # Unix seconds, supplied by caller for clock consistency
}

200 OK
{
  "allowed":     true,
  "limit":       1000,
  "remaining":   42,
  "reset_at":    1748780060,     # epoch of window boundary
  "retry_after": null
}

429 Too Many Requests
{
  "allowed":     false,
  "limit":       1000,
  "remaining":   0,
  "reset_at":    1748780060,
  "retry_after": 18              # seconds
}
```

### Rule management (internal admin)

```
GET    /v1/rules?client_id=&endpoint=   # list matching rules
POST   /v1/rules                        # create rule  { client_id, endpoint, limit, window_seconds }
PUT    /v1/rules/{rule_id}              # update limit or window
DELETE /v1/rules/{rule_id}             # soft-delete; counter continues until window expires
```

Notes: all write endpoints require an `Idempotency-Key` header; rules are versioned for audit.

---

## 4. Data Model

### Rule entity (PostgreSQL)

```
rules
  id           UUID PRIMARY KEY
  client_id    TEXT NOT NULL
  endpoint     TEXT NOT NULL          -- e.g. "POST /v2/messages" or wildcard "*"
  limit        INTEGER NOT NULL
  window_sec   INTEGER NOT NULL       -- 1, 60, 3600, 86400
  burst_factor FLOAT  DEFAULT 1.0    -- token-bucket burst multiplier
  created_at   TIMESTAMPTZ
  updated_at   TIMESTAMPTZ
  deleted_at   TIMESTAMPTZ            -- soft delete

INDEX (client_id, endpoint)           -- primary lookup
```

Why PostgreSQL: rules are low-volume, relational, need audit history, and are read at rule-load
time (cached in memory on each gateway pod). Writes are rare. SQL joins and versioning are
natural here.

### Counter entity (Redis)

Key schema: `rl:{client_id}:{endpoint_hash}:{window_bucket}`
- `window_bucket` = `floor(unix_ts / window_sec)` — changes the key every window, enabling
  natural TTL expiry.
- Value: integer, incremented atomically via Lua.
- TTL: `window_sec * 2` (keeps the previous window for sliding-window reads).

Why Redis, not SQL: ~2 M atomic increments/s at < 1 ms latency is the core requirement. Redis
single-threaded command model gives serializable counter ops without locking. Persistence
(RDB/AOF) is disabled — counters are ephemeral by design.

---

## 5. High-Level Architecture

```
                   ┌─────────────────────────────────────────────────┐
  Clients          │              API Gateway Cluster                 │
  ──────►  LB ──►  │  [Pod]  ──►  Rate-Limit Middleware              │
                   │              │  1. Check in-process local cache  │
                   │              │     (token-bucket per pod, 50ms)  │
                   │              │  2. EVALSHA → Redis Cluster       │
                   │              └─────────┬──────────────────────── │
                   │                        │  allowed / denied        │
                   │              ◄─────────┘                         │
                   │  [Pod]  ──►  Upstream Service                    │
                   └──────────────────────────────────────────────────┘
                                            │
                   ┌────────────────────────▼──────────────────────── ┐
                   │            Redis Cluster (6 shards, 3 replicas)   │
                   │   Shard assignment: consistent hash on client_id   │
                   └────────────────────────┬──────────────────────── ┘
                                            │
                   ┌────────────────────────▼──────────────────────── ┐
                   │   Rule Config Service  (reads from PostgreSQL)    │
                   │   Pushes rule updates to gateway pods via pub-sub │
                   └─────────────────────────────────────────────────  ┘
```

**Read path (check):**
1. Gateway middleware checks the pod-local token-bucket cache (< 1 µs, covers burst within pod).
2. On cache miss or bucket exhausted: executes the Redis Lua script for the authoritative counter.
3. Redis returns `{allowed, remaining, reset_at}`; middleware sets response headers and either
   passes the request or returns 429.

**Write path (rule change):**
1. Admin calls `PUT /v1/rules/{id}` on the Rule Config Service.
2. Config Service writes to PostgreSQL, then publishes a `rule_updated` event to a Redis pub-sub
   channel.
3. All gateway pods subscribe; on receipt they reload the rule from a local cache (backed by
   PostgreSQL replica). Rule changes propagate in < 500 ms.

---

## 6. Deep Dives

### 6.1 Algorithm choice: token bucket vs sliding window

| Algorithm | Accuracy | Memory | Burst behaviour | Complexity |
|---|---|---|---|---|
| Fixed window | Low (edge burst = 2x limit) | O(1) per key | Allows 2x burst at boundary | Trivial |
| Sliding window log | Exact | O(requests in window) | Precise | Expensive at high QPS |
| Sliding window counter | ~1 % error | O(1) per key | Smooth | Moderate |
| Token bucket | Exact within burst param | O(1) per key | Configurable burst | Moderate |

**Decision: sliding window counter for the general path; token bucket for burst-sensitive APIs.**

Sliding window counter formula:
```
rate = prev_count × (1 - elapsed/window) + curr_count
```
Requires two Redis keys (previous + current window bucket). Error is bounded at < 1 % because
the linear interpolation slightly over-counts at the window boundary — acceptable for most APIs.

Token bucket is kept for payment/sensitive endpoints where burst spikes must be strictly capped.
It stores `{tokens, last_refill_ts}` in a Redis hash; refill is computed lazily inside Lua.

### 6.2 Shared atomic counters in Redis with Lua

The core correctness problem: `GET` then `INCR` is not atomic from a gateway pod. Two pods can
both read `999/1000`, both increment, and both allow the request — admitting 1001.

**Solution: Lua script executed atomically on the Redis primary.**

```lua
-- EVALSHA <sha> 1 <key> <limit> <window_sec> <now_sec>
local key        = KEYS[1]
local limit      = tonumber(ARGV[1])
local window     = tonumber(ARGV[2])
local now        = tonumber(ARGV[3])
local bucket     = math.floor(now / window)
local prev_key   = key .. ":" .. (bucket - 1)
local curr_key   = key .. ":" .. bucket

local prev_count = tonumber(redis.call("GET", prev_key) or 0)
local curr_count = tonumber(redis.call("GET", curr_key) or 0)
local elapsed    = now % window
local rate       = prev_count * (1 - elapsed / window) + curr_count

if rate < limit then
  redis.call("INCR", curr_key)
  redis.call("EXPIRE", curr_key, window * 2)
  return {1, math.floor(limit - rate - 1), bucket * window + window}
else
  return {0, 0, bucket * window + window}
end
```

Redis executes Lua atomically — no other command runs between the two GETs and the INCR. The
script is loaded once (`SCRIPT LOAD`) and called by SHA to save bandwidth. The SHA is stored in
the pod's config on startup.

**Why not Redis transactions (MULTI/EXEC)?** MULTI/EXEC is optimistic; it aborts if a WATCHed
key is touched, requiring the pod to retry — which burns extra RTTs and is complex to reason
about. Lua gives unconditional atomicity with a single RTT.

### 6.3 Accuracy vs performance: the local cache layer

A pure Redis call per request at 2 M req/s × 1 ms latency = 2 000 simultaneous Redis connections.
That is manageable but burns CPU on the Redis side. Optimization: **pod-local token-bucket
pre-filter**.

Each pod keeps an in-process token bucket with a fraction of the global quota
(e.g., `quota / num_pods * 1.5` for headroom). If the local bucket is not exhausted, skip the
Redis call entirely. Sync the global counter back to Redis every 50 ms via a background flush.

Cost of this optimization: in the worst case (`num_pods` pods each burning their local budget
before syncing) over-admission is at most `quota × 0.5` per 50 ms window. For a 1 000 req/min
limit that is < 1 % overage — acceptable.

This reduces Redis calls by ~80 % in steady state, dropping Redis p99 load significantly and
improving end-to-end latency from ~4 ms to ~0.3 ms for the common allowed path.

### 6.4 Multi-region sync

Options for global limits across two regions (e.g., us-east-1 and eu-west-1):

| Approach | Consistency | Latency cost | Complexity |
|---|---|---|---|
| Single global Redis (one region) | Strong | +60–120 ms cross-region RTT | Low |
| Async replication + CRDT counter | Eventually consistent | Near-zero | High |
| Budget splitting (quota / 2 per region) | Bounded over-admission | Near-zero | Low |

**Decision: budget splitting with periodic re-balancing.**

Each region gets `quota / num_regions`. A background job (runs every 30 s) reads each region's
rolling usage via a side-channel metrics API and rebalances budgets proportionally. This adds at
most 30 s of lag when a region spikes, but eliminates cross-region synchronous calls on the hot
path. Over-admission bound = `quota × (1 - 1/num_regions)` for 30 s — stated explicitly in SLA.

For payment/billing endpoints where strong global limits are required: route all checks to a
single authoritative region (us-east-1) at the cost of +80 ms latency. This is configured
per-rule in the rules table (`consistency = "strong"`).

**Observability hook (2026 must-know):** every rate-limit decision emits a structured log line
(`client_id`, `endpoint`, `decision`, `remaining`, `region`, `algorithm`, `latency_us`) and a
counter metric. A per-`client_id` burn-rate alert fires when remaining drops below 10 % of quota
within the first 20 % of the window — catching runaway clients before they hit the hard wall.
Cost-per-request is tracked as a Prometheus label to attribute Redis cost to tenants.

---

## 7. Bottlenecks & Scaling

| Bottleneck | Symptom | Fix | Cost of fix |
|---|---|---|---|
| Redis CPU (single-threaded) | Lua script latency spikes > 2 ms | Shard by `client_id` (consistent hash) across 12 shards; each shard handles ~167 K req/s | Operational complexity; cross-shard scripts impossible (keep Lua to one key family) |
| Redis memory | OOM evictions evict active counters | Set `maxmemory-policy allkeys-lru`; hot set is 6 GB — add replica to scale reads | Cost; LRU eviction silently resets counters (mitigated by conservative TTL) |
| Network to Redis | Bandwidth saturation | Pipeline non-critical background flushes; use Unix sockets for co-located Redis | Pipelining adds up to 50 ms delivery lag for background flush |
| Rule-config propagation lag | Stale limits for 10–30 s after update | Push via Redis pub-sub (< 500 ms); pods re-read on cache miss | Extra Redis connection per pod for subscription channel |
| Single-region Redis failure | All traffic blocked or all traffic passes | Fail open: if Redis call times out (> 10 ms), allow request, record miss in local counter, alert | Over-admission during outage; requires post-incident quota correction |
| Hot-key client | One `client_id` on one Redis shard = hot spot | Append a random suffix `{0–9}` to spread across 10 sub-keys; sum reads across sub-keys in Lua | Increases Lua complexity; sub-key reads require a MULTI-key Lua — keep to one shard via hash tags |

---

## 8. Trade-offs & Summary

### Decision 1: Lua atomicity over MULTI/EXEC or application-level locking

Lua gives unconditional single-RTT atomicity. MULTI/EXEC would require watch-retry loops (2–3
RTTs on contention). Application-level locks (Redlock) add ~3 ms per lock and are fragile under
network partition. Trade-off: Lua scripts are opaque to Redis Cluster (must target one slot), so
all keys in a script must share a hash tag — constraining the key schema.

### Decision 2: Pod-local pre-filter (50 ms sync) over pure Redis path

This drops median latency from 4 ms to 0.3 ms and cuts Redis load by 80 %. Trade-off: up to
50 ms × `num_pods` × local_quota worth of over-admission is possible if pods crash mid-window
without flushing. Acceptable for non-billing APIs; disabled for `consistency = "strong"` rules.

### Decision 3: Budget splitting for multi-region over strong global counter

Eliminates 80 ms cross-region RTT on the critical path for the vast majority of clients.
Trade-off: a client who splits traffic across regions can exceed their global quota by up to
`(num_regions - 1) / num_regions × quota` for up to 30 s. This is disclosed in the SLA and
acceptable for rate limiting (which is a fairness mechanism, not a billing hard stop).

---

## Key Takeaways

1. **Lua scripts are the canonical way to implement atomic check-and-modify in Redis.** Any
   read-modify-write that must be consistent across processes belongs in a single Lua script, not
   in application code with WATCH/MULTI/EXEC.

2. **Layered defense is the right model for rate limiting.** Pod-local cache handles bursts and
   reduces load; the shared Redis counter is the authoritative source of truth; the rule store
   is the control plane. Each layer has a different consistency/latency profile.

3. **Fail open with observability beats fail closed for availability-critical infrastructure.**
   A rate limiter that brings down your API is worse than one that briefly admits extra traffic.
   Instrument the fallback path aggressively so you know when it fires.

4. **Multi-region forces you to be explicit about consistency requirements per rule.** Not all
   limits need strong global consistency. Splitting by sensitivity (soft quota vs hard billing
   cap) and routing accordingly is cleaner than a single global architecture.

5. **The sliding window counter's O(1) memory and bounded error (< 1 %) make it the default
   algorithm.** Reserve exact token bucket for endpoints where burst shape matters, and sliding
   window log only for compliance/audit where exactness is contractual.
