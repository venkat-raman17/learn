package com.venkat.backend.ops;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * A custom readiness check, contributed into {@code /actuator/health} (and the
 * {@code /actuator/health/readiness} probe group).
 *
 * <p>This is the hook a real service uses to gate traffic: a load balancer or Kubernetes readiness
 * probe pulls a pod <em>out of rotation</em> when this reports {@code DOWN}, without killing it.
 * In production the check would ping critical dependencies (DB pool, cache, downstream API); here it
 * stays in-memory so the kata has no external requirements. Flip {@link #setDependencyReady} to see
 * the endpoint report {@code OUT_OF_SERVICE}.
 *
 * <p>The bean name yields the health component key {@code datastore} (avoiding a clash with the
 * built-in {@code readiness} probe group). {@link HealthIndicator} is blocking; Spring Boot adapts
 * it onto the reactive health endpoint automatically, so it works unchanged under WebFlux.
 */
@Component("datastore")
public class DatastoreHealthIndicator implements HealthIndicator {

    private volatile boolean dependencyReady = true;

    public void setDependencyReady(boolean ready) {
        this.dependencyReady = ready;
    }

    @Override
    public Health health() {
        if (!dependencyReady) {
            return Health.outOfService()
                    .withDetail("dependency", "primary datastore")
                    .withDetail("reason", "simulated not-ready")
                    .build();
        }
        return Health.up()
                .withDetail("dependency", "primary datastore")
                .withDetail("checkedVia", "in-memory readiness probe")
                .build();
    }
}
