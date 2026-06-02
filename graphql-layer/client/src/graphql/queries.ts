import { gql } from '@apollo/client';

// ── Fragments — reusable field selections ────────────────────────────────────

export const ARTICLE_FIELDS = gql`
  fragment ArticleFields on Article {
    id
    title
    content
    author
    tags
    createdAt
  }
`;

// ── Queries ──────────────────────────────────────────────────────────────────

export const GET_ARTICLES = gql`
  ${ARTICLE_FIELDS}
  query GetArticles {
    articles {
      ...ArticleFields
    }
  }
`;

export const GET_ARTICLE = gql`
  ${ARTICLE_FIELDS}
  query GetArticle($id: ID!) {
    article(id: $id) {
      ...ArticleFields
    }
  }
`;

export const SEARCH_ARTICLES = gql`
  ${ARTICLE_FIELDS}
  query SearchArticles($query: String!) {
    search(query: $query) {
      ...ArticleFields
    }
  }
`;

// ── Mutations ────────────────────────────────────────────────────────────────

export const CREATE_ARTICLE = gql`
  ${ARTICLE_FIELDS}
  mutation CreateArticle($input: CreateArticleInput!) {
    createArticle(input: $input) {
      ...ArticleFields
    }
  }
`;

export const UPDATE_ARTICLE = gql`
  ${ARTICLE_FIELDS}
  mutation UpdateArticle($id: ID!, $input: UpdateArticleInput!) {
    updateArticle(id: $id, input: $input) {
      ...ArticleFields
    }
  }
`;

export const DELETE_ARTICLE = gql`
  mutation DeleteArticle($id: ID!) {
    deleteArticle(id: $id)
  }
`;
