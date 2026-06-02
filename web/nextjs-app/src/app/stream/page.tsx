/**
 * CONCEPT: Streaming — Suspense + Async Server Components
 *
 * Next.js streams the page shell immediately, then progressively
 * inserts each Suspense boundary's content as it resolves.
 *
 * The browser starts rendering the visible header/nav while
 * slower data-fetching components are still loading on the server.
 * This dramatically improves Time To First Byte (TTFB) and
 * Largest Contentful Paint (LCP) for data-heavy pages.
 *
 * Pattern:
 *   <Suspense fallback={<Skeleton />}>
 *     <SlowComponent />   ← async RSC, fetches its own data
 *   </Suspense>
 */

import { Suspense } from "react";

export const metadata = { title: "Streaming" };

// Simulates a slow data-fetch (e.g. external API, heavy DB query)
async function SlowFeed({ delayMs }: { delayMs: number }) {
  await new Promise((r) => setTimeout(r, delayMs));

  const items = Array.from({ length: 5 }, (_, i) => ({
    id: i + 1,
    title: `Feed item ${i + 1}`,
    body: `This item took ${delayMs}ms to load — streamed independently.`,
  }));

  return (
    <ul style={{ listStyle: "none", padding: 0, display: "grid", gap: "0.75rem" }}>
      {items.map((item) => (
        <li
          key={item.id}
          style={{ border: "1px solid #e0e0e0", borderRadius: 8, padding: "1rem 1.25rem" }}
        >
          <strong>{item.title}</strong>
          <p style={{ margin: "0.25rem 0 0", color: "#555", fontSize: "0.9rem" }}>{item.body}</p>
        </li>
      ))}
    </ul>
  );
}

function Skeleton({ rows = 3 }: { rows?: number }) {
  return (
    <div style={{ display: "grid", gap: "0.75rem" }}>
      {Array.from({ length: rows }, (_, i) => (
        <div
          key={i}
          style={{
            border: "1px solid #e0e0e0",
            borderRadius: 8,
            padding: "1rem 1.25rem",
            background: "linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%)",
            backgroundSize: "200% 100%",
            animation: "shimmer 1.5s infinite",
            height: 72,
          }}
        />
      ))}
      <style>{`
        @keyframes shimmer {
          0% { background-position: 200% 0; }
          100% { background-position: -200% 0; }
        }
      `}</style>
    </div>
  );
}

export default function StreamPage() {
  return (
    <main style={{ maxWidth: 760, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>Streaming</h1>

      {/* This renders immediately — no waiting */}
      <p style={{ color: "#555", marginBottom: "2rem" }}>
        The page shell (this text) renders instantly. Each{" "}
        <code>{"<Suspense>"}</code> boundary below streams in independently
        when its data resolves. Open the Network tab and watch the chunks arrive.
      </p>

      <div style={{ display: "grid", gap: "2rem" }}>
        <section>
          <h2 style={{ marginBottom: "0.75rem" }}>
            Fast Feed{" "}
            <span style={{ background: "#dcfce7", color: "#16a34a", fontSize: "0.75rem", padding: "0.15rem 0.5rem", borderRadius: 4, fontWeight: 700 }}>
              ~200ms
            </span>
          </h2>
          <Suspense fallback={<Skeleton rows={5} />}>
            <SlowFeed delayMs={200} />
          </Suspense>
        </section>

        <section>
          <h2 style={{ marginBottom: "0.75rem" }}>
            Slow Feed{" "}
            <span style={{ background: "#fef9c3", color: "#ca8a04", fontSize: "0.75rem", padding: "0.15rem 0.5rem", borderRadius: 4, fontWeight: 700 }}>
              ~1500ms
            </span>
          </h2>
          <Suspense fallback={<Skeleton rows={5} />}>
            <SlowFeed delayMs={1500} />
          </Suspense>
        </section>
      </div>

      <aside
        style={{
          marginTop: "2rem",
          background: "#f8f9fb",
          borderLeft: "4px solid #9333ea",
          borderRadius: "0 8px 8px 0",
          padding: "1rem 1.25rem",
          fontSize: "0.85rem",
          color: "#555",
        }}
      >
        <strong>Why streaming matters:</strong> Without streaming, the entire
        page waits for the slowest data fetch. With streaming, users see and
        interact with the fast parts immediately while slow parts load in the
        background — same data, much better perceived performance.
      </aside>
    </main>
  );
}
