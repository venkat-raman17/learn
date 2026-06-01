# Design patterns catalog

Reusable solutions to recurring design problems. You don't need all 23 — know the **interview
core** cold (★), recognize the rest. Always justify *why* a pattern fits; cargo-culting patterns is
itself a smell.

## Creational — how objects get made
- **★ Factory Method** — defer instantiation to a method/subclass; create by "kind" without `new`
  scattered everywhere. *Use:* `VehicleFactory.create(type)`.
- **★ Builder** — construct complex objects step by step; avoids telescoping constructors. *Use:*
  `new Pizza.Builder().size(L).cheese().build()`.
- **Abstract Factory** — families of related objects (`UIFactory` → Mac vs Windows widgets).
- **★ Singleton** — one instance, global access. *Use sparingly* (global state hurts testability);
  in Java prefer an enum or DI-managed singleton.
- **Prototype** — clone an existing instance instead of building anew.

## Structural — how objects compose
- **★ Adapter** — make an incompatible interface usable (`Enumeration` → `Iterator`).
- **★ Decorator** — add behavior by wrapping, not subclassing (`BufferedInputStream`). Open/Closed
  in action.
- **★ Facade** — one simple interface over a messy subsystem.
- **Composite** — treat individual objects and groups uniformly (file/folder tree).
- **Proxy** — stand-in controlling access (lazy load, caching, access control).
- **Bridge / Flyweight** — decouple abstraction from impl / share fine-grained objects to save memory.

## Behavioral — how objects interact
- **★ Strategy** — encapsulate interchangeable algorithms behind an interface (`PricingStrategy`,
  `SortStrategy`). The most-used LLD pattern; the answer to most "now add a new rule" follow-ups.
- **★ Observer** — publish/subscribe; notify dependents on state change (event listeners, "spot
  freed"). 
- **★ State** — object changes behavior as its state changes (ticket: ISSUED → PAID → EXITED);
  replaces big `if (state == ...)` blocks.
- **★ Command** — encapsulate a request as an object (undo/redo, queues, scheduling).
- **Template Method** — fixed algorithm skeleton, subclasses fill steps.
- **Iterator / Chain of Responsibility / Mediator / Visitor / Memento** — traverse without exposing
  internals / pass a request along handlers / centralize chatty interactions / op on a structure
  without changing it / capture & restore state.

## Patterns ↔ SOLID
Most patterns are SOLID made concrete: Strategy/Decorator/Factory = Open-Closed; depending on the
Strategy/Observer *interface* = Dependency Inversion; small handler interfaces = Interface
Segregation. When you reach for a pattern, you're usually buying "extend without modifying."

## In practice
For each classic LLD problem, pick 1–3 patterns deliberately: parking lot (Strategy pricing,
Factory spots, Singleton lot), vending machine (State), notification service (Observer + Strategy
per channel), logger (Chain of Responsibility / Strategy). Reference: refactoring.guru.
