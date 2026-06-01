package com.venkat.dsa.hashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavioural tests for {@link HashMapOpenAddressing}.
 *
 * <p>All inputs are deterministic (no random keys) so tests are repeatable
 * and expected values can be reasoned about without running code.</p>
 */
class HashMapOpenAddressingTest {

    private HashMapOpenAddressing<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new HashMapOpenAddressing<>();
    }

    // -----------------------------------------------------------------------
    // Empty-map invariants
    // -----------------------------------------------------------------------

    @Test
    void newMap_isEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    void getOnEmptyMap_returnsNull() {
        assertNull(map.get("missing"));
    }

    @Test
    void removeOnEmptyMap_returnsNull() {
        assertNull(map.remove("missing"));
    }

    @Test
    void containsKeyOnEmptyMap_returnsFalse() {
        assertFalse(map.containsKey("missing"));
    }

    // -----------------------------------------------------------------------
    // Null-key rejection
    // -----------------------------------------------------------------------

    @Test
    void put_nullKey_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> map.put(null, 1));
    }

    @Test
    void get_nullKey_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> map.get(null));
    }

    @Test
    void remove_nullKey_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> map.remove(null));
    }

    @Test
    void containsKey_nullKey_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> map.containsKey(null));
    }

    // -----------------------------------------------------------------------
    // Invalid constructor argument
    // -----------------------------------------------------------------------

    @Test
    void constructor_nonPositiveCapacity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new HashMapOpenAddressing<>(0));
        assertThrows(IllegalArgumentException.class, () -> new HashMapOpenAddressing<>(-1));
    }

    // -----------------------------------------------------------------------
    // Single entry: put / get / size
    // -----------------------------------------------------------------------

    @Test
    void putAndGet_singleEntry() {
        map.put("alpha", 1);
        assertEquals(1, map.get("alpha"));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    void get_absentKey_returnsNull() {
        map.put("alpha", 1);
        assertNull(map.get("beta"));
    }

    // -----------------------------------------------------------------------
    // Value update (duplicate key)
    // -----------------------------------------------------------------------

    @Test
    void put_duplicateKey_updatesValue() {
        map.put("key", 10);
        map.put("key", 20);
        assertEquals(20, map.get("key"));
        // Size must not grow on an update.
        assertEquals(1, map.size());
    }

    @Test
    void put_duplicateKey_multipleUpdates() {
        for (int i = 0; i < 5; i++) {
            map.put("k", i);
        }
        assertEquals(4, map.get("k"));
        assertEquals(1, map.size());
    }

    // -----------------------------------------------------------------------
    // Remove and tombstone semantics
    // -----------------------------------------------------------------------

    @Test
    void remove_presentKey_returnsOldValue() {
        map.put("x", 42);
        assertEquals(42, map.remove("x"));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    void remove_absentKey_returnsNull() {
        map.put("x", 42);
        assertNull(map.remove("z"));
        assertEquals(1, map.size());
    }

    @Test
    void remove_thenGet_returnsNull() {
        map.put("x", 42);
        map.remove("x");
        assertNull(map.get("x"));
        assertFalse(map.containsKey("x"));
    }

    /**
     * After a tombstone is left by remove, re-inserting the same key must work
     * and the tombstone slot should be reused (size reflects live entries only).
     */
    @Test
    void remove_thenReInsert_viaTomstonePath() {
        map.put("x", 1);
        map.remove("x");           // slot becomes tombstone
        map.put("x", 99);          // must find tombstone slot
        assertEquals(99, map.get("x"));
        assertEquals(1, map.size());
    }

    @Test
    void remove_allEntries_mapBecomesEmpty() {
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.remove("a");
        map.remove("b");
        map.remove("c");
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    // -----------------------------------------------------------------------
    // Multiple entries and containsKey
    // -----------------------------------------------------------------------

    @Test
    void putMultipleEntries_getAll() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
        assertEquals(3, map.size());
    }

    @Test
    void containsKey_presentAndAbsent() {
        map.put("present", 7);
        assertTrue(map.containsKey("present"));
        assertFalse(map.containsKey("absent"));
    }

    // -----------------------------------------------------------------------
    // Collision handling (linear probing)
    //
    // We craft keys that, with the mixing in HashMapOpenAddressing#hash, land
    // in the same initial bucket at capacity=16.  Rather than relying on a
    // specific internal hash, we insert many items and rely on pigeonhole: with
    // 8 entries in a capacity-16 table (load = 0.5) at least some must collide.
    // We verify all keys are retrievable, proving probing is correct.
    // -----------------------------------------------------------------------

    @Test
    void probing_multipleKeysAllRetrievable() {
        // Insert 8 entries - load factor hits exactly 0.5 before the 9th,
        // which triggers resize.  All 8 must be retrievable without corruption.
        for (int i = 0; i < 8; i++) {
            map.put("key" + i, i * 10);
        }
        for (int i = 0; i < 8; i++) {
            assertEquals(i * 10, map.get("key" + i));
        }
        assertEquals(8, map.size());
    }

    /**
     * Probe chain integrity: deleting a middle entry in a collision chain must
     * not break lookup of keys that were inserted after it.
     *
     * We use Integer keys so we can control hashCode precisely.  We wrap them
     * in a custom key class to control hash codes and force collisions.
     */
    @Test
    void probing_tombstoneInMiddleOfChain_doesNotBreakLookup() {
        // Use a small map (capacity=4) to force clustering more easily.
        HashMapOpenAddressing<CollidingKey, String> smallMap =
                new HashMapOpenAddressing<>(4);

        // All three keys hash to 0 mod 4, so they probe slots 0, 1, 2.
        CollidingKey k0 = new CollidingKey(0, "k0");
        CollidingKey k1 = new CollidingKey(0, "k1");
        CollidingKey k2 = new CollidingKey(0, "k2");

        smallMap.put(k0, "v0");
        smallMap.put(k1, "v1");
        // After two inserts: size=2, capacity=4, load=0.5 -> next insert resizes.
        // Resize to 8 before inserting k2.
        smallMap.put(k2, "v2");

        // Remove the middle entry (k1) — leaves a tombstone.
        assertEquals("v1", smallMap.remove(k1));

        // k2 was inserted after k1 (and probed past it), so its lookup must
        // still skip the tombstone and find slot 2.
        assertEquals("v0", smallMap.get(k0));
        assertEquals("v2", smallMap.get(k2));
        assertNull(smallMap.get(k1));
    }

    // -----------------------------------------------------------------------
    // Resize / growth
    // -----------------------------------------------------------------------

    @Test
    void resize_triggeredByLoadFactor_allEntriesSurvive() {
        // Insert 20 entries; multiple resizes must occur (16->32->64).
        for (int i = 0; i < 20; i++) {
            map.put("item" + i, i);
        }
        assertEquals(20, map.size());
        for (int i = 0; i < 20; i++) {
            assertEquals(i, map.get("item" + i));
        }
    }

    @Test
    void resize_tombstonesDiscarded_sizeRemainsSame() {
        // Fill, delete some, fill again past resize boundary.
        for (int i = 0; i < 8; i++) {
            map.put("k" + i, i);
        }
        map.remove("k0");
        map.remove("k1");
        // Insert enough to trigger resize; tombstones should not inflate live count.
        for (int i = 8; i < 20; i++) {
            map.put("k" + i, i);
        }
        // Live entries: keys k2..k19 = 18
        assertEquals(18, map.size());
        for (int i = 2; i < 20; i++) {
            assertEquals(i, map.get("k" + i));
        }
    }

    // -----------------------------------------------------------------------
    // Null value (allowed)
    // -----------------------------------------------------------------------

    @Test
    void nullValue_canBeStoredAndRetrieved() {
        map.put("nullVal", null);
        assertTrue(map.containsKey("nullVal"));
        assertNull(map.get("nullVal"));
        assertEquals(1, map.size());
    }

    // -----------------------------------------------------------------------
    // keys() utility
    // -----------------------------------------------------------------------

    @Test
    void keys_returnsAllLiveKeys() {
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.remove("b");
        List<String> keys = map.keys();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("c"));
        assertFalse(keys.contains("b"));
    }

    @Test
    void keys_emptyMap_returnsEmptyList() {
        assertTrue(map.keys().isEmpty());
    }

    // -----------------------------------------------------------------------
    // Helper: key class with forced hash code for collision testing
    // -----------------------------------------------------------------------

    /** A key whose {@code hashCode()} returns a fixed value, forcing collisions. */
    private static final class CollidingKey {
        private final int fixedHash;
        private final String id;

        CollidingKey(int fixedHash, String id) {
            this.fixedHash = fixedHash;
            this.id = id;
        }

        @Override
        public int hashCode() {
            return fixedHash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CollidingKey)) return false;
            return id.equals(((CollidingKey) o).id);
        }

        @Override
        public String toString() {
            return "CollidingKey{" + id + "}";
        }
    }
}
