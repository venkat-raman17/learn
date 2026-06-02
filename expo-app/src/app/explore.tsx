/**
 * CONCEPT: Platform-Specific Code in React Native
 *
 * React Native runs on iOS, Android, and web (via react-native-web).
 * There are three ways to write platform-specific code:
 *
 * 1. Platform.OS — runtime check (fine for small differences)
 *    `Platform.OS === 'ios' | 'android' | 'web'`
 *
 * 2. Platform.select() — picks from a map, type-safe
 *    `Platform.select({ ios: 'system', android: 'roboto', default: 'sans-serif' })`
 *
 * 3. File extensions — separate implementations per platform (clean, tree-shakeable)
 *    `PlatformInput.tsx`       → fallback (Android)
 *    `PlatformInput.ios.tsx`   → iOS-specific
 *    `PlatformInput.web.tsx`   → web-specific
 *    Metro/Webpack resolves the correct file at build time.
 *
 * This screen demonstrates all three patterns.
 */

import { Link } from 'expo-router';
import {
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { BottomTabInset, MaxContentWidth, Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

// --- Pattern 1: Platform.OS -------------------------------------------------

function PlatformBadge() {
  const colors: Record<string, string> = { ios: '#007AFF', android: '#3DDC84', web: '#FF6B35' };
  const color = colors[Platform.OS] ?? '#888';
  return (
    <View style={[styles.badge, { backgroundColor: color }]}>
      <Text style={styles.badgeText}>{Platform.OS.toUpperCase()}</Text>
    </View>
  );
}

// --- Pattern 2: Platform.select() -------------------------------------------

const FONT_FAMILY = Platform.select({
  ios:     'System',
  android: 'Roboto',
  web:     'system-ui, sans-serif',
  default: 'sans-serif',
});

// Numeric value selection
const HEADER_HEIGHT = Platform.select({ ios: 44, android: 56, default: 48 }) ?? 48;

// --- Pattern 3: Simulated platform split (demonstrating the concept) --------
// In a real project: create PlatformInput.tsx + PlatformInput.web.tsx
// Metro picks the .web.tsx file on web builds automatically.

function PlatformNativeInput() {
  const theme = useTheme();
  return (
    <TextInput
      placeholder="Native TextInput (iOS/Android)"
      style={[
        styles.nativeInput,
        {
          color: theme.text,
          borderColor: theme.backgroundElement,
          backgroundColor: theme.backgroundElement,
          // Android-specific ripple on focus
          ...Platform.select({
            android: { paddingLeft: Spacing.two },
            default: {},
          }),
        },
      ]}
      placeholderTextColor={theme.textSecondary}
    />
  );
}

// --- Sections ---------------------------------------------------------------

type Section = { title: string; badge: string; content: React.ReactNode };

export default function ExploreScreen() {
  const theme = useTheme();
  const insets = useSafeAreaInsets();

  const sections: Section[] = [
    {
      title: 'Platform.OS',
      badge: 'Runtime check',
      content: (
        <View style={{ gap: Spacing.two }}>
          <ThemedText type="small" themeColor="textSecondary">
            This device / environment reports:
          </ThemedText>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.two }}>
            <PlatformBadge />
            <ThemedText type="code">{JSON.stringify(Platform.constants, null, 2).slice(0, 80) + '…'}</ThemedText>
          </View>
        </View>
      ),
    },
    {
      title: 'Platform.select()',
      badge: 'Type-safe map',
      content: (
        <View style={{ gap: Spacing.two }}>
          {[
            ['Font family', FONT_FAMILY],
            ['Header height', `${HEADER_HEIGHT}px`],
            ['Version', Platform.Version?.toString() ?? 'n/a'],
            ['Is TV?', String(Platform.isTV ?? false)],
          ].map(([label, value]) => (
            <View key={label} style={styles.row}>
              <ThemedText type="small" themeColor="textSecondary" style={{ width: 140 }}>
                {label}
              </ThemedText>
              <ThemedText type="code">{value}</ThemedText>
            </View>
          ))}
        </View>
      ),
    },
    {
      title: 'Platform File Split',
      badge: '.web.tsx / .ios.tsx',
      content: (
        <View style={{ gap: Spacing.two }}>
          <ThemedText type="small" themeColor="textSecondary">
            Metro/webpack resolves the right file at build time:
          </ThemedText>
          <View style={[styles.codeBlock, { backgroundColor: theme.backgroundElement }]}>
            <ThemedText type="code" style={{ fontSize: 11 }}>
              {`PlatformInput.tsx        ← default (Android)\nPlatformInput.ios.tsx    ← iOS\nPlatformInput.web.tsx    ← web (HTML <input>)`}
            </ThemedText>
          </View>
          <ThemedText type="small" themeColor="textSecondary">
            Current platform renders:
          </ThemedText>
          <PlatformNativeInput />
        </View>
      ),
    },
    {
      title: 'StyleSheet vs Inline',
      badge: 'Performance',
      content: (
        <View style={{ gap: Spacing.two }}>
          <ThemedText type="small" themeColor="textSecondary">
            {`StyleSheet.create() processes styles at startup:\n`}
            {'• Styles are registered by ID (faster lookups)\n'}
            {'• Validated in development\n'}
            {'• Flattened before being sent to the native thread\n\n'}
            {'Inline styles ({{}}) are re-evaluated every render — use for dynamic values only.'}
          </ThemedText>
          <TouchableOpacity
            style={[styles.button, { backgroundColor: theme.backgroundElement }]}
            onPress={() => {/* demo interaction */}}
          >
            <ThemedText type="small">StyleSheet button (tap me)</ThemedText>
          </TouchableOpacity>
        </View>
      ),
    },
    {
      title: 'Safe Area Insets',
      badge: 'Device awareness',
      content: (
        <View style={{ gap: Spacing.two }}>
          <ThemedText type="small" themeColor="textSecondary">
            useSafeAreaInsets() avoids content being hidden by notch/home indicator:
          </ThemedText>
          {(['top', 'bottom', 'left', 'right'] as const).map((side) => (
            <View key={side} style={styles.row}>
              <ThemedText type="small" themeColor="textSecondary" style={{ width: 60 }}>
                {side}
              </ThemedText>
              <ThemedText type="code">{insets[side]}px</ThemedText>
            </View>
          ))}
        </View>
      ),
    },
  ];

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={[
          styles.scrollContent,
          {
            paddingBottom: insets.bottom + BottomTabInset + Spacing.four,
            paddingTop: Platform.OS === 'web' ? Spacing.six : Spacing.four,
          },
        ]}
        showsVerticalScrollIndicator={Platform.OS !== 'web'}
      >
        <View style={{ maxWidth: MaxContentWidth, alignSelf: 'center', width: '100%' }}>
          <ThemedText type="subtitle" style={{ paddingHorizontal: Spacing.three }}>
            Platform Patterns
          </ThemedText>
          <ThemedText
            type="small"
            themeColor="textSecondary"
            style={{ paddingHorizontal: Spacing.three, marginTop: Spacing.one, marginBottom: Spacing.three }}
          >
            Three ways to write platform-specific code in React Native.
          </ThemedText>

          <Link href="/observability" style={{ paddingHorizontal: Spacing.three, marginBottom: Spacing.four }}>
            <ThemedText type="smallBold" style={{ color: '#3b82f6' }}>
              → Open Observability demo (analytics + crash reporting)
            </ThemedText>
          </Link>

          <View style={{ gap: Spacing.four, paddingHorizontal: Spacing.three }}>
            {sections.map((section) => (
              <ThemedView key={section.title} type="backgroundElement" style={styles.section}>
                <View style={styles.sectionHeader}>
                  <ThemedText type="smallBold">{section.title}</ThemedText>
                  <View style={[styles.sectionBadge, { backgroundColor: theme.backgroundSelected }]}>
                    <ThemedText type="code" style={{ fontSize: 10 }}>{section.badge}</ThemedText>
                  </View>
                </View>
                {section.content}
              </ThemedView>
            ))}
          </View>
        </View>
      </ScrollView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  scrollContent: {},
  section: {
    padding: Spacing.three,
    borderRadius: Spacing.two,
    gap: Spacing.two,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  sectionBadge: {
    paddingHorizontal: Spacing.two,
    paddingVertical: Spacing.half,
    borderRadius: Spacing.one,
  },
  badge: {
    paddingHorizontal: Spacing.two,
    paddingVertical: Spacing.one,
    borderRadius: Spacing.one,
  },
  badgeText: { color: '#fff', fontSize: 12, fontWeight: '700' },
  row: { flexDirection: 'row', alignItems: 'center', gap: Spacing.two },
  nativeInput: {
    borderWidth: 1,
    borderRadius: Spacing.one,
    padding: Spacing.two,
    fontSize: 14,
  },
  codeBlock: {
    padding: Spacing.two,
    borderRadius: Spacing.one,
  },
  button: {
    padding: Spacing.two,
    borderRadius: Spacing.two,
    alignItems: 'center',
    alignSelf: 'flex-start',
  },
});
