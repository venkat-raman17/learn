/**
 * GraphQL schema — schema-first design.
 *
 * The type system is the contract between server and client.
 * Define types first; write resolvers second.
 *
 * Key concepts demonstrated:
 *  - Query (reads) and Mutation (writes)
 *  - Non-null (!) vs nullable fields — nullable by default is GraphQL's "safe default"
 *  - Input types — typed arguments for mutations (avoids inline scalar sprawl)
 *  - ID scalar — opaque identifier, serialised as String
 */
export const typeDefs = /* GraphQL */ `
  type Article {
    id:        ID!
    title:     String!
    content:   String!
    author:    String!
    tags:      [String!]!
    createdAt: String!
  }

  # Input types are distinct from output types — prevents accidentally exposing internal fields.
  input CreateArticleInput {
    title:   String!
    content: String!
    author:  String!
    tags:    [String!]
  }

  input UpdateArticleInput {
    title:   String
    content: String
    tags:    [String!]
  }

  type Query {
    articles:              [Article!]!
    article(id: ID!):      Article
    articlesByAuthor(author: String!): [Article!]!
    search(query: String!): [Article!]!
  }

  type Mutation {
    createArticle(input: CreateArticleInput!): Article!
    updateArticle(id: ID!, input: UpdateArticleInput!): Article
    deleteArticle(id: ID!): Boolean!
  }

  # Subscription shows real-time capability — requires WebSocket transport.
  # Uncomment and add graphql-ws + expressMiddleware to enable.
  # type Subscription {
  #   articleCreated: Article!
  # }
`;
