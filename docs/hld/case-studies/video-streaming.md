# Video Streaming (YouTube / Netflix)

**Prompt:** Design video upload, processing, and playback at scale.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you would ask

1. Is this primarily on-demand (Netflix) or user-generated (YouTube)? — affects write path priority.
2. Do we support live streaming, or only pre-recorded uploads?
3. What devices must be supported? (mobile, web, smart TV, desktop) — drives codec + bitrate ladder.
4. Must we support regional content restrictions / DRM?
5. Is ad insertion in scope?

### Functional (locked)

- Users upload videos (up to 10 GB, any common format).
- System transcodes into multiple resolutions (360p, 480p, 720p, 1080p, 4K) and packages as HLS/DASH.
- Users search, browse, and play videos with adaptive bitrate streaming.
- Metadata: title, description, tags, view count, likes, comments.
- Recommendations feed (basic; not the ML ranking problem — out of scope here).

### Non-functional (locked assumptions)

| Dimension | Assumption |
|---|---|
| DAU | 2 billion (YouTube-scale) |
| Uploads/day | 500 k videos/day (~6 uploads/sec) |
| Views/day | 5 billion (~58 k plays/sec peak ×3 = 175 k) |
| Read:write ratio | ~10,000:1 on video data bytes |
| Upload latency | Best-effort; up to minutes for large files |
| Playback start latency | < 2 s (p99) |
| Availability | 99.99 % (< 1 h/year downtime) |
| Consistency | Eventual on view counts/likes; strong on upload success |
| Durability | 11 nines (S3-class) |

---

## 2. Capacity Estimates

### Uploads

- 500 k videos/day × 2 GB avg raw = **1 PB/day** raw ingested.
- After transcoding to 5 renditions × ~300 MB avg each = 1.5 GB/video stored.
- 500 k × 1.5 GB = **750 TB/day** of transcoded video added to storage.
- Cumulative at 10 years: ~2.7 EB — sharded object storage required.

### Playback QPS

- 5 B views/day / 86,400 = ~58 k plays/sec average; peak ×3 = **175 k plays/sec**.
- Avg video: 7 min; avg bitrate served: 2 Mbps.
- Bandwidth: 175 k × 2 Mbps = **350 Gbps** egress at peak — must be CDN-offloaded.
- Without CDN, 350 Gbps from origin is economically impossible (~$2 M/day at $0.08/GB).

### Metadata & cache

- Video metadata row: ~2 KB. 500 k new/day × 365 × 10 years = 1.8 B rows × 2 KB = **3.6 TB** metadata DB.
- Hot set: top 0.1 % of videos (~1.8 M) account for ~90 % of views. 1.8 M × 2 KB = **3.6 GB** — fits in a single Redis cluster.
- Thumbnail hot set: 1.8 M × 100 KB = **180 GB** — needs distributed edge cache.

### Transcoding compute

- 6 uploads/sec; each takes ~10× realtime on a 4-vCPU worker (1-min video → 10 min CPU-min).
- 6 × 5 renditions × 10 CPU-min = 300 CPU-min/sec = **18,000 vCPUs** sustained at peak.
- Use spot/preemptible instances; keep warm pool of ~2,000 for burst.

---

## 3. API Design

```
# Upload flow (chunked, resumable)
POST   /v1/uploads                         # initiate; returns upload_id (idempotency key)
PUT    /v1/uploads/{upload_id}/chunks/{n}  # upload chunk; 8 MB recommended
POST   /v1/uploads/{upload_id}/complete    # finalize; triggers async transcode job

# Playback
GET    /v1/videos/{video_id}/manifest      # returns HLS (.m3u8) or DASH (.mpd) URL
GET    /v1/videos/{video_id}               # metadata (title, duration, view_count, …)
GET    /v1/videos?q={query}&cursor=…       # search/browse (cursor-based pagination)

# Engagement
POST   /v1/videos/{video_id}/views         # idempotent; body: {session_id, watched_pct}
POST   /v1/videos/{video_id}/likes         # idempotent per user; 204 on repeat
GET    /v1/videos/{video_id}/comments?cursor=…

# Admin / recommendation feed
GET    /v1/users/{user_id}/feed?cursor=…   # personalised home feed
```

**Idempotency:** `upload_id` is a client-generated UUID sent on `POST /uploads`. The server stores `(upload_id → state)` so retries are safe. View events carry `session_id` to deduplicate within a session.

---

## 4. Data Model

### Core entities

**Videos** — primary lookup by `video_id`; also by `channel_id` (list uploads).

```
videos(video_id PK, channel_id, title, description, status,
       duration_s, upload_ts, published_ts, view_count, like_count)
```

**Renditions** — one row per (video, resolution).

```
renditions(video_id FK, resolution, codec, manifest_url, size_bytes)
```

**Users / Channels** — standard relational.

**Comments** — append-heavy, high fan-out reads.

**View events** — time-series, write-heavy.

### Store choices

| Entity | Store | Reason |
|---|---|---|
| Video metadata | PostgreSQL (sharded by `video_id`) | Relational queries (joins to channel, tags); ACID for upload status transitions |
| Comments | Cassandra / DynamoDB | Append-only, high write rate, partition by `video_id`, no complex joins |
| View/like counters | Redis INCR + periodic flush to Postgres | Sub-ms counter increments; eventual flush avoids hot-row contention |
| Raw + transcoded video | Object storage (S3-compatible) | Byte-range GETs, 11-nines durability, tiered pricing |
| Search index | Elasticsearch | Full-text on title/description + tag filters |
| Transcode job queue | Kafka | Durable, replayable, back-pressure |

---

## 5. High-Level Architecture

```
                          ┌─────────────────────────────────────────────┐
                          │             Upload path                      │
  Client ──HTTPS──► Upload API ──► S3 (raw)                             │
                          │            │                                 │
                          │         Kafka topic: video.uploaded          │
                          │            │                                 │
                          │     Transcode Workers (spot fleet)           │
                          │     ├─ ffmpeg: 360p/480p/720p/1080p/4K      │
                          │     └─ Packager: HLS segments + manifest     │
                          │            │                                 │
                          │         S3 (transcoded) ──► CDN origin       │
                          └─────────────────────────────────────────────┘

                          ┌─────────────────────────────────────────────┐
                          │             Read / playback path             │
  Client ──► CDN Edge (PoP) ──► (cache HIT) ──► segment bytes           │
                  │                                                       │
              cache MISS                                                  │
                  │                                                       │
            CDN Origin ──► S3 (transcoded segments)                      │
                  │                                                       │
  Client ──► API Gateway ──► Video Service ──► Redis (metadata cache)    │
                                    │                                     │
                                Postgres (metadata, cold miss)            │
                          └─────────────────────────────────────────────┘

Legend: CDN = CloudFront / Akamai / Fastly; PoPs in 200+ cities.
```

**Write path:** Client → Upload API → S3 raw → Kafka event → Transcode Workers → S3 transcoded + manifest URL written to Postgres → CDN prefetch for popular videos.

**Read path:** Player requests manifest → Video Service → Redis hit (metadata + manifest URL, TTL 5 min) → Player fetches `.m3u8` → CDN PoP serves segments (cache hit ratio target: 95 %).

---

## 6. Deep Dives

### 6.1 Upload + Transcoding Pipeline

**Problem:** A 10 GB file upload over a flaky mobile connection; transcode must produce 5 renditions reliably without re-uploading raw bytes.

**Approach — chunked resumable upload:**

1. Client calls `POST /uploads` → server creates an upload session in Redis (`upload_id → {chunks_received, total_chunks, s3_multipart_id}`).
2. Client sends 8 MB chunks via `PUT /uploads/{id}/chunks/{n}` (idempotent; server checks if chunk already stored by etag before writing).
3. On `POST /uploads/{id}/complete`, server calls S3 `CompleteMultipartUpload` and publishes a `video.uploaded` event to Kafka.

**Transcode workers (fan-out):**

- Each Kafka consumer pulls a job and spawns a pipeline: `demux → decode → filter → encode per rendition → package (HLS/DASH)`.
- Renditions are produced in parallel using separate ffmpeg processes on the same worker (or distributed across workers for large files).
- Each rendition is uploaded to S3 under a stable path: `s3://vod/{video_id}/{resolution}/index.m3u8`.
- On completion, worker publishes `video.transcoded` event; a metadata service updates Postgres `status = published` and writes manifest URLs.

**Failure handling:** If a worker crashes mid-transcode, the Kafka offset is not committed. Another worker picks up the message. All S3 writes are idempotent (fixed key), so re-processing is safe.

**Alternatives considered:**

| Option | Trade-off |
|---|---|
| Synchronous transcode during upload | Simpler, but blocks upload API threads; not scalable |
| Lambda / serverless transcode | No GPU access, 15-min limit kills large files |
| Dedicated transcode SaaS (AWS MediaConvert) | Eliminates ops cost but per-minute pricing blows budget at scale; lose fine-grained control over codec settings |

**Pick:** Kafka + spot fleet of GPU-enabled workers. At 18 k vCPUs, spot pricing (~$0.15/vCPU-hr) costs ~$2.7 k/hr vs on-demand ~$18 k/hr. With checkpointing (save rendition progress to S3 every 2 min), spot interruptions cost < 2 min of re-work.

---

### 6.2 CDN Delivery + Adaptive Bitrate (HLS/DASH)

**HLS mechanics:** Manifest (`.m3u8`) lists 2–10 s segments (`.ts` or fMP4). The player requests the manifest, picks a bitrate variant, and fetches segments. It switches variants every few segments based on measured bandwidth.

**ABR algorithm:** The player (e.g., hls.js, ExoPlayer) uses a throughput-based or buffer-based controller. Buffer-based (BOLA) is preferred: switches up when buffer > 30 s, switches down when buffer < 10 s. This is entirely client-side — the CDN just serves static files.

**CDN strategy:**

- Use a multi-CDN setup (primary + fallback) with Anycast DNS steering. If PoP miss rate > 5 %, the request fans to origin.
- Segment duration: 6 s balances switch latency (longer = fewer ABR decisions) vs startup time (shorter = first segment faster). Use 2 s for live, 6 s for VoD.
- `Cache-Control: public, max-age=31536000, immutable` on segments (content-addressed filenames). Manifest TTL: 5 s for live, 60 s for VoD.
- Preposition: on `video.transcoded` event, a prefetch job warms CDN PoPs in the user's region using a signed URL batch-fetch.

**DASH vs HLS:** DASH is codec-agnostic and uses standard HTTP byte-range. HLS has better Safari/iOS support. Serve both; the player negotiates. Backend cost: negligible (same segments, two manifest files).

**2026 note:** Low-latency HLS (LLHLS, Apple) and DASH-LL reduce live latency from ~30 s to ~3 s using chunked transfer + partial segments. Relevant if live streaming is added.

---

### 6.3 Metadata Store & View-Count Scalability

**Problem:** `view_count` on a popular video receives millions of increments/sec. A direct `UPDATE videos SET view_count = view_count + 1` creates a hot-row lock storm in Postgres.

**Solution — counter buffer in Redis:**

```
INCR video:{video_id}:views          # O(1), in-memory, no locking
EXPIRE video:{video_id}:views 3600   # TTL safety
```

A periodic job (every 60 s) does `GETDEL` on each dirty key and issues a single `UPDATE videos SET view_count = view_count + $delta` per video. This collapses millions of DB writes into one per minute per video.

**Durability trade-off:** Redis without persistence loses up to 60 s of view counts on a crash. Acceptable for an eventually-consistent metric. For revenue-sensitive counts (ad impressions), use Kafka → Flink aggregate → DB, which gives at-least-once with deduplication.

**Metadata sharding:** Shard Postgres by `video_id % N` (hash). `video_id` is a UUID so distribution is uniform. Cross-shard queries (e.g., all videos by a channel) use a secondary index table or an Elasticsearch query.

---

### 6.4 Storage Tiers

| Tier | Content | Latency | Cost |
|---|---|---|---|
| Hot (S3 Standard + CDN) | Recently uploaded + top 1 % by views | < 50 ms to CDN edge | ~$0.023/GB/mo |
| Warm (S3 Infrequent Access) | Videos < 1 M views, > 30 days old | 50–200 ms (no CDN pre-warm) | ~$0.0125/GB/mo |
| Cold (S3 Glacier Instant) | Videos < 10 k views, > 1 year old | 1–5 min restore | ~$0.004/GB/mo |
| Archive (S3 Glacier Deep Archive) | Raw uploads, legal hold | Hours | ~$0.001/GB/mo |

A lifecycle policy runs nightly: views in the last 30 days < threshold → move tier down. A cache-miss on a cold video triggers an async restore + re-warm, and the player shows a "processing" state for < 5 min. Raw uploads (pre-transcode) move to archive immediately after transcode completes.

At 2.7 EB total and 80 % in Glacier tiers, storage cost ≈ $6 M/month vs $62 M/month on S3 Standard alone.

---

## 7. Bottlenecks & Scaling

| Bottleneck | Fix | Cost |
|---|---|---|
| CDN egress 350 Gbps | Multi-CDN with Anycast; 200 + PoPs; 95 % cache hit ratio | $0.01–0.05/GB CDN vs $0.08/GB origin egress |
| Transcode queue depth spikes | Kafka consumer lag alert; auto-scale spot fleet to 50 k vCPUs in < 5 min via ASG | Spot pricing; cold start ~3 min |
| Upload API single-region SPOF | Active-active upload in 3 regions (us-east, eu-west, ap-southeast); geo-DNS routes to nearest | 3× infra cost; use S3 Cross-Region Replication for raw files |
| Metadata DB hot shard | Consistent hash sharding (video_id); if shard splits needed, use online resharding via dual-write + backfill | Ops complexity; plan for 2× shard count every 3 years |
| Redis counter loss on failover | Redis Sentinel + AOF persistence; 60-s flush window acceptable for view counts | 2× Redis memory for replica |
| Search latency at 1.8 B docs | Elasticsearch with 3-tier node topology (hot/warm/cold); route new-video queries to hot nodes | ES license cost; consider OpenSearch to reduce vendor lock |
| Observability cost | Sample 1 % of playback sessions for full traces (OpenTelemetry); aggregate metrics per video_id; cost-per-video-play dashboard | ~$0.002/traced request; budget $50 k/mo for observability |

**Multi-region failover:** Use Route 53 latency + health-check routing. If a region's upload API health check fails, traffic shifts in < 30 s. Postgres uses async replication to standby region; RTO ~5 min, RPO < 60 s.

---

## 8. Trade-offs & Summary

### Decision 1: Async transcoding via Kafka vs synchronous

**Chose:** Async. Upload API returns immediately after S3 write; transcode is decoupled.

**Traded:** Upload → publish latency increases by minutes (user sees "processing"). Gained: upload API can scale independently; transcode workers are spot-interruptible; backpressure is handled gracefully.

### Decision 2: Counter buffering in Redis vs DB write-through

**Chose:** Redis buffer + periodic flush.

**Traded:** Up to 60 s of view count loss on Redis crash; counts are approximate (eventual). Gained: eliminates hot-row contention; supports millions of concurrent increments without DB connection exhaustion.

### Decision 3: Multi-CDN with pre-positioning vs pull-through only

**Chose:** Pre-position top-10 % of videos on publish.

**Traded:** Wasted CDN storage for videos that under-perform. Gained: first-viewer experience identical to millionth viewer; eliminates thundering herd on a viral upload hitting CDN cold.

---

## Key Takeaways

1. **Decouple upload from processing.** An async queue (Kafka) between ingestion and transcoding is the single most important architectural decision: it absorbs burst, enables independent scaling, and makes failures recoverable.

2. **CDN is the product, not an optimization.** At 350 Gbps peak egress, the origin is a cache-miss handler. Design the origin to serve < 5 % of requests; everything else must be edge-cached with immutable URLs.

3. **Hot-row anti-pattern is everywhere in social systems.** View counts, like counts, inventory — any heavily incremented scalar needs an in-memory counter buffer with periodic flush. The Redis INCR + batch-write pattern is reusable across dozens of system design problems.

4. **Storage tiering pays for itself at EB scale.** A lifecycle policy that moves cold video to Glacier reduces storage opex by 10× with acceptable UX impact (< 5 min restore for rarely-watched content).

5. **Idempotency at every write surface.** Chunk uploads, transcode jobs, view events — all are retried. Stable S3 keys + Kafka at-least-once + dedup on session_id gives you exactly-once-ish semantics without a distributed transaction.

6. **Observability must be cost-modelled.** Tracing every playback session at YouTube scale costs more than the CDN. Sample aggressively; keep per-video aggregate metrics cheap; set a cost-per-request budget before you build the pipeline.
