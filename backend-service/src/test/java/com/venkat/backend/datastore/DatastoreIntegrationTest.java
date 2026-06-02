package com.venkat.backend.datastore;

import com.venkat.backend.datastore.nosql.Note;
import com.venkat.backend.datastore.nosql.NoteRepository;
import com.venkat.backend.datastore.sql.Article;
import com.venkat.backend.datastore.sql.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

/**
 * Integration tests for the datastore layer — requires real Postgres + MongoDB.
 *
 * To run:
 *   docker compose up -d postgres mongodb
 *   ./mvnw test -Dspring.profiles.active=datastore -Dgroups=integration
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("datastore")
@Tag("integration")
@Disabled("requires running infra (docker compose up -d postgres mongodb)")
class DatastoreIntegrationTest {

    @Autowired ArticleRepository articleRepo;
    @Autowired NoteRepository    noteRepo;

    @AfterEach
    void tearDown() {
        articleRepo.deleteAll().block();
        noteRepo.deleteAll().block();
    }

    @Test
    void sql_saveAndRetrieveArticle() {
        StepVerifier.create(
                articleRepo.save(Article.of("Hello R2DBC", "Content here", "alice"))
                           .flatMap(saved -> articleRepo.findById(saved.id()))
        )
                .expectNextMatches(a -> "Hello R2DBC".equals(a.title()) && a.id() != null)
                .verifyComplete();
    }

    @Test
    void sql_findByAuthor_returnsMatchingRows() {
        var save1 = articleRepo.save(Article.of("A1", "c", "bob"));
        var save2 = articleRepo.save(Article.of("A2", "c", "bob"));
        var save3 = articleRepo.save(Article.of("A3", "c", "alice"));

        StepVerifier.create(save1.then(save2).then(save3)
                .thenMany(articleRepo.findByAuthor("bob")))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void nosql_saveAndRetrieveNote() {
        StepVerifier.create(
                noteRepo.save(Note.of("Mongo Note", "body", "carol", List.of("nosql", "mongodb")))
                        .flatMap(saved -> noteRepo.findById(saved.id()))
        )
                .expectNextMatches(n -> "Mongo Note".equals(n.title()) && n.tags().contains("nosql"))
                .verifyComplete();
    }

    @Test
    void nosql_findByTag_matchesEmbeddedArray() {
        StepVerifier.create(
                noteRepo.save(Note.of("T1", "c", "alice", List.of("java", "spring")))
                        .then(noteRepo.save(Note.of("T2", "c", "alice", List.of("python"))))
                        .thenMany(noteRepo.findByTagsContaining("java"))
        )
                .expectNextMatches(n -> n.tags().contains("java"))
                .verifyComplete();
    }
}
