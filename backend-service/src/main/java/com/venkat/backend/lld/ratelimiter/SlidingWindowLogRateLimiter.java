package com.venkat.backend.lld.ratelimiter;

/**
 * Sliding Window Log rate limiter.
 *
 * <h3>Algorithm sketch</h3>
 * <ul>
 *   <li>For each client, maintain a <em>log</em> (e.g., a deque) of timestamps of allowed
 *       requests.</li>
 *   <li>When a new request arrives at time {@code now}:
 *       <ol>
 *         <li>Remove all timestamps older than {@code now - window} from the front of the log.</li>
 *         <li>If the log size is less than {@code capacity}, append {@code now} and return
 *             {@code true}.</li>
 *         <li>Otherwise return {@code false} (do not append).</li>
 *       </ol>
 *   </li>
 * </ul>
 *
 * <h3>Memory consideration</h3>
 * <p>In theory the log holds at most {@code capacity} entries per client, but this must be
 * enforced.  What happens if a client sends exactly {@code capacity} requests and then goes
 * silent — how long do those log entries live in memory?  What cleanup strategy would you add?</p>
 *
 * <h3>Concurrency note</h3>
 * <p>Eviction and size-check-plus-append must be atomic per client.  A per-client
 * {@code ReentrantLock} or {@code synchronized} block is appropriate here.</p>
 */
public class SlidingWindowLogRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;

    /**
     * Creates a SlidingWindowLogRateLimiter with the given configuration.
     *
     * @param config rate limiter configuration; must use
     *               {@link RateLimiterAlgorithm#SLIDING_WINDOW_LOG}
     */
    public SlidingWindowLogRateLimiter(RateLimiterConfig config) {
        this.config = config;
        // TODO: initialise per-client log storage (e.g., ConcurrentHashMap<String, Deque<Long>>)
    }

    /**
     * {@inheritDoc}
     *
     * <p>Evicts stale timestamps, then checks whether the client is within the rate limit.
     */
    @Override
    public boolean allow(String clientId) {
        throw new UnsupportedOperationException("implement me");
    }
}
