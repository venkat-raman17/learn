package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MinStackTest {

    @Test
    public void testGetMinAfterPushes() {
        MinStack ms = new MinStack();
        ms.push(-2);
        ms.push(0);
        ms.push(-3);
        assertEquals(-3, ms.getMin());
    }

    @Test
    public void testTopAfterPop() {
        MinStack ms = new MinStack();
        ms.push(-2);
        ms.push(0);
        ms.push(-3);
        ms.pop();
        assertEquals(0, ms.top());
    }

    @Test
    public void testGetMinAfterPop() {
        MinStack ms = new MinStack();
        ms.push(-2);
        ms.push(0);
        ms.push(-3);
        ms.pop();
        assertEquals(-2, ms.getMin());
    }
}
