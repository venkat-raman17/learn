# Distributed Unique ID Generator (Snowflake)

**Prompt:** Design a service generating unique, time-sortable 64-bit IDs at scale.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you must ask

| Question | Why it matters |
|---|---|
| What generates IDs — one central service or embedded in each app node? | Determines coordination overhead |
| Do IDs need to be monotonically increasing within a single node, or globally? | Affects bit layout |
| What is the expected peak QPS? | Sizing the sequence bits |
| Do clients care about the numeric range (is 64-bit always enough)? | Rules in/out 128-bit UUID |
| Multi-region from day one? | Clock-drift exposure multiplies |
| Are IDs exposed externally (security: enumerable)? | May require obfuscation on top |

### Functional requirements

- Generate globally unique 64-bit integer IDs.
- IDs are roughly time-sortable (k-sorted, not strictly monotone across all nodes).
- IDs can be generated without network round-trips (coordination-free per node).
- System must expose a simple API; IDs are usable as primary keys in any datastore.

### Non-functional requirements & locked assumptions

| Dimension | Assumption |
|---|---|
| Peak write QPS | 500 000 IDs/s globally (Twitter-scale); bursty to 1M/s |
| Latency | p99 < 1 ms per ID (local generation); p99 < 5 ms over RPC |
| Availability | 99.99 % (four nines); no SPOF |
| Consistency | IDs are unique; monotonicity is per-node only |
| Retention | IDs live as long as the records they key — decades |
| Multi-region | Three active regions (us-east, eu-west, ap-southeast) from launch |
| Clock source | NTP-synced; true time API (Google TrueTime / AWS Time Sync) available |

---

## 2. Capacity Estimates

### ID generation throughput

```
Global peak QPS      = 1 000 000 IDs/s
Sequence bits        = 12  → 4 096 IDs per millisecond per node
Required nodes       = ceil(1 000 000 / (4 096 × 1 000 ms/s))
                     = ceil(1 000 000 / 4 096 000) ≈ 1 node sufficient in theory
                     (in practice, deploy ≥ 3 per region for HA, 9 globally)
```

### Storage

IDs are 8 bytes each. They live in the application's primary tables — there is no separate ID store.

```
1 000 000 IDs/s × 8 B = 8 MB/s written into primary tables
Per day               = 8 MB/s × 86 400 s ≈ 700 GB/day of ID-indexed primary data
```

### Bandwidth (ID service RPC)

```
Request payload  : ~50 B (gRPC framing + batch size hint)
Response payload : batch of 100 IDs × 8 B = 800 B
RPC calls/s      : 1 000 000 / 100 = 10 000 RPC/s
Egress           : 10 000 × 800 B ≈ 8 MB/s — negligible
```

### Memory / cache

No caching of IDs — each is generated fresh. The ID nodes themselves are stateless except for the in-memory sequence counter and last-timestamp register (< 100 bytes per node).

---

## 3. API Design

### Option A — embedded library (preferred at scale)

Each service imports a Go/Java/Rust library; no network call. The library is initialized with a `node_id` obtained at startup from the coordination plane.

### Option B — ID service (RPC)

```
POST /v1/ids
Body : { "count": 100 }               // 1–10 000; default 1
Response 200 :
{
  "ids": [284214917452357632, ...]     // int64 array
  "generated_at_ms": 1748803200000    // server wall-clock, for observability
}

GET /v1/ids/{id}/decode
Response 200 :
{
  "timestamp_ms": 1748803200123,
  "node_id":      42,
  "sequence":     17,
  "epoch_offset_ms": 1420070400000   // Twitter epoch, configurable
}
```

**Notes:**
- gRPC preferred; REST for debug/ops tooling.
- Idempotency: ID generation is inherently idempotent per call — the same call at a different millisecond yields different IDs, which is correct. Batch calls should be retried only if the caller received no response (at-most-once at the network level).
- Auth: mTLS between internal services; no public exposure without an obfuscation layer.
- Observability: every response includes `X-Node-ID` and `X-Sequence-Exhaustion-Rate` headers for dashboards.

---

## 4. Data Model

### Core entities

**ID Node Registry** — maps logical `node_id` (0–1023) to a running process.

```
node_id       SMALLINT PK     (0–1023, fits 10 bits)
region        VARCHAR(16)
host          VARCHAR(253)
last_heartbeat TIMESTAMPTZ
leased_until  TIMESTAMPTZ
```

Access pattern: read at startup, heartbeat write every 10 s, read on failure detection. Row count: ≤ 1 024. This is a small, strongly-consistent lookup — **PostgreSQL (or etcd/ZooKeeper) is the right store.**

No separate "generated ID" table — IDs exist only inside application tables as primary keys.

### Store choice

| Need | Choice | Reason |
|---|---|---|
| Node registry | etcd / PostgreSQL | Strong consistency for lease acquisition; tiny dataset |
| Application primary keys | Whatever the app uses | IDs are opaque integers |
| Audit / decode | No store needed | ID is self-describing; decode API reconstructs from bits |

---

## 5. High-Level Architecture

```
                         ┌─────────────────────────────────┐
                         │        Application Services      │
                         │  (Order Svc, User Svc, …)        │
                         └──────────┬──────────────────────┘
                                    │  gRPC (or embedded lib)
                          ┌─────────▼─────────┐
                          │   ID Service       │
                          │  (3 nodes / region)│
                          │  stateless compute │
                          └─────────┬──────────┘
                                    │  node_id lease at startup
                          ┌─────────▼──────────┐
                          │   etcd cluster      │
                          │  (node registry)    │
                          │  3 nodes / region   │
                          └────────────────────┘

Multi-region: us-east / eu-west / ap-southeast each run the above stack.
Node IDs are partitioned by region (bits [9:8] = region index) so no cross-region
coordination is needed at generation time.
```

**Write path (ID generation)**

1. App calls `POST /v1/ids?count=1` to local region's ID service (or invokes embedded lib).
2. ID node reads in-memory `last_ms` and `sequence`.
3. If `now_ms > last_ms`: reset sequence to 0, update `last_ms`.
4. If `now_ms == last_ms`: increment sequence; if sequence overflows 4 096, spin-wait until next ms.
5. If `now_ms < last_ms` (clock skew): apply skew policy (see §6).
6. Compose 64-bit ID, return to caller. No disk I/O, no network call.

**Read path (ID decode)**

Stateless: extract timestamp, node, sequence from bit positions. Served by any replica with O(1) bit math.

---

## 6. Deep Dives

### 6.1 Snowflake Bit Layout

```
Bit 63        : sign bit = 0 (keep ID positive in signed 64-bit languages)
Bits 62–22    : 41 bits of millisecond timestamp (relative to custom epoch)
                → covers 2^41 ms ≈ 69 years from epoch
Bits 21–12    : 10 bits of node/machine ID (0–1023)
                → split as [9:8] = 2-bit region, [7:0] = 8-bit node within region
Bits 11–0     : 12 bits of per-ms sequence (0–4095)
                → 4 096 IDs per ms per node = ~4M IDs/s per node
```

Custom epoch: 2024-01-01T00:00:00Z (millisecond offset = 0). This maximises the timestamp range starting from a known point and avoids the Unix epoch wasting the high bits on a large constant.

**Why 64 bits, not 128?**

128-bit UUIDs fit in a CHAR(36) or BINARY(16) column but blow up B-tree index size by 2x and cause page fragmentation on insertion (random UUIDs are not time-sorted). 64-bit Snowflake IDs are natively a BIGINT, indexable with half the storage, and still have 4K IDs/ms/node headroom.

### 6.2 Clock Skew Handling

NTP adjustments can move the clock backward by tens of milliseconds. If `now_ms < last_ms`, several strategies exist:

| Strategy | Behaviour | Trade-off |
|---|---|---|
| **Refuse & wait** (Twitter original) | Spin until clock catches up, log error | Safe; adds latency equal to skew |
| **Error to caller** | Return 503; caller retries after backoff | Simple; pushes complexity to client |
| **Borrow from future** (dangerous) | Use `last_ms + 1`; sequence space shrinks | Risks duplicates if node restarts |
| **TrueTime-style** | Wait until `earliest_possible > last_ms` | Correct; requires TrueTime API |
| **Hybrid Logical Clocks (HLC)** | Physical + logical component; advances monotonically | Correct across skew; complex |

**Recommendation (2026):** Use AWS Time Sync Service (sub-microsecond accuracy) or Google TrueTime. Set a skew tolerance of 5 ms. If `now_ms < last_ms - 5`, return 503 and alert. If `0 < last_ms - now_ms <= 5`, spin-wait. This bounds worst-case latency to 5 ms while keeping correctness guarantees.

### 6.3 Coordination-Free Generation

The Snowflake design is coordination-free *at generation time* because the `node_id` is pre-assigned. The only coordination happens once at startup (lease acquisition from etcd). After that, each node generates IDs entirely from local state (memory-resident sequence + timestamp).

**Node ID lease protocol:**

1. On startup, node atomically CAS-writes its hostname into an etcd key `id-service/nodes/{id}` with a 30 s TTL.
2. It refreshes the TTL every 10 s via a heartbeat.
3. On crash, the TTL expires; another node can safely claim that `node_id` after TTL passes (at most 30 s of gap — acceptable since IDs from the old node in that window are already committed and will not be re-issued because the sequence was in-memory).
4. Never assign the same `node_id` to two live nodes simultaneously — etcd's Compare-And-Swap with leader election guarantees this.

### 6.4 Alternatives Evaluated

| Approach | Uniqueness | Sortable | Coordination | DB load | Chosen? |
|---|---|---|---|---|---|
| **UUID v4** | Cryptographic | No (random) | None | Low | No — random inserts fragment B-trees; not sortable |
| **UUID v7** (2023 RFC) | Strong | Yes (ms-prefix) | None | Low | Viable fallback; 128-bit cost; good for polyglot |
| **DB auto-increment** | Yes (per-shard) | Yes | High (DB roundtrip) | Write bottleneck | No — single point of write contention |
| **DB ticket server** (Flickr) | Yes | Weakly | Medium (one DB) | Medium | No — SPOF unless multi-master; latency per ID |
| **Range allocation** | Yes | Partial | Low (batch fetch) | Low | Good for embedded; loses ms-precision sort |
| **Snowflake 64-bit** | Yes | Yes (ms-grain) | Startup only | None | **YES** — best balance for high-throughput, latency-sensitive workloads |
| **ULID** | Strong | Yes | None | Low | Viable; string-encoded (26 chars) adds storage cost |

**Decision: Snowflake 64-bit with etcd-leased node IDs.** The only scenario to reconsider is a polyglot environment where teams use multiple languages and cannot share a native library — in that case, deploy the ID service as a sidecar and accept the < 1 ms RPC overhead.

---

## 7. Bottlenecks & Scaling

### Limiting resource: sequence exhaustion within a millisecond

At 4 096 IDs/ms/node, a single node saturates at ~4M IDs/s. At 1M global IDs/s with 9 nodes, each node handles ~111 K IDs/s — well within the 4M limit. This is not the bottleneck today.

**If QPS grows 40x (40M/s globally):**

| Fix | How | Cost |
|---|---|---|
| Add nodes | Increase node count (up to 1 023 globally) | Ops overhead; etcd grows |
| Widen sequence bits | Use 13 bits (sacrifice 1 timestamp bit = 34-year range) | Schema change; coordinate with all consumers |
| Reduce timestamp granularity | Microseconds instead of ms (lose 10 bits → re-layout) | Breaking change |
| Shard by tenant | Each tenant-shard has its own node pool | Complexity; great for multi-tenant SaaS |

### Network / RPC bottleneck (Option B — service mode)

- Load balance over 3+ ID service pods per region with L4 LB (not L7 — latency matters).
- Deploy as a **sidecar** in the same pod as the calling service to eliminate cross-host RTT.
- Batch requests: callers pre-fetch 1 000 IDs at a time, store in a local ring buffer. Refill asynchronously when buffer drops below 10 %. This trades slight staleness (IDs issued slightly ahead of use) for throughput.

### Clock reliability

- Pin NTP to AWS Time Sync / PTP hardware clocks in each DC.
- Alert on `clock_skew_ms > 2` via Prometheus + PagerDuty. At 5 ms, circuit-break the node (stop issuing, return 503).
- Never run ID nodes as spot/preemptible instances — clock drift is worse on migrating VMs.

### Multi-region coordination

- Node ID space partitioned by region (bits 9:8). Regions never share node IDs — zero cross-region coordination at generation time.
- etcd is regional; cross-region replication is read-only (for audit), not on the hot path.
- Failover: if us-east loses its etcd, nodes already holding leases continue generating for up to 30 s (TTL). After that, they stop and redirect traffic to eu-west until etcd recovers.

### Observability & cost-per-request (2026 must-have)

```
Metrics (Prometheus / OpenTelemetry):
  id_generation_total          counter  (by node_id, region)
  id_sequence_exhaustion_total counter  (clock too-fast events)
  id_clock_skew_ms             histogram
  id_rpc_duration_ms           histogram (p50/p99/p999)
  id_node_lease_renewals_total counter

Cost accounting:
  ID service: ~3 pods × 0.25 vCPU × $0.04/vCPU-hr = ~$0.03/hr per region
  At 1M IDs/s: cost per 1B IDs ≈ $0.03/hr × 1 hr / 3.6T IDs/hr = negligible
  Dominant cost: the primary-key index in the application DB, not the ID service.
```

Structured logs emit `node_id`, `region`, `sequence_at_ms` on every 10 000th ID for sampling-based audit without log volume explosion.

---

## 8. Trade-offs & Summary

### Decision 1: 64-bit Snowflake vs 128-bit UUID v7

**Chose:** 64-bit Snowflake.
**Trades:** Operational complexity (node ID leasing) for half the index size, BIGINT native type support in every DB, and 4 096 IDs/ms/node throughput. UUID v7 would be the right call if you prioritize zero coordination over index efficiency in a small-scale or polyglot system.

### Decision 2: Coordination-free generation (etcd lease at startup only) vs per-ID coordination

**Chose:** Startup-only lease with in-memory sequence.
**Trades:** A 30 s window of potential duplicate node ID re-use (post-crash, pre-TTL-expiry) for O(1) latency generation with no network calls. The 30 s gap is acceptable because the crashed node's in-memory sequence is lost — a new node acquiring the same ID after TTL will start its sequence at 0, and any overlap is impossible within the same millisecond (different physical time).

### Decision 3: Spin-wait on clock skew vs error on clock skew

**Chose:** Spin-wait up to 5 ms, then 503.
**Trades:** Tail latency increase (up to 5 ms added) for guaranteed uniqueness without rejecting the request in the common case. Returning an error immediately is safer but pushes retry burden to every caller; the 5 ms bound keeps p99 acceptable.

---

## Key Takeaways

**Transferable patterns this problem teaches:**

1. **Embed coordination in startup, not in the hot path.** Snowflake pays the etcd round-trip once per node lifetime, not once per ID. This pattern (lease/register at boot, act autonomously thereafter) applies to any high-throughput, low-latency service.

2. **Bit packing as schema design.** A 64-bit integer is a struct. Choosing field widths (timestamp vs node vs sequence) is a capacity planning decision with a 69-year clock: allocate bits deliberately and document the epoch.

3. **Monotonicity is per-shard, not global — and that is usually fine.** Most systems need IDs to be sortable within a shard (user's posts, order items). Global total order is much harder and usually unnecessary. Identify the actual sortability requirement before paying for consensus.

4. **Clock skew is a correctness issue, not a performance issue.** Design the skew policy before you deploy, not after the first duplicate appears at 3 AM. TrueTime-style bounded uncertainty is the 2026 best practice.

5. **Observability at the bit level.** Because the timestamp is embedded in the ID, you can decode any ID to a wall-clock time with no database lookup — valuable for incident forensics and data TTL enforcement.

6. **Cost-per-request thinking.** The ID service itself is nearly free; the real cost is the B-tree index in the downstream DB. Choosing time-sorted IDs (Snowflake, ULID, UUID v7) over random UUIDs reduces index fragmentation, lowers write amplification, and improves cache hit rate on range scans — a systems win that also reduces your DB bill.
