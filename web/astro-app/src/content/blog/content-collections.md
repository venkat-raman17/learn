---
title: "Content Collections: Type-Safe CMS in the Filesystem"
description: "Use the filesystem as your CMS with full TypeScript safety."
date: "2025-03-15"
tags: ["astro", "cms", "typescript"]
author: "Margaret Hamilton"
---

# Content Collections

Astro's Content Collections let you organize Markdown/MDX files into typed,
queryable collections. Define a Zod schema; Astro auto-generates types.

## Defining a Collection

```ts
// src/content/config.ts
import { defineCollection, z } from 'astro:content';

const blog = defineCollection({
  schema: z.object({
    title: z.string(),
    date: z.string(),
    tags: z.array(z.string()),
  }),
});

export const collections = { blog };
```

## Querying

```astro
---
import { getCollection } from 'astro:content';
const posts = await getCollection('blog');
---
```

## The CMS Pattern

This pattern maps directly to headless CMS usage:

- **Local**: `getCollection('blog')` reads Markdown files
- **Remote CMS**: `await client.getEntries({ content_type: 'blogPost' })`

The page/component code is identical — you swap the data source, not the template.
