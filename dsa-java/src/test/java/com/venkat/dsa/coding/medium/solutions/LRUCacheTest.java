package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void example1_officialExample() {
        // LRUCache(2): put(1,1), put(2,2), get(1)->1, put(3,3) evicts 2,
        // get(2)->-1, put(4,4) evicts 1, get(1)->-1, get(3)->3, get(4)->4
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1,  cache.get(1));
        cache.put(3, 3);   // evicts key 2
        assertEquals(-1, cache.get(2));
        cache.put(4, 4);   // evicts key 1
        assertEquals(-1, cache.get(1));
        assertEquals(3,  cache.get(3));
        assertEquals(4,  cache.get(4));
    }

    @Test
    void updateExistingKey() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 10);
        cache.put(1, 20); // update
        assertEquals(20, cache.get(1));
    }

    @Test
    void evictionOrderLRU() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);      // key 1 is now MRU; key 2 is LRU
        cache.put(3, 3);   // evicts key 2
        assertEquals(-1, cache.get(2));
        assertEquals(1,  cache.get(1));
        assertEquals(3,  cache.get(3));
    }

    @Test
    void capacityOne() {
        LRUCache cache = new LRUCache(1);
        cache.put(1, 1);
        assertEquals(1, cache.get(1));
        cache.put(2, 2); // evicts key 1
        assertEquals(-1, cache.get(1));
        assertEquals(2,  cache.get(2));
    }

    @Test
    void getMissingKey() {
        LRUCache cache = new LRUCache(3);
        assertEquals(-1, cache.get(99));
    }

    @Test
    void putPromotesExistingToMRU() {
        // Put the same key twice; it should survive the subsequent eviction
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(1, 100); // re-insert key 1, making it MRU; key 2 is now LRU
        cache.put(3, 3);   // evicts key 2
        assertEquals(-1,  cache.get(2));
        assertEquals(100, cache.get(1));
        assertEquals(3,   cache.get(3));
    }
}
