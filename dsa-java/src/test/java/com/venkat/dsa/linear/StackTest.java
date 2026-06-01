package com.venkat.dsa.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Stack}.
 *
 * <p>Each test method is named after the behaviour it verifies.
 */
class StackTest {

    private Stack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new Stack<>();
    }

    // -----------------------------------------------------------------------
    // isEmpty / size on a fresh stack
    // -----------------------------------------------------------------------

    @Test
    void newStack_isEmptyAndSizeZero() {
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    // -----------------------------------------------------------------------
    // push
    // -----------------------------------------------------------------------

    @Test
    void push_singleElement_stackNotEmpty() {
        stack.push(42);
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
    }

    @Test
    void push_multipleElements_sizeTrackedCorrectly() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }

    // -----------------------------------------------------------------------
    // peek
    // -----------------------------------------------------------------------

    @Test
    void peek_afterPush_returnTopWithoutRemoving() {
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.peek());
        assertEquals(2, stack.size()); // peek must not remove
    }

    @Test
    void peek_onEmptyStack_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }

    // -----------------------------------------------------------------------
    // pop
    // -----------------------------------------------------------------------

    @Test
    void pop_onEmptyStack_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @Test
    void pop_singleElement_returnsElementAndBecomesEmpty() {
        stack.push(99);
        assertEquals(99, stack.pop());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void pop_multipleElements_lifoOrder() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void pop_afterPopUntilEmpty_subsequentPopThrows() {
        stack.push(5);
        stack.pop();
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    // -----------------------------------------------------------------------
    // LIFO ordering — thorough sequence
    // -----------------------------------------------------------------------

    @Test
    void pushThenPop_lifoOrderPreservedForLargeSequence() {
        int n = 10;
        for (int i = 0; i < n; i++) {
            stack.push(i);
        }
        for (int i = n - 1; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
        assertTrue(stack.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Duplicates
    // -----------------------------------------------------------------------

    @Test
    void push_duplicateValues_allStoredAndPoppedInLifoOrder() {
        stack.push(7);
        stack.push(7);
        stack.push(7);
        assertEquals(3, stack.size());
        assertEquals(7, stack.pop());
        assertEquals(7, stack.pop());
        assertEquals(7, stack.pop());
        assertTrue(stack.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Growth / resize (more than DEFAULT_CAPACITY = 4 elements)
    // -----------------------------------------------------------------------

    @Test
    void push_beyondInitialCapacity_growsCorrectlyAndLifoIntact() {
        // Default capacity is 4; push 16 elements to force multiple resizes
        int n = 16;
        for (int i = 1; i <= n; i++) {
            stack.push(i);
        }
        assertEquals(n, stack.size());
        for (int i = n; i >= 1; i--) {
            assertEquals(i, stack.pop());
        }
        assertTrue(stack.isEmpty());
    }

    @Test
    void pushAndPop_interleaved_sizeAndOrderCorrect() {
        // Push 8, pop 4, push 4 more — exercises resize down then up
        for (int i = 1; i <= 8; i++) {
            stack.push(i);
        }
        // Pop 4 (top-of-stack values: 8,7,6,5)
        assertEquals(8, stack.pop());
        assertEquals(7, stack.pop());
        assertEquals(6, stack.pop());
        assertEquals(5, stack.pop());
        assertEquals(4, stack.size());

        // Push 4 more (9,10,11,12)
        for (int i = 9; i <= 12; i++) {
            stack.push(i);
        }
        assertEquals(8, stack.size());

        // Now pop all: expect 12,11,10,9,4,3,2,1
        int[] expected = {12, 11, 10, 9, 4, 3, 2, 1};
        for (int exp : expected) {
            assertEquals(exp, stack.pop());
        }
        assertTrue(stack.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Generic type — String stack
    // -----------------------------------------------------------------------

    @Test
    void stringStack_pushAndPop_lifoOrder() {
        Stack<String> strings = new Stack<>();
        strings.push("alpha");
        strings.push("beta");
        strings.push("gamma");
        assertEquals("gamma", strings.pop());
        assertEquals("beta", strings.pop());
        assertEquals("alpha", strings.pop());
        assertTrue(strings.isEmpty());
    }

    @Test
    void stringStack_peekDoesNotMutateState() {
        Stack<String> strings = new Stack<>();
        strings.push("hello");
        assertEquals("hello", strings.peek());
        assertEquals("hello", strings.peek()); // repeatable
        assertEquals(1, strings.size());
    }

    // -----------------------------------------------------------------------
    // Boundary: single-element edge cases
    // -----------------------------------------------------------------------

    @Test
    void pushThenPeekThenPop_singleElement_correctBehaviour() {
        stack.push(100);
        assertEquals(100, stack.peek());
        assertFalse(stack.isEmpty());
        assertEquals(100, stack.pop());
        assertTrue(stack.isEmpty());
    }
}
