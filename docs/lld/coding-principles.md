# Coding Principles

Companion to [`solid.md`](solid.md). SOLID governs class-level design; these principles govern
the code *inside* and *between* classes. Staff engineers apply them instinctively — and can
articulate the trade-off when breaking one is the right call.

---

## DRY — Don't Repeat Yourself

> Every piece of knowledge must have a single, unambiguous, authoritative representation.

**What it means:** duplicated logic is duplicated *decisions*. When the logic must change, you
pay twice — and the second copy is the one you forget.

**Good — extracted once:**
```java
// lld/cache/Cache.java — LRU node operations extracted so evict() and put() share the same logic
private void moveToHead(Node node) {
    removeNode(node);
    insertAfterHead(node);
}
// evict() and put() both call moveToHead() — the "what a cache hit means" decision lives once
```

**Good — factory centralises the choice:**
```java
// lld/ratelimiter/RateLimiterFactory.java
public static RateLimiter create(RateLimiterConfig config) {
    return switch (config.algorithm()) {
        case TOKEN_BUCKET  -> new TokenBucketRateLimiter(config);
        case SLIDING_WINDOW -> new SlidingWindowRateLimiter(config);
    };
}
// Every caller gets the right impl from one place; algorithm knowledge doesn't leak everywhere
```

**Anti-pattern to avoid:**
```java
// ❌ Same null-check scattered in 5 methods
if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId required");
// ✅ Extract once: static Preconditions.requireNonBlank(userId, "userId")
```

**Trade-off:** Premature DRY (abstracting two things that *look* similar but have different
reasons to change) is worse than duplication. The rule of three: abstract on the *third*
recurrence, not the second.

---

## KISS — Keep It Simple, Stupid

> The simpler solution is almost always correct. Complexity is debt you pay every time someone reads the code.

**What it means:** choose the boring, obvious implementation unless you have a measured reason
not to. The enemy is *accidental* complexity — the kind your architecture introduced, not the
kind the problem requires.

```python
# agentic/llm.py — FakeLLM grades by word overlap, not a neural scorer
# Simple, deterministic, testable — exactly what a test seam needs
def grade(self, query: str, doc: str) -> bool:
    q_words = set(query.lower().split())
    d_words = set(doc.lower().split())
    return len(q_words & d_words) >= 2
```

```typescript
// graphql-layer/server/resolvers.ts — in-memory Map, not a DB adapter with retries+pooling
// Swapping to a real DB is one function change; the complexity belongs there, not here
const store = new Map<string, Article>();
```

**Questions to ask before adding complexity:**
- Does this abstraction exist because the problem requires it, or because it feels nice?
- Could a future engineer understand this in 60 seconds?
- Am I solving a real bottleneck or an imagined one?

---

## YAGNI — You Aren't Gonna Need It

> Don't add functionality until it is needed.

**What it means:** future requirements are speculation. Unused code still has a cost: it must be
read, tested, documented, and migrated. Build what you need *today* for the problem you have
*today*.

**Violations to recognise:**
```java
// ❌ Adding a plugin system "in case we need it later"
interface Formatter { String format(LogEvent e); }
class FormatterRegistry { ... }   // 80 lines nobody calls

// ✅ One formatter, inline — extract when the second formatter appears
String format(LogEvent e) {
    return "[%s] %s: %s".formatted(e.level(), e.logger(), e.message());
}
```

**The YAGNI / DRY interaction:** DRY is about *existing* duplication. YAGNI is about *future*
features. They don't conflict — DRY the code you wrote; YAGNI the code you didn't.

---

## Fail-Fast

> Report an error as close to its source as possible, before bad state propagates.

**What it means:** validate at the entry point of every public method. An exception thrown at
the validation site is infinitely easier to debug than a `NullPointerException` five stack frames
deeper — or worse, a silently corrupt record in the database.

**Good — guard clauses at method entry:**
```java
// lld/auth/TokenService.java
public TokenPair issue(AuthUser user, Duration ttl) {
    Objects.requireNonNull(user, "user");
    if (ttl.isNegative() || ttl.isZero())
        throw new IllegalArgumentException("ttl must be positive, got: " + ttl);
    if (secret.length() < 32)
        throw new IllegalStateException("JWT secret too short — minimum 32 chars");
    // ... happy path follows with no further defensive checks needed
}
```

```python
# mcp-server/embeddings.py
def encode(self, texts: list[str]) -> np.ndarray:
    if not texts:
        raise ValueError("texts must be non-empty")   # fail here, not inside np.vstack
```

**Anti-pattern:**
```java
// ❌ Null check buried inside a helper called 3 frames later — useless stack trace
private String buildPayload(User user) {
    return user.getEmail().toLowerCase(); // NPE here; caller gave null 3 frames up
}
```

**Rule:** validate public API inputs; *trust* internal invariants (don't re-validate inside private methods that only you call).

---

## Immutability

> Prefer values that cannot change after creation. Shared mutable state is the root of most concurrency bugs.

**What it means:** an immutable object can be freely shared across threads, cached, and
reasoned about without worrying about who changed it and when.

**Java records — immutable by construction:**
```java
// lld/auth/AuthModels.java
public record Claims(String userId, String email, Role role, long expiresAt) {
    public boolean isExpired() { return System.currentTimeMillis() > expiresAt; }
    // No setters. No way to mutate. Safe to share across threads.
}
```

**Return copies, not references to mutable internals:**
```java
// lld/observability/MetricsCollector.java
public Map<String, Number> snapshot() {
    return Map.copyOf(new TreeMap<>(counters));  // caller cannot mutate the registry
}
```

**TypeScript `readonly`:**
```typescript
// observability/types.ts
interface ObservabilitySink {
  readonly events: readonly AnalyticsEvent[];  // callers read; only the sink writes
}
```

**Mutability where it belongs:** not everything should be immutable — `LongAdder` counters,
`CopyOnWriteArrayList` trace stores, React state — all *controlled* mutation behind a clean interface. The goal is to confine mutation, not eliminate it.

---

## Law of Demeter (Principle of Least Knowledge)

> A method should only call methods on: itself, its parameters, objects it creates, and its direct component objects.

**What it means:** each unit should only talk to its immediate neighbours. Long chains
(`a.getB().getC().doX()`) couple you to the internal structure of objects you don't own.

**Violation:**
```java
// ❌ DatastoreController knows that an Article has a Metadata that has an Author that has an email
String email = article.getMetadata().getAuthor().getEmail();
// Now Article, Metadata, and Author are all frozen — you can't refactor any of them
```

**Good — tell the object to do the work:**
```java
// ✅ Article owns the decision about its author email
String email = article.authorEmail();   // Article encapsulates Metadata internally
```

**GraphQL resolvers follow this naturally:**
```typescript
// graphql-layer/server/resolvers.ts
article: (_, { id }) => store.get(id) ?? null
// Resolver talks to the store; it doesn't reach inside Article to call sub-methods
```

**Heuristic:** more than one `.` on a line that crosses object boundaries is a smell. One `.`
on a builder (fluent API) or stream chain is fine — those are designed for chaining.

---

## Command-Query Separation (CQS)

> A method either changes state (command) or returns data (query) — never both.

**What it means:** mixing the two makes code hard to reason about. A "getter" that has side
effects is a trap; a "setter" that also returns something surprises callers.

**Good:**
```java
// lld/auth/RbacService.java
// Query — returns data, no side effects
boolean hasPermission(AuthUser user, Permission p) { ... }

// Command — mutates state, returns void
void grantRole(String userId, Role role) { ... }
```

```typescript
// observability/MemorySink.ts
trackEvent(event: AnalyticsEvent): void { ... }   // command — mutates buffer
subscribe(listener: () => void): () => void { ... } // query — returns unsubscribe fn
// subscribe() doesn't mutate; trackEvent() doesn't return
```

**The classic CQS violation:**
```java
// ❌ Stack.pop() — both removes AND returns. Unavoidable in standard APIs.
// ❌ cache.getOrCreate() — both creates AND returns.
// These are accepted exceptions; document them explicitly.
```

**When to break it:** `getOrCreate`, `compareAndSwap`, iterator `next()` — all are CQS
violations that are acceptable because atomicity requires it. Know *why* you're breaking it.

---

## Tell-Don't-Ask

> Tell objects what to do; don't ask for their data and do it yourself.

Closely related to Law of Demeter. CQS says *when* to return data; Tell-Don't-Ask says *who*
should own the behaviour.

**Violation — asking:**
```java
// ❌ Caller extracts data and makes the decision that belongs inside User
if (user.getSubscription().getExpiryDate().isBefore(LocalDate.now())) {
    user.setStatus(Status.EXPIRED);
}
```

**Good — telling:**
```java
// ✅ User owns the expiry logic
user.expireIfOverdue();   // User knows its own subscription; caller just tells it to check
```

**From the codebase:**
```java
// lld/notification/NotificationService.java
// ✅ Caller says send(notification) — doesn't reach in to extract recipient, template, channel
notificationService.send(notification);
// NotificationService asks nothing from the caller; it does the routing internally
```

```python
# agentic/rag_graph.py
# ✅ Each node tells the next what to do via typed state transitions
# The graph doesn't ask grade() "what did you find?" and then decide — decide() is a node
```

---

## Defensive Programming

> Write code that behaves predictably even when the environment doesn't.

**What it means:** assume inputs can be malformed, dependencies can fail, and invariants can be
violated. Build in checks, but at the right level — validate at system boundaries, trust
internal code.

**Where to be defensive:**
```java
// ✅ At HTTP/API boundaries — untrusted input
@PostMapping("/api/articles")
public Mono<Article> create(@RequestBody Map<String, String> body) {
    String title = Objects.requireNonNullElse(body.get("title"), "").strip();
    if (title.isBlank()) return Mono.error(new ResponseStatusException(BAD_REQUEST, "title required"));
    ...
}
```

**Where NOT to be defensive:**
```java
// ❌ Re-checking invariants inside private methods you control
private void insertAfterHead(Node node) {
    if (node == null) throw new IllegalArgumentException("node"); // you just created it above
    ...
}
// This adds noise and erodes trust in the actual external checks
```

**In tests:** defensive checks make assertions meaningful — a test that passes vacuously
(because a null slipped through) is worse than no test. Combine with fail-fast.

---

## Single Source of Truth (SSOT)

> Every piece of data or logic has exactly one canonical home. All other places reference that home, never copy it.

**Configuration as SSOT:**
```java
// lld/ratelimiter/RateLimiterConfig.java — one place holds algorithm + capacity + window
// RateLimiterFactory reads it; no algorithm logic leaks into controllers or tests
RateLimiterConfig config = new RateLimiterConfig(Algorithm.TOKEN_BUCKET, 100, Duration.ofMinutes(1));
RateLimiter limiter = RateLimiterFactory.create(config);
```

**Singleton sink as SSOT for observability:**
```typescript
// observability/MemorySink.ts
export const sink = new MemorySink();   // one instance, shared across the app
// ObservabilityProvider injects it; components read via useObservabilitySnapshot()
// No component holds its own copy of events
```

**In databases:** SSOT drives the "database per service" rule in microservices — each service
owns its data model; no two services share a table. Cross-service queries go through an API,
never a shared schema.

**Anti-pattern:** copying a value from one place to another and keeping both in sync. The sync
always drifts.

---

## Design by Contract (DbC)

> Every public method has a contract: preconditions (what it requires), postconditions (what it guarantees), and invariants (what is always true).

**What it means:** make the contract explicit — in types, in guard clauses, in tests, and in
documentation. Callers who violate preconditions get an exception immediately (fail-fast).
The method guarantees its postconditions if preconditions hold.

```java
// lld/auth/TokenService.java — contract spelled out in Javadoc + enforced in code
/**
 * @param user     non-null, must have at least one role
 * @param ttl      positive duration, max 24h
 * @return         a signed token pair valid for exactly ttl
 * @throws IllegalArgumentException if preconditions are violated
 */
public TokenPair issue(AuthUser user, Duration ttl) {
    // Preconditions (fail-fast)
    Objects.requireNonNull(user, "user");
    if (user.roles().isEmpty()) throw new IllegalArgumentException("user must have a role");
    if (ttl.toHours() > 24)    throw new IllegalArgumentException("ttl max 24h");

    // ... implementation

    // Postcondition implicit in return type — TokenPair is an immutable record,
    // so the caller knows both tokens are non-null strings
}
```

**Invariants — what is always true of an object:**
```java
// lld/observability/Histogram.java — invariant: reservoir is always within bounds
// Enforced by Algorithm R in record(); never violated because no other code touches the array
```

**In practice:** DbC is not about adding `assert` everywhere. It's about thinking clearly
about the contract and making it visible — through types (`Optional` vs nullable, records vs
mutable classes), guard clauses, and tests that verify boundary behaviour.

---

## Quick reference

| Principle | One-liner | Red flag |
|---|---|---|
| **DRY** | One representation per decision | Copy-paste, then "search-replace all" |
| **KISS** | Simplest thing that works | "Flexible" abstraction nobody uses |
| **YAGNI** | Build it when you need it | "Plugin system for future extensibility" |
| **Fail-Fast** | Crash loudly at the source | Silent nulls, magic `-1` returns |
| **Immutability** | Values don't change after creation | Shared mutable state, defensive copies everywhere |
| **Law of Demeter** | Talk to your neighbours | `a.b().c().doX()` across ownership boundaries |
| **CQS** | Commands change, queries read | `pop()` without acknowledging the violation |
| **Tell-Don't-Ask** | Tell objects to act, don't extract data to act yourself | Long getter chains doing work the class should own |
| **Defensive Programming** | Validate at boundaries, trust internals | Checks inside every private method |
| **SSOT** | One home for each fact | Keeping two copies in sync |
| **Design by Contract** | Explicit pre/post/invariants | Implicit assumptions, no validation |

---

## Interview talking points

1. **DRY vs KISS tension**: over-DRYing leads to a wrong abstraction that's harder to change than the duplication was. The real question is "same reason to change?" not "same syntax?".
2. **Fail-fast in distributed systems**: fail-fast at the client (circuit breaker, timeout) prevents cascade failures — the same principle applied to services, not just methods.
3. **Immutability and concurrency**: immutable objects eliminate the need for locks in read-heavy scenarios; describe how `CompletedSpan` records in the observability kata are safe to share across threads.
4. **CQS and event sourcing**: `append(event)` is a pure command; `project(events)` is a pure query. CQRS (the architectural pattern) scales this to separate read and write models.
5. **Tell-Don't-Ask and anemic domain models**: an anemic model (all getters, no behaviour) forces callers to implement business logic — violating Tell-Don't-Ask at architectural scale.
