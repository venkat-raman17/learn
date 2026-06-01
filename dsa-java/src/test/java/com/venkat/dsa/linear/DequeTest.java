package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Deque}.
 *
 * <p>Coverage: addFirst, addLast, removeFirst, removeLast, peekFirst, peekLast,
 * size, isEmpty, wrap-around behaviour, automatic resize, duplicate elements,
 * and all empty-deque error conditions.
 */
class DequeTest {

    private Deque<Integer> deque;

    @BeforeEach
    void setUp() {
        deque = new Deque<>();
    }

    // -------------------------------------------------------------------------
    // isEmpty / size on fresh deque
    // -------------------------------------------------------------------------

    @Test
    void newDeque_isEmptyAndSizeIsZero() {
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
    }

    // -------------------------------------------------------------------------
    // addLast / removeFirst — queue-style FIFO
    // -------------------------------------------------------------------------

    @Test
    void addLast_thenRemoveFirst_returnsFIFOOrder() {
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        assertEquals(3, deque.size());
        assertFalse(deque.isEmpty());

        assertEquals(1, deque.removeFirst());
        assertEquals(2, deque.removeFirst());
        assertEquals(3, deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    // -------------------------------------------------------------------------
    // addFirst / removeLast — reverse queue-style
    // -------------------------------------------------------------------------

    @Test
    void addFirst_thenRemoveLast_returnsInsertionOrder() {
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        // logical order front→back: 3, 2, 1
        assertEquals(1, deque.removeLast());
        assertEquals(2, deque.removeLast());
        assertEquals(3, deque.removeLast());
    }

    // -------------------------------------------------------------------------
    // addFirst / removeFirst — stack-style LIFO
    // -------------------------------------------------------------------------

    @Test
    void addFirst_thenRemoveFirst_returnsLIFOOrder() {
        deque.addFirst(10);
        deque.addFirst(20);
        deque.addFirst(30);

        assertEquals(30, deque.removeFirst());
        assertEquals(20, deque.removeFirst());
        assertEquals(10, deque.removeFirst());
    }

    // -------------------------------------------------------------------------
    // addLast / removeLast — reverse stack
    // -------------------------------------------------------------------------

    @Test
    void addLast_thenRemoveLast_returnsReverseOrder() {
        deque.addLast(10);
        deque.addLast(20);
        deque.addLast(30);

        assertEquals(30, deque.removeLast());
        assertEquals(20, deque.removeLast());
        assertEquals(10, deque.removeLast());
    }

    // -------------------------------------------------------------------------
    // peekFirst / peekLast — non-destructive
    // -------------------------------------------------------------------------

    @Test
    void peekFirst_doesNotRemoveElement() {
        deque.addLast(5);
        deque.addLast(10);

        assertEquals(5, deque.peekFirst());
        assertEquals(2, deque.size()); // still 2 elements
    }

    @Test
    void peekLast_doesNotRemoveElement() {
        deque.addLast(5);
        deque.addLast(10);

        assertEquals(10, deque.peekLast());
        assertEquals(2, deque.size());
    }

    @Test
    void peekFirst_afterAddFirst_returnsNewestFront() {
        deque.addLast(1);
        deque.addFirst(99);

        assertEquals(99, deque.peekFirst());
        assertEquals(1, deque.peekLast());
    }

    // -------------------------------------------------------------------------
    // Single-element edge cases
    // -------------------------------------------------------------------------

    @Test
    void singleElement_peekFirstAndLastAreEqual() {
        deque.addLast(42);
        assertEquals(42, deque.peekFirst());
        assertEquals(42, deque.peekLast());
        assertEquals(1, deque.size());
    }

    @Test
    void singleElement_removeFirst_leavesDequeEmpty() {
        deque.addLast(42);
        assertEquals(42, deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    @Test
    void singleElement_removeLast_leavesDequeEmpty() {
        deque.addFirst(42);
        assertEquals(42, deque.removeLast());
        assertTrue(deque.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Wrap-around (circular buffer) — force head past index 0
    // -------------------------------------------------------------------------

    @Test
    void wrapAround_removeSeveralFromFrontThenAddMore() {
        // Use small initial capacity to trigger wrap-around early
        Deque<Integer> small = new Deque<>(4);

        // Fill: [1, 2, 3, 4], head=0, tail=0 (full)
        small.addLast(1);
        small.addLast(2);
        small.addLast(3);
        small.addLast(4);

        // Remove 3 from front — head advances to index 3
        assertEquals(1, small.removeFirst());
        assertEquals(2, small.removeFirst());
        assertEquals(3, small.removeFirst());

        // Now add 5 and 6 — tail wraps around past end of backing array
        small.addLast(5);
        small.addLast(6);

        // Logical order: 4, 5, 6
        assertEquals(3, small.size());
        assertEquals(4, small.removeFirst());
        assertEquals(5, small.removeFirst());
        assertEquals(6, small.removeFirst());
        assertTrue(small.isEmpty());
    }

    @Test
    void wrapAround_addFirstWrapsHeadBelowZero() {
        Deque<Integer> small = new Deque<>(4);

        // Put elements via addLast to advance tail
        small.addLast(10);
        small.addLast(20);
        // Remove from front to push head forward
        small.removeFirst(); // head = 1
        small.removeFirst(); // head = 2

        // addFirst twice — head wraps: 1, then 0, then back to 3
        small.addFirst(99);  // head = 1
        small.addFirst(88);  // head = 0

        assertEquals(2, small.size());
        assertEquals(88, small.peekFirst());
        assertEquals(99, small.peekLast());
    }

    // -------------------------------------------------------------------------
    // Automatic resize
    // -------------------------------------------------------------------------

    @Test
    void resize_addBeyondInitialCapacity_allElementsPreserved() {
        // Default capacity is 8; add 20 elements to force at least one resize
        for (int i = 1; i <= 20; i++) {
            deque.addLast(i);
        }
        assertEquals(20, deque.size());

        for (int i = 1; i <= 20; i++) {
            assertEquals(i, deque.removeFirst());
        }
        assertTrue(deque.isEmpty());
    }

    @Test
    void resize_addFirstBeyondCapacity_orderPreserved() {
        Deque<Integer> d = new Deque<>(2);
        // Add to front: logical order will be 4,3,2,1
        d.addFirst(1);
        d.addFirst(2); // resize from 2 → 4
        d.addFirst(3);
        d.addFirst(4); // resize from 4 → 8

        assertEquals(4, d.size());
        assertEquals(4, d.removeFirst());
        assertEquals(3, d.removeFirst());
        assertEquals(2, d.removeFirst());
        assertEquals(1, d.removeFirst());
    }

    @Test
    void resize_mixedAddFirstAndAddLast_orderPreserved() {
        Deque<Integer> d = new Deque<>(2);
        d.addLast(3);
        d.addLast(4);  // triggers resize → capacity 4
        d.addFirst(2);
        d.addFirst(1); // triggers resize → capacity 8

        // logical order: 1, 2, 3, 4
        assertEquals(4, d.size());
        assertEquals(1, d.removeFirst());
        assertEquals(2, d.removeFirst());
        assertEquals(3, d.removeFirst());
        assertEquals(4, d.removeFirst());
    }

    // -------------------------------------------------------------------------
    // Duplicate elements
    // -------------------------------------------------------------------------

    @Test
    void duplicateElements_allStoredAndReturnedCorrectly() {
        deque.addLast(7);
        deque.addLast(7);
        deque.addLast(7);

        assertEquals(3, deque.size());
        assertEquals(7, deque.removeFirst());
        assertEquals(7, deque.removeFirst());
        assertEquals(7, deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Null elements
    // -------------------------------------------------------------------------

    @Test
    void nullElements_acceptedAndReturnedCorrectly() {
        deque.addLast(null);
        deque.addLast(1);
        deque.addFirst(null);

        // logical order: null, null, 1
        assertNull(deque.removeFirst());
        assertNull(deque.removeFirst());
        assertEquals(1, deque.removeFirst());
    }

    // -------------------------------------------------------------------------
    // Error conditions on empty deque
    // -------------------------------------------------------------------------

    @Test
    void removeFirst_onEmptyDeque_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> deque.removeFirst());
    }

    @Test
    void removeLast_onEmptyDeque_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> deque.removeLast());
    }

    @Test
    void peekFirst_onEmptyDeque_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> deque.peekFirst());
    }

    @Test
    void peekLast_onEmptyDeque_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> deque.peekLast());
    }

    @Test
    void removeFirst_onEmptyDeque_afterDraining_throwsNoSuchElementException() {
        deque.addLast(1);
        deque.removeFirst();
        // Now empty again
        assertThrows(NoSuchElementException.class, () -> deque.removeFirst());
    }

    // -------------------------------------------------------------------------
    // Constructor validation
    // -------------------------------------------------------------------------

    @Test
    void constructor_withZeroCapacity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Deque<>(0));
    }

    @Test
    void constructor_withNegativeCapacity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Deque<>(-5));
    }

    // -------------------------------------------------------------------------
    // Interleaved add/remove from both ends
    // -------------------------------------------------------------------------

    @Test
    void interleavedOperations_bothEnds_correctState() {
        deque.addLast(2);   // [2]
        deque.addFirst(1);  // [1, 2]
        deque.addLast(3);   // [1, 2, 3]

        assertEquals(1, deque.removeFirst()); // [2, 3]
        assertEquals(3, deque.removeLast());  // [2]

        deque.addFirst(0);  // [0, 2]
        deque.addLast(4);   // [0, 2, 4]

        assertEquals(3, deque.size());
        assertEquals(0, deque.peekFirst());
        assertEquals(4, deque.peekLast());

        assertEquals(0, deque.removeFirst());
        assertEquals(4, deque.removeLast());
        assertEquals(2, deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Size tracks correctly through a series of operations
    // -------------------------------------------------------------------------

    @Test
    void size_tracksCorrectlyThroughMixedOperations() {
        assertEquals(0, deque.size());
        deque.addLast(1);
        assertEquals(1, deque.size());
        deque.addFirst(0);
        assertEquals(2, deque.size());
        deque.removeFirst();
        assertEquals(1, deque.size());
        deque.removeLast();
        assertEquals(0, deque.size());
    }
}
