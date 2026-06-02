import { useState } from 'react';
import { useMutation } from '@apollo/client';
import { CREATE_ARTICLE, GET_ARTICLES } from '../graphql/queries';

export function CreateArticle() {
  const [title,   setTitle]   = useState('');
  const [content, setContent] = useState('');
  const [author,  setAuthor]  = useState('');
  const [tags,    setTags]    = useState('');

  // refetchQueries keeps the list in sync — alternative: manual cache.modify
  const [createArticle, { loading, error }] = useMutation(CREATE_ARTICLE, {
    refetchQueries: [{ query: GET_ARTICLES }],
    onCompleted: () => {
      setTitle(''); setContent(''); setAuthor(''); setTags('');
    },
  });

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!title.trim() || !content.trim() || !author.trim()) return;
    void createArticle({
      variables: {
        input: {
          title:   title.trim(),
          content: content.trim(),
          author:  author.trim(),
          tags:    tags.split(',').map(t => t.trim()).filter(Boolean),
        },
      },
    });
  }

  const fieldStyle: React.CSSProperties = {
    display: 'block', width: '100%', marginBottom: '0.75rem',
    padding: '0.5rem', fontSize: '1rem', boxSizing: 'border-box',
    border: '1px solid #ccc', borderRadius: '4px',
  };

  return (
    <section>
      <h2>New Article</h2>
      <form onSubmit={handleSubmit} style={{ maxWidth: '600px' }}>
        <input   style={fieldStyle} placeholder="Title *"   value={title}   onChange={e => setTitle(e.target.value)}   required />
        <input   style={fieldStyle} placeholder="Author *"  value={author}  onChange={e => setAuthor(e.target.value)}  required />
        <textarea style={{ ...fieldStyle, minHeight: '80px', resize: 'vertical' }}
                  placeholder="Content *" value={content} onChange={e => setContent(e.target.value)} required />
        <input   style={fieldStyle} placeholder="Tags (comma-separated)" value={tags} onChange={e => setTags(e.target.value)} />

        {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}

        <button
          type="submit"
          disabled={loading}
          style={{ padding: '0.5rem 1.5rem', background: '#0070f3', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '1rem' }}
        >
          {loading ? 'Creating…' : 'Create Article'}
        </button>
      </form>
    </section>
  );
}
