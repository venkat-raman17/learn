# backend-service

Spring Boot service for **backend LLD/HLD** practice — REST APIs, event-driven (Kafka) flows, and
database design. Used in Phases 3–4 of [`../STUDY-PLAN.md`](../STUDY-PLAN.md).

- **Stack:** Java 21, Spring Boot 4.0.6 (WebFlux + springdoc/Swagger today; Spring Kafka, Spring
  Data JPA, and Testcontainers added when the LLD/HLD phases need them).
- **Package:** `com.venkat.backend`
- **Pairs with:** [`../infra/`](../infra/) (Postgres, Redis, Kafka via Docker Compose).

## Run

```powershell
.\mvnw.cmd test          # context-load test
.\mvnw.cmd spring-boot:run
```

Swagger UI (when running): `http://localhost:8080/swagger-ui.html`.

> Status: 🟡 scaffold — a health endpoint and the first event-driven slice land in Phase 4.
