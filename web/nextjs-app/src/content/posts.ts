/**
 * CONCEPT: Mock CMS data source
 *
 * In production, replace this with a real CMS client:
 *   - Contentful:  createClient().getEntries({ content_type: 'blogPost' })
 *   - Sanity:      client.fetch('*[_type == "post"]')
 *   - Strapi:      fetch('/api/posts?populate=*')
 *
 * The page/component code that consumes getCmsPosts() is identical
 * regardless of which data source is used.
 */

export type Post = {
  slug: string;
  title: string;
  excerpt: string;
  body: string;
  publishedAt: string;
  tags: string[];
  author: string;
};

export const POSTS: Post[] = [
  {
    slug: "rsc-deep-dive",
    title: "React Server Components: A Deep Dive",
    excerpt: "How RSC lets you fetch data in components without shipping JS to the client.",
    body: `React Server Components (RSC) let you write components that run only on the server.
They can read databases, call internal APIs, and access secrets — none of this code
ships to the browser. The result is smaller bundles and faster pages.

## How Data Gets There

Instead of useEffect → fetch → useState, you just await the data in the component:

\`\`\`tsx
export default async function BlogPost({ slug }) {
  const post = await db.posts.findFirst({ where: { slug } });
  return <article>{post.body}</article>;
}
\`\`\`

No hydration script, no loading state, no waterfall.`,
    publishedAt: "2025-05-20",
    tags: ["react", "rsc", "performance"],
    author: "Alice Chen",
  },
  {
    slug: "isr-explained",
    title: "ISR Explained: The Best of Both Worlds",
    excerpt: "Incremental Static Regeneration combines SSG speed with SSR freshness.",
    body: `Incremental Static Regeneration (ISR) is a Next.js strategy where pages are
pre-rendered at build time, then regenerated in the background after a set interval.

## The Pattern

\`\`\`ts
// Revalidate this page at most once per 60 seconds
export const revalidate = 60;

export default async function Page() {
  const data = await fetch('/api/data').then(r => r.json());
  return <main>{/* render data */}</main>;
}
\`\`\`

The first request after the interval serves the stale page and triggers a
background re-render. Subsequent requests get the fresh page.`,
    publishedAt: "2025-04-10",
    tags: ["nextjs", "isr", "caching"],
    author: "Bob Kim",
  },
  {
    slug: "server-actions-forms",
    title: "Server Actions: Forms Without an API Layer",
    excerpt: "Submit forms directly to server functions — no REST endpoint needed.",
    body: `Server Actions let you define async functions that run on the server
and call them directly from Client Components — no API route, no fetch, no CORS.

## Basic Pattern

\`\`\`tsx
// app/actions.ts
'use server';
export async function createTodo(formData: FormData) {
  const text = formData.get('text');
  await db.todos.create({ data: { text } });
}

// app/page.tsx
import { createTodo } from './actions';
export default function Page() {
  return <form action={createTodo}><input name="text" /><button>Add</button></form>;
}
\`\`\`

Works without JavaScript (native form submit), enhanced with React's transitions when JS is available.`,
    publishedAt: "2025-03-05",
    tags: ["nextjs", "server-actions", "forms"],
    author: "Carol Davis",
  },
];
