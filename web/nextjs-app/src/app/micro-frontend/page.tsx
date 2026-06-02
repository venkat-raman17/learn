/**
 * CONCEPT: Micro-frontends — Independent Team Deployability
 *
 * Micro-frontends extend the micro-services idea to the UI layer:
 * different teams own, build, and deploy different parts of the frontend
 * independently. The "shell" app stitches them together at runtime.
 *
 * Module Federation (webpack/Rspack) is the production tool for this.
 * This page demonstrates the same PATTERN using dynamic import() as a
 * stand-in for loadRemoteModule() — the mental model is identical.
 *
 * Topology:
 *   Shell App (this app)
 *     ├── Remote A: Marketing Widget (Team A deploys independently)
 *     ├── Remote B: Cart Widget     (Team B deploys independently)
 *     └── Remote C: Auth Widget     (Team C deploys independently)
 */

"use client";

import { Suspense, lazy, useState } from "react";

// In production with Module Federation (webpack.config.js):
//   const MarketingWidget = lazy(() => import('marketing_app/MarketingWidget'));
//   The remote is served from https://marketing.example.com/remoteEntry.js
//
// Here we use a local dynamic import as a stand-in to show the identical usage pattern:
const MarketingWidget = lazy(() =>
  // Simulated remote load delay
  new Promise<{ default: React.ComponentType }>((resolve) =>
    setTimeout(
      () =>
        resolve({
          default: function MarketingWidget() {
            return (
              <div style={{ background: "#eff6ff", border: "2px solid #3b82f6", borderRadius: 10, padding: "1.25rem" }}>
                <h3 style={{ margin: "0 0 0.5rem", color: "#1d4ed8" }}>📣 Marketing Widget</h3>
                <p style={{ margin: 0, color: "#555", fontSize: "0.9rem" }}>
                  Owned by Team A. Deployed at <code>marketing.example.com/remoteEntry.js</code>.
                  Loaded at runtime — Team A can deploy without touching this shell.
                </p>
              </div>
            );
          },
        }),
      800
    )
  )
);

const CartWidget = lazy(() =>
  new Promise<{ default: React.ComponentType }>((resolve) =>
    setTimeout(
      () =>
        resolve({
          default: function CartWidget() {
            const [count, setCount] = useState(0);
            return (
              <div style={{ background: "#f0fdf4", border: "2px solid #22c55e", borderRadius: 10, padding: "1.25rem" }}>
                <h3 style={{ margin: "0 0 0.5rem", color: "#15803d" }}>🛒 Cart Widget</h3>
                <p style={{ margin: "0 0 0.75rem", color: "#555", fontSize: "0.9rem" }}>
                  Owned by Team B. Has its own React state, its own router, its own CSS.
                  Communicates with the shell via a shared event bus (CustomEvent).
                </p>
                <button
                  onClick={() => setCount((c) => c + 1)}
                  style={{ background: "#16a34a", color: "#fff", border: "none", borderRadius: 6, padding: "0.4rem 1rem", cursor: "pointer", fontWeight: 600 }}
                >
                  Add item ({count} in cart)
                </button>
              </div>
            );
          },
        }),
      1200
    )
  )
);

function WidgetSkeleton({ color }: { color: string }) {
  return (
    <div
      style={{
        border: `2px dashed ${color}`,
        borderRadius: 10,
        padding: "1.25rem",
        height: 120,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        color,
        fontWeight: 600,
        fontSize: "0.9rem",
      }}
    >
      Loading remote widget…
    </div>
  );
}

export default function MicroFrontendPage() {
  return (
    <main style={{ maxWidth: 760, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>Micro-frontends</h1>
      <p style={{ color: "#555", marginBottom: "1rem" }}>
        Each coloured widget below is a "remote" owned by a separate team. In
        production, it's served from a separate deployment using{" "}
        <strong>Module Federation</strong>. The shell loads them at runtime —
        neither the shell nor the remotes need to be redeployed together.
      </p>

      <div
        style={{
          background: "#f5f5f5",
          borderRadius: 8,
          padding: "0.75rem 1rem",
          fontFamily: "monospace",
          fontSize: "0.82rem",
          color: "#333",
          marginBottom: "2rem",
          overflowX: "auto",
        }}
      >
        {`Shell App (next.config.js)
└── ModuleFederationPlugin({
      remotes: {
        marketing_app: 'marketing_app@https://marketing.example.com/remoteEntry.js',
        cart_app:      'cart_app@https://cart.example.com/remoteEntry.js',
      }
    })

// Usage in any component:
const MarketingWidget = lazy(() => import('marketing_app/MarketingWidget'));`}
      </div>

      <div style={{ display: "grid", gap: "1.25rem", marginBottom: "2rem" }}>
        <Suspense fallback={<WidgetSkeleton color="#3b82f6" />}>
          <MarketingWidget />
        </Suspense>

        <Suspense fallback={<WidgetSkeleton color="#22c55e" />}>
          <CartWidget />
        </Suspense>
      </div>

      <h2>Architecture Principles</h2>
      <div style={{ display: "grid", gap: "0.75rem" }}>
        {[
          { title: "Independent deployability", desc: "Team A deploys their widget without coordination with Team B or the shell team." },
          { title: "Technology agnosticism", desc: "Marketing Widget could be React 18, Cart Widget React 19, Auth Widget Vue — the shell doesn't care." },
          { title: "Shared dependencies", desc: "React and ReactDOM are shared singletons (singleton: true in ModuleFederationPlugin) to avoid loading them twice." },
          { title: "Communication", desc: "Remotes communicate with the shell via CustomEvent, a shared EventBus, or a framework-agnostic store (Zustand/Jotai)." },
          { title: "CSS isolation", desc: "Each remote uses CSS Modules or scoped styles. No global CSS leaks between teams." },
        ].map((item) => (
          <div key={item.title} style={{ border: "1px solid #e0e0e0", borderRadius: 8, padding: "0.875rem 1.25rem" }}>
            <strong>{item.title}</strong>
            <p style={{ margin: "0.25rem 0 0", color: "#555", fontSize: "0.875rem" }}>{item.desc}</p>
          </div>
        ))}
      </div>
    </main>
  );
}
