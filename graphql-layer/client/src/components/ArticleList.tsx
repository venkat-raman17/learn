import { useQuery, useMutation } from '@apollo/client';
import { GET_ARTICLES, DELETE_ARTICLE } from '../graphql/queries';

interface Article {
  id: string;
  title: string;
  content: string;
  author: string;
  tags: string[];
  createdAt: string;
}

export function ArticleList() {
  const { loading, error, data } = useQuery<{ articles: Article[] }>(GET_ARTICLES);

  const [deleteArticle] = useMutation(DELETE_ARTICLE, {
    // Update the cache after deletion — avoids a full refetch.
    update(cache, { data: mutData }, { variables }) {
      if (!mutData?.deleteArticle) return;
      cache.modify({
        fields: {
          articles(existing: Article[] = [], { readField }) {
            return existing.filter(ref => readField('id', ref) !== variables?.id);
          },
        },
      });
    },
  });

  if (loading) return <p>Loading…</p>;
  if (error)   return <p style={{ color: 'red' }}>Error: {error.message}</p>;

  const articles = data?.articles ?? [];

  return (
    <section>
      <h2>Articles ({articles.length})</h2>
      {articles.length === 0 && <p>No articles yet. Create one below.</p>}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {articles.map(a => (
          <li key={a.id} style={{ border: '1px solid #ddd', padding: '1rem', marginBottom: '0.5rem', borderRadius: '4px' }}>
            <strong>{a.title}</strong>
            <span style={{ color: '#666', marginLeft: '0.5rem' }}>by {a.author}</span>
            <p style={{ margin: '0.25rem 0' }}>{a.content}</p>
            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
              {a.tags.map(t => (
                <span key={t} style={{ background: '#e0f0ff', padding: '2px 8px', borderRadius: '12px', fontSize: '0.8rem' }}>
                  {t}
                </span>
              ))}
            </div>
            <div style={{ marginTop: '0.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <small style={{ color: '#999' }}>{new Date(a.createdAt).toLocaleString()}</small>
              <button
                onClick={() => deleteArticle({ variables: { id: a.id } })}
                style={{ background: '#fee', border: '1px solid #fcc', borderRadius: '4px', padding: '2px 8px', cursor: 'pointer' }}
              >
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </section>
  );
}
