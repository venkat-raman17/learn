package com.venkat.backend.lld.ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Log rate limiter.
 *
 * <p>Maintains a per-client log (deque) of timestamps for allowed requests.
 * On each call: (1) evict timestamps older than {@code now - window},
 * (2) allow if log size < capacity.
 *
 * <p>Thread-safety: each client deque is guarded by its own intrinsic lock.
 */
public class SlidingWindowLogRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;
    private final ConcurrentHashMap<String, Deque<Long>> clientLogs = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(RateLimiterConfig config) {
        this.config = config;
    }

    @Override
    public boolean allow(String clientId) {
        long windowMs = config.getWindow().toMillis();
        int  capacity = config.getCapacity();
        long nowMs    = System.currentTimeMillis();

        Deque<Long> log = clientLogs.computeIfAbsent(clientId, k -> new ArrayDeque<>());

        synchronized (log) {
            // Evict timestamps outside the current window
            while (!log.isEmpty() && log.peekFirst() <= nowMs - windowMs) {
                log.pollFirst();
            }
            if (log.size() < capacity) {
                log.addLast(nowMs);
                return true;
            }
            return false;
        }
    }
}
