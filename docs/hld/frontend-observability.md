# Front-End Observability — Analytics, Crash Reporting & RUM

Backend observability (the [`backend-service` ops layer](../../backend-service/): health, metrics,
traces, structured logs) stops at the edge. **The actual user experience happens in the browser and
on the phone** — where the network is hostile, devices vary wildly, and a single uncaught error can
white-screen the app with the backend none the wiser. Front-end observability closes that gap.

This repo demonstrates the architecture **by working sample** in two places, and the breadth below
**by theory**:

- Web: [`web/spa-react-vite/src/observability/`](../../web/spa-react-vite/src/observability/) — analytics + error boundary + global handlers + Core Web Vitals.
- Mobile: [`expo-app/src/observability/`](../../expo-app/src/observability/) — analytics + `ErrorUtils` crash hook + screen tracking.

---

## The three client signals

| Signal | Question it answers | Web hook | Mobile hook |
|--------|--------------------|----------|-------------|
| **Analytics** (product + RUM) | What are users doing? Where do they drop off? | `track()`, route-change page_view | `track()`, `usePathname` screen_view |
| **Errors / crashes** | What's broken in the wild? | `window.onerror`, `unhandledrejection`, React error boundary | `ErrorUtils.setGlobalHandler`, native crash, error boundary |
| **Performance** | Is it fast for *real* users (not your laptop)? | Core Web Vitals via `PerformanceObserver` | TTI, frame drops, ANRs, startup time |

## The non-negotiable pattern: a vendor-agnostic seam

App code must depend on a small interface (`track` / `captureException` / `recordVital`), **never a
vendor SDK directly**. Swapping GA4 → PostHog, or Sentry → Datadog RUM, should be a one-file change.
This is the same "mock-but-real-shaped" approach as the [headless-CMS client](../../web/nextjs-app/src/lib/cms.ts);
the repo's default sink buffers in memory + logs to console so everything runs with **zero API keys**.

```
UI components ──▶ ObservabilitySink (interface) ──▶ [ MemorySink (dev) | Sentry | PostHog | … ]
```

---

## Analytics

- **Product analytics** (funnels, retention, feature adoption) vs **RUM** (Real User Monitoring — perf + errors keyed by real sessions/geos/devices). Often the same event pipeline, different dashboards.
- **Event taxonomy**: a small, reviewed set of named events with typed props. Resist per-call ad-hoc names — they explode dashboards.
- **Identity**: anonymous id → `identify(userId, traits)` on login; stitch pre/post-auth sessions.
- **Transport**: batch events and flush with `navigator.sendBeacon` (survives page unload) or `fetch(..., {keepalive:true})`. On mobile, queue offline and flush on reconnect.
- **Sampling & cost**: high-volume events get sampled; keep errors at 100%.

## Error & crash reporting

- **Web**: a React **error boundary** only catches render-phase errors — pair it with `window.onerror` (sync) and `unhandledrejection` (promises) for full coverage. See [`globalHandlers.ts`](../../web/spa-react-vite/src/observability/globalHandlers.ts).
- **Mobile**: `ErrorUtils.setGlobalHandler` is RN's crash hook (with a `fatal` flag); chain the previous handler so the red-box/native crash still happens. Native (Java/ObjC) crashes need a native SDK (Crashlytics/Sentry-native).
- **Source maps / symbolication**: ship minified, upload source maps to the vendor so stacks are readable. Mobile needs dSYM (iOS) / ProGuard mappings (Android).
- **Breadcrumbs**: a ring buffer of recent actions/network calls attached to each error for repro context.
- **Release health**: tag every event with app version/release; track crash-free-sessions % per release; gate rollouts on it.

## Performance — Core Web Vitals & RUM

| Metric | Measures | Good |
|--------|----------|------|
| **LCP** | Largest Contentful Paint (loading) | < 2.5s |
| **INP** | Interaction to Next Paint (responsiveness; replaced FID in 2024) | < 200ms |
| **CLS** | Cumulative Layout Shift (visual stability) | < 0.1 |
| FCP / TTFB | First paint / time to first byte | < 1.8s / < 0.8s |

- Collected from real users via `PerformanceObserver` (the [`web-vitals`](https://github.com/GoogleChrome/web-vitals) library; the repo reimplements LCP/CLS/FCP/TTFB from scratch in [`webVitals.ts`](../../web/spa-react-vite/src/observability/webVitals.ts) to show the mechanics). Final-value metrics (LCP, CLS) are flushed on `visibilitychange → hidden`.
- **Mobile**: cold/warm start time, JS thread frame drops, ANRs (Android), TTI.

## Tracing into the browser (joining front + back end)

Propagate the **W3C `traceparent`** header on outbound `fetch`/XHR so the frontend span becomes the
**root** of the same distributed trace the backend continues — the browser-side counterpart to the
[backend tracing kata](../../backend-service/src/main/java/com/venkat/backend/lld/observability/). This
is how you answer "the user saw 3s — was it network, render, or the API?".

## Session replay

DOM-mutation (or native view) recordings that reconstruct a session for debugging UX/errors
(FullStory, LogRocket, PostHog, Sentry Replay). Powerful but **privacy-heavy** — mask inputs by
default, sample aggressively, and get consent.

## Privacy, consent & cost (the part that gets you in trouble)

- **PII**: never send emails/tokens/card numbers as event props or breadcrumbs. Scrub on the client.
- **Consent**: gate analytics/replay behind GDPR/CCPA consent; default to off in the EU until granted.
- **Sampling**: head-based sampling keeps a whole trace/session together; tune to control vendor bill.

## Mobile-specific concerns

- **Crashlytics / Sentry-native** for native crashes (JS handler can't catch a native segfault).
- **Offline-first**: queue events/crashes on device, upload on reconnect.
- **OTA updates** (Expo Updates): tag the update id so you know *which* JS bundle crashed.

## Vendor matrix

| Concern | Common vendors |
|---------|----------------|
| Product analytics | GA4, PostHog, Amplitude, Mixpanel, Segment (router) |
| Error/crash (web) | Sentry, Datadog RUM, Bugsnag, Rollbar |
| Error/crash (mobile) | Firebase Crashlytics, Sentry, Bugsnag |
| Performance / RUM | Datadog RUM, New Relic, SpeedCurve, Vercel Analytics |
| Session replay | FullStory, LogRocket, PostHog, Sentry Replay |

Swapping any of these should touch **only** the `ObservabilitySink` implementation — that's the whole
point of the seam.
