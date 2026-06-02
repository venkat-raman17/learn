package com.venkat.backend.lld.ratelimiter;

import java.util.Objects;

public final class RateLimiterFactory {

    private RateLimiterFactory() {}

    public static RateLimiter create(RateLimiterConfig config) {
        Objects.requireNonNull(config, "config must not be null");
        return switch (config.getAlgorithm()) {
            case TOKEN_BUCKET       -> new TokenBucketRateLimiter(config);
            case FIXED_WINDOW       -> new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW_LOG -> new SlidingWindowLogRateLimiter(config);
        };
    }
}
