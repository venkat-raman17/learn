package com.venkat.dsa.graphs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavioural tests for {@link UnionFind}.
 */
class UnionFindTest {

    // ── construction ─────────────────────────────────────────────────────────

    @Test
    void singleElement_isItsOwnComponent() {
        UnionFind uf = new UnionFind(1);
        assertEquals(1, uf.count());
        assertEquals(0, uf.find(0));
        assertEquals(1, uf.size());
    }

    @Test
    void construction_allElementsAreDisjoint() {
        int n = 5;
        UnionFind uf = new UnionFind(n);
        assertEquals(n, uf.count());
        assertEquals(n, uf.size());
        // each element is its own root
        for (int i = 0; i < n; i++) {
            assertEquals(i, uf.find(i));
        }
    }

    @Test
    void construction_invalidSize_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new UnionFind(0));
        assertThrows(IllegalArgumentException.class, () -> new UnionFind(-1));
    }

    // ── find / validation ────────────────────────────────────────────────────

    @Test
    void find_outOfRange_throwsIllegalArgument() {
        UnionFind uf = new UnionFind(3);
        assertThrows(IllegalArgumentException.class, () -> uf.find(-1));
        assertThrows(IllegalArgumentException.class, () -> uf.find(3));
    }

    @Test
    void find_afterUnion_returnsSameRoot() {
        UnionFind uf = new UnionFind(4);
        uf.union(0, 1);
        assertEquals(uf.find(0), uf.find(1));
    }

    // ── union ────────────────────────────────────────────────────────────────

    @Test
    void union_differentComponents_returnsTrue() {
        UnionFind uf = new UnionFind(4);
        assertTrue(uf.union(0, 1));
    }

    @Test
    void union_alreadyConnected_returnsFalse() {
        UnionFind uf = new UnionFind(4);
        uf.union(0, 1);
        assertFalse(uf.union(0, 1));
        assertFalse(uf.union(1, 0)); // commutative
    }

    @Test
    void union_sameElement_returnsFalse() {
        UnionFind uf = new UnionFind(3);
        assertFalse(uf.union(2, 2));
    }

    @Test
    void union_outOfRange_throwsIllegalArgument() {
        UnionFind uf = new UnionFind(3);
        assertThrows(IllegalArgumentException.class, () -> uf.union(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> uf.union(0, 3));
    }

    @Test
    void union_mergePairDecrementsCount() {
        UnionFind uf = new UnionFind(4);
        assertEquals(4, uf.count());
        uf.union(0, 1);
        assertEquals(3, uf.count());
        uf.union(2, 3);
        assertEquals(2, uf.count());
        uf.union(1, 2); // merges the two groups of 2
        assertEquals(1, uf.count());
    }

    @Test
    void union_redundantMerge_doesNotDecrement() {
        UnionFind uf = new UnionFind(3);
        uf.union(0, 1);
        uf.union(0, 1); // no-op
        assertEquals(2, uf.count());
    }

    // ── connected ────────────────────────────────────────────────────────────

    @Test
    void connected_beforeUnion_returnsFalse() {
        UnionFind uf = new UnionFind(4);
        assertFalse(uf.connected(0, 3));
    }

    @Test
    void connected_afterUnion_returnsTrue() {
        UnionFind uf = new UnionFind(4);
        uf.union(1, 3);
        assertTrue(uf.connected(1, 3));
        assertTrue(uf.connected(3, 1)); // symmetry
    }

    @Test
    void connected_elementWithItself_alwaysTrue() {
        UnionFind uf = new UnionFind(5);
        for (int i = 0; i < 5; i++) {
            assertTrue(uf.connected(i, i));
        }
    }

    @Test
    void connected_transitivity() {
        // 0-1-2 chain -> 0 connected to 2 transitively
        UnionFind uf = new UnionFind(5);
        uf.union(0, 1);
        uf.union(1, 2);
        assertTrue(uf.connected(0, 2));
        assertFalse(uf.connected(0, 3));
    }

    @Test
    void connected_outOfRange_throwsIllegalArgument() {
        UnionFind uf = new UnionFind(3);
        assertThrows(IllegalArgumentException.class, () -> uf.connected(0, 5));
        assertThrows(IllegalArgumentException.class, () -> uf.connected(-1, 0));
    }

    // ── count ────────────────────────────────────────────────────────────────

    @Test
    void count_allMergedIntoOne() {
        int n = 6;
        UnionFind uf = new UnionFind(n);
        for (int i = 1; i < n; i++) {
            uf.union(0, i);
        }
        assertEquals(1, uf.count());
    }

    @Test
    void count_neverBelowOne() {
        int n = 3;
        UnionFind uf = new UnionFind(n);
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(0, 2); // already connected
        assertEquals(1, uf.count());
    }

    // ── path compression ─────────────────────────────────────────────────────

    @Test
    void find_longChain_compressesCorrectly() {
        // Build a chain 0->1->2->...->9 by exploiting equal-rank unions
        // then verify all elements find the same root
        int n = 10;
        UnionFind uf = new UnionFind(n);
        // Union sequentially to build a somewhat linear structure before compression
        for (int i = 0; i < n - 1; i++) {
            uf.union(i, i + 1);
        }
        // After connecting the whole chain, count must be 1
        assertEquals(1, uf.count());

        // All elements must share the same root
        int root = uf.find(0);
        for (int i = 1; i < n; i++) {
            assertEquals(root, uf.find(i),
                    "Element " + i + " should share root with element 0 after chain union");
        }

        // Path compression: after calling find on every element, each element's
        // parent should now be the root (nearly flat tree).
        // We verify by checking that connected(0, n-1) still holds and count stays 1.
        assertTrue(uf.connected(0, n - 1));
        assertEquals(1, uf.count());
    }

    @Test
    void find_pathCompressionDoesNotChangeConnectivity() {
        // Union 0-4, 4-3, 3-7 -> all in one component
        UnionFind uf = new UnionFind(8);
        uf.union(0, 4);
        uf.union(4, 3);
        uf.union(3, 7);
        // Before compression call
        assertTrue(uf.connected(0, 7));
        // Trigger path compression for all nodes in the component
        int root = uf.find(7);
        assertEquals(root, uf.find(0));
        assertEquals(root, uf.find(4));
        assertEquals(root, uf.find(3));
        // After compression connectivity must be unchanged
        assertTrue(uf.connected(0, 7));
        assertEquals(5, uf.count()); // 4 elements merged -> 8-3=5 components
    }

    // ── union by rank ────────────────────────────────────────────────────────

    @Test
    void union_byRank_componentCountIsCorrect() {
        // Build two trees of depth 2 and then merge them
        UnionFind uf = new UnionFind(7);
        // Group A: 0,1,2 -> count 5
        uf.union(0, 1);
        uf.union(0, 2);
        // Group B: 3,4,5 -> count 3
        uf.union(3, 4);
        uf.union(3, 5);
        // Isolated: 6
        assertEquals(3, uf.count());
        // Merge A and B
        uf.union(2, 4);
        assertEquals(2, uf.count());
        // Merge last
        uf.union(5, 6);
        assertEquals(1, uf.count());
        assertTrue(uf.connected(0, 6));
    }

    // ── size ─────────────────────────────────────────────────────────────────

    @Test
    void size_alwaysEqualsConstructorArgument() {
        for (int n : new int[]{1, 2, 10, 100}) {
            assertEquals(n, new UnionFind(n).size());
        }
    }
}
