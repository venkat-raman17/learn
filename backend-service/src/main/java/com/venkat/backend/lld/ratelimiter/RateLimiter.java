package com.venkat.backend.lld.ratelimiter;

/**
 * Contract for a rate limiter that decides whether a request from a given client
 * should be allowed or rejected.
 *
 * <p>Implementations must be <strong>thread-safe</strong>: {@code allow()} may be called
 * concurrently from multiple threads for the same or different client identifiers.
 *
 * <p>Three implementations are expected:
 * <ol>
 *   <li>{@link TokenBucketRateLimiter}</li>
 *   <li>{@link FixedWindowRateLimiter}</li>
 *   <li>{@link SlidingWindowLogRateLimiter}</li>
 * </ol>
 *
 * <p>Obtain an instance via {@link RateLimiterFactory#create(RateLimiterConfig)}.
 */
public interface RateLimiter {

    /**
     * Determines whether the client identified by {@code clientId} is permitted to
     * proceed with its current request.
     *
     * <p>A return value of {@code true} means the request is within the allowed rate;
     * {@code false} means it has been rate-limited and should be rejected (e.g., HTTP 429).
     *
     * @param clientId a non-null, non-blank identifier for the requesting client
     *                 (e.g., user ID, IP address, API key)
     * @return {@code true} if the request is allowed, {@code false} if it is rate-limited
     */
    boolean allow(String clientId);
}
