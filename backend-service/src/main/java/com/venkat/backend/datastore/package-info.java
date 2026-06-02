/**
 * Datastore demo: SQL (R2DBC / Postgres) vs NoSQL (MongoDB Reactive) side-by-side.
 *
 * <h2>Problem statement</h2>
 * Show the Spring Data programming model for two different persistence paradigms — both
 * fully reactive (non-blocking Mono/Flux) — so you can compare them on a Staff interview.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@code sql/}   — {@code Article}: relational entity, R2DBC CRUD + custom @Query</li>
 *   <li>{@code nosql/} — {@code Note}: MongoDB document, embedded tags array, index</li>
 * </ul>
 *
 * <h2>How to run</h2>
 * <pre>
 *   docker compose up -d postgres mongodb
 *   ./mvnw spring-boot:run -Dspring-boot.run.profiles=datastore
 *   # then hit http://localhost:8080/api/articles and /api/notes
 * </pre>
 *
 * <h2>SQL vs NoSQL key contrasts</h2>
 * <table>
 *   <tr><th>Dimension</th><th>R2DBC / Postgres</th><th>MongoDB Reactive</th></tr>
 *   <tr><td>Schema</td><td>Fixed; DDL via schema.sql</td><td>Schema-less; structure per document</td></tr>
 *   <tr><td>Relationships</td><td>JOINs, foreign keys</td><td>Embed or @DBRef (avoid)</td></tr>
 *   <tr><td>ID</td><td>BIGSERIAL sequence (Long)</td><td>BSON ObjectId (String)</td></tr>
 *   <tr><td>Arrays</td><td>Normalise to a join table</td><td>Native embedded arrays (tags)</td></tr>
 *   <tr><td>Transactions</td><td>ACID, multi-table</td><td>Multi-doc tx available but costly</td></tr>
 *   <tr><td>Query language</td><td>SQL (R2DBC dialect)</td><td>MongoDB JSON operators</td></tr>
 * </table>
 *
 * <h2>Follow-ups (HLD discussion)</h2>
 * <ul>
 *   <li>R2DBC vs JDBC: when does non-blocking matter? (high-concurrency fan-out, no CPU work)</li>
 *   <li>Connection pooling: r2dbc-pool vs HikariCP trade-offs</li>
 *   <li>MongoDB Atlas vs self-hosted: ops overhead, change streams for CDC</li>
 *   <li>Schema evolution: Flyway for SQL; document versioning patterns for Mongo</li>
 *   <li>Read replicas, sharding, partition tolerance (CAP theorem for each)</li>
 * </ul>
 */
package com.venkat.backend.datastore;
