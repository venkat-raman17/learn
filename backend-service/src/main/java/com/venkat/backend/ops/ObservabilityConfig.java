package com.venkat.backend.ops;

import com.venkat.backend.lld.observability.MetricsCollector;
import com.venkat.backend.lld.observability.TracingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the from-scratch observability kata ({@code lld/observability}) into the Spring context as
 * singletons, so {@link OpsController} can exercise it side-by-side with the real Micrometer
 * {@code MeterRegistry} (autoconfigured by the actuator starter). The pairing is deliberate: the
 * kata shows the mechanism, Micrometer shows the production tool.
 */
@Configuration
public class ObservabilityConfig {

    @Bean
    public MetricsCollector metricsCollector() {
        return new MetricsCollector();
    }

    @Bean
    public TracingService tracingService() {
        return new TracingService();
    }
}
