# Rideshare (Uber / Lyft)

**Prompt:** Design a service matching riders to nearby drivers in real time.

> Try it yourself first, then read on.

---

## 1. Requirements

### Clarifying questions to ask

- Do we support scheduled rides or only on-demand?
- Which geographies (single city, multi-region, global)?
- Do we own payments or delegate to Stripe/Braintree?
- What latency is acceptable for match delivery to the rider?
- Do we need real-time surge pricing, or is batch/periodic enough?

### Functional requirements (locked)

1. Rider requests a trip from A to B.
2. System finds available drivers within ~2 km radius and dispatches the best match.
3. Driver accepts/declines; rider is notified in real time.
4. Trip progresses through states: `REQUESTED → MATCHED → PICKUP → IN_PROGRESS → COMPLETED`.
5. Fare calculated on completion; payment charged.
6. Driver streams location every 4 seconds while the app is active.
7. Surge pricing multiplier computed per geohash cell continuously.

### Non-functional requirements (locked)

| Property | Target |
|---|---|
| DAU riders | 5 M |
| Peak concurrent active trips | 500 K |
| Driver location update rate | 250 K writes/s at peak |
| Match latency (p99) | < 3 s end-to-end |
| Trip state durability | Zero loss (payment path) |
| Availability | 99.99 % (four nines) |
| Consistency | Eventual for location; strong for trip state + payments |
| Read:write ratio (location store) | ~1:10 (writes dominate) |

### Assumptions

- Rides are on-demand only; no scheduling MVP.
- Payments delegated to external processor; we own the idempotency key.
- Multi-region (NA, EU, APAC) from day one; data residency matters.
- ETA uses a third-party routing API (Google Maps / Mapbox); we cache results.

---

## 2. Capacity Estimates

### Driver location writes

```
250 K drivers active at peak × 1 update/4 s = 62,500 writes/s
Add 4× headroom for reconnects/retries → ~250 K writes/s
Payload per update: driver_id (8 B) + lat/lng (16 B) + ts (8 B) ≈ 32 B
Throughput: 250 K × 32 B ≈ 8 MB/s  — trivial for Kafka
```

### Ride requests

```
5 M DAU × 2 rides/day / 86,400 s ≈ 116 rides/s avg
Peak (5× surge hour): ~600 ride requests/s
```

### Location store size

```
500 K active drivers × 32 B = 16 MB in memory — fits in a single Redis node;
replicate to 3 nodes.
Retention: last known position only (overwrite, not append) → O(drivers) not O(time).
```

### Trip event log (Kafka / storage)

```
600 events/s × 512 B avg payload × 86,400 s × 30-day retention
≈ 600 × 512 × 86 400 × 30 ≈ 800 GB/month — one medium Kafka cluster.
```

### Bandwidth (driver → server)

```
250 K updates/s × 32 B = 8 MB/s inbound — fits a 10 Gbps NIC with room to spare.
```

---

## 3. API Design

All endpoints are REST/JSON over HTTPS; gRPC for internal services.
Idempotency key required on all mutating calls.

```
# Rider
POST   /v1/rides
       Body: { origin, destination, rider_id, idempotency_key }
       Returns: { ride_id, status:"REQUESTED", eta_seconds }

GET    /v1/rides/{ride_id}
       Returns: { ride_id, status, driver, location, eta_seconds }

DELETE /v1/rides/{ride_id}          # cancel

# Driver
PUT    /v1/drivers/{driver_id}/location
       Body: { lat, lng, heading, ts }       # called every 4 s
       Returns: 204

PATCH  /v1/drivers/{driver_id}/availability
       Body: { available: true|false }

POST   /v1/rides/{ride_id}/accept   # driver accepts dispatch
POST   /v1/rides/{ride_id}/decline

# Internal (service-to-service, gRPC)
MatchService.FindDrivers(origin_geohash, radius_km) → []DriverCandidate
SurgeService.GetMultiplier(geohash6) → float
```

Pagination: `GET /v1/rides` uses cursor-based (`?after=<ride_id>`).
Auth: JWT + short-lived tokens; driver tokens scoped to location writes only.

---

## 4. Data Model

### Core entities

**User** (riders + drivers) — PostgreSQL (strong consistency, FK integrity)
```
users(user_id PK, role ENUM, name, phone, rating, created_at)
```

**Trip** — PostgreSQL (strong consistency; ties to payment)
```
trips(trip_id PK, rider_id FK, driver_id FK,
      status ENUM, origin POINT, destination POINT,
      fare_cents INT, surge_multiplier FLOAT,
      requested_at, matched_at, completed_at,
      idempotency_key UNIQUE)
```

**Driver location** — Redis (low-latency, overwrite-in-place)
```
Key: driver:{driver_id}:loc
Value: { lat, lng, heading, geohash6, ts }
TTL: 30 s (stale-driver eviction)
Geo index: Redis GEOADD drivers_geo <lng> <lat> <driver_id>
```

**Trip events** — Kafka topics (append-only, replayed for analytics / fraud)
```
topic: trip-events  partitioned by trip_id
```

**Surge cells** — Redis Hash
```
Key: surge:{geohash6}  Value: { multiplier, demand_count, supply_count, updated_at }
TTL: 60 s
```

### Store justification

| Data | Store | Why |
|---|---|---|
| Users, Trips, Payments | PostgreSQL (RDS Multi-AZ) | ACID, FK, idempotency_key UNIQUE constraint |
| Driver locations | Redis + Geo commands | Sub-ms reads, O(N) radius search, TTL auto-eviction |
| Event log | Kafka | High-throughput append, durable replay, fan-out to analytics |
| Fare/ETA cache | Redis | 60 s TTL; avoid expensive routing API calls |

---

## 5. High-Level Architecture

```
                           ┌─────────────────────────────────────┐
  Rider App  ──WebSocket──▶│          API Gateway / LB           │
  Driver App ──WebSocket──▶│   (TLS termination, auth, routing)  │
                           └────────┬───────────────┬────────────┘
                                    │               │
                     ┌──────────────▼──┐   ┌────────▼──────────┐
                     │  Ride Service   │   │  Location Service  │
                     │ (trip FSM, SQL) │   │  (Redis Geo write) │
                     └───────┬─────┬──┘   └──────────┬─────────┘
                             │     │                  │
              ┌──────────────▼─┐ ┌─▼──────────┐   ┌──▼───────────┐
              │ Match Service  │ │  Payment   │   │  Kafka Bus   │
              │ (geosearch +   │ │  Service   │   │(trip-events, │
              │  dispatch)     │ │ (Stripe)   │   │ loc-events)  │
              └──────┬─────────┘ └────────────┘   └──────┬───────┘
                     │                                    │
              ┌──────▼──────┐                    ┌────────▼──────┐
              │ Redis Geo   │                    │  Surge/       │
              │ (driver loc)│                    │  Analytics    │
              └─────────────┘                    │  Worker       │
                                                 └───────────────┘
```

**Read path (rider polls trip status):**
`Rider App → API GW → Ride Service → PostgreSQL read replica → response`

**Write path (driver location update):**
`Driver App → API GW → Location Service → Redis GEOADD → Kafka loc-events (async)`

**Matching path:**
`Ride Service emits REQUESTED event → Match Service subscribes (Kafka) →
GEORADIUS query on Redis → score candidates (ETA, rating, acceptance rate) →
dispatch gRPC to Ride Service → PATCH trip.driver_id + status=MATCHED →
push WebSocket notification to rider and driver`

**Real-time rider tracking (in-trip):**
Driver location updates flow Redis → push via WebSocket from Ride Service (Pub/Sub fanout per trip_id).

---

## 6. Deep Dives

### 6a. Geospatial indexing — geohash vs quadtree

**Problem:** find all available drivers within 2 km of a given point in < 50 ms.

**Geohash approach (chosen):**
- Encode each driver's position to a 6-character geohash (~1.2 km cell).
- Redis `GEOADD` stores positions in a sorted set (internally encodes as 52-bit geohash integer).
- `GEORADIUS <key> <lng> <lat> 2 km ASC COUNT 50` returns candidates in O(log N + M).
- Edge case: cells straddle the search radius boundary — always query the cell and its 8 neighbors.
- At 500 K drivers, the sorted set is ~30 MB in RAM; single-node Redis handles it easily.

**Quadtree alternative:**
- Better theoretical query performance for highly non-uniform distributions (dense city centers).
- Requires custom implementation or PostGIS; more operational complexity.
- Chosen over: geohash via Redis is simpler, battle-tested at Uber scale, and fast enough.

**S2 Geometry (Google):**
- Hierarchical; cells are equal-area unlike geohash. Used by Google Maps internally.
- Overkill for our MVP; revisit if we need polygon-based geofencing (airport zones, surge boundaries).

**Decision:** Redis GEOADD/GEORADIUS with geohash6 cells. Shard Redis by city cluster once any city exceeds 50 M keys (~rare).

### 6b. High-rate driver location updates

**Problem:** 250 K writes/s, each requiring a Redis update and potential WebSocket fanout.

**Write path:**
1. Driver SDK batches 4-second updates; HTTP/2 multiplexed to Location Service fleet.
2. Location Service is stateless; horizontally scaled behind LB. Each instance writes to Redis.
3. Redis is the single source of truth for "current position." No write-through to Postgres (too slow; location data is ephemeral).
4. Kafka `loc-events` receives every update for async consumers (surge pricing, analytics, ML).

**WebSocket fanout (in-trip tracking):**
- Ride Service subscribes to Redis Pub/Sub channel `trip:{trip_id}:loc`.
- Location Service publishes on every update for trips in `IN_PROGRESS` state.
- Rider's WebSocket connection receives pushes; no polling needed.
- At 500 K active trips × 1 update/4 s = 125 K fanout messages/s — manageable with ~50 Ride Service pods.

**Cost knob (2026 must-know):** each Location Service instance emits a `location.write.latency` histogram and `location.writes_per_second` counter → cost-per-request dashboard alerts if p99 > 20 ms (Redis saturated).

### 6c. Matching algorithm

**Inputs:** rider origin geohash, demand context, pool of candidate drivers from GEORADIUS.

**Scoring per candidate:**
```
score = w1 × (1 / eta_seconds)
      + w2 × driver_rating
      + w3 × driver_acceptance_rate
      - w4 × (cancellations_last_30d)
```
ETA fetched from routing cache (Redis, 60 s TTL) or Mapbox API on miss.

**Dispatch:** Match Service sends a gRPC offer to the driver's WebSocket session via Ride Service. Driver has 15 s to accept. On timeout, the next candidate is offered. At most 3 sequential offers before the system widens the radius to 5 km.

**Idempotency:** the `MATCHED` state transition uses a DB-level UNIQUE constraint on `(trip_id, driver_id)` so concurrent Match Service retries cannot double-assign a driver.

### 6d. Surge pricing

**Problem:** compute supply/demand ratio per micro-zone in near real time.

**Approach:**
- Kafka Surge Consumer reads `loc-events` and `trip-events`.
- Every 30 s, compute for each geohash6 cell: `demand = open_requests_count`, `supply = available_drivers_count`.
- `multiplier = f(demand / supply)` — a piecewise function capped at 5×, rounded to 0.1× increments.
- Written to Redis Hash `surge:{geohash6}` with 60 s TTL.
- Match Service reads surge at dispatch time; Ride Service reads it at fare calculation.

**Alternative:** ML model predicting surge 15 min ahead (Uber's real implementation). Adds prediction latency and model drift risk; current rule-based approach is explainable and auditable — preferred for MVP.

### 6e. Trip state machine

States: `REQUESTED → MATCHED → DRIVER_EN_ROUTE → PICKUP → IN_PROGRESS → COMPLETED | CANCELLED`

**Guarantees needed:** exactly-once state transitions; no ghost trips in `IN_PROGRESS`.

**Implementation:**
- PostgreSQL row with `status ENUM` + `version INT` (optimistic locking).
- All transitions via `UPDATE trips SET status=$new, version=version+1 WHERE trip_id=$id AND status=$expected AND version=$v` — atomic CAS.
- Failed CAS retried by the caller with exponential backoff; idempotency key prevents duplicate charges.
- State change publishes a Kafka `trip-events` message (CDC pattern via Debezium or explicit publish-after-commit).
- Outbox pattern: write event to `trip_outbox` table in same transaction; Debezium streams it to Kafka → eliminates dual-write inconsistency.

---

## 7. Bottlenecks & Scaling

| Bottleneck | Symptom | Fix | Cost |
|---|---|---|---|
| Redis Geo at global scale | GEORADIUS p99 spikes as city exceeds 5 M drivers | Shard by city/metro cluster; consistent hashing routes Location Service to correct shard | Ops complexity; cross-shard queries for multi-city drivers |
| PostgreSQL trip writes | Write throughput hits IOPS ceiling on single writer | Add PgBouncer connection pool; shard trips by city_id; or migrate to CockroachDB for distributed SQL | Schema complexity; cross-shard transactions need sagas |
| Match Service latency | ETA API calls add 200 ms | Precompute ETA grid (city-wide road graph cache, updated every 5 min); use cached travel-time matrices | Staleness during incidents; storage for O(zones²) matrix |
| WebSocket fanout (in-trip) | Memory pressure on Ride Service pods at 500 K concurrent trips | Use Redis Pub/Sub + dedicated Notification Service; or switch to SSE for rider (stateless) | Redis Pub/Sub at 125 K msg/s needs a dedicated Redis cluster |
| Surge pricing lag | Demand spikes not reflected for 30 s | Reduce aggregation window to 10 s; add event-driven trigger on demand spike | Kafka partition pressure; more CPU on surge consumer |
| Multi-region consistency | Driver crosses region boundary mid-trip | Pin trip to origin region for its lifetime; replicate trip record async to destination region | Read latency for cross-region monitoring dashboards |

**Observability (2026 standard):**
- Every service emits: `request_count`, `error_rate`, `p50/p99 latency`, `cost_per_request` (infra cost / RPS).
- Distributed tracing (OpenTelemetry) across Location → Match → Ride → Payment path.
- Alert: match latency p99 > 2 s → page on-call; surge multiplier anomaly (> 4× for > 5 min) → auto-review.

**Multi-region:**
- Geo-routing (GeoDNS / Anycast) sends riders and drivers to nearest regional cluster.
- PostgreSQL: single-writer per region with async cross-region replication for analytics.
- Redis: regional primaries; no cross-region location data (drivers are local).
- Trip records replicate cross-region via Kafka for global dashboards and fraud detection.

---

## 8. Trade-offs & Summary

| Decision | What we gained | What we traded |
|---|---|---|
| Redis GEOADD for driver location (not PostGIS) | Sub-millisecond geo queries; simple TTL eviction; horizontally trivial for per-city shards | Durability — Redis loss means stale locations until drivers re-ping; no historical audit trail (Kafka compensates) |
| Outbox pattern for trip state → Kafka | Exactly-once event delivery; no dual-write inconsistency; safe retries | Two writes per trip transition (trips table + outbox); Debezium adds operational overhead |
| Rule-based surge pricing (not ML) | Explainable, auditable, zero model-drift risk; fast to deploy | Less accurate demand prediction; misses spatial spillover effects; needs manual tuning of piecewise function |

---

## Key Takeaways

1. **Write-optimized ephemeral store + durable event log is the canonical pattern for high-rate sensor data.** Redis for hot state, Kafka for durability and fan-out — this pair recurs in IoT, gaming, and logistics.

2. **Geohash is a prefix-compression trick that converts 2-D proximity into a 1-D range query.** Any system with "find things near a point" (delivery, social, real estate) can apply GEORADIUS or S2 cells instead of a full spatial DB.

3. **Optimistic locking + idempotency keys turn a distributed state machine into a safe CAS loop.** The trip FSM pattern (versioned row + outbox) applies to any workflow with money involved: order fulfillment, hotel booking, flight check-in.

4. **The limiting resource in a rideshare system is not compute — it is write throughput to the location store and WebSocket connection state.** Size your Redis and connection-manager tier first; the API tier is cheap to scale horizontally.

5. **Observability and cost-per-request are first-class design outputs** (not afterthoughts). At 250 K location writes/s, a 10 % latency regression costs real money; a p99 alert on Redis write latency is as important as the architecture diagram.
