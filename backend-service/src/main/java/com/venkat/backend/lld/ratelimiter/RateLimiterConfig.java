package com.venkat.backend.lld.ratelimiter;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable configuration for a {@link RateLimiter}.
 *
 * <p>Fields are algorithm-specific; not every field is relevant to every algorithm:
 * <ul>
 *   <li>{@code algorithm} — which strategy to instantiate.</li>
 *   <li>{@code capacity} — max tokens (Token Bucket) <em>or</em> max requests per window
 *       (Fixed Window / Sliding Window Log).</li>
 *   <li>{@code window} — refill interval (Token Bucket: 1 token per window)
 *       <em>or</em> window duration (Fixed Window / Sliding Window Log).</li>
 * </ul>
 *
 * <p>Example — 5-token bucket, refill 1 token per second:
 * <pre>{@code
 * new RateLimiterConfig(RateLimiterAlgorithm.TOKEN_BUCKET, 5, Duration.ofSeconds(1))
 * }</pre>
 */
public final class RateLimiterConfig {

    private final RateLimiterAlgorithm algorithm;
    private final int capacity;
    private final Duration window;

    /**
     * Constructs a configuration.
     *
     * @param algorithm the algorithm to use; must not be {@code null}
     * @param capacity  max tokens or max requests; must be positive
     * @param window    refill interval or window duration; must not be {@code null} or zero
     * @throws IllegalArgumentException if {@code capacity} is not positive or {@code window} is non-positive
     */
    public RateLimiterConfig(RateLimiterAlgorithm algorithm, int capacity, Duration window) {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        Objects.requireNonNull(window, "window must not be null");
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive, got: " + capacity);
        }
        if (window.isNegative() || window.isZero()) {
            throw new IllegalArgumentException("window must be positive, got: " + window);
        }
        this.algorithm = algorithm;
        this.capacity = capacity;
        this.window = window;
    }

    /** Returns the chosen algorithm. */
    public RateLimiterAlgorithm getAlgorithm() {
        return algorithm;
    }

    /** Returns the capacity (max tokens or max requests per window). */
    public int getCapacity() {
        return capacity;
    }

    /** Returns the window duration (refill interval or sliding/fixed window length). */
    public Duration getWindow() {
        return window;
    }

    @Override
    public String toString() {
        return "RateLimiterConfig{algorithm=" + algorithm
                + ", capacity=" + capacity
                + ", window=" + window + '}';
    }
}
