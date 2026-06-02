import { useState } from 'react';
import { useLazyQuery } from '@apollo/client';
import { SEARCH_ARTICLES } from '../graphql/queries';

interface Article { id: string; title: string; author: string; tags: string[] }

export function SearchArticles() {
  const [term, setTerm] = useState('');
  const [search, { loading, data, error, called }] =
    useLazyQuery<{ search: Article[] }>(SEARCH_ARTICLES);

  function handleSearch(e: React.FormEvent) {
    e.preventDefault();
    if (!term.trim()) return;
    void search({ variables: { query: term.trim() } });
  }

  const results = data?.search ?? [];

  return (
    <section>
      <h2>Search</h2>
      <form onSubmit={handleSearch} style={{ display: 'flex', gap: '0.5rem', marginBottom: '1rem' }}>
        <input
          value={term}
          onChange={e => setTerm(e.target.value)}
          placeholder="Search by title, content, or tag…"
          style={{ flex: 1, padding: '0.5rem', fontSize: '1rem', border: '1px solid #ccc', borderRadius: '4px' }}
        />
        <button type="submit" disabled={loading}
          style={{ padding: '0.5rem 1rem', background: '#333', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
          {loading ? '…' : 'Search'}
        </button>
      </form>

      {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}
      {called && !loading && results.length === 0 && <p>No results for "{term}".</p>}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {results.map(a => (
          <li key={a.id} style={{ padding: '0.5rem', borderBottom: '1px solid #eee' }}>
            <strong>{a.title}</strong> <span style={{ color: '#666' }}>by {a.author}</span>
            <div>{a.tags.map(t => <span key={t} style={{ marginRight: '0.25rem', fontSize: '0.8rem', color: '#0070f3' }}>#{t}</span>)}</div>
          </li>
        ))}
      </ul>
    </section>
  );
}
