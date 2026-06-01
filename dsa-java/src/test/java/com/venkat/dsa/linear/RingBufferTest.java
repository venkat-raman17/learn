package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RingBuffer}.
 *
 * <p>Covers every public operation and all significant edge cases:
 * empty buffer, single-element buffer, fill to capacity, over-fill rejection,
 * wrap-around behaviour, peek without removal, and error conditions.</p>
 */
class RingBufferTest {

    private RingBuffer<Integer> buf;

    @BeforeEach
    void setUp() {
        buf = new RingBuffer<>(3);
    }

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    @Test
    void newBuffer_isEmptyAndNotFull() {
        assertTrue(buf.isEmpty());
        assertFalse(buf.isFull());
        assertEquals(0, buf.size());
        assertEquals(3, buf.capacity());
    }

    @Test
    void constructor_withZeroCapacity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer<>(0));
    }

    @Test
    void constructor_withNegativeCapacity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer<>(-5));
    }

    @Test
    void constructor_withCapacityOne_works() {
        RingBuffer<String> single = new RingBuffer<>(1);
        assertEquals(1, single.capacity());
        assertTrue(single.isEmpty());
    }

    // -------------------------------------------------------------------------
    // offer
    // -------------------------------------------------------------------------

    @Test
    void offer_intoEmptyBuffer_returnsTrueAndUpdatesSize() {
        assertTrue(buf.offer(10));
        assertEquals(1, buf.size());
        assertFalse(buf.isEmpty());
        assertFalse(buf.isFull());
    }

    @Test
    void offer_nullValue_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> buf.offer(null));
    }

    @Test
    void offer_fillsBufferToCapacity_allReturnTrue() {
        assertTrue(buf.offer(1));
        assertTrue(buf.offer(2));
        assertTrue(buf.offer(3));
        assertTrue(buf.isFull());
        assertEquals(3, buf.size());
    }

    @Test
    void offer_whenFull_returnsFalseAndSizeUnchanged() {
        buf.offer(1);
        buf.offer(2);
        buf.offer(3);

        boolean result = buf.offer(99);

        assertFalse(result);
        assertEquals(3, buf.size());
        assertTrue(buf.isFull());
    }

    @Test
    void offer_duplicateValues_accepted() {
        assertTrue(buf.offer(7));
        assertTrue(buf.offer(7));
        assertEquals(2, buf.size());
    }

    // -------------------------------------------------------------------------
    // poll
    // -------------------------------------------------------------------------

    @Test
    void poll_singleElement_returnsElementAndBufferBecomesEmpty() {
        buf.offer(42);
        int value = buf.poll();
        assertEquals(42, value);
        assertTrue(buf.isEmpty());
        assertEquals(0, buf.size());
    }

    @Test
    void poll_multipleElements_returnsFIFOOrder() {
        buf.offer(1);
        buf.offer(2);
        buf.offer(3);

        assertEquals(1, buf.poll());
        assertEquals(2, buf.poll());
        assertEquals(3, buf.poll());
    }

    @Test
    void poll_onEmptyBuffer_throwsNoSuchElement() {
        assertThrows(NoSuchElementException.class, () -> buf.poll());
    }

    @Test
    void poll_afterFillAndDrain_bufferIsEmpty() {
        buf.offer(10);
        buf.offer(20);
        buf.offer(30);

        buf.poll();
        buf.poll();
        buf.poll();

        assertTrue(buf.isEmpty());
        assertFalse(buf.isFull());
    }

    // -------------------------------------------------------------------------
    // peek
    // -------------------------------------------------------------------------

    @Test
    void peek_onNonEmptyBuffer_returnsHeadWithoutRemoving() {
        buf.offer(55);
        buf.offer(66);

        assertEquals(55, buf.peek());
        assertEquals(2, buf.size()); // element not removed
    }

    @Test
    void peek_onEmptyBuffer_throwsNoSuchElement() {
        assertThrows(NoSuchElementException.class, () -> buf.peek());
    }

    @Test
    void peek_repeatedly_alwaysReturnsSameHead() {
        buf.offer(9);
        assertEquals(9, buf.peek());
        assertEquals(9, buf.peek());
        assertEquals(1, buf.size());
    }

    // -------------------------------------------------------------------------
    // Wrap-around (circular behaviour)
    // -------------------------------------------------------------------------

    @Test
    void wrapAround_pollThenOffer_maintainsFIFOAcrossWraps() {
        // Fill buffer: [1, 2, 3], head=0, tail=0 (full)
        buf.offer(1);
        buf.offer(2);
        buf.offer(3);

        // Poll two elements: head advances to index 2
        assertEquals(1, buf.poll());
        assertEquals(2, buf.poll());

        // Offer two more — tail wraps from index 0 and 1
        assertTrue(buf.offer(4));
        assertTrue(buf.offer(5));

        // Buffer logical contents: [3, 4, 5] in FIFO order
        assertEquals(3, buf.size());
        assertEquals(3, buf.poll());
        assertEquals(4, buf.poll());
        assertEquals(5, buf.poll());
        assertTrue(buf.isEmpty());
    }

    @Test
    void wrapAround_multipleFullCycles_correctOrder() {
        // Capacity-1 ring: use a buf of size 2 for tight wrapping
        RingBuffer<Integer> tiny = new RingBuffer<>(2);

        tiny.offer(10);
        tiny.offer(20);
        assertEquals(10, tiny.poll());
        tiny.offer(30);  // tail wraps to index 0
        assertEquals(20, tiny.poll());
        assertEquals(30, tiny.poll());
        assertTrue(tiny.isEmpty());
    }

    @Test
    void wrapAround_offerAfterFullDrain_bufferReusable() {
        buf.offer(1);
        buf.offer(2);
        buf.offer(3);
        buf.poll();
        buf.poll();
        buf.poll();

        // Buffer should be reusable from scratch
        assertTrue(buf.offer(7));
        assertTrue(buf.offer(8));
        assertEquals(2, buf.size());
        assertEquals(7, buf.peek());
    }

    // -------------------------------------------------------------------------
    // Capacity-1 edge cases
    // -------------------------------------------------------------------------

    @Test
    void capacityOne_fillThenReject() {
        RingBuffer<String> one = new RingBuffer<>(1);
        assertTrue(one.offer("hello"));
        assertTrue(one.isFull());
        assertFalse(one.offer("world"));
    }

    @Test
    void capacityOne_pollMakesRoomForNext() {
        RingBuffer<String> one = new RingBuffer<>(1);
        one.offer("a");
        one.poll();
        assertTrue(one.offer("b"));
        assertEquals("b", one.peek());
    }

    // -------------------------------------------------------------------------
    // isFull / isEmpty / size / capacity consistency
    // -------------------------------------------------------------------------

    @Test
    void sizeAndIsEmpty_areConsistentThroughoutLifecycle() {
        assertEquals(0, buf.size());
        assertTrue(buf.isEmpty());

        buf.offer(1);
        assertEquals(1, buf.size());
        assertFalse(buf.isEmpty());

        buf.offer(2);
        buf.offer(3);
        assertEquals(3, buf.size());
        assertTrue(buf.isFull());

        buf.poll();
        assertEquals(2, buf.size());
        assertFalse(buf.isFull());
        assertFalse(buf.isEmpty());
    }

    @Test
    void capacity_neverChanges() {
        buf.offer(1);
        buf.offer(2);
        buf.offer(3);
        buf.poll();
        assertEquals(3, buf.capacity());
    }
}
