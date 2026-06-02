/**
 * CONCEPT: Headless CMS Client Abstraction
 *
 * This module abstracts the data source behind a typed interface.
 * Today it reads from local data. To switch to Contentful/Sanity/Strapi:
 *
 * Contentful swap:
 *   import { createClient } from 'contentful';
 *   const client = createClient({ space: '...', accessToken: '...' });
 *   export async function getCmsPosts() {
 *     const entries = await client.getEntries({ content_type: 'blogPost' });
 *     return entries.items.map(item => ({
 *       slug: item.fields.slug,
 *       title: item.fields.title,
 *       ...
 *     }));
 *   }
 *
 * Sanity swap:
 *   import { createClient } from '@sanity/client';
 *   const client = createClient({ projectId: '...', dataset: 'production', useCdn: true });
 *   export async function getCmsPosts() {
 *     return client.fetch('*[_type == "post"] | order(publishedAt desc)');
 *   }
 *
 * The page code (app/blog/page.tsx) never changes — only this module.
 */

import { POSTS, type Post } from "@/content/posts";

// Simulated network delay (remove for real CMS client)
async function delay(ms = 50) {
  await new Promise((r) => setTimeout(r, ms));
}

export async function getCmsPosts(): Promise<Post[]> {
  await delay();
  return [...POSTS].sort(
    (a, b) => new Date(b.publishedAt).getTime() - new Date(a.publishedAt).getTime()
  );
}

export async function getCmsPost(slug: string): Promise<Post | null> {
  await delay(30);
  return POSTS.find((p) => p.slug === slug) ?? null;
}
