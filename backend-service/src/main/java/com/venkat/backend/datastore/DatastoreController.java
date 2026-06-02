package com.venkat.backend.datastore;

import com.venkat.backend.datastore.nosql.Note;
import com.venkat.backend.datastore.nosql.NoteRepository;
import com.venkat.backend.datastore.sql.Article;
import com.venkat.backend.datastore.sql.ArticleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * REST demo: compares SQL (R2DBC Postgres) and NoSQL (MongoDB) side-by-side on the same
 * resource shape.  Both paths are fully non-blocking (Reactor Mono/Flux throughout).
 *
 * Endpoints:
 *   GET  /api/articles          → list all (SQL)
 *   POST /api/articles          → create (SQL)
 *   GET  /api/notes             → list all (NoSQL)
 *   POST /api/notes             → create (NoSQL)
 *   GET  /api/notes/tag/{tag}   → filter by tag (demonstrates MongoDB array query)
 *
 * Active only with the `datastore` Spring profile (see DatastoreConfig).
 */
@RestController
@Profile("datastore")
public class DatastoreController {

    private final ArticleRepository articleRepo;
    private final NoteRepository    noteRepo;

    DatastoreController(ArticleRepository articleRepo, NoteRepository noteRepo) {
        this.articleRepo = articleRepo;
        this.noteRepo    = noteRepo;
    }

    // ── SQL (R2DBC / Postgres) ───────────────────────────────────────────────

    @GetMapping("/api/articles")
    public Flux<Article> listArticles() {
        return articleRepo.findAll();
    }

    @PostMapping("/api/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Article> createArticle(@RequestBody Map<String, String> body) {
        return articleRepo.save(
                Article.of(body.get("title"), body.get("content"), body.get("author")));
    }

    @GetMapping("/api/articles/{id}")
    public Mono<Article> getArticle(@PathVariable Long id) {
        return articleRepo.findById(id);
    }

    @DeleteMapping("/api/articles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteArticle(@PathVariable Long id) {
        return articleRepo.deleteById(id);
    }

    // ── NoSQL (MongoDB reactive) ─────────────────────────────────────────────

    @GetMapping("/api/notes")
    public Flux<Note> listNotes() {
        return noteRepo.findAll();
    }

    @PostMapping("/api/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Note> createNote(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> tags = body.containsKey("tags")
                ? (List<String>) body.get("tags")
                : List.of();
        return noteRepo.save(Note.of(
                (String) body.get("title"),
                (String) body.get("content"),
                (String) body.get("author"),
                tags));
    }

    @GetMapping("/api/notes/tag/{tag}")
    public Flux<Note> notesByTag(@PathVariable String tag) {
        return noteRepo.findByTagsContaining(tag);
    }

    @DeleteMapping("/api/notes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteNote(@PathVariable String id) {
        return noteRepo.deleteById(id);
    }
}
