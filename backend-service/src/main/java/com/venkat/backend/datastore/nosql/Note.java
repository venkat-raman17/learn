package com.venkat.backend.datastore.nosql;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB document mapped to the `notes` collection.
 *
 * Key contrasts with the relational Article:
 * - Schema-less: fields can vary per document; embedded lists (tags) are natural.
 * - String-based ID (BSON ObjectId as hex string) rather than a DB sequence.
 * - Rich nested structure (sub-documents, arrays) without JOINs.
 * - Auto-index-creation enabled in the datastore profile for @Indexed fields.
 */
@Document(collection = "notes")
public record Note(
        @Id                          String       id,
        String                       title,
        String                       content,
        @Indexed                     String       author,
        List<String>                 tags,
        @CreatedDate                 Instant      createdAt,
        @LastModifiedDate            Instant      updatedAt
) {
    public static Note of(String title, String content, String author, List<String> tags) {
        return new Note(null, title, content, author, tags, null, null);
    }
}
