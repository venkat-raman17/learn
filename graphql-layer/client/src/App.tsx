import { ArticleList }    from './components/ArticleList';
import { CreateArticle }  from './components/CreateArticle';
import { SearchArticles } from './components/SearchArticles';

export default function App() {
  return (
    <main style={{ fontFamily: 'system-ui, sans-serif', maxWidth: '800px', margin: '0 auto', padding: '2rem' }}>
      <h1 style={{ borderBottom: '2px solid #0070f3', paddingBottom: '0.5rem' }}>
        Apollo GraphQL Demo
      </h1>
      <p style={{ color: '#666' }}>
        React + Apollo Client talking to an Apollo Server 4 backend.
        Data is in-memory — restart the server to reset.
      </p>

      <SearchArticles />
      <hr style={{ margin: '2rem 0' }} />
      <ArticleList />
      <hr style={{ margin: '2rem 0' }} />
      <CreateArticle />
    </main>
  );
}
