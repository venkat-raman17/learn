package com.venkat.backend.datastore;

import com.venkat.backend.datastore.nosql.Note;
import com.venkat.backend.datastore.nosql.NoteRepository;
import com.venkat.backend.datastore.sql.Article;
import com.venkat.backend.datastore.sql.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the datastore layer — all mocked, no infra required.
 *
 * Uses reactor-test's StepVerifier to assert reactive stream behavior synchronously.
 * Integration tests that exercise real Postgres + MongoDB are in DatastoreIntegrationTest
 * (require docker compose up -d; run with -Dgroups=integration).
 */
class DatastoreUnitTest {

    private ArticleRepository articleRepo;
    private NoteRepository    noteRepo;
    private DatastoreController controller;

    @BeforeEach
    void setUp() {
        articleRepo = mock(ArticleRepository.class);
        noteRepo    = mock(NoteRepository.class);
        controller  = new DatastoreController(articleRepo, noteRepo);
    }

    // ── SQL (R2DBC Article) tests ────────────────────────────────────────────

    @Nested
    class ArticleTests {

        @Test
        void listArticles_returnsAllFromRepository() {
            var a1 = new Article(1L, "Title A", "Content A", "alice", Instant.now(), Instant.now());
            var a2 = new Article(2L, "Title B", "Content B", "bob",   Instant.now(), Instant.now());
            when(articleRepo.findAll()).thenReturn(Flux.just(a1, a2));

            StepVerifier.create(controller.listArticles())
                    .expectNext(a1)
                    .expectNext(a2)
                    .verifyComplete();
        }

        @Test
        void createArticle_savesAndReturnsSavedEntity() {
            var saved = new Article(42L, "T", "C", "alice", Instant.now(), Instant.now());
            when(articleRepo.save(any())).thenReturn(Mono.just(saved));

            var body = Map.of("title", "T", "content", "C", "author", "alice");
            StepVerifier.create(controller.createArticle(body))
                    .expectNextMatches(a -> a.id() == 42L && "alice".equals(a.author()))
                    .verifyComplete();

            verify(articleRepo).save(argThat(a ->
                    "T".equals(a.title()) && a.id() == null)); // id null before save
        }

        @Test
        void deleteArticle_delegatesToRepository() {
            when(articleRepo.deleteById(7L)).thenReturn(Mono.empty());

            StepVerifier.create(controller.deleteArticle(7L))
                    .verifyComplete();

            verify(articleRepo).deleteById(7L);
        }

        @Test
        void articleFactory_setsNullIdAndTimestamps() {
            var a = Article.of("T", "C", "author");
            assert a.id() == null;
            assert a.createdAt() == null;
            assert a.updatedAt() == null;
        }
    }

    // ── NoSQL (MongoDB Note) tests ────────────────────────────────────────────

    @Nested
    class NoteTests {

        @Test
        void listNotes_returnsAllFromRepository() {
            var n = new Note("id1", "Note A", "body", "alice", List.of("java", "spring"),
                    Instant.now(), Instant.now());
            when(noteRepo.findAll()).thenReturn(Flux.just(n));

            StepVerifier.create(controller.listNotes())
                    .expectNext(n)
                    .verifyComplete();
        }

        @Test
        void createNote_embedsTagsInDocument() {
            var saved = new Note("id2", "T", "C", "bob", List.of("tag1", "tag2"),
                    Instant.now(), Instant.now());
            when(noteRepo.save(any())).thenReturn(Mono.just(saved));

            var body = Map.<String, Object>of(
                    "title", "T", "content", "C", "author", "bob",
                    "tags", List.of("tag1", "tag2"));

            StepVerifier.create(controller.createNote(body))
                    .expectNextMatches(n -> n.tags().contains("tag1") && n.tags().contains("tag2"))
                    .verifyComplete();
        }

        @Test
        void notesByTag_delegatesToRepositoryMethod() {
            var n = new Note("id3", "T", "C", "alice", List.of("spring"), Instant.now(), Instant.now());
            when(noteRepo.findByTagsContaining("spring")).thenReturn(Flux.just(n));

            StepVerifier.create(controller.notesByTag("spring"))
                    .expectNext(n)
                    .verifyComplete();

            verify(noteRepo).findByTagsContaining("spring");
        }

        @Test
        void noteFactory_setsNullIdAndTimestamps() {
            var n = Note.of("T", "C", "author", List.of("a"));
            assert n.id() == null;
            assert n.createdAt() == null;
        }
    }
}
