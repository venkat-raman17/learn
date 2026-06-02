import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';
import App from './App';

/**
 * Apollo Client setup.
 *
 * InMemoryCache: normalises responses by __typename + id, so mutations and queries
 * that touch the same entity share a single cached copy — avoids stale data.
 *
 * In production you'd also configure:
 *  - persistedQueries (APQ): hash-based GET requests, smaller payloads.
 *  - errorLink / retryLink: centralized error handling and retries.
 *  - authLink: inject Authorization headers from a token store.
 */
const client = new ApolloClient({
  uri: '/graphql',             // proxied to http://localhost:4000 by Vite
  cache: new InMemoryCache(),
  connectToDevTools: true,     // enable Apollo DevTools browser extension
});

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ApolloProvider client={client}>
      <App />
    </ApolloProvider>
  </StrictMode>,
);
