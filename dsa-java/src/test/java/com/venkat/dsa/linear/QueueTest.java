package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Queue} — a generic FIFO circular-array queue.
 *
 * <p>Coverage: FIFO ordering, wrap-around after many enqueue/dequeue cycles,
 * automatic resize (grow and shrink), null elements, duplicates, and all
 * error conditions.
 */
class QueueTest {

    private Queue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new Queue<>();
    }

    // -----------------------------------------------------------------------
    // isEmpty / size on fresh queue
    // -----------------------------------------------------------------------

    @Test
    void newQueueIsEmpty() {
        assertTrue(queue.isEmpty());
    }

    @Test
    void newQueueHasSizeZero() {
        assertEquals(0, queue.size());
    }

    // -----------------------------------------------------------------------
    // Single element
    // -----------------------------------------------------------------------

    @Test
    void enqueueSingleElementMakesQueueNonEmpty() {
        queue.enqueue(42);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
    }

    @Test
    void peekAfterSingleEnqueueReturnsThatElement() {
        queue.enqueue(7);
        assertEquals(7, queue.peek());
    }

    @Test
    void peekDoesNotRemoveElement() {
        queue.enqueue(7);
        queue.peek();
        assertEquals(1, queue.size());
    }

    @Test
    void dequeueAfterSingleEnqueueReturnsThatElement() {
        queue.enqueue(99);
        assertEquals(99, queue.dequeue());
    }

    @Test
    void dequeueAfterSingleEnqueueLeavesQueueEmpty() {
        queue.enqueue(99);
        queue.dequeue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    // -----------------------------------------------------------------------
    // FIFO ordering
    // -----------------------------------------------------------------------

    @Test
    void dequeueReturnsElementsInFifoOrder() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
    }

    @Test
    void peekAlwaysReturnsHeadElement() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(10, queue.peek());
        queue.dequeue();
        assertEquals(20, queue.peek());
        queue.dequeue();
        assertEquals(30, queue.peek());
    }

    // -----------------------------------------------------------------------
    // Duplicates
    // -----------------------------------------------------------------------

    @Test
    void duplicateValuesAreEnqueuedAndDequeuedCorrectly() {
        queue.enqueue(5);
        queue.enqueue(5);
        queue.enqueue(5);

        assertEquals(3, queue.size());
        assertEquals(5, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Null elements
    // -----------------------------------------------------------------------

    @Test
    void nullElementsAreAccepted() {
        Queue<String> q = new Queue<>();
        q.enqueue(null);
        assertEquals(1, q.size());
        assertNull(q.peek());
        assertNull(q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void nullAndNonNullMixed() {
        Queue<String> q = new Queue<>();
        q.enqueue("a");
        q.enqueue(null);
        q.enqueue("b");

        assertEquals("a", q.dequeue());
        assertNull(q.dequeue());
        assertEquals("b", q.dequeue());
    }

    // -----------------------------------------------------------------------
    // Wrap-around (circular buffer behavior)
    // -----------------------------------------------------------------------

    /**
     * Fill the queue to capacity, drain half, then fill again so that
     * the tail index wraps around past the end of the backing array.
     * Verifies all values come out in correct FIFO order.
     *
     * <p>Uses initial capacity 4 so the wrap occurs quickly and deterministically.
     */
    @Test
    void wrapAroundAfterManyEnqueueDequeue() {
        Queue<Integer> q = new Queue<>(4); // capacity = 4

        // Fill to capacity: [0, 1, 2, 3]  head=0, tail=0(full), size=4
        q.enqueue(0);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        // Drain two elements: head advances to index 2
        assertEquals(0, q.dequeue());
        assertEquals(1, q.dequeue());
        // Now size=2, internal head=2, tail=0 (wrapped conceptually)

        // Enqueue two more — tail wraps around past end of array
        q.enqueue(4);
        q.enqueue(5);
        // size=4 again, elements in logical order: 2, 3, 4, 5

        assertEquals(2, q.dequeue());
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertEquals(5, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void interleavedEnqueueDequeuePreservesFifo() {
        // Interleave to force many wrap-around cycles on a small backing array
        Queue<Integer> q = new Queue<>(2);

        for (int round = 0; round < 10; round++) {
            q.enqueue(round * 10);
            q.enqueue(round * 10 + 1);
            assertEquals(round * 10,     q.dequeue());
            assertEquals(round * 10 + 1, q.dequeue());
        }
        assertTrue(q.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Resize — grow
    // -----------------------------------------------------------------------

    @Test
    void queueGrowsBeyondInitialCapacity() {
        // Default capacity is 8; enqueue 20 elements to force at least one resize
        for (int i = 0; i < 20; i++) {
            queue.enqueue(i);
        }
        assertEquals(20, queue.size());
    }

    @Test
    void fifoOrderPreservedAfterGrowResize() {
        for (int i = 0; i < 20; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertTrue(queue.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Resize — shrink
    // -----------------------------------------------------------------------

    @Test
    void queueShrinksAfterManyDequeues() {
        // Fill well past default capacity, then drain most elements.
        // The shrink should happen automatically; verify correctness, not internals.
        for (int i = 0; i < 32; i++) {
            queue.enqueue(i);
        }
        // Drain all but 2 (triggers multiple shrink cycles)
        for (int i = 0; i < 30; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertEquals(2, queue.size());
        assertEquals(30, queue.dequeue());
        assertEquals(31, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Error conditions
    // -----------------------------------------------------------------------

    @Test
    void dequeueOnEmptyQueueThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
    }

    @Test
    void peekOnEmptyQueueThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> queue.peek());
    }

    @Test
    void dequeueOnDrainedQueueThrowsNoSuchElementException() {
        queue.enqueue(1);
        queue.dequeue(); // drains the queue
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
    }

    @Test
    void constructorWithZeroCapacityThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Queue<>(0));
    }

    @Test
    void constructorWithNegativeCapacityThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Queue<>(-1));
    }

    // -----------------------------------------------------------------------
    // Boundary / stress
    // -----------------------------------------------------------------------

    @Test
    void singleCapacityQueueResizesOnFirstEnqueue() {
        Queue<String> q = new Queue<>(1);
        q.enqueue("first");
        q.enqueue("second"); // triggers resize to capacity 2
        assertEquals("first",  q.dequeue());
        assertEquals("second", q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void sizeTracksCorrectlyThroughMixedOperations() {
        assertEquals(0, queue.size());
        queue.enqueue(1); assertEquals(1, queue.size());
        queue.enqueue(2); assertEquals(2, queue.size());
        queue.dequeue();  assertEquals(1, queue.size());
        queue.enqueue(3); assertEquals(2, queue.size());
        queue.dequeue();  assertEquals(1, queue.size());
        queue.dequeue();  assertEquals(0, queue.size());
    }

    @Test
    void queueCanBeReusedAfterFullyDrained() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.dequeue();
        queue.dequeue();

        // Re-use the same queue instance
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
}
