# Web Crawler

**Prompt:** Design a scalable, polite web crawler.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you must ask

| Question | Why it matters |
|---|---|
| Scope: general-purpose (search engine) or focused (price monitoring, link checker)? | Drives URL seed policy, freshness SLA, legal exposure |
| Target scale — how many pages in the index, new pages/day? | Sizing entire pipeline |
| Freshness SLA — how stale can a page be? | Drives recrawl frequency and frontier priority |
| Robots.txt / crawl-delay compliance mandatory? | Politeness design, legal risk |
| JavaScript-rendered content needed? | Headless-browser fleet vs raw HTTP |
| Target latency from page publish to index? | Near-real-time vs batch |

### Assumptions locked for this design

- **Scale:** 10 billion pages in the index; crawl 1 billion pages/day (refresh cycle ~10 days for the full corpus, with priority tiering).
- **Read:Write ratio on the URL store:** heavy writes (newly discovered URLs) + heavy reads (scheduler polling); roughly 1:1.
- **Freshness:** top-tier pages (news, e-commerce) re-crawled every 24 h; long-tail static pages every 30 days.
- **Politeness:** robots.txt honored; max 1 req/s per domain unless crawl-delay specifies otherwise.
- **Consistency:** eventual is fine — the index can lag by minutes; duplicated fetches are wasteful but not catastrophic.
- **No JavaScript rendering** in v1; raw HTTP only.

### Functional requirements

1. Accept seed URLs; discover new URLs by parsing links from fetched pages.
2. Store raw content (HTML) and extracted metadata.
3. Respect robots.txt and per-domain crawl-delay.
4. Avoid duplicate fetches (URL-level and near-duplicate content).
5. Expose an API for the downstream indexer to pull fresh content.

### Non-functional requirements

- **Throughput:** ~11,600 pages/s sustained (1 B / 86,400 s).
- **Availability:** 99.9 % uptime for the scheduler + fetcher fleet.
- **Durability:** raw HTML stored with 99.999 % durability.
- **Observability:** per-domain crawl rate, error rate, queue depth, cost-per-page visible in real time.

---

## 2. Capacity Estimates

### Pages per second

```
1,000,000,000 pages / day
= 1e9 / 86,400 s
≈ 11,600 pages/s  (round to ~12,000 pages/s)
Peak burst 2× → 24,000 pages/s
```

### Average page size and bandwidth

```
Average HTML page: ~150 KB (compressed ~40 KB over wire)
Inbound bandwidth: 12,000 pages/s × 40 KB = 480 MB/s ≈ 4 Gbps
Outbound (DNS + HTTP headers): ~10 % of inbound ≈ 400 Mbps
```

### Storage per day

```
Raw HTML:    12,000 × 150 KB × 86,400 s = ~155 TB/day
              → compress 3:1 → ~50 TB/day raw storage
Metadata:    12,000 × 1 KB  × 86,400 s = ~1 TB/day
URL store:   1 B new URLs/day × 200 B/URL = 200 GB/day
Bloom filter: 10 B URLs × 10 bits/URL (1% FP rate) = ~12.5 GB  (fits in RAM)
```

### Cache (hot-set) — robots.txt

```
Unique domains crawled: ~500 M (Pareto: 10 M active)
Robots.txt entry: ~2 KB average
Hot 10 M domains: 10 M × 2 KB = 20 GB  → fits in a Redis cluster
```

---

## 3. API Design

All internal services; auth via mTLS + service-account JWTs.

```
# Seed / schedule new URLs
POST /v1/urls/seed
Body: { "urls": ["https://example.com/page"], "priority": "high"|"normal"|"low" }
Response: 202 Accepted { "enqueued": 1 }
Idempotency-Key header required (SHA-256 of URL); duplicate seeds are no-ops.

# Fetch result (called by indexer)
GET /v1/pages/{url_hash}
Response: { "url", "fetched_at", "status_code", "content_type",
            "raw_html_ref": "s3://...", "outlinks": ["..."], "checksum": "..." }

# Recrawl policy override
PUT /v1/domains/{domain}/policy
Body: { "crawl_delay_s": 2, "max_pages_per_day": 50000,
        "next_crawl_after": "2026-06-02T00:00:00Z" }

# Metrics (internal scrape endpoint for Prometheus)
GET /internal/metrics
```

Pagination on bulk pulls uses cursor tokens (opaque base64 of last-seen `fetched_at + url_hash`) to avoid offset scans.

---

## 4. Data Model

### Core entities

**URL record** (URL Frontier store)
```
url_hash       BINARY(20)   PK  (SHA-1 for compactness)
url            TEXT
domain         VARCHAR(255)
priority       TINYINT
status         ENUM(pending, in_flight, done, error)
last_fetched   TIMESTAMP
next_crawl     TIMESTAMP    (indexed — scheduler poll)
crawl_count    INT
http_status    SMALLINT
content_hash   BINARY(20)   (for change detection)
```

**Domain policy** (robots + rate limit cache)
```
domain         VARCHAR(255) PK
robots_txt     TEXT
crawl_delay_s  INT
disallow_paths JSON
robots_etag    VARCHAR(64)
robots_ttl     TIMESTAMP
```

**Raw content** — stored in object storage (S3-compatible), not in the DB.
Key: `{year}/{month}/{day}/{url_hash}.html.gz`

**Content metadata** — write-optimized wide-column store.
```
url_hash, fetched_at  -> content_type, status_code, size_bytes,
                         outlinks[], title, lang, content_hash
```

### Store choices

| Entity | Store | Justification |
|---|---|---|
| URL frontier | Cassandra (or ScyllaDB) | Write-heavy, partition by domain for locality; TTL on done rows to self-prune |
| Domain policy / robots cache | Redis cluster | Sub-ms read on hot domains; TTL eviction matches robots.txt re-fetch cadence |
| Raw HTML | S3 / object storage | Immutable blobs; cheap; 99.999 % durability; decoupled from compute |
| Content metadata | Cassandra | Append-heavy, time-series access pattern; wide rows per URL |
| Bloom filter | In-process memory (one per fetcher pod, gossip-synced) | 12.5 GB fits; probabilistic dedup with no round-trip |
| Recrawl priority queue | Redis sorted sets (score = next_crawl epoch) | O(log N) pop; evict when done |

SQL would be a poor fit for the frontier: the table is write-dominated, requires partition-level locality (per domain), and has no complex joins. Cassandra's partition key on `domain` naturally co-locates all URLs for a domain, enabling efficient politeness enforcement.

---

## 5. High-Level Architecture

```
Seed API
   |
   v
+------------------+       robots.txt cache (Redis)
|  URL Scheduler   |<----->+------------------+
|  (priority queue)|       | Domain Policy DB |
+------------------+       +------------------+
   | batches of URLs (domain-bucketed)
   v
+------------------+     +------------------+
|  Fetcher Fleet   |---->|  DNS Resolver    |
|  (stateless pods)|     |  (local + cache) |
+------------------+     +------------------+
   |                |
   | raw HTML       | outlinks
   v                v
+--------+    +------------------+
|  S3    |    |  Link Extractor  |
| (HTML) |    |  + Dedup Filter  |
+--------+    |  (bloom filter)  |
   |           +------------------+
   |                |
   v                v (new URLs back to scheduler)
+------------------+
| Metadata Writer  |
| (Cassandra)      |
+------------------+
   |
   v
Indexer / Downstream consumers (pull via GET /v1/pages/{hash})
```

**Write path:** Scheduler pops a URL batch from Redis sorted set → assigns to a fetcher pod → fetcher resolves DNS, checks robots.txt (Redis hit or live fetch) → HTTP GET with configurable User-Agent → stores HTML in S3 → Link Extractor parses outlinks, runs bloom filter check, enqueues novel URLs back into the scheduler → Metadata Writer records result in Cassandra → URL record status updated to `done`.

**Read path (indexer):** Indexer polls `GET /v1/pages/{hash}` → Metadata service reads Cassandra for metadata → returns S3 presigned URL for raw HTML → indexer fetches directly from S3 (no hop through the crawler).

---

## 6. Deep Dives

### 6.1 URL Frontier + Politeness / Rate Limits

**The problem:** A naive FIFO queue hammers single domains, violating robots.txt crawl-delay and risking IP bans or legal liability.

**Design:** Partition the frontier by domain. Each domain gets its own sub-queue. The scheduler maintains a per-domain `next_allowed_at` timestamp in Redis. It only pops a URL from a domain's queue when `now >= next_allowed_at`. After each fetch, it sets `next_allowed_at = now + crawl_delay_s`.

- `crawl_delay_s` defaults to 1 s; overridden by `Crawl-delay` in robots.txt; capped at 30 s to prevent starvation.
- For large domains (e.g., Wikipedia), allow up to N concurrent in-flight requests where N = (1 / crawl_delay_s) × fetcher threads.
- Fetcher pods are assigned a domain slice (consistent hashing on domain) so one pod owns all rate-limit state for a domain — no distributed lock needed.

**Alternative considered:** Token-bucket in a shared Redis key per domain. Rejected: adds a Redis round-trip per fetch; the partition-ownership model eliminates that.

### 6.2 Deduplication with Bloom Filters

**The problem:** The web has ~trillion URLs counting query-string variants. Checking a DB for every outlink before enqueuing is too slow (~100 µs/check × 10 outlinks/page × 12,000 pages/s = 12 M checks/s).

**Design:** Each fetcher pod maintains an in-process Counting Bloom Filter (CBF) seeded from a daily snapshot. Parameters: 10 billion URLs, 10 bits/element, k=7 hash functions → ~1 % false-positive rate, 12.5 GB RAM.

- False positive: we skip a URL we haven't seen — acceptable; it will be re-discovered via other inlinks.
- False negative: impossible — if a URL is in the filter, we correctly skip it.
- Gossip protocol (every 5 min) merges filters across pods so newly discovered URLs propagate.
- Daily compaction: serialize the CBF, write to S3, reload on pod restart.

**Alternative:** Redis Set (SADD + SISMEMBER). Rejected: at 10 billion entries × 50 bytes/entry = 500 GB; expensive and adds network hop. A distributed HyperLogLog could count uniques but cannot answer "have I seen X?".

**2026 note:** Use a Xor filter (successor to bloom) for 30 % smaller memory with same FP rate if you're building new in 2026.

### 6.3 Distributed Fetchers

**The problem:** 12,000 pages/s requires horizontal scale; each fetch involves DNS, TCP handshake, TLS, HTTP — latency is 100 ms–2 s per page.

**Design:** Stateless fetcher pods, each running ~1,000 async coroutines (Python asyncio / Go goroutines). With 1 s average fetch latency: 1,000 concurrent × 1 page/s = 1,000 pages/s/pod. Need ~15 pods for sustained load, 30 for peak.

- DNS: each pod runs a local unbound resolver + TTL cache; upstream to anycast resolvers. Avoids DNS amplification.
- Connection pool: keep-alive HTTP/2 per host up to 6 connections (matching browser heuristic to avoid overloading targets).
- Timeouts: connect 5 s, read 30 s, total 60 s; exponential backoff on 429/503 with jitter.
- Observability: emit `crawl.fetch.duration_ms`, `crawl.fetch.status_code`, `crawl.fetch.bytes` per-domain per-pod → Prometheus → Grafana. Alert on p99 > 10 s or error rate > 5 %.

### 6.4 Freshness / Recrawl Policy

**The problem:** Not all pages deserve equal re-fetch frequency; over-crawling wastes money and upsets site owners.

**Design:** Tiered priority scored on a composite signal:

| Tier | Signal | Recrawl interval |
|---|---|---|
| News / real-time | sitemap `changefreq=always`, high inlink velocity | 1–6 h |
| High-authority | PageRank > 0.01, `changefreq=daily` | 24 h |
| Normal | Default | 7–14 days |
| Archive / static | Last 3 fetches identical content_hash | 30 days |

- `content_hash` (SHA-1 of normalized HTML body) is stored on each fetch. If hash unchanged 3× in a row, demote tier.
- Sitemap.xml and RSS feeds are crawled first to seed high-priority URLs.
- Cost-per-page is tracked (cloud egress + compute); recrawl decisions are budget-aware: if daily spend > threshold, demote all normal-tier by 2×.

### 6.5 Crawler-Trap Avoidance

**The problem:** Dynamically generated infinite URL spaces (calendars, filters, session IDs) can consume the entire frontier.

**Defenses (defense in depth):**

1. **URL canonicalization:** strip tracking params (`?utm_*`, `?sessionid=`), normalize scheme+host+path, sort query params → hash the canonical form.
2. **Path depth limit:** reject URLs with more than 8 path segments.
3. **Per-domain page cap:** fetch no more than 500,000 pages/domain/day by default (overridable via policy API).
4. **Query-param explosion detector:** if a domain generates > 1,000 distinct URLs differing only in one param, flag it and sample 1 % randomly.
5. **Content similarity (SimHash):** if SimHash of new page is within Hamming distance 3 of 100+ already-crawled pages from the same domain, suppress further crawling of that pattern.
6. **Redirect loop detection:** track redirect chain; abort if length > 5 or if URL appears twice in the chain.

---

## 7. Bottlenecks & Scaling

| Bottleneck | Symptom | Fix | Cost |
|---|---|---|---|
| URL Frontier write throughput | Cassandra write latency spikes | Add nodes (consistent hashing); tune compaction to TWCS | Ops complexity, $$ |
| DNS resolution latency | p99 fetch time dominated by DNS | Local resolver per pod + aggressive TTL caching | RAM per pod (~1 GB) |
| Robots.txt cold cache | Redis miss storm on new domain burst | Pre-warm on seed ingestion; negative-cache (no robots.txt found) | Slight over-memory |
| Single-region failure | Full crawler outage | Active-active in two regions; each region has its own frontier shard | 2× infra cost |
| S3 PUT throughput | 155 TB/day = ~1.8 GB/s; S3 PUT at ~500 req/s default | Use multi-part uploads; request S3 rate-limit increase; prefix-randomize keys | Request limit increase ticket |
| Bloom filter staleness across pods | Duplicate fetches spike | Reduce gossip interval; move to a shared Redis Bitmap for top-1B URLs (100 MB) | Redis memory + net traffic |
| Cost overrun | Daily spend alert fires | Reduce recrawl tier frequencies; kill low-priority seeds | Freshness degrades |

**Multi-region (2026 must-have):** Route seed API traffic to nearest region via GeoDNS. Each region crawls its geographic slice of the web (e.g., ccTLDs: .de, .fr, .jp). Content metadata is replicated cross-region asynchronously (Cassandra multi-DC replication factor 3). S3 cross-region replication for raw HTML. Failover: if a region goes down, its domain slices are redistributed across remaining regions within 5 min via the scheduler's heartbeat-based lease system.

---

## 8. Trade-offs & Summary

| Decision | What we gained | What we gave up |
|---|---|---|
| Partition frontier by domain (ownership model) | No distributed lock for rate limiting; simple politeness; local state | Domain skew if one domain has 10 M pages; mitigated by per-domain caps |
| Bloom filter in-process (not Redis) | Zero network latency on dedup check; 12.5 GB fits in RAM | Eventual consistency across pods (5-min gossip lag); ~1 % false positives |
| Cassandra for frontier + metadata over SQL | Horizontal write scale; TTL-based self-pruning; no schema migrations for wide rows | No ACID transactions; can't do complex joins; operational complexity |
| Tiered recrawl + content_hash change detection | Drastically reduces compute + egress cost (archive tier crawled 30× less) | Freshness lag on pages that don't change often but then do change suddenly |

The two biggest tensions in a web crawler: **throughput vs. politeness** (you want to go fast; the web wants you to slow down) and **completeness vs. cost** (you want every page fresh; storage and compute are finite). Every design decision is a point on those two axes.

---

## Key Takeaways

- **Domain-partitioned queues with ownership** are the idiomatic solution for rate limiting in distributed systems — avoid per-item distributed locks wherever you can.
- **Probabilistic data structures (bloom/xor filters)** unlock O(1) in-memory membership tests at billion-key scale; accept the false-positive rate explicitly and design around it.
- **Tiered priority + change detection** is a general pattern beyond crawling: any system that refreshes a large corpus (search index, CDN, DNS cache) needs a freshness policy that balances cost against staleness.
- **Observability is load-bearing:** per-domain crawl rate + cost-per-page metrics are not vanity dashboards — they are the control plane for recrawl policy decisions.
- **Defense in depth for adversarial inputs** (trap avoidance): canonicalization + depth limits + per-domain caps + SimHash together are far more robust than any single check.
- **Stateless fetcher pods** make horizontal scaling trivial; push all state (frontier, rate-limit timestamps, robots cache) to dedicated stores, not local pod memory.
