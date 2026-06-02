package com.venkat.backend.lld.observability;

import com.venkat.backend.lld.observability.ObservabilityModels.HistogramSummary;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;

/**
 * A minimal in-process metrics registry — the three primitive instruments every metrics system
 * (Micrometer, Prometheus, OpenTelemetry) is built on:
 *
 * <ul>
 *   <li><strong>Counter</strong> — monotonically increasing total (requests, errors). Backed by
 *       {@link LongAdder}, which beats {@code AtomicLong} under high write contention.</li>
 *   <li><strong>Gauge</strong> — an instantaneous value sampled on read (queue depth, pool size).
 *       Stored as a {@link Supplier} so it always reflects live state, never a stale snapshot.</li>
 *   <li><strong>Histogram</strong> — a distribution of recorded values with percentile queries
 *       (request latency). See {@link Histogram}.</li>
 * </ul>
 *
 * <p>All instruments are created lazily and are safe to register/read from many threads.
 */
public final class MetricsCollector {

    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Supplier<Number>> gauges = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Histogram> histograms = new ConcurrentHashMap<>();

    // ── Counters ──────────────────────────────────────────────────────

    public void increment(String name) {
        increment(name, 1L);
    }

    public void increment(String name, long delta) {
        counters.computeIfAbsent(name, k -> new LongAdder()).add(delta);
    }

    public long counter(String name) {
        LongAdder adder = counters.get(name);
        return adder == null ? 0L : adder.sum();
    }

    // ── Gauges ────────────────────────────────────────────────────────

    /** Registers (or replaces) a gauge sampled lazily on every {@link #snapshot()} / read. */
    public void gauge(String name, Supplier<Number> sampler) {
        gauges.put(name, sampler);
    }

    public Number gaugeValue(String name) {
        Supplier<Number> sampler = gauges.get(name);
        return sampler == null ? null : sampler.get();
    }

    // ── Histograms / timers ───────────────────────────────────────────

    public void recordTiming(String name, double millis) {
        histograms.computeIfAbsent(name, k -> new Histogram()).record(millis);
    }

    public Histogram histogram(String name) {
        return histograms.computeIfAbsent(name, k -> new Histogram());
    }

    // ── Snapshot ──────────────────────────────────────────────────────

    /**
     * A flat, sorted, immutable view of every instrument — the shape a scrape endpoint would
     * serialize. Histograms expand into {@code name.count / .p50 / .p90 / .p99 / .max} keys so the
     * whole registry fits a single {@code Map<String, Number>}.
     */
    public Map<String, Number> snapshot() {
        Map<String, Number> out = new TreeMap<>();
        counters.forEach((name, adder) -> out.put(name, adder.sum()));
        gauges.forEach((name, sampler) -> out.put(name, sampler.get()));
        histograms.forEach((name, h) -> {
            HistogramSummary s = h.summary();
            out.put(name + ".count", s.count());
            out.put(name + ".p50", s.p50());
            out.put(name + ".p90", s.p90());
            out.put(name + ".p99", s.p99());
            out.put(name + ".max", s.max());
        });
        return Map.copyOf(out);
    }
}
