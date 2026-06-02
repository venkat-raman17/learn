/**
 * CONCEPT: Loader — Server-side data fetching before render
 *
 * `loader` runs on the server before the component renders.
 * The component receives data via `useLoaderData()` — no useEffect, no loading state.
 * React Router handles the loading UI automatically while the loader is running.
 *
 * On client-side navigation the loader still runs on the server (SSR mode),
 * giving you a single data-fetching model for both SSR and SPA navigations.
 */

import { Link, useLoaderData } from "react-router";
import type { Route } from "./+types/users";
import { getUsers } from "../data/users";

export async function loader({}: Route.LoaderArgs) {
  const users = await getUsers();
  return { users };
}

export function meta({}: Route.MetaArgs) {
  return [{ title: "Users — React Router SSR" }];
}

export default function UsersPage() {
  const { users } = useLoaderData<typeof loader>();

  return (
    <main style={{ maxWidth: 700, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>Users</h1>
      <p style={{ color: "#666", marginBottom: "1.5rem" }}>
        Data loaded by a <code>loader</code> function — runs on the server before
        this component renders.
      </p>

      <ul style={{ listStyle: "none", padding: 0, display: "grid", gap: "0.75rem" }}>
        {users.map((user) => (
          <li
            key={user.id}
            style={{
              border: "1px solid #e0e0e0",
              borderRadius: 8,
              padding: "1rem 1.25rem",
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <div>
              <strong>{user.name}</strong>
              <div style={{ color: "#666", fontSize: "0.875rem" }}>{user.email}</div>
            </div>
            <div style={{ display: "flex", alignItems: "center", gap: "1rem" }}>
              <span
                style={{
                  background: user.role === "admin" ? "#e0f0ff" : user.role === "editor" ? "#f0ffe0" : "#f5f5f5",
                  color: user.role === "admin" ? "#0066cc" : user.role === "editor" ? "#006600" : "#555",
                  padding: "0.2rem 0.6rem",
                  borderRadius: 12,
                  fontSize: "0.8rem",
                  fontWeight: 600,
                }}
              >
                {user.role}
              </span>
              <Link to={`/users/${user.id}`} style={{ color: "#0066cc", fontWeight: 500 }}>
                View →
              </Link>
            </div>
          </li>
        ))}
      </ul>
    </main>
  );
}
