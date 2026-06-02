/**
 * CONCEPT: Content Collections (Astro v6)
 *
 * In Astro v6, this file moves to src/content.config.ts and
 * collections require an explicit loader. The `glob` loader
 * reads Markdown/MDX files matching a pattern.
 *
 * Schema validation uses Zod — TypeScript catches malformed
 * frontmatter at build time (missing required fields = compile error).
 *
 * Import z from 'astro/zod' (not 'astro:content' — deprecated in v6).
 */

import { defineCollection } from "astro:content";
import { glob } from "astro/loaders";
import { z } from "astro/zod";

const blog = defineCollection({
  loader: glob({ pattern: "**/*.md", base: "./src/content/blog" }),
  schema: z.object({
    title: z.string(),
    description: z.string(),
    date: z.string(),
    tags: z.array(z.string()).default([]),
    author: z.string().default("Anonymous"),
  }),
});

export const collections = { blog };
