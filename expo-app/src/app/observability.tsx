/**
 * CONCEPT: Mobile observability — analytics + crash reporting, vendor-agnostic
 *
 * App code depends only on the ObservabilityProvider's API (track / captureException), never a
 * vendor SDK. The default MemorySink buffers in memory + logs to console, so this runs with no API
 * keys. Swap the sink for Firebase Analytics + Crashlytics (or Sentry) to ship — no screen changes.
 *
 * Demonstrated here:
 *   - track() custom analytics events
 *   - captureException() for handled errors
 *   - a global JS error via ErrorUtils (the RN crash hook)
 *   - an ErrorBoundary recovering from a render crash
 *   - live buffers via useSyncExternalStore
 */

import { useState } from 'react';
import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { BottomTabInset, MaxContentWidth, Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';
import { ErrorBoundary } from '@/observability/ErrorBoundary';
import { useObservability, useObservabilitySnapshot } from '@/observability/ObservabilityProvider';

function Bomb({ explode }: { explode: boolean }) {
  if (explode) throw new Error('💥 Render exploded (caught by ErrorBoundary)');
  return <ThemedText type="small" themeColor="textSecondary">Component is healthy.</ThemedText>;
}

export default function ObservabilityScreen() {
  const theme = useTheme();
  const insets = useSafeAreaInsets();
  const { track, captureException } = useObservability();
  const { events, errors } = useObservabilitySnapshot();
  const [explode, setExplode] = useState(false);

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={[
          styles.scrollContent,
          { paddingBottom: insets.bottom + BottomTabInset + Spacing.four, paddingTop: Spacing.four },
        ]}
      >
        <View style={{ maxWidth: MaxContentWidth, alignSelf: 'center', width: '100%', paddingHorizontal: Spacing.three, gap: Spacing.four }}>
          <View>
            <ThemedText type="subtitle">Mobile Observability</ThemedText>
            <ThemedText type="small" themeColor="textSecondary" style={{ marginTop: Spacing.one }}>
              Vendor-agnostic analytics + crash reporting. Default sink buffers in memory.
            </ThemedText>
          </View>

          <Section title="Analytics — custom events" theme={theme}>
            <DemoButton label='track "cta_pressed"' color="#3b82f6" onPress={() => track('cta_pressed', { screen: 'observability' })} />
            <DemoButton label='track "purchase"' color="#16a34a" onPress={() => track('purchase', { amount: 9.99 })} />
          </Section>

          <Section title="Crash reporting" theme={theme}>
            <DemoButton
              label="captureException() (handled)"
              color="#f59e0b"
              onPress={() => {
                try {
                  throw new Error('Handled failure in press handler');
                } catch (e) {
                  captureException(e, { handler: 'manual' });
                }
              }}
            />
            <DemoButton
              label="Throw global error (ErrorUtils)"
              color="#dc2626"
              onPress={() => {
                // Uncaught → RN global handler reports it (non-fatal in dev).
                setTimeout(() => {
                  throw new Error('Uncaught error via setTimeout');
                }, 0);
              }}
            />
          </Section>

          <Section title="Error boundary — recover from a render crash" theme={theme}>
            <DemoButton label="Throw render error" color="#dc2626" onPress={() => setExplode(true)} />
            <ErrorBoundary
              fallback={(error, reset) => (
                <View style={{ gap: Spacing.two }}>
                  <ThemedText type="small">Caught: {error.message}</ThemedText>
                  <DemoButton label="Recover" color="#16a34a" onPress={() => { setExplode(false); reset(); }} />
                </View>
              )}
            >
              <Bomb explode={explode} />
            </ErrorBoundary>
          </Section>

          <Section title={`Live signals — ${events.length} events · ${errors.length} errors`} theme={theme}>
            {events.slice(-5).reverse().map((e, i) => (
              <ThemedText key={`e${i}`} type="code" style={{ fontSize: 11 }}>
                {e.kind}: {e.name}
              </ThemedText>
            ))}
            {errors.slice(-5).reverse().map((e, i) => (
              <ThemedText key={`x${i}`} type="code" style={{ fontSize: 11, color: '#e5484d' }}>
                {e.source}: {e.message}
              </ThemedText>
            ))}
            {events.length === 0 && errors.length === 0 && (
              <ThemedText type="small" themeColor="textSecondary">none yet — tap a button above</ThemedText>
            )}
          </Section>
        </View>
      </ScrollView>
    </ThemedView>
  );
}

function Section({ title, theme, children }: { title: string; theme: ReturnType<typeof useTheme>; children: React.ReactNode }) {
  return (
    <ThemedView type="backgroundElement" style={styles.section}>
      <ThemedText type="smallBold">{title}</ThemedText>
      <View style={{ gap: Spacing.two, marginTop: Spacing.two }}>{children}</View>
    </ThemedView>
  );
}

function DemoButton({ label, color, onPress }: { label: string; color: string; onPress: () => void }) {
  return (
    <Pressable onPress={onPress} style={[styles.button, { backgroundColor: color }]}>
      <Text style={styles.buttonText}>{label}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  scrollContent: {},
  section: { padding: Spacing.three, borderRadius: Spacing.two, gap: Spacing.one },
  button: { paddingHorizontal: Spacing.three, paddingVertical: Spacing.two, borderRadius: Spacing.two, alignSelf: 'flex-start' },
  buttonText: { color: '#fff', fontWeight: '600', fontSize: 13 },
});
