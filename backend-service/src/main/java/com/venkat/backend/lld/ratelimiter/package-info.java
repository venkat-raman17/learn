/**
 * <h2>LLD Practice Problem: Rate Limiter</h2>
 *
 * <hr>
 *
 * <h3>Problem Statement</h3>
 * <p>
 * Design and implement a configurable, per-client <strong>Rate Limiter</strong> that can be used
 * to control how frequently a client (identified by a {@code clientId} string) is allowed to
 * perform an action. The system must support multiple interchangeable rate-limiting algorithms,
 * be thread-safe for concurrent use, and expose a clean, minimal API.
 * </p>
 *
 * <hr>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li>The primary API is {@code boolean allow(String clientId)}: returns {@code true} if the
 *       request is permitted, {@code false} if it should be rejected.</li>
 *   <li>Support three algorithms, selectable at construction time:
 *       <ul>
 *         <li><strong>Token Bucket</strong> — tokens accumulate up to a capacity at a fixed refill
 *             rate. Each allowed request consumes one token.</li>
 *         <li><strong>Fixed Window Counter</strong> — counts requests in fixed, non-overlapping
 *             time windows of a configured duration. Resets the count at each new window.</li>
 *         <li><strong>Sliding Window Log</strong> — keeps a per-client log of request timestamps
 *             and allows a request only if fewer than the configured limit have occurred within the
 *             sliding window ending at "now".</li>
 *       </ul>
 *   </li>
 *   <li>Each algorithm must be independently configurable: capacity, window size, rate, limit, etc.</li>
 *   <li>State is per-client: each {@code clientId} is tracked independently.</li>
 *   <li>A factory ({@link com.venkat.backend.lld.ratelimiter.RateLimiterFactory}) must create the
 *       correct implementation based on a {@link com.venkat.backend.lld.ratelimiter.RateLimiterConfig}
 *       value object.</li>
 * </ol>
 *
 * <hr>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ol>
 *   <li><strong>Thread-safety</strong>: {@code allow()} must be safe to call from multiple threads
 *       concurrently for the same or different {@code clientId} values without data races or
 *       inconsistencies. Use {@code java.util.concurrent} primitives, not coarse
 *       {@code synchronized} on the whole object.</li>
 *   <li><strong>Extensibility</strong>: Adding a new algorithm should require only creating a new
 *       class that implements {@link com.venkat.backend.lld.ratelimiter.RateLimiter} — no changes
 *       to existing classes.</li>
 *   <li><strong>No external dependencies</strong>: use only {@code java.util} and
 *       {@code java.util.concurrent}.</li>
 *   <li><strong>Determinism in tests</strong>: consider how to inject a clock so unit tests can
 *       control time without sleeping.</li>
 * </ol>
 *
 * <hr>
 *
 * <h3>Suggested Entities / Types</h3>
 * <ul>
 *   <li>{@code RateLimiterAlgorithm} (enum) — {@code TOKEN_BUCKET}, {@code FIXED_WINDOW},
 *       {@code SLIDING_WINDOW_LOG}.</li>
 *   <li>{@code RateLimiterConfig} (value object / record) — holds algorithm choice, capacity,
 *       window duration, etc.</li>
 *   <li>{@code RateLimiter} (interface) — {@code boolean allow(String clientId)}.</li>
 *   <li>{@code TokenBucketRateLimiter} — implements {@code RateLimiter}.</li>
 *   <li>{@code FixedWindowRateLimiter} — implements {@code RateLimiter}.</li>
 *   <li>{@code SlidingWindowLogRateLimiter} — implements {@code RateLimiter}.</li>
 *   <li>{@code RateLimiterFactory} — static/instance factory, creates the right implementation
 *       from a {@code RateLimiterConfig}.</li>
 * </ul>
 *
 * <hr>
 *
 * <h3>Public API (minimum surface)</h3>
 * <pre>{@code
 * // Create a token-bucket limiter: 5 tokens capacity, refills 1 token/second
 * RateLimiterConfig config = new RateLimiterConfig(
 *         RateLimiterAlgorithm.TOKEN_BUCKET, 5, Duration.ofSeconds(1));
 * RateLimiter limiter = RateLimiterFactory.create(config);
 *
 * boolean ok = limiter.allow("user-42");   // true if token available
 *
 * // Create a fixed-window limiter: 10 requests per 1-minute window
 * RateLimiterConfig fw = new RateLimiterConfig(
 *         RateLimiterAlgorithm.FIXED_WINDOW, 10, Duration.ofMinutes(1));
 * RateLimiter fwLimiter = RateLimiterFactory.create(fw);
 *
 * // Create a sliding-window-log limiter: 10 requests in any 1-minute window
 * RateLimiterConfig sl = new RateLimiterConfig(
 *         RateLimiterAlgorithm.SLIDING_WINDOW_LOG, 10, Duration.ofMinutes(1));
 * RateLimiter slLimiter = RateLimiterFactory.create(sl);
 * }</pre>
 *
 * <hr>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><strong>Strategy</strong> — each algorithm is a strategy implementing the same
 *       {@code RateLimiter} interface. The factory selects the strategy based on config.</li>
 *   <li><strong>Factory Method / Static Factory</strong> — {@code RateLimiterFactory.create(config)}
 *       encapsulates construction details.</li>
 *   <li><strong>Template Method</strong> (optional) — if multiple algorithms share scaffolding
 *       (e.g., per-client state lookup), an abstract base class can hold the shared logic.</li>
 * </ul>
 *
 * <hr>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>How would you modify the design to support a <em>distributed</em> rate limiter backed by
 *       Redis, while keeping the same {@code RateLimiter} interface and factory?</li>
 *   <li>Token Bucket and Sliding Window Log have different fairness and burst characteristics.
 *       When would you choose one over the other in a real API gateway?</li>
 *   <li>How could you expose real-time metrics (current token count, rejection rate) without
 *       exposing internal implementation details through the public interface?</li>
 * </ol>
 *
 * <hr>
 *
 * <h3>Extensibility and Concurrency Prompt</h3>
 * <p>
 * Your implementation will be reviewed against two axes:
 * </p>
 * <ul>
 *   <li><strong>Extensibility</strong>: Can you add a fourth algorithm (e.g., Leaky Bucket) by
 *       creating a single new class without modifying existing ones? Walk through the steps.</li>
 *   <li><strong>Concurrency correctness</strong>: Demonstrate with a test using
 *       {@code java.util.concurrent.ExecutorService} and 20 threads each firing 100 requests
 *       against the same {@code clientId}. The total number of allowed requests must not exceed
 *       the configured limit, and must be provably correct — no lost updates.</li>
 * </ul>
 *
 * @author  learner
 * @version 1.0
 */
package com.venkat.backend.lld.ratelimiter;
