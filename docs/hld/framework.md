# The system design (HLD) framework

A structure for a 45–60 min system-design round. Drive it; manage the clock; go deep where the
interviewer pushes. The signal at Staff level is **trade-off reasoning under constraints**, not
naming technologies.

## The steps

1. **Requirements (5–8 min).** Don't design yet — scope it.
   - *Functional:* the few core features ("post a tweet, see a home timeline, follow users").
   - *Non-functional:* scale (DAU, QPS), read:write ratio, latency target, consistency vs
     availability, durability. These drive every later decision.
   - State assumptions out loud and get buy-in.
2. **Capacity estimate (3–5 min).** Back-of-envelope: QPS (DAU × actions / 86,400, then ×peak),
   storage/day (objects × size × retention), bandwidth, memory for cache (hot set). Round
   aggressively — it's about orders of magnitude. (See [`../cheatsheets/`](../cheatsheets/) numbers.)
3. **API design (3–5 min).** The handful of endpoints: `POST /tweet`, `GET /feed?cursor=`. Pick
   REST/gRPC; note pagination, idempotency keys, auth.
4. **Data model (5 min).** Core entities + access patterns → choice of store. Tweet, User, Follow.
   Decide SQL vs NoSQL *from the access pattern*, not taste.
5. **High-level architecture (10 min).** Draw the boxes: clients → LB → API/service tier →
   cache → DB(s) → async workers/queues. Show the read path and the write path explicitly.
6. **Deep dives (10–15 min).** The interviewer steers here. Common: the timeline (fan-out on write
   vs read), the hot-key/celebrity problem, caching strategy, sharding key, search, media storage.
7. **Bottlenecks & scale (ongoing).** Identify the limiting resource and address it: add caching,
   read replicas, shard, introduce a queue, denormalize, add a CDN. Each fix has a cost — say it.
8. **Trade-offs & wrap (3 min).** Recap the 2–3 big decisions and what you traded (e.g., "fan-out
   on write for fast reads, at the cost of write amplification and a special path for celebrities").

## Mental toolkit (reach for these by name)
Load balancing · horizontal scaling + statelessness · caching (look-aside, write-through, TTL,
eviction) · CDN/edge · SQL vs NoSQL · indexing · **sharding** (and the key choice) · replication
(leader/follower) · **CAP/PACELC** · message queues / pub-sub (Kafka) · consistent hashing ·
**idempotency** · rate limiting · CDC · bloom filters · observability. Details in
[`core-concepts.md`](core-concepts.md).

## 2026 additions you should weave in
- **Idempotency & exactly-once-ish** for any write/payment path (dedupe keys, retries).
- **Observability & cost-per-request** as first-class — interviewers dock points if you "finish"
  without monitoring/alerting and a sense of cost.
- **Multi-region** (geo-routing, replication, failover) once you're past single-DC.
- If the prompt is AI-flavored: **vector DB / RAG**, LLM serving (batching, token caching, cost),
  and **agentic** safety/tool-gateways. See [`README.md`](README.md).

## Anti-patterns
Jumping to a diagram before requirements · name-dropping tech without justifying · ignoring the
read:write ratio · one giant DB with no sharding story · hand-waving "we'll just cache it" · never
mentioning failure or cost.
