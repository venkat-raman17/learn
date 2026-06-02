/**
 * <h2>LLD Practice Problem: In-Process Observability Toolkit (Metrics + Tracing)</h2>
 *
 * <hr>
 *
 * <h3>Problem Statement</h3>
 * <p>
 * Build, from scratch and with no external libraries, the core of an observability stack: a
 * <strong>metrics registry</strong> and a <strong>distributed-tracing</strong> engine. The goal is
 * to understand what production tools — Micrometer, Prometheus, OpenTelemetry — actually do
 * internally before wiring them in. (This package is the conceptual sibling of the Spring Actuator +
 * Micrometer wiring under {@code com.venkat.backend.ops}, which exposes the <em>real</em> tools.)
 * </p>
 *
 * <hr>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li><strong>Metrics</strong> — support the three primitive instruments:
 *       <ul>
 *         <li><em>Counter</em>: monotonically increasing total (e.g. requests, errors).</li>
 *         <li><em>Gauge</em>: an instantaneous value sampled on read (e.g. queue depth).</li>
 *         <li><em>Histogram</em>: a distribution of recorded values that answers percentile
 *             queries (p50/p90/p99) for latency.</li>
 *       </ul>
 *   </li>
 *   <li>Expose a {@code snapshot()} that serializes the whole registry as a flat
 *       {@code Map<String, Number>} — the shape a scrape endpoint would emit.</li>
 *   <li><strong>Tracing</strong> — model a trace as a tree of spans. A root span starts a trace;
 *       child spans inherit the {@code traceId} and reference the parent's {@code spanId}. Closing a
 *       span records its duration. A whole trace can be retrieved by {@code traceId}.</li>
 * </ol>
 *
 * <hr>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ol>
 *   <li><strong>Thread-safety</strong>: instruments are written from many request threads
 *       concurrently. Counters use {@link java.util.concurrent.atomic.LongAdder} (better than
 *       {@code AtomicLong} under contention); the trace store uses
 *       {@link java.util.concurrent.ConcurrentHashMap} +
 *       {@link java.util.concurrent.CopyOnWriteArrayList}.</li>
 *   <li><strong>Bounded memory</strong>: a real histogram cannot retain every sample — use a
 *       capped <em>reservoir</em> with reservoir sampling, exact when samples &le; capacity.</li>
 *   <li><strong>No external dependencies</strong>: {@code java.util} / {@code java.util.concurrent}
 *       only.</li>
 *   <li><strong>Determinism in tests</strong>: inject the clock (a
 *       {@link java.util.function.LongSupplier} of nanos) so durations can be asserted without
 *       sleeping — mirrors the rate-limiter kata.</li>
 * </ol>
 *
 * <hr>
 *
 * <h3>Key Types</h3>
 * <ul>
 *   <li>{@link com.venkat.backend.lld.observability.MetricsCollector} — counter / gauge / histogram
 *       registry with {@code snapshot()}.</li>
 *   <li>{@link com.venkat.backend.lld.observability.Histogram} — reservoir-backed distribution with
 *       nearest-rank percentiles.</li>
 *   <li>{@link com.venkat.backend.lld.observability.TracingService} — span lifecycle
 *       ({@code startSpan} / {@code endSpan} / {@code getTrace}).</li>
 *   <li>{@link com.venkat.backend.lld.observability.TraceStore} — in-memory sink keyed by trace.</li>
 *   <li>{@link com.venkat.backend.lld.observability.ObservabilityModels} — {@code Span},
 *       {@code CompletedSpan}, {@code HistogramSummary} records.</li>
 * </ul>
 *
 * <p>
 * Note: the existing {@code com.venkat.backend.lld.logger.Logger.JsonFormatter} is the "DIY
 * structured logging" precursor to Spring's native structured logging (configured in
 * {@code application.yaml}) — logs, metrics, and traces are the three pillars this repo now covers.
 * </p>
 *
 * <hr>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>How would you export these metrics to Prometheus (pull/scrape) vs. push them over OTLP to a
 *       collector? What changes in the registry's API?</li>
 *   <li>Tag cardinality: why does adding an unbounded label (e.g. {@code userId}) to a counter blow
 *       up memory, and how do real systems guard against it?</li>
 *   <li>Tracing in production samples (e.g. 1%) to control cost. Where would you add a sampling
 *       decision so that a whole trace is kept or dropped together (head-based sampling)?</li>
 *   <li>How do you propagate {@code traceId}/{@code spanId} across a network hop (W3C
 *       {@code traceparent} header) so spans from different services join one trace?</li>
 * </ol>
 *
 * @author  learner
 * @version 1.0
 */
package com.venkat.backend.lld.observability;
