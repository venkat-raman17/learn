# Splitwise — reference design

---

## Approach

**Entity model**

The system has five core entities and one ledger:

| Entity | Role |
|---|---|
| `User` | Value object — userId + name |
| `Group` | Aggregates a `Set<User>`; enforces member validity |
| `Expense` | Records payer, amount, and a list of resolved `Split` objects |
| `Split` | (userId, share) — the per-participant share after strategy runs |
| `BalanceSheet` | Central ledger: `Map<String, Map<String, Double>>` — net pairwise debts |

**Design patterns**

- **Strategy** (`SplitStrategy`) — the primary pattern. Each `SplitType` maps to a stateless strategy object. Adding a new split type (`SHARE`, `CAP`) requires only a new class and a factory entry; no existing code changes.
- **Factory** (`SplitStrategyFactory`) — a static map from `SplitType` enum to `SplitStrategy` instance. Isolates strategy selection from the service.
- **Repository / Service separation** — `UserRepository` and `GroupRepository` own data stores; `ExpenseService` owns expense logic and delegates to the repositories and the ledger. `ExpenseManager` is a thin facade that wires them together.
- **Facade** (`ExpenseManager`) — the public API class coordinates internal services without exposing them.

---

## Class design

| Class / Interface | Responsibility |
|---|---|
| `ExpenseManager` | Public facade; factory via `create()`; delegates to internal services |
| `User` | Immutable value: `userId`, `name` |
| `Group` | `groupId`, `groupName`, `Set<String> memberIds` |
| `Expense` | `expenseId`, `payerId`, `amount`, `List<Split>` |
| `Split` | `(userId, share)` — one participant's computed share |
| `SplitStrategy` (interface) | `Map<String,Double> compute(amount, participants, details)` |
| `EqualSplitStrategy` | `share = amount / n` for each participant |
| `ExactSplitStrategy` | Pass-through; validates sum == amount |
| `PercentSplitStrategy` | `share = amount * pct / 100`; validates sum of pct == 100 |
| `SplitStrategyFactory` | Static `EnumMap<SplitType, SplitStrategy>` — returns correct strategy |
| `BalanceSheet` | `Map<String, Map<String, Double>>` ledger; `debit`, `credit`, `settle`, `getEntries` |
| `UserRepository` | `Map<String, User>` — add, lookup, validate existence |
| `GroupRepository` | `Map<String, Group>` — add, lookup |
| `ExpenseService` | Orchestrates: validate inputs, call strategy, apply splits to ledger |

---

## Key code

**BalanceSheet — the ledger invariant**

Keep a single canonical direction: `balance[a][b] > 0` means `a` owes `b`. Never store both directions simultaneously.

```java
// net(debtor=from, creditor=to) += share
void debit(String from, String to, double share) {
    if (from.equals(to)) return;          // payer owes themselves nothing
    // Normalise: always store under the pair (from, to) cancelling cross-entries
    double existing = getNet(from, to);   // positive → from owes to
    double opposite = getNet(to, from);   // positive → to owes from

    if (opposite > 0) {
        // creditor and debtor are swapped — reduce opposite debt first
        if (share >= opposite) {
            clearEntry(to, from);
            setEntry(from, to, share - opposite);
        } else {
            setEntry(to, from, opposite - share);
        }
    } else {
        setEntry(from, to, existing + share);
    }
}

void settle(String u1, String u2) {
    clearEntry(u1, u2);
    clearEntry(u2, u1);
}
```

**ExpenseService.addExpense — the orchestration**

```java
void addExpense(String payerId, double amount,
                List<String> participantIds,
                SplitType type, Map<String, Double> details) {

    User payer = userRepo.getOrThrow(payerId);
    participantIds.forEach(userRepo::getOrThrow);          // validate all

    SplitStrategy strategy = SplitStrategyFactory.get(type);
    Map<String, Double> shares = strategy.compute(amount, participantIds, details);

    shares.forEach((participantId, share) -> {
        if (!participantId.equals(payerId)) {
            balanceSheet.debit(participantId, payerId, share);
        }
        // payer's own share: they already paid it, no self-entry needed
    });
}
```

**EqualSplitStrategy**

```java
public Map<String, Double> compute(double amount,
                                   List<String> participants,
                                   Map<String, Double> details) {
    double share = amount / participants.size();
    return participants.stream()
        .collect(Collectors.toMap(id -> id, id -> share));
}
```

**ExactSplitStrategy — validation**

```java
public Map<String, Double> compute(double amount,
                                   List<String> participants,
                                   Map<String, Double> details) {
    double sum = details.values().stream().mapToDouble(Double::doubleValue).sum();
    if (Math.abs(sum - amount) > 1e-9)
        throw new IllegalArgumentException("Exact splits must sum to " + amount);
    // ensure every participant has an entry
    participants.forEach(id -> {
        if (!details.containsKey(id))
            throw new IllegalArgumentException("No exact amount for " + id);
    });
    return Map.copyOf(details);
}
```

**PercentSplitStrategy**

```java
public Map<String, Double> compute(double amount,
                                   List<String> participants,
                                   Map<String, Double> details) {
    double sumPct = details.values().stream().mapToDouble(Double::doubleValue).sum();
    if (Math.abs(sumPct - 100.0) > 1e-9)
        throw new IllegalArgumentException("Percentages must sum to 100");
    return participants.stream()
        .collect(Collectors.toMap(id -> id,
                                  id -> amount * details.get(id) / 100.0));
}
```

**SplitStrategyFactory**

```java
private static final EnumMap<SplitType, SplitStrategy> MAP = new EnumMap<>(Map.of(
    SplitType.EQUAL,   new EqualSplitStrategy(),
    SplitType.EXACT,   new ExactSplitStrategy(),
    SplitType.PERCENT, new PercentSplitStrategy()
));

public static SplitStrategy get(SplitType type) {
    return Objects.requireNonNull(MAP.get(type), "Unknown SplitType: " + type);
}
```

---

## Walkthrough

**Scenario from the kata spec:**

```
addUser("u1","Alice"), addUser("u2","Bob"), addUser("u3","Charlie")

addExpense("u1", 300.0, ["u1","u2","u3"], EQUAL, {})
  → EqualSplitStrategy: each share = 100.0
  → debit(u2 → u1, 100)  debit(u3 → u1, 100)  (u1's own share skipped)
  → balanceSheet: {u2: {u1:100}, u3: {u1:100}}

addExpense("u1", 200.0, ["u2","u3"], EXACT, {u2:100, u3:100})
  → ExactSplitStrategy: u2→100, u3→100
  → debit(u2 → u1, 100)  debit(u3 → u1, 100)
  → balanceSheet: {u2: {u1:200}, u3: {u1:200}}

showBalances("u2")
  → iterate balanceSheet row "u2": [{from=u2, to=u1, amount=200}]
  → also scan all rows for entries pointing at u2 (none here)
  → returns [BalanceEntry("u2","u1",200)]

settle("u1","u2")
  → clearEntry(u1,u2) clearEntry(u2,u1)
  → balanceSheet: {u3: {u1:200}}
```

---

## Concurrency & edge cases

**Edge cases to guard:**

| Case | Guard |
|---|---|
| Unknown userId in any operation | `userRepo.getOrThrow` throws `IllegalArgumentException` |
| Duplicate userId / groupId | Check map before put; throw on collision |
| Empty participant list | Validate `!participantIds.isEmpty()` before strategy call |
| amount <= 0 | Validate at `addExpense` entry; throw `IllegalArgumentException` |
| EXACT/PERCENT keys don't cover all participants | Strategy validates each participant has an entry |
| Floating-point drift (sums off by tiny epsilon) | Use `Math.abs(sum - expected) > 1e-9` for all equality checks; consider `BigDecimal` for production |
| Payer not in participant list | Payer paid but owes nothing in this expense — correctly handled because `debit` skips `from.equals(to)` and payer is never in the debit loop unless they are a participant |

**Concurrency (follow-up, not baseline):**

Use a `ReentrantReadWriteLock` on `BalanceSheet`:
- `addExpense` acquires the **write** lock for the duration of all `debit` calls.
- `showBalances` / `showAllBalances` acquire the **read** lock (multiple readers allowed).
- `settle` acquires the **write** lock.

Avoid per-row locking on the nested map — the compound read-modify-write in `debit` must be atomic across both map levels to prevent lost updates.

---

## Complexity & extensibility

| Operation | Time | Space |
|---|---|---|
| `addUser` / `createGroup` | O(1) amortised | O(U) / O(G) |
| `addExpense` | O(P) where P = participants | O(P) per expense |
| `showBalances(userId)` | O(U) — scan all rows for entries to/from userId | — |
| `showAllBalances()` | O(U²) worst case | — |
| `settle` | O(1) | — |

**Adding a new split type** (e.g. `SHARE`):

1. Add `SHARE` to the `SplitType` enum.
2. Implement `ShareSplitStrategy implements SplitStrategy` — no existing class changes.
3. Register it in `SplitStrategyFactory.MAP` — one line.

`ExpenseService.addExpense`, `BalanceSheet`, and all other classes remain untouched — Open/Closed Principle satisfied.

---

## Follow-ups

1. **SHARE-based split** — `splitDetails` maps userId to integer share count. Strategy: `share_i = amount * count_i / sum(counts)`. Zero changes to any existing class; add enum value + strategy + factory entry.

2. **Minimum cash-flow settlement** — Compute net balance per user (positive = creditor, negative = debtor). Greedily match the largest creditor with the largest debtor and generate a payment, reducing both; repeat until all are zero. This is O(U log U) with a priority queue and minimises transaction count to at most U-1.

3. **Concurrency** — `ReentrantReadWriteLock` on `BalanceSheet` is the best fit here: reads are frequent and reads can proceed concurrently; writes (`addExpense`, `settle`) are exclusive. `ConcurrentHashMap` alone is insufficient because `debit` is a compound read-modify-write that needs to be atomic across two map levels.

4. **Group-scoped expenses** — Add `addGroupExpense(groupId, payerId, ...)` on `ExpenseService`. Validate that `participantIds` is a subset of `group.getMemberIds()`; then delegate to the existing `addExpense` path unchanged.

5. **Persistence** — Swap `UserRepository` / `GroupRepository` / `BalanceSheet` for JPA-backed implementations behind the same interfaces; `ExpenseService` requires no changes.
