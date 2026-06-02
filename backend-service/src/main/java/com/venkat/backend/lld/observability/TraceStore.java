package com.venkat.backend.lld.observability;

import com.venkat.backend.lld.observability.ObservabilityModels.CompletedSpan;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory sink for completed spans, keyed by {@code traceId} — the analogue of a tracing
 * backend (Jaeger, Tempo, Honeycomb). A real backend would batch-export these over OTLP; here we
 * just retain them so a trace can be reassembled and inspected.
 *
 * <p>Spans for one trace are appended in completion order. {@link CopyOnWriteArrayList} keeps
 * per-trace appends safe alongside concurrent reads.
 */
public final class TraceStore {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<CompletedSpan>> traces =
            new ConcurrentHashMap<>();

    public void record(CompletedSpan span) {
        traces.computeIfAbsent(span.traceId(), k -> new CopyOnWriteArrayList<>()).add(span);
    }

    /** @return spans for the trace in completion order (empty if unknown), as an immutable copy */
    public List<CompletedSpan> get(String traceId) {
        CopyOnWriteArrayList<CompletedSpan> spans = traces.get(traceId);
        return spans == null ? List.of() : List.copyOf(spans);
    }

    public Set<String> traceIds() {
        return Map.copyOf(traces).keySet();
    }

    public int size() {
        return traces.size();
    }

    public void clear() {
        traces.clear();
    }
}
