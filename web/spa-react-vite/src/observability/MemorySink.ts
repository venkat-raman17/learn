import type {
  AnalyticsEvent,
  CapturedError,
  ObservabilitySink,
  WebVital,
} from './types';

/**
 * The default, vendor-free {@link ObservabilitySink}: keeps a bounded in-memory buffer of every
 * signal and mirrors it to the console. This is what makes the demo runnable with no API keys and
 * what the tests assert against.
 *
 * It also exposes a tiny pub/sub so React UI can render the live buffers. A real sink (Sentry,
 * PostHog, …) would instead batch + POST these to a backend; the app code calling
 * {@link trackEvent} / {@link captureError} / {@link recordVital} would not change.
 */
export class MemorySink implements ObservabilitySink {
  readonly events: AnalyticsEvent[] = [];
  readonly errors: CapturedError[] = [];
  readonly vitals: WebVital[] = [];

  private readonly max: number;
  private readonly listeners = new Set<() => void>();
  private readonly log: boolean;
  private _version = 0;

  /** Monotonic counter bumped on every change — a stable snapshot key for useSyncExternalStore. */
  get version(): number {
    return this._version;
  }

  constructor(options: { max?: number; log?: boolean } = {}) {
    this.max = options.max ?? 200;
    this.log = options.log ?? true;
  }

  trackEvent(event: AnalyticsEvent): void {
    this.push(this.events, event);
    if (this.log) console.info('[analytics]', event.kind, event.name, event.props ?? {});
    this.emit();
  }

  captureError(error: CapturedError): void {
    this.push(this.errors, error);
    if (this.log) console.error('[crash]', error.source, error.message);
    this.emit();
  }

  recordVital(vital: WebVital): void {
    this.push(this.vitals, vital);
    if (this.log) console.info('[web-vital]', vital.name, Math.round(vital.value));
    this.emit();
  }

  /** Subscribe to buffer changes; returns an unsubscribe fn. */
  subscribe(listener: () => void): () => void {
    this.listeners.add(listener);
    return () => this.listeners.delete(listener);
  }

  clear(): void {
    this.events.length = 0;
    this.errors.length = 0;
    this.vitals.length = 0;
    this.emit();
  }

  private push<T>(buffer: T[], item: T): void {
    buffer.push(item);
    if (buffer.length > this.max) buffer.shift(); // drop oldest, keep memory bounded
  }

  private emit(): void {
    this._version++;
    for (const listener of this.listeners) listener();
  }
}

/**
 * App-wide singleton. Class components (the {@link import('./ErrorBoundary')}) and non-React global
 * handlers can't read React context, so they share this instance. The React provider subscribes to
 * it for live UI. Swap this construction for a real sink in one place to ship to production.
 */
export const sink = new MemorySink();
