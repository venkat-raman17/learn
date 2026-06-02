/**
 * CONCEPT: Dynamic route + Nested loader
 *
 * `$id` in the filename creates a URL parameter `/users/:id`.
 * Each route segment can have its own loader — they run in parallel.
 *
 * `useRouteLoaderData("routes/users")` reads data from the parent route's
 * loader without re-fetching — demonstrating shared data across nested routes.
 */

import { Link, useLoaderData } from "react-router";
import type { Route } from "./+types/users.$id";
import { getUser } from "../data/users";

export async function loader({ params }: Route.LoaderArgs) {
  const user = await getUser(params.id);
  if (!user) throw new Response("Not Found", { status: 404 });
  return { user };
}

export function meta({ data }: Route.MetaArgs) {
  if (!data) return [{ title: "User Not Found" }];
  return [{ title: `${data.user.name} — React Router SSR` }];
}

// Demonstrates error boundary at the route level
export function ErrorBoundary() {
  return (
    <main style={{ maxWidth: 700, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>404 — User not found</h1>
      <Link to="/users">← Back to users</Link>
    </main>
  );
}

export default function UserDetailPage() {
  const { user } = useLoaderData<typeof loader>();

  return (
    <main style={{ maxWidth: 700, margin: "0 auto", padding: "2rem 1rem" }}>
      <Link to="/users" style={{ color: "#0066cc", fontWeight: 500 }}>
        ← Back to users
      </Link>

      <div
        style={{
          marginTop: "1.5rem",
          border: "1px solid #e0e0e0",
          borderRadius: 12,
          padding: "2rem",
        }}
      >
        <h1 style={{ marginTop: 0 }}>{user.name}</h1>

        <table style={{ borderCollapse: "collapse", width: "100%" }}>
          <tbody>
            {[
              ["ID", user.id],
              ["Email", user.email],
              ["Role", user.role],
              ["Joined", new Date(user.joinedAt).toLocaleDateString("en-US", { dateStyle: "long" })],
            ].map(([label, value]) => (
              <tr key={label} style={{ borderBottom: "1px solid #f0f0f0" }}>
                <td style={{ padding: "0.6rem 1rem 0.6rem 0", color: "#666", width: 100 }}>{label}</td>
                <td style={{ padding: "0.6rem 0", fontWeight: 500 }}>{value}</td>
              </tr>
            ))}
          </tbody>
        </table>

        <aside
          style={{
            marginTop: "1.5rem",
            background: "#f8f9fa",
            borderRadius: 8,
            padding: "1rem",
            fontSize: "0.875rem",
            color: "#555",
          }}
        >
          <strong>How this data arrived:</strong> The <code>loader</code> on this
          route ran on the server, fetched <code>getUser("{user.id}")</code>, and
          serialized the result as JSON. No client-side useEffect or state needed.
        </aside>
      </div>
    </main>
  );
}
