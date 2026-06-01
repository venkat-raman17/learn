package com.venkat.backend.lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Token Bucket rate limiter.
 *
 * <p>Each client gets its own bucket seeded with {@code capacity} tokens.
 * One token is added every {@code window} nanoseconds (lazy refill on each request).
 * A request is allowed only if at least one token is available.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;
    // Per-client bucket state stored as double[]{tokens, lastRefillNanos}
    private final ConcurrentHashMap<String, double[]> clientState = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimiterConfig config) {
        this.config = config;
    }

    @Override
    public boolean allow(String clientId) {
        long windowNanos = config.getWindow().toNanos();
        int capacity     = config.getCapacity();
        long nowNanos    = System.nanoTime();

        // computeIfAbsent initialises the bucket to full
        double[] state = clientState.computeIfAbsent(
            clientId, k -> new double[]{capacity, nowNanos});

        synchronized (state) {
            // Refill: add (elapsed / windowNanos) tokens, capped at capacity
            double elapsed = nowNanos - state[1];
            double tokensToAdd = elapsed / windowNanos;
            state[0] = Math.min(capacity, state[0] + tokensToAdd);
            state[1] = nowNanos;

            if (state[0] >= 1.0) {
                state[0] -= 1.0;
                return true;
            }
            return false;
        }
    }
}
