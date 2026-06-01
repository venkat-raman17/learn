# infra

Local backing services (Docker Compose) for the **HLD / event-driven** hands-on work in Phase 4 —
used by [`../backend-service/`](../backend-service/).

| Service | Image | Host port | Notes |
| --- | --- | --- | --- |
| Postgres | `postgres:16-alpine` | 5432 | user/pass/db from `.env` |
| Redis | `redis:7-alpine` | 6379 | cache / rate-limit |
| Kafka | `apache/kafka:latest` | 29092 | KRaft (no ZooKeeper). Apps use **localhost:29092** |
| Kafka UI | `kafbat/kafka-ui` | 8081 | http://localhost:8081 |

## Run

```powershell
Copy-Item .env.example .env      # first time
docker compose up -d
docker compose ps                # check health
docker compose down              # stop (add -v to wipe volumes)
```

> Kafka on the host is **localhost:29092** (the internal `kafka:9092` listener is for other
> containers like Kafka UI). Kafka UI is on **8081** to avoid clashing with backend-service's 8080.
