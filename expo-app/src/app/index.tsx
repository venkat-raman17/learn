/**
 * CONCEPT: FlatList — React Native's virtualized list
 *
 * FlatList renders only visible items + a configurable buffer, making it
 * efficient for long lists. Key patterns demonstrated:
 *
 *   - `data` + `renderItem`: the core rendering pair
 *   - `keyExtractor`: stable keys for React reconciliation
 *   - `onRefresh` + `refreshing`: pull-to-refresh (native gesture)
 *   - `ListHeaderComponent`: sticky content above the list
 *   - `ItemSeparatorComponent`: declarative separators
 *   - `onEndReached` + `onEndReachedThreshold`: pagination trigger
 *
 * Platform differences:
 *   - Pull-to-refresh uses the native UIRefreshControl on iOS,
 *     SwipeRefreshLayout on Android, and a custom spinner on web.
 *   - `contentInsetAdjustmentBehavior="automatic"` on iOS respects safe areas.
 */

import { useState, useCallback } from 'react';
import {
  FlatList,
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { BottomTabInset, MaxContentWidth, Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

type FeedItem = {
  id: string;
  title: string;
  subtitle: string;
  badge: string;
  badgeColor: string;
};

const INITIAL_DATA: FeedItem[] = [
  { id: '1', title: 'FlatList Virtualization', subtitle: 'Only visible rows are rendered — efficient for 10k+ items.', badge: 'Core', badgeColor: '#3b82f6' },
  { id: '2', title: 'Pull to Refresh', subtitle: 'Swipe down to trigger onRefresh → refetch data.', badge: 'UX', badgeColor: '#16a34a' },
  { id: '3', title: 'keyExtractor', subtitle: 'Stable string keys prevent unnecessary re-renders on list updates.', badge: 'Perf', badgeColor: '#9333ea' },
  { id: '4', title: 'ItemSeparatorComponent', subtitle: 'Renders between items but NOT before the first or after the last.', badge: 'UI', badgeColor: '#f59e0b' },
  { id: '5', title: 'onEndReached', subtitle: 'Fires when scroll position is within threshold of the list bottom — use for pagination.', badge: 'Infra', badgeColor: '#dc2626' },
  { id: '6', title: 'ListHeaderComponent', subtitle: 'Sticky header that scrolls with the list (not a fixed nav bar).', badge: 'UI', badgeColor: '#0891b2' },
  { id: '7', title: 'ListEmptyComponent', subtitle: 'Shown when data=[] — great for empty state UX.', badge: 'UX', badgeColor: '#7c3aed' },
  { id: '8', title: 'getItemLayout', subtitle: 'Optimizes scroll-to-index performance when all rows have fixed height.', badge: 'Perf', badgeColor: '#0d9488' },
];

function loadMoreItems(page: number): FeedItem[] {
  return Array.from({ length: 4 }, (_, i) => ({
    id: `page${page}-item${i}`,
    title: `Paginated item ${page * 4 + i + 1}`,
    subtitle: 'Loaded via onEndReached — simulates infinite scroll pagination.',
    badge: 'Page ' + page,
    badgeColor: '#888',
  }));
}

export default function HomeScreen() {
  const theme = useTheme();
  const [data, setData] = useState<FeedItem[]>(INITIAL_DATA);
  const [refreshing, setRefreshing] = useState(false);
  const [page, setPage] = useState(1);
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const onRefresh = useCallback(() => {
    setRefreshing(true);
    // Simulate network refetch
    setTimeout(() => {
      setData(INITIAL_DATA);
      setPage(1);
      setSelectedId(null);
      setRefreshing(false);
    }, 1200);
  }, []);

  const onEndReached = useCallback(() => {
    const more = loadMoreItems(page);
    setData((prev) => [...prev, ...more]);
    setPage((p) => p + 1);
  }, [page]);

  const renderItem = useCallback(({ item }: { item: FeedItem }) => {
    const isSelected = item.id === selectedId;
    return (
      <TouchableOpacity
        onPress={() => setSelectedId(isSelected ? null : item.id)}
        activeOpacity={0.7}
      >
        <ThemedView
          type="backgroundElement"
          style={[styles.card, isSelected && { borderColor: item.badgeColor, borderWidth: 2 }]}
        >
          <View style={styles.cardHeader}>
            <ThemedText type="smallBold" style={styles.cardTitle}>{item.title}</ThemedText>
            <View style={[styles.badge, { backgroundColor: item.badgeColor }]}>
              <Text style={styles.badgeText}>{item.badge}</Text>
            </View>
          </View>
          <ThemedText type="small" themeColor="textSecondary">{item.subtitle}</ThemedText>
          {isSelected && (
            <View style={[styles.selectedIndicator, { backgroundColor: item.badgeColor }]}>
              <Text style={styles.selectedText}>✓ selected — tap to deselect</Text>
            </View>
          )}
        </ThemedView>
      </TouchableOpacity>
    );
  }, [selectedId]);

  const renderSeparator = useCallback(() => (
    <View style={[styles.separator, { backgroundColor: theme.backgroundElement }]} />
  ), [theme]);

  const renderHeader = useCallback(() => (
    <ThemedView style={styles.header}>
      <ThemedText type="subtitle">React Native FlatList</ThemedText>
      <ThemedText type="small" themeColor="textSecondary" style={{ marginTop: Spacing.one }}>
        Running on: <ThemedText type="code">{Platform.OS}</ThemedText>
      </ThemedText>
      <ThemedText type="small" themeColor="textSecondary" style={{ marginTop: Spacing.one }}>
        {data.length} items · Pull down to refresh · Scroll to bottom to load more
      </ThemedText>
    </ThemedView>
  ), [data.length]);

  return (
    <ThemedView style={styles.container}>
      <SafeAreaView style={styles.safeArea}>
        <FlatList
          data={data}
          renderItem={renderItem}
          keyExtractor={(item) => item.id}
          onRefresh={onRefresh}
          refreshing={refreshing}
          onEndReached={onEndReached}
          onEndReachedThreshold={0.3}  // trigger when 30% from bottom
          ListHeaderComponent={renderHeader}
          ItemSeparatorComponent={renderSeparator}
          contentContainerStyle={[
            styles.listContent,
            { paddingBottom: BottomTabInset + Spacing.four },
          ]}
          style={{ flex: 1 }}
          // Platform-specific: iOS scroll indicator
          showsVerticalScrollIndicator={Platform.OS !== 'web'}
        />
      </SafeAreaView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, flexDirection: 'row', justifyContent: 'center' },
  safeArea: { flex: 1, maxWidth: MaxContentWidth },
  listContent: { paddingHorizontal: Spacing.three },
  header: { paddingVertical: Spacing.four, paddingHorizontal: Spacing.one },
  card: {
    padding: Spacing.three,
    borderRadius: Spacing.two,
    borderWidth: 0,
    gap: Spacing.one,
  },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  cardTitle: { flex: 1, marginRight: Spacing.two },
  badge: { paddingHorizontal: Spacing.two, paddingVertical: 2, borderRadius: Spacing.one },
  badgeText: { color: '#fff', fontSize: 10, fontWeight: '700' },
  separator: { height: Spacing.two, marginVertical: Spacing.one },
  selectedIndicator: { marginTop: Spacing.one, padding: Spacing.one, borderRadius: Spacing.one },
  selectedText: { color: '#fff', fontSize: 11, fontWeight: '600' },
});
