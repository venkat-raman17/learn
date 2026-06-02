import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useSyncExternalStore,
  type ReactNode,
} from 'react';
import { sink, MemorySink } from './MemorySink';
import { installGlobalErrorHandlers } from './globalHandlers';
import { collectWebVitals } from './webVitals';
import type { CapturedError } from './types';

// React surface over the vendor-agnostic sink. Components call `track` / `pageView` /
// `captureException`; they never import a vendor SDK. The provider also wires the browser-level
// concerns (global error handlers + web-vitals) for the lifetime of the app.

interface ObservabilityApi {
  track(name: string, props?: Record<string, unknown>): void;
  pageView(name: string, props?: Record<string, unknown>): void;
  captureException(error: unknown, context?: Record<string, unknown>): void;
}

const ObservabilityContext = createContext<ObservabilityApi | null>(null);

export function ObservabilityProvider({ children }: { children: ReactNode }) {
  useEffect(() => {
    const uninstallHandlers = installGlobalErrorHandlers(sink);
    const stopVitals = collectWebVitals(sink);
    return () => {
      uninstallHandlers();
      stopVitals();
    };
  }, []);

  const api = useMemo<ObservabilityApi>(
    () => ({
      track: (name, props) => sink.trackEvent({ kind: 'track', name, props, ts: Date.now() }),
      pageView: (name, props) =>
        sink.trackEvent({ kind: 'page_view', name, props, ts: Date.now() }),
      captureException: (error, context) => {
        const err: CapturedError = {
          message: error instanceof Error ? error.message : String(error),
          stack: error instanceof Error ? error.stack : undefined,
          source: 'manual',
          context,
          ts: Date.now(),
        };
        sink.captureError(err);
      },
    }),
    [],
  );

  return <ObservabilityContext.Provider value={api}>{children}</ObservabilityContext.Provider>;
}

/** Actions hook — track events / capture exceptions from any component. */
export function useObservability(): ObservabilityApi {
  const api = useContext(ObservabilityContext);
  if (!api) throw new Error('useObservability must be used within <ObservabilityProvider>');
  return api;
}

/**
 * Live read of the buffered signals for UI. Re-renders only when the sink changes, via the sink's
 * monotonic {@link MemorySink#version} as the snapshot key.
 */
export function useObservabilitySnapshot(source: MemorySink = sink) {
  useSyncExternalStore(
    (onChange) => source.subscribe(onChange),
    () => source.version,
    () => source.version,
  );
  return { events: source.events, errors: source.errors, vitals: source.vitals };
}
