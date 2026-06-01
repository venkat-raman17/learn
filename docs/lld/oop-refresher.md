# OOP refresher

The fundamentals LLD is built on. Quick reference, with the bits interviewers probe.

## The four pillars
- **Encapsulation** — bundle state + behavior, hide internals behind methods. Keep fields private;
  expose intent (`account.withdraw(x)`), not representation (`account.balance -= x`). Protects
  invariants.
- **Abstraction** — model the essential, hide the rest. A `PaymentGateway` interface says *what*
  (charge, refund) without the *how* (Stripe vs PayPal).
- **Inheritance** — share/extend behavior via an "is-a" relationship. Powerful but easy to misuse
  (see composition below).
- **Polymorphism** — one interface, many implementations; the runtime type decides behavior.
  `for (Shape s : shapes) s.area();` — no `switch` on type.

## Composition over inheritance
Inheritance is rigid (one parent, compile-time, exposes the base's API) and leaks LSP violations.
Prefer **has-a**: a `Car` *has an* `Engine`, not *is an* `Engine`. Compose behaviors with
interfaces/strategies you can swap at runtime.
> Use inheritance only for genuine "behaves-as" hierarchies with a stable base contract.

## Interface vs abstract class
- **Interface** — a contract, no state (default methods aside); a class can implement many. Use for
  capabilities that cut across unrelated types (`Comparable`, `Iterable`, `PricingStrategy`).
- **Abstract class** — shared state + partial implementation; single inheritance. Use for a
  template with common fields and some default behavior plus abstract hooks.

## Coupling & cohesion (the quality axes)
- **Low coupling** — modules depend on each other minimally and through abstractions. Changing one
  doesn't ripple. Achieved via DIP/interfaces.
- **High cohesion** — a class's members all serve one purpose. Achieved via SRP.
> Aim: *low coupling, high cohesion*. Most LLD smells reduce to one of these.

## UML you'll actually draw
- **Class diagram** — boxes (name / fields / methods), lines: association (—), composition
  (filled ◆, owner controls lifecycle), inheritance (▷), dependency (dashed →).
- **Sequence diagram** — objects across the top, time down, messages as arrows. Useful to walk a
  use case ("park a car") through your classes.

Keep diagrams legible, not exhaustive — they're a thinking aid, not the deliverable.
