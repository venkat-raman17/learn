# Distributed Message Queue (Kafka-like)

**Prompt:** Design a durable, scalable pub/sub message queue.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions to ask

- What are the primary use cases — event streaming, job queues, or both?
- What durability guarantees are required (messages must survive broker crashes)?
- Should the system support replay / rewind, or is fire-and-forget acceptable?
- Multiple consumer groups reading the same topic independently?
- What are acceptable end-to-end latency SLOs (p99)?
- Do we need exactly-once delivery, or is at-least-once tolerable with idempotent consumers?
- Cross-region replication or single-DC for now?

### Functional requirements (locked)

1. **Producers** publish messages to named **topics**.
2. Topics are split into ordered **partitions**; messages within a partition are strictly ordered.
3. **Consumers** subscribe to topics via **consumer groups**; each partition is assigned to exactly one consumer in a group at a time.
4. Messages are **durably stored** on disk for a configurable retention window (default 7 days).
5. Consumers track progress via **offsets**; they can replay from any committed offset.
6. At-least-once delivery by default; exactly-once delivery supported via idempotent producers + transactional API.

### Non-functional requirements (assumptions locked)

| Property | Assumption |
|---|---|
| Throughput | 2 M messages/s ingested globally |
| Message size | 1 KB average (10 KB p99) |
| Retention | 7 days on disk; archival to object store indefinitely |
| Read:write ratio | 3:1 (multiple consumer groups per topic) |
| Write latency | p99 < 20 ms producer ack (async replication) |
| Read latency | p99 < 10 ms for in-memory segments |
| Durability | Survive loss of 1 broker; survive loss of 1 DC (multi-region) |
| Consistency | Per-partition ordering; cross-partition ordering not guaranteed |
| Availability | 99.99 % write path; 99.95 % read path |

---

## 2. Capacity Estimates

### Write throughput

```
2 M msg/s × 1 KB = 2 GB/s raw ingestion
Peak 3× = 6 GB/s  →  need ~50 Gbps network capacity across cluster
```

### Storage per day (3 replicas)

```
2 GB/s × 86,400 s = ~170 TB/day raw
× 3 replicas       = 510 TB/day
× 7-day retention  = ~3.5 PB hot storage across cluster
```

Object-store archival (S3/GCS) at ~$23/TB/month adds up fast — tiered storage (move segments older than 24 h to cheap object store) cuts hot-disk cost by ~6×, keeping ~570 TB on local NVMe.

### Consumer read bandwidth

```
3× read amplification on 2 GB/s = 6 GB/s out
Each broker node: 10 Gbps NIC → need ≥ 60 broker cores minimum
```

### Partition sizing

```
Target max 100 MB/s per partition (NVMe sequential write ceiling / safety margin)
2,000 MB/s peak per topic → 20 partitions per busy topic
Global: assume 500 topics avg × 20 partitions = 10,000 partitions total
```

### Cache (hot-set)

OS page cache is the primary cache — recent segments stay hot automatically. A broker with 256 GB RAM and 50 GB/s NVMe will serve 90%+ of consumer reads from page cache for the last ~2 minutes of data.

---

## 3. API Design

All APIs use gRPC internally (lower framing overhead, streaming support). A REST gateway translates for HTTP clients.

### Producer API

```
POST /v1/topics/{topic}/produce
Body:  { records: [{ key: bytes, value: bytes, headers: map }], acks: "all"|"leader"|"none" }
Response: { base_offset: int64, partition: int, log_append_time: int64, error_code: int }
```

- `key` drives partition assignment (hash mod N); null key → round-robin.
- Idempotency: include `producer_id` + `sequence_number` in record header; broker deduplicates within a single producer epoch.

### Consumer API

```
GET  /v1/topics/{topic}/partitions/{partition}/fetch
     ?offset=<int64>&max_bytes=1048576&max_wait_ms=500
Response: { records: [...], high_watermark: int64, last_stable_offset: int64 }

POST /v1/consumer-groups/{group}/commit
Body: { offsets: [{ topic, partition, offset, metadata }] }

POST /v1/consumer-groups/{group}/join    // triggers rebalance
GET  /v1/consumer-groups/{group}/assignment
```

### Admin API

```
POST /v1/topics              // create topic: partitions, replication factor, retention
DELETE /v1/topics/{topic}
GET  /v1/topics/{topic}/metadata   // leaders, ISR per partition
```

---

## 4. Data Model

### Core entities

**Log segment file** (the fundamental storage unit)

```
segment-{base_offset}.log     — raw message bytes, append-only
segment-{base_offset}.index   — sparse offset→physical-position index (every ~4 KB)
segment-{base_offset}.timeindex — timestamp→offset index
```

Each partition is a sequence of rolled segment files (roll at 1 GB or 1 hour).

**Offset store** — where each consumer group is in each partition.

```
__consumer_offsets (internal compacted topic)
Key:   group_id + topic + partition
Value: committed_offset + metadata + timestamp
```

**Cluster metadata** — stored in a replicated metadata log (KRaft, Kafka's Raft-based controller since 2.8 — replaces ZooKeeper dependency).

```
metadata.log entries: TopicRecord, PartitionRecord, BrokerRecord, ProducerRecord
```

### Store choice justification

| Layer | Store | Reason |
|---|---|---|
| Message log | Append-only log files on local NVMe | Sequential I/O is 10–100× faster than random; no DB overhead |
| Offset commits | Internal compacted topic | Leverages the same log machinery; co-locates offset state with the queue |
| Cluster metadata | KRaft log (Raft consensus) | Strong consistency for leader election; eliminates ZooKeeper operability burden |
| Long-term archive | Object store (S3/GCS) | Infinite retention, ~$0.023/GB/month vs ~$0.08/GB/month for SSD |

No SQL database in the hot path — the log IS the database.

---

## 5. High-Level Architecture

```
Producers                           Consumers
   │                                     │
   │  gRPC / HTTP                        │  gRPC / HTTP
   ▼                                     ▼
┌──────────────────────────────────────────────────┐
│                  Load Balancer                    │
│           (routes by partition leader)            │
└────────────────────┬─────────────────────────────┘
                     │
        ┌────────────▼────────────┐
        │     Broker Cluster      │
        │  ┌────────┐ ┌────────┐  │
        │  │Broker 1│ │Broker 2│  │   ← each holds partition leaders + followers
        │  │Leader  │ │Follower│  │
        │  │P0,P2   │ │P0,P2   │  │
        │  └────────┘ └────────┘  │
        │  ┌────────┐             │
        │  │Broker 3│ (+ more)    │
        │  │Follower│             │
        │  └────────┘             │
        └──────────┬──────────────┘
                   │  async segment tiering
                   ▼
        ┌──────────────────────┐
        │  Object Store (S3)   │   ← cold segments, infinite retention
        └──────────────────────┘
                   │
        ┌──────────▼───────────┐
        │  KRaft Controller    │   ← cluster metadata, leader election
        │  Quorum (3 nodes)    │
        └──────────────────────┘
```

**Write path:**
1. Producer hashes record key → partition → resolves leader broker from metadata cache.
2. Producer sends `ProduceRequest` to leader broker.
3. Leader appends to partition log (WAL), writes to page cache (OS), acknowledges ISR sync.
4. Follower brokers pull from leader log, append locally, send fetch-ack to leader.
5. Once `min.insync.replicas` followers ack, leader advances high-watermark (HWM) and returns success to producer.

**Read path:**
1. Consumer sends `FetchRequest` to partition leader (or follower with `fetch.from.replica` set).
2. Broker checks page cache for requested offset range — cache hit ~95 %+ for recent data.
3. Broker returns batch up to `max_bytes`, including HWM so consumer knows how far behind it is.
4. Consumer commits offset to `__consumer_offsets` topic after processing.
5. Tiered storage: if offset falls outside local segment range, broker fetches remotely from object store and streams to consumer.

---

## 6. Deep Dives

### 6.1 Partitioning + Ordering

**The problem:** Global ordering across all messages is impossible at scale — it creates a single bottleneck. Per-partition ordering is achievable and sufficient for most use cases (e.g., all events for a user ID land on the same partition via key-hashing).

**Key choice:**

- `murmur2(key) mod N` — deterministic, even distribution.
- If keys are UUIDs, distribution is naturally even.
- If keys are skewed (e.g., one hot seller), add a random suffix for hot keys and strip it at consumer — this trades strict per-key ordering for throughput.

**Repartitioning:** Increasing partition count mid-stream breaks key-to-partition mapping. Mitigate by over-partitioning at creation (e.g., 100 partitions for a topic expected to need 20) and using consistent hashing ring with virtual nodes.

**Chosen approach:** murmur2 hash with configurable hot-key suffix injection at the SDK level; over-partition by 5× at topic creation.

### 6.2 Replication + Durability

**In-sync replica set (ISR):** The leader maintains a list of followers that are within `replica.lag.time.max.ms` (default 30 s) of the leader's log end offset. A message is committed only when all ISR members have appended it.

**`acks` trade-off table:**

| `acks` | Durability | Latency |
|---|---|---|
| `0` | None — fire and forget | ~1 ms |
| `1` | Survives follower loss | ~5 ms |
| `all` + `min.insync.replicas=2` | Survives broker loss | ~15–20 ms p99 |

**Unclean leader election:** Allowing an out-of-ISR broker to become leader risks data loss. Default: disabled (`unclean.leader.election.enable=false`). Enable only for low-durability topics where availability beats correctness.

**Multi-region replication (2026 must-know):** Use MirrorMaker 2 (or a custom cross-cluster replicator) for async replication to a standby region. RPO ~500 ms. Active-active multi-region requires conflict-free topic design (partition-level ownership per region, merge at consumer) — complex; typically active-passive is chosen.

**Chosen approach:** `acks=all`, `min.insync.replicas=2`, replication factor 3, unclean election disabled; async MirrorMaker 2 to standby region with consumer group offset translation.

### 6.3 Consumer Groups + Offsets

**Group coordinator:** One broker is elected coordinator for each consumer group (hash of group ID mod broker count). It manages group membership, triggers rebalances, and stores committed offsets in `__consumer_offsets`.

**Rebalance problem:** Adding/removing a consumer triggers a stop-the-world rebalance — all consumers in the group pause while partitions are redistributed. Mitigation:

- **Static membership** (`group.instance.id`): A consumer rejoining within `session.timeout.ms` reclaims its prior partitions, avoiding unnecessary rebalance.
- **Cooperative incremental rebalance** (Kafka 2.4+): Only reassign affected partitions rather than revoking all; consumers can continue processing unaffected partitions during rebalance.

**Offset commit strategies:**

- **Auto-commit** (every 5 s by default): Simple but can re-process records after crash if commit lags processing.
- **Manual sync commit** after processing: Exactly-once processing if no side-effects, but adds latency.
- **Manual async commit** with retry: Best throughput; requires idempotent downstream writes.

**Chosen approach:** Static membership + cooperative rebalance + manual async commit with idempotent downstream writes.

### 6.4 At-Least-Once vs Exactly-Once

**At-least-once** is the default and sufficient when consumers are idempotent (writes keyed on record offset or a business ID).

**Exactly-once** (EOS) requires:
1. **Idempotent producer** — broker deduplicates retried sends using `(producer_id, epoch, sequence_number)`. Sequence gaps or duplicates within an epoch are detected and rejected.
2. **Transactions** — producer wraps a batch of produce + offset-commit in a transaction; the broker either commits all or aborts all. Consumers set `isolation.level=read_committed` to skip uncommitted records.

**Cost of EOS:**
- ~10–15 % throughput reduction due to transaction coordinator overhead.
- Increases p99 latency by one extra round-trip for the transaction end marker.
- Operational complexity: zombie producers (old epoch still alive) require fencing via epoch bump.

**Chosen approach for this design:** At-least-once delivery with idempotent consumers (deduplication by offset or event ID at sink). Offer EOS as opt-in per-topic for financial/billing pipelines.

### 6.5 Back-Pressure

**Problem:** A slow consumer can fall arbitrarily far behind, eventually triggering log retention deletion and losing messages ("consumer falling off the end of the log").

**Mechanisms:**

- **Fetch max bytes + poll interval:** Consumer-side throttle — pull only what you can process per poll cycle.
- **Broker-side quota (`consumer_byte_rate`):** Throttle fetch bandwidth per client-ID at broker; returns throttle delay header. Prevents one runaway consumer from saturating broker NIC.
- **Dead-letter queue (DLQ):** On processing failure after N retries, publish to a `{topic}.DLQ` partition; main consumer continues. DLQ is processed separately at a reduced rate.
- **Lag monitoring:** Track `consumer_lag = HWM - committed_offset` per partition. Alert at lag > 10 M records or lag growth > 0 (Prometheus + Grafana; metric: `kafka_consumergroup_lag`).
- **Auto-scaler:** When lag exceeds threshold, scale consumer pod count up to max partitions (Kubernetes KEDA `KafkaTopicLagTrigger`).

**Chosen approach:** Broker quotas + KEDA autoscaling + DLQ for poison-pill messages + lag alerting with SLO budget burn-rate alerts.

---

## 7. Bottlenecks & Scaling

| Bottleneck | Symptom | Fix | Cost |
|---|---|---|---|
| Leader broker NIC saturation | High produce/fetch latency | Increase partition count to spread leaders; add brokers | Operational complexity; rebalance downtime |
| Hot partition (skewed key) | One broker at 100 % while others idle | Key salting or partition-level routing at SDK | Breaks per-key ordering guarantee |
| `__consumer_offsets` topic | Offset commit timeouts under high group count | Increase `offsets.topic.num.partitions` (default 50) | One-time cluster reconfiguration |
| Page cache eviction (broker) | Cache miss → NVMe reads → latency spike | Add RAM to brokers or dedicate "cache brokers" for hot topics | Cost; diminishing returns past 512 GB |
| Controller throughput (KRaft) | Slow metadata propagation under partition count growth | Shard controller quorum; limit partition count per cluster | Multi-cluster topology adds routing complexity |
| Cross-region replication lag | Standby region RPO > SLO | Dedicated MirrorMaker 2 cluster with priority-based topic filtering | More infra; selective replication reduces full fidelity |
| Tiered storage read amplification | Cold consumer fetch latency | Pre-warm object-store segments into broker cache on consumer join | Storage cost |

**Observability (2026 must-know):** Instrument every broker with:
- `kafka.server:type=BrokerTopicMetrics` → bytes in/out, message rate.
- `kafka.network:type=RequestMetrics` → p99/p999 produce and fetch latency.
- Cost-per-request: tag Prometheus metrics by topic + consumer group to enable per-tenant cost allocation (essential for multi-tenant deployments or internal chargeback).

---

## 8. Trade-offs & Summary

### Decision 1: Per-partition ordering, not global ordering

**Chose:** Partition-level ordering keyed by record key.
**Trades:** Strong global ordering (impossible at 2 M msg/s) for horizontal scalability. Consumers must co-locate related keys or accept out-of-order reads across partitions. This is the right trade for 99 % of use cases.

### Decision 2: `acks=all` with ISR over `acks=1`

**Chose:** Wait for all in-sync replicas before acknowledging producer.
**Trades:** ~15 ms p99 write latency (vs ~5 ms for `acks=1`) for zero data loss on broker failure. For durability-sensitive workloads (financial events, audit logs) this is non-negotiable; for metrics/telemetry, `acks=1` is acceptable.

### Decision 3: At-least-once by default, EOS as opt-in

**Chose:** At-least-once with idempotent consumers as the default delivery mode.
**Trades:** Simpler, higher-throughput pipeline for the majority of topics vs exactly-once semantics (which carry 10–15 % throughput penalty and significant operational complexity). Financial/billing topics opt into the transactional API.

---

## Key Takeaways

1. **The log abstraction is universally powerful.** Append-only sequential writes + consumer-tracked offsets decouple producers from consumers without coordination overhead. This pattern appears in databases (WAL), event sourcing, and CDC pipelines.

2. **Partition design is a one-way door.** Over-partition at creation; repartitioning mid-stream is painful. Key selection drives ordering guarantees — get it right at the schema-design phase.

3. **Replication factor + min.insync.replicas is your durability dial.** RF=3, ISR=2 is the industry default for a reason: it tolerates one failure without halting writes, and one more failure before data loss.

4. **Page cache is a first-class caching layer.** Kafka's performance advantage over traditional message brokers is largely because it lets the OS page cache do the work. Adding a Redis layer on top usually hurts more than it helps — profile before adding.

5. **Consumer lag is the most actionable operational signal.** A queue that fills faster than it drains is a service outage in slow motion. Wire lag alerts before going to production.

6. **Exactly-once is a spectrum.** "Exactly-once" requires coordination at producer, broker, and consumer simultaneously. In practice, at-least-once + idempotent sinks (dedup by event ID) achieves the same observable outcome for most systems at much lower cost.

7. **Multi-region is an async replication + conflict-avoidance problem, not a sync-replication problem.** Design topics with region-scoped partitions; use async MirrorMaker for disaster recovery, not for active-active consistency.
