package com.venkat.dsa.hashing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link LRUCache}.
 *
 * <p>Covers: basic get/put, get returns null for absent keys, capacity metadata,
 * eviction order, get refreshes recency, update existing key, single-element cache,
 * boundary/edge cases, and illegal-argument error conditions.</p>
 */
class LRUCacheTest {

    // -------------------------------------------------------------------------
    // Constructor / capacity validation
    // -------------------------------------------------------------------------

    @Test
    void constructorWithZeroCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache<>(0));
    }

    @Test
    void constructorWithNegativeCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache<>(-5));
    }

    @Test
    void constructorWithValidCapacitySucceeds() {
        LRUCache<Integer, String> cache = new LRUCache<>(5);
        assertEquals(5, cache.capacity());
        assertEquals(0, cache.size());
    }

    // -------------------------------------------------------------------------
    // size() and capacity()
    // -------------------------------------------------------------------------

    @Test
    void sizeIsZeroOnEmptyCache() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        assertEquals(0, cache.size());
    }

    @Test
    void sizeGrowsWithInserts() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(5);
        cache.put(1, 10);
        assertEquals(1, cache.size());
        cache.put(2, 20);
        assertEquals(2, cache.size());
    }

    @Test
    void sizeNeverExceedsCapacity() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4); // triggers eviction
        assertEquals(3, cache.size());
    }

    // -------------------------------------------------------------------------
    // get() — absent keys
    // -------------------------------------------------------------------------

    @Test
    void getOnEmptyCacheReturnsNull() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        assertNull(cache.get("missing"));
    }

    @Test
    void getForNonExistentKeyReturnsNull() {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "one");
        assertNull(cache.get(99));
    }

    // -------------------------------------------------------------------------
    // get() / put() — basic round trip
    // -------------------------------------------------------------------------

    @Test
    void putAndGetSingleEntry() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 42);
        assertEquals(42, cache.get("a"));
    }

    @Test
    void putNullKeyThrows() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, 1));
    }

    @Test
    void putMultipleEntriesAndRetrieveThem() {
        LRUCache<Integer, String> cache = new LRUCache<>(5);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        assertEquals("one",   cache.get(1));
        assertEquals("two",   cache.get(2));
        assertEquals("three", cache.get(3));
    }

    // -------------------------------------------------------------------------
    // Update existing key
    // -------------------------------------------------------------------------

    @Test
    void putExistingKeyUpdatesValue() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("x", 1);
        cache.put("x", 99);
        assertEquals(99, cache.get("x"));
    }

    @Test
    void updateExistingKeyDoesNotIncreaseSize() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("x", 1);
        cache.put("x", 2);
        assertEquals(1, cache.size());
    }

    // -------------------------------------------------------------------------
    // Eviction order
    // -------------------------------------------------------------------------

    @Test
    void lruEntryIsEvictedWhenCapacityExceeded() {
        // Insert 1, 2, 3 into capacity-3 cache, then insert 4.
        // LRU at that point is 1 (never accessed after insert), so 1 is evicted.
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4); // 1 should be evicted
        assertNull(cache.get(1));
        assertEquals(2, cache.get(2));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
    }

    @Test
    void evictionRemovesOldestInsertWhenNoAccess() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("a", "A");
        cache.put("b", "B");
        cache.put("c", "C"); // "a" should be evicted
        assertNull(cache.get("a"));
        assertEquals("B", cache.get("b"));
        assertEquals("C", cache.get("c"));
    }

    @Test
    void multipleEvictionsInOrder() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3); // evicts 1
        cache.put(4, 4); // evicts 2
        assertNull(cache.get(1));
        assertNull(cache.get(2));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
    }

    // -------------------------------------------------------------------------
    // get() refreshes recency
    // -------------------------------------------------------------------------

    @Test
    void getRefreshesRecencyPreventingEviction() {
        // capacity=2: insert 1, insert 2, access 1 (so 2 becomes LRU), insert 3 → 2 evicted.
        LRUCache<Integer, Integer> cache = new LRUCache<>(2);
        cache.put(1, 100);
        cache.put(2, 200);
        cache.get(1);      // 1 is now MRU; 2 is LRU
        cache.put(3, 300); // 2 should be evicted
        assertNull(cache.get(2));
        assertEquals(100, cache.get(1));
        assertEquals(300, cache.get(3));
    }

    @Test
    void getRefreshesRecencyChain() {
        // capacity=3: insert 1,2,3; access 1 (1 becomes MRU), then access 2 (2 becomes MRU).
        // LRU is now 3. Insert 4 → 3 evicted.
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.get(1); // order: 3,2,1 → after get(1): 1 is MRU, LRU=3 → actually order head→tail: 1,3,2
        // Let's reason carefully:
        // After put(1): list = [1]
        // After put(2): list = [2,1]  (2 at head=MRU)
        // After put(3): list = [3,2,1] (3 at head=MRU, 1 at tail=LRU)
        // After get(1): moves 1 to head → list = [1,3,2] (2 is LRU)
        // After get(2): moves 2 to head → list = [2,1,3] (3 is LRU)
        cache.get(2);
        cache.put(4, 4); // 3 should be evicted (LRU)
        assertNull(cache.get(3));
        assertEquals(1, cache.get(1));
        assertEquals(2, cache.get(2));
        assertEquals(4, cache.get(4));
    }

    // -------------------------------------------------------------------------
    // put() refreshes recency (update existing)
    // -------------------------------------------------------------------------

    @Test
    void putExistingKeyRefreshesRecency() {
        // capacity=2: insert 1, insert 2, update 1 (1 becomes MRU), insert 3 → 2 evicted.
        LRUCache<Integer, Integer> cache = new LRUCache<>(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(1, 11); // update 1; now 2 is LRU
        cache.put(3, 30); // 2 should be evicted
        assertNull(cache.get(2));
        assertEquals(11, cache.get(1));
        assertEquals(30, cache.get(3));
    }

    // -------------------------------------------------------------------------
    // Single-element cache
    // -------------------------------------------------------------------------

    @Test
    void singleElementCacheEvictsSelfOnNewInsert() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(1);
        cache.put(1, 10);
        assertEquals(10, cache.get(1));
        cache.put(2, 20); // 1 is evicted
        assertNull(cache.get(1));
        assertEquals(20, cache.get(2));
        assertEquals(1, cache.size());
    }

    @Test
    void singleElementCacheUpdateDoesNotEvict() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(1);
        cache.put(1, 10);
        cache.put(1, 99); // update same key
        assertEquals(99, cache.get(1));
        assertEquals(1, cache.size());
    }

    // -------------------------------------------------------------------------
    // Capacity exactly respected
    // -------------------------------------------------------------------------

    @Test
    void fillToCapacityAndVerifyAllPresent() {
        int cap = 4;
        LRUCache<Integer, Integer> cache = new LRUCache<>(cap);
        for (int i = 0; i < cap; i++) {
            cache.put(i, i * 10);
        }
        assertEquals(cap, cache.size());
        for (int i = 0; i < cap; i++) {
            assertEquals(i * 10, cache.get(i));
        }
    }

    @Test
    void insertBeyondCapacityKeepsSizeAtCapacity() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        for (int i = 0; i < 10; i++) {
            cache.put(i, i);
        }
        assertEquals(3, cache.size());
        // The last 3 inserted (7, 8, 9) should still be present
        // (none were re-accessed, so eviction is strictly in insertion order)
        assertEquals(7, cache.get(7));
        assertEquals(8, cache.get(8));
        assertEquals(9, cache.get(9));
        // Earlier keys should be gone
        assertNull(cache.get(0));
        assertNull(cache.get(6));
    }

    // -------------------------------------------------------------------------
    // Null value is allowed (key is not null)
    // -------------------------------------------------------------------------

    @Test
    void putAndGetNullValue() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("key", null);
        // get("key") should return null — but the key IS present.
        // We can verify presence by checking size vs. absent key distinction.
        assertNull(cache.get("key")); // value is null
        assertEquals(1, cache.size()); // but key exists
        assertNull(cache.get("other")); // also null — absent key
    }
}
