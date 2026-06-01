# web — frontend practice & rendering comparison

Build the same kinds of features across frameworks with **different rendering paradigms** — the
core of frontend LLD/HLD practice (Phases 3–4). Each app is a separate scaffold; `node_modules` is
git-ignored, so run `npm install` per app when you work on it.

| App | Paradigm | Rendering modes |
| --- | --- | --- |
| `spa-react-vite/` | Classic SPA (React + Vite + TS) | CSR |
| `ssr-tanstack-start/` | Client-first, explicit/selective SSR | CSR · SSR · `ssr: 'data-only'` · streaming |
| `nextjs-app/` | RSC-first (server by default), App Router | SSR · SSG · ISR · streaming · PPR · edge |
| `react-router-app/` | Loader/action data (the modern Remix) | SSR · framework-mode · nested routes |
| `astro-app/` | Islands architecture | SSG / zero-JS by default + opt-in hydration |

> Native + web lives at [`../expo-app/`](../expo-app/) (Expo / React Native) — one codebase for
> iOS, Android, and web.

## Run any app

```powershell
cd <app>            # e.g. nextjs-app
npm install         # per app (node_modules is git-ignored)
npm run dev
```

## ssr-tanstack-start — ✅ scaffolded (create-start-app)

In place: TanStack Start + Prisma + Biome. Install and run:

```powershell
cd ssr-tanstack-start
npm install
npm run dev
```

Explore rendering per route (CSR / full SSR / `ssr: 'data-only'` / streaming).
Reference: https://tanstack.com/start/latest/docs/framework/react/guide/selective-ssr

## How to use this for practice

Pick one feature (e.g. a product list with search + detail pages) and implement it in 2–3 of these
apps. Compare: TTFB vs FCP, JS shipped, the data-fetching model, caching/revalidation, and **where
code runs** (client vs server). Write the comparison up in [`../docs/hld/`](../docs/hld/) (frontend
system design).
