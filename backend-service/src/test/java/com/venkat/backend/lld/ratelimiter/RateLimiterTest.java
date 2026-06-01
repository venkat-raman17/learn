package com.venkat.backend.lld.ratelimiter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Definition-of-done tests for the Rate Limiter LLD practice problem.
 *
 * <p>Remove the {@code @Disabled} annotation once you have implemented all classes.
 * All tests must pass without modifying this file.
 */
@Disabled("LLD practice — implement, then remove @Disabled")
class RateLimiterTest {

    /**
     * Token Bucket: a client may fire exactly {@code capacity} requests in a burst
     * before being rejected, and is never rejected below the capacity threshold.
     *
     * <p>This test deliberately does NOT sleep; it exercises the burst behaviour of
     * the bucket before any refill occurs.
     */
    @Test
    void tokenBucket_allowsUpToCapacityThenRejects() {
        int capacity = 3;
        RateLimiterConfig config = new RateLimiterConfig(
                RateLimiterAlgorithm.TOKEN_BUCKET, capacity, Duration.ofSeconds(1));
        RateLimiter limiter = RateLimiterFactory.create(config);

        String client = "user-token-1";

        // First 'capacity' requests must be allowed
        for (int i = 0; i < capacity; i++) {
            assertTrue(limiter.allow(client),
                    "Request " + (i + 1) + " should be allowed (tokens available)");
        }

        // The next request must be rejected (bucket empty, no time has passed)
        assertFalse(limiter.allow(client),
                "Request after capacity exhausted should be rejected");
    }

    /**
     * Fixed Window: counts requests within the current window independently per client.
     *
     * <p>Two different clients share the same limiter instance but maintain separate
     * counters; exhausting one client's quota must not affect the other.
     */
    @Test
    void fixedWindow_perClientIsolation() {
        int limit = 2;
        RateLimiterConfig config = new RateLimiterConfig(
                RateLimiterAlgorithm.FIXED_WINDOW, limit, Duration.ofMinutes(1));
        RateLimiter limiter = RateLimiterFactory.create(config);

        String alice = "user-alice";
        String bob   = "user-bob";

        // Alice exhausts her quota
        assertTrue(limiter.allow(alice), "Alice request 1 allowed");
        assertTrue(limiter.allow(alice), "Alice request 2 allowed");
        assertFalse(limiter.allow(alice), "Alice request 3 rejected (limit reached)");

        // Bob is a separate client — he should still be allowed
        assertTrue(limiter.allow(bob), "Bob request 1 allowed (independent counter)");
        assertTrue(limiter.allow(bob), "Bob request 2 allowed");
        assertFalse(limiter.allow(bob), "Bob request 3 rejected (limit reached)");
    }

    /**
     * Sliding Window Log: concurrency test — 20 threads each fire 50 requests for the
     * same client; the total allowed count must be exactly {@code capacity} (no more,
     * no less if enough requests were made).
     *
     * <p>This test verifies that the per-client state is properly synchronised and that
     * no two threads can simultaneously exceed the limit.
     */
    @Test
    void slidingWindowLog_concurrentRequests_neverExceedLimit() throws Exception {
        int capacity = 30;
        RateLimiterConfig config = new RateLimiterConfig(
                RateLimiterAlgorithm.SLIDING_WINDOW_LOG, capacity, Duration.ofMinutes(5));
        RateLimiter limiter = RateLimiterFactory.create(config);

        String client = "concurrent-client";
        int threadCount = 20;
        int requestsPerThread = 50; // total attempts = 1000, well above capacity

        AtomicInteger allowed = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            tasks.add(() -> {
                for (int r = 0; r < requestsPerThread; r++) {
                    if (limiter.allow(client)) {
                        allowed.incrementAndGet();
                    }
                }
                return null;
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);
        executor.shutdown();

        // Rethrow any exception from worker threads
        for (Future<Void> f : futures) {
            f.get();
        }

        // Exactly 'capacity' requests should have been allowed — no race condition should
        // cause the count to exceed the limit
        assertEquals(capacity, allowed.get(),
                "Allowed count must equal capacity under concurrent load");
    }
}
