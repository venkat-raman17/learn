package com.venkat.backend.datastore.nosql;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive MongoDB repository for {@link Note}.
 *
 * MongoDB query derivation follows the same Spring Data conventions as JPA, but uses
 * MongoDB's query operators under the hood.  @Query accepts MongoDB JSON query syntax.
 */
public interface NoteRepository extends ReactiveCrudRepository<Note, String> {

    Flux<Note> findByAuthor(String author);

    /** Match any note whose tags list contains the given tag (MongoDB $in style). */
    Flux<Note> findByTagsContaining(String tag);

    /** MongoDB JSON query — demonstrates escaping to raw driver syntax when derivation falls short. */
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    Flux<Note> findByTitlePattern(String pattern);
}
