---
title: "Zero JS by Default: Astro's Unique Advantage"
description: "Why shipping no JavaScript is a feature, not a limitation."
date: "2025-04-22"
tags: ["performance", "core-web-vitals", "astro"]
author: "Grace Hopper"
---

# Zero JS by Default

When you write an Astro component, none of the template code is shipped to
the browser as JavaScript. It runs at build time (or request time in SSR mode)
and emits pure HTML.

## Contrast With React

A React page ships:
1. The React runtime (~45 KB gzipped)
2. Your component code
3. The serialized "props" to hydrate with

Astro ships:
1. Nothing (unless you add `client:*`)

## When This Helps Most

Content-heavy sites — blogs, documentation, marketing pages — benefit most.
If 80% of your page is text and images, why pay the JS cost?

## Content Collections + Zero JS

Pairing Content Collections (Markdown from the filesystem) with zero-JS
rendering gives you a CMS-like workflow with static-site performance.
