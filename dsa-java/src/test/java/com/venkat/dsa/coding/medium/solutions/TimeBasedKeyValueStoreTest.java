package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeBasedKeyValueStoreTest {

    // --- Official LeetCode examples ---

    @Test
    void example1() {
        TimeBasedKeyValueStore kv = new TimeBasedKeyValueStore();
        kv.set("foo", "bar", 1);
        assertEquals("bar", kv.get("foo", 1));  // exact match
        assertEquals("bar", kv.get("foo", 3));  // latest before 3 is timestamp=1 => "bar"
        kv.set("foo", "bar2", 4);
        assertEquals("bar2", kv.get("foo", 4)); // exact match at 4
        assertEquals("bar2", kv.get("foo", 5)); // latest before 5 is timestamp=4 => "bar2"
    }

    // --- Edge cases ---

    @Test
    void getBeforeAnySet_returnsEmpty() {
        TimeBasedKeyValueStore kv = new TimeBasedKeyValueStore();
        kv.set("foo", "bar", 5);
        assertEquals("", kv.get("foo", 1)); // timestamp 1 < earliest set (5)
    }

    @Test
    void unknownKey_returnsEmpty() {
        TimeBasedKeyValueStore kv = new TimeBasedKeyValueStore();
        assertEquals("", kv.get("missing", 10));
    }

    @Test
    void multipleKeys_independent() {
        TimeBasedKeyValueStore kv = new TimeBasedKeyValueStore();
        kv.set("a", "alpha", 1);
        kv.set("b", "beta", 2);
        assertEquals("alpha", kv.get("a", 10));
        assertEquals("beta", kv.get("b", 10));
        assertEquals("", kv.get("a", 0));
    }

    @Test
    void manyTimestamps_binarySearchReturnsCorrect() {
        TimeBasedKeyValueStore kv = new TimeBasedKeyValueStore();
        kv.set("k", "v1", 10);
        kv.set("k", "v2", 20);
        kv.set("k", "v3", 30);
        kv.set("k", "v4", 40);

        assertEquals("", kv.get("k", 5));    // before all
        assertEquals("v1", kv.get("k", 10)); // exact
        assertEquals("v1", kv.get("k", 15)); // between 10 and 20
        assertEquals("v2", kv.get("k", 20)); // exact
        assertEquals("v2", kv.get("k", 29)); // between 20 and 30, largest ts<=29 is 20 => "v2"
        assertEquals("v4", kv.get("k", 100)); // after all
    }

    @Test
    void singleEntry_exactAndBeyond() {
        TimeBasedKeyValueStore kv = new TimeBasedKeyValueStore();
        kv.set("x", "only", 7);
        assertEquals("only", kv.get("x", 7));
        assertEquals("only", kv.get("x", 100));
        assertEquals("", kv.get("x", 6));
    }
}
