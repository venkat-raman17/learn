import { usePathname } from 'expo-router';
import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useSyncExternalStore,
  type ReactNode,
} from 'react';
import { installGlobalErrorHandler } from './globalHandlers';
import { MemorySink, sink } from './MemorySink';
import type { CapturedError } from './types';

// React surface over the vendor-agnostic sink. Screens call `track` / `captureException`; navigation
// changes are auto-tracked as screen_view via expo-router's usePathname (the mobile analogue of the
// web SPA's per-tab page_view).

interface ObservabilityApi {
  track(name: string, props?: Record<string, unknown>): void;
  captureException(error: unknown, context?: Record<string, unknown>): void;
}

const ObservabilityContext = createContext<ObservabilityApi | null>(null);

export function ObservabilityProvider({ children }: { children: ReactNode }) {
  const pathname = usePathname();

  // Install the RN global error handler once for the app's lifetime.
  useEffect(() => installGlobalErrorHandler(sink), []);

  // Auto-track navigation as screen views.
  useEffect(() => {
    sink.trackEvent({ kind: 'screen_view', name: pathname, ts: Date.now() });
  }, [pathname]);

  const api = useMemo<ObservabilityApi>(
    () => ({
      track: (name, props) => sink.trackEvent({ kind: 'track', name, props, ts: Date.now() }),
      captureException: (error, context) => {
        const err: CapturedError = {
          message: error instanceof Error ? error.message : String(error),
          stack: error instanceof Error ? error.stack : undefined,
          source: 'manual',
          fatal: false,
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

export function useObservability(): ObservabilityApi {
  const api = useContext(ObservabilityContext);
  if (!api) throw new Error('useObservability must be used within <ObservabilityProvider>');
  return api;
}

/** Live read of buffered signals; re-renders only when the sink changes (via its `version`). */
export function useObservabilitySnapshot(source: MemorySink = sink) {
  useSyncExternalStore(
    (onChange) => source.subscribe(onChange),
    () => source.version,
    () => source.version,
  );
  return { events: source.events, errors: source.errors };
}
