package com.venkat.backend.lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Fixed Window Counter rate limiter.
 *
 * <p>Time is divided into non-overlapping windows of {@code window} length.
 * Each client maintains a counter for the current window; the counter resets
 * when a new window begins.
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;
    // Per-client state: long[]{windowIndex, count}
    private final ConcurrentHashMap<String, long[]> clientState = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimiterConfig config) {
        this.config = config;
    }

    @Override
    public boolean allow(String clientId) {
        long windowMs     = config.getWindow().toMillis();
        int  capacity     = config.getCapacity();
        long nowMs        = System.currentTimeMillis();
        long currentWindow = nowMs / windowMs;

        long[] state = clientState.computeIfAbsent(clientId, k -> new long[]{currentWindow, 0});

        synchronized (state) {
            // New window — reset counter
            if (state[0] != currentWindow) {
                state[0] = currentWindow;
                state[1] = 0;
            }
            if (state[1] < capacity) {
                state[1]++;
                return true;
            }
            return false;
        }
    }
}
