# Core concepts (HLD)

The vocabulary of distributed systems, condensed. Each entry: what it is + the trade-off you cite.

## Scaling
- **Vertical** (bigger box) — simple, has a ceiling, single point of failure.
- **Horizontal** (more boxes) — scales out, needs **stateless** services + a **load balancer**
  (L4/L7; round-robin, least-conn, consistent-hash). Push session state to a shared store (Redis).

## Caching
- **Where:** client → CDN/edge → in-process → distributed (Redis/Memcached) → DB buffer pool.
- **Strategies:** *look-aside* (app reads cache, on miss loads DB and fills), *write-through*
  (write cache+DB together), *write-back* (write cache, flush async — fast but can lose data).
- **Eviction:** LRU/LFU/TTL. Beware **stampede** (many misses at once → lock/lease or early
  recompute) and **stale reads** (set TTLs; invalidate on write).
- *Trade-off:* speed/load-reduction vs consistency and added moving parts.

## Databases
- **SQL** — strong schema, ACID, joins, transactions. Default for relational/financial data.
- **NoSQL** — key-value (Redis/Dynamo), document (Mongo), wide-column (Cassandra), graph. Pick by
  access pattern: high write throughput, flexible schema, simple key lookups at scale.
- **Indexing** — speeds reads, slows writes, costs space. Index the columns you filter/sort on.
- **Sharding/partitioning** — split data across nodes by a **shard key** (hash for even spread,
  range for range-queries). The key choice is the whole ballgame: bad keys → hot shards. Resharding
  is painful — **consistent hashing** minimizes reshuffling when nodes change.
- **Replication** — leader/follower (reads scale on followers; failover on leader loss) vs
  multi-leader/leaderless (write availability, conflict resolution). Replication lag → stale reads.

## Consistency & availability
- **CAP** — under a network **partition**, choose **C** (reject/stale-block) or **A** (serve
  possibly-stale). You don't "give up P" — partitions happen.
- **PACELC** — and *else* (no partition), choose **L**atency or **C**onsistency. Most real systems
  tune a dial (e.g., quorum reads/writes: `R + W > N` for strong-ish).
- **Eventual consistency** — replicas converge; fine for feeds/likes, not for balances.

## Async & messaging
- **Message queue / pub-sub (Kafka, SQS)** — decouple producers/consumers, absorb spikes
  (back-pressure), enable retries + **DLQ**. At-least-once delivery is the norm → consumers must be
  **idempotent**.
- **Fan-out on write vs read** — precompute per-consumer (fast reads, write amplification, celeb
  problem) vs compute at read time (cheap writes, slow reads). Often hybrid.

## Reliability building blocks
- **Idempotency** — same request applied once even if retried (dedupe keys); essential for
  payments and any at-least-once path.
- **Rate limiting** — token bucket / leaky bucket / sliding window; protects services; often at the
  API gateway with a distributed counter (Redis).
- **Consistent hashing** — map keys→nodes on a ring so adding/removing a node moves ~1/N keys.
- **Bloom filter** — probabilistic "definitely not present / maybe present"; saves disk/DB lookups.
- **Leader election** (ZooKeeper/Raft) — agree on a coordinator; basis of failover.

## Cross-cutting (don't skip in interviews)
- **Observability** — metrics, logs, distributed tracing; SLOs/alerts. 
- **CDN / object storage** — serve static/media near users; store blobs in S3-like stores, metadata in DB.
- **Cost** — every replica/cache/region costs money; name the cost/QPS trade-off.
- **Multi-region** — geo-DNS, regional replicas, failover, data-residency.

## 2026 must-knows
Vector DBs/embeddings (RAG, semantic search) · LLM serving (batching, token/KV caching, cost &
latency budgets, model routing) · agentic systems (tool gateways, action-risk classification,
safety) · idempotency & multi-region as table stakes. See [`README.md`](README.md) for resources
(DDIA 2e, ByteByteGo, Hello Interview).
