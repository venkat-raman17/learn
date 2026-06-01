# Frontend system design

The frontend half of HLD — its own interview at senior/staff level (especially for full-stack and
frontend-leaning roles). You design a complex UI feature end to end: component architecture, data
flow, rendering, performance, and accessibility. Practice the *building* in the [`web/`](../../web/)
apps; this is the reading.

## The round, and what's assessed
A prompt like "design a news feed", "a data grid with 100k rows", "a collaborative editor", or
"an autocomplete". They grade: structured decomposition, **trade-off reasoning**, performance
sense, a11y as a first-class concern, and API/state design — not pixel-perfect CSS.

A repeatable structure:
1. **Requirements** — features, scale (rows, QPS, payload), devices, latency, offline, i18n, a11y.
2. **Component architecture** — break the UI into components; define each component's props/API and ownership.
3. **Data flow** — what state lives where (server vs client vs URL); how data is fetched and cached.
4. **Rendering strategy** — CSR/SSR/SSG/streaming per route; where code runs.
5. **Performance** — bundle, code-split, virtualize, image strategy; the Core Web Vitals budget.
6. **Edge cases & a11y** — loading/empty/error states, keyboard/screen-reader, race conditions.
7. **Trade-offs** — the 2–3 big decisions and what they cost.

## Rendering strategies (recap → when)
| Strategy | Use when | Cost |
| --- | --- | --- |
| CSR | App-like, auth-gated, low SEO | Slow first paint, big JS |
| SSR | Dynamic + SEO + fast first paint | Server cost, TTFB |
| SSG | Mostly-static content | Rebuild to update |
| ISR | Static + periodic freshness | Stale window |
| Streaming SSR | Large pages, show shell fast | Complexity |
| RSC (server components) | Cut client JS for non-interactive UI | Newer model, framework lock |
| Islands | Content sites, minimal JS | Limited interactivity |

Build the same feature across [`web/`](../../web/) (TanStack Start, Next.js, React Router 7, Astro)
to feel these in your hands.

## Data fetching & caching
- **Server state ≠ client state.** Use a server-cache library (React Query / SWR) for fetched data —
  it gives caching, dedup, background refetch, and stale-while-revalidate for free.
- **HTTP caching + CDN/edge** for static and cacheable API responses; `Cache-Control`, ETags.
- **Pagination/virtualization** for large lists (cursor pagination + windowed rendering).
- **Race conditions:** ignore stale responses (request IDs / `AbortController`) — the classic autocomplete bug.

## State management at scale
- **Local** (useState) → **shared** (Context / Zustand / Redux / signals) → **server** (React Query) → **URL** (filters, tabs — shareable, back-button-friendly).
- Pick the *narrowest* scope that works; over-globalizing state is the common smell. Context re-renders
  the subtree — split contexts or use a store for high-frequency updates.

## Performance (Core Web Vitals)
- **LCP** (load): code-split, lazy-load below the fold, optimize images (responsive, modern formats, priority hints), preconnect.
- **INP** (interactivity): keep the main thread free — debounce, web workers for heavy work, avoid large synchronous renders.
- **CLS** (stability): reserve space for images/ads/async content.
- Enforce a **performance budget** (bundle size, LCP target) in CI; measure with Lighthouse / web-vitals.

## Accessibility as architecture
Semantic HTML first; ARIA only to fill gaps. Keyboard navigation, focus management (modals/menus),
`aria-live` for async updates, color contrast, reduced-motion. Bake it into component APIs, not as a
last-pass audit. (See [`../../docs`](../) and the a11y checklist mindset.)

## Bigger-architecture topics
- **Micro-frontends** — independent deploy per team (Module Federation); pay in bundle duplication + integration complexity. Justify with org scale, not tech novelty.
- **Real-time** — polling vs SSE vs WebSocket; reconnection, backpressure, optimistic UI + reconciliation.
- **Design systems** — tokens, composable primitives, variants/states, documented a11y — the UI-LLD foundation (see [`../lld/`](../lld/)).

## Resources
- [GreatFrontEnd — Front End System Design Playbook](https://www.greatfrontend.com/front-end-system-design-playbook)
- [patterns.dev](https://www.patterns.dev/) (rendering + React patterns) · [Frontend at Scale](https://frontendatscale.com/)
- [web.dev — Core Web Vitals](https://web.dev/vitals/)
