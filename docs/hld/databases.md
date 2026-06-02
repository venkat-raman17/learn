# Databases — SQL vs NoSQL

## When to use what

| Dimension | Relational (SQL) | Document (MongoDB) | Wide-column (Cassandra) | Key-Value (Redis) |
|---|---|---|---|---|
| Schema | Fixed, enforced | Flexible per document | Fixed rows, flexible cols | No schema |
| Relationships | JOINs, FK constraints | Embed or denormalise | Denormalise only | None |
| Transactions | ACID multi-row | ACID single-doc; multi-doc available | Eventual (LWT for lightweight tx) | Single-key atomic |
| Query | SQL (rich aggregations) | MQL (rich) | CQL (limited) | Key lookup only |
| Scale pattern | Vertical + read replicas | Horizontal sharding | Linear horizontal | Horizontal |
| Best for | ERP, billing, ledgers | Catalogs, CMS, user profiles | Time-series, event logs | Sessions, caches, leaderboards |

**Heuristic**: if you don't know the access patterns yet, start relational. You can always denormalise or move hot paths to a document store later.

---

## SQL: R2DBC (reactive JDBC)

### JDBC vs R2DBC

| | JDBC | R2DBC |
|---|---|---|
| Threading | Blocks the calling thread | Fully non-blocking (Reactor) |
| Connection pool | HikariCP | r2dbc-pool |
| Transaction | `@Transactional` (thread-bound) | `TransactionalOperator` (reactive) |
| Best with | Spring MVC (sync) | Spring WebFlux (async) |
| Maturity | Very mature | Stable since 2022 |

### Key R2DBC patterns

```java
// Entity — @Table on a record, @Id for the primary key, no proxying or lazy loading
@Table("articles")
public record Article(@Id Long id, String title, String content, String author) {}

// Repository — all methods return Mono/Flux, never blocks
public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {
    Flux<Article> findByAuthor(String author);

    @Query("SELECT * FROM articles WHERE author = :author ORDER BY created_at DESC LIMIT :limit")
    Flux<Article> findRecentByAuthor(String author, int limit);
}

// Service — chain reactive operators, no .block() in prod code
public Mono<Article> publish(String title, String content, String author) {
    return repo.save(Article.of(title, content, author))
               .doOnNext(a -> eventBus.emit("article.published", a.id()));
}
```

### Schema management

R2DBC has no DDL generation (unlike Hibernate). Use one of:
- **Flyway** (recommended for prod): SQL migrations in `db/migration/V1__*.sql`, runs synchronously at startup via JDBC (even in reactive apps).
- **Liquibase**: XML/YAML changelogs, same approach.
- **Spring schema.sql**: set `spring.sql.init.mode=always` — quick for demos, not for prod.
- **`ConnectionFactoryInitializer`**: run a `schema.sql` on startup reactively (used in this repo's `DatastoreConfig`).

### Running the sample

```bash
docker compose up -d postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=datastore

# CRUD
curl -X POST http://localhost:8080/api/articles \
     -H 'Content-Type: application/json' \
     -d '{"title":"Hello R2DBC","content":"reactive SQL","author":"alice"}'
curl http://localhost:8080/api/articles
curl -X DELETE http://localhost:8080/api/articles/1
```

---

## NoSQL: MongoDB (reactive)

### Document model key ideas

- **Embed vs reference**: embed when you always read the child with the parent (no JOIN cost); reference when the child has independent lifecycle or is large.
- **Schema-less ≠ schema-free**: design your schema for your query patterns. Random fields on documents is an anti-pattern.
- **Arrays as first-class**: `tags`, `comments`, `prices` are naturally embedded arrays — no join table needed.
- **Indexes**: MongoDB uses BSON B-trees. Always index fields in `find()` filters. Compound index order: equality → sort → range.

### Spring Data MongoDB reactive

```java
// Document — @Document maps to a collection; @Indexed creates an index
@Document(collection = "notes")
public record Note(@Id String id, String title, String content,
                   @Indexed String author, List<String> tags) {}

// Repository — derives queries from method names, supports @Query with MQL JSON
public interface NoteRepository extends ReactiveMongoRepository<Note, String> {
    Flux<Note> findByTagsContaining(String tag);     // $elemMatch on array

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    Flux<Note> findByTitlePattern(String pattern);   // raw MQL when derivation falls short
}
```

### MongoDB vs Postgres — which to use when

| Use MongoDB when... | Use Postgres when... |
|---|---|
| Document structure varies per record | Every row has the same shape |
| Sub-documents are always read together | Data is highly relational (lots of JOINs) |
| You need horizontal sharding from day 1 | You need strong ACID multi-table transactions |
| Rapid schema evolution | Schema is stable and well-known |
| Rich text / blob storage alongside metadata | Numeric / aggregate queries dominate |

### Running the sample

```bash
docker compose up -d mongodb
./mvnw spring-boot:run -Dspring-boot.run.profiles=datastore

curl -X POST http://localhost:8080/api/notes \
     -H 'Content-Type: application/json' \
     -d '{"title":"Mongo Note","content":"embedded tags","author":"bob","tags":["nosql","mongodb"]}'
curl http://localhost:8080/api/notes
curl http://localhost:8080/api/notes/tag/nosql
```

---

## CAP theorem quick reference

| System | Consistent | Available | Partition-tolerant | Notes |
|---|---|---|---|---|
| Postgres (single) | ✅ | ✅ | ❌ | CP — halts on network split |
| Postgres (Citus/replica) | ✅ | partial | ✅ | CP with tunable lag |
| MongoDB (replica set) | ✅ | partial | ✅ | CP by default (majority write concern) |
| MongoDB (eventual) | ❌ | ✅ | ✅ | AP if you relax write concern |
| Cassandra | ❌ | ✅ | ✅ | AP — tunable consistency per query |
| Redis Cluster | partial | ✅ | ✅ | AP — async replication |

---

## Interview talking points

1. **N+1 query problem** — occurs with lazy-loaded ORM associations; fix with JOIN FETCH, batch size, or denormalise to embed.
2. **Optimistic locking** (`@Version` in JPA / `findAndModify` in Mongo) vs pessimistic (SELECT FOR UPDATE).
3. **Connection pool sizing**: `(core_count × 2) + disk_spindles` is the classic Hikari recommendation; too large a pool hurts more than helps.
4. **Read replicas**: route read-heavy traffic to replicas; be aware of replication lag for consistency-sensitive reads.
5. **Database per service** (DDD microservices): each service owns its schema; cross-service data access via API or event, never shared DB.
