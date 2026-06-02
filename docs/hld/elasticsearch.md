# Elasticsearch — Full-Text & Vector Search

## What it is

Elasticsearch (ES) is a distributed search and analytics engine built on Apache Lucene. It stores JSON documents in an **inverted index** that supports millisecond full-text queries across billions of records. It's also converging into a **vector database** (kNN search) for AI-powered retrieval.

Running locally: `docker compose up -d elasticsearch` (see `infra/`).

---

## Core concepts

### Index, shard, replica

```
Index "articles"
  ├── Shard 0 (primary)  ──→  Replica shard 0
  ├── Shard 1 (primary)  ──→  Replica shard 1
  └── Shard 2 (primary)  ──→  Replica shard 2
```

- **Index**: logical namespace for a collection of documents (like a SQL table).
- **Shard**: a Lucene index instance; unit of parallelism and distribution. Default: 1 primary shard.
- **Replica**: a copy of a shard; provides HA and increases read throughput.
- Rule of thumb: shard size 10-50 GB; aim for ≤20 shards per node.

### Inverted index

```
Term      → Document IDs
"reactive" → [1, 3, 7]
"spring"   → [1, 2, 4]
"kafka"    → [2, 5]
```

A full-text query for "reactive spring" expands, analyses (lowercase, stem), then intersects posting lists. O(log N) per term — extremely fast.

### Mapping (schema)

ES auto-detects types but you should define explicit mappings for production:

```json
PUT /articles
{
  "mappings": {
    "properties": {
      "title":    { "type": "text",    "analyzer": "english" },
      "content":  { "type": "text",    "analyzer": "english" },
      "author":   { "type": "keyword" },
      "tags":     { "type": "keyword" },
      "created":  { "type": "date" },
      "embedding":{ "type": "dense_vector", "dims": 768, "index": true, "similarity": "cosine" }
    }
  }
}
```

- **`text`**: analysed (tokenised, lowercased, stemmed) — for full-text search.
- **`keyword`**: exact match — for filtering, aggregations, sorting.
- **`dense_vector`**: for kNN vector similarity search (RAG retrieval).

---

## Query types

### Full-text (BM25)

BM25 (Best Match 25) is ES's default relevance algorithm — a term-frequency / inverse-document-frequency variant that saturates TF and normalises for document length.

```json
GET /articles/_search
{
  "query": {
    "multi_match": {
      "query": "reactive programming spring",
      "fields": ["title^3", "content"],
      "type": "best_fields",
      "fuzziness": "AUTO"
    }
  }
}
```

- `^3`: boost title matches 3×.
- `fuzziness: AUTO`: edit-distance tolerance for typos.

### Filter (exact, cached)

```json
{
  "query": {
    "bool": {
      "must":   [{ "match": { "content": "kafka" } }],
      "filter": [
        { "term":  { "author": "alice" } },
        { "range": { "created": { "gte": "now-7d" } } }
      ]
    }
  }
}
```

Filters are **cached** (bitsets) and not scored — combine with `must` for relevance + filter.

### Aggregations (analytics)

```json
GET /articles/_search
{
  "size": 0,
  "aggs": {
    "by_author": {
      "terms": { "field": "author", "size": 10 },
      "aggs": {
        "monthly": { "date_histogram": { "field": "created", "calendar_interval": "month" } }
      }
    }
  }
}
```

Comparable to SQL `GROUP BY` + sub-group. No JOINs needed — denormalise the author name into the document at index time.

### kNN vector search (semantic / RAG)

```json
GET /articles/_search
{
  "knn": {
    "field": "embedding",
    "query_vector": [0.12, -0.43, ...],   // embed the user query first
    "k": 10,
    "num_candidates": 100
  },
  "_source": ["title", "content", "author"]
}
```

Used in **RAG** pipelines: embed the user question → kNN retrieve top-k passages → feed to LLM.

### Hybrid search (BM25 + kNN)

```json
{
  "query": { "multi_match": { "query": "reactive kafka", "fields": ["title", "content"] } },
  "knn":   { "field": "embedding", "query_vector": [...], "k": 10, "num_candidates": 50, "boost": 0.5 }
}
```

Scores are combined. Hybrid search outperforms either alone on most benchmarks — a key trend in production RAG systems.

---

## Spring Boot integration

Add to `backend-service/pom.xml` (and activate with `datastore` profile):

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

Application config (`application-datastore.yaml`):
```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
```

Entity + repository pattern (same Spring Data conventions):

```java
@Document(indexName = "articles")
public record ArticleIndex(
    @Id       String id,
    @Field(type = FieldType.Text,    analyzer = "english") String title,
    @Field(type = FieldType.Text,    analyzer = "english") String content,
    @Field(type = FieldType.Keyword) String author,
    @Field(type = FieldType.Date)    Instant created
) {}

public interface ArticleSearchRepository
        extends ReactiveElasticsearchRepository<ArticleIndex, String> {
    Flux<ArticleIndex> findByAuthor(String author);
    Flux<ArticleIndex> findByTitleContaining(String term);
}
```

For complex queries (BM25 + kNN), use `ReactiveElasticsearchClient` directly with `NativeQuery`.

---

## When to use Elasticsearch vs alternatives

| Option | Latency | Full-text | Vector | Ops cost | Best for |
|---|---|---|---|---|---|
| **Elasticsearch** | ~10ms | Excellent (BM25) | Yes (kNN) | Medium | Mixed full-text + analytics + vector |
| **Postgres FTS** (`tsvector`) | ~50ms | Good | pgvector plugin | Low | Simple FTS in existing Postgres stack |
| **Typesense** | ~5ms | Great | Yes | Low | Developer-friendly, simple FTS |
| **Meilisearch** | ~2ms | Great | Limited | Low | Consumer search, instant results |
| **Pinecone / Weaviate** | ~20ms | No | Excellent | SaaS | Pure vector / RAG at scale |
| **OpenSearch** | ~10ms | Excellent | Yes | Medium | ES alternative (open-source fork) |

**Rule of thumb**:
- Already on Postgres + simple search → `tsvector` + `gin` index.
- Need relevance tuning, aggregations, or Kibana dashboards → Elasticsearch.
- Pure RAG retrieval (semantic only) → Pinecone/Weaviate or pgvector.
- Hybrid search at scale → Elasticsearch 8.x with both `_source` field and `dense_vector`.

---

## Production checklist

- [ ] Explicit mappings (no dynamic mapping in prod — cardinality explosions).
- [ ] `index.number_of_replicas ≥ 1` for HA.
- [ ] ILM (Index Lifecycle Management) policy to roll over hot → warm → cold → delete.
- [ ] `refresh_interval: 30s` on write-heavy indices (default 1s is expensive).
- [ ] Circuit breaker + `indices.breaker.total.limit` to prevent OOM.
- [ ] Snapshot repository (S3/GCS) for disaster recovery.
- [ ] Monitor: JVM heap > 75% → shard pressure; search rejected → add nodes or reduce shards.

---

## Interview talking points

1. **Inverted index vs B-tree**: ES excels at "find all docs containing word X"; Postgres B-tree excels at range queries and equality.
2. **Near-real-time**: ES uses segment merging — newly indexed docs appear after `refresh` (default 1s). Not truly real-time; use DB + CDC for strict consistency.
3. **No transactions**: ES is eventually consistent across shards. Not a replacement for a source-of-truth DB.
4. **Dual-write problem**: keeping ES in sync with a DB — use Debezium CDC to capture DB changes and stream to ES rather than dual-writing from the app.
5. **The N+1 of search**: deep pagination (`from+size`) is O(N) across all shards; use `search_after` (keyset pagination) for deep pages.
6. **RAG retrieval choice**: BM25 wins on keyword-heavy domains; dense vector wins on semantic queries; hybrid reranking (BM25 + vector + cross-encoder) wins generally.
