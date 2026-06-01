package com.venkat.backend.lld.cache;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Definition-of-done tests for the In-Memory Cache (LRU / LFU) LLD practice.
 *
 * Remove the {@code @Disabled} annotation only after you have implemented the
 * full solution.  All tests must pass without modifying the test code.
 */
class CacheTest {

    // ------------------------------------------------------------------
    // Test 1: LRU eviction — the least-recently-used entry is evicted
    // ------------------------------------------------------------------

    @Test
    void lruEvictsLeastRecentlyUsedEntry() {
        // Arrange: capacity-2 LRU cache
        Cache<String, Integer> cache = CacheFactory.create(PolicyType.LRU, 2);

        // Act: fill the cache then access "a" so "b" becomes the LRU entry
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a"); // "a" is now most-recently used; "b" is LRU

        // Inserting a third entry must evict "b"
        cache.put("c", 3);

        // Assert
        assertTrue(cache.get("b").isEmpty(),
                "'b' should have been evicted as the LRU entry");
        assertEquals(Optional.of(1), cache.get("a"),
                "'a' should still be present");
        assertEquals(Optional.of(3), cache.get("c"),
                "'c' should be present as the newest entry");

        // Stats: 2 hits (a, c after eviction), 1 miss (b), 1 eviction, size 2
        CacheStats stats = cache.stats();
        assertEquals(1, stats.evictions(), "exactly one eviction should have occurred");
        assertEquals(2, stats.size(), "cache should hold exactly two entries");
    }

    // ------------------------------------------------------------------
    // Test 2: LFU eviction — the least-frequently-used entry is evicted
    // ------------------------------------------------------------------

    @Test
    void lfuEvictsLeastFrequentlyUsedEntry() {
        // Arrange: capacity-3 LFU cache
        Cache<String, String> cache = CacheFactory.create(PolicyType.LFU, 3);

        cache.put("x", "apple");
        cache.put("y", "banana");
        cache.put("z", "cherry");

        // Boost frequencies: x accessed 3 times, y accessed 2 times, z accessed 1 time
        cache.get("x");
        cache.get("x");
        cache.get("x");
        cache.get("y");
        cache.get("y");
        // "z" has frequency 1 (only the initial put) — it is the LFU candidate

        // Act: inserting a fourth entry must evict "z"
        cache.put("w", "date");

        // Assert
        assertTrue(cache.get("z").isEmpty(),
                "'z' should have been evicted as the LFU entry");
        assertEquals(Optional.of("apple"),  cache.get("x"), "'x' must still be cached");
        assertEquals(Optional.of("banana"), cache.get("y"), "'y' must still be cached");
        assertEquals(Optional.of("date"),   cache.get("w"), "'w' must be cached");

        CacheStats stats = cache.stats();
        assertEquals(1, stats.evictions(), "exactly one eviction should have occurred");
        assertEquals(3, stats.size(), "cache should hold exactly three entries");
    }

    // ------------------------------------------------------------------
    // Test 3: stats tracking — hits, misses, clear, and capacity contract
    // ------------------------------------------------------------------

    @Test
    void statsAreTrackedAccuratelyAndClearResetsState() {
        // Arrange
        Cache<Integer, String> cache = CacheFactory.create(PolicyType.LRU, 5);
        assertEquals(5, cache.capacity(), "capacity must equal the value passed to the factory");

        // Act: two puts, one hit, two misses
        cache.put(1, "one");
        cache.put(2, "two");

        cache.get(1);  // hit
        cache.get(3);  // miss
        cache.get(4);  // miss

        CacheStats before = cache.stats();
        assertEquals(1, before.hits(),   "should record 1 hit");
        assertEquals(2, before.misses(), "should record 2 misses");
        assertEquals(0, before.evictions(), "no evictions should have occurred yet");
        assertEquals(2, before.size(),   "two entries in cache");

        // Explicit remove
        cache.remove(2);
        assertEquals(1, cache.stats().size(), "size should decrease after explicit remove");
        assertTrue(cache.get(2).isEmpty(),    "removed key must not be retrievable");

        // Clear resets everything
        cache.clear();
        CacheStats after = cache.stats();
        assertEquals(0, after.hits(),      "hits must be 0 after clear");
        assertEquals(0, after.misses(),    "misses must be 0 after clear");
        assertEquals(0, after.evictions(), "evictions must be 0 after clear");
        assertEquals(0, after.size(),      "size must be 0 after clear");
    }
}
