/**
 * CONCEPT: Server Actions — Mutations Without an API Layer
 *
 * `'use server'` marks a function as a Server Action (Server Function in mutation context).
 * It can be called directly from a Client Component — Next.js generates a POST endpoint
 * automatically and handles serialization. You never write fetch/axios for your own data.
 *
 * The <form action={serverAction}> pattern:
 * - Works without JavaScript (native form POST)
 * - Enhanced with React transitions when JS is available
 * - Validation and DB writes happen exclusively on the server
 */

"use client";

import { useActionState, useRef } from "react";

type Todo = { id: string; text: string; done: boolean };
type State = { todos: Todo[]; error?: string };

// In Next.js 16 with App Router, Server Actions must be in separate files
// OR defined inline with 'use server' in a Server Component.
// Since we want to demo the pattern in one file, we simulate the server action
// with a client-side function. In a real app, move addTodo to app/actions.ts.
//
// Real server action pattern (app/actions.ts):
//   'use server';
//   export async function addTodo(prevState: State, formData: FormData): Promise<State> {
//     const text = formData.get('text')?.toString().trim() ?? '';
//     if (!text) return { ...prevState, error: 'Text is required' };
//     await db.todos.create({ data: { text } });
//     return { todos: [...prevState.todos, { id: crypto.randomUUID(), text, done: false }], error: undefined };
//   }

async function addTodo(prevState: State, formData: FormData): Promise<State> {
  // Simulate server latency
  await new Promise((r) => setTimeout(r, 300));
  const text = formData.get("text")?.toString().trim() ?? "";
  if (!text) return { ...prevState, error: "Text is required" };
  const newTodo: Todo = { id: crypto.randomUUID(), text, done: false };
  return { todos: [...prevState.todos, newTodo], error: undefined };
}

const INITIAL_STATE: State = {
  todos: [
    { id: "1", text: "Read the Next.js 16 docs", done: false },
    { id: "2", text: "Implement Server Actions", done: false },
  ],
};

export default function ActionsPage() {
  const [state, formAction, pending] = useActionState(addTodo, INITIAL_STATE);
  const formRef = useRef<HTMLFormElement>(null);

  return (
    <main style={{ maxWidth: 600, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>Server Actions</h1>
      <p style={{ color: "#555", marginBottom: "1.5rem" }}>
        <code>useActionState</code> wires a form to a server function. The
        action runs on the server, validates input, mutates data, and returns
        new state — no API route, no fetch, no state management library.
      </p>

      <form
        ref={formRef}
        action={formAction}
        style={{ display: "flex", gap: "0.5rem", marginBottom: "1.5rem" }}
        onSubmit={() => {
          // Clear input after submission via React transition
          requestAnimationFrame(() => formRef.current?.reset());
        }}
      >
        <input
          name="text"
          type="text"
          placeholder="New todo…"
          style={{
            flex: 1,
            padding: "0.6rem 0.75rem",
            border: "1px solid #ccc",
            borderRadius: 6,
            fontSize: "1rem",
            fontFamily: "inherit",
          }}
        />
        <button
          type="submit"
          disabled={pending}
          style={{
            padding: "0.6rem 1.25rem",
            background: pending ? "#888" : "#0066cc",
            color: "#fff",
            border: "none",
            borderRadius: 6,
            cursor: pending ? "not-allowed" : "pointer",
            fontWeight: 600,
            fontSize: "0.9rem",
          }}
        >
          {pending ? "Adding…" : "Add"}
        </button>
      </form>

      {state.error && (
        <p style={{ color: "red", marginBottom: "1rem", fontSize: "0.875rem" }}>{state.error}</p>
      )}

      <ul style={{ listStyle: "none", padding: 0, display: "grid", gap: "0.5rem" }}>
        {state.todos.map((todo) => (
          <li
            key={todo.id}
            style={{
              border: "1px solid #e0e0e0",
              borderRadius: 8,
              padding: "0.75rem 1rem",
              display: "flex",
              alignItems: "center",
              gap: "0.75rem",
            }}
          >
            <span style={{ width: 8, height: 8, borderRadius: "50%", background: "#0066cc", flexShrink: 0 }} />
            {todo.text}
          </li>
        ))}
      </ul>

      <aside
        style={{
          marginTop: "2rem",
          background: "#f8f9fb",
          borderLeft: "4px solid #dc2626",
          borderRadius: "0 8px 8px 0",
          padding: "1rem 1.25rem",
          fontSize: "0.85rem",
          color: "#555",
        }}
      >
        <strong>Production pattern:</strong> Move the action to{" "}
        <code>app/actions.ts</code>, add <code>&apos;use server&apos;</code> at
        the top, and call <code>await db.todos.create(...)</code> then{" "}
        <code>revalidatePath(&apos;/actions&apos;)</code> to refresh the list.
        Zero API routes needed.
      </aside>
    </main>
  );
}
