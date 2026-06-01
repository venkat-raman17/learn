package com.venkat.dsa.linear;

import java.util.NoSuchElementException;

/**
 * A generic fixed-capacity circular (ring) buffer.
 *
 * <p><b>Definition:</b> A FIFO queue of fixed maximum size backed by a circular array
 * where head and tail pointers wrap around modulo capacity.</p>
 *
 * <p><b>Backing representation:</b> A single {@code Object[]} array of length {@code capacity}.
 * Two integer indices, {@code head} (read pointer) and {@code tail} (write pointer), advance
 * modulo capacity. A separate {@code size} counter disambiguates the full vs. empty states.</p>
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code 0 <= size <= capacity}</li>
 *   <li>{@code 0 <= head < capacity}</li>
 *   <li>{@code 0 <= tail < capacity}</li>
 *   <li>The next element to read is at {@code array[head]}; the next write slot is {@code array[tail]}.</li>
 * </ul>
 * </p>
 *
 * <p><b>Operations:</b></p>
 * <pre>
 * +----------+----------+--------+
 * | Op       | Time     | Space  |
 * +----------+----------+--------+
 * | offer(v) | O(1)     | O(1)   |
 * | poll()   | O(1)     | O(1)   |
 * | peek()   | O(1)     | O(1)   |
 * | isFull() | O(1)     | O(1)   |
 * | isEmpty()| O(1)     | O(1)   |
 * | size()   | O(1)     | O(1)   |
 * | capacity()| O(1)   | O(1)   |
 * +----------+----------+--------+
 * </pre>
 *
 * <p><b>When to use:</b> Producer-consumer pipelines where bounded memory is required and
 * oldest data may be discarded (or producers must back-pressure). Common in audio buffers,
 * network I/O, and embedded systems where heap allocation is prohibited.</p>
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Key insight: {@code (index + 1) % capacity} gives O(1) wrap-around without branching.</li>
 *   <li>Distinguish full ({@code size == capacity}) from empty ({@code size == 0}) with a counter
 *       rather than leaving one slot empty — avoids wasting a slot and keeps {@code capacity} exact.</li>
 *   <li>Thread-safety is NOT provided; external synchronisation is required for concurrent use.</li>
 * </ul>
 * </p>
 *
 * @param <E> the type of elements held in this ring buffer
 */
public class RingBuffer<E> {

    private final Object[] array;
    private final int capacity;
    private int head;  // index of next element to read
    private int tail;  // index of next slot to write
    private int size;

    /**
     * Constructs a new RingBuffer with the given fixed capacity.
     *
     * @param capacity the maximum number of elements the buffer can hold; must be &gt; 0
     * @throws IllegalArgumentException if {@code capacity} is less than 1
     * <p><b>O(capacity)</b> time and space to allocate the backing array.</p>
     */
    public RingBuffer(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1, got: " + capacity);
        }
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    /**
     * Inserts the specified element at the tail of this buffer if space is available.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @param value the element to add; must not be {@code null}
     * @return {@code true} if the element was added, {@code false} if the buffer is full
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public boolean offer(E value) {
        if (value == null) {
            throw new NullPointerException("Null elements are not permitted");
        }
        if (isFull()) {
            return false;
        }
        array[tail] = value;
        tail = (tail + 1) % capacity;
        size++;
        return true;
    }

    /**
     * Retrieves and removes the element at the head of this buffer.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @return the head element
     * @throws NoSuchElementException if the buffer is empty
     */
    public E poll() {
        if (isEmpty()) {
            throw new NoSuchElementException("Ring buffer is empty");
        }
        @SuppressWarnings("unchecked")
        E value = (E) array[head];
        array[head] = null; // allow GC
        head = (head + 1) % capacity;
        size--;
        return value;
    }

    /**
     * Retrieves, but does not remove, the element at the head of this buffer.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @return the head element
     * @throws NoSuchElementException if the buffer is empty
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Ring buffer is empty");
        }
        @SuppressWarnings("unchecked")
        E value = (E) array[head];
        return value;
    }

    /**
     * Returns {@code true} if this buffer contains no elements.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @return {@code true} if the buffer is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns {@code true} if this buffer has reached its maximum capacity.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @return {@code true} if no more elements can be added without polling one first
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * Returns the number of elements currently in this buffer.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @return current element count
     */
    public int size() {
        return size;
    }

    /**
     * Returns the fixed maximum number of elements this buffer can hold.
     *
     * <p><b>O(1)</b> time and space.</p>
     *
     * @return the capacity supplied at construction time
     */
    public int capacity() {
        return capacity;
    }
}
