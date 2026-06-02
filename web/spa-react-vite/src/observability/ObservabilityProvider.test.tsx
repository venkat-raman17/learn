import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ObservabilityProvider, useObservability } from './ObservabilityProvider';
import { ErrorBoundary } from './ErrorBoundary';
import { sink } from './MemorySink';

// COMPONENT / INTEGRATION TEST — exercises the React layer against the singleton sink (which the
// boundary and global handlers share). Reset the shared buffers before each test.

beforeEach(() => sink.clear());

function TrackButton() {
  const { track } = useObservability();
  return <button onClick={() => track('cta_clicked', { cta: 'primary' })}>fire</button>;
}

function Bomb(): never {
  throw new Error('render exploded');
}

describe('ObservabilityProvider', () => {
  it('track() from a component lands in the sink', async () => {
    render(
      <ObservabilityProvider>
        <TrackButton />
      </ObservabilityProvider>,
    );

    await userEvent.click(screen.getByText('fire'));

    expect(sink.events).toHaveLength(1);
    expect(sink.events[0]).toMatchObject({ kind: 'track', name: 'cta_clicked', props: { cta: 'primary' } });
  });

  it('useObservability outside a provider throws', () => {
    // Silence React's error log for this expected throw.
    const spy = vi.spyOn(console, 'error').mockImplementation(() => {});
    expect(() => render(<TrackButton />)).toThrow(/must be used within/);
    spy.mockRestore();
  });
});

describe('ErrorBoundary', () => {
  it('catches a render crash, shows fallback, and reports to the sink', () => {
    const spy = vi.spyOn(console, 'error').mockImplementation(() => {});

    render(
      <ErrorBoundary>
        <Bomb />
      </ErrorBoundary>,
    );

    // Default fallback UI is shown instead of unmounting.
    expect(screen.getByRole('alert')).toBeInTheDocument();
    expect(screen.getByText(/something went wrong/i)).toBeInTheDocument();

    // The crash was captured with the right provenance.
    expect(sink.errors).toHaveLength(1);
    expect(sink.errors[0]).toMatchObject({ source: 'error-boundary', message: 'render exploded' });

    spy.mockRestore();
  });
});
