import { DarkTheme, DefaultTheme, ThemeProvider } from 'expo-router';
import { useColorScheme } from 'react-native';

import { AnimatedSplashOverlay } from '@/components/animated-icon';
import AppTabs from '@/components/app-tabs';
import { ErrorBoundary } from '@/observability/ErrorBoundary';
import { ObservabilityProvider } from '@/observability/ObservabilityProvider';

export default function TabLayout() {
  const colorScheme = useColorScheme();
  return (
    <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
      {/* App-wide observability: global crash handler + screen-view analytics, with an
          ErrorBoundary so a render crash shows a recoverable fallback, not a blank app. */}
      <ObservabilityProvider>
        <ErrorBoundary>
          <AnimatedSplashOverlay />
          <AppTabs />
        </ErrorBoundary>
      </ObservabilityProvider>
    </ThemeProvider>
  );
}
