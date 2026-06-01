package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupAnagramsTest {

    private final GroupAnagrams sol = new GroupAnagrams();

    /** Normalize result for order-independent comparison. */
    private List<List<String>> normalize(List<List<String>> groups) {
        List<List<String>> copy = new ArrayList<>();
        for (List<String> g : groups) {
            List<String> sorted = new ArrayList<>(g);
            Collections.sort(sorted);
            copy.add(sorted);
        }
        copy.sort((a, b) -> a.get(0).compareTo(b.get(0)));
        return copy;
    }

    // LeetCode example 1
    @Test
    void example1() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = sol.groupAnagrams(input);
        assertEquals(3, result.size());

        // Verify that "eat","tea","ate" end up in the same group
        List<List<String>> norm = normalize(result);
        // After normalization: [ate,eat,tea], [bat], [ant,nat]
        List<String> group0 = norm.get(0);
        List<String> group1 = norm.get(1);
        List<String> group2 = norm.get(2);

        assertTrue(group0.containsAll(List.of("ate", "eat", "tea")));
        assertEquals(List.of("bat"), group1);
        assertTrue(group2.containsAll(List.of("nat", "tan")));
    }

    // LeetCode example 2: single empty string
    @Test
    void example2_emptyString() {
        List<List<String>> result = sol.groupAnagrams(new String[]{""});
        assertEquals(1, result.size());
        assertEquals(List.of(""), result.get(0));
    }

    // LeetCode example 3: single non-empty string
    @Test
    void example3_singleChar() {
        List<List<String>> result = sol.groupAnagrams(new String[]{"a"});
        assertEquals(1, result.size());
        assertEquals(List.of("a"), result.get(0));
    }

    // All words are anagrams of each other
    @Test
    void allSameGroup() {
        List<List<String>> result = sol.groupAnagrams(new String[]{"abc", "bca", "cab"});
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }

    // No two words are anagrams
    @Test
    void noAnagrams() {
        List<List<String>> result = sol.groupAnagrams(new String[]{"a", "b", "c"});
        assertEquals(3, result.size());
    }

    // Words with duplicate letters
    @Test
    void duplicateLetterWords() {
        List<List<String>> result = sol.groupAnagrams(new String[]{"aab", "baa", "aba", "xyz"});
        assertEquals(2, result.size());
    }
}
