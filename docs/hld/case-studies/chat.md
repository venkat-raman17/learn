# Chat System (WhatsApp / Messenger)

**Prompt:** Design real-time 1:1 and group messaging (up to 1,000 members per group).

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions you must ask

1. 1:1 only, or groups? If groups, what is the member cap?
2. Must messages be delivered in strict order (per-conversation, or global)?
3. Offline delivery — should messages queue while a recipient is offline?
4. Media (images, video) in scope, or text only for this round?
5. Read receipts ("delivered" vs "read")? Presence ("last seen")?
6. Multi-device (same account on phone + laptop simultaneously)?
7. Target regions — single-DC or multi-region?

### Assumptions (locked)

| Dimension | Value |
|---|---|
| DAU | 500 M |
| Messages sent/day | 50 B (WhatsApp-scale) |
| Avg message size | 200 B (text only; media out of scope) |
| Group cap | 1,000 members |
| Groups in system | 5 B groups; avg 50 members |
| Consistency model | Per-conversation causal ordering; eventual across conversations |
| Delivery guarantee | At-least-once with client-side dedup |
| Read:write ratio | ~1:1 (every send triggers at least one receive) |
| Availability target | 99.99 % (< 52 min downtime/year) |
| Message retention | 90 days hot, then cold archive |

### Functional requirements

- Send/receive 1:1 messages in real time.
- Send/receive group messages (up to 1,000 members).
- Delivery receipt (server received) + read receipt (recipient opened).
- Presence — online / last-seen.
- Offline queue — deliver missed messages on reconnect.

### Non-functional requirements

- P99 send-to-deliver latency < 200 ms (same region).
- Fan-out for a 1,000-member group < 1 s.
- Message durability: zero loss after server ACK.
- Multi-region: users in EU/APAC connect to nearest DC; messages replicated globally.

---

## 2. Capacity Estimates

### QPS

```
50 B messages / 86,400 s ≈ 580,000 sends/s  (call it 600 K writes/s)
Peak factor 3x             ≈ 1.8 M writes/s
Receives ≈ sends × avg_recipients
  1:1 share (say 70%):  420 K × 1  = 420 K receives/s
  group share (30%):    180 K × 50 = 9 M receives/s
Total receive QPS ≈ 9.4 M/s  (peak ~28 M/s)
```

### Storage per day

```
50 B messages × 200 B = 10 TB/day raw
+ 20% overhead (metadata, receipts)  ≈ 12 TB/day
90-day hot tier: 12 TB × 90          ≈ 1.1 PB
After 90 days → cold (S3/GCS): compressed ~0.3 PB/quarter
```

### Bandwidth

```
Inbound:  600 K msg/s × 200 B ≈ 120 MB/s (≈ 1 Gbps)
Outbound: 9.4 M recv/s × 200 B ≈ 1.9 GB/s (≈ 15 Gbps) — fan-out dominates
```

### Cache (hot-set)

```
Online users: 500 M DAU × 20% concurrent = 100 M users
Presence record: 64 B each → 100 M × 64 B = 6.4 GB  (fits in Redis cluster)
Last ~20 messages per conversation in cache:
  DAU active convos (est. 200 M) × 20 msgs × 200 B = 800 GB
  Keep top 10% hot → ~80 GB L2 cache per region
```

---

## 3. API Design

All endpoints require `Authorization: Bearer <JWT>` and a `Request-Id` header for idempotency.

```
# Send a message
POST /v1/messages
Body: { conversation_id, sender_id, content, client_msg_id, type: "text" }
Response 201: { msg_id, server_ts, conversation_id }
Idempotency: client_msg_id deduped server-side for 24 h.

# Fetch conversation history (pull / reconnect sync)
GET /v1/conversations/{conv_id}/messages?after_cursor=<msg_id>&limit=50
Response 200: { messages: [...], next_cursor }

# Mark messages read
POST /v1/conversations/{conv_id}/read
Body: { last_read_msg_id }
Response 204

# Get presence
GET /v1/users/{user_id}/presence
Response 200: { status: "online"|"offline", last_seen_ts }

# WebSocket handshake (persists for session)
GET /ws  (Upgrade: websocket)
Auth: token in query param or first frame.
Server pushes: { type: "message"|"receipt"|"presence", payload: {...} }
```

---

## 4. Data Model

### Core entities

**messages** (per-conversation sharding key = `conversation_id`)

| Field | Type | Notes |
|---|---|---|
| msg_id | UUID v7 (time-ordered) | monotone within shard |
| conversation_id | UUID | shard key |
| sender_id | UUID | FK to users |
| content | bytes(200) | encrypted at rest |
| client_msg_id | UUID | idempotency |
| created_at | timestamp | server clock |
| status | enum(sent,delivered,read) | updated via receipts |

**conversations**

| Field | Type |
|---|---|
| conversation_id | UUID |
| type | enum(1:1, group) |
| created_at | timestamp |

**conversation_members** (for groups; fan-out lookup)

| conversation_id | user_id | joined_at |
|---|---|---|

**user_presence** — stored only in Redis (volatile):
`presence:{user_id}` → `{ status, last_seen_ts, session_id }` TTL 30 s, refreshed by heartbeat.

### Store choice

| Store | Entity | Justification |
|---|---|---|
| Cassandra (wide-column) | messages | Write-heavy, time-series access `WHERE conv_id = X ORDER BY msg_id DESC LIMIT N`; horizontal sharding native; tunable consistency. |
| PostgreSQL (sharded) | conversations, members | Relational joins needed (member list, group metadata); moderate scale; shard by conversation_id using Citus or application-level routing. |
| Redis Cluster | presence, delivery cursors, dedup cache | Sub-ms reads; volatile data fits. |
| S3-compatible (object store) | media blobs; cold message archive | Cheap, durable, CDN-friendly. |

Why not a single relational DB: at 600 K writes/s, a single Postgres leader saturates. Cassandra's leaderless, multi-master model handles this write volume without a fan-out bottleneck.

---

## 5. High-Level Architecture

```
Clients (mobile / web)
        |  WebSocket (persistent)
        v
  +-----------+    gRPC    +--------------+
  |  WS        |---------->|  Message     |
  |  Gateway   |           |  Service     |
  |  (stateful)|<----------| (stateless)  |
  +-----------+            +--------------+
        |                        |  write
        | push                   v
        |              +------------------+
        |              |  Cassandra       | <-- messages table
        |              |  (msg store)     |
        |              +------------------+
        |                        |
        |              +------------------+
        |              |  Fan-out Queue   | (Kafka per-conv partition)
        |              +------------------+
        |                        |
        |              +------------------+
        |              |  Delivery Worker | -- resolves recipients
        |              +------------------+         |
        |                                           | push to
        +<------------------------------------------+
        (WS Gateway for each online recipient)
                  |
         Redis Cluster (presence, cursors)
                  |
         PostgreSQL (conversation metadata, member lists)
```

**Write path (send message)**

1. Client sends frame over WebSocket.
2. WS Gateway validates JWT, forwards to Message Service via gRPC.
3. Message Service dedupes on `client_msg_id` in Redis (24 h TTL).
4. Writes to Cassandra (immediate, quorum write for durability).
5. Returns ACK to sender — server-received receipt shown immediately.
6. Publishes event to Kafka topic `chat.messages` partitioned by `conversation_id`.
7. Delivery Worker consumes, looks up online members (Redis presence), and pushes frames to the correct WS Gateway instances via internal pub-sub (Redis Pub/Sub or a fanout bus).
8. WS Gateway delivers to connected clients.
9. Offline recipients: event stays in Kafka until consumer catches up; on reconnect, client pulls missed messages via REST cursor API.

**Read path (load history)**

1. Client calls `GET /v1/conversations/{id}/messages?after_cursor=`.
2. Message Service checks Redis L2 cache for recent messages.
3. Cache miss: query Cassandra `WHERE conversation_id = X AND msg_id > cursor LIMIT 50`.
4. Populate cache; return paginated response.

---

## 6. Deep Dives

### 6a. Persistent Connections (WebSocket) + Gateway

**The problem:** 500 M DAU; 100 M concurrent. A WebSocket is a stateful, long-lived TCP connection. A single server can hold ~100 K connections; you need O(1,000) gateway nodes.

**Routing:** Each WS Gateway registers its live sessions in Redis: `ws_session:{user_id} → {gateway_host, session_id}`. The Delivery Worker looks up this key before pushing. If the host is gone (crash), the key has a TTL and the message falls back to the offline queue.

**Scaling gateways:** Gateways are horizontally scaled behind an L4 load balancer (not L7 — you need to preserve TCP connections). Use consistent hashing so that reconnects land on the same node where possible, reducing Redis lookups.

**Alternative — HTTP/2 server push or SSE:** SSE is simpler (stateless from the load-balancer's view), but lacks bidirectional flow needed for receipts. WebSocket wins here.

**Multi-region:** Each region runs its own gateway cluster. Cross-region delivery: Kafka topics are mirrored (MirrorMaker 2 or Confluent Replication). A user in APAC and a user in EU chatting: message written in EU Kafka, mirrored to APAC Kafka, delivered via APAC gateway. Adds ~80 ms cross-region vs sub-10 ms intra-region — acceptable for chat.

### 6b. Delivery & Read Receipts

Three states: `sent` (client sent), `delivered` (server + recipient device received), `read` (user opened).

**Implementation:** Recipient device sends a receipt frame over its WS connection when it receives a message, and again when the user opens the conversation. Receipt is written to Cassandra (update `messages.status`) and a Kafka event triggers the original sender's gateway to send a "tick" update.

**At-scale concern:** 10 B messages/day × 2 receipts = 20 B small writes/day. Cassandra handles this with lightweight transactions (LWT) disabled for receipt updates — idempotent upserts via `UPDATE ... IF status < new_status`. Batch receipt writes in micro-batches (50 ms windows) per conversation to avoid hot partitions.

**2026 note:** Instrument cost-per-receipt via a request-level counter (OpenTelemetry span attribute `msg.receipt_type`). Receipts are often the hidden top-10 cost driver.

### 6c. Presence

**Naive:** Poll every 10 s. At 100 M users × 1 poll/10 s = 10 M reads/s on DB. Too expensive.

**Better:** Heartbeat-driven Redis TTL. Each online client sends a heartbeat frame every 20 s. Gateway updates `presence:{user_id}` with TTL 30 s. If the key expires, the user is offline. Reads are Redis O(1) lookups.

**Subscriptions:** When Alice opens a conversation with Bob, the client subscribes to Bob's presence. WS Gateway holds the subscription; when Bob's presence key changes (via a Redis keyspace notification or a Pub/Sub channel `presence:{bob_id}`), the gateway pushes to Alice.

**Privacy:** "Last seen" timestamps are stored only after the user goes offline; the exact online status is only exposed to mutual contacts (enforced at the presence service level).

### 6d. Message Ordering & Storage

**Problem:** In a distributed system, two clients sending simultaneously can arrive at different Cassandra nodes in different orders.

**Solution:** `msg_id` is a UUID v7 (timestamp-monotone). The client clock is untrusted; the server assigns `msg_id` at write time with a per-server sequence suffix. For strict causal ordering within a conversation, use a per-conversation sequence counter stored in Redis (`INCR conv_seq:{conv_id}`). This becomes the `seq` field on each message. Clients sort by `seq`, not wall clock.

**Hot partition:** A conversation with 1,000 members sending rapidly creates a hot Cassandra partition. Mitigate with partition-per-month bucketing: `partition_key = (conversation_id, yyyymm)`. Queries span at most 2 partitions for recent history.

### 6e. Group Fan-out

**The problem:** 1 send → up to 1,000 deliveries. At 180 K group messages/s × 50 avg members = 9 M delivery events/s.

**Fan-out on write (chosen):** At send time, the Delivery Worker expands the member list from PostgreSQL and enqueues one delivery task per online member. Advantage: delivery is push-based, low latency. Cost: write amplification; a 1,000-member group creates 1,000 tasks.

**Fan-out on read alternative:** Store the message once; each client polls for new messages. Simpler writes, but 100 M concurrent clients polling every second overwhelms the read path and kills battery life.

**Hybrid for large groups (>500 members):** Write the message once to Cassandra; publish a single Kafka event. Clients in the group subscribe to a group-level pub/sub channel. Gateway pushes the same event to all subscribers of that channel without per-member expansion. This caps write amplification at O(1) regardless of group size, at the cost of a more complex subscription model. Adopt at group sizes where per-member fan-out becomes untenable (empirically >200 members).

---

## 7. Bottlenecks & Scaling

| Limiting resource | Symptom | Fix | Cost |
|---|---|---|---|
| WS Gateway TCP connections | Connection refused at ~100 K/node | Add gateway nodes (stateless horizontal scale) | Linear infra cost; Redis session store grows |
| Cassandra write throughput | Write timeouts at peak | Add Cassandra nodes; tune `LOCAL_QUORUM`; use token-aware drivers | Node cost + rebalancing ops |
| Kafka fan-out consumer lag | Delivery delays > 1 s | Increase partition count (cap = number of consumers); add consumer instances | More CPU; partition rebalance storms |
| Redis presence memory | OOM on 100 M keys | Shard Redis by `user_id % N`; use Redis Cluster with 32+ shards | Complexity; cross-shard ops slower |
| PostgreSQL member list joins | Slow group sends during peak | Denormalize member list into Redis sorted set `group_members:{conv_id}` | Cache invalidation on member add/remove |
| Cross-region replication lag | Stale reads in secondary region | Read from local replica; accept eventual consistency for non-critical reads; strong reads route to origin | Latency for strong reads |
| Cold storage retrieval | Slow load of messages older than 90 days | Tiered storage: hot (Cassandra) → warm (Parquet on S3 + Athena) → glacier | Query cost + latency |

**Observability & cost-per-request (2026):** Instrument every Kafka consumer with span attributes: `fan_out.group_size`, `fan_out.online_recipients`, `delivery.latency_ms`. A dashboard that surfaces cost-per-1K-messages-delivered lets you catch a runaway group (e.g., a 1,000-member group where everyone is online) before it burns the budget.

---

## 8. Trade-offs & Summary

| Decision | What you gain | What you give up |
|---|---|---|
| WebSocket over SSE/polling | True bidirectional, low-latency push; delivery receipts are native | Stateful gateways; harder to load-balance; connection state must survive restarts (session registry in Redis) |
| Cassandra for message store | Horizontal write scale; native time-series access; tunable consistency | No joins; eventual consistency requires client-side dedup; operational complexity (compaction, repair) |
| Fan-out on write for small groups, pub/sub for large groups | Consistent low-latency delivery for most conversations; write amplification bounded for huge groups | Two code paths to maintain; threshold tuning; hybrid adds subscription management complexity |

**Biggest single decision:** The choice of per-conversation sequence numbers (Redis `INCR`) as the ordering authority. It is simple and correct for moderate traffic, but the Redis key becomes a hot spot under extreme write bursts. The alternative — vector clocks or CRDT-based ordering — is correct under partition but is significantly harder to implement and explain. For a 45-minute interview, Redis `INCR` + `IF status < new_status` upserts is the right answer.

---

## Key Takeaways

- **Stateful connections demand a session registry.** Any push-based system needs a mapping from `user_id → gateway_host`; Redis with TTL is the idiomatic answer.
- **Fan-out is the dominant cost driver**, not storage. Size your Kafka partitions and consumer fleet for peak fan-out, not peak sends.
- **Receipts are surprisingly expensive.** They generate 2–3× the write volume of messages; batch them and instrument cost-per-receipt early.
- **Sequence numbers over wall clocks.** Distributed clocks are unreliable; a per-conversation monotone counter (even if it's a Redis INCR) gives consistent ordering with minimal overhead.
- **Presence is a heartbeat problem, not a query problem.** TTL-keyed Redis presence scales to hundreds of millions of users; polling a DB does not.
- **Multi-region chat is a replication lag problem.** Accept eventual delivery across regions; make it visible via the receipt states rather than blocking the sender on cross-region confirmation.
- **Observability from day one:** cost-per-message, fan-out size distribution, and gateway connection counts are the three dashboards that catch real incidents before users do.
