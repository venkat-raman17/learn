import type { ObservabilitySink, WebVitalName } from './types';

// A from-scratch Core Web Vitals collector via the Performance APIs — the mechanics behind the
// `web-vitals` npm package, kept dependency-free (same teaching approach as the backend metrics
// kata). Each metric is feature-detected so it degrades silently in jsdom/older browsers.
//
//   LCP  — largest-contentful-paint entries (report the last one at page hide)
//   CLS  — sum of layout-shift values without recent input
//   FCP  — first-contentful-paint paint entry
//   TTFB — navigation responseStart
//
// INP (Interaction to Next Paint) needs event-timing buffering + the "next paint" heuristic; it is
// covered in docs/hld/frontend-observability.md rather than reimplemented here.

type CleanupFn = () => void;

function safeObserve(
  type: string,
  callback: (entries: PerformanceEntryList) => void,
): CleanupFn | undefined {
  if (typeof PerformanceObserver === 'undefined') return undefined;
  try {
    const observer = new PerformanceObserver((list) => callback(list.getEntries()));
    // `buffered: true` replays entries that fired before we subscribed.
    observer.observe({ type, buffered: true } as PerformanceObserverInit);
    return () => observer.disconnect();
  } catch {
    return undefined; // unsupported entry type
  }
}

/**
 * Starts collecting vitals into the given sink. Returns a cleanup function that disconnects every
 * observer. No-ops gracefully where the Performance APIs are unavailable.
 */
export function collectWebVitals(sink: ObservabilitySink): CleanupFn {
  const cleanups: CleanupFn[] = [];
  const report = (name: WebVitalName, value: number) =>
    sink.recordVital({ name, value, ts: Date.now() });

  // LCP — keep the latest candidate, flush when the page is hidden.
  let lcp = 0;
  const lcpCleanup = safeObserve('largest-contentful-paint', (entries) => {
    const last = entries[entries.length - 1] as PerformanceEntry & { startTime: number };
    if (last) lcp = last.startTime;
  });
  if (lcpCleanup) cleanups.push(lcpCleanup);

  // CLS — accumulate shifts that weren't caused by recent user input.
  let cls = 0;
  const clsCleanup = safeObserve('layout-shift', (entries) => {
    for (const entry of entries as Array<PerformanceEntry & { value: number; hadRecentInput: boolean }>) {
      if (!entry.hadRecentInput) cls += entry.value;
    }
  });
  if (clsCleanup) cleanups.push(clsCleanup);

  // FCP — single paint entry.
  const fcpCleanup = safeObserve('paint', (entries) => {
    for (const entry of entries) {
      if (entry.name === 'first-contentful-paint') report('FCP', entry.startTime);
    }
  });
  if (fcpCleanup) cleanups.push(fcpCleanup);

  // TTFB — from the navigation timing entry.
  if (typeof performance !== 'undefined' && performance.getEntriesByType) {
    const [nav] = performance.getEntriesByType('navigation') as PerformanceNavigationTiming[];
    if (nav) report('TTFB', nav.responseStart);
  }

  // Flush the "final value" metrics when the user leaves the page.
  const flush = () => {
    report('LCP', lcp);
    report('CLS', cls);
  };
  if (typeof document !== 'undefined') {
    const onHidden = () => {
      if (document.visibilityState === 'hidden') flush();
    };
    document.addEventListener('visibilitychange', onHidden);
    cleanups.push(() => document.removeEventListener('visibilitychange', onHidden));
  }

  return () => cleanups.forEach((fn) => fn());
}
