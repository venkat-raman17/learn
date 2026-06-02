/**
 * CONCEPT: Basic Loader — the entry point to React Router's data model
 *
 * Every route can export a `loader` that runs on the server before rendering.
 * Data arrives synchronously in the component via `useLoaderData()`.
 */

import { Link, useLoaderData } from "react-router";
import type { Route } from "./+types/home";

export async function loader({}: Route.LoaderArgs) {
  // Could be a DB query, fetch, or any async I/O
  const renderTime = new Date().toISOString();
  const conceptCount = 3;
  return { renderTime, conceptCount };
}

export function meta({}: Route.MetaArgs) {
  return [
    { title: "React Router SSR — Rendering Concepts" },
    { name: "description", content: "Loaders, Actions, nested routes and deferred data" },
  ];
}

const cardStyle = {
  border: "1px solid #e0e0e0",
  borderRadius: 10,
  padding: "1.5rem",
  textDecoration: "none",
  color: "inherit",
  display: "block",
  transition: "box-shadow 0.15s",
};

export default function Home() {
  const { renderTime, conceptCount } = useLoaderData<typeof loader>();

  return (
    <main style={{ maxWidth: 700, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1 style={{ marginBottom: "0.25rem" }}>React Router 7 — SSR Patterns</h1>
      <p style={{ color: "#666", fontSize: "0.9rem", marginBottom: "2rem" }}>
        Page rendered on the server at:{" "}
        <code style={{ background: "#f5f5f5", padding: "0.1rem 0.4rem", borderRadius: 4 }}>
          {renderTime}
        </code>{" "}
        — this timestamp comes from the <strong>loader</strong>, not the client.
      </p>

      <p style={{ marginBottom: "1.5rem" }}>
        {conceptCount} patterns to explore:
      </p>

      <nav style={{ display: "grid", gap: "1rem" }}>
        <Link to="/users" style={cardStyle}>
          <h2 style={{ margin: "0 0 0.5rem" }}>📋 Users — Loader</h2>
          <p style={{ margin: 0, color: "#555", fontSize: "0.9rem" }}>
            Server-side data fetching before render. <code>loader</code> runs on
            the server; component reads via <code>useLoaderData()</code>.
          </p>
        </Link>

        <Link to="/users/u1" style={cardStyle}>
          <h2 style={{ margin: "0 0 0.5rem" }}>🔍 User Detail — Dynamic Route + Error Boundary</h2>
          <p style={{ margin: 0, color: "#555", fontSize: "0.9rem" }}>
            <code>/users/:id</code> — per-item loader, 404 error boundary,
            nested URL parameters.
          </p>
        </Link>

        <Link to="/contact" style={cardStyle}>
          <h2 style={{ margin: "0 0 0.5rem" }}>📬 Contact — Action (Form Mutation)</h2>
          <p style={{ margin: 0, color: "#555", fontSize: "0.9rem" }}>
            Server-side form handler with validation. Works without JavaScript
            (progressive enhancement), enhanced with React Router's{" "}
            <code>{"<Form>"}</code>.
          </p>
        </Link>
      </nav>

      <aside
        style={{
          marginTop: "2rem",
          background: "#f8f9fb",
          borderRadius: 8,
          padding: "1rem 1.25rem",
          fontSize: "0.85rem",
          color: "#555",
          borderLeft: "4px solid #0066cc",
        }}
      >
        <strong>Key insight:</strong> In React Router 7 (SSR mode), the{" "}
        <code>loader</code> / <code>action</code> pattern gives you a single
        mental model for both server-rendered pages and client navigations — no
        separate REST API needed for your own data.
      </aside>
    </main>
  );
}
