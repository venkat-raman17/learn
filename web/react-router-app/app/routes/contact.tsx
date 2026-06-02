/**
 * CONCEPT: Action — Server-side form mutation
 *
 * `action` is the server-side handler for form submissions (POST/PUT/DELETE).
 * The browser posts a FormData to this route; no fetch/axios needed.
 * After the action completes React Router re-runs loaders to refresh stale data.
 *
 * `useActionData()` returns the last action's return value — great for
 * validation errors or success messages without managing any state.
 */

import { Form, useActionData, useNavigation } from "react-router";
import type { Route } from "./+types/contact";

type ActionResult =
  | { ok: true; name: string }
  | { ok: false; errors: Record<string, string> };

export async function action({ request }: Route.ActionArgs): Promise<ActionResult> {
  const data = await request.formData();
  const name = String(data.get("name") ?? "").trim();
  const email = String(data.get("email") ?? "").trim();
  const message = String(data.get("message") ?? "").trim();

  const errors: Record<string, string> = {};
  if (!name) errors.name = "Name is required";
  if (!email || !email.includes("@")) errors.email = "Valid email is required";
  if (message.length < 10) errors.message = "Message must be at least 10 characters";

  if (Object.keys(errors).length > 0) return { ok: false, errors };

  // Simulate saving: in a real app, write to DB here
  await new Promise((r) => setTimeout(r, 300));
  return { ok: true, name };
}

export function meta({}: Route.MetaArgs) {
  return [{ title: "Contact — React Router SSR" }];
}

const fieldStyle = {
  display: "block",
  width: "100%",
  padding: "0.6rem 0.75rem",
  border: "1px solid #ccc",
  borderRadius: 6,
  fontSize: "1rem",
  boxSizing: "border-box" as const,
  fontFamily: "inherit",
};

export default function ContactPage() {
  const result = useActionData<typeof action>();
  const navigation = useNavigation();
  const submitting = navigation.state === "submitting";

  if (result?.ok) {
    return (
      <main style={{ maxWidth: 600, margin: "0 auto", padding: "2rem 1rem", textAlign: "center" }}>
        <div style={{ fontSize: "3rem" }}>✅</div>
        <h2>Thanks, {result.name}!</h2>
        <p style={{ color: "#555" }}>
          Your message was processed by the server <strong>action</strong> — no
          API route or fetch needed.
        </p>
      </main>
    );
  }

  const errors = result?.ok === false ? result.errors : {};

  return (
    <main style={{ maxWidth: 600, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>Contact</h1>
      <p style={{ color: "#666", marginBottom: "1.5rem" }}>
        This form posts to a server <code>action</code> — the handler runs on
        the server and can validate, save, and redirect.
      </p>

      {/*
        Using the React Router <Form> component:
        - Without JS: submits as a native HTML form POST
        - With JS: intercepts, calls the action, updates state — no page reload
      */}
      <Form method="post" style={{ display: "grid", gap: "1.25rem" }}>
        <div>
          <label htmlFor="name" style={{ display: "block", fontWeight: 600, marginBottom: 4 }}>
            Name
          </label>
          <input id="name" name="name" type="text" style={fieldStyle} />
          {errors.name && <p style={{ color: "red", margin: "4px 0 0", fontSize: "0.875rem" }}>{errors.name}</p>}
        </div>

        <div>
          <label htmlFor="email" style={{ display: "block", fontWeight: 600, marginBottom: 4 }}>
            Email
          </label>
          <input id="email" name="email" type="email" style={fieldStyle} />
          {errors.email && <p style={{ color: "red", margin: "4px 0 0", fontSize: "0.875rem" }}>{errors.email}</p>}
        </div>

        <div>
          <label htmlFor="message" style={{ display: "block", fontWeight: 600, marginBottom: 4 }}>
            Message
          </label>
          <textarea id="message" name="message" rows={4} style={fieldStyle} />
          {errors.message && <p style={{ color: "red", margin: "4px 0 0", fontSize: "0.875rem" }}>{errors.message}</p>}
        </div>

        <button
          type="submit"
          disabled={submitting}
          style={{
            padding: "0.75rem 2rem",
            background: submitting ? "#888" : "#0066cc",
            color: "#fff",
            border: "none",
            borderRadius: 8,
            fontSize: "1rem",
            cursor: submitting ? "not-allowed" : "pointer",
            fontWeight: 600,
            width: "fit-content",
          }}
        >
          {submitting ? "Sending…" : "Send message"}
        </button>
      </Form>
    </main>
  );
}
