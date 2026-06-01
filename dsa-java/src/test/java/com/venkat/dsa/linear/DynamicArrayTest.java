package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavioral tests for {@link DynamicArray}.
 * Every public operation is covered including edge cases:
 * empty, single-element, growth/resize, duplicates, boundary indices,
 * null values, and error conditions.
 */
class DynamicArrayTest {

    private DynamicArray<Integer> arr;

    @BeforeEach
    void setUp() {
        arr = new DynamicArray<>();
    }

    // -----------------------------------------------------------------------
    // size() and isEmpty()
    // -----------------------------------------------------------------------

    @Test
    void newArray_isEmpty() {
        assertTrue(arr.isEmpty());
        assertEquals(0, arr.size());
    }

    @Test
    void afterAddOne_notEmpty_sizeIsOne() {
        arr.add(42);
        assertFalse(arr.isEmpty());
        assertEquals(1, arr.size());
    }

    // -----------------------------------------------------------------------
    // add(v) — append
    // -----------------------------------------------------------------------

    @Test
    void appendBeyondDefaultCapacity_grows() {
        // Default capacity is 8; adding 16 elements forces at least one resize
        for (int i = 0; i < 16; i++) {
            arr.add(i);
        }
        assertEquals(16, arr.size());
        for (int i = 0; i < 16; i++) {
            assertEquals(i, arr.get(i));
        }
    }

    @Test
    void append_null_isAllowed() {
        arr.add(null);
        assertEquals(1, arr.size());
        assertNull(arr.get(0));
    }

    @Test
    void append_duplicates_allStored() {
        arr.add(7);
        arr.add(7);
        arr.add(7);
        assertEquals(3, arr.size());
        assertEquals(7, arr.get(0));
        assertEquals(7, arr.get(1));
        assertEquals(7, arr.get(2));
    }

    // -----------------------------------------------------------------------
    // add(i, v) — insert at index
    // -----------------------------------------------------------------------

    @Test
    void insertAtBeginning_shiftsRight() {
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.add(0, 0);          // [0, 1, 2, 3]
        assertEquals(4, arr.size());
        assertEquals(0, arr.get(0));
        assertEquals(1, arr.get(1));
        assertEquals(2, arr.get(2));
        assertEquals(3, arr.get(3));
    }

    @Test
    void insertAtMiddle_shiftsRight() {
        arr.add(10);
        arr.add(30);
        arr.add(1, 20);         // [10, 20, 30]
        assertEquals(3, arr.size());
        assertEquals(10, arr.get(0));
        assertEquals(20, arr.get(1));
        assertEquals(30, arr.get(2));
    }

    @Test
    void insertAtEnd_equivalentToAppend() {
        arr.add(5);
        arr.add(1, 6);          // [5, 6]
        assertEquals(2, arr.size());
        assertEquals(5, arr.get(0));
        assertEquals(6, arr.get(1));
    }

    @Test
    void insertAtEnd_onEmpty_size0() {
        // add(0, v) on empty list — index == size == 0, which is valid
        arr.add(0, 99);
        assertEquals(1, arr.size());
        assertEquals(99, arr.get(0));
    }

    @Test
    void insertOutOfBounds_throwsIndexOutOfBoundsException() {
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.add(2, 99));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.add(-1, 99));
    }

    // -----------------------------------------------------------------------
    // get(i)
    // -----------------------------------------------------------------------

    @Test
    void get_firstAndLastElements() {
        arr.add(100);
        arr.add(200);
        arr.add(300);
        assertEquals(100, arr.get(0));
        assertEquals(300, arr.get(2));
    }

    @Test
    void get_outOfBounds_throwsIndexOutOfBoundsException() {
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
    }

    @Test
    void get_onEmptyArray_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0));
    }

    // -----------------------------------------------------------------------
    // set(i, v)
    // -----------------------------------------------------------------------

    @Test
    void set_replacesValueAndReturnsOld() {
        arr.add(10);
        arr.add(20);
        Integer old = arr.set(0, 99);
        assertEquals(10, old);
        assertEquals(99, arr.get(0));
        assertEquals(20, arr.get(1));
        assertEquals(2, arr.size());   // size unchanged
    }

    @Test
    void set_outOfBounds_throwsIndexOutOfBoundsException() {
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.set(1, 99));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.set(-1, 99));
    }

    @Test
    void set_onEmptyArray_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> arr.set(0, 1));
    }

    // -----------------------------------------------------------------------
    // remove(i)
    // -----------------------------------------------------------------------

    @Test
    void removeFromMiddle_shiftsLeft_returnsRemovedValue() {
        arr.add(1);
        arr.add(2);
        arr.add(3);
        Integer removed = arr.remove(1);   // remove 2 → [1, 3]
        assertEquals(2, removed);
        assertEquals(2, arr.size());
        assertEquals(1, arr.get(0));
        assertEquals(3, arr.get(1));
    }

    @Test
    void removeFromBeginning_shiftsLeft() {
        arr.add(10);
        arr.add(20);
        arr.add(30);
        Integer removed = arr.remove(0);   // [20, 30]
        assertEquals(10, removed);
        assertEquals(2, arr.size());
        assertEquals(20, arr.get(0));
        assertEquals(30, arr.get(1));
    }

    @Test
    void removeFromEnd_doesNotShift() {
        arr.add(5);
        arr.add(6);
        Integer removed = arr.remove(1);   // [5]
        assertEquals(6, removed);
        assertEquals(1, arr.size());
        assertEquals(5, arr.get(0));
    }

    @Test
    void removeSingleElement_leavesEmptyArray() {
        arr.add(42);
        Integer removed = arr.remove(0);
        assertEquals(42, removed);
        assertEquals(0, arr.size());
        assertTrue(arr.isEmpty());
    }

    @Test
    void remove_outOfBounds_throwsIndexOutOfBoundsException() {
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(-1));
    }

    @Test
    void remove_onEmptyArray_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(0));
    }

    // -----------------------------------------------------------------------
    // indexOf(v) and contains(v)
    // -----------------------------------------------------------------------

    @Test
    void indexOf_existingElement_returnsFirstIndex() {
        arr.add(10);
        arr.add(20);
        arr.add(10);   // duplicate
        assertEquals(0, arr.indexOf(10));
    }

    @Test
    void indexOf_missingElement_returnsNegativeOne() {
        arr.add(1);
        assertEquals(-1, arr.indexOf(99));
    }

    @Test
    void indexOf_null_found() {
        arr.add(1);
        arr.add(null);
        arr.add(2);
        assertEquals(1, arr.indexOf(null));
    }

    @Test
    void contains_existingElement_returnsTrue() {
        arr.add(55);
        assertTrue(arr.contains(55));
    }

    @Test
    void contains_missingElement_returnsFalse() {
        arr.add(55);
        assertFalse(arr.contains(99));
    }

    @Test
    void contains_onEmptyArray_returnsFalse() {
        assertFalse(arr.contains(1));
    }

    // -----------------------------------------------------------------------
    // clear()
    // -----------------------------------------------------------------------

    @Test
    void clear_setsIsEmptyAndSizeToZero() {
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.clear();
        assertTrue(arr.isEmpty());
        assertEquals(0, arr.size());
    }

    @Test
    void clear_thenAdd_works() {
        arr.add(1);
        arr.clear();
        arr.add(42);
        assertEquals(1, arr.size());
        assertEquals(42, arr.get(0));
    }

    // -----------------------------------------------------------------------
    // Growth / resize stress
    // -----------------------------------------------------------------------

    @Test
    void addManyAndRemoveAll_sizeRemainsConsistent() {
        for (int i = 0; i < 100; i++) {
            arr.add(i);
        }
        assertEquals(100, arr.size());
        for (int i = 99; i >= 0; i--) {
            assertEquals(i, arr.remove(arr.size() - 1));
        }
        assertTrue(arr.isEmpty());
    }

    @Test
    void addBeyondCapacityThenGetAll_valuesPreserved() {
        // Capacity 1 forces many doublings
        DynamicArray<String> tiny = new DynamicArray<>(1);
        String[] values = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (String v : values) {
            tiny.add(v);
        }
        assertEquals(values.length, tiny.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], tiny.get(i));
        }
    }

    // -----------------------------------------------------------------------
    // Custom initial capacity
    // -----------------------------------------------------------------------

    @Test
    void illegalInitialCapacity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DynamicArray<>(0));
        assertThrows(IllegalArgumentException.class, () -> new DynamicArray<>(-5));
    }

    // -----------------------------------------------------------------------
    // toString
    // -----------------------------------------------------------------------

    @Test
    void toString_emptyArray_returnsEmptyBrackets() {
        assertEquals("[]", arr.toString());
    }

    @Test
    void toString_multipleElements_returnsCommaSeparated() {
        arr.add(1);
        arr.add(2);
        arr.add(3);
        assertEquals("[1, 2, 3]", arr.toString());
    }
}
