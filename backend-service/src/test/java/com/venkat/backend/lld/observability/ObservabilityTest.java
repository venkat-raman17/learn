package com.venkat.backend.lld.observability;

import com.venkat.backend.lld.observability.ObservabilityModels.CompletedSpan;
import com.venkat.backend.lld.observability.ObservabilityModels.Span;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class ObservabilityTest {

    private MetricsCollector metrics;

    @BeforeEach
    void setUp() {
        metrics = new MetricsCollector();
    }

    // ── Counters: atomic under contention ─────────────────────────────

    @Test
    void counter_isAtomic_under20Threads() throws InterruptedException {
        int threads = 20;
        int perThread = 1_000;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) {
                        metrics.increment("requests");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS), "workers should finish");
        pool.shutdownNow();

        // No lost updates: exact total expected.
        assertEquals((long) threads * perThread, metrics.counter("requests"));
    }

    @Test
    void increment_withDelta_accumulates() {
        metrics.increment("bytes", 100);
        metrics.increment("bytes", 23);
        assertEquals(123L, metrics.counter("bytes"));
    }

    // ── Gauge: sampled live on read ───────────────────────────────────

    @Test
    void gauge_reflectsLiveValue() {
        AtomicLong queueDepth = new AtomicLong(3);
        metrics.gauge("queue.depth", queueDepth::get);

        assertEquals(3L, metrics.gaugeValue("queue.depth").longValue());
        queueDepth.set(7);
        assertEquals(7L, metrics.gaugeValue("queue.depth").longValue(), "gauge must re-sample");
    }

    // ── Histogram: nearest-rank percentiles ───────────────────────────

    @Test
    void histogram_percentiles_overKnownDistribution() {
        Histogram h = new Histogram();
        for (int v = 1; v <= 100; v++) {   // 1..100, fully retained (< default capacity)
            h.record(v);
        }
        // nearest-rank: p-th value = sorted[ceil(p/100 * n) - 1]
        assertEquals(50.0, h.percentile(50));
        assertEquals(90.0, h.percentile(90));
        assertEquals(99.0, h.percentile(99));
        assertEquals(100L, h.count());
        assertEquals(1.0, h.min());
        assertEquals(100.0, h.max());
        assertEquals(50.5, h.mean(), 1e-9);
    }

    @Test
    void histogram_empty_returnsZeroPercentiles() {
        Histogram h = new Histogram();
        assertEquals(0.0, h.percentile(50));
        assertEquals(0L, h.count());
    }

    // ── Snapshot: all instrument kinds present ────────────────────────

    @Test
    void snapshot_containsCountersGaugesAndHistogramSummaries() {
        metrics.increment("requests", 5);
        metrics.gauge("threads.active", () -> 12);
        metrics.recordTiming("latency.ms", 10);
        metrics.recordTiming("latency.ms", 20);
        metrics.recordTiming("latency.ms", 30);

        Map<String, Number> snap = metrics.snapshot();

        assertEquals(5L, snap.get("requests").longValue());
        assertEquals(12, snap.get("threads.active").intValue());
        assertEquals(3L, snap.get("latency.ms.count").longValue());
        assertEquals(30.0, snap.get("latency.ms.max").doubleValue());
        assertTrue(snap.containsKey("latency.ms.p50"));
        assertTrue(snap.containsKey("latency.ms.p99"));

        // snapshot is an immutable view
        assertThrows(UnsupportedOperationException.class, () -> snap.put("x", 1));
    }

    // ── Tracing: parent/child linkage + deterministic durations ───────

    @Test
    void tracing_rootAndChildren_shareTraceAndLinkParents() {
        // Deterministic clock: each read advances by 5ns.
        AtomicLong now = new AtomicLong(0);
        TracingService tracing = new TracingService(new TraceStore(), () -> now.getAndAdd(5));

        Span root = tracing.startSpan("handleRequest");   // start @0
        Span childA = tracing.startSpan("db.query", root); // start @5
        Span childB = tracing.startSpan("cache.get", root);// start @10
        tracing.endSpan(childB);                            // end @15 -> dur 5
        tracing.endSpan(childA);                            // end @20 -> dur 15
        tracing.endSpan(root);                              // end @25 -> dur 25

        List<CompletedSpan> trace = tracing.getTrace(root.traceId());
        assertEquals(3, trace.size());

        // All share one traceId.
        assertTrue(trace.stream().allMatch(s -> s.traceId().equals(root.traceId())));

        // Exactly one root; both children reference the root's spanId.
        assertEquals(1, trace.stream().filter(CompletedSpan::isRoot).count());
        assertTrue(trace.stream()
                .filter(s -> !s.isRoot())
                .allMatch(s -> s.parentId().equals(root.spanId())));

        // Durations are deterministic from the injected clock.
        CompletedSpan completedRoot = trace.stream().filter(CompletedSpan::isRoot).findFirst().orElseThrow();
        assertEquals(25L, completedRoot.durationNanos());
    }

    @Test
    void startSpan_withNullParent_rejected() {
        TracingService tracing = new TracingService();
        assertThrows(IllegalArgumentException.class, () -> tracing.startSpan("x", null));
    }

    @Test
    void getTrace_unknownId_returnsEmpty() {
        TracingService tracing = new TracingService();
        assertTrue(tracing.getTrace("nope").isEmpty());
    }
}
