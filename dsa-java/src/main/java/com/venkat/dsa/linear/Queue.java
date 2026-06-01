package com.venkat.dsa.linear;

import java.util.NoSuchElementException;

/**
 * A generic FIFO (First-In, First-Out) queue backed by a circular array with dynamic resizing.
 *
 * <p><b>Definition:</b> A queue is a linear data structure that follows FIFO ordering —
 * elements are added at the rear (enqueue) and removed from the front (dequeue).
 *
 * <p><b>Backing Representation:</b> A circular (ring) array of type {@code Object[]}.
 * Two indices, {@code head} and {@code tail}, track the front and next-write positions.
 * When the array is full, capacity doubles; when the array is quarter-full (and capacity > initial),
 * capacity halves — both via array copy, preserving logical order.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code 0 <= head < capacity} and {@code 0 <= tail < capacity}</li>
 *   <li>{@code size == 0} implies the queue is empty; both {@code head} and {@code tail}
 *       may point anywhere (they are reset on resize/clear but not otherwise).</li>
 *   <li>All live elements occupy contiguous (mod-capacity) positions from {@code head}
 *       to {@code (head + size - 1) % capacity}.</li>
 * </ul>
 *
 * <p><b>Operations Table:</b>
 * <pre>
 * Operation   Time (avg)   Time (amortized)   Space
 * enqueue     O(1)*        O(1)               O(1)
 * dequeue     O(1)*        O(1)               O(1)
 * peek        O(1)         O(1)               O(1)
 * isEmpty     O(1)         O(1)               O(1)
 * size        O(1)         O(1)               O(1)
 * resize      O(n)         —                  O(n)
 * Overall storage                              O(n)
 * * amortized O(1); individual resize calls are O(n)
 * </pre>
 *
 * <p><b>When to use:</b> Use a queue when you need strict FIFO processing — BFS graph
 * traversal, task scheduling, producer/consumer pipelines, or sliding-window buffering.
 * Prefer this circular-array variant over a linked-list queue when cache locality matters
 * and the working size is somewhat predictable.
 *
 * <p><b>Interview Notes:</b>
 * <ul>
 *   <li>Circular indexing ({@code (idx + 1) % capacity}) avoids shifting elements on
 *       every dequeue — a common naive mistake.</li>
 *   <li>Know the resize-trigger trade-offs: doubling on full gives amortized O(1) enqueue;
 *       halving at 1/4 (not 1/2) prevents thrashing on alternating enqueue/dequeue.</li>
 *   <li>Distinguish from a stack (LIFO) and a deque (both-end access).</li>
 *   <li>Java's {@code ArrayDeque} is essentially this structure; knowing the internals
 *       demonstrates depth.</li>
 * </ul>
 *
 * @param <T> the type of elements held in this queue
 */
public class Queue<T> {

    private static final int DEFAULT_INITIAL_CAPACITY = 8;

    private Object[] data;
    private int head;   // index of the front element
    private int tail;   // index of the next write position
    private int size;

    /**
     * Constructs an empty queue with the default initial capacity (8).
     */
    public Queue() {
        data = new Object[DEFAULT_INITIAL_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * Constructs an empty queue with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity; must be >= 1
     * @throws IllegalArgumentException if {@code initialCapacity < 1}
     */
    public Queue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("initialCapacity must be >= 1, got: " + initialCapacity);
        }
        data = new Object[initialCapacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * Adds an element to the rear of the queue.
     *
     * <p>Amortized O(1); individual call is O(n) when a resize is triggered.
     *
     * @param element the element to add (may be {@code null})
     */
    public void enqueue(T element) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[tail] = element;
        tail = (tail + 1) % data.length;
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * <p>Amortized O(1); individual call is O(n) when a shrink is triggered.
     *
     * @return the element at the front
     * @throws NoSuchElementException if the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from an empty queue.");
        }
        T element = (T) data[head];
        data[head] = null; // help GC
        head = (head + 1) % data.length;
        size--;
        // Shrink when array is quarter-full and capacity is above minimum
        if (size > 0 && size == data.length / 4 && data.length / 2 >= DEFAULT_INITIAL_CAPACITY) {
            resize(data.length / 2);
        }
        return element;
    }

    /**
     * Returns (without removing) the element at the front of the queue.
     *
     * <p>O(1).
     *
     * @return the front element
     * @throws NoSuchElementException if the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek at an empty queue.");
        }
        return (T) data[head];
    }

    /**
     * Returns {@code true} if this queue contains no elements.
     *
     * <p>O(1).
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this queue.
     *
     * <p>O(1).
     *
     * @return the current size
     */
    public int size() {
        return size;
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    /**
     * Resizes the backing array to {@code newCapacity}, copying live elements
     * in logical FIFO order starting at index 0 of the new array.
     *
     * <p>O(n) — called only during enqueue (grow) or dequeue (shrink).
     */
    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(head + i) % data.length];
        }
        data = newData;
        head = 0;
        tail = size;
    }
}
