package com.venkat.dsa.linear;

import java.util.NoSuchElementException;

/**
 * A generic Last-In-First-Out (LIFO) stack.
 *
 * <p><b>Backing representation:</b> A resizable {@code Object[]} array. The array doubles
 * in capacity when full and halves when the size falls to one-quarter of capacity
 * (minimum capacity 4), keeping amortised cost low while avoiding memory waste.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code 0 <= size <= data.length}</li>
 *   <li>{@code data[size - 1]} is the top element when {@code size > 0}.</li>
 *   <li>Slots {@code data[size..data.length-1]} are always {@code null} (no loitering).</li>
 * </ul>
 *
 * <table border="1" summary="Operations Big-O">
 *   <tr><th>Operation</th><th>Time</th><th>Space</th></tr>
 *   <tr><td>push</td><td>O(1) amortised</td><td>O(1) amortised</td></tr>
 *   <tr><td>pop</td><td>O(1) amortised</td><td>O(1) amortised</td></tr>
 *   <tr><td>peek</td><td>O(1)</td><td>O(1)</td></tr>
 *   <tr><td>isEmpty</td><td>O(1)</td><td>O(1)</td></tr>
 *   <tr><td>size</td><td>O(1)</td><td>O(1)</td></tr>
 * </table>
 *
 * <p><b>When to use:</b> Undo/redo buffers, expression evaluation, depth-first traversal,
 * call-stack simulation, and any algorithm that needs LIFO access.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Prefer this over {@link java.util.Stack} (which is synchronized and extends Vector).</li>
 *   <li>{@link java.util.ArrayDeque} is the standard JDK alternative when you need a stack.</li>
 *   <li>The doubling strategy guarantees O(1) amortised push by spreading resize cost.</li>
 *   <li>Null elements are <em>not</em> supported intentionally; null on pop signals misuse,
 *       not an empty stack—use {@link #isEmpty()} before popping.</li>
 * </ul>
 *
 * @param <T> the type of elements held in this stack
 */
public class Stack<T> {

    private static final int DEFAULT_CAPACITY = 4;

    private Object[] data;
    private int size;

    /**
     * Constructs an empty stack with an initial capacity of {@value #DEFAULT_CAPACITY}.
     */
    public Stack() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Pushes {@code item} onto the top of this stack.
     *
     * <p>Time: O(1) amortised; Space: O(1) amortised (resize doubles the array when full).
     *
     * @param item the element to push
     */
    public void push(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size++] = item;
    }

    /**
     * Removes and returns the element at the top of this stack.
     *
     * <p>Time: O(1) amortised; Space: O(1) amortised (resize halves the array when quarter-full).
     *
     * @return the top element
     * @throws NoSuchElementException if the stack is empty
     */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        T item = (T) data[--size];
        data[size] = null; // avoid loitering
        if (size > 0 && size == data.length / 4) {
            resize(data.length / 2);
        }
        return item;
    }

    /**
     * Returns the element at the top of this stack without removing it.
     *
     * <p>Time: O(1); Space: O(1).
     *
     * @return the top element
     * @throws NoSuchElementException if the stack is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return (T) data[size - 1];
    }

    /**
     * Returns {@code true} if this stack contains no elements.
     *
     * <p>Time: O(1); Space: O(1).
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this stack.
     *
     * <p>Time: O(1); Space: O(1).
     *
     * @return number of elements
     */
    public int size() {
        return size;
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    private void resize(int newCapacity) {
        int cap = Math.max(newCapacity, DEFAULT_CAPACITY);
        Object[] copy = new Object[cap];
        System.arraycopy(data, 0, copy, 0, size);
        data = copy;
    }
}
