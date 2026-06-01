# Parking Lot — reference design

---

## Approach

**Entity model**

The lot is composed of `ParkingFloor` objects, each owning a sorted list of `ParkingSpot` entities. Spots are sorted by `(level, spotNumber)` so that scanning floors in order naturally satisfies the "nearest first" rule. A `ParkingLot` facade owns the floors, delegates fee calculation, and maintains an active-ticket registry.

**Design patterns**

| Pattern | Where | Why |
|---|---|---|
| **Strategy** | `PricingStrategy` | Swap hourly / surge / free-first-N pricing without touching `ParkingLot`. |
| **Factory method** | `ParkingLot.create(...)` | Encapsulates construction validation and spot materialisation; keeps the constructor private, enabling controlled instantiation. |
| **Facade** | `ParkingLot` | Single entry point hiding floor iteration, locking, and ticket bookkeeping. |
| **Template / enum dispatch** | `VehicleType.requiredSize()` | Adding a new vehicle type (BUS→EXTRA_LARGE) requires only a new enum constant with its size — no `if-else` in the core loop. |

**Singleton trade-off** — do *not* make `ParkingLot` a singleton. It prevents multi-lot tests and breaks dependency injection. Use the factory method and let callers manage scope.

---

## Class design

| Class / Interface | Role |
|---|---|
| `VehicleType` (enum) | `BIKE`, `CAR`, `TRUCK`; carries `requiredSize()` → `SpotSize` |
| `SpotSize` (enum) | `SMALL`, `MEDIUM`, `LARGE` |
| `Vehicle` (record) | Immutable value: `licensePlate + VehicleType` |
| `ParkingSpot` (entity) | `level`, `spotNumber`, `SpotSize`, `volatile boolean available` |
| `ParkingFloor` (entity) | Ordered list of `ParkingSpot`; exposes `firstAvailable(SpotSize)` |
| `Ticket` (record) | Issued on park: `ticketId`, `Vehicle`, spot coords, `entryTime` |
| `Receipt` (record) | Issued on unpark: original `Ticket`, `exitTime`, duration, `fee` |
| `PricingStrategy` (interface) | `calculate(VehicleType, long durationMinutes) → BigDecimal` |
| `HourlyPricing` (impl) | Per-type rate map; ceiling-rounds partial hours |
| `LotConfig` / `FloorConfig` (records) | Layout DTO injected at construction |
| `ParkingLot` (facade) | Factory method, `park`, `unpark`, `getAvailability`; owns lock + ticket registry |
| `ParkingLotFullException` | Unchecked; thrown by `park` when no spot available |
| `InvalidTicketException` | Unchecked; thrown by `unpark` for unknown/redeemed ticket |

---

## Key code

### 1 — Enum dispatch instead of if-else chains

```java
enum VehicleType {
    BIKE  { @Override public SpotSize requiredSize() { return SpotSize.SMALL;  } },
    CAR   { @Override public SpotSize requiredSize() { return SpotSize.MEDIUM; } },
    TRUCK { @Override public SpotSize requiredSize() { return SpotSize.LARGE;  } };

    public abstract SpotSize requiredSize();
}
```

Adding `BUS` later means adding one constant — `ParkingLot.park()` never changes.

### 2 — ParkingSpot with volatile flag

```java
class ParkingSpot {
    final int level;
    final int spotNumber;
    final SpotSize size;
    volatile boolean available = true;  // written under lot-level lock; volatile for visibility

    ParkingSpot(int level, int spotNumber, SpotSize size) {
        this.level = level; this.spotNumber = spotNumber; this.size = size;
    }
}
```

### 3 — park() — atomic spot acquisition with ReentrantLock

```java
// Inside ParkingLot
private final ReentrantLock lock = new ReentrantLock();
private final List<ParkingFloor> floors;                       // ordered lowest→highest
private final Map<String, ParkingSpot> activeTickets =        // ticketId → spot
        new ConcurrentHashMap<>();
private final PricingStrategy strategy;
private final Clock clock;

public Ticket park(Vehicle vehicle) {
    Objects.requireNonNull(vehicle, "vehicle must not be null");
    SpotSize required = vehicle.type().requiredSize();

    lock.lock();
    try {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.firstAvailable(required);
            if (spot != null) {
                spot.available = false;
                Ticket ticket = new Ticket(
                        UUID.randomUUID().toString(),
                        vehicle,
                        spot.level, spot.spotNumber, spot.size,
                        Instant.now(clock));
                activeTickets.put(ticket.ticketId(), spot);
                return ticket;
            }
        }
    } finally {
        lock.unlock();
    }
    throw new ParkingLotFullException(
            "No available " + required + " spot for " + vehicle.type());
}
```

**Lock choice — `ReentrantLock` over `synchronized`**: provides `tryLock(timeout)` for future fairness/timeout extensions, and makes the critical section boundary explicit. The section is short (linear scan of spots, then a map put), so a single coarse lock is acceptable until scale demands per-size queues.

### 4 — unpark() — validate, release, compute fee

```java
public Receipt unpark(Ticket ticket) {
    Objects.requireNonNull(ticket, "ticket must not be null");
    ParkingSpot spot = activeTickets.remove(ticket.ticketId());
    if (spot == null) {
        throw new InvalidTicketException("Unknown or already redeemed ticket: " + ticket.ticketId());
    }
    lock.lock();
    try {
        spot.available = true;
    } finally {
        lock.unlock();
    }
    Instant exitTime = Instant.now(clock);
    long durationMinutes = (Duration.between(ticket.entryTime(), exitTime).toSeconds() + 59) / 60;
    BigDecimal fee = strategy.calculate(ticket.vehicle().type(), durationMinutes);
    return new Receipt(ticket, exitTime, durationMinutes, fee);
}
```

Note: `activeTickets.remove()` is atomic on `ConcurrentHashMap`, so double-redemption is rejected even without the coarse lock. The lock is only needed to restore `spot.available` visibly.

### 5 — HourlyPricing strategy

```java
class HourlyPricing implements PricingStrategy {
    private final Map<VehicleType, BigDecimal> ratePerHour;

    HourlyPricing(Map<VehicleType, BigDecimal> ratePerHour) {
        this.ratePerHour = Map.copyOf(ratePerHour);
    }

    @Override
    public BigDecimal calculate(VehicleType type, long durationMinutes) {
        long fullHours = (durationMinutes + 59) / 60;   // ceiling already done upstream
        return ratePerHour.getOrDefault(type, BigDecimal.ZERO)
                          .multiply(BigDecimal.valueOf(fullHours));
    }
}
```

### 6 — getAvailability()

```java
public Map<VehicleType, Integer> getAvailability() {
    Map<VehicleType, Integer> result = new EnumMap<>(VehicleType.class);
    for (VehicleType vt : VehicleType.values()) {
        SpotSize size = vt.requiredSize();
        int count = floors.stream()
                .flatMap(f -> f.spots().stream())
                .filter(s -> s.size == size && s.available)
                .mapToInt(s -> 1).sum();
        result.put(vt, count);
    }
    return Collections.unmodifiableMap(result);
}
```

---

## Walkthrough

Scenario from the kata test: 2-level lot (level 0: 10 SMALL / 20 MEDIUM / 5 LARGE; level 1: same). Three vehicles park, one exits.

1. `lot.park(new Vehicle("BIKE-1", BIKE))`
   - `VehicleType.BIKE.requiredSize()` → `SMALL`
   - Lock acquired. Iterate floors: floor 0, `firstAvailable(SMALL)` returns spot `(0,0)`.
   - `spot.available = false`. Ticket `T1` created with `entryTime = now`. Stored in `activeTickets["T1"] = spot(0,0)`. Lock released.

2. `lot.park(new Vehicle("CAR-1", CAR))`
   - Needs MEDIUM. Floor 0 first available MEDIUM → spot `(0, 10)` (if spots are numbered sequentially after SMALL block).
   - Ticket `T2` issued.

3. `lot.park(new Vehicle("TRUCK-1", TRUCK))`
   - Needs LARGE. Floor 0 first LARGE spot → `(0, 30)`.
   - Ticket `T3` issued.

4. `lot.unpark(T1)` (BIKE leaves after 45 minutes)
   - `activeTickets.remove("T1")` → spot `(0,0)`.
   - `spot.available = true` (under lock).
   - `durationMinutes = ceil(45) = 45`. `fullHours = ceil(45/60) = 1`.
   - `fee = BIKE_rate × 1`. `Receipt` returned.

5. `lot.getAvailability()` → `{BIKE=10, CAR=19, TRUCK=4}` (BIKE spot 0 freed, one CAR and one TRUCK still occupied across the lot).

---

## Concurrency & edge cases

| Scenario | Guard |
|---|---|
| Two threads race for the last SMALL spot | `ReentrantLock` in `park()` ensures only one thread can assign a spot at a time. The other sees no available spot and gets `ParkingLotFullException`. |
| Double redemption of a ticket | `ConcurrentHashMap.remove()` is atomic — only one caller gets the spot back; the second gets `null` → `InvalidTicketException`. |
| Ticket for a spot that no longer exists | Cannot happen: tickets are only created from spots produced by the factory from `LotConfig`, so `(level, spotNumber)` always resolves. |
| Negative / zero duration | Guard in `unpark`: if `durationMinutes < 1`, clamp to 1 to avoid charging zero for ultra-fast exits. |
| `null` vehicle or ticket | `Objects.requireNonNull` guard at method entry — fail fast before acquiring the lock. |
| Clock skew / test determinism | Inject `java.time.Clock` via constructor; tests pass `Clock.fixed(...)` for reproducible durations. |

---

## Complexity & extensibility

**Big-O of core ops**

| Operation | Time | Notes |
|---|---|---|
| `park()` | O(F × S) | F floors, S spots per floor per size. With a `PriorityQueue<ParkingSpot>` per `SpotSize`, drops to **O(log S)** amortised. |
| `unpark()` | O(log S) for map remove | `ConcurrentHashMap.remove` is O(1) amortised; lock + spot toggle is O(1). |
| `getAvailability()` | O(F × S) | Stream over all spots. Cache available-count per size as `AtomicInteger` for O(1) reads. |

**Adding a new vehicle type (e.g. BUS → EXTRA_LARGE)**

1. Add `EXTRA_LARGE` to `SpotSize`.
2. Add `BUS { public SpotSize requiredSize() { return SpotSize.EXTRA_LARGE; } }` to `VehicleType`.
3. Include `SpotSize.EXTRA_LARGE` counts in `FloorConfig`.

Zero changes to `ParkingLot`, `ParkingFloor`, or `PricingStrategy`. The enum dispatch and config-driven construction absorb the extension entirely.

---

## Follow-ups

1. **Reserved / monthly-pass spots** — introduce a `SpotCategory` enum (`PUBLIC`, `RESERVED`). Add a `SpotFilter` parameter to `park()` or use a secondary `park(Vehicle, SpotCategory)` overload. `PricingStrategy.calculate` already takes `VehicleType`; extend it with a `SpotCategory` parameter. No change to the core loop — just an additional predicate in `firstAvailable`.

2. **10 000 floors × 1 000 spots — sub-millisecond `park()`** — replace the per-floor list scan with a `Map<SpotSize, PriorityQueue<ParkingSpot>>` (min-heap by `(level, spotNumber)`). `park()` becomes one `poll()` → O(log N) total. `unpark()` calls `offer()` to return the spot → also O(log N). Replace the coarse lock with a per-size `ReentrantLock` (or `Semaphore`) for maximum parallelism across different vehicle types.

3. **Persistence across JVM restarts** — serialize the `activeTickets` map to a durable store (Redis or a DB) on every `park()`/`unpark()`. On startup, reload active tickets and mark corresponding spots occupied. Use a write-ahead log pattern: persist before confirming success to the caller, so a crash mid-operation leaves the lot in a recoverable state rather than an inconsistent one.
