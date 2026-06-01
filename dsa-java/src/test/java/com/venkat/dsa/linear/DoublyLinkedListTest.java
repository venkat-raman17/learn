package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DoublyLinkedList}.
 *
 * <p>Every public operation is exercised, including edge cases:
 * empty list, single element, multi-element growth, duplicate values,
 * and error conditions.</p>
 */
class DoublyLinkedListTest {

    private DoublyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new DoublyLinkedList<>();
    }

    // -------------------------------------------------------------------------
    // isEmpty / size on fresh list
    // -------------------------------------------------------------------------

    @Test
    void newList_isEmptyAndSizeZero() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    // -------------------------------------------------------------------------
    // addFirst
    // -------------------------------------------------------------------------

    @Test
    void addFirst_singleElement_headAndTailAreSame() {
        list.addFirst(10);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(10, list.peekFirst());
        assertEquals(10, list.peekLast());
    }

    @Test
    void addFirst_multipleElements_maintainsOrder() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        // Expected order: 1 -> 2 -> 3
        assertEquals(List.of(1, 2, 3), list.toList());
        assertEquals(3, list.size());
    }

    @Test
    void addFirst_duplicateValues_allRetained() {
        list.addFirst(5);
        list.addFirst(5);
        list.addFirst(5);
        assertEquals(3, list.size());
        assertEquals(List.of(5, 5, 5), list.toList());
    }

    // -------------------------------------------------------------------------
    // addLast
    // -------------------------------------------------------------------------

    @Test
    void addLast_singleElement_headAndTailAreSame() {
        list.addLast(42);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(42, list.peekFirst());
        assertEquals(42, list.peekLast());
    }

    @Test
    void addLast_multipleElements_maintainsOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        // Expected order: 1 -> 2 -> 3
        assertEquals(List.of(1, 2, 3), list.toList());
        assertEquals(3, list.size());
    }

    @Test
    void addLast_duplicateValues_allRetained() {
        list.addLast(7);
        list.addLast(7);
        assertEquals(2, list.size());
        assertEquals(List.of(7, 7), list.toList());
    }

    // -------------------------------------------------------------------------
    // Mixed addFirst / addLast
    // -------------------------------------------------------------------------

    @Test
    void addFirstAndAddLast_mixed_correctOrder() {
        list.addLast(2);
        list.addFirst(1);
        list.addLast(3);
        list.addFirst(0);
        // Expected: 0 -> 1 -> 2 -> 3
        assertEquals(List.of(0, 1, 2, 3), list.toList());
    }

    // -------------------------------------------------------------------------
    // removeFirst
    // -------------------------------------------------------------------------

    @Test
    void removeFirst_singleElement_listBecomesEmpty() {
        list.addLast(99);
        int removed = list.removeFirst();
        assertEquals(99, removed);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void removeFirst_multipleElements_removesFromFront() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(10, list.removeFirst());
        assertEquals(2, list.size());
        assertEquals(List.of(20, 30), list.toList());
    }

    @Test
    void removeFirst_untilEmpty_sizeTrackedCorrectly() {
        list.addLast(1);
        list.addLast(2);
        list.removeFirst();
        list.removeFirst();
        assertTrue(list.isEmpty());
    }

    @Test
    void removeFirst_onEmptyList_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    @Test
    void removeFirst_afterBecomingEmpty_throwsNoSuchElementException() {
        list.addFirst(1);
        list.removeFirst();
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    // -------------------------------------------------------------------------
    // removeLast
    // -------------------------------------------------------------------------

    @Test
    void removeLast_singleElement_listBecomesEmpty() {
        list.addFirst(55);
        int removed = list.removeLast();
        assertEquals(55, removed);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void removeLast_multipleElements_removesFromBack() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(30, list.removeLast());
        assertEquals(2, list.size());
        assertEquals(List.of(10, 20), list.toList());
    }

    @Test
    void removeLast_untilEmpty_sizeTrackedCorrectly() {
        list.addLast(1);
        list.addLast(2);
        list.removeLast();
        list.removeLast();
        assertTrue(list.isEmpty());
    }

    @Test
    void removeLast_onEmptyList_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    @Test
    void removeLast_afterBecomingEmpty_throwsNoSuchElementException() {
        list.addLast(1);
        list.removeLast();
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    // -------------------------------------------------------------------------
    // peekFirst
    // -------------------------------------------------------------------------

    @Test
    void peekFirst_doesNotRemoveElement() {
        list.addLast(7);
        list.addLast(8);
        assertEquals(7, list.peekFirst());
        assertEquals(7, list.peekFirst()); // second call still returns same
        assertEquals(2, list.size());
    }

    @Test
    void peekFirst_onEmptyList_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> list.peekFirst());
    }

    // -------------------------------------------------------------------------
    // peekLast
    // -------------------------------------------------------------------------

    @Test
    void peekLast_doesNotRemoveElement() {
        list.addLast(7);
        list.addLast(8);
        assertEquals(8, list.peekLast());
        assertEquals(8, list.peekLast()); // second call still returns same
        assertEquals(2, list.size());
    }

    @Test
    void peekLast_onEmptyList_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> list.peekLast());
    }

    // -------------------------------------------------------------------------
    // toList
    // -------------------------------------------------------------------------

    @Test
    void toList_emptyList_returnsEmptyList() {
        assertEquals(List.of(), list.toList());
    }

    @Test
    void toList_singleElement_returnsSingletonList() {
        list.addLast(100);
        assertEquals(List.of(100), list.toList());
    }

    @Test
    void toList_multipleElements_returnsCorrectOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(List.of(1, 2, 3), list.toList());
    }

    @Test
    void toList_returnsCopy_mutatingItDoesNotAffectList() {
        list.addLast(1);
        list.addLast(2);
        List<Integer> snapshot = list.toList();
        snapshot.clear();
        // Original list must be unchanged
        assertEquals(2, list.size());
        assertEquals(List.of(1, 2), list.toList());
    }

    // -------------------------------------------------------------------------
    // Boundary / growth stress
    // -------------------------------------------------------------------------

    @Test
    void growthFromBothEnds_largeInput_sizeAndOrderCorrect() {
        // Add 0..9 alternating front and back:
        // addFirst(9), addFirst(8), ..., addFirst(5) -> front: 5,6,7,8,9
        // addLast(0), addLast(1), ..., addLast(4)   -> back:  0,1,2,3,4
        // Combined: 5 6 7 8 9 0 1 2 3 4
        for (int i = 9; i >= 5; i--) {
            list.addFirst(i);
        }
        for (int i = 0; i <= 4; i++) {
            list.addLast(i);
        }
        assertEquals(10, list.size());
        assertEquals(5, list.peekFirst());
        assertEquals(4, list.peekLast());
        assertEquals(List.of(5, 6, 7, 8, 9, 0, 1, 2, 3, 4), list.toList());
    }

    @Test
    void interleavedAddAndRemove_maintainsConsistency() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(1, list.removeFirst());
        list.addFirst(0);
        assertEquals(3, list.removeLast());
        list.addLast(4);
        // State: 0 -> 2 -> 4
        assertEquals(List.of(0, 2, 4), list.toList());
        assertEquals(3, list.size());
    }

    // -------------------------------------------------------------------------
    // Null element support (nulls are valid values)
    // -------------------------------------------------------------------------

    @Test
    void addFirst_nullValue_storedAndRetrievedCorrectly() {
        DoublyLinkedList<String> strList = new DoublyLinkedList<>();
        strList.addFirst(null);
        assertNull(strList.peekFirst());
        assertNull(strList.peekLast());
        assertEquals(1, strList.size());
        assertNull(strList.removeFirst());
        assertTrue(strList.isEmpty());
    }

    @Test
    void addLast_nullValue_storedAndRetrievedCorrectly() {
        DoublyLinkedList<String> strList = new DoublyLinkedList<>();
        strList.addLast(null);
        assertNull(strList.peekLast());
        assertEquals(1, strList.size());
        assertNull(strList.removeLast());
        assertTrue(strList.isEmpty());
    }
}
