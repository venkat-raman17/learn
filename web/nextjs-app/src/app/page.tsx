/**
 * CONCEPT: SSG — Static Site Generation
 *
 * This page (and all Server Components) renders at BUILD TIME by default.
 * The HTML is pre-generated and served from the edge/CDN — no per-request work.
 *
 * In Next.js 16, Server Components are the default.
 * They can be async and await any I/O directly in the component body.
 */

import Link from "next/link";

// No `export const dynamic` needed — static is the default
// Build time is captured at build, not at runtime
const BUILD_TIME = new Date().toISOString();

const concepts = [
  {
    href: "/blog",
    badge: "SSG + CMS",
    color: "#2563eb",
    title: "Blog — Content Collections + CMS Pattern",
    desc: "getCollection() abstracted behind a CMS client. Swap local data for Contentful/Sanity/Strapi with one line change.",
  },
  {
    href: "/dashboard",
    badge: "SSR",
    color: "#16a34a",
    title: "Dashboard — Server-Side Rendering",
    desc: "export const dynamic = 'force-dynamic'. Rendered fresh on every request. Can read cookies, headers, and live data.",
  },
  {
    href: "/stream",
    badge: "Streaming",
    color: "#9333ea",
    title: "Streaming — Suspense + Async RSC",
    desc: "Wrap slow data-fetching components in <Suspense>. The shell renders instantly; data streams in when ready.",
  },
  {
    href: "/actions",
    badge: "Server Actions",
    color: "#dc2626",
    title: "Server Actions — Forms Without an API",
    desc: "'use server' functions called directly from Client Components. No fetch, no API route, no CORS.",
  },
  {
    href: "/micro-frontend",
    badge: "Micro-frontend",
    color: "#d97706",
    title: "Micro-frontend — Module Federation Concepts",
    desc: "Independent team deployability via dynamic import(). Shell app loads remote widgets at runtime.",
  },
];

export default function HomePage() {
  return (
    <main style={{ maxWidth: 760, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1 style={{ marginBottom: "0.25rem" }}>Next.js 16 — Rendering Patterns</h1>
      <p style={{ color: "#666", fontSize: "0.9rem", marginBottom: "2rem" }}>
        Built at:{" "}
        <code style={{ background: "#f5f5f5", padding: "0.1rem 0.4rem", borderRadius: 4 }}>
          {BUILD_TIME}
        </code>{" "}
        — this is <strong>SSG</strong>: pre-rendered at build time, not per-request.
      </p>

      <div style={{ display: "grid", gap: "1rem" }}>
        {concepts.map((c) => (
          <Link
            key={c.href}
            href={c.href}
            style={{
              border: "1px solid #e0e0e0",
              borderRadius: 10,
              padding: "1.25rem 1.5rem",
              textDecoration: "none",
              color: "inherit",
              display: "block",
            }}
          >
            <div style={{ display: "flex", alignItems: "center", gap: "0.75rem", marginBottom: "0.5rem" }}>
              <span
                style={{
                  background: c.color,
                  color: "#fff",
                  padding: "0.15rem 0.6rem",
                  borderRadius: 6,
                  fontSize: "0.75rem",
                  fontWeight: 700,
                }}
              >
                {c.badge}
              </span>
              <h2 style={{ margin: 0, fontSize: "1rem" }}>{c.title}</h2>
            </div>
            <p style={{ margin: 0, color: "#555", fontSize: "0.9rem" }}>{c.desc}</p>
          </Link>
        ))}
      </div>
    </main>
  );
}
