/**
 * CONCEPT: SSG with Headless CMS Pattern
 *
 * getCmsPosts() is called at BUILD TIME. The result is baked into the HTML.
 * No per-request fetch, no API call in the browser — pure static output.
 *
 * To switch to a real CMS: edit src/lib/cms.ts only.
 * This page file never changes regardless of which CMS you use.
 */

import Link from "next/link";
import { getCmsPosts } from "@/lib/cms";

export const metadata = {
  title: "Blog",
  description: "Posts fetched from the CMS at build time",
};

export default async function BlogPage() {
  const posts = await getCmsPosts();

  return (
    <main style={{ maxWidth: 760, margin: "0 auto", padding: "2rem 1rem" }}>
      <h1>Blog</h1>
      <p style={{ color: "#555", marginBottom: "1.5rem" }}>
        Posts loaded via <code>getCmsPosts()</code> — a CMS client abstraction
        that reads local data today and can swap to Contentful/Sanity tomorrow.
        This runs at <strong>build time</strong> (SSG).
      </p>

      <div style={{ display: "grid", gap: "1rem" }}>
        {posts.map((post) => (
          <Link
            key={post.slug}
            href={`/blog/${post.slug}`}
            style={{
              border: "1px solid #e0e0e0",
              borderRadius: 10,
              padding: "1.25rem 1.5rem",
              textDecoration: "none",
              color: "inherit",
              display: "block",
            }}
          >
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
              <div>
                <h2 style={{ margin: "0 0 0.4rem", fontSize: "1.05rem" }}>{post.title}</h2>
                <p style={{ margin: 0, color: "#555", fontSize: "0.9rem" }}>{post.excerpt}</p>
                <div style={{ marginTop: "0.5rem", display: "flex", gap: "0.4rem", flexWrap: "wrap" }}>
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
              </div>
              <time
                dateTime={post.publishedAt}
                style={{ color: "#888", fontSize: "0.8rem", whiteSpace: "nowrap", marginLeft: "1rem" }}
              >
                {new Date(post.publishedAt).toLocaleDateString("en-US", { dateStyle: "medium" })}
              </time>
            </div>
          </Link>
        ))}
      </div>
    </main>
  );
}
