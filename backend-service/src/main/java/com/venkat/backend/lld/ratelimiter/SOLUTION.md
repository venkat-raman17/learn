# Rate Limiter — reference design

---

## Approach

### Entity model

| Concern | Type |
|---|---|
| Algorithm selection | `RateLimiterAlgorithm` enum |
| Construction params | `RateLimiterConfig` (immutable value object) |
| Public contract | `RateLimiter` interface — single method `boolean allow(String clientId)` |
| Per-algorithm logic | Three concrete strategy classes |
| Object creation | `RateLimiterFactory` static factory |
| Per-client mutable state | Inner `BucketState` / `WindowState` / `Deque<Long>` held in a `ConcurrentHashMap` keyed by `clientId` |
| Testable time | `java.time.Clock` injected via a second constructor; defaults to `Clock.systemUTC()` |

### Design patterns

- **Strategy** — `RateLimiter` is the strategy interface; each concrete class is an interchangeable algorithm. The factory selects the strategy; callers never know which one they hold.
- **Static Factory** — `RateLimiterFactory.create(config)` hides constructors and couples algorithm choice to instantiation in one place.
- **Template Method** (optional) — an abstract base `AbstractRateLimiter` can own the `ConcurrentHashMap` and the `computeIfAbsent` lookup, letting subclasses implement only `tryConsume(ClientState, long nowMs)`. This removes boilerplate without forcing inheritance on callers.

---

## Class design

| Class / Interface | Role |
|---|---|
| `RateLimiter` | Strategy interface — `boolean allow(String clientId)` |
| `RateLimiterAlgorithm` | Enum — `TOKEN_BUCKET`, `FIXED_WINDOW`, `SLIDING_WINDOW_LOG` |
| `RateLimiterConfig` | Immutable VO — `algorithm`, `capacity` (int), `window` (Duration) |
| `RateLimiterFactory` | Static factory; switches on `config.getAlgorithm()` and passes `config` to the correct constructor |
| `TokenBucketRateLimiter` | Strategy; per-client `BucketState` (tokens + lastRefillMs); lazy-initialised in `ConcurrentHashMap`; per-client `ReentrantLock` |
| `FixedWindowRateLimiter` | Strategy; per-client `WindowState` (windowIndex + counter); lock per client |
| `SlidingWindowLogRateLimiter` | Strategy; per-client `ArrayDeque<Long>` of timestamps; lock per client |

Per-client state objects are stored in a `ConcurrentHashMap<String, ClientState>`. The map itself is only accessed via `computeIfAbsent` (atomic) so no external lock is needed for initialisation. Each `ClientState` carries its own `ReentrantLock` so two different clients never block each other.

---

## Key code

### Factory

```java
public static RateLimiter create(RateLimiterConfig config) {
    Objects.requireNonNull(config);
    return switch (config.getAlgorithm()) {
        case TOKEN_BUCKET        -> new TokenBucketRateLimiter(config);
        case FIXED_WINDOW        -> new FixedWindowRateLimiter(config);
        case SLIDING_WINDOW_LOG  -> new SlidingWindowLogRateLimiter(config);
    };
}
```

### Token Bucket — crux

```java
// BucketState is a plain mutable holder; access is always under its own lock.
private static final class BucketState {
    final ReentrantLock lock = new ReentrantLock();
    double tokens;          // current token count (double allows fractional accumulation)
    long   lastRefillMs;    // epoch-ms of last refill
}

// Inside TokenBucketRateLimiter:
private final ConcurrentHashMap<String, BucketState> buckets = new ConcurrentHashMap<>();
private final long windowMs;   // config.getWindow().toMillis()
private final int  capacity;   // config.getCapacity()
private final Clock clock;

@Override
public boolean allow(String clientId) {
    BucketState state = buckets.computeIfAbsent(clientId, id -> {
        BucketState s = new BucketState();
        s.tokens       = capacity;     // start full
        s.lastRefillMs = clock.millis();
        return s;
    });

    state.lock.lock();
    try {
        long now     = clock.millis();
        long elapsed = now - state.lastRefillMs;
        // Refill: 1 token per windowMs elapsed
        double newTokens = (double) elapsed / windowMs;
        state.tokens       = Math.min(capacity, state.tokens + newTokens);
        state.lastRefillMs = now;

        if (state.tokens >= 1.0) {
            state.tokens -= 1.0;
            return true;
        }
        return false;
    } finally {
        state.lock.unlock();
    }
}
```

### Fixed Window — crux

```java
private static final class WindowState {
    final ReentrantLock lock = new ReentrantLock();
    long windowIndex = -1;   // floor(nowMs / windowMs)
    int  count       = 0;
}

@Override
public boolean allow(String clientId) {
    WindowState state = windows.computeIfAbsent(clientId, id -> new WindowState());
    state.lock.lock();
    try {
        long nowMs       = clock.millis();
        long curWindow   = nowMs / windowMs;   // integer division = floor
        if (curWindow != state.windowIndex) {
            state.windowIndex = curWindow;
            state.count       = 0;
        }
        if (state.count < capacity) {
            state.count++;
            return true;
        }
        return false;
    } finally {
        state.lock.unlock();
    }
}
```

### Sliding Window Log — crux

```java
// Per-client log stored as an ArrayDeque<Long> of epoch-ms timestamps.
private final ConcurrentHashMap<String, LogEntry> logs = new ConcurrentHashMap<>();

private static final class LogEntry {
    final ReentrantLock  lock = new ReentrantLock();
    final Deque<Long>    timestamps = new ArrayDeque<>();
}

@Override
public boolean allow(String clientId) {
    LogEntry entry = logs.computeIfAbsent(clientId, id -> new LogEntry());
    entry.lock.lock();
    try {
        long now      = clock.millis();
        long cutoff   = now - windowMs;
        // Evict stale timestamps (front of deque is oldest)
        while (!entry.timestamps.isEmpty() && entry.timestamps.peekFirst() <= cutoff) {
            entry.timestamps.pollFirst();
        }
        if (entry.timestamps.size() < capacity) {
            entry.timestamps.addLast(now);
            return true;
        }
        return false;
    } finally {
        entry.lock.unlock();
    }
}
```

---

## Walkthrough

**Scenario:** Token Bucket, capacity = 3, window = 1 s; client `"user-42"` fires 4 rapid requests then waits 1 s and fires a 5th.

1. Request 1 at t=0 ms — `computeIfAbsent` creates `BucketState{tokens=3, lastRefill=0}`. Lock acquired. elapsed=0, no refill. tokens=3 >= 1 -> tokens becomes 2, returns **true**.
2. Request 2 at t=50 ms — state fetched from map. elapsed=50 ms, newTokens=0.05, tokens=min(3, 2.05)=2.05. Consume -> 1.05, returns **true**.
3. Request 3 at t=80 ms — tokens ~1.08. Consume -> 0.08, returns **true**.
4. Request 4 at t=100 ms — elapsed=20 ms, newTokens=0.02, tokens=0.10 < 1.0 -> returns **false** (rate limited).
5. After 1 000 ms sleep, request 5 at t=1100 ms — elapsed ~1020 ms, newTokens=1.02, tokens=min(3, 0.10+1.02)=1.12 >= 1 -> consume, returns **true**.

The per-client lock ensures no other thread can observe a partially-updated `tokens` value between the refill computation and the deduction.

---

## Concurrency & edge cases

| Risk | Guard |
|---|---|
| Two threads racing on the same `clientId` first call | `ConcurrentHashMap.computeIfAbsent` is atomic; only one state object is ever inserted |
| Lost update on token count / window counter | Per-client `ReentrantLock` wraps the entire read-modify-write sequence |
| Different clients blocking each other | Each `ClientState` has its own lock; map-level lock is never held during business logic |
| Clock skew / system clock going backwards | Clamp `elapsed = Math.max(0, now - lastRefillMs)` before computing refill |
| `capacity` requests at window boundary (Fixed Window burst) | Document the known 2x burst; mitigate with Sliding Window Log or a hybrid counter algorithm |
| Stale client entries leaking memory | Schedule periodic eviction via `ScheduledExecutorService` that removes entries whose last-access timestamp exceeds a TTL (e.g., 2 * windowMs) |
| Null / blank clientId | Validate at the top of `allow()` with `Objects.requireNonNull` / a blank check before touching the map |

---

## Complexity & extensibility

| Operation | Token Bucket | Fixed Window | Sliding Window Log |
|---|---|---|---|
| `allow()` time | O(1) | O(1) | O(k) eviction where k = stale entries (amortised O(1) under uniform load) |
| Memory per client | O(1) | O(1) | O(capacity) — at most `capacity` timestamps |

**Adding a new algorithm (e.g., Leaky Bucket):**

1. Add `LEAKY_BUCKET` to `RateLimiterAlgorithm` (enum, one line).
2. Create `LeakyBucketRateLimiter implements RateLimiter`.
3. Add `case LEAKY_BUCKET -> new LeakyBucketRateLimiter(config);` to the factory switch.
4. Zero changes to existing concrete classes or the interface.

For a fully open-closed factory (no switch modification), register a `Map<RateLimiterAlgorithm, Function<RateLimiterConfig, RateLimiter>>` and let each implementation self-register — but the enum-switch approach is simpler and equally valid for an LLD interview.

---

## Follow-ups

1. **Distributed rate limiting** — implement a `RedisRateLimiter` that executes a Lua script (atomic on the Redis side) and satisfies the same `RateLimiter` interface. The factory reads an environment flag (`REDIS_URL`) and returns the Redis-backed strategy instead of the in-process one — zero change to callers.

2. **Algorithm trade-offs** — Token Bucket allows controlled bursts (up to `capacity` at once) and is well-suited to API gateways where clients occasionally spike. Sliding Window Log is fairer and never permits more than `capacity` requests in any rolling window, but uses more memory and is O(capacity) per call; prefer it for strict per-user billing or abuse prevention.

3. **Metrics without leaking internals** — expose a `RateLimiterMetrics` interface (separate from `RateLimiter`) that each implementation optionally implements, or use an out-of-band observer (Micrometer `MeterRegistry`). The `allow()` method records hits/rejections internally; callers query metrics through the separate interface, keeping the public surface clean.

4. **Graceful degradation** — wrap the limiter in a decorator that catches exceptions (e.g., Redis timeout) and fails open (`return true`) with an alert, rather than blocking all traffic.

5. **Hierarchical limits** — compose limiters (decorator pattern): a per-IP limiter wrapping a global limiter. `allow()` returns `true` only when both pass.
