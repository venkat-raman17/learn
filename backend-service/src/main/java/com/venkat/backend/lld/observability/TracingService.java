package com.venkat.backend.lld.observability;

import com.venkat.backend.lld.observability.ObservabilityModels.CompletedSpan;
import com.venkat.backend.lld.observability.ObservabilityModels.Span;

import java.util.List;
import java.util.UUID;
import java.util.function.LongSupplier;

/**
 * Minimal distributed-tracing core — the concept behind OpenTelemetry spans.
 *
 * <p>A <strong>trace</strong> is one logical request; it is a tree of <strong>spans</strong>, each a
 * timed unit of work. Every span shares the trace's {@code traceId}; a child carries its parent's
 * {@code spanId} as {@code parentId}, which is what lets a backend reconstruct the tree.
 *
 * <p>The clock is injected as a {@link LongSupplier} of nanoseconds so tests can advance time
 * deterministically instead of sleeping — the same clock-injection trick the rate-limiter kata uses.
 *
 * <pre>{@code
 * Span root  = tracing.startSpan("handleRequest");
 * Span child = tracing.startSpan("db.query", root);
 * tracing.endSpan(child);
 * tracing.endSpan(root);
 * List<CompletedSpan> trace = tracing.getTrace(root.traceId()); // 2 spans
 * }</pre>
 */
public final class TracingService {

    private final TraceStore store;
    private final LongSupplier nanoClock;

    public TracingService() {
        this(new TraceStore(), System::nanoTime);
    }

    public TracingService(TraceStore store, LongSupplier nanoClock) {
        this.store = store;
        this.nanoClock = nanoClock;
    }

    /** Opens a new root span, starting a fresh trace. */
    public Span startSpan(String name) {
        return new Span(newId(), newId(), null, name, nanoClock.getAsLong());
    }

    /** Opens a child span within the parent's trace. */
    public Span startSpan(String name, Span parent) {
        if (parent == null) {
            throw new IllegalArgumentException("parent must not be null; use startSpan(name) for a root");
        }
        return new Span(parent.traceId(), newId(), parent.spanId(), name, nanoClock.getAsLong());
    }

    /** Closes a span, recording its duration into the {@link TraceStore}. */
    public CompletedSpan endSpan(Span span) {
        if (span == null) {
            throw new IllegalArgumentException("span must not be null");
        }
        long duration = nanoClock.getAsLong() - span.startNanos();
        CompletedSpan completed = new CompletedSpan(
                span.traceId(), span.spanId(), span.parentId(), span.name(), duration);
        store.record(completed);
        return completed;
    }

    /** @return all completed spans for the trace, in completion order */
    public List<CompletedSpan> getTrace(String traceId) {
        return store.get(traceId);
    }

    public TraceStore store() {
        return store;
    }

    private static String newId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
