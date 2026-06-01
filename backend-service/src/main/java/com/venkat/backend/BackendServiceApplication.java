package com.venkat.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Backend practice service: REST APIs, event-driven (Kafka) flows, and DB design for the
 * LLD/HLD phases. Kept intentionally minimal for now; Spring Kafka, Spring Data JPA, and
 * Testcontainers are added when Phase 3/4 needs them.
 */
@SpringBootApplication
public class BackendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendServiceApplication.class, args);
    }
}
