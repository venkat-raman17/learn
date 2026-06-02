package com.venkat.backend.ops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * End-to-end check of the operational layer: the custom health indicator, the demo work endpoint,
 * and that the work shows up in both the Micrometer metrics endpoint and the Prometheus scrape.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
class OpsEndpointsTest {

    @LocalServerPort
    private int port;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Test
    void health_isUp_withCustomReadinessDetail() {
        client.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP")
                // detail contributed by DatastoreHealthIndicator (component key = "datastore")
                .jsonPath("$.components.datastore.details.dependency").isEqualTo("primary datastore");
    }

    @Test
    void work_returnsTraceId_andTraceIsRetrievable() {
        @SuppressWarnings("unchecked")
        Map<String, Object> body = client.get().uri("/api/ops/work?ms=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(body, "work response body");
        String traceId = (String) body.get("traceId");
        assertNotNull(traceId, "traceId present in response");

        // The kata trace store should now contain the span for that trace.
        client.get().uri("/api/ops/trace/{id}", traceId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].traceId").isEqualTo(traceId);
    }

    @Test
    void work_incrementsMicrometerCounter() {
        client.get().uri("/api/ops/work?ms=1").exchange().expectStatus().isOk();

        client.get().uri("/actuator/metrics/ops.requests")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("ops.requests")
                .jsonPath("$.measurements[0].value").value(v ->
                        assertTrue(((Number) v).doubleValue() >= 1.0, "counter should be >= 1"));
    }

    @Test
    void prometheus_exposesOpsRequestsTotal() {
        client.get().uri("/api/ops/work?ms=1").exchange().expectStatus().isOk();

        client.get().uri("/actuator/prometheus")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertTrue(body.contains("ops_requests_total"),
                        "prometheus scrape should expose ops_requests_total"));
    }
}
