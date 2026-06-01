package com.venkat.dsa.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BinaryHeap} — a generic binary min-heap.
 *
 * <p>Covers: empty state, single element, ordered / reverse / random inserts,
 * ascending poll order, peek accuracy, duplicates, boundary conditions, null
 * rejection, and exception contracts.
 */
class BinaryHeapTest {

    private BinaryHeap<Integer> heap;

    @BeforeEach
    void setUp() {
        heap = new BinaryHeap<>();
    }

    // -----------------------------------------------------------------------
    // isEmpty / size on fresh instance
    // -----------------------------------------------------------------------

    @Test
    void newHeapIsEmpty() {
        assertTrue(heap.isEmpty());
    }

    @Test
    void newHeapHasSizeZero() {
        assertEquals(0, heap.size());
    }

    // -----------------------------------------------------------------------
    // peek / poll throw on empty heap
    // -----------------------------------------------------------------------

    @Test
    void peekOnEmptyHeapThrowsNoSuchElement() {
        assertThrows(NoSuchElementException.class, () -> heap.peek());
    }

    @Test
    void pollOnEmptyHeapThrowsNoSuchElement() {
        assertThrows(NoSuchElementException.class, () -> heap.poll());
    }

    // -----------------------------------------------------------------------
    // add throws on null
    // -----------------------------------------------------------------------

    @Test
    void addNullThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> heap.add(null));
    }

    // -----------------------------------------------------------------------
    // Single-element behaviour
    // -----------------------------------------------------------------------

    @Test
    void afterAddOneElementIsNotEmpty() {
        heap.add(42);
        assertFalse(heap.isEmpty());
    }

    @Test
    void afterAddOneElementSizeIsOne() {
        heap.add(42);
        assertEquals(1, heap.size());
    }

    @Test
    void peekReturnsSingleElement() {
        heap.add(7);
        assertEquals(7, heap.peek());
    }

    @Test
    void peekDoesNotRemoveElement() {
        heap.add(7);
        heap.peek();
        assertEquals(1, heap.size());
    }

    @Test
    void pollReturnsSingleElementAndHeapBecomesEmpty() {
        heap.add(7);
        assertEquals(7, heap.poll());
        assertTrue(heap.isEmpty());
    }

    // -----------------------------------------------------------------------
    // poll returns elements in ascending (min-first) order
    // -----------------------------------------------------------------------

    @Test
    void pollReturnsAscendingOrderAfterOrderedInserts() {
        // Insert already sorted: 1, 2, 3, 4, 5
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);

        assertEquals(1, heap.poll());
        assertEquals(2, heap.poll());
        assertEquals(3, heap.poll());
        assertEquals(4, heap.poll());
        assertEquals(5, heap.poll());
        assertTrue(heap.isEmpty());
    }

    @Test
    void pollReturnsAscendingOrderAfterReverseOrderInserts() {
        // Insert in reverse: 5, 4, 3, 2, 1
        heap.add(5);
        heap.add(4);
        heap.add(3);
        heap.add(2);
        heap.add(1);

        assertEquals(1, heap.poll());
        assertEquals(2, heap.poll());
        assertEquals(3, heap.poll());
        assertEquals(4, heap.poll());
        assertEquals(5, heap.poll());
        assertTrue(heap.isEmpty());
    }

    @Test
    void pollReturnsAscendingOrderAfterArbitraryInserts() {
        // Arbitrary order: 9, 3, 7, 1, 5
        heap.add(9);
        heap.add(3);
        heap.add(7);
        heap.add(1);
        heap.add(5);

        assertEquals(1, heap.poll());
        assertEquals(3, heap.poll());
        assertEquals(5, heap.poll());
        assertEquals(7, heap.poll());
        assertEquals(9, heap.poll());
    }

    // -----------------------------------------------------------------------
    // peek always reflects current minimum
    // -----------------------------------------------------------------------

    @Test
    void peekReflectsMinimumAfterEachAdd() {
        heap.add(10);
        assertEquals(10, heap.peek());

        heap.add(5);
        assertEquals(5, heap.peek());   // 5 is new min

        heap.add(8);
        assertEquals(5, heap.peek());   // 5 still min

        heap.add(1);
        assertEquals(1, heap.peek());   // 1 is new min
    }

    @Test
    void peekAfterPollReflectsNewMin() {
        heap.add(4);
        heap.add(2);
        heap.add(6);

        heap.poll();                    // removes 2
        assertEquals(4, heap.peek());  // next min is 4
    }

    // -----------------------------------------------------------------------
    // Duplicate elements
    // -----------------------------------------------------------------------

    @Test
    void duplicatesAreStoredAndReturnedCorrectly() {
        heap.add(3);
        heap.add(3);
        heap.add(3);

        assertEquals(3, heap.poll());
        assertEquals(3, heap.poll());
        assertEquals(3, heap.poll());
        assertTrue(heap.isEmpty());
    }

    @Test
    void mixedDuplicatesReturnAscendingOrder() {
        heap.add(2);
        heap.add(1);
        heap.add(2);
        heap.add(1);
        heap.add(3);

        assertEquals(1, heap.poll());
        assertEquals(1, heap.poll());
        assertEquals(2, heap.poll());
        assertEquals(2, heap.poll());
        assertEquals(3, heap.poll());
    }

    // -----------------------------------------------------------------------
    // Size tracking through mixed operations
    // -----------------------------------------------------------------------

    @Test
    void sizeTracksAddsAndPolls() {
        heap.add(10);
        heap.add(20);
        heap.add(30);
        assertEquals(3, heap.size());

        heap.poll();
        assertEquals(2, heap.size());

        heap.poll();
        heap.poll();
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Many adds (stress / growth)
    // -----------------------------------------------------------------------

    @Test
    void manyAddsProduceCorrectAscendingPollOrder() {
        int n = 100;
        // Insert values 100 down to 1 (reverse order stresses sift-up)
        for (int i = n; i >= 1; i--) {
            heap.add(i);
        }
        assertEquals(n, heap.size());

        for (int expected = 1; expected <= n; expected++) {
            assertEquals(expected, heap.poll(),
                "poll #" + expected + " should return " + expected);
        }
        assertTrue(heap.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Generic type: works with String
    // -----------------------------------------------------------------------

    @Test
    void worksWithStringNaturalOrdering() {
        BinaryHeap<String> strHeap = new BinaryHeap<>();
        strHeap.add("banana");
        strHeap.add("apple");
        strHeap.add("cherry");

        // Natural String ordering: apple < banana < cherry
        assertEquals("apple",  strHeap.poll());
        assertEquals("banana", strHeap.poll());
        assertEquals("cherry", strHeap.poll());
        assertTrue(strHeap.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Interleaved add and poll
    // -----------------------------------------------------------------------

    @Test
    void interleavedAddAndPollMaintainsHeapOrder() {
        heap.add(5);
        heap.add(3);
        assertEquals(3, heap.poll()); // removes 3, heap has [5]

        heap.add(1);
        heap.add(4);
        assertEquals(1, heap.poll()); // removes 1, heap has [4, 5] (or equivalent)

        heap.add(2);
        // heap now contains: 5, 4, 2
        assertEquals(2, heap.poll());
        assertEquals(4, heap.poll());
        assertEquals(5, heap.poll());
        assertTrue(heap.isEmpty());
    }
}
