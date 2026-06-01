package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinStackTest {

    // Official LeetCode example
    @Test
    void officialExample() {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        assertEquals(-3, minStack.getMin()); // min is -3
        minStack.pop();
        assertEquals(0, minStack.top());     // top is 0
        assertEquals(-2, minStack.getMin()); // min is now -2
    }

    @Test
    void singleElement() {
        MinStack ms = new MinStack();
        ms.push(5);
        assertEquals(5, ms.top());
        assertEquals(5, ms.getMin());
        ms.pop();
    }

    @Test
    void ascendingPushMinStaysFirst() {
        MinStack ms = new MinStack();
        ms.push(1);
        ms.push(2);
        ms.push(3);
        assertEquals(1, ms.getMin());
        ms.pop(); // remove 3
        assertEquals(1, ms.getMin());
        ms.pop(); // remove 2
        assertEquals(1, ms.getMin());
    }

    @Test
    void descendingPushMinFollowsTop() {
        MinStack ms = new MinStack();
        ms.push(3);
        ms.push(2);
        ms.push(1);
        assertEquals(1, ms.getMin());
        ms.pop(); // remove 1
        assertEquals(2, ms.getMin());
        ms.pop(); // remove 2
        assertEquals(3, ms.getMin());
    }

    @Test
    void duplicateMinValues() {
        MinStack ms = new MinStack();
        ms.push(0);
        ms.push(0);
        assertEquals(0, ms.getMin());
        ms.pop();
        assertEquals(0, ms.getMin()); // second copy still there
    }

    @Test
    void negativeValues() {
        MinStack ms = new MinStack();
        ms.push(-1);
        ms.push(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, ms.getMin());
        ms.pop();
        assertEquals(-1, ms.getMin());
    }

    @Test
    void pushPopInterleaved() {
        MinStack ms = new MinStack();
        ms.push(5);
        ms.push(3);
        ms.push(7);
        assertEquals(3, ms.getMin());
        ms.pop(); // remove 7
        assertEquals(3, ms.getMin());
        ms.push(1);
        assertEquals(1, ms.getMin());
        ms.pop(); // remove 1
        assertEquals(3, ms.getMin());
        ms.pop(); // remove 3
        assertEquals(5, ms.getMin());
    }
}
