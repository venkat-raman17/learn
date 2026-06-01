package com.venkat.dsa.linear;

import java.util.NoSuchElementException;

/**
 * Double-Ended Queue (Deque) — a linear data structure that supports insertion
 * and removal at both the front and rear in O(1) amortized time.
 *
 * <p><b>Backing representation:</b> Circular array (ring buffer). A {@code head}
 * index points to the first element; a {@code tail} index points one past the
 * last element. Both indices wrap around using modulo arithmetic, eliminating
 * the need to shift elements on front operations.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code size >= 0} and {@code size <= data.length}</li>
 *   <li>When {@code size == data.length} a resize doubles capacity before the
 *       next insertion.</li>
 *   <li>{@code head} and {@code tail} are always in {@code [0, data.length)}.</li>
 * </ul>
 *
 * <p><b>Operations table:</b>
 * <pre>
 * Operation      Time (avg) Time (worst) Space
 * -------------------------------------------------
 * addFirst       O(1)*      O(n) resize  O(1)
 * addLast        O(1)*      O(n) resize  O(1)
 * removeFirst    O(1)       O(1)         O(1)
 * removeLast     O(1)       O(1)         O(1)
 * peekFirst      O(1)       O(1)         O(1)
 * peekLast       O(1)       O(1)         O(1)
 * size           O(1)       O(1)         O(1)
 * isEmpty        O(1)       O(1)         O(1)
 *
 * * amortized — occasional O(n) resize doubled capacity
 * Overall space: O(n)
 * </pre>
 *
 * <p><b>When to use:</b> When you need efficient insertion/removal at both
 * ends — e.g., sliding-window algorithms, palindrome checks, work-stealing
 * schedulers, undo/redo stacks, BFS with priority at front.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>A Deque generalises both Stack (addFirst/removeFirst) and Queue
 *       (addLast/removeFirst).</li>
 *   <li>Circular array avoids O(n) shifts; linked-list alternative avoids
 *       resize but has higher constant factors and poor cache locality.</li>
 *   <li>Java's {@code java.util.ArrayDeque} uses the same circular-array
 *       strategy internally.</li>
 *   <li>Common trap: forgetting to wrap indices with {@code (head - 1 + capacity) % capacity}
 *       when stepping backward past index 0.</li>
 * </ul>
 *
 * @param <T> type of elements held in this deque
 */
public class Deque<T> {

    private static final int DEFAULT_CAPACITY = 8;

    private Object[] data;
    private int head; // index of the first element
    private int tail; // index one past the last element
    private int size;

    /**
     * Constructs an empty deque with the default initial capacity (8).
     */
    public Deque() {
        data = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * Constructs an empty deque with the given initial capacity.
     *
     * @param initialCapacity must be &gt;= 1
     * @throws IllegalArgumentException if {@code initialCapacity < 1}
     */
    public Deque(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("initialCapacity must be >= 1");
        }
        data = new Object[initialCapacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Inserts the specified element at the front of this deque.
     * Amortized O(1); O(n) when a resize is triggered.
     *
     * @param item element to add (may be {@code null})
     */
    public void addFirst(T item) {
        ensureCapacity();
        // Step head backward one position (wrapping around)
        head = (head - 1 + data.length) % data.length;
        data[head] = item;
        size++;
    }

    /**
     * Appends the specified element to the end of this deque.
     * Amortized O(1); O(n) when a resize is triggered.
     *
     * @param item element to add (may be {@code null})
     */
    public void addLast(T item) {
        ensureCapacity();
        data[tail] = item;
        tail = (tail + 1) % data.length;
        size++;
    }

    /**
     * Removes and returns the element at the front of this deque.
     * O(1).
     *
     * @return the element previously at the front
     * @throws NoSuchElementException if the deque is empty
     */
    public T removeFirst() {
        checkNotEmpty();
        @SuppressWarnings("unchecked")
        T item = (T) data[head];
        data[head] = null; // help GC
        head = (head + 1) % data.length;
        size--;
        return item;
    }

    /**
     * Removes and returns the element at the end of this deque.
     * O(1).
     *
     * @return the element previously at the end
     * @throws NoSuchElementException if the deque is empty
     */
    public T removeLast() {
        checkNotEmpty();
        tail = (tail - 1 + data.length) % data.length;
        @SuppressWarnings("unchecked")
        T item = (T) data[tail];
        data[tail] = null; // help GC
        size--;
        return item;
    }

    /**
     * Returns (without removing) the element at the front of this deque.
     * O(1).
     *
     * @return the element at the front
     * @throws NoSuchElementException if the deque is empty
     */
    public T peekFirst() {
        checkNotEmpty();
        @SuppressWarnings("unchecked")
        T item = (T) data[head];
        return item;
    }

    /**
     * Returns (without removing) the element at the end of this deque.
     * O(1).
     *
     * @return the element at the end
     * @throws NoSuchElementException if the deque is empty
     */
    public T peekLast() {
        checkNotEmpty();
        int lastIndex = (tail - 1 + data.length) % data.length;
        @SuppressWarnings("unchecked")
        T item = (T) data[lastIndex];
        return item;
    }

    /**
     * Returns the number of elements in this deque.
     * O(1).
     *
     * @return current element count
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this deque contains no elements.
     * O(1).
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /** Doubles capacity when the backing array is full. */
    private void ensureCapacity() {
        if (size < data.length) {
            return;
        }
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];
        // Copy elements in logical order starting from head
        for (int i = 0; i < size; i++) {
            newData[i] = data[(head + i) % data.length];
        }
        data = newData;
        head = 0;
        tail = size;
    }

    /** Throws if the deque is empty. */
    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
    }
}
