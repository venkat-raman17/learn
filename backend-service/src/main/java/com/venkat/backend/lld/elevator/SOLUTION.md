# Elevator System — reference design

## Approach

**Entity model**

The domain has three moving parts: the *building* (floor count, pending unassigned
hall-calls), a collection of *elevators* (each with its own stop queue and state
machine), and a *scheduler* that maps an unassigned hall-call to an elevator.
`ElevatorSystem` is the façade that owns all three.

**Design patterns**

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | `SchedulingStrategy` / `NearestIdleStrategy` | Swap scheduling algorithm (nearest-idle → SCAN → round-robin) at construction time or runtime without touching the core loop |
| State | `Elevator` tick logic — three implicit states: `IDLE`, `MOVING`, `DOOR_OPEN` | Each state reacts differently to a tick; modelling it as an enum-driven state machine avoids deeply nested `if` chains |
| Snapshot / DTO | `ElevatorState` record | Gives the strategy an immutable view; observers never touch mutable internals |
| Façade | `ElevatorSystem` | Single entry-point; hides the `Elevator[]` array and the pending queue from callers |

---

## Class design

| Type | Kind | Responsibility |
|------|------|----------------|
| `ElevatorSystem` | class (façade) | Owns elevators, strategy, pending hall-call queue; routes public API calls; runs the tick loop |
| `Elevator` | class (internal) | Holds current floor, `ElevatorDirection`, `DoorState`, sorted stop set; implements per-tick state machine |
| `ElevatorState` | record | Immutable snapshot: `id`, `currentFloor`, `direction`, `doorState` |
| `SchedulingStrategy` | interface | `selectElevator(states, floor, direction) -> int id` |
| `NearestIdleStrategy` | class | Picks the IDLE elevator with minimum `|currentFloor - requestFloor|`; falls back to any idle car if none perfect |
| `Direction` | enum | `UP`, `DOWN` — external hall-call panel |
| `ElevatorDirection` | enum | `UP`, `DOWN`, `IDLE` — elevator cabin movement |
| `DoorState` | enum | `OPEN`, `CLOSED` |

---

## Key code

### Elevator internal model

```java
class Elevator {
    final int id;
    int currentFloor;
    ElevatorDirection direction = ElevatorDirection.IDLE;
    DoorState doorState = DoorState.CLOSED;

    // TreeSet keeps stops sorted; ascending for UP, descending for DOWN.
    // One set is sufficient: next-stop logic inspects direction.
    private final TreeSet<Integer> stops = new TreeSet<>();

    void addStop(int floor) { stops.add(floor); }

    // Called by ElevatorSystem.step() — returns the new ElevatorState.
    void tick() {
        if (doorState == DoorState.OPEN) {
            // Phase 1: close door, remove the floor we just served.
            stops.remove(currentFloor);
            doorState = DoorState.CLOSED;
            if (stops.isEmpty()) direction = ElevatorDirection.IDLE;
            return;
        }

        if (stops.isEmpty()) { direction = ElevatorDirection.IDLE; return; }

        // Phase 2: determine next stop and move one floor toward it.
        int next = nextStop();
        if (next > currentFloor)      { direction = ElevatorDirection.UP;   currentFloor++; }
        else if (next < currentFloor) { direction = ElevatorDirection.DOWN;  currentFloor--; }

        // Phase 3: arrived?
        if (currentFloor == next) doorState = DoorState.OPEN;
    }

    private int nextStop() {
        // SCAN-lite: continue in current direction while stops exist ahead;
        // reverse only when none remain in current direction.
        if (direction == ElevatorDirection.UP || direction == ElevatorDirection.IDLE) {
            Integer ahead = stops.ceiling(currentFloor);
            if (ahead != null) return ahead;
        }
        // Going DOWN or no stops ahead — take the highest stop below (or reverse).
        Integer below = stops.floor(currentFloor);
        return (below != null) ? below : stops.first();
    }

    ElevatorState snapshot() {
        return new ElevatorState(id, currentFloor, direction, doorState);
    }
}
```

### ElevatorSystem tick loop

```java
public void step() {
    // Phase 1 & 2: advance every elevator's state machine.
    for (Elevator e : elevators) e.tick();

    // Phase 3: dispatch pending hall-calls to newly-idle elevators.
    Iterator<ExternalRequest> it = pendingRequests.iterator();
    while (it.hasNext()) {
        ExternalRequest req = it.next();
        List<ElevatorState> snapshot = getStates();
        // Strategy sees only IDLE elevators as useful; it must handle its own
        // filtering, but NearestIdleStrategy will return -1 if none are idle.
        int chosen = strategy.selectElevator(snapshot, req.floor(), req.direction());
        if (chosen >= 0) {
            elevators[chosen].addStop(req.floor());
            it.remove();
        }
    }
}
```

### NearestIdleStrategy

```java
class NearestIdleStrategy implements SchedulingStrategy {
    @Override
    public int selectElevator(List<ElevatorState> states, int floor, Direction direction) {
        int bestId = -1, bestDist = Integer.MAX_VALUE;
        for (ElevatorState s : states) {
            if (s.direction() != ElevatorDirection.IDLE) continue;
            int dist = Math.abs(s.currentFloor() - floor);
            if (dist < bestDist) { bestDist = dist; bestId = s.id(); }
        }
        return bestId;  // -1 signals "no idle elevator available right now"
    }
}
```

### ExternalRequest helper record (package-private)

```java
record ExternalRequest(int floor, Direction direction) {}
```

---

## Walkthrough

Scenario: 10-floor building, 2 elevators (E0 at floor 1 IDLE, E1 at floor 5 IDLE).
Passenger presses UP on floor 3; another presses DOWN on floor 8.

1. `externalRequest(3, UP)` — strategy compares |1-3|=2 (E0) vs |5-3|=2 (E1); tie-break by id → E0 assigned, stop 3 added.
2. `externalRequest(8, DOWN)` — E1 is still IDLE, |5-8|=3 vs E0 is now non-IDLE → E1 assigned, stop 8 added.
3. `step()` tick 1 — E0 moves 1→2 (MOVING UP); E1 moves 5→6 (MOVING UP).
4. `step()` tick 2 — E0 moves 2→3, arrives → door OPEN; E1 moves 6→7.
5. `step()` tick 3 — E0 door closes, stop 3 removed → IDLE; E1 moves 7→8, arrives → door OPEN.
6. `step()` tick 4 — E1 door closes, stop 8 removed → IDLE.

`getStates()` after tick 4: both elevators IDLE, E0 at floor 3, E1 at floor 8.

---

## Concurrency & edge cases

| Concern | Guard |
|---------|-------|
| `step()` called concurrently | Wrap the whole method in `synchronized(this)` or a `ReentrantLock`; holds briefly, acceptable for a simulation loop |
| Strategy swap mid-tick | Hold a `ReadWriteLock` around `this.strategy`; `step()` grabs a read lock; `setStrategy()` grabs a write lock |
| Duplicate stops | `TreeSet.add()` is idempotent — pressing the same floor button twice is safe |
| Invalid floor/id args | Validate in `externalRequest` and `internalRequest`; throw `IllegalArgumentException` |
| All elevators busy, hall-call arrives | Keep the call in `pendingRequests` (a `LinkedList`); retry dispatch every tick |
| Door stays OPEN one full tick | By design: tick N opens door, tick N+1 closes it and de-queues the floor |
| Direction confusion at boundary floors | `nextStop()` always finds a real stop via `TreeSet.ceiling/floor`; can never return out-of-bounds |

---

## Complexity & extensibility

**Big-O**

| Operation | Complexity | Notes |
|-----------|------------|-------|
| `step()` | O(N + P) | N elevators each tick in O(log S); P = pending hall-calls (usually small) |
| `externalRequest` | O(N) | Strategy scans all states once |
| `internalRequest` | O(log S) | `TreeSet.add` where S = stops per elevator |
| `getStates()` | O(N) | One record allocation per elevator |

**Open/Closed extensibility**

- **New strategy**: implement `SchedulingStrategy`, pass to constructor — zero edits to existing classes.
- **Express elevators**: subclass `Elevator` to override `addStop` with a floor-range check, or implement `SchedulingStrategy` that never returns express-car ids for non-express floors.
- **Observer / arrival events**: add an `ArrivalListener` functional interface; `Elevator.tick()` calls it when the door opens. Core loop stays unchanged.
- **Runtime strategy swap**: add `ElevatorSystem.setStrategy(SchedulingStrategy)` guarded by a write lock.

---

## Follow-ups

1. **SCAN algorithm**: maintain direction until the last stop in that direction is served, then reverse. Worst-case wait is bounded by `2*M` ticks vs unbounded starvation possible with nearest-idle when high-traffic floors monopolise idle cars.

2. **Multi-threaded `step()`**: each `Elevator`'s stop-set needs a `ConcurrentSkipListSet` (sorted, thread-safe); the outer pending queue needs a `ConcurrentLinkedQueue`. `step()` itself should still be serialised (one logical clock) — use a `ReentrantLock` around the tick body to prevent two threads from double-advancing an elevator.

3. **Express elevators**: implement `FilteredSchedulingStrategy` that wraps another strategy and marks express-car ids ineligible for non-express floors. No changes to `ElevatorSystem` or `Elevator`.

4. **Runtime strategy swap**: expose `setStrategy(SchedulingStrategy s)` protected by a `WriteLock`; `step()` holds the corresponding `ReadLock` so a swap cannot interleave with dispatch.

5. **Energy-saving mode**: during off-peak, park idle elevators at lobby (floor 1) after N idle ticks. Implement as a `ParkingStrategy` decorator around `NearestIdleStrategy` — again, zero core changes.
