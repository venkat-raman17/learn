# In-Memory Cache (LRU/LFU) — reference design

---

## Approach

### Entity model

The design centres on three collaborating roles:

- **Cache** — owns the primary key-value store (`HashMap<K,V>`), stat counters, and the concurrency lock. It is the only class the caller ever touches.
- **EvictionPolicy** — owns all ordering/frequency bookkeeping. It knows *which* key to evict but has no access to values.
- **CacheStats** / **PolicyType** — pure data; no behaviour.

The cache calls policy callbacks (`recordInsert`, `recordAccess`, `remove`, `evict`) at the right moments; the policy never calls back into the cache.

### Design patterns

| Pattern | Where applied | Why |
|---|---|---|
| **Strategy** | `EvictionPolicy<K>` interface | Swap LRU for LFU (or future FIFO) by passing a different policy object; `LruCache`/`LfuCache` are thin shells, or a single `GenericCache` can hold any policy. |
| **Factory** | `CacheFactory.create(PolicyType, int)` | Hides which concrete cache + policy pair is wired; callers depend only on `Cache<K,V>`. |
| **Template Method** (optional) | `AbstractCache<K,V>` | Centralises stat bookkeeping and locking; subclasses implement only the storage operations. |

---

## Class design

| Class / Interface | Responsibility |
|---|---|
| `Cache<K,V>` | Public contract: `get`, `put`, `remove`, `evict`, `stats`, `clear`, `capacity` |
| `EvictionPolicy<K>` | Strategy contract: `recordAccess`, `recordInsert`, `remove`, `evict` |
| `PolicyType` | Enum marker: `LRU`, `LFU` |
| `CacheStats` | Immutable record: `hits`, `misses`, `evictions`, `size` |
| `CacheFactory` | Static factory; wires concrete cache + policy pair |
| `LruEvictionPolicy<K>` | Doubly-linked list + `HashMap<K, Node<K>>`; MRU at tail, LRU at head |
| `LfuEvictionPolicy<K>` | `keyFreq: HashMap<K,Integer>` + `freqBucket: HashMap<Integer, LinkedHashSet<K>>` + `minFreq` int |
| `LruCache<K,V>` | `HashMap<K,V>` data store + `LruEvictionPolicy` + `ReentrantLock` + stat counters |
| `LfuCache<K,V>` | Same shape as `LruCache` but wired to `LfuEvictionPolicy` |

A single `GenericCache<K,V>` class that accepts any `EvictionPolicy<K>` is an equally valid (and more open/closed) alternative to two separate concrete cache classes.

---

## Key code

### LRU eviction policy — doubly-linked list + map

```java
class LruEvictionPolicy<K> implements EvictionPolicy<K> {

    private static class Node<K> {
        K key;
        Node<K> prev, next;
        Node(K key) { this.key = key; }
    }

    // sentinel nodes; actual entries live between head.next and tail.prev
    private final Node<K> head = new Node<>(null);
    private final Node<K> tail = new Node<>(null);
    private final Map<K, Node<K>> map = new HashMap<>();

    LruEvictionPolicy() { head.next = tail; tail.prev = head; }

    private void detach(Node<K> n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }
    private void insertBeforeTail(Node<K> n) {
        n.prev = tail.prev; n.next = tail;
        tail.prev.next = n; tail.prev = n;
    }

    @Override public void recordInsert(K key) {
        Node<K> n = new Node<>(key);
        map.put(key, n);
        insertBeforeTail(n);          // newest = most-recently-used
    }
    @Override public void recordAccess(K key) {
        Node<K> n = map.get(key);
        if (n == null) return;
        detach(n);
        insertBeforeTail(n);          // move to MRU end
    }
    @Override public void remove(K key) {
        Node<K> n = map.remove(key);
        if (n != null) detach(n);
    }
    @Override public Optional<K> evict() {
        if (head.next == tail) return Optional.empty();
        Node<K> lru = head.next;      // LRU candidate is at head
        detach(lru);
        map.remove(lru.key);
        return Optional.of(lru.key);
    }
}
```

### LFU eviction policy — frequency buckets

```java
class LfuEvictionPolicy<K> implements EvictionPolicy<K> {

    private final Map<K, Integer> keyFreq = new HashMap<>();
    // LinkedHashSet preserves insertion order -> LRU within same frequency
    private final Map<Integer, LinkedHashSet<K>> freqBucket = new HashMap<>();
    private int minFreq = 0;

    private void increment(K key) {
        int f = keyFreq.getOrDefault(key, 0);
        keyFreq.put(key, f + 1);
        freqBucket.computeIfAbsent(f, x -> new LinkedHashSet<>()).remove(key);
        if (freqBucket.getOrDefault(f, Set.of()).isEmpty() && f == minFreq) minFreq++;
        freqBucket.computeIfAbsent(f + 1, x -> new LinkedHashSet<>()).add(key);
    }

    @Override public void recordInsert(K key) {
        keyFreq.put(key, 1);
        freqBucket.computeIfAbsent(1, x -> new LinkedHashSet<>()).add(key);
        minFreq = 1;                  // new entry always has freq=1
    }
    @Override public void recordAccess(K key) {
        if (!keyFreq.containsKey(key)) return;
        increment(key);
    }
    @Override public void remove(K key) {
        Integer f = keyFreq.remove(key);
        if (f != null) freqBucket.getOrDefault(f, Set.of()).remove(key);
    }
    @Override public Optional<K> evict() {
        LinkedHashSet<K> bucket = freqBucket.get(minFreq);
        if (bucket == null || bucket.isEmpty()) return Optional.empty();
        K victim = bucket.iterator().next();   // oldest at minFreq
        bucket.remove(victim);
        keyFreq.remove(victim);
        return Optional.of(victim);
    }
}
```

### Generic cache (thread-safe)

```java
class GenericCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, V> store = new HashMap<>();
    private final EvictionPolicy<K> policy;
    private final ReentrantLock lock = new ReentrantLock();
    private long hits, misses, evictions;

    GenericCache(int capacity, EvictionPolicy<K> policy) {
        if (capacity < 1) throw new IllegalArgumentException("capacity >= 1 required");
        this.capacity = capacity;
        this.policy = policy;
    }

    @Override public Optional<V> get(K key) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            if (!store.containsKey(key)) { misses++; return Optional.empty(); }
            hits++;
            policy.recordAccess(key);
            return Optional.ofNullable(store.get(key));
        } finally { lock.unlock(); }
    }

    @Override public void put(K key, V value) {
        Objects.requireNonNull(key);
        lock.lock();
        try {
            if (store.containsKey(key)) {
                store.put(key, value);
                policy.recordAccess(key);   // update = treat as access
            } else {
                if (store.size() >= capacity) {
                    policy.evict().ifPresent(k -> { store.remove(k); evictions++; });
                }
                store.put(key, value);
                policy.recordInsert(key);
            }
        } finally { lock.unlock(); }
    }

    @Override public void remove(K key) {
        Objects.requireNonNull(key);
        lock.lock();
        try { if (store.remove(key) != null) policy.remove(key); }
        finally { lock.unlock(); }
    }

    @Override public Optional<K> evict() {
        lock.lock();
        try {
            return policy.evict().map(k -> { store.remove(k); evictions++; return k; });
        } finally { lock.unlock(); }
    }

    @Override public CacheStats stats() {
        lock.lock();
        try { return new CacheStats(hits, misses, evictions, store.size()); }
        finally { lock.unlock(); }
    }

    @Override public void clear() {
        lock.lock();
        try {
            store.keySet().forEach(policy::remove);
            store.clear();
            hits = misses = evictions = 0;
        } finally { lock.unlock(); }
    }

    @Override public int capacity() { return capacity; }
}
```

### Factory

```java
final class CacheFactory {
    private CacheFactory() {}

    public static <K, V> Cache<K, V> create(PolicyType type, int capacity) {
        Objects.requireNonNull(type);
        EvictionPolicy<K> policy = switch (type) {
            case LRU -> new LruEvictionPolicy<>();
            case LFU -> new LfuEvictionPolicy<>();
        };
        return new GenericCache<>(capacity, policy);
    }
}
```

---

## Walkthrough

Scenario: `Cache<String,Integer> c = CacheFactory.create(LRU, 2)`

```
put("a", 1)   store={a=1}          policy list: [a]   (a at MRU)
put("b", 2)   store={a=1, b=2}     policy list: [a, b]
get("a")      hit -> recordAccess  policy list: [b, a] (a promoted to MRU)
put("c", 3)   at capacity -> evict: b (LRU head)
              store={a=1, c=3}     policy list: [a, c]
stats()       -> CacheStats(hits=1, misses=0, evictions=1, size=2)
```

For LFU with the same sequence, `get("a")` raises a's frequency to 2, so when `put("c")` triggers eviction it removes `b` (freq=1), same result — but if we had done `get("b")` too, the tie would break by insertion order (b before a at freq=2), so a would be evicted instead.

---

## Concurrency & edge cases

| Concern | Guard |
|---|---|
| Concurrent `put` at full capacity (two threads both see `size == capacity`) | Single `ReentrantLock` around the whole put body prevents double-eviction |
| `get` calls `recordAccess` which mutates policy state — cannot be read-only | Requires write lock even for reads; `ReadWriteLock` does NOT help here without a separate `recordAccess` queue |
| `null` key | `Objects.requireNonNull(key)` at every public entry point |
| `null` value (valid cached result) | `store.containsKey(key)` check before `store.get(key)` — never confuse `null` value with a miss |
| `clear()` must notify policy for every key | Iterate `store.keySet()` and call `policy.remove(k)` to keep policy state consistent |
| `evict()` on empty cache | Policy returns `Optional.empty()`; cache must handle gracefully (no NPE, no stat increment) |
| Capacity = 1 | Works naturally: every `put` of a new key evicts the single existing entry |

---

## Complexity & extensibility

### Big-O

| Operation | LRU | LFU |
|---|---|---|
| `get` | O(1) | O(1) |
| `put` (no eviction) | O(1) | O(1) |
| `put` (with eviction) | O(1) | O(1) |
| `remove` | O(1) | O(1) |
| `clear` | O(n) | O(n) |

Space: O(capacity) for both policies.

### Adding a new policy (e.g. FIFO, Random)

1. Add `FIFO` to the `PolicyType` enum.
2. Implement `FifoEvictionPolicy<K> implements EvictionPolicy<K>` using a `LinkedHashMap` or `Queue`.
3. Add a `case FIFO -> new FifoEvictionPolicy<>()` branch in `CacheFactory`.

No existing class is modified. `GenericCache`, `Cache`, and `CacheStats` are untouched — this is the open/closed principle delivered by the Strategy + Factory combination.

---

## Follow-ups

1. **LFU tie-breaking by recency** — `LinkedHashSet` as the bucket type preserves insertion order at O(1) add/remove, so the oldest key at `minFreq` is naturally `bucket.iterator().next()`. No second data structure needed.

2. **TTL per entry** — wrap values in an `Entry<V>` record that carries an `expiresAt` (Instant). The `get` path checks `Instant.now().isAfter(expiresAt)` and treats stale entries as misses (and evicts them). Expiry logic lives in `GenericCache.get` — the `EvictionPolicy` interface stays unchanged. A background `ScheduledExecutorService` can sweep expired entries proactively.

3. **Minimal locking for concurrent puts** — a `ReentrantLock` per-shard (striped lock) reduces contention. However, since `recordAccess` is a write to policy state inside `get`, a simple `ReadWriteLock` does not help without additional complexity (e.g. buffering accesses and draining on writes, as Caffeine does). For an interview answer: start with a single `ReentrantLock`; mention striped locking or the `W-TinyLFU` / Caffeine buffer-drain approach as a follow-up optimisation.

4. **Distributed cache extension** — promote `Cache<K,V>` to a remote-capable interface; implement a `RedisCache` that maps `get`/`put` to Redis commands. The Strategy pattern for eviction becomes irrelevant (Redis handles it server-side), but the `Cache` interface and `CacheFactory` remain unchanged from the caller's perspective.
