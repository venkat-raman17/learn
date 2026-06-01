# SOLID

Five principles that keep object-oriented code changeable. At Staff level you should *name the
violation* in a design review and show the refactor.

## S — Single Responsibility
A class should have one reason to change. If `Invoice` computes totals **and** formats PDF **and**
emails itself, three unrelated changes all touch it. Split into `Invoice`, `InvoiceRenderer`,
`InvoiceMailer`.
> Smell: "and" in a class description; methods that don't use the same fields (low cohesion).

## O — Open/Closed
Open for extension, closed for modification. Adding a new shape shouldn't mean editing a
`switch (shape.type)`. Make `Shape` an interface with `area()`; new shapes implement it.
> Smell: a growing `if/else`/`switch` on a type code. Replace with polymorphism or a Strategy.

```java
// Closed for modification, open for extension:
interface DiscountPolicy { double apply(double total); }
class BlackFriday implements DiscountPolicy { public double apply(double t){ return t*0.7; } }
// add new policies without touching the checkout code
```

## L — Liskov Substitution
A subtype must be usable anywhere its supertype is, without surprises. The classic violation:
`Square extends Rectangle` then `setWidth` breaks `height` invariants. If overriding a method
*weakens* guarantees (throws where the parent didn't, narrows accepted inputs), it's an LSP break.
> Rule of thumb: inheritance is for "behaves-as", not just "has the same fields".

## I — Interface Segregation
Don't force clients to depend on methods they don't use. A fat `Machine { print(); scan(); fax(); }`
forces a simple printer to stub `scan/fax`. Split into `Printer`, `Scanner`, `Fax`.
> Smell: implementations full of `throw new UnsupportedOperationException()`.

## D — Dependency Inversion
High-level modules depend on **abstractions**, not concretions; details depend on abstractions.
`OrderService` should depend on a `PaymentGateway` interface, not `StripeClient`. Inject the
concrete impl (constructor injection). This is what makes code testable (pass a fake) and
swappable.
```java
class OrderService {
  private final PaymentGateway gateway;          // abstraction
  OrderService(PaymentGateway gateway){ this.gateway = gateway; }   // injected
}
```

## How they connect
DIP + OCP + ISP together are *the* toolkit for "design for change": program to interfaces, inject
dependencies, keep interfaces small, extend by adding new implementers. SRP + LSP keep each piece
coherent and substitutable. In an interview, reach for these by name when justifying a boundary.
