/**
 * CONCEPT: Per-page SSG + ISR (Incremental Static Regeneration)
 *
 * generateStaticParams() tells Next.js which slugs to pre-render at build time.
 * `export const revalidate = 60` means: after 60 seconds, regenerate this page
 * in the background on the next request (ISR). Visitors always get a fast response.
 *
 * In Next.js 16 (previous caching model):
 * - params is a Promise — must await it
 * - revalidate export still works for time-based ISR
 */

import Link from "next/link";
import { getCmsPosts, getCmsPost } from "@/lib/cms";
import { notFound } from "next/navigation";

// ISR: regenerate at most once per minute
export const revalidate = 60;

// Tell Next.js which slugs to pre-render at build time
export async function generateStaticParams() {
  const posts = await getCmsPosts();
  return posts.map((p) => ({ slug: p.slug }));
}

// Dynamic metadata from the CMS
export async function generateMetadata({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params;
  const post = await getCmsPost(slug);
  if (!post) return { title: "Not Found" };
  return { title: post.title, description: post.excerpt };
}

export default async function BlogPostPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params; // params is a Promise in Next.js 16
  const post = await getCmsPost(slug);
  if (!post) notFound();

  return (
    <main style={{ maxWidth: 720, margin: "0 auto", padding: "2rem 1rem" }}>
      <Link href="/blog" style={{ color: "#0066cc", fontWeight: 500, textDecoration: "none" }}>
        ← Blog
      </Link>

      <article style={{ marginTop: "1.5rem" }}>
        <div style={{ display: "flex", gap: "0.4rem", flexWrap: "wrap", marginBottom: "0.75rem" }}>
          {post.tags.map((tag) => (
            <span
              key={tag}
              style={{
                background: "#f0f4ff",
                color: "#0055aa",
                padding: "0.15rem 0.5rem",
                borderRadius: 4,
                fontSize: "0.75rem",
                fontWeight: 600,
              }}
            >
              {tag}
            </span>
          ))}
        </div>

        <h1 style={{ margin: "0 0 0.5rem" }}>{post.title}</h1>
        <div style={{ color: "#666", fontSize: "0.875rem", marginBottom: "2rem" }}>
          By {post.author} ·{" "}
          {new Date(post.publishedAt).toLocaleDateString("en-US", { dateStyle: "long" })}
        </div>

        <div
          style={{
            whiteSpace: "pre-wrap",
            color: "#333",
            lineHeight: 1.8,
            maxWidth: "65ch",
            fontFamily: "var(--font-geist-sans)",
          }}
        >
          {post.body}
        </div>
      </article>

      <aside
        style={{
          marginTop: "2.5rem",
          background: "#f8f9fb",
          borderLeft: "4px solid #2563eb",
          borderRadius: "0 8px 8px 0",
          padding: "1rem 1.25rem",
          fontSize: "0.85rem",
          color: "#555",
        }}
      >
        <strong>ISR in action:</strong> This page was pre-rendered at build time.
        After <code>revalidate = 60</code> seconds, the next visitor triggers a
        background regeneration. No user ever waits for a fresh render.
      </aside>
    </main>
  );
}
