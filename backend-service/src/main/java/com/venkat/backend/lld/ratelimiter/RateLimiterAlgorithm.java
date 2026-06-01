package com.venkat.backend.lld.ratelimiter;

/**
 * Supported rate-limiting algorithms.
 *
 * <ul>
 *   <li>{@link #TOKEN_BUCKET} — tokens accumulate up to capacity; each request consumes one.</li>
 *   <li>{@link #FIXED_WINDOW} — counts requests in fixed, non-overlapping time windows.</li>
 *   <li>{@link #SLIDING_WINDOW_LOG} — tracks a per-client log of timestamps within a rolling window.</li>
 * </ul>
 */
public enum RateLimiterAlgorithm {
    TOKEN_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW_LOG
}
