/**
 * <h2>LLD Practice: In-Memory Cache with Pluggable Eviction (LRU / LFU)</h2>
 *
 * <h3>Problem Statement</h3>
 * Design a generic, thread-safe, in-memory cache with a fixed capacity that evicts
 * entries when full according to a configurable eviction policy. The cache must
 * expose a clean public API decoupled from any specific eviction algorithm, so that
 * LRU, LFU (or a future FIFO / Random policy) can be swapped without touching the
 * cache consumer. O(1) amortised time for {@code get} and {@code put} is the target
 * for both LRU and LFU implementations.
 *
 * <h3>Distinct from the DSA LRU exercise</h3>
 * The {@code dsa-java} module contains a raw LRU data-structure problem.  This LLD
 * problem is about <em>design</em>:
 * <ul>
 *   <li>Separating the <em>cache contract</em> ({@code Cache<K,V>} interface) from
 *       the <em>eviction strategy</em> ({@code EvictionPolicy}) using the
 *       <strong>Strategy pattern</strong>.</li>
 *   <li>Adding a {@code CacheStats} value type so callers can observe hits/misses
 *       without coupling to internals.</li>
 *   <li>Providing a {@code CacheFactory} that assembles the correct implementation
 *       based on a {@code PolicyType} enum — an application of the
 *       <strong>Factory / Factory-Method pattern</strong>.</li>
 *   <li>Thinking about concurrency (see the concurrency prompt below).</li>
 * </ul>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li><strong>put(key, value)</strong> — insert or update an entry.  If at
 *       capacity, evict one entry according to the active policy before inserting.</li>
 *   <li><strong>get(key)</strong> — return the value, or {@code Optional.empty()} on
 *       a miss.  A successful get must update the entry's usage metadata so the
 *       eviction policy has accurate information.</li>
 *   <li><strong>evict()</strong> — remove exactly one entry chosen by the policy;
 *       return the evicted key wrapped in {@code Optional} (empty if cache is already
 *       empty).</li>
 *   <li><strong>remove(key)</strong> — explicitly remove an entry; no-op if absent.</li>
 *   <li><strong>stats()</strong> — return a snapshot {@code CacheStats} with
 *       hit-count, miss-count, eviction-count, and current size.</li>
 *   <li><strong>clear()</strong> — remove all entries and reset stats.</li>
 *   <li>Capacity is fixed at construction time and must be &ge; 1.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ul>
 *   <li><strong>Time complexity</strong>: {@code get} and {@code put} should run in
 *       O(1) amortised time for both LRU and LFU.  Hint: LRU typically uses a
 *       doubly-linked list + HashMap; LFU typically uses a frequency bucket
 *       structure.</li>
 *   <li><strong>Thread safety</strong>: the public API must be safe for concurrent
 *       use from multiple threads (see extensibility prompt).</li>
 *   <li><strong>No external dependencies</strong>: only {@code java.util.*} and
 *       {@code java.util.concurrent.*}.</li>
 *   <li><strong>Generics</strong>: fully generic {@code Cache<K,V>} — keys must be
 *       non-null; values may be null (treat null value as a valid cached result).</li>
 * </ul>
 *
 * <h3>Suggested Entities &amp; Responsibilities</h3>
 * <pre>
 *  Cache&lt;K,V&gt;            — public interface (get, put, remove, evict, stats, clear)
 *  EvictionPolicy&lt;K&gt;     — Strategy interface (recordAccess, recordInsert, evict)
 *  PolicyType            — enum: LRU, LFU  (used by CacheFactory)
 *  CacheFactory          — static factory: create(PolicyType, capacity) -&gt; Cache&lt;K,V&gt;
 *  CacheStats            — immutable value record: hits, misses, evictions, size
 *  LruCache&lt;K,V&gt;         — implements Cache; delegates eviction to an LRU policy
 *  LfuCache&lt;K,V&gt;         — implements Cache; delegates eviction to an LFU policy
 *  LruEvictionPolicy&lt;K&gt;  — implements EvictionPolicy using doubly-linked list + map
 *  LfuEvictionPolicy&lt;K&gt;  — implements EvictionPolicy using frequency buckets
 * </pre>
 *
 * <h3>Public API (defined in Cache.java)</h3>
 * <pre>
 *  interface Cache&lt;K,V&gt; {
 *      Optional&lt;V&gt;   get(K key);
 *      void          put(K key, V value);
 *      void          remove(K key);
 *      Optional&lt;K&gt;  evict();
 *      CacheStats    stats();
 *      void          clear();
 *      int           capacity();
 *  }
 *
 *  interface EvictionPolicy&lt;K&gt; {
 *      void       recordAccess(K key);   // called on cache hit
 *      void       recordInsert(K key);   // called on new insertion
 *      void       remove(K key);         // called on explicit removal
 *      Optional&lt;K&gt; evict();             // choose and remove one key to evict
 *  }
 * </pre>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><strong>Strategy</strong> — {@code EvictionPolicy} is the strategy; the
 *       cache implementation is the context.  Swap policies without changing the
 *       cache.</li>
 *   <li><strong>Factory / Factory-Method</strong> — {@code CacheFactory.create()}
 *       hides which concrete cache + policy pair is assembled.</li>
 *   <li><strong>Template Method</strong> (optional) — a {@code AbstractCache} base
 *       class could handle stats bookkeeping while subclasses implement the
 *       eviction-aware data store operations.</li>
 * </ul>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>LFU breaks ties (equal frequency) by recency — how would you track this
 *       without a second data structure costing O(n) time?</li>
 *   <li>How would you add a TTL (time-to-live) per entry without changing the
 *       {@code Cache} interface?  Which class should own expiry logic?</li>
 *   <li>If multiple threads call {@code put} concurrently at full capacity, what
 *       is the minimal locking granularity to avoid both data corruption and a
 *       liveness deadlock?</li>
 * </ol>
 *
 * <h3>Extensibility &amp; Concurrency Prompt</h3>
 * <p>Your initial implementation may use coarse-grained synchronisation
 * ({@code synchronized} methods or a single {@code ReentrantReadWriteLock}).
 * Once working, consider: can reads ({@code get}) proceed concurrently with each
 * other while only writes ({@code put}, {@code evict}) require exclusive access?
 * How does this interact with the obligation to call {@code recordAccess} (a
 * write to the policy's internal structure) inside {@code get}?</p>
 *
 * @see com.venkat.backend.lld.cache.Cache
 * @see com.venkat.backend.lld.cache.EvictionPolicy
 * @see com.venkat.backend.lld.cache.CacheFactory
 */
package com.venkat.backend.lld.cache;
