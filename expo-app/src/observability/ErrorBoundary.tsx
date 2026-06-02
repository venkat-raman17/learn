import { Component, type ReactNode } from 'react';
import { Pressable, StyleSheet, Text, View } from 'react-native';
import { sink } from './MemorySink';

interface Props {
  children: ReactNode;
  fallback?: (error: Error, reset: () => void) => ReactNode;
}
interface State {
  error: Error | null;
}

/**
 * Catches render-time errors in the subtree, reports them to the observability {@link sink}, and
 * shows a recoverable fallback instead of unmounting (the RN equivalent of a white screen). Error
 * boundaries must be class components.
 */
export class ErrorBoundary extends Component<Props, State> {
  state: State = { error: null };

  static getDerivedStateFromError(error: Error): State {
    return { error };
  }

  componentDidCatch(error: Error): void {
    sink.captureError({
      message: error.message,
      stack: error.stack,
      source: 'error-boundary',
      fatal: false,
      ts: Date.now(),
    });
  }

  reset = (): void => this.setState({ error: null });

  render(): ReactNode {
    const { error } = this.state;
    if (error) {
      if (this.props.fallback) return this.props.fallback(error, this.reset);
      return (
        <View accessibilityRole="alert" style={styles.fallback}>
          <Text style={styles.title}>Something went wrong.</Text>
          <Text style={styles.message}>{error.message}</Text>
          <Pressable onPress={this.reset} style={styles.button}>
            <Text style={styles.buttonText}>Try again</Text>
          </Pressable>
        </View>
      );
    }
    return this.props.children;
  }
}

const styles = StyleSheet.create({
  fallback: { padding: 16, borderRadius: 8, borderWidth: 1, borderColor: '#e5484d', gap: 8 },
  title: { fontWeight: '700', fontSize: 16 },
  message: { opacity: 0.8 },
  button: { alignSelf: 'flex-start', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 6, backgroundColor: '#e5484d' },
  buttonText: { color: '#fff', fontWeight: '600' },
});
