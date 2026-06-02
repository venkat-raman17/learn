export type User = {
  id: string;
  name: string;
  email: string;
  role: "admin" | "editor" | "viewer";
  joinedAt: string;
};

export const USERS: User[] = [
  { id: "u1", name: "Alice Chen", email: "alice@example.com", role: "admin", joinedAt: "2023-01-15" },
  { id: "u2", name: "Bob Kim", email: "bob@example.com", role: "editor", joinedAt: "2023-03-22" },
  { id: "u3", name: "Carol Davis", email: "carol@example.com", role: "viewer", joinedAt: "2023-05-10" },
  { id: "u4", name: "Dan Park", email: "dan@example.com", role: "editor", joinedAt: "2023-07-04" },
  { id: "u5", name: "Eve Nguyen", email: "eve@example.com", role: "viewer", joinedAt: "2023-09-18" },
];

// Simulated async fetch (replace with real DB/API call)
export async function getUsers(): Promise<User[]> {
  await new Promise((r) => setTimeout(r, 80)); // simulate network latency
  return USERS;
}

export async function getUser(id: string): Promise<User | null> {
  await new Promise((r) => setTimeout(r, 40));
  return USERS.find((u) => u.id === id) ?? null;
}
