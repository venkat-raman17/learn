---
title: "Islands Architecture: Partial Hydration Explained"
description: "How Astro ships zero JS by default and hydrates only the interactive pieces."
date: "2025-05-10"
tags: ["astro", "performance", "hydration"]
author: "Ada Lovelace"
---

# Islands Architecture

Traditional SPAs ship a large JS bundle that hydrates the entire page, even
the parts that are completely static. Islands architecture flips this: the
default is **zero JavaScript**. Interactive pieces ("islands") are hydrated
individually, only when they need to be.

## The `client:*` Directives

Astro controls hydration via directives on component imports:

| Directive | When it hydrates |
|-----------|-----------------|
| `client:load` | Immediately on page load |
| `client:idle` | When the browser is idle |
| `client:visible` | When the component enters the viewport |
| `client:media` | When a CSS media query matches |
| `client:only` | Client-only, never SSR'd |

## Why It Matters

A typical marketing page might have 95% static content and one interactive
widget (a counter, a newsletter signup). With islands, you ship JS only for
that widget — the rest is pure HTML.
