# Vending Machine — reference design

---

## Approach

**Entity model**

The machine has three kinds of objects:

| Kind | Role |
|---|---|
| Value / enum | `Coin`, `Product`, `DispenseResult` — pure data, no behaviour |
| State objects | `NoMoneyState`, `HasMoneyState`, `DispensingState`, `SoldOutState` — behaviour per state |
| Context | `VendingMachine` — holds shared mutable data; delegates every call to the current state |

**Patterns chosen**

- **State (primary)** — each of the four states is its own class implementing `VendingMachineState`. The machine never inspects which state it is in; it just calls `currentState.someAction()`. Each state drives its own transition by calling `machine.setState(...)`. This eliminates the sprawling `switch`/`if-else` that would otherwise live in `VendingMachine`.
- **Strategy (change-making)** — extracting `ChangeStrategy` keeps the dispensing logic swappable (greedy coin-return vs. exact-denomination output) without touching any state class.
- **Factory Method (testing convenience)** — a static `VendingMachine.preloaded(Product...)` factory lets tests spin up a ready machine in one line.

**Why not Command or Observer?**
The lifecycle here is strictly synchronous and single-user per transaction; State alone cleanly models the four-step flow (`insert → select → dispense → idle`).

---

## Class design

| Class / Interface | Responsibility |
|---|---|
| `Coin` (enum) | Carries cent value; `getCents()` only |
| `Product` | Slot code, name, price, mutable stock; `setStock()` called after dispense |
| `DispenseResult` | Immutable DTO: `productName` + `changeCents` |
| `VendingMachineState` | Interface: `insertCoin`, `selectProduct`, `dispense`, `refund` |
| `NoMoneyState` | Idle. Accepts coins → `HasMoneyState`. Rejects all else. |
| `HasMoneyState` | Balance present. Accepts more coins (stays). `selectProduct` → `DispensingState` or `SoldOutState`. `refund` → `NoMoneyState`. |
| `DispensingState` | Product locked in. Only `dispense()` allowed → `NoMoneyState`. All others throw. |
| `SoldOutState` | Out-of-stock. Only `refund()` allowed → `NoMoneyState`. |
| `VendingMachine` | Context. Owns `inventory`, `balanceCents`, `selectedProduct`, `currentState`. Thin delegator plus package-private mutators for state objects. |

---

## Key code

### The state interface

```java
interface VendingMachineState {
    void insertCoin(Coin coin);
    void selectProduct(String code);
    DispenseResult dispense();
    int refund();
}
```

### VendingMachine constructor and delegation

```java
public VendingMachine() {
    this.currentState = new NoMoneyState(this);
    this.balanceCents = 0;
    this.selectedProduct = null;
}

public void insertCoin(Coin coin)      { currentState.insertCoin(coin); }
public void selectProduct(String code) { currentState.selectProduct(code); }
public DispenseResult dispense()       { return currentState.dispense(); }
public int refund()                    { return currentState.refund(); }

// Package-private mutators used by state objects:
void setState(VendingMachineState s)   { this.currentState = s; }
void addBalance(int cents)             { this.balanceCents += cents; }
void resetBalance()                    { this.balanceCents = 0; }
void setSelectedProduct(Product p)     { this.selectedProduct = p; }
Product findProduct(String code)       { return inventory.get(code); }
```

### NoMoneyState — the only state that accepts coins

```java
class NoMoneyState implements VendingMachineState {
    private final VendingMachine machine;
    NoMoneyState(VendingMachine machine) { this.machine = machine; }

    @Override
    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getCents());
        machine.setState(new HasMoneyState(machine));
    }

    @Override public void selectProduct(String code) {
        throw new IllegalStateException("Insert a coin first.");
    }
    @Override public DispenseResult dispense() {
        throw new IllegalStateException("No money inserted.");
    }
    @Override public int refund() {
        throw new IllegalStateException("Nothing to refund.");
    }
}
```

### HasMoneyState — selectProduct drives the branching

```java
class HasMoneyState implements VendingMachineState {
    private final VendingMachine machine;
    HasMoneyState(VendingMachine machine) { this.machine = machine; }

    @Override
    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getCents());   // stays in HasMoneyState
    }

    @Override
    public void selectProduct(String code) {
        Product p = machine.findProduct(code);
        if (p == null) throw new IllegalArgumentException("Unknown product: " + code);
        if (p.getStock() == 0) {
            machine.setSelectedProduct(p);
            machine.setState(new SoldOutState(machine));
            return;
        }
        if (machine.getBalanceCents() < p.getPriceCents()) {
            throw new IllegalStateException(
                "Insufficient balance. Need " + p.getPriceCents() +
                "c, have " + machine.getBalanceCents() + "c.");
        }
        machine.setSelectedProduct(p);
        machine.setState(new DispensingState(machine));
    }

    @Override public DispenseResult dispense() {
        throw new IllegalStateException("Select a product first.");
    }

    @Override
    public int refund() {
        int amount = machine.getBalanceCents();
        machine.resetBalance();
        machine.setState(new NoMoneyState(machine));
        return amount;
    }
}
```

### DispensingState — the crux: atomic deduct + decrement + change

```java
class DispensingState implements VendingMachineState {
    private final VendingMachine machine;
    DispensingState(VendingMachine machine) { this.machine = machine; }

    @Override
    public DispenseResult dispense() {
        Product p = machine.getSelectedProduct();
        int change = machine.getBalanceCents() - p.getPriceCents();
        p.setStock(p.getStock() - 1);           // decrement stock
        machine.resetBalance();                  // clear balance
        machine.setSelectedProduct(null);
        machine.setState(new NoMoneyState(machine));
        return new DispenseResult(p.getName(), change);
    }

    @Override public void insertCoin(Coin coin) {
        throw new IllegalStateException("Complete the current transaction first.");
    }
    @Override public void selectProduct(String code) {
        throw new IllegalStateException("Already in dispensing state.");
    }
    @Override public int refund() {
        // Allow cancel before physical dispense: return balance, clear selection.
        int amount = machine.getBalanceCents();
        machine.resetBalance();
        machine.setSelectedProduct(null);
        machine.setState(new NoMoneyState(machine));
        return amount;
    }
}
```

### SoldOutState — guard for zero-stock selection

```java
class SoldOutState implements VendingMachineState {
    private final VendingMachine machine;
    SoldOutState(VendingMachine machine) { this.machine = machine; }

    @Override public void insertCoin(Coin coin) {
        throw new IllegalStateException("Product sold out. Please refund and choose another.");
    }
    @Override public void selectProduct(String code) {
        throw new IllegalStateException("Current selection is sold out. Refund first.");
    }
    @Override public DispenseResult dispense() {
        throw new IllegalStateException("Cannot dispense: sold out.");
    }
    @Override public int refund() {
        int amount = machine.getBalanceCents();
        machine.resetBalance();
        machine.setSelectedProduct(null);
        machine.setState(new NoMoneyState(machine));
        return amount;
    }
}
```

---

## Walkthrough

Scenario from the kata spec:

```
VendingMachine vm = new VendingMachine();
vm.loadProduct(new Product("A1", "Cola", 75, 10));
vm.insertCoin(Coin.QUARTER);   // balance = 25
vm.insertCoin(Coin.QUARTER);   // balance = 50
vm.insertCoin(Coin.QUARTER);   // balance = 75
vm.selectProduct("A1");
DispenseResult result = vm.dispense();
// result.getProductName() == "Cola"
// result.getChangeCents() == 0
```

Step-by-step state transitions:

1. `new VendingMachine()` → state = `NoMoneyState`, balance = 0.
2. `insertCoin(QUARTER)` → `NoMoneyState.insertCoin`: `addBalance(25)`, `setState(HasMoneyState)`. Balance = 25.
3. `insertCoin(QUARTER)` → `HasMoneyState.insertCoin`: `addBalance(25)`. Balance = 50. Stays in `HasMoneyState`.
4. `insertCoin(QUARTER)` → balance = 75. Still `HasMoneyState`.
5. `selectProduct("A1")` → `HasMoneyState.selectProduct`: stock=10 > 0, balance 75 >= price 75. `setSelectedProduct(cola)`, `setState(DispensingState)`.
6. `dispense()` → `DispensingState.dispense`: change = 75 - 75 = 0. stock becomes 9. `resetBalance()`, `setSelectedProduct(null)`, `setState(NoMoneyState)`. Returns `DispenseResult("Cola", 0)`.

**Overpayment scenario** (3x QUARTER for a 50c item):

Same path through step 5, but `dispense()` computes change = 75 - 50 = 25. Returns `DispenseResult("Item", 25)`.

---

## Concurrency & edge cases

| Risk | Guard |
|---|---|
| Two threads call `insertCoin` simultaneously | `synchronized` on `VendingMachine` methods, or `ReentrantLock` wrapping each public method |
| Double-dispense race (two threads reach `dispense()` together) | `synchronized dispense()` on the context; the second caller sees `NoMoneyState` and throws `IllegalStateException` |
| Stock decrement to -1 | Guard inside `DispensingState.dispense`: re-check `p.getStock() > 0` under lock before decrementing |
| `selectProduct` with null or blank code | `Objects.requireNonNull(code)` at context entry; `findProduct` returns null → `IllegalArgumentException` |
| Price in floating point | All values are `int` cents — no `double` anywhere; avoids rounding errors |
| Maintenance mode / restocking | Add a `MaintenanceState` that rejects all user ops; a privileged `vm.enterMaintenance()` transitions to it; `vm.restock(code, qty)` only allowed from that state |

**Recommended locking strategy for multi-terminal use:**

```java
private final ReentrantLock lock = new ReentrantLock();

public void insertCoin(Coin coin) {
    lock.lock();
    try { currentState.insertCoin(coin); }
    finally { lock.unlock(); }
}
// ... same wrapper for selectProduct, dispense, refund
```

A single coarse lock is correct and simple here; fine-grained per-slot locking is over-engineering for a single-selection machine.

---

## Complexity & extensibility

**Big-O of key operations**

| Operation | Time | Space |
|---|---|---|
| `insertCoin` | O(1) | O(1) |
| `selectProduct` | O(1) — HashMap lookup | O(1) |
| `dispense` | O(1) | O(1) |
| `loadProduct` | O(1) amortised | O(n) total inventory |

**Adding a new denomination (bill acceptor)**

Introduce a `Currency` interface with `getCents()`; `Coin` and `Bill` both implement it. Change the state method signature to `insertCurrency(Currency c)`. Zero edits to existing state classes.

**Adding a new product type (e.g. age-restricted)**

Subclass `Product` as `RestrictedProduct` and override a `boolean isEligible(User u)` hook. `HasMoneyState.selectProduct` calls `product.isEligible(currentUser)` — no change to the state machine.

**Adding a loyalty discount**

Inject a `PricingStrategy` into the context. `HasMoneyState` asks `pricingStrategy.effectivePrice(product, balance)` instead of `product.getPriceCents()` directly.

**Open/Closed principle check:** every extension above adds a new class or injects a new collaborator; no existing state class is modified.

---

## Follow-ups

1. **Bill / note acceptance** — introduce a `Currency` interface; states already call `getCents()` so existing state code is untouched. Add `insertBill(Bill b)` to the interface or keep one `insertCurrency(Currency c)`.

2. **Thread-safety** — a single `ReentrantLock` per `VendingMachine` instance is sufficient. The critical section is the read-check-write sequence in `selectProduct` + `dispense`; both must execute atomically under the same lock to prevent double-dispense.

3. **Persistence / restore** — serialize `inventory` (stock levels) and `balanceCents` to JSON on every state transition (write-ahead style). On startup, deserialize and reconstruct the matching state object. Use `selectedProduct == null` to determine whether to restore `NoMoneyState` or `HasMoneyState`.

4. **Multiple concurrent users / networked terminals** — each terminal holds a session with its own balance and selected product; the shared resource is only the inventory stock count. Model this with per-session state objects and a shared `InventoryService` protected by `ConcurrentHashMap` + `AtomicInteger` per slot.

5. **Exact change only** — add a `ChangeStrategy` that checks whether the machine's coin float can make exact change; if not, it refuses the transaction or forces the user to insert exact coins.

6. **Audit log / receipt** — add an `EventPublisher` observer; each state transition fires an event (`CoinInserted`, `ProductDispensed`, `Refunded`). The publisher writes to a log without touching any state class.
