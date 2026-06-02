package com.venkat.backend.lld.observability;

import com.venkat.backend.lld.observability.ObservabilityModels.HistogramSummary;

import java.util.Arrays;
import java.util.Random;

/**
 * A thread-safe latency/value distribution that supports percentile queries.
 *
 * <p>This is the concept behind a Micrometer {@code Timer} or a Prometheus histogram: a real
 * service cannot keep every sample forever, so we hold a bounded <em>reservoir</em> and compute
 * percentiles over it. With <a href="https://en.wikipedia.org/wiki/Reservoir_sampling">reservoir
 * sampling</a> the retained sample stays statistically representative of an unbounded stream while
 * using O(capacity) memory. Running {@code count/min/max/sum} are kept exactly for cheap aggregates.
 *
 * <p>When the number of recorded values is &le; {@code capacity} (the common case in tests), every
 * value is retained and percentiles are <em>exact</em>.
 *
 * <p>Percentiles use the <strong>nearest-rank</strong> method: the p-th percentile is the value at
 * rank {@code ceil(p/100 * n)} in the sorted sample.
 */
public final class Histogram {

    /** Default reservoir size — large enough that kata workloads are retained in full. */
    public static final int DEFAULT_CAPACITY = 4096;

    private final int capacity;
    private final double[] reservoir;
    private final Random random;

    // Guarded by `this`.
    private int size;        // number of slots currently filled in `reservoir`
    private long count;      // total values ever recorded (may exceed capacity)
    private double sum;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    public Histogram() {
        this(DEFAULT_CAPACITY, new Random());
    }

    public Histogram(int capacity) {
        this(capacity, new Random());
    }

    /** Package-visible constructor with an injectable {@link Random} for deterministic tests. */
    Histogram(int capacity, Random random) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.reservoir = new double[capacity];
        this.random = random;
    }

    public synchronized void record(double value) {
        count++;
        sum += value;
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }

        if (size < capacity) {
            reservoir[size++] = value;
        } else {
            // Reservoir full: keep this value with probability capacity/count (Algorithm R).
            long idx = (long) (random.nextDouble() * count);
            if (idx < capacity) {
                reservoir[(int) idx] = value;
            }
        }
    }

    /**
     * @param p percentile in {@code [0, 100]}
     * @return the value at that percentile via nearest-rank, or {@code 0} if no values recorded
     */
    public synchronized double percentile(double p) {
        if (p < 0 || p > 100) {
            throw new IllegalArgumentException("percentile must be in [0, 100]");
        }
        if (size == 0) {
            return 0.0;
        }
        double[] sorted = Arrays.copyOf(reservoir, size);
        Arrays.sort(sorted);
        int rank = (int) Math.ceil(p / 100.0 * size);
        rank = Math.max(1, Math.min(rank, size)); // clamp into [1, size]
        return sorted[rank - 1];
    }

    public synchronized long count() {
        return count;
    }

    public synchronized double min() {
        return count == 0 ? 0.0 : min;
    }

    public synchronized double max() {
        return count == 0 ? 0.0 : max;
    }

    public synchronized double mean() {
        return count == 0 ? 0.0 : sum / count;
    }

    public synchronized HistogramSummary summary() {
        return new HistogramSummary(count, percentile(50), percentile(90), percentile(99), max());
    }
}
