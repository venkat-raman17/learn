package com.venkat.backend.datastore.sql;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * Reactive relational entity mapped to the `articles` table via Spring Data R2DBC.
 *
 * Key contrasts with JPA:
 * - No lazy loading (everything is explicit Mono/Flux).
 * - No entity-level relationships (@OneToMany etc.) — model as document or use explicit JOINs.
 * - Schema managed separately (schema.sql / Flyway), not via @Column DDL generation.
 * - Thread-safe and non-blocking end-to-end (R2DBC driver + Project Reactor).
 */
@Table("articles")
public record Article(
        @Id                          Long    id,
        String                       title,
        String                       content,
        String                       author,
        @CreatedDate                 Instant createdAt,
        @LastModifiedDate            Instant updatedAt
) {
    /** Factory for new (unsaved) articles — id/timestamps set by the DB. */
    public static Article of(String title, String content, String author) {
        return new Article(null, title, content, author, null, null);
    }
}
