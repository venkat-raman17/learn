package com.venkat.backend.lld.observability;

/**
 * Immutable value objects shared across the observability kata.
 *
 * <p>Grouped in one file (like {@code AuthModels}) so the package's data shapes are easy to scan.
 */
public final class ObservabilityModels {

    private ObservabilityModels() {
    }

    /**
     * An <em>open</em> span — created by {@link TracingService#startSpan} and not yet ended.
     *
     * @param traceId   shared id for every span in one trace (request)
     * @param spanId    unique id for this span
     * @param parentId  the enclosing span's id, or {@code null} for a root span
     * @param name      human-readable operation name (e.g. {@code "db.query"})
     * @param startNanos monotonic timestamp captured when the span opened
     */
    public record Span(String traceId, String spanId, String parentId, String name, long startNanos) {

        public boolean isRoot() {
            return parentId == null;
        }
    }

    /**
     * A <em>closed</em> span recorded into the {@link TraceStore}.
     *
     * @param durationNanos elapsed time between {@code startSpan} and {@code endSpan}
     */
    public record CompletedSpan(String traceId, String spanId, String parentId, String name,
                                long durationNanos) {

        public boolean isRoot() {
            return parentId == null;
        }
    }

    /**
     * A point-in-time summary of a {@link Histogram}. Percentiles use the nearest-rank method.
     */
    public record HistogramSummary(long count, double p50, double p90, double p99, double max) {
    }
}
