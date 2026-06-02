import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,
    proxy: {
      // Proxy GraphQL requests to the Apollo server in dev (avoids CORS).
      '/graphql': 'http://localhost:4000',
    },
  },
});
