# infra

Local backing services (Docker Compose) for the **HLD / event-driven** hands-on work — used by
[`../backend-service/`](../backend-service/) and the capstone.

| Service | Image | Host port | Purpose |
| --- | --- | --- | --- |
| Postgres | `postgres:16-alpine` | 5432 | Relational SQL — R2DBC + JDBC samples |
| Redis | `redis:7-alpine` | 6379 | Cache / rate-limit |
| Kafka | `apache/kafka:latest` | **29092** | Event streaming (KRaft, no ZooKeeper) |
| Kafka UI | `kafbat/kafka-ui` | 8081 | Browse topics — http://localhost:8081 |
| **MongoDB** | `mongo:7` | **27017** | Document store — MongoDB Reactive samples |
| **Mongo Express** | `mongo-express` | **8082** | MongoDB UI — http://localhost:8082 |
| **Elasticsearch** | `elasticsearch:8.17.0` | **9200** | Full-text / vector search |

> Kafka from the host: use **localhost:29092** (the internal `kafka:9092` listener is for other
> containers). Security is disabled on Elasticsearch for local dev — never do this in production.

## Run

```bash
docker compose up -d          # start all services
docker compose ps             # check health
docker compose logs -f        # stream logs

# Selective start (e.g. only the datastores)
docker compose up -d postgres mongodb elasticsearch

docker compose down           # stop (add -v to wipe volumes)
```

## Quick-smoke checks

```bash
# Postgres
psql -h localhost -U app -d appdb -c "SELECT version();"

# MongoDB
mongosh --eval "db.adminCommand('ping')"

# Elasticsearch
curl http://localhost:9200/_cluster/health?pretty

# Kafka — produce + consume a test message
docker exec learn-kafka \
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

## Spring Boot connection strings

| Store | Property | Value |
| --- | --- | --- |
| Postgres (R2DBC) | `spring.r2dbc.url` | `r2dbc:postgresql://localhost:5432/appdb` |
| Postgres (JDBC) | `spring.datasource.url` | `jdbc:postgresql://localhost:5432/appdb` |
| MongoDB | `spring.data.mongodb.uri` | `mongodb://localhost:27017/appdb` |
| Redis | `spring.data.redis.host` | `localhost` (port 6379) |
| Elasticsearch | `spring.elasticsearch.uris` | `http://localhost:9200` |
| Kafka | `spring.kafka.bootstrap-servers` | `localhost:29092` |

> Credentials for Postgres: user `app` / password `app_password` (override via `.env` file).
