import { Component, type ErrorInfo, type ReactNode } from 'react';
import { sink } from './MemorySink';

interface Props {
  children: ReactNode;
  /** Optional custom fallback; receives the error and a reset callback. */
  fallback?: (error: Error, reset: () => void) => ReactNode;
}

interface State {
  error: Error | null;
}

/**
 * Catches render-time errors in the subtree, reports them to the observability {@link sink}, and
 * shows a recoverable fallback instead of unmounting the whole app (a white screen). Error
 * boundaries must be class components — there is no hook equivalent for `componentDidCatch`.
 */
export class ErrorBoundary extends Component<Props, State> {
  state: State = { error: null };

  static getDerivedStateFromError(error: Error): State {
    return { error };
  }

  componentDidCatch(error: Error, info: ErrorInfo): void {
    sink.captureError({
      message: error.message,
      stack: error.stack,
      source: 'error-boundary',
      context: { componentStack: info.componentStack },
      ts: Date.now(),
    });
  }

  reset = (): void => this.setState({ error: null });

  render(): ReactNode {
    const { error } = this.state;
    if (error) {
      if (this.props.fallback) return this.props.fallback(error, this.reset);
      return (
        <div role="alert" className="observability-fallback">
          <h2>Something went wrong.</h2>
          <p>{error.message}</p>
          <button type="button" onClick={this.reset}>
            Try again
          </button>
        </div>
      );
    }
    return this.props.children;
  }
}
