import { MemorySink } from './MemorySink';
import type { AnalyticsEvent, CapturedError } from './types';

// UNIT TEST (jest-expo) — pure logic of the mobile sink. No RN rendering.

const event = (name: string): AnalyticsEvent => ({ kind: 'track', name, ts: Date.now() });
const error = (message: string, fatal = false): CapturedError => ({
  message,
  source: 'global-handler',
  fatal,
  ts: Date.now(),
});

describe('MemorySink (mobile)', () => {
  let sink: MemorySink;

  beforeEach(() => {
    sink = new MemorySink({ log: false });
  });

  it('buffers analytics events and errors separately', () => {
    sink.trackEvent(event('screen_open'));
    sink.captureError(error('native crash', true));

    expect(sink.events).toHaveLength(1);
    expect(sink.errors).toHaveLength(1);
    expect(sink.events[0].name).toBe('screen_open');
    expect(sink.errors[0]).toMatchObject({ message: 'native crash', fatal: true });
  });

  it('bumps version and notifies subscribers on each write', () => {
    const listener = jest.fn();
    const unsubscribe = sink.subscribe(listener);

    expect(sink.version).toBe(0);
    sink.trackEvent(event('a'));
    sink.captureError(error('b'));
    expect(sink.version).toBe(2);
    expect(listener).toHaveBeenCalledTimes(2);

    unsubscribe();
    sink.trackEvent(event('c'));
    expect(listener).toHaveBeenCalledTimes(2);
  });

  it('caps each buffer at `max`, dropping the oldest', () => {
    const small = new MemorySink({ max: 2, log: false });
    small.trackEvent(event('e0'));
    small.trackEvent(event('e1'));
    small.trackEvent(event('e2'));

    expect(small.events).toHaveLength(2);
    expect(small.events.map((e) => e.name)).toEqual(['e1', 'e2']);
  });

  it('clear() empties buffers and notifies', () => {
    const listener = jest.fn();
    sink.subscribe(listener);
    sink.trackEvent(event('a'));
    sink.clear();

    expect(sink.events).toHaveLength(0);
    expect(sink.errors).toHaveLength(0);
    expect(listener).toHaveBeenCalledTimes(2);
  });
});
