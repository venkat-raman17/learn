import type { ObservabilitySink } from './types';

// React Native's global JS error hook. Unlike the browser (window.onerror), RN exposes `ErrorUtils`:
// the runtime calls the registered global handler for any uncaught JS error, with a `fatal` flag.
// This is exactly where Crashlytics/Sentry chain themselves in. We preserve the previous handler so
// the red-box / app crash behaviour still happens after we report.
//
// Promise-rejection tracking is engine-specific (Hermes) and is covered in
// docs/hld/frontend-observability.md rather than wired here.

type ErrorHandler = (error: unknown, isFatal?: boolean) => void;
interface ErrorUtilsLike {
  getGlobalHandler?: () => ErrorHandler;
  setGlobalHandler: (handler: ErrorHandler) => void;
}

type CleanupFn = () => void;

export function installGlobalErrorHandler(sink: ObservabilitySink): CleanupFn {
  const errorUtils = (globalThis as { ErrorUtils?: ErrorUtilsLike }).ErrorUtils;
  if (!errorUtils) return () => {};

  const previous = errorUtils.getGlobalHandler?.();

  const handler: ErrorHandler = (error, isFatal) => {
    const err = error instanceof Error ? error : new Error(String(error));
    sink.captureError({
      message: err.message,
      stack: err.stack,
      source: 'global-handler',
      fatal: Boolean(isFatal),
      ts: Date.now(),
    });
    previous?.(error, isFatal); // keep default crash/red-box behaviour
  };

  errorUtils.setGlobalHandler(handler);
  return () => {
    if (previous) errorUtils.setGlobalHandler(previous);
  };
}
