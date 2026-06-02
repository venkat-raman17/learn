// Provider-agnostic mobile observability contracts (mirrors the web SPA's layer).
//
// App/screen code depends ONLY on these interfaces — never a vendor SDK. The default sink buffers
// in memory + logs to console (zero API keys). To ship, implement `ObservabilitySink` once against
// a vendor — Firebase Analytics / Segment / Amplitude for analytics; Firebase Crashlytics / Sentry
// / Bugsnag for crashes — and swap it at the provider. No screen changes.

export type AnalyticsEventKind = 'screen_view' | 'track';

export interface AnalyticsEvent {
  kind: AnalyticsEventKind;
  /** e.g. "/explore" for a screen_view, or "cta_pressed" for a custom event. */
  name: string;
  props?: Record<string, unknown>;
  ts: number;
}

export type ErrorSource = 'error-boundary' | 'global-handler' | 'manual';

export interface CapturedError {
  message: string;
  stack?: string;
  source: ErrorSource;
  /** RN global handler reports whether the error was fatal (would crash the app). */
  fatal?: boolean;
  context?: Record<string, unknown>;
  ts: number;
}

/** The single seam every vendor plugs into. Implement once per provider. */
export interface ObservabilitySink {
  trackEvent(event: AnalyticsEvent): void;
  captureError(error: CapturedError): void;
}
