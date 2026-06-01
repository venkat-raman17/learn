# Ad Click Aggregator

**Prompt:** Design near-real-time ingestion and aggregation of ad-click events.

> Try it yourself first, then read on.

---

## 1. Requirements

### Functional
- Ingest raw ad-click events (click_id, ad_id, user_id, campaign_id, timestamp, country, device).
- Aggregate clicks by (ad_id, time-window) — 1-min, 5-min, 1-hour buckets.
- Query API: total clicks and unique users per ad, per campaign, filterable by country/device, over a time range.
- Billing hook: downstream billing system reads hourly roll-ups for cost-per-click invoicing.

### Non-functional
- **Scale:** 1 B ad impressions/day; click-through rate ~1 % → ~10 M clicks/day.
- **Write QPS:** 10 M / 86,400 ≈ 116 avg; peak (primetime, flash sales) 10× → ~1,200 writes/s.
- **Read QPS:** dashboards poll every 30 s; ~500 concurrent advertisers → ~17 reads/s avg, ~100 reads/s peak. Read:write ≈ 1:12 (write-heavy).
- **Latency:** click ingestion acknowledged < 100 ms (p99); aggregated results stale by at most 5 min acceptable; billing roll-ups T+15 min ok.
- **Consistency:** at-least-once delivery acceptable; idempotency at the aggregation layer makes duplicates safe. Billing requires exactly-once-ish reconciliation.
- **Retention:** raw events 30 days; aggregates 2 years.
- **Availability:** 99.99 % for ingestion (revenue-critical); 99.9 % for query API.

### Clarifying questions you would ask
1. Do advertisers need real-time (sub-second) dashboards or near-real-time (< 5 min)?
2. Is fraud detection in scope? (separate service, but affects deduplication SLA)
3. Do we need per-user attribution, or only aggregate counts? (shapes storage schema)
4. Multi-region from day 1, or single-region with DR?

### Locked assumptions
- Near-real-time (< 5 min lag) is sufficient; billing is T+15 min.
- Fraud detection is out of scope; dedup by click_id only.
- Single primary region (us-east-1) with read replicas in eu-west-1 and ap-southeast-1. Global writes route to primary.

---

## 2. Capacity Estimates

### QPS
| Signal | Value |
|---|---|
| Avg write QPS | 116 /s |
| Peak write QPS (10×) | 1,200 /s |
| Avg read QPS | 17 /s |
| Peak read QPS | 100 /s |

### Storage
- Raw event size: ~200 bytes (JSON) → ~500 bytes Avro on disk after encoding.
- Events/day: 10 M → **5 GB/day** raw. 30-day retention → **150 GB** raw cold storage (S3/object store, cheap).
- Aggregates: ~1 M unique ad_ids; 1-min buckets × 1,440 min/day × 1 M ads = 1.44 B rows/day, but sparse (most ads get zero clicks most minutes). Realistically ~50 M rows/day × 50 bytes → **2.5 GB/day** aggregate. 2-year retention → ~1.8 TB.

### Bandwidth
- Ingest: 1,200 writes/s × 500 bytes = **600 KB/s** inbound → negligible.
- Stream processing internal: 600 KB/s replicated 3× across Kafka brokers → ~2 MB/s.

### Cache hot-set
- Hot ads (top 1 % of 1 M = 10,000 ad_ids) × 3 granularities × ~200 bytes = **6 MB** — fits trivially in Redis. Realistically cache the last 24 h of hourly aggregates for all ads: 1 M × 24 × 50 bytes = **1.2 GB** → single Redis node is fine.

---

## 3. API Design

```
# Ingest (called by ad-server SDK, fire-and-forget with ack)
POST /v1/clicks
Headers: X-Idempotency-Key: <click_id>        # prevents double-counting on retry
Body:    { ad_id, user_id, campaign_id, timestamp, country, device, ... }
Response 202 Accepted: { status: "queued" }

# Aggregate query
GET /v1/aggregates?ad_id=&campaign_id=&granularity=1m|5m|1h
                  &start=<ISO8601>&end=<ISO8601>
                  &filter[country]=US&filter[device]=mobile
Response 200:
{
  "ad_id": "ad_42",
  "granularity": "5m",
  "buckets": [
    { "window_start": "2026-06-01T10:00:00Z", "clicks": 1234, "uniques": 987 },
    ...
  ]
}

# Billing roll-up (internal, service-to-service, not public)
GET /internal/v1/billing?campaign_id=&hour=2026-06-01T10:00:00Z
Response 200: { "campaign_id": "c_7", "hour": "...", "clicks": 45000, "cost_usd": 90.00 }
```

Key choices: REST over gRPC for ingestion (easy for ad-server SDKs in any language); gRPC for internal billing service (schema-safe, streaming). Idempotency key on POST is the click_id — upstream ad-server stamps it once, so retries are safe.

---

## 4. Data Model

### Core entities

**raw_clicks** (event store — Kafka + S3 Parquet)
```
click_id      UUID   PK  (idempotency / dedup)
ad_id         UUID
campaign_id   UUID
user_id       UUID   (nullable; guest clicks allowed)
timestamp     TIMESTAMPTZ
country       CHAR(2)
device        ENUM(mobile, desktop, tablet)
ip_hash       CHAR(64)   (for fraud, hashed at ingest)
```

**click_aggregates** (time-series store — Apache Druid or ClickHouse)
```
ad_id         UUID
window_start  TIMESTAMPTZ
granularity   ENUM(1m, 5m, 1h)
clicks        BIGINT
uniques       BIGINT      (HyperLogLog sketch — 2% error, 1/1000 the size)
country       CHAR(2)     (pre-bucketed dimension)
device        ENUM
```
Partition key: (ad_id, granularity, window_start). Sorted by window_start DESC within partition.

**Store choice justification:**
- Raw events: Kafka (durable log, replay) + S3 Parquet (cheap cold storage, Athena/Spark queryable). Not a transactional DB — append-only, no updates.
- Aggregates: **ClickHouse** (columnar OLAP). Access pattern is always a range scan on (ad_id, time range) with GROUP BY — columnar compression + vectorized execution beats row stores by 10–100×. Alternative: Apache Druid (better for sub-second queries, higher operational cost). A plain Postgres with TimescaleDB would work up to ~10 M rows/day but falls over at our 50 M row/day write rate.
- Hot-path cache: Redis (sorted sets for leaderboards, strings for pre-computed JSON blobs of recent aggregates).
- Dedup store: Redis SET of recent click_ids with 24-h TTL (bloom filter if memory is a concern at scale).

---

## 5. High-Level Architecture

```
Ad Server (SDK)
     |
     | POST /v1/clicks (HTTPS, with idempotency key)
     v
 [API Gateway / Load Balancer]  (rate-limit per advertiser, TLS termination)
     |
     v
 [Click Ingest Service]  (stateless, horizontally scaled, Go/Rust)
     |  1. Validate & enrich (geo-IP lookup via local MaxMind DB)
     |  2. Check Redis dedup SET for click_id (DROP if seen)
     |  3. Publish to Kafka topic: clicks-raw  (ack after Kafka leader write)
     |  4. Return 202 to ad server
     v
 [Kafka: clicks-raw]  (3 brokers, 64 partitions, keyed on ad_id)
     |
     +---> [Stream Processor: Flink/Kafka Streams]
     |          - Tumbling windows: 1 m, 5 m, 1 h
     |          - HyperLogLog for uniques
     |          - Emits aggregate records to: clicks-agg topic
     |
     +---> [S3 Sink Connector]  raw Parquet files (cold archive, Athena)
     |
     v
 [Kafka: clicks-agg]
     |
     +---> [Aggregate Writer Service]  upsert into ClickHouse
     |
     +---> [Cache Warmer]  write hot ad aggregates to Redis (TTL 10 min)

QUERY PATH
 Advertiser Dashboard
     |
     v
 [Query API Service]  (stateless)
     |
     +-- Cache HIT  --> Redis (sub-ms)
     |
     +-- Cache MISS --> ClickHouse (10–50 ms for range scans)
                    --> backfill Redis
```

**Write path:** Ad-server SDK → HTTPS POST → Ingest Service (dedup + enrich) → Kafka clicks-raw → Flink (window aggregation) → ClickHouse + Redis.

**Read path:** Dashboard → Query API → Redis (HIT, ~1 ms) or ClickHouse (MISS, ~20 ms) → response.

---

## 6. Deep Dives

### 6.1 High-Throughput Ingestion

1,200 writes/s is modest today but can spike 50× on Super Bowl ad campaigns (60,000 /s). The ingest service must be:

- **Stateless** — Kubernetes HPA on CPU/RPS; each pod does geo-IP lookup from an in-memory MaxMind database (updated nightly via S3, no network hop).
- **Async ack** — return 202 after Kafka leader acknowledges (`acks=1`). Full ISR sync (`acks=all`) adds ~5 ms latency; accept the tradeoff — a broker crash losing 1 s of clicks is worth 5× throughput gain.
- **Batching at the SDK** — ad-server SDKs buffer clicks for 200 ms and send micro-batches; cuts HTTP overhead by ~50×.
- **Back-pressure** — if Kafka consumer lag exceeds threshold, Ingest Service returns 429 with `Retry-After`; upstream SDK retries with exponential backoff + jitter.

### 6.2 Stream Processing and Windowed Aggregation

**Flink** is the choice over Kafka Streams because:
- Native exactly-once with two-phase commit across Kafka source and ClickHouse sink.
- `ProcessWindowFunction` with event-time semantics handles late arrivals (ad-server clocks drift ±30 s).
- Stateful operators checkpoint to S3 every 30 s — recovery from a pod crash replays only since last checkpoint.

Window design:
- **Tumbling 1-min windows** close when Flink watermark passes `window_end + 30 s` (late-data allowance).
- **Aggregation state:** Flink keeps a `ConcurrentHashMap<(ad_id, country, device), HLLSketch>` per window in managed memory (RocksDB state backend). HyperLogLog sketches merge across partitions without full deduplication cost.
- **Output trigger:** each closed window emits one aggregate record per (ad_id, country, device) tuple to clicks-agg Kafka topic.

Alternative: **Kafka Streams** — simpler operationally (no separate Flink cluster), but exactly-once across an external sink (ClickHouse) requires manual transactional logic. Acceptable if the team is small; migrate to Flink when exactly-once billing matters.

### 6.3 Exactly-Once / Idempotency

Three layers:
1. **Click dedup (Redis SET):** Ingest service checks `SISMEMBER dedup:{click_id}` before publishing to Kafka. TTL 24 h catches duplicate retries from ad-servers. False positive rate: ~0 % with exact match (vs bloom filter's ~1 %). Memory: 10 M click_ids/day × 20 bytes = 200 MB/day — fine.
2. **Kafka to Flink (exactly-once source):** Flink reads Kafka with `enable.idempotence=true` + consumer group offsets stored in Flink checkpoints (not Kafka __consumer_offsets). Flink will re-read from last checkpoint on failure, re-process windows, and produce the same aggregate output.
3. **Flink to ClickHouse (idempotent sink):** ClickHouse `ReplacingMergeTree` engine — duplicate writes with the same (ad_id, window_start, granularity, country, device) key are deduplicated at merge time. The aggregate writer uses `INSERT INTO ... ON CONFLICT DO UPDATE` semantics (ClickHouse's `INSERT ... DEDUPLICATION` token).

Billing reconciliation: The billing service reads the T+15 min hourly roll-up. A nightly batch job re-aggregates from raw S3 Parquet (Spark) and reconciles against ClickHouse — any discrepancy > 0.1 % triggers an alert. This **lambda-style reconciliation** covers any Flink failure window.

### 6.4 Hot Partitions

Kafka partitions by `ad_id`. If a mega-campaign (Super Bowl) drives 80 % of traffic to a single ad_id, one Kafka partition gets all the load — that partition's broker becomes the bottleneck.

**Fixes (pick the cheapest that works):**

- **Salted partition key:** append a random 0–7 suffix to ad_id → 8× distribution across partitions. The stream processor groups by true ad_id after reading. Cost: stream processor must merge 8 sub-aggregates per window — trivial.
- **Dedicated partition expansion:** pre-allocate 8 partitions for known mega-ad campaigns (campaign metadata flag). Flink reads all 8 as a single keyed stream. Cost: operational complexity.
- **ClickHouse hot tier:** mark hot ad_ids in Redis; Query API bypasses ClickHouse and reads from a Redis sorted set of pre-computed 1-min buckets updated by the stream processor. Cost: extra write path, cache invalidation.

Chosen: salted partition key by default; dedicated partitions for top-10 campaigns (auto-detected when a campaign's click rate exceeds 10,000 /s for 5 consecutive minutes).

### 6.5 Lambda vs Kappa Reconciliation

**Lambda architecture** (our current design):
- Speed layer: Flink on Kafka → ClickHouse (near-real-time, <5 min lag).
- Batch layer: nightly Spark job on S3 Parquet → recompute aggregates → patch ClickHouse.
- Query layer merges both views (in practice, the nightly Spark job overwrites ClickHouse rows via `ReplacingMergeTree`).

**Kappa architecture** alternative:
- Single stream processor (Flink) handles both real-time and historical reprocessing. To reprocess, replay the Kafka topic (or S3 → Kafka re-ingestion) with a new consumer group.
- Eliminates the dual codebase (batch + stream).
- Downside: Kafka retention cost for 30 days of raw events = 150 GB × 3 replicas = **450 GB** of Kafka storage vs near-zero (S3 at ~$0.023/GB/month = $3.45/month). At our scale, Kappa storage cost is acceptable.

**Verdict:** Start with Kappa for simplicity. If the team grows and Flink operational complexity becomes painful, add a dedicated batch reconciliation job (Spark on S3) — that's Lambda. The key insight is that Kappa is operationally simpler; Lambda gives you a cheaper historical store but two codebases to maintain.

---

## 7. Bottlenecks & Scaling

| Limiting Resource | Symptom | Fix | Cost |
|---|---|---|---|
| Kafka throughput | Consumer lag grows | Add partitions; expand broker fleet | ~$200/mo per broker |
| Flink state backend | Checkpoint latency > 30 s | Switch RocksDB → EBS io2 volumes; tune `state.backend.rocksdb.block-cache-size` | +$50/mo per TaskManager |
| ClickHouse write throughput | Insert queue depth > 100 k rows | Add ClickHouse shards (horizontal); use Buffer engine to batch inserts | Doubles cluster cost |
| ClickHouse read latency (cold) | Dashboard p99 > 500 ms | Add ClickHouse replicas for reads; pre-materialize common queries as materialized views | +50 % read capacity |
| Redis dedup SET memory | OOM at > 1 B clicks/day | Switch to a Redis bloom filter (`BF.ADD`, 1 % FP rate) — 1 B entries ≈ 1.2 GB vs 20 GB for exact SET | Tiny FP rate accepted |
| Single-region availability | DC failure → total outage | Active-passive: stream Kafka MirrorMaker 2 to DR region; ClickHouse async replication; promote in < 5 min | ~40 % infra cost increase |
| Ingest service CPU (geo-IP) | CPU-bound at 60 k /s | Move MaxMind to a sidecar; or use anycast edge (Cloudflare Workers) for geo-enrichment at PoP before ingest | Edge cost ~$0.50/M requests |

**Multi-region (2026 must-have):** Geo-route ad-server traffic to nearest PoP (Cloudflare/Fastly). Each PoP runs a lightweight ingest edge that validates, deduplicates locally (small Redis), and forwards to the regional Kafka cluster. Central aggregation in primary region. Billing always reads from primary. RPO < 30 s (Kafka replication lag); RTO < 5 min (automated failover).

**Observability & cost-per-request:** Every ingest pod emits:
- `clicks_ingested_total` (counter, labeled by ad_id bucket, country)
- `kafka_publish_latency_ms` (histogram, p50/p95/p99)
- `dedup_hit_rate` (gauge — a spike signals a retry storm or upstream bug)
- Cost-per-request tracked via distributed tracing (OpenTelemetry → Jaeger) — target < $0.0001/click end-to-end.

Alerts: Kafka consumer lag > 100 k (stream processor stalled), ClickHouse insert error rate > 0.1 %, Redis dedup SET memory > 80 %.

---

## 8. Trade-offs & Summary

| Decision | What we chose | What we traded |
|---|---|---|
| **Async 202 ack (acks=1 Kafka)** | Lower ingest latency (<100 ms p99), simpler SDK retry logic | ~0.001 % data loss on broker crash; mitigated by SDK retry + dedup |
| **HyperLogLog for uniques** | 1/1000 storage vs exact count; mergeable across partitions | ~2 % error on unique user count — acceptable for dashboards, not for legal billing (billing uses exact count from batch reconciliation) |
| **Kappa over Lambda** | Single stream-processing codebase; simpler ops | Higher Kafka storage cost; long historical reprocessing ties up the Flink cluster |
| **ClickHouse over Druid** | Simpler cluster management, SQL-compatible, lower write overhead | Druid has faster sub-second queries and a native real-time ingestion path; worth revisiting if p99 query latency becomes an SLO |

---

## Key Takeaways

1. **Write-heavy, append-only systems belong in a stream + columnar store stack.** A transactional SQL DB is the wrong hammer — it serializes writes and can't vectorize range aggregations over billions of rows.

2. **Idempotency is a three-layer problem.** Dedup at the edge (Redis), exactly-once through the stream processor (Flink checkpoints + transactional sink), and reconciliation at rest (batch re-aggregate from the immutable raw store). No single layer is sufficient.

3. **Hot partitions are an inevitable consequence of Zipf-distributed keys.** Salt the partition key proactively rather than reacting when a mega-campaign hits. The merge cost in the stream processor is trivial.

4. **Kappa simplicity wins at moderate scale; Lambda wins at cost scale.** The break-even is roughly when Kafka 30-day retention storage cost exceeds the engineering cost of maintaining a separate batch pipeline.

5. **Observability and cost-per-request are first-class design outputs.** In 2026, interviewers expect you to name the metrics, set the alert thresholds, and estimate the per-click infrastructure cost — not just draw boxes.
