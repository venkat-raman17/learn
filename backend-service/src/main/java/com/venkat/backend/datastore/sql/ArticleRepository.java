package com.venkat.backend.datastore.sql;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive CRUD repository for {@link Article}.
 *
 * Spring Data generates the implementation at runtime.  All methods return Reactor types
 * (Mono/Flux) — the caller never blocks.  Custom queries use @Query (R2DBC dialect SQL).
 */
public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {

    Flux<Article> findByAuthor(String author);

    Mono<Article> findFirstByTitleContainingIgnoreCase(String titleFragment);

    @Query("SELECT * FROM articles WHERE author = :author ORDER BY created_at DESC LIMIT :limit")
    Flux<Article> findRecentByAuthor(String author, int limit);
}
