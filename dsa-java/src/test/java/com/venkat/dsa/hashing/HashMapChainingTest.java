package com.venkat.dsa.hashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HashMapChaining}.
 * Every public operation and relevant edge case is covered.
 */
class HashMapChainingTest {

    private HashMapChaining<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new HashMapChaining<>();
    }

    // ---------------------------------------------------------------- isEmpty / size on new map --

    @Test
    void newMap_isEmptyAndSizeZero() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    // ---------------------------------------------------------------- put and get basics --

    @Test
    void put_singleEntry_getReturnsValue() {
        map.put("alpha", 1);
        assertEquals(1, map.get("alpha"));
    }

    @Test
    void put_multipleDistinctKeys_eachReturnsCorrectValue() {
        map.put("a", 10);
        map.put("b", 20);
        map.put("c", 30);
        assertEquals(10, map.get("a"));
        assertEquals(20, map.get("b"));
        assertEquals(30, map.get("c"));
        assertEquals(3, map.size());
    }

    @Test
    void put_newKey_returnsNull() {
        assertNull(map.put("x", 99));
    }

    @Test
    void put_returnsNullForFirstInsertion_andNonNullOnUpdate() {
        assertNull(map.put("k", 1));
        Integer previous = map.put("k", 2);
        assertEquals(1, previous);
    }

    // ---------------------------------------------------------------- update existing key --

    @Test
    void put_existingKey_updatesValue() {
        map.put("dup", 7);
        map.put("dup", 42);
        assertEquals(42, map.get("dup"));
    }

    @Test
    void put_existingKey_doesNotIncreaseSize() {
        map.put("dup", 7);
        map.put("dup", 42);
        assertEquals(1, map.size());
    }

    // ---------------------------------------------------------------- get absent key --

    @Test
    void get_absentKey_returnsNull() {
        assertNull(map.get("missing"));
    }

    @Test
    void get_onEmptyMap_returnsNull() {
        assertNull(map.get("anything"));
    }

    // ---------------------------------------------------------------- containsKey --

    @Test
    void containsKey_presentKey_returnsTrue() {
        map.put("present", 1);
        assertTrue(map.containsKey("present"));
    }

    @Test
    void containsKey_absentKey_returnsFalse() {
        assertFalse(map.containsKey("absent"));
    }

    @Test
    void containsKey_afterRemoval_returnsFalse() {
        map.put("gone", 5);
        map.remove("gone");
        assertFalse(map.containsKey("gone"));
    }

    // ---------------------------------------------------------------- remove --

    @Test
    void remove_existingKey_returnsOldValue() {
        map.put("r", 55);
        assertEquals(55, map.remove("r"));
    }

    @Test
    void remove_existingKey_decreasesSize() {
        map.put("r", 55);
        map.remove("r");
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    void remove_existingKey_subsequentGetReturnsNull() {
        map.put("r", 55);
        map.remove("r");
        assertNull(map.get("r"));
    }

    @Test
    void remove_absentKey_returnsNull() {
        assertNull(map.remove("nope"));
    }

    @Test
    void remove_absentKey_sizeUnchanged() {
        map.put("a", 1);
        map.remove("missing");
        assertEquals(1, map.size());
    }

    @Test
    void remove_onEmptyMap_returnsNull() {
        assertNull(map.remove("anything"));
    }

    // ---------------------------------------------------------------- forced collisions --

    /**
     * CollidingKey objects whose hashCode() always returns the same value, forcing
     * every entry into the same bucket chain.
     */
    static final class CollidingKey {
        private final int id;

        CollidingKey(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return 42; // constant — all keys land in the same bucket
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CollidingKey)) return false;
            return this.id == ((CollidingKey) o).id;
        }
    }

    @Test
    void put_forcedCollisions_allEntriesStored() {
        HashMapChaining<CollidingKey, Integer> colMap = new HashMapChaining<>();
        for (int i = 0; i < 10; i++) {
            colMap.put(new CollidingKey(i), i * 100);
        }
        assertEquals(10, colMap.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i * 100, colMap.get(new CollidingKey(i)));
        }
    }

    @Test
    void remove_forcedCollisions_correctEntryRemoved() {
        HashMapChaining<CollidingKey, Integer> colMap = new HashMapChaining<>();
        CollidingKey k0 = new CollidingKey(0);
        CollidingKey k1 = new CollidingKey(1);
        CollidingKey k2 = new CollidingKey(2);
        colMap.put(k0, 0);
        colMap.put(k1, 1);
        colMap.put(k2, 2);

        assertEquals(1, colMap.remove(k1));
        assertEquals(2, colMap.size());
        assertNull(colMap.get(k1));
        assertEquals(0, colMap.get(k0));
        assertEquals(2, colMap.get(k2));
    }

    @Test
    void update_forcedCollisions_onlyTargetEntryChanged() {
        HashMapChaining<CollidingKey, Integer> colMap = new HashMapChaining<>();
        CollidingKey k0 = new CollidingKey(0);
        CollidingKey k1 = new CollidingKey(1);
        colMap.put(k0, 0);
        colMap.put(k1, 1);

        colMap.put(k0, 999);
        assertEquals(999, colMap.get(k0));
        assertEquals(1, colMap.get(k1));
        assertEquals(2, colMap.size());
    }

    // ---------------------------------------------------------------- growth / rehash --

    @Test
    void put_manyEntries_capacityDoublesAndEntriesPreserved() {
        // Insert 13 entries into a default-capacity-16 map:
        // load factor threshold = 0.75 * 16 = 12 entries triggers rehash on 13th.
        int count = 13;
        for (int i = 0; i < count; i++) {
            map.put("key" + i, i);
        }

        // After rehash capacity should have grown to 32
        assertEquals(32, map.capacity());
        assertEquals(count, map.size());

        // All entries must still be retrievable
        for (int i = 0; i < count; i++) {
            assertEquals(i, map.get("key" + i));
        }
    }

    @Test
    void put_triggerMultipleRehashes_allEntriesPreserved() {
        // Push past two rehash thresholds:
        // cap 16 -> rehash at 13th entry (> 0.75*16=12)
        // cap 32 -> rehash at 25th entry (> 0.75*32=24)
        int count = 30;
        for (int i = 0; i < count; i++) {
            map.put("entry" + i, i * 2);
        }
        assertEquals(count, map.size());
        for (int i = 0; i < count; i++) {
            assertEquals(i * 2, map.get("entry" + i));
        }
    }

    // ---------------------------------------------------------------- keySet --

    @Test
    void keySet_emptyMap_returnsEmptySet() {
        assertTrue(map.keySet().isEmpty());
    }

    @Test
    void keySet_containsAllInsertedKeys() {
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        Set<String> keys = map.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
        assertTrue(keys.contains("c"));
    }

    @Test
    void keySet_afterRemoval_doesNotContainRemovedKey() {
        map.put("x", 1);
        map.put("y", 2);
        map.remove("x");
        Set<String> keys = map.keySet();
        assertFalse(keys.contains("x"));
        assertTrue(keys.contains("y"));
    }

    // ---------------------------------------------------------------- null key --

    @Test
    void put_nullKey_storesAndRetrievesValue() {
        map.put(null, 77);
        assertEquals(77, map.get(null));
        assertTrue(map.containsKey(null));
        assertEquals(1, map.size());
    }

    @Test
    void put_nullKey_update_returnsOldValue() {
        map.put(null, 1);
        assertEquals(1, map.put(null, 2).intValue());
        assertEquals(2, map.get(null));
        assertEquals(1, map.size());
    }

    @Test
    void remove_nullKey_returnsOldValueAndDecrementsSize() {
        map.put(null, 5);
        assertEquals(5, map.remove(null));
        assertNull(map.get(null));
        assertEquals(0, map.size());
    }

    // ---------------------------------------------------------------- isEmpty transitions --

    @Test
    void isEmpty_trueAfterAllEntriesRemoved() {
        map.put("only", 1);
        map.remove("only");
        assertTrue(map.isEmpty());
    }

    @Test
    void isEmpty_falseAfterFirstPut() {
        map.put("k", 0);
        assertFalse(map.isEmpty());
    }
}
