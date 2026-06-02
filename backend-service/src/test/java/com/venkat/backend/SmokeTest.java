package com.venkat.backend;

import com.venkat.backend.lld.observability.MetricsCollector;
import com.venkat.backend.lld.observability.TracingService;
import com.venkat.backend.ops.OpsController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SMOKE TEST — the fast "is it fundamentally alive?" gate you run before the deeper suite (and as a
 * post-deploy check). It boots the Spring context (no web server: fastest) and asserts the critical
 * beans wired up. If this fails, nothing else is worth running.
 *
 * <p>Tagged {@code "smoke"} so it can be selected on its own:
 * <pre>{@code ./mvnw test -Dgroups=smoke}</pre>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Tag("smoke")
class SmokeTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads_withCriticalBeans() {
        assertNotNull(context.getBean(MetricsCollector.class), "observability metrics bean");
        assertNotNull(context.getBean(TracingService.class), "tracing bean");
        assertNotNull(context.getBean(OpsController.class), "ops endpoint");
    }
}
