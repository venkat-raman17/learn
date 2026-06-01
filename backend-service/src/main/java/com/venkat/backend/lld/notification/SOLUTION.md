# Notification Service — reference design

## Approach

### Entity model

| Entity | Role |
|---|---|
| `ChannelType` | Enum key; doubles as registry key and subscription set element |
| `Notification` | Immutable value object (Java record); body may be re-rendered before dispatch |
| `User` | Immutable entity (Java record); carries channel-specific contact fields |
| `RetryPolicy` | Immutable value object; consumed by `RetryExecutor` |
| `DeliveryResult` | Sealed interface with two permitted records: `Success` and `Failure` |

Subscriptions are **mutable state** on the service, not on `User` — because `User` is a record (immutable). The service holds a `Map<String, Set<ChannelType>>` keyed by `userId`.

### Design patterns chosen

| Pattern | Where | Why |
|---|---|---|
| **Strategy** | `NotificationChannel` | Swap/add delivery channels at runtime without touching `NotificationService` |
| **Observer** | `DeliveryObserver` | Decouple result consumers (logging, metrics) from the send path |
| **Decorator** | `RetryExecutor` wraps any `NotificationChannel` | Keeps retry logic out of every channel implementation |
| **Registry** | `Map<ChannelType, NotificationChannel>` | O(1) lookup; new channels register themselves at startup |
| **Functional Interface / Template Method** | `NotificationTemplate` | Lambda-friendly hook; personalisation without subclassing `Notification` |
| **Static factory** | `NotificationService.create()` | Hides constructor; signals "no mandatory arguments needed to build" |

---

## Class design

```
NotificationService                  — orchestrator; holds all registries
  Map<ChannelType, NotificationChannel>   channels   (ConcurrentHashMap)
  Map<ChannelType, RetryPolicy>           retryPolicies (ConcurrentHashMap)
  Map<String,      NotificationTemplate>  templates  (ConcurrentHashMap)
  Map<String,      User>                  users      (ConcurrentHashMap)
  Map<String,      Set<ChannelType>>      subscriptions (ConcurrentHashMap +
                                                         CopyOnWriteArraySet)
  List<DeliveryObserver>                  observers  (CopyOnWriteArrayList)

RetryExecutor                        — stateless helper; wraps one channel call
  DeliveryResult execute(NotificationChannel, User, Notification, RetryPolicy)

NotificationChannel (interface)      — Strategy; one impl per ChannelType
  EmailChannel / SmsChannel / PushChannel

NotificationTemplate (interface)     — @FunctionalInterface render hook

DeliveryObserver (interface)         — @FunctionalInterface result callback

DeliveryResult (sealed interface)
  Success(channelType, timestampEpochMs)
  Failure(channelType, reason, attempts)
```

`RetryExecutor` is package-private. Channel implementations are thin; they validate the contact field (`email`, `phone`, `deviceToken`) and either return `Failure` (permanent — e.g. null address) or throw `RuntimeException` (transient — triggers retry).

---

## Key code

### RetryExecutor — the retry loop

```java
class RetryExecutor {

    static DeliveryResult execute(
            NotificationChannel channel,
            User user,
            Notification notification,
            RetryPolicy policy) {

        int maxAttempts = (policy != null) ? policy.maxAttempts() : 1;
        long delay      = (policy != null) ? policy.delayMillis() : 0;
        RuntimeException lastEx = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                DeliveryResult result = channel.send(user, notification);
                // Channel signalled a permanent failure — do not retry
                if (result instanceof DeliveryResult.Failure) return result;
                return result;                         // Success
            } catch (RuntimeException ex) {
                lastEx = ex;
                if (attempt < maxAttempts && delay > 0) {
                    try { Thread.sleep(delay); } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        return new DeliveryResult.Failure(
                channel.type(), lastEx != null ? lastEx.getMessage() : "unknown", maxAttempts);
    }
}
```

### NotificationService.send — dispatch core

```java
public List<DeliveryResult> send(Notification notification) {
    Objects.requireNonNull(notification, "notification must not be null");

    User user = users.get(notification.recipientUserId());
    if (user == null) {
        return List.of(new DeliveryResult.Failure(
                notification.channelType(), "user not found", 0));
    }

    // Template rendering — replace body once, shared across channels
    String renderedBody = notification.body();
    NotificationTemplate tmpl = templates.get(notification.type());
    if (tmpl != null) {
        renderedBody = tmpl.render(notification, user);
    }
    Notification rendered = new Notification(
            notification.recipientUserId(), notification.type(),
            notification.subject(), renderedBody, notification.channelType());

    // Fan-out to subscribed + registered channels
    Set<ChannelType> subscribed = subscriptions.getOrDefault(
            user.userId(), Collections.emptySet());

    List<DeliveryResult> results = new ArrayList<>();
    for (ChannelType ct : subscribed) {
        NotificationChannel ch = channels.get(ct);
        if (ch == null) continue;                      // no impl registered; skip
        RetryPolicy policy = retryPolicies.get(ct);
        DeliveryResult result = RetryExecutor.execute(ch, user, rendered, policy);
        notifyObservers(result);
        results.add(result);
    }
    return results.isEmpty()
           ? List.of(new DeliveryResult.Failure(notification.channelType(),
                     "no active subscription or channel registered", 0))
           : results;
}

private void notifyObservers(DeliveryResult result) {
    for (DeliveryObserver obs : observers) {
        try { obs.onDelivery(result); }
        catch (Exception ignored) {}   // isolate misbehaving observers
    }
}
```

### Subscription management — thread-safe

```java
public void subscribe(String userId, ChannelType channelType) {
    if (!users.containsKey(userId))
        throw new IllegalArgumentException("unknown user: " + userId);
    subscriptions
        .computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
        .add(channelType);
}
```

---

## Walkthrough

Scenario from the kata spec: register service, send a WELCOME email.

```
1. NotificationService svc = NotificationService.create();
      → new NotificationService(); all maps/lists empty

2. svc.registerChannel(new EmailChannel());
      → channels.put(EMAIL, emailChannel)

3. svc.setRetryPolicy(EMAIL, new RetryPolicy(3, 200));
      → retryPolicies.put(EMAIL, policy)

4. svc.registerTemplate("WELCOME", (n, u) -> "Hi " + u.name() + ", " + n.body());
      → templates.put("WELCOME", lambda)

5. svc.addObserver(result -> System.out.println(result));
      → observers.add(lambda)

6. svc.registerUser(alice);   svc.subscribe("alice", EMAIL);
      → users.put("alice", alice)
      → subscriptions.put("alice", {EMAIL})

7. svc.send(new Notification("alice", "WELCOME", "Welcome!", "Click here.", EMAIL));
   a. Resolve alice from users map        → User found
   b. Look up template "WELCOME"          → renders "Hi Alice, Click here."
   c. subscribed = {EMAIL}; channel = EmailChannel
   d. RetryExecutor.execute(emailChannel, alice, rendered, RetryPolicy(3,200))
        attempt 1 → emailChannel.send(alice, rendered)
          → validates alice.email() != null
          → (simulates SMTP call) → returns Success(EMAIL, now)
   e. notifyObservers(Success) → println called
   f. results = [Success(EMAIL, <ts>)]
   g. return results
```

---

## Concurrency & edge cases

| Concern | Guard |
|---|---|
| Concurrent subscribe + send | `ConcurrentHashMap` for `subscriptions`; `computeIfAbsent` is atomic. The read in `send` takes a snapshot view — a subscribe racing with send may or may not be seen; both outcomes are valid (eventual consistency). |
| Concurrent channel / template registration | `ConcurrentHashMap.put` is atomic; last writer wins, which matches the Javadoc spec ("replaces any previously registered"). |
| Observer throwing | Wrap each `obs.onDelivery()` call in try/catch; log and continue so one bad observer cannot suppress others. |
| Missing contact field | Channel implementation checks (e.g. `user.email() == null`) and returns `Failure` immediately — `RetryExecutor` sees a `Failure` result (not an exception) and does not retry. |
| Null notification or required fields | `send` calls `Objects.requireNonNull`; subscribe/unsubscribe throw `IllegalArgumentException` for unknown userId. |
| InterruptedException in sleep | Restore interrupt flag (`Thread.currentThread().interrupt()`) and break the retry loop. |
| Empty subscription set | `send` returns a single `Failure("no active subscription or channel registered")`. |

---

## Complexity & extensibility

| Operation | Time complexity | Notes |
|---|---|---|
| `registerChannel` / `setRetryPolicy` | O(1) | ConcurrentHashMap put |
| `subscribe` / `unsubscribe` | O(1) amortised | ConcurrentHashSet add/remove |
| `send` — channel fan-out | O(S) where S = subscribed channels (≤ number of ChannelType values) | Bounded by enum cardinality |
| `notifyObservers` | O(N) where N = registered observers | Linear scan of CopyOnWriteArrayList |
| Retry loop | O(maxAttempts) per channel | Bounded by policy |

**Adding a new channel (e.g. SLACK) — zero modifications to existing code:**

```java
// 1. Add SLACK to ChannelType enum  (only this enum edit needed)
// 2. Implement NotificationChannel:
class SlackChannel implements NotificationChannel {
    public ChannelType type() { return ChannelType.SLACK; }
    public DeliveryResult send(User user, Notification n) { /* call Slack API */ }
}
// 3. Register at startup:
svc.registerChannel(new SlackChannel());
```

`NotificationService`, `RetryExecutor`, and all other channel classes are untouched — OCP satisfied.

---

## Follow-ups

1. **Missing contact field (FR-1 / follow-up 1)** — return `Failure` with reason `"no email address"` rather than skipping silently; this surfaces the misconfiguration to the caller and to observers. Do not throw — a missing address is a data problem, not a programming error.

2. **Concurrent subscribe + send consistency (follow-up 2)** — the current design gives "at-least-once-attempted" semantics within a single send: the subscription set snapshot is taken before the fan-out loop. If subscribe races with send, the result depends on scheduling; both are safe. For strict "subscribe-then-send always sees the subscription" guarantee, wrap the snapshot + loop in a read lock.

3. **Exponential backoff with jitter (follow-up 3)** — introduce a `BackoffStrategy` interface:
   ```java
   @FunctionalInterface
   interface BackoffStrategy {
       long nextDelayMillis(int attempt, long baseDelayMillis);
   }
   // Exponential + jitter:
   BackoffStrategy expJitter = (attempt, base) ->
       (long)(base * Math.pow(2, attempt - 1)) + ThreadLocalRandom.current().nextLong(base);
   ```
   `RetryPolicy` gains an optional `BackoffStrategy` field; `RetryExecutor` calls it. `RetryPolicy` stays a value object — no logic inside it.

4. **Async fan-out (stretch goal)** — inject an `ExecutorService`; use `invokeAll` with `Callable<DeliveryResult>` per channel; collect `Future` results; always invoke observers in the `finally` of each callable so one channel exception does not suppress others.

5. **Dead-letter queue** — persist `Failure` records to a queue (Kafka, SQS) for offline reprocessing, wired in as a `DeliveryObserver`.

6. **Rate limiting per channel** — wrap each `NotificationChannel` in a `RateLimitedChannel` decorator that uses a `Semaphore` or token bucket before calling `send`.
