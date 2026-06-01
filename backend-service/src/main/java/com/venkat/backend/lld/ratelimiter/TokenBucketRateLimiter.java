package com.venkat.backend.lld.ratelimiter;

/**
 * Token Bucket rate limiter.
 *
 * <h3>Algorithm sketch</h3>
 * <ul>
 *   <li>Each client starts with a full bucket of {@code capacity} tokens.</li>
 *   <li>One token is added every {@code window} duration (i.e., the refill rate is
 *       1 token / window), up to the configured capacity.</li>
 *   <li>A request is allowed only if at least one token is available; if so, one token
 *       is consumed atomically.</li>
 *   <li>If no token is available the request is rejected.</li>
 * </ul>
 *
 * <h3>Concurrency note</h3>
 * <p>Per-client state must be protected against races. Consider
 * {@code ConcurrentHashMap#computeIfAbsent} to lazily initialise per-client state,
 * and per-client locks (or atomic variables) to guard token consumption.</p>
 *
 * <h3>Clock injection</h3>
 * <p>To make unit tests deterministic, accept a {@code java.time.Clock} in the constructor
 * instead of calling {@code System.currentTimeMillis()} directly.</p>
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;

    /**
     * Creates a TokenBucketRateLimiter with the given configuration.
     *
     * @param config rate limiter configuration; must use
     *               {@link RateLimiterAlgorithm#TOKEN_BUCKET}
     */
    public TokenBucketRateLimiter(RateLimiterConfig config) {
        this.config = config;
        // TODO: initialise per-client state storage (e.g., ConcurrentHashMap)
    }

    /**
     * {@inheritDoc}
     *
     * <p>Refills tokens for the client based on elapsed time, then attempts to consume one.
     */
    @Override
    public boolean allow(String clientId) {
        throw new UnsupportedOperationException("implement me");
    }
}
