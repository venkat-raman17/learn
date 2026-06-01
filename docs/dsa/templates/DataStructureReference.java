/*
 * TEMPLATE — not compiled (lives outside src/). Copy into the matching package under
 * dsa-java/src/main/java/com/venkat/dsa/..., rename, delete this banner, fill in. Then write a
 * sibling test under src/test/... See docs/dsa/CONVENTIONS.md.
 *
 * Reference-implementation style: a complete, documented data structure you read to learn.
 */
package com.venkat.dsa.linear;

/**
 * One-line definition of what this structure is.
 *
 * <p>Backed by &lt;representation&gt;. Maintains the invariant(s): &lt;state them&gt;.
 *
 * <h2>Operations</h2>
 * <pre>
 *   Operation     Time      Space   Notes
 *   ---------     ----      -----   -----
 *   get(i)        O(1)      —
 *   add(x)        O(1)*     —       amortized; O(n) when it grows
 *   remove(i)     O(n)      —       shifts trailing elements
 * </pre>
 *
 * <p><b>When to use:</b> &lt;trade-offs vs. alternatives&gt;.
 * <b>Interview notes:</b> &lt;gotchas, follow-ups, variations&gt;.
 *
 * @param <T> element type
 */
public class TemplateStructure<T> {

    /** O(1). Returns the element at {@code index}. */
    public T get(int index) {
        throw new UnsupportedOperationException("template");
    }

    /** O(1) amortized. Appends {@code value}; resizes (O(n)) when capacity is exceeded. */
    public void add(T value) {
        throw new UnsupportedOperationException("template");
    }

    /** O(1). Current number of elements. */
    public int size() {
        throw new UnsupportedOperationException("template");
    }
}
