import type { ObservabilitySink } from './types';

// Catch-all error capture — the two browser hooks every crash-reporting SDK installs:
//   window.onerror             → uncaught synchronous errors
//   unhandledrejection         → promise rejections with no .catch()
// A React ErrorBoundary only catches errors thrown during render; these cover everything else.

type CleanupFn = () => void;

export function installGlobalErrorHandlers(sink: ObservabilitySink): CleanupFn {
  if (typeof window === 'undefined') return () => {};

  const onError = (event: ErrorEvent) => {
    sink.captureError({
      message: event.message || 'Uncaught error',
      stack: event.error?.stack,
      source: 'window.onerror',
      context: { filename: event.filename, line: event.lineno, col: event.colno },
      ts: Date.now(),
    });
  };

  const onRejection = (event: PromiseRejectionEvent) => {
    const reason = event.reason;
    sink.captureError({
      message: reason instanceof Error ? reason.message : String(reason),
      stack: reason instanceof Error ? reason.stack : undefined,
      source: 'unhandledrejection',
      ts: Date.now(),
    });
  };

  window.addEventListener('error', onError);
  window.addEventListener('unhandledrejection', onRejection);

  return () => {
    window.removeEventListener('error', onError);
    window.removeEventListener('unhandledrejection', onRejection);
  };
}
