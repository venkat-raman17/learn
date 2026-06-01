# Low-Level Design (LLD)

Turn a vague prompt into clean, extensible classes/APIs with the right patterns, concurrency, and
boundaries — for **backend (Java)** and **frontend (React)**. Code practice lives in
[`../../backend-service/`](../../backend-service/) and [`../../web/spa-react-vite/`](../../web/spa-react-vite/).

## Foundations (read first) — ✅ written
- [x] [`oop-refresher.md`](oop-refresher.md) — the four pillars, composition over inheritance, interface vs abstract, coupling/cohesion, UML
- [x] [`solid.md`](solid.md) — SOLID with Java examples and the smell that signals each violation
- [x] [`framework.md`](framework.md) — the 7-step LLD interview framework (requirements → entities → diagram → APIs → patterns → concurrency → extensibility)
- [x] [`design-patterns.md`](design-patterns.md) — creational / structural / behavioral catalog, the interview core (Strategy, Factory, Observer, State, Decorator, Builder…) mapped to SOLID

## Practice problems
**Backend (Java, in `backend-service`):**
- [ ] Parking Lot · [ ] Rate Limiter · [ ] LRU/LFU Cache · [ ] Splitwise · [ ] Elevator
- [ ] Vending Machine · [ ] Notification Service · [ ] Logger/Logging framework · [ ] Tic-Tac-Toe / Chess

**UI (React, in `web/spa-react-vite`):**
- [ ] Reusable Data Table (sort/filter/paginate/virtualize) · [ ] Autocomplete / Typeahead
- [ ] Design-system component (variants/states/a11y) · [ ] Modal/Toast/Notification system

Each problem: a short spec + a failing test/checklist → you implement → reference solution on request.

## Why this matters at Staff level
LLD shows you can model a domain, pick patterns deliberately (not cargo-cult), reason about
thread-safety, and design for change. UI LLD adds component API design, state ownership, and
accessibility as first-class concerns.

## Resources
- *Head First Design Patterns* (2e) · [refactoring.guru/design-patterns](https://refactoring.guru/design-patterns)
- [Grokking the Low Level Design Interview (DesignGurus)](https://www.designgurus.io/course/grokking-the-low-level-design-interview-using-ood-principles)
