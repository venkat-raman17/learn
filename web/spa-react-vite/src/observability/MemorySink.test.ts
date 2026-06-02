import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MemorySink } from './MemorySink';
import type { AnalyticsEvent, CapturedError, WebVital } from './types';

// UNIT TEST — pure logic of the default sink. No DOM, no React.

const event = (name: string): AnalyticsEvent => ({ kind: 'track', name, ts: Date.now() });
const error = (message: string): CapturedError => ({ message, source: 'manual', ts: Date.now() });
const vital = (value: number): WebVital => ({ name: 'LCP', value, ts: Date.now() });

describe('MemorySink', () => {
  let sink: MemorySink;

  beforeEach(() => {
    sink = new MemorySink({ log: false });
  });

  it('buffers events, errors and vitals separately', () => {
    sink.trackEvent(event('a'));
    sink.captureError(error('boom'));
    sink.recordVital(vital(1200));

    expect(sink.events).toHaveLength(1);
    expect(sink.errors).toHaveLength(1);
    expect(sink.vitals).toHaveLength(1);
    expect(sink.events[0].name).toBe('a');
    expect(sink.errors[0].message).toBe('boom');
    expect(sink.vitals[0].value).toBe(1200);
  });

  it('bumps version and notifies subscribers on each write', () => {
    const listener = vi.fn();
    const unsubscribe = sink.subscribe(listener);

    expect(sink.version).toBe(0);
    sink.trackEvent(event('a'));
    sink.captureError(error('b'));

    expect(sink.version).toBe(2);
    expect(listener).toHaveBeenCalledTimes(2);

    unsubscribe();
    sink.trackEvent(event('c'));
    expect(listener).toHaveBeenCalledTimes(2); // no longer notified
  });

  it('caps each buffer at `max`, dropping the oldest', () => {
    const small = new MemorySink({ max: 3, log: false });
    for (let i = 0; i < 5; i++) small.trackEvent(event(`e${i}`));

    expect(small.events).toHaveLength(3);
    expect(small.events.map((e) => e.name)).toEqual(['e2', 'e3', 'e4']);
  });

  it('clear() empties all buffers and notifies', () => {
    const listener = vi.fn();
    sink.subscribe(listener);
    sink.trackEvent(event('a'));
    sink.clear();

    expect(sink.events).toHaveLength(0);
    expect(sink.errors).toHaveLength(0);
    expect(sink.vitals).toHaveLength(0);
    expect(listener).toHaveBeenCalledTimes(2); // one for track, one for clear
  });
});
