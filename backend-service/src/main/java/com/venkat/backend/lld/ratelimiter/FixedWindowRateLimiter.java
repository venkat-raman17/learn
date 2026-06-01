package com.venkat.backend.lld.ratelimiter;

/**
 * Fixed Window Counter rate limiter.
 *
 * <h3>Algorithm sketch</h3>
 * <ul>
 *   <li>Time is divided into non-overlapping windows of length {@code window}.</li>
 *   <li>The window boundary for a given instant is computed as
 *       {@code floor(now / windowMs) * windowMs}.</li>
 *   <li>Each client maintains a counter for the current window.  When a request arrives:
 *       <ol>
 *         <li>Determine which window "now" falls in.</li>
 *         <li>If the stored window index differs from the current one, reset the counter to 0.</li>
 *         <li>If the counter is below {@code capacity}, increment it and return {@code true}.</li>
 *         <li>Otherwise return {@code false}.</li>
 *       </ol>
 *   </li>
 * </ul>
 *
 * <h3>Known limitation to discuss</h3>
 * <p>A client can send up to {@code 2 * capacity} requests in a short burst spanning a window
 * boundary (e.g., {@code capacity} requests at the end of window N and {@code capacity} at the
 * start of window N+1).  How would you address this?</p>
 *
 * <h3>Concurrency note</h3>
 * <p>The window-reset-plus-increment sequence must be atomic per client to avoid lost updates.</p>
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimiterConfig config;

    /**
     * Creates a FixedWindowRateLimiter with the given configuration.
     *
     * @param config rate limiter configuration; must use
     *               {@link RateLimiterAlgorithm#FIXED_WINDOW}
     */
    public FixedWindowRateLimiter(RateLimiterConfig config) {
        this.config = config;
        // TODO: initialise per-client state storage
    }

    /**
     * {@inheritDoc}
     *
     * <p>Counts requests within the current fixed window; resets when the window rolls over.
     */
    @Override
    public boolean allow(String clientId) {
        throw new UnsupportedOperationException("implement me");
    }
}
