package com.venkat.backend.ops;

import com.venkat.backend.lld.observability.MetricsCollector;
import com.venkat.backend.lld.observability.ObservabilityModels.CompletedSpan;
import com.venkat.backend.lld.observability.ObservabilityModels.Span;
import com.venkat.backend.lld.observability.TracingService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * A deliberately thin endpoint whose only job is to make the operational layer tangible: one call
 * exercises <em>both</em> the real Micrometer registry and the from-scratch observability kata, so
 * the same work shows up at {@code /actuator/metrics/ops.requests}, {@code /actuator/prometheus},
 * the kata snapshot, and the in-memory trace store.
 */
@RestController
@RequestMapping("/api/ops")
public class OpsController {

    private final MetricsCollector kataMetrics;
    private final TracingService tracing;
    private final Counter micrometerRequests;
    private final Timer micrometerWork;

    public OpsController(MetricsCollector kataMetrics, TracingService tracing, MeterRegistry registry) {
        this.kataMetrics = kataMetrics;
        this.tracing = tracing;
        this.micrometerRequests = Counter.builder("ops.requests")
                .description("Total /api/ops/work calls")
                .register(registry);                       // -> ops_requests_total in Prometheus
        this.micrometerWork = Timer.builder("ops.work")
                .description("Simulated work latency")
                .register(registry);
    }

    /**
     * Simulates a unit of work taking {@code ms} milliseconds (non-blocking via {@link Mono#delay}),
     * instrumenting it across every signal.
     */
    @GetMapping("/work")
    public Mono<Map<String, Object>> work(@RequestParam(defaultValue = "10") long ms) {
        Span span = tracing.startSpan("ops.work");
        long startNanos = System.nanoTime();

        return Mono.delay(Duration.ofMillis(Math.max(0, ms)))
                .map(tick -> {
                    long elapsedNanos = System.nanoTime() - startNanos;
                    long elapsedMs = elapsedNanos / 1_000_000;

                    // Real tools (Micrometer / Actuator / Prometheus).
                    micrometerRequests.increment();
                    micrometerWork.record(Duration.ofNanos(elapsedNanos));

                    // From-scratch kata, mirroring the same signals.
                    kataMetrics.increment("ops.requests");
                    kataMetrics.recordTiming("ops.work.ms", elapsedMs);
                    CompletedSpan completed = tracing.endSpan(span);

                    return Map.of(
                            "traceId", completed.traceId(),
                            "spanId", completed.spanId(),
                            "elapsedMs", elapsedMs,
                            "kataMetrics", kataMetrics.snapshot());
                });
    }

    /** Reassembles a trace from the kata's in-memory store — demonstrates {@code getTrace}. */
    @GetMapping("/trace/{traceId}")
    public Mono<List<CompletedSpan>> trace(@PathVariable String traceId) {
        return Mono.just(tracing.getTrace(traceId));
    }

    /** The kata registry's flat snapshot — the shape a scrape endpoint would serialize. */
    @GetMapping("/metrics")
    public Mono<Map<String, Number>> metrics() {
        return Mono.just(kataMetrics.snapshot());
    }
}
