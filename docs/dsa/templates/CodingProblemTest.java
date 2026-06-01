/*
 * TEMPLATE — not compiled (lives outside src/). Copy into the mirror test package under
 * src/test/java/... alongside the problem, rename, delete this banner, fill in real cases.
 * Write the official examples + edge cases FIRST, then solve the stub until green. Keep @Disabled
 * while unsolved so the build stays green. See docs/dsa/CONVENTIONS.md.
 */
package com.venkat.dsa.coding.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("start me — delete this line when you begin solving")
class TemplateProblemTest {

    private final TemplateProblem sut = new TemplateProblem();

    @Test
    void example1() {
        assertEquals(/* expected */ 0, sut.solve(new int[] {/* input */}));
    }

    @Test
    void edgeCases() {
        // empty, single, duplicates, max-size, negatives, ...
    }
}
