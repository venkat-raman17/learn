# The LLD interview framework

A repeatable way to take a vague prompt ("design a parking lot") to clean, extensible classes in
~40 minutes. Narrate each step out loud — interviewers grade your *process* and trade-offs, not
just the final diagram.

## The 7 steps

1. **Clarify requirements & scope** (5 min). Functional ("a car enters, gets a ticket, pays on
   exit") and non-functional (concurrency, scale, persistence?). Pin the scope explicitly — "I'll
   support cars/bikes and hourly pricing; multi-floor but single lot; in-memory state." Write down
   the use cases you'll satisfy.
2. **Identify core entities (nouns).** Pull the nouns from the requirements: `ParkingLot`, `Level`,
   `ParkingSpot`, `Vehicle`, `Ticket`, `Payment`. These become your classes/enums.
3. **Define relationships & cardinality.** Has-a vs is-a. `ParkingLot` *has many* `Level`; `Level`
   *has many* `ParkingSpot`; `Car` *is-a* `Vehicle`. Prefer **composition over inheritance**.
4. **Sketch the class diagram.** Fields, key methods, visibility. Use interfaces/abstract classes
   for the things that vary (`Vehicle`, `PricingStrategy`, `PaymentMethod`).
5. **Define the public APIs.** The handful of operations the system exposes:
   `Ticket park(Vehicle)`, `Payment unpark(Ticket)`. Method signatures force you to be concrete.
6. **Apply design patterns where they fit** (not everywhere). Strategy for pricing, Factory for
   creating vehicles/spots, Singleton for the lot registry, Observer for "spot freed" events,
   State for ticket lifecycle. Name the pattern and *why*.
7. **Concurrency, edge cases, extensibility.** What happens when two cars race for the last spot
   (lock the spot / atomic assignment)? Lot full? Lost ticket? Then: "to add a new vehicle type or
   pricing rule, I only touch X" — show the design is open for extension.

## Time budget (45 min)
Requirements 5 · entities + relationships 10 · class diagram + APIs 15 · patterns + concurrency 10
· walk through a use case end-to-end 5.

## What "good" looks like at Staff level
- **SOLID by default** (see [`solid.md`](solid.md)) — especially single-responsibility and
  depending on abstractions.
- **The varying parts are behind interfaces** — you can predict the follow-up ("now add valet
  parking / surge pricing") and answer "that's a new `Strategy`, no existing code changes."
- **Thread-safety reasoned about**, not hand-waved (what's the shared mutable state, how is it
  guarded — a lock, a concurrent collection, an atomic CAS?).
- **You drove it** — stated assumptions, made decisions, called out trade-offs, didn't wait to be
  asked.

## Common problems to practice (in `backend-service`)
Parking lot · elevator system · vending machine · rate limiter · LRU/LFU cache · Splitwise ·
notification service · logging framework · tic-tac-toe/chess · in-memory key-value store.
See [`README.md`](README.md) for the full list and the UI-LLD variants.
