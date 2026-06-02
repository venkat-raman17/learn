import { useState } from 'react';
import { Button } from '../lld/button/Button';
import { ErrorBoundary } from './ErrorBoundary';
import { useObservability, useObservabilitySnapshot } from './ObservabilityProvider';

// A component that throws during render on demand — to demonstrate the ErrorBoundary path.
function Bomb({ explode }: { explode: boolean }) {
  if (explode) throw new Error('💥 Render exploded (caught by ErrorBoundary)');
  return <p style={{ fontSize: '0.875rem', opacity: 0.7 }}>Component is healthy.</p>;
}

export function ObservabilityPanel() {
  const { track, captureException } = useObservability();
  const { events, errors, vitals } = useObservabilitySnapshot();
  const [explode, setExplode] = useState(false);

  return (
    <div className="tab-panel">
      <div className="demo-section">
        <h2>Analytics — custom events</h2>
        <p style={{ marginBottom: '1rem', fontSize: '0.875rem' }}>
          App code calls a vendor-neutral <code>track()</code>. The default sink buffers in memory +
          logs to console; swap it for GA4 / PostHog / Segment with no component changes.
        </p>
        <div className="demo-row">
          <Button onClick={() => track('cta_clicked', { cta: 'primary', section: 'hero' })}>
            Track “cta_clicked”
          </Button>
          <Button variant="secondary" onClick={() => track('signup_started', { plan: 'pro' })}>
            Track “signup_started”
          </Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Crash reporting — handled & unhandled</h2>
        <div className="demo-row">
          <Button
            variant="danger"
            onClick={() => {
              try {
                throw new Error('Handled failure in click handler');
              } catch (e) {
                captureException(e, { handler: 'manual-capture' });
              }
            }}
          >
            captureException()
          </Button>
          <Button
            variant="secondary"
            onClick={() => {
              // No .catch → surfaces via the global unhandledrejection handler.
              void Promise.reject(new Error('Unhandled promise rejection'));
            }}
          >
            Reject a promise
          </Button>
        </div>
      </div>

      <div className="demo-section">
        <h2>Error boundary — recover from a render crash</h2>
        <div className="demo-row">
          <Button variant="danger" onClick={() => setExplode(true)}>
            Throw render error
          </Button>
        </div>
        <ErrorBoundary
          fallback={(error, reset) => (
            <div role="alert" className="selected-output">
              Caught: {error.message}{' '}
              <Button size="sm" onClick={() => { setExplode(false); reset(); }}>
                Recover
              </Button>
            </div>
          )}
        >
          <Bomb explode={explode} />
        </ErrorBoundary>
      </div>

      <div className="demo-section">
        <h2>Live signals</h2>
        <div className="obs-grid">
          <SignalList title={`Events (${events.length})`} items={events.map((e) => `${e.kind}: ${e.name}`)} />
          <SignalList title={`Errors (${errors.length})`} items={errors.map((e) => `${e.source}: ${e.message}`)} />
          <SignalList title={`Web Vitals (${vitals.length})`} items={vitals.map((v) => `${v.name}: ${Math.round(v.value)}`)} />
        </div>
      </div>
    </div>
  );
}

function SignalList({ title, items }: { title: string; items: string[] }) {
  return (
    <div className="obs-card">
      <h3 style={{ fontSize: '0.9rem', margin: '0 0 0.5rem' }}>{title}</h3>
      {items.length === 0 ? (
        <p style={{ fontSize: '0.8rem', opacity: 0.6 }}>none yet</p>
      ) : (
        <ul style={{ margin: 0, paddingLeft: '1rem', fontSize: '0.8rem' }}>
          {items.slice(-6).reverse().map((text, i) => (
            <li key={i}>{text}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
