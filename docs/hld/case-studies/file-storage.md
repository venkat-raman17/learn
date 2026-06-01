# File Storage & Sync (Dropbox / Drive)

**Prompt:** Design a file storage and multi-device sync service.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you would ask

1. What are the core user actions — upload, download, sync across devices, share with others?
2. What is the target scale — DAU, number of files per user, average file size?
3. Do we need real-time sync or is eventual consistency (within a few seconds) acceptable?
4. What is the expected read:write ratio? (File storage is typically read-heavy after initial upload.)
5. Do we need versioning and deleted-file recovery?
6. How granular is sharing — per-file, per-folder, with expiry and permission tiers?
7. What is the durability SLA — 99.9% or 11 nines (S3-class)?

### Functional requirements (locked)

- Upload a file (up to 5 GB); chunked upload required for large files.
- Download a file by ID.
- Sync a folder across multiple devices — detect changes, push deltas.
- Share a file/folder with other users (read / read-write permission).
- List folder contents; file metadata (name, size, modified time, version).
- Soft-delete with a 30-day recovery window; retain last 5 versions.

### Non-functional requirements (assumptions locked)

| Dimension | Assumption |
|---|---|
| DAU | 50 M |
| Avg files per user | 2,000; avg size 200 KB → 400 GB/user total storage |
| Total corpus | 50 M × 400 GB = 20 EB — use tiered cold storage |
| Upload:download ratio | 1:5 (mostly reads) |
| Sync events/DAU/day | 20 (background delta checks) |
| Latency (metadata ops) | p99 < 100 ms |
| Latency (download first byte) | p99 < 500 ms for cached; < 2 s for cold |
| Consistency | Eventual for sync (seconds); strong for permissions and billing |
| Durability | 99.999999999% (11 nines) via multi-region replication |
| Availability | 99.99% per region; geo-failover within 60 s |

---

## 2. Capacity Estimates

### QPS

```
DAU = 50 M
Upload actions/day  = 50 M × 2   = 100 M   → ~1,200 uploads/s (2× peak = 2,400/s)
Download actions/day = 50 M × 10  = 500 M  → ~5,800 downloads/s (peak ~12,000/s)
Sync checks/day     = 50 M × 20  = 1 B     → ~11,600 metadata reads/s
Metadata writes (new file, rename, delete):
  ~10% of upload volume = 120 M/day → ~1,400/s
```

### Storage per day

```
New uploads/day:  50 M users × 2 uploads × 200 KB avg = 20 TB/day raw
After dedup (assume 30% duplicate chunks across users): net ~14 TB/day
Replication ×3 (multi-region):                         net ~42 TB/day written to object store
Metadata DB growth: 50 M × 2 records × ~500 B = 50 GB/day (manageable)
```

### Bandwidth

```
Inbound (uploads): 20 TB/day / 86,400 s ≈ 230 MB/s average; 500 MB/s peak
Outbound (downloads): 100 TB/day / 86,400 s ≈ 1.2 GB/s average; CDN absorbs ~80%
CDN origin traffic: ~240 MB/s
```

### Cache (hot-set)

```
Top 1% of files = 0.5 M files × 200 KB = 100 GB — fits in a Redis cluster.
Metadata hot set: 10% of active users × 2,000 entries × 500 B = 50 GB.
Total in-memory cache target: ~200 GB (Redis cluster, 5 nodes × 64 GB).
```

---

## 3. API Design

All endpoints require `Authorization: Bearer <JWT>`. Idempotency key required on all mutating calls.

```
# Initiate chunked upload — returns uploadId + pre-signed URLs per chunk
POST /v1/files/upload/init
  Body: { name, size, mimeType, parentFolderId, clientMtime, sha256 }
  Response: { uploadId, chunkSize, chunkUrls: [{ seq, presignedPutUrl }] }

# Complete upload — triggers assembly + dedup
POST /v1/files/upload/complete
  Header: Idempotency-Key: <uuid>
  Body: { uploadId, chunkChecksums: [{ seq, sha256 }] }
  Response: { fileId, version, deduplicated: bool }

# Download (redirect to CDN or object store)
GET /v1/files/{fileId}/content?version=latest
  Response: 302 → signed CDN URL (TTL 15 min)

# List folder
GET /v1/folders/{folderId}/items?cursor=&limit=100
  Response: { items: [{id, name, type, size, mtime, version}], nextCursor }

# Get sync delta (device polls this)
GET /v1/sync/delta?deviceId=&cursor=<serverCursor>
  Response: { changes: [{op, path, fileId, version}], newCursor }

# Share
POST /v1/files/{fileId}/shares
  Header: Idempotency-Key: <uuid>
  Body: { recipientEmail, permission: "read"|"write", expiresAt? }
  Response: { shareId, shareLink }

# Delete (soft)
DELETE /v1/files/{fileId}
  Response: 204
```

---

## 4. Data Model

### Core entities

**files** (PostgreSQL — strong consistency for metadata, ACID transactions for versioning)

| Column | Type | Notes |
|---|---|---|
| file_id | UUID PK | |
| owner_id | UUID FK users | |
| parent_folder_id | UUID FK folders | nullable (root) |
| name | TEXT | |
| size_bytes | BIGINT | |
| content_hash | CHAR(64) | SHA-256 of full file |
| blob_key | TEXT | pointer into object store |
| version | INT | incremented on overwrite |
| deleted_at | TIMESTAMPTZ | soft delete |
| created_at / updated_at | TIMESTAMPTZ | |

**chunks** (separate table; enables dedup)

| Column | Type | Notes |
|---|---|---|
| chunk_hash | CHAR(64) PK | SHA-256 of chunk |
| blob_key | TEXT | actual object store key |
| ref_count | INT | GC trigger when 0 |
| size_bytes | INT | |

**file_chunks** (junction: file version → ordered chunk list)

| file_id, version, seq | → chunk_hash |

**shares**

| share_id, file_id, grantor_id, grantee_id, permission, expires_at |

**sync_cursors** — per-device watermark into the change log.

### Store choices

- **PostgreSQL (CockroachDB multi-region in 2026):** metadata, shares, versions. Access pattern is point lookups by `file_id` and range scans by `(owner_id, parent_folder_id)` — relational joins, ACID for versioning.
- **Object store (S3-compatible):** chunk blobs. Immutable write-once; no joins needed; content-addressed by hash. NoSQL key-value at object scale.
- **Redis:** metadata hot cache (LRU, 1-hour TTL), pre-signed URL cache, session tokens.
- **Kafka:** change-event stream for sync delta delivery and async post-processing (virus scan, thumbnail generation).

---

## 5. High-Level Architecture

```
Clients (web / mobile / desktop)
       │ HTTPS / WebSocket (sync events)
       ▼
  ┌─────────────┐
  │  CDN / Edge │  (CloudFront / Fastly — serves cached downloads)
  └──────┬──────┘
         │ cache miss
         ▼
  ┌──────────────────────────────────────────┐
  │            API Gateway / LB              │
  └────────┬──────────────────┬─────────────┘
           │                  │
    ┌──────▼──────┐   ┌───────▼────────┐
    │  File API   │   │  Sync Service  │
    │  Service    │   │  (long-poll /  │
    │  (stateless)│   │   WebSocket)   │
    └──────┬──────┘   └───────┬────────┘
           │                  │
     ┌─────▼─────┐      ┌─────▼──────┐
     │ Metadata  │      │   Kafka    │
     │  DB       │      │ (changes)  │
     │(CockroachDB│      └─────┬──────┘
     │ multi-rgn)│            │
     └─────┬─────┘      ┌─────▼──────┐
           │            │  Async     │
     ┌─────▼─────┐      │  Workers   │
     │  Redis    │      │ (scan,     │
     │  Cache    │      │ thumbnail, │
     └───────────┘      │ dedup GC)  │
                        └────────────┘
           │
    ┌──────▼──────────────────────────┐
    │      Object Store (S3/GCS)      │
    │   content-addressed chunks      │
    │   multi-region replication      │
    └─────────────────────────────────┘
```

**Write path:** Client → API Gateway → File Service → (1) write chunk blobs to object store via pre-signed URLs directly from client, (2) POST complete → File Service writes metadata + file_chunks to CockroachDB, publishes change event to Kafka. Sync Service consumes Kafka, pushes delta to other devices.

**Read path:** Client → CDN (hit: return bytes). Miss → API Gateway → File Service → Redis (metadata hit) or CockroachDB → generate signed URL → redirect to CDN/object store. CDN caches the blob with a content-hash-based cache key (immutable TTL = 7 days).

---

## 6. Deep Dives

### 6.1 Chunking + Deduplication

**The problem:** A 2 GB video upload over a flaky mobile connection must be resumable. Storing the whole file as one blob wastes storage when many users share similar content (especially Office docs, common libraries).

**Content-defined chunking (CDC) vs fixed-size:**
- Fixed 4 MB chunks are simple but shift on any insertion, invalidating all downstream chunks.
- CDC (Rabin fingerprinting / FastCDC) produces variable-size chunks (2–8 MB) with boundaries that are content-stable — a 1-byte insertion only affects one chunk. **Pick CDC.**

**Dedup flow:**
1. Client computes SHA-256 per chunk before upload.
2. `POST /upload/init` — client sends chunk hashes; server responds with which hashes already exist in the `chunks` table (`upload_needed: false`). Client skips uploading known chunks.
3. Only novel chunks are PUT directly to object store via pre-signed URL.
4. On complete, `file_chunks` rows link file version → chunk list.
5. Chunk `ref_count` is incremented; GC worker decrements on delete and reclaims blobs when `ref_count = 0`.

**Cross-user dedup:** hash comparison is hash-only (no content inspection), so identical public files (e.g., a shared ISO) deduplicate across users without privacy exposure.

**Numbers:** In practice CDC achieves 30–50% storage savings on typical enterprise datasets; for consumer photos the gain is lower (~10%) due to JPEG entropy.

### 6.2 Sync and Conflict Resolution

**The problem:** User edits the same file on laptop (offline) and phone simultaneously. Both devices push their version. Who wins?

**Approach — vector clocks + last-write-wins with divergence detection:**
1. Each file version carries a `(deviceId, lamportClock)` pair.
2. Server assigns a monotonic `serverVersion` on each write.
3. On upload, server compares the client's `baseVersion` to the current `serverVersion`:
   - If `baseVersion == currentVersion`: clean apply, increment version.
   - If `baseVersion < currentVersion`: conflict. Server stores **both** as sibling versions.
4. Conflict resolution policy (configurable per account):
   - **Last-write-wins (LWW):** simpler, loses data. Good default for binary files (images, PDFs).
   - **Three-way merge:** for text files, compute diff against common ancestor. Surface conflict markers. Used by Google Docs' OT layer.
   - **Keep both:** rename conflicting copy to `file (Device B's copy).ext`. Dropbox default.

**Sync delta protocol:**
- Each device holds a `serverCursor` (Kafka offset or DB watermark).
- `GET /sync/delta?cursor=X` returns ordered list of changes since X.
- Changes published to Kafka; Sync Service fans out to connected WebSocket clients.
- Offline device replays delta on reconnect; cursor advances atomically only after local apply succeeds (at-least-once, idempotent by `(fileId, version)` key).

### 6.3 Metadata vs Blob Storage

**Why separate them:** Blob store (S3) is optimized for large sequential I/O and durability, not for sub-millisecond metadata queries. If you store file name, modified time, and permissions in S3 object tags, every folder listing becomes an expensive S3 List call (1,000 objects/page, billed per request).

**Pattern:** Store all queryable metadata in PostgreSQL/CockroachDB; blobs in S3. The `blob_key` column in `files` is the join key. This lets you:
- Run `SELECT ... WHERE owner_id = ? AND parent_folder_id = ? ORDER BY name` at DB speed.
- Update file name or permissions with a single DB row update — no object copy.
- Implement soft delete and versioning entirely in the DB layer; blobs are never moved.

**Pitfall:** metadata DB and object store can drift (DB row exists but blob missing, or orphan blob after crash). Mitigate with: (a) write blob first, then DB row; (b) async reconciliation job that compares DB `blob_key` list against object store inventory (S3 Inventory report, daily).

### 6.4 Sharing and Permissions

**Access control model:** Hierarchical + RBAC.
- `shares` table stores `(file_id, grantee_id, permission, expires_at)`.
- Folder share implies recursive permission on all children — avoid storing one row per child.
- Resolve permissions at read time: walk the ancestor path upward until a share row is found, or deny.

**At scale, permission checks on hot paths are expensive.** Two mitigations:
1. **Permission cache:** Cache `(userId, fileId) → permission` in Redis (TTL 5 min). Revocation invalidates the cache entry. Acceptable lag for most enterprise use cases; tighten for financial docs.
2. **Zanzibar-style relationship DB (Google's model, open-sourced as SpiceDB / OpenFGA):** stores `(object, relation, subject)` tuples and answers "can user U read object O?" via BFS over a graph. Correct and fast, but operationally heavy. Worth adopting at Dropbox/Drive scale (>10 B share relationships).

**Public links:** generate a `shareToken` (HMAC-signed, 128-bit random), stored in `shares` with `grantee_id = NULL`. Validate token at API gateway before hitting auth service.

---

## 7. Bottlenecks & Scaling

| Bottleneck | Symptom | Fix | Cost |
|---|---|---|---|
| Metadata DB write hot spot | CockroachDB CPU spikes on upload bursts | Shard by `(owner_id % N)` — owner's files co-located | Rebalance complexity; cross-shard queries for admin ops |
| Blob write throughput | S3 503 SlowDown on PUT bursts | Multi-part upload + S3 request rate prefix sharding (randomise key prefix) | Extra latency for small files |
| Sync fan-out | 1 shared folder, 10 K members; 1 write = 10 K Kafka messages | Lazy fan-out: push notification "folder changed", clients pull delta | Slight latency increase for large teams |
| Download bandwidth | CDN origin overwhelmed for new popular files | Thundering-herd: stagger CDN TTL with jitter; use CDN shield (origin shield) | Additional CDN cost tier |
| Chunk dedup contention | Two users upload same chunk simultaneously, both insert | Upsert with `ON CONFLICT (chunk_hash) DO UPDATE ref_count = ref_count + 1`; idempotent | Negligible |
| Permission check latency | SpiceDB/OpenFGA query p99 spikes | Redis permission cache in front of graph DB | Stale cache window (5 min) on revocation |

**Multi-region (2026 must-know):**
- CockroachDB: geo-partition `files` table by `owner_region` — EU rows live in EU nodes, US rows in US nodes. Cross-region reads only for globally shared files.
- Object store: S3 Cross-Region Replication for durability; CRR adds ~60 s lag. Reads served from nearest region (geo-routing via Route 53 / Cloudflare).
- Sync cursors are region-local; failover resets cursor to last-known-good watermark (at-least-once redelivery, idempotent on client).

**Observability & cost-per-request:**
- Trace every upload/download with `traceId` propagated through Kafka messages to async workers (OpenTelemetry).
- Emit cost attribution metric: `s3.put.bytes`, `db.query.ms`, `cdn.egress.bytes` per `(userId, planTier)`.
- Budget alert: if `cdn.egress.cost` for a tenant exceeds plan quota, throttle download speed (fair-use policy).

---

## 8. Trade-offs & Summary

| Decision | What you gain | What you give up |
|---|---|---|
| Content-defined chunking + cross-user dedup | 30–50% storage cost reduction; resumable uploads | Client-side hashing CPU; GC complexity (ref-counting); small files have disproportionate chunk overhead |
| Separate metadata DB from blob store | Fast queries, cheap renames, rich permissions, soft-delete without object moves | Two systems to keep in sync; reconciliation job required; operational overhead |
| Last-write-wins conflict resolution (default) | Simple, no user-visible conflict dialogs | Data loss on concurrent offline edits — acceptable for binary files, wrong for collaborative docs |

---

## Key Takeaways

- **Content-addressed storage is the core primitive.** SHA-256 chunk hashing enables dedup, resumability, and integrity verification simultaneously — carry this pattern to any large-object system.
- **Separate the hot (metadata) from the cold (bytes).** Relational DB for queryable attributes + object store for immutable blobs is a reusable split applicable to video platforms, backup systems, and artifact registries.
- **Sync is a distributed-systems problem, not a file problem.** Vector clocks, idempotent cursors, and at-least-once delivery with client-side dedup are the invariants. The file is just the payload.
- **Permissions at scale need a graph, not a JOIN.** Hierarchical ACLs resolved via BFS (Zanzibar model) are the industry answer once share relationships exceed a few hundred million rows.
- **Cost observability is a feature.** Dedup savings and CDN egress costs should be first-class metrics; in 2026 interviewers expect you to close the loop on cost, not just throughput.
