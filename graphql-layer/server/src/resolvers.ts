/**
 * Resolvers — the implementation behind each schema field.
 *
 * In-memory store (Map) keeps this self-contained and runnable without Docker.
 * In production you'd swap the store calls for DB queries (R2DBC, MongoDB, etc.).
 *
 * Key patterns:
 *  - Resolver shape mirrors the schema structure (Query, Mutation, Type).
 *  - Each resolver receives (parent, args, context, info).
 *  - Null return from a nullable field is valid; null from a non-null field → GraphQL error.
 *  - Context (3rd arg) carries per-request data: auth, dataloaders, DB clients.
 */
import { GraphQLError } from 'graphql';

interface Article {
  id: string;
  title: string;
  content: string;
  author: string;
  tags: string[];
  createdAt: string;
}

// ── In-memory store ──────────────────────────────────────────────────────────

const store = new Map<string, Article>();
let nextId = 1;

function seed() {
  const articles: Omit<Article, 'id'>[] = [
    {
      title: 'Introduction to GraphQL',
      content: 'GraphQL is a query language for APIs and a runtime for executing those queries.',
      author: 'alice',
      tags: ['graphql', 'api'],
      createdAt: new Date(Date.now() - 86400000).toISOString(),
    },
    {
      title: 'Apollo Server 4',
      content: 'Apollo Server 4 is a spec-compliant GraphQL server that is production-ready.',
      author: 'bob',
      tags: ['apollo', 'graphql', 'nodejs'],
      createdAt: new Date().toISOString(),
    },
  ];
  for (const a of articles) {
    const id = String(nextId++);
    store.set(id, { id, ...a });
  }
}
seed();

// ── Resolver map ─────────────────────────────────────────────────────────────

export const resolvers = {
  Query: {
    articles: () => [...store.values()],

    article: (_: unknown, { id }: { id: string }) =>
      store.get(id) ?? null,

    articlesByAuthor: (_: unknown, { author }: { author: string }) =>
      [...store.values()].filter(a => a.author === author),

    // Naive full-text search — replace with an Elasticsearch call in production.
    search: (_: unknown, { query }: { query: string }) => {
      const q = query.toLowerCase();
      return [...store.values()].filter(a =>
        a.title.toLowerCase().includes(q) ||
        a.content.toLowerCase().includes(q) ||
        a.tags.some(t => t.toLowerCase().includes(q))
      );
    },
  },

  Mutation: {
    createArticle: (
      _: unknown,
      { input }: { input: { title: string; content: string; author: string; tags?: string[] } }
    ): Article => {
      const id = String(nextId++);
      const article: Article = {
        id,
        title:     input.title,
        content:   input.content,
        author:    input.author,
        tags:      input.tags ?? [],
        createdAt: new Date().toISOString(),
      };
      store.set(id, article);
      return article;
    },

    updateArticle: (
      _: unknown,
      { id, input }: { id: string; input: { title?: string; content?: string; tags?: string[] } }
    ): Article | null => {
      const existing = store.get(id);
      if (!existing) return null;
      const updated: Article = {
        ...existing,
        ...(input.title   !== undefined && { title:   input.title }),
        ...(input.content !== undefined && { content: input.content }),
        ...(input.tags    !== undefined && { tags:    input.tags }),
      };
      store.set(id, updated);
      return updated;
    },

    deleteArticle: (_: unknown, { id }: { id: string }): boolean => {
      if (!store.has(id)) {
        throw new GraphQLError(`Article ${id} not found`, {
          extensions: { code: 'NOT_FOUND' },
        });
      }
      return store.delete(id);
    },
  },
};
