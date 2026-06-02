# graphql-layer

Apollo GraphQL full stack: **schema-first** API server (Apollo Server 4 + Node.js + TypeScript) wired to a **React client** (Apollo Client 3 + Vite). No database required — in-memory store keeps it self-contained.

## Architecture

```
React + Apollo Client ──(HTTP/GraphQL)──► Apollo Server 4
   InMemoryCache                           │
   useQuery / useMutation                  ├── schema.ts   (type definitions)
   useLazyQuery                            ├── resolvers.ts (implementations)
   Fragments                               └── in-memory store (swap for DB in prod)
```

Data flow:
1. Client sends a **typed GraphQL operation** (`query GetArticles { articles { id title } }`).
2. Server validates it against the schema, then calls the matching resolver.
3. Resolver fetches data (in-memory map → swap for R2DBC / MongoDB in prod).
4. Client caches the normalised response by `__typename + id`.

## Run

```bash
# Terminal 1 — GraphQL server (port 4000)
cd server
npm install
npm run dev
# → Apollo Sandbox at http://localhost:4000 (built-in query explorer)

# Terminal 2 — React client (port 5174, proxied to server)
cd client
npm install
npm run dev
# → http://localhost:5174
```

## Schema

```graphql
type Article {
  id: ID!; title: String!; content: String!; author: String!
  tags: [String!]!; createdAt: String!
}

type Query {
  articles: [Article!]!
  article(id: ID!): Article
  articlesByAuthor(author: String!): [Article!]!
  search(query: String!): [Article!]!
}

type Mutation {
  createArticle(input: CreateArticleInput!): Article!
  updateArticle(id: ID!, input: UpdateArticleInput!): Article
  deleteArticle(id: ID!): Boolean!
}
```

## Key GraphQL concepts demonstrated

### Queries vs REST

| | REST | GraphQL |
|---|---|---|
| Endpoint per resource | Many (`/articles`, `/articles/:id`, `/users/:id/articles`) | One (`/graphql`) |
| Response shape | Fixed by server | Declared by client |
| Over-fetching | Common | Eliminated — ask for exactly what you need |
| Under-fetching (N+1) | Common (multiple round-trips) | Solved with DataLoader or joins |
| Type system | OpenAPI (optional) | Built-in, enforced |

### Apollo InMemoryCache normalisation

Apollo normalises by `__typename + id`. Mutations that return the same entity **automatically update all queries** that show that entity — no manual cache invalidation needed:

```typescript
// Create returns Article { id: "3", title: "New", ... }
// → cache stores it at Article:3
// → GET_ARTICLES query result is automatically updated with the new entry
```

### N+1 problem and DataLoader

In a relational schema (e.g. `author: User` on `Article`), fetching 10 articles would trigger 10 separate `User` lookups. DataLoader batches them into one:

```typescript
// Without DataLoader: 10 articles → 10 SELECT * FROM users WHERE id = ?
// With DataLoader:    10 articles → 1 SELECT * FROM users WHERE id IN (1..10)

import DataLoader from 'dataloader';
const userLoader = new DataLoader(ids => UserDB.findManyByIds(ids));
// Pass through context: { req, userLoader }
```

### Cache update strategies

| Strategy | When | How |
|---|---|---|
| `refetchQueries` | Simple, correctness over perf | Re-execute queries after mutation |
| `update` (cache.modify) | Perf-sensitive, large lists | Surgically remove/add from cache |
| Optimistic UI | Low-latency UX | Assume success, rollback on error |

### Connecting to the Spring Boot backend

To use the `backend-service` as the data source instead of in-memory:

```typescript
// resolvers.ts — replace the Map store with HTTP calls to backend-service
import fetch from 'node-fetch';

const BASE = 'http://localhost:8080';

articles: async () => {
  const res = await fetch(`${BASE}/api/articles`);
  return res.json();
},

createArticle: async (_, { input }) => {
  const res = await fetch(`${BASE}/api/articles`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return res.json();
},
```

Or federate: Apollo Federation lets you split the schema across multiple subgraph services and compose them into a supergraph — each microservice owns its slice of the schema.

### Subscriptions (real-time)

GraphQL Subscriptions push server events to connected clients via WebSocket. Add to this project:

```bash
npm install graphql-ws ws express @apollo/server
```

```typescript
// schema.ts
type Subscription {
  articleCreated: Article!
}

// resolvers.ts
import { PubSub } from 'graphql-subscriptions';
const pubsub = new PubSub();

Mutation: {
  createArticle: (_, { input }) => {
    const article = store.save(input);
    pubsub.publish('ARTICLE_CREATED', { articleCreated: article }); // notify subscribers
    return article;
  }
},
Subscription: {
  articleCreated: { subscribe: () => pubsub.asyncIterableIterator('ARTICLE_CREATED') }
}
```

Use case: live dashboards, collaborative editors, chat — the `capstone/` TinyLink analytics board uses this pattern.

## Interview talking points

1. **GraphQL vs REST vs gRPC**: GraphQL wins on complex, multi-client APIs (mobile vs web need different shapes). gRPC wins on internal service-to-service (binary, streaming, codegen contracts). REST wins on simplicity and HTTP cache-ability.
2. **Schema-first vs code-first**: schema-first (SDL) makes the contract the source of truth; code-first (TypeGraphQL, Pothos) generates the schema from decorators — better for large teams with type safety.
3. **Authorization**: never trust client-requested fields alone. Implement field-level auth in resolvers or use a directive (`@auth(role: ADMIN)`). Depth limiting + rate limiting prevent abuse.
4. **Persisted Queries (APQ)**: client hashes the query, sends only the hash on subsequent requests — reduces payload size and enables CDN caching.
5. **Federation**: Apollo Federation splits a monolithic schema into independently deployable subgraphs; the Router (supergraph) composes them. Allows separate teams to own their slice.
