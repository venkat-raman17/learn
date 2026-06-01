package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LRUCacheTest {

    @Test
    void testBasicEviction() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));   // returns 1
        cache.put(3, 3);                 // evicts key 2
        assertEquals(-1, cache.get(2)); // key 2 evicted
        cache.put(4, 4);                 // evicts key 1
        assertEquals(-1, cache.get(1)); // key 1 evicted
        assertEquals(3, cache.get(3));  // returns 3
        assertEquals(4, cache.get(4));  // returns 4
    }

    @Test
    void testGetMissingKey() {
        LRUCache cache = new LRUCache(1);
        assertEquals(-1, cache.get(42));
    }

    @Test
    void testUpdateExistingKey() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 10);
        cache.put(1, 20); // update
        assertEquals(20, cache.get(1));
    }
}
