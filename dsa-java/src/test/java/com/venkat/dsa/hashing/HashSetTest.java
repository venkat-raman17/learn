package com.venkat.dsa.hashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavioural tests for {@link HashSet}.
 *
 * <p>Coverage: every public operation (add, contains, remove, size, isEmpty),
 * plus edge cases: empty set, single element, duplicates, growth/resize,
 * boundaries (capacity boundaries via custom initial capacity), and
 * error conditions (null argument).</p>
 */
class HashSetTest {

    private HashSet<String> set;

    @BeforeEach
    void setUp() {
        set = new HashSet<>();
    }

    // -------------------------------------------------------------------------
    // isEmpty / size — empty state
    // -------------------------------------------------------------------------

    @Test
    void newSetIsEmpty() {
        assertTrue(set.isEmpty());
    }

    @Test
    void newSetHasSizeZero() {
        assertEquals(0, set.size());
    }

    // -------------------------------------------------------------------------
    // add — basic behaviour
    // -------------------------------------------------------------------------

    @Test
    void addReturnsTrueForNewElement() {
        assertTrue(set.add("alpha"));
    }

    @Test
    void addIncreasesSize() {
        set.add("alpha");
        assertEquals(1, set.size());
    }

    @Test
    void setIsNotEmptyAfterAdd() {
        set.add("alpha");
        assertFalse(set.isEmpty());
    }

    @Test
    void addMultipleDistinctElements() {
        assertTrue(set.add("a"));
        assertTrue(set.add("b"));
        assertTrue(set.add("c"));
        assertEquals(3, set.size());
    }

    // -------------------------------------------------------------------------
    // add — duplicate handling
    // -------------------------------------------------------------------------

    @Test
    void addDuplicateReturnsFalse() {
        set.add("dup");
        assertFalse(set.add("dup"));
    }

    @Test
    void addDuplicateDoesNotIncreaseSize() {
        set.add("dup");
        set.add("dup");
        assertEquals(1, set.size());
    }

    @Test
    void addManyDuplicatesKeepsSizeAtOne() {
        for (int i = 0; i < 50; i++) {
            set.add("same");
        }
        assertEquals(1, set.size());
    }

    // -------------------------------------------------------------------------
    // contains
    // -------------------------------------------------------------------------

    @Test
    void containsReturnsFalseOnEmptySet() {
        assertFalse(set.contains("missing"));
    }

    @Test
    void containsReturnsTrueAfterAdd() {
        set.add("hello");
        assertTrue(set.contains("hello"));
    }

    @Test
    void containsReturnsFalseForAbsentElement() {
        set.add("hello");
        assertFalse(set.contains("world"));
    }

    @Test
    void containsReturnsTrueForAllAddedElements() {
        String[] elements = {"alpha", "beta", "gamma", "delta", "epsilon"};
        for (String e : elements) {
            set.add(e);
        }
        for (String e : elements) {
            assertTrue(set.contains(e), "Expected set to contain: " + e);
        }
    }

    // -------------------------------------------------------------------------
    // remove — basic behaviour
    // -------------------------------------------------------------------------

    @Test
    void removeReturnsFalseOnEmptySet() {
        assertFalse(set.remove("ghost"));
    }

    @Test
    void removeReturnsTrueForPresentElement() {
        set.add("x");
        assertTrue(set.remove("x"));
    }

    @Test
    void removeDecreasesSize() {
        set.add("x");
        set.remove("x");
        assertEquals(0, set.size());
    }

    @Test
    void setIsEmptyAfterRemovingLastElement() {
        set.add("x");
        set.remove("x");
        assertTrue(set.isEmpty());
    }

    @Test
    void removeReturnsFalseForAbsentElement() {
        set.add("present");
        assertFalse(set.remove("absent"));
    }

    @Test
    void containsReturnsFalseAfterRemove() {
        set.add("to-remove");
        set.remove("to-remove");
        assertFalse(set.contains("to-remove"));
    }

    @Test
    void removeLeavesOtherElementsIntact() {
        set.add("keep1");
        set.add("remove-me");
        set.add("keep2");
        set.remove("remove-me");
        assertTrue(set.contains("keep1"));
        assertTrue(set.contains("keep2"));
        assertFalse(set.contains("remove-me"));
        assertEquals(2, set.size());
    }

    @Test
    void removeFromHeadOfChain() {
        // Force two strings into the same bucket by using a small-capacity set.
        // We rely on deterministic Java String hashCode values.
        // "Aa" and "BB" both have hashCode == 2112 in Java.
        HashSet<String> small = new HashSet<>(1); // capacity rounded to 1, so all in bucket 0
        small.add("Aa");
        small.add("BB");
        assertEquals(2, small.size());
        // Remove head
        assertTrue(small.remove("BB")); // most recently prepended = head
        assertFalse(small.contains("BB"));
        assertTrue(small.contains("Aa"));
        assertEquals(1, small.size());
    }

    @Test
    void removeMidAndTailOfChain() {
        HashSet<String> small = new HashSet<>(1);
        small.add("first");
        small.add("second");
        small.add("third");
        // Remove middle (second was prepended after first, third after second → chain: third→second→first)
        assertTrue(small.remove("second"));
        assertFalse(small.contains("second"));
        assertTrue(small.contains("first"));
        assertTrue(small.contains("third"));
        assertEquals(2, small.size());
    }

    // -------------------------------------------------------------------------
    // resize / growth
    // -------------------------------------------------------------------------

    @Test
    void setGrowsCorrectlyBeyondDefaultLoadFactor() {
        // Insert 20 elements — well past the 0.75 * 16 = 12-element threshold
        for (int i = 0; i < 20; i++) {
            set.add("element-" + i);
        }
        assertEquals(20, set.size());
        // All elements still reachable after resize
        for (int i = 0; i < 20; i++) {
            assertTrue(set.contains("element-" + i), "Missing: element-" + i);
        }
    }

    @Test
    void addAndContainAfterMultipleResizes() {
        for (int i = 0; i < 200; i++) {
            set.add("item-" + i);
        }
        assertEquals(200, set.size());
        for (int i = 0; i < 200; i++) {
            assertTrue(set.contains("item-" + i));
        }
    }

    @Test
    void removeAfterResize() {
        for (int i = 0; i < 20; i++) {
            set.add("e-" + i);
        }
        for (int i = 0; i < 20; i++) {
            assertTrue(set.remove("e-" + i), "Could not remove: e-" + i);
        }
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Integer element type (generic)
    // -------------------------------------------------------------------------

    @Test
    void worksWithIntegerType() {
        HashSet<Integer> intSet = new HashSet<>();
        intSet.add(1);
        intSet.add(2);
        intSet.add(3);
        intSet.add(2); // duplicate
        assertEquals(3, intSet.size());
        assertTrue(intSet.contains(1));
        assertTrue(intSet.contains(3));
        assertFalse(intSet.contains(99));
        assertTrue(intSet.remove(2));
        assertFalse(intSet.contains(2));
        assertEquals(2, intSet.size());
    }

    // -------------------------------------------------------------------------
    // Custom initial capacity
    // -------------------------------------------------------------------------

    @Test
    void customInitialCapacityWorks() {
        HashSet<String> customSet = new HashSet<>(4);
        customSet.add("a");
        customSet.add("b");
        customSet.add("c");
        customSet.add("d");
        customSet.add("e"); // triggers resize (5 > 0.75 * 4 = 3)
        assertEquals(5, customSet.size());
        assertTrue(customSet.contains("a"));
        assertTrue(customSet.contains("e"));
    }

    @Test
    void customInitialCapacityOfOneWorks() {
        HashSet<String> tiny = new HashSet<>(1);
        tiny.add("x");
        tiny.add("y");
        assertEquals(2, tiny.size());
        assertTrue(tiny.contains("x"));
        assertTrue(tiny.contains("y"));
    }

    // -------------------------------------------------------------------------
    // Error conditions — null argument
    // -------------------------------------------------------------------------

    @Test
    void addNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> set.add(null));
    }

    @Test
    void containsNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> set.contains(null));
    }

    @Test
    void removeNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> set.remove(null));
    }

    @Test
    void invalidInitialCapacityThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new HashSet<>(0));
        assertThrows(IllegalArgumentException.class, () -> new HashSet<>(-5));
    }

    // -------------------------------------------------------------------------
    // Single-element edge cases
    // -------------------------------------------------------------------------

    @Test
    void singleElementAddContainsRemoveCycle() {
        set.add("solo");
        assertTrue(set.contains("solo"));
        assertTrue(set.remove("solo"));
        assertFalse(set.contains("solo"));
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void reAddAfterRemove() {
        set.add("reborn");
        set.remove("reborn");
        assertTrue(set.add("reborn")); // should succeed again
        assertEquals(1, set.size());
        assertTrue(set.contains("reborn"));
    }
}
