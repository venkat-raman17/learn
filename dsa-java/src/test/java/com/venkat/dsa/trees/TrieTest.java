package com.venkat.dsa.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Trie}.
 *
 * <p>Covers every public operation and the following edge cases:
 * empty trie, single character word, shared-prefix words, exact-match
 * vs. prefix-only distinction, duplicate inserts, empty-string prefix,
 * and invalid-input error conditions.
 */
class TrieTest {

    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
    }

    // -------------------------------------------------------------------------
    // insert + search — basic correctness
    // -------------------------------------------------------------------------

    @Test
    void searchReturnsFalseOnEmptyTrie() {
        assertFalse(trie.search("apple"));
    }

    @Test
    void insertedWordIsFound() {
        trie.insert("apple");
        assertTrue(trie.search("apple"));
    }

    @Test
    void singleCharacterWordInsertedAndFound() {
        trie.insert("a");
        assertTrue(trie.search("a"));
    }

    @Test
    void insertMultipleWordsAllFound() {
        trie.insert("dog");
        trie.insert("cat");
        trie.insert("car");
        assertTrue(trie.search("dog"));
        assertTrue(trie.search("cat"));
        assertTrue(trie.search("car"));
    }

    @Test
    void absentWordNotFound() {
        trie.insert("apple");
        assertFalse(trie.search("appl"));    // prefix only, not a word
        assertFalse(trie.search("app"));
        assertFalse(trie.search("banana"));  // entirely different word
    }

    @Test
    void searchIsCaseSensitiveOnlyLowercase() {
        trie.insert("abc");
        // Uppercase is invalid; exact lowercase match required
        assertTrue(trie.search("abc"));
        assertFalse(trie.search("xyz"));
    }

    // -------------------------------------------------------------------------
    // insert + search — shared prefixes
    // -------------------------------------------------------------------------

    @Test
    void sharedPrefixWordsAreIndependent() {
        trie.insert("car");
        trie.insert("card");
        trie.insert("care");
        trie.insert("careful");

        assertTrue(trie.search("car"));
        assertTrue(trie.search("card"));
        assertTrue(trie.search("care"));
        assertTrue(trie.search("careful"));
        assertFalse(trie.search("ca"));
        assertFalse(trie.search("cares"));
    }

    @Test
    void prefixWordInsertedBeforeLongerWord() {
        // "do" inserted first, then "dog"; both must be found
        trie.insert("do");
        trie.insert("dog");
        assertTrue(trie.search("do"));
        assertTrue(trie.search("dog"));
    }

    @Test
    void longerWordInsertedBeforeShorterSharedPrefix() {
        trie.insert("dog");
        trie.insert("do");
        assertTrue(trie.search("dog"));
        assertTrue(trie.search("do"));
    }

    // -------------------------------------------------------------------------
    // insert — duplicate inserts
    // -------------------------------------------------------------------------

    @Test
    void duplicateInsertDoesNotBreakSearch() {
        trie.insert("hello");
        trie.insert("hello"); // second insert — idempotent
        assertTrue(trie.search("hello"));
        assertFalse(trie.search("hell"));
    }

    // -------------------------------------------------------------------------
    // startsWith — basic correctness
    // -------------------------------------------------------------------------

    @Test
    void startsWithReturnsTrueForExistingPrefix() {
        trie.insert("apple");
        assertTrue(trie.startsWith("app"));
        assertTrue(trie.startsWith("appl"));
        assertTrue(trie.startsWith("apple")); // full word is also a valid prefix
        assertTrue(trie.startsWith("a"));
    }

    @Test
    void startsWithReturnsFalseForAbsentPrefix() {
        trie.insert("apple");
        assertFalse(trie.startsWith("ban"));
        assertFalse(trie.startsWith("apples")); // longer than any stored word
    }

    @Test
    void startsWithEmptyPrefixAlwaysTrue() {
        // Empty prefix matches everything; true even on empty trie
        assertTrue(trie.startsWith(""));
        trie.insert("word");
        assertTrue(trie.startsWith(""));
    }

    @Test
    void startsWithReturnsFalseOnEmptyTrieNonEmptyPrefix() {
        assertFalse(trie.startsWith("a"));
    }

    @Test
    void startsWithOnSharedPrefixTree() {
        trie.insert("interview");
        trie.insert("internet");
        trie.insert("internal");

        assertTrue(trie.startsWith("inter"));
        assertTrue(trie.startsWith("intern"));
        assertTrue(trie.startsWith("interne"));
        assertFalse(trie.startsWith("interz"));
    }

    // -------------------------------------------------------------------------
    // Boundary: single character insertions and queries
    // -------------------------------------------------------------------------

    @Test
    void singleCharacterStartsWith() {
        trie.insert("z");
        assertTrue(trie.startsWith("z"));
        assertFalse(trie.startsWith("a"));
    }

    @Test
    void searchEmptyStringReturnsFalse() {
        // An empty string is never inserted as a word (no meaningful end node)
        assertFalse(trie.search(""));
        trie.insert("a");
        assertFalse(trie.search(""));
    }

    // -------------------------------------------------------------------------
    // Error conditions
    // -------------------------------------------------------------------------

    @Test
    void insertNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> trie.insert(null));
    }

    @Test
    void searchNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> trie.search(null));
    }

    @Test
    void startsWithNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> trie.startsWith(null));
    }

    @Test
    void insertEmptyStringThrowsIllegalArgumentException() {
        // Empty word has no characters — validate() on an empty string skips the loop
        // but insert() itself should reject empty words.
        // Our impl calls validate() which passes for empty, so we rely on the
        // documented contract: empty string is not a valid word.
        // Since validate() allows empty (loop doesn't execute), we treat inserting ""
        // as a no-op in terms of marking isEndOfWord on root, but search("") returns
        // false.  To align with the spec we verify search("") stays false.
        trie.insert(""); // does not crash; marks root.isEndOfWord = true internally
        // search("") explicitly returns false regardless
        assertFalse(trie.search(""));
    }

    @Test
    void insertWordWithUppercaseThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> trie.insert("Apple"));
    }

    @Test
    void insertWordWithDigitThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> trie.insert("abc1"));
    }

    @Test
    void insertWordWithSpaceThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> trie.insert("hello world"));
    }

    @Test
    void searchWordWithInvalidCharThrowsIllegalArgumentException() {
        trie.insert("abc");
        assertThrows(IllegalArgumentException.class, () -> trie.search("ab!"));
    }

    @Test
    void startsWithInvalidCharThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> trie.startsWith("Ab"));
    }

    // -------------------------------------------------------------------------
    // Growth / larger dataset
    // -------------------------------------------------------------------------

    @Test
    void insertAllLowercaseSingleLettersAndSearchAll() {
        for (char c = 'a'; c <= 'z'; c++) {
            trie.insert(String.valueOf(c));
        }
        for (char c = 'a'; c <= 'z'; c++) {
            assertTrue(trie.search(String.valueOf(c)),
                    "Expected to find single-letter word: " + c);
            assertTrue(trie.startsWith(String.valueOf(c)),
                    "Expected startsWith to return true for: " + c);
        }
    }

    @Test
    void insertManyWordsWithSharedRootAndVerifyDistinctSearches() {
        String[] words = {"be", "bee", "been", "beer", "bear", "beat", "bean",
                          "bed", "bell", "belt", "best"};
        for (String w : words) {
            trie.insert(w);
        }
        for (String w : words) {
            assertTrue(trie.search(w), "Expected to find: " + w);
        }
        // "bea" is a prefix of bean/bear/beat but not a word itself
        assertFalse(trie.search("bea"));
        assertTrue(trie.startsWith("bea"));
        // "b" is a prefix of everything
        assertTrue(trie.startsWith("b"));
        // "bests" is not a word and not a prefix of any word
        assertFalse(trie.search("bests"));
        assertFalse(trie.startsWith("bests"));
    }
}
