package com.venkat.backend.lld.ratelimiter;

/**
 * Factory that creates the appropriate {@link RateLimiter} implementation based on a
 * {@link RateLimiterConfig}.
 *
 * <p>Usage:
 * <pre>{@code
 * RateLimiterConfig config = new RateLimiterConfig(
 *         RateLimiterAlgorithm.TOKEN_BUCKET, 10, Duration.ofSeconds(1));
 * RateLimiter limiter = RateLimiterFactory.create(config);
 * }</pre>
 *
 * <p>Extending the factory: when a new algorithm is added, add a corresponding
 * {@code case} to the switch statement (or, for a more open-closed design, consider a
 * registry of {@code Supplier<RateLimiter>} keyed by {@link RateLimiterAlgorithm}).
 */
public final class RateLimiterFactory {

    /** Prevent instantiation. */
    private RateLimiterFactory() {}

    /**
     * Creates and returns a {@link RateLimiter} configured according to {@code config}.
     *
     * @param config the configuration driving algorithm selection and parameters;
     *               must not be {@code null}
     * @return a new {@link RateLimiter} instance
     * @throws IllegalArgumentException if the algorithm in {@code config} is not supported
     */
    public static RateLimiter create(RateLimiterConfig config) {
        throw new UnsupportedOperationException("implement me");
    }
}
