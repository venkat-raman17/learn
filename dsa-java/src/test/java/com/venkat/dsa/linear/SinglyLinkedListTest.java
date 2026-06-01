package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SinglyLinkedList}.
 *
 * <p>Covers every public operation and the key edge cases:
 * empty list, single element, multi-element sequences, duplicates,
 * boundary indices, and expected exception types.
 */
class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new SinglyLinkedList<>();
    }

    // -------------------------------------------------------------------------
    // isEmpty / size on a fresh list
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
    void addFirst_singleElement_becomesHeadAndTail() {
        list.addFirst(10);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(List.of(10), list.toList());
    }

    @Test
    void addFirst_multipleElements_orderIsReversedInsertion() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        // inserted 3 first, then 2 (prepended), then 1 (prepended) -> [1, 2, 3]
        assertEquals(List.of(1, 2, 3), list.toList());
        assertEquals(3, list.size());
    }

    // -------------------------------------------------------------------------
    // addLast
    // -------------------------------------------------------------------------

    @Test
    void addLast_singleElement_becomesHeadAndTail() {
        list.addLast(42);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(List.of(42), list.toList());
    }

    @Test
    void addLast_multipleElements_orderMatchesInsertion() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(List.of(1, 2, 3), list.toList());
        assertEquals(3, list.size());
    }

    // -------------------------------------------------------------------------
    // addFirst + addLast interleaved
    // -------------------------------------------------------------------------

    @Test
    void addFirstAndAddLast_interleaved_correctOrder() {
        list.addLast(2);   // [2]
        list.addFirst(1);  // [1, 2]
        list.addLast(3);   // [1, 2, 3]
        list.addFirst(0);  // [0, 1, 2, 3]
        assertEquals(List.of(0, 1, 2, 3), list.toList());
        assertEquals(4, list.size());
    }

    // -------------------------------------------------------------------------
    // removeFirst
    // -------------------------------------------------------------------------

    @Test
    void removeFirst_emptyList_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    @Test
    void removeFirst_singleElement_returnsValueAndListBecomesEmpty() {
        list.addLast(99);
        int removed = list.removeFirst();
        assertEquals(99, removed);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void removeFirst_multipleElements_removesHeadAndDecreasesSize() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(10, list.removeFirst());
        assertEquals(List.of(20, 30), list.toList());
        assertEquals(2, list.size());
    }

    @Test
    void removeFirst_untilEmpty_allElementsRemovedInOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(1, list.removeFirst());
        assertEquals(2, list.removeFirst());
        assertEquals(3, list.removeFirst());
        assertTrue(list.isEmpty());
    }

    @Test
    void removeFirst_afterBecomingEmpty_throwsNoSuchElementException() {
        list.addLast(5);
        list.removeFirst();
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    // -------------------------------------------------------------------------
    // get(index)
    // -------------------------------------------------------------------------

    @Test
    void get_validIndices_returnsCorrectElements() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void get_negativeIndex_throwsIndexOutOfBoundsException() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void get_indexEqualToSize_throwsIndexOutOfBoundsException() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void get_emptyList_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    // -------------------------------------------------------------------------
    // indexOf
    // -------------------------------------------------------------------------

    @Test
    void indexOf_presentElement_returnsFirstOccurrenceIndex() {
        list.addLast(5);
        list.addLast(10);
        list.addLast(5);
        assertEquals(0, list.indexOf(5));
    }

    @Test
    void indexOf_absentElement_returnsNegativeOne() {
        list.addLast(1);
        list.addLast(2);
        assertEquals(-1, list.indexOf(99));
    }

    @Test
    void indexOf_emptyList_returnsNegativeOne() {
        assertEquals(-1, list.indexOf(7));
    }

    @Test
    void indexOf_nullValue_returnsCorrectIndex() {
        SinglyLinkedList<String> strList = new SinglyLinkedList<>();
        strList.addLast("a");
        strList.addLast(null);
        strList.addLast("b");
        assertEquals(1, strList.indexOf(null));
    }

    @Test
    void indexOf_nullAbsent_returnsNegativeOne() {
        SinglyLinkedList<String> strList = new SinglyLinkedList<>();
        strList.addLast("a");
        assertEquals(-1, strList.indexOf(null));
    }

    // -------------------------------------------------------------------------
    // contains
    // -------------------------------------------------------------------------

    @Test
    void contains_presentElement_returnsTrue() {
        list.addLast(7);
        list.addLast(14);
        assertTrue(list.contains(14));
    }

    @Test
    void contains_absentElement_returnsFalse() {
        list.addLast(1);
        assertFalse(list.contains(999));
    }

    @Test
    void contains_emptyList_returnsFalse() {
        assertFalse(list.contains(0));
    }

    @Test
    void contains_nullValue_worksCorrectly() {
        SinglyLinkedList<String> strList = new SinglyLinkedList<>();
        strList.addLast(null);
        assertTrue(strList.contains(null));
    }

    // -------------------------------------------------------------------------
    // reverse
    // -------------------------------------------------------------------------

    @Test
    void reverse_emptyList_remainsEmpty() {
        list.reverse();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void reverse_singleElement_unchanged() {
        list.addLast(42);
        list.reverse();
        assertEquals(List.of(42), list.toList());
    }

    @Test
    void reverse_multipleElements_orderIsFlipped() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.reverse();
        assertEquals(List.of(4, 3, 2, 1), list.toList());
        assertEquals(4, list.size());
    }

    @Test
    void reverse_twoElements_orderIsSwapped() {
        list.addLast(1);
        list.addLast(2);
        list.reverse();
        assertEquals(List.of(2, 1), list.toList());
    }

    @Test
    void reverse_twice_restoresOriginalOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();
        list.reverse();
        assertEquals(List.of(1, 2, 3), list.toList());
    }

    @Test
    void reverse_addLastAfterReverse_appendsAtNewTail() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();           // [3, 2, 1]
        list.addLast(0);          // [3, 2, 1, 0]
        assertEquals(List.of(3, 2, 1, 0), list.toList());
    }

    @Test
    void reverse_addFirstAfterReverse_prependsAtNewHead() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();           // [3, 2, 1]
        list.addFirst(4);         // [4, 3, 2, 1]
        assertEquals(List.of(4, 3, 2, 1), list.toList());
    }

    // -------------------------------------------------------------------------
    // toList
    // -------------------------------------------------------------------------

    @Test
    void toList_emptyList_returnsEmptyList() {
        assertEquals(List.of(), list.toList());
    }

    @Test
    void toList_returnsCopyNotLiveView() {
        list.addLast(1);
        List<Integer> snapshot = list.toList();
        list.addLast(2);
        assertEquals(1, snapshot.size()); // snapshot unchanged
    }

    // -------------------------------------------------------------------------
    // Duplicate values
    // -------------------------------------------------------------------------

    @Test
    void duplicates_sizeAndContentsAreCorrect() {
        list.addLast(5);
        list.addLast(5);
        list.addLast(5);
        assertEquals(3, list.size());
        assertEquals(List.of(5, 5, 5), list.toList());
        assertEquals(0, list.indexOf(5)); // first occurrence
        assertTrue(list.contains(5));
    }

    // -------------------------------------------------------------------------
    // toString (smoke test)
    // -------------------------------------------------------------------------

    @Test
    void toString_nonEmptyList_containsElements() {
        list.addLast(1);
        list.addLast(2);
        String s = list.toString();
        assertTrue(s.contains("1"));
        assertTrue(s.contains("2"));
    }
}
