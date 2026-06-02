/**
 * CONCEPT: SSR — Server-Side Rendering
 *
 * `export const dynamic = 'force-dynamic'` opts this page out of
 * static generation entirely. Next.js renders it fresh on EVERY request.
 *
 * Use SSR when you need:
 * - Per-user data (auth cookies, session)
 * - Real-time data that must not be stale
 * - Request-specific headers (Accept-Language, User-Agent)
 *
 * In Next.js 16, reading `cookies()` or `headers()` automatically
 * makes the page dynamic — explicit `force-dynamic` is an escape hatch.
 */

import { cookies, headers } from "next/headers";

export const dynamic = "force-dynamic";

export const metadata = {
  title: "Dashboard",
  description: "SSR — rendered fresh on every request",
};

async function getServerStats() {
  // Simulate a per-request DB query or API call
  await new Promise((r) => setTimeout(r, 50));
  return {
    activeUsers: Math.floor(Math.random() * 500) + 100,
    requestsPerMin: Math.floor(Math.random() * 2000) + 500,
    errorRate: (Math.random() * 0.5).toFixed(2),
    p99Latency: Math.floor(Math.random() * 80) + 20,
  };
}

export default async function DashboardPage() {
  const cookieStore = await cookies();
  const headersList = await headers();

  const sessionToken = cookieStore.get("session")?.value ?? "(none)";
  const acceptLanguage = headersList.get("accept-language") ?? "(none)";
  const userAgent = headersList.get("user-agent")?.substring(0, 60) ?? "(none)";
  const renderTime = new Date().toISOString();
  const stats = await getServerStats();

  return (
    <main style={{ maxWidth: 760, margin: "0 auto", padding: "2rem 1rem" }}>
      <div style={{ display: "flex", alignItems: "center", gap: "0.75rem", marginBottom: "0.5rem" }}>
        <h1 style={{ margin: 0 }}>Dashboard</h1>
        <span
          style={{
            background: "#16a34a",
            color: "#fff",
            padding: "0.15rem 0.6rem",
            borderRadius: 6,
            fontSize: "0.75rem",
            fontWeight: 700,
          }}
        >
          SSR
        </span>
      </div>
      <p style={{ color: "#555", marginBottom: "2rem" }}>
        Rendered at:{" "}
        <code style={{ background: "#f5f5f5", padding: "0.1rem 0.4rem", borderRadius: 4 }}>
          {renderTime}
        </code>{" "}
        — refresh to see a new timestamp. This is different from the SSG home page.
      </p>

      <h2 style={{ marginBottom: "1rem" }}>Request Context</h2>
      <div
        style={{
          border: "1px solid #e0e0e0",
          borderRadius: 10,
          overflow: "hidden",
          marginBottom: "2rem",
        }}
      >
        {[
          ["Session cookie", sessionToken],
          ["Accept-Language", acceptLanguage],
          ["User-Agent", `${userAgent}…`],
        ].map(([label, value], i) => (
          <div
            key={label}
            style={{
              display: "grid",
              gridTemplateColumns: "160px 1fr",
              padding: "0.75rem 1.25rem",
              borderBottom: i < 2 ? "1px solid #f0f0f0" : undefined,
            }}
          >
            <span style={{ color: "#666", fontWeight: 600, fontSize: "0.875rem" }}>{label}</span>
            <code style={{ fontSize: "0.8rem", wordBreak: "break-all" }}>{value}</code>
          </div>
        ))}
      </div>

      <h2 style={{ marginBottom: "1rem" }}>Live Stats (random per request)</h2>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)", gap: "1rem", marginBottom: "2rem" }}>
        {[
          { label: "Active Users", value: stats.activeUsers.toLocaleString(), unit: "" },
          { label: "Requests/min", value: stats.requestsPerMin.toLocaleString(), unit: "" },
          { label: "Error Rate", value: stats.errorRate, unit: "%" },
          { label: "p99 Latency", value: stats.p99Latency, unit: "ms" },
        ].map((s) => (
          <div
            key={s.label}
            style={{ border: "1px solid #e0e0e0", borderRadius: 10, padding: "1.25rem", textAlign: "center" }}
          >
            <div style={{ fontSize: "2rem", fontWeight: 700, fontVariantNumeric: "tabular-nums" }}>
              {s.value}
              {s.unit}
            </div>
            <div style={{ color: "#666", fontSize: "0.875rem", marginTop: "0.25rem" }}>{s.label}</div>
          </div>
        ))}
      </div>

      <aside
        style={{
          background: "#f8f9fb",
          borderLeft: "4px solid #16a34a",
          borderRadius: "0 8px 8px 0",
          padding: "1rem 1.25rem",
          fontSize: "0.85rem",
          color: "#555",
        }}
      >
        <strong>When to use SSR vs SSG:</strong> Use SSR for authenticated pages,
        real-time data, or when content varies per user. Use SSG for public,
        infrequently-changing pages (marketing, blog). Use ISR for the middle ground.
      </aside>
    </main>
  );
}
