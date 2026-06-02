import { ApolloServer } from '@apollo/server';
import { startStandaloneServer } from '@apollo/server/standalone';
import { typeDefs } from './schema.js';
import { resolvers } from './resolvers.js';

const server = new ApolloServer({ typeDefs, resolvers });

const { url } = await startStandaloneServer(server, {
  listen: { port: 4000 },
  // context: async ({ req }) => ({ token: req.headers.authorization }),
});

console.log(`🚀  GraphQL server ready at ${url}`);
console.log(`📖  Sandbox (query explorer): ${url}`);
