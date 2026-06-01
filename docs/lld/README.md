# Low-Level Design (LLD)

Turn a vague prompt into clean, extensible classes/APIs with the right patterns, concurrency, and
boundaries — for **backend (Java)** and **frontend (React)**. Code practice lives in
[`../../backend-service/`](../../backend-service/) and [`../../web/spa-react-vite/`](../../web/spa-react-vite/).

## Foundations (read first) — ✅ written
- [x] [`oop-refresher.md`](oop-refresher.md) — the four pillars, composition over inheritance, interface vs abstract, coupling/cohesion, UML
- [x] [`solid.md`](solid.md) — SOLID with Java examples and the smell that signals each violation
- [x] [`framework.md`](framework.md) — the 7-step LLD interview framework (requirements → entities → diagram → APIs → patterns → concurrency → extensibility)
- [x] [`design-patterns.md`](design-patterns.md) — creational / structural / behavioral catalog, the interview core (Strategy, Factory, Observer, State, Decorator, Builder…) mapped to SOLID

## Practice problems — ✅ scaffolded (practice-first)
**Backend (Java)** in `backend-service/src/main/java/com/venkat/backend/lld/<problem>/` — each has a
`package-info.java` spec, a stubbed API skeleton (methods throw `UnsupportedOperationException`),
and a `@Disabled` JUnit test that defines "done". Design the internals, implement, delete
`@Disabled`, make it pass.
- [ ] parkinglot · [ ] ratelimiter · [ ] cache (LRU/LFU) · [ ] vendingmachine · [ ] elevator
- [ ] splitwise · [ ] notification · [ ] logger  *(tic-tac-toe / chess: future add)*

**UI (React)** in `web/spa-react-vite/src/lld/<problem>/` — each has a `README.md` spec and a
typed starter component stub. Implement the component.
- [ ] data-table · [ ] autocomplete · [ ] button (design system) · [ ] toast/notification

Reference designs: each kata folder has a `SOLUTION.md` (read it after you attempt).

## Why this matters at Staff level
LLD shows you can model a domain, pick patterns deliberately (not cargo-cult), reason about
thread-safety, and design for change. UI LLD adds component API design, state ownership, and
accessibility as first-class concerns.

## Resources
- *Head First Design Patterns* (2e) · [refactoring.guru/design-patterns](https://refactoring.guru/design-patterns)
- [Grokking the Low Level Design Interview (DesignGurus)](https://www.designgurus.io/course/grokking-the-low-level-design-interview-using-ood-principles)
