import type { AnalyticsEvent, CapturedError, ObservabilitySink } from './types';

/**
 * Default vendor-free {@link ObservabilitySink}: bounded in-memory buffers + console mirroring, so
 * the app runs with no API keys and tests have something to assert against. A real sink (Crashlytics
 * / Sentry / Segment) would batch + upload these instead; calling code does not change.
 *
 * A tiny pub/sub + monotonic `version` lets React render the live buffers via useSyncExternalStore.
 */
export class MemorySink implements ObservabilitySink {
  readonly events: AnalyticsEvent[] = [];
  readonly errors: CapturedError[] = [];

  private readonly max: number;
  private readonly log: boolean;
  private readonly listeners = new Set<() => void>();
  private _version = 0;

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
    if (this.log) console.error('[crash]', error.source, error.fatal ? '(fatal)' : '', error.message);
    this.emit();
  }

  subscribe(listener: () => void): () => void {
    this.listeners.add(listener);
    return () => this.listeners.delete(listener);
  }

  clear(): void {
    this.events.length = 0;
    this.errors.length = 0;
    this.emit();
  }

  private push<T>(buffer: T[], item: T): void {
    buffer.push(item);
    if (buffer.length > this.max) buffer.shift();
  }

  private emit(): void {
    this._version++;
    for (const listener of this.listeners) listener();
  }
}

/** App-wide singleton, shared by the class ErrorBoundary, the global handler, and the provider. */
export const sink = new MemorySink();
