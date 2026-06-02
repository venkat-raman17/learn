// Provider-agnostic front-end observability contracts.
//
// The whole point of this layer: app code depends ONLY on these interfaces, never on a vendor SDK.
// The default implementation (`MemorySink`) buffers in memory + logs to the console so everything
// runs with zero API keys. To go to production you implement `ObservabilitySink` once against a
// real vendor (GA4 / PostHog / Segment for analytics; Sentry / Datadog RUM / Bugsnag for errors)
// and swap it at the provider — no page/component changes. This mirrors the headless-CMS mock
// pattern used in nextjs-app (`src/lib/cms.ts`).

export type AnalyticsEventKind = 'page_view' | 'track';

export interface AnalyticsEvent {
  kind: AnalyticsEventKind;
  /** e.g. "/dashboard" for a page_view, or "cta_clicked" for a custom event. */
  name: string;
  props?: Record<string, unknown>;
  ts: number;
}

export type ErrorSource =
  | 'error-boundary'
  | 'window.onerror'
  | 'unhandledrejection'
  | 'manual';

export interface CapturedError {
  message: string;
  stack?: string;
  source: ErrorSource;
  context?: Record<string, unknown>;
  ts: number;
}

/** Core Web Vitals (and friends). INP is intentionally documented-but-not-sampled here. */
export type WebVitalName = 'LCP' | 'CLS' | 'FCP' | 'TTFB';

export interface WebVital {
  name: WebVitalName;
  value: number;
  ts: number;
}

/**
 * The single seam every vendor plugs into. Implement this once per provider.
 */
export interface ObservabilitySink {
  trackEvent(event: AnalyticsEvent): void;
  captureError(error: CapturedError): void;
  recordVital(vital: WebVital): void;
}
