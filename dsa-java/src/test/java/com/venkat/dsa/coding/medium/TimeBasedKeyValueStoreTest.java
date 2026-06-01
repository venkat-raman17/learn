package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class TimeBasedKeyValueStoreTest {

    @Test
    public void testGetAtExactTimestamp() {
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        store.set("foo", "bar", 1);
        assertEquals("bar", store.get("foo", 1));
    }

    @Test
    public void testGetAtLaterTimestamp() {
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        store.set("foo", "bar", 1);
        assertEquals("bar", store.get("foo", 3));
    }

    @Test
    public void testGetWithMultipleValues() {
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        store.set("foo", "bar", 1);
        store.set("foo", "bar2", 4);
        assertEquals("bar2", store.get("foo", 4));
        assertEquals("bar2", store.get("foo", 5));
        assertEquals("bar", store.get("foo", 3));
        assertEquals("", store.get("foo", 0));
    }
}
