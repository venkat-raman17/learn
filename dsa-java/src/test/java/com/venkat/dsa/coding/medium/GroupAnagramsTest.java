package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class GroupAnagramsTest {

    private final GroupAnagrams solution = new GroupAnagrams();

    @Test
    public void testStandardCase() {
        List<List<String>> result = solution.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"});
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testSingleEmpty() {
        List<List<String>> result = solution.groupAnagrams(new String[]{""});
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
    }

    @Test
    public void testSingleLetter() {
        List<List<String>> result = solution.groupAnagrams(new String[]{"a"});
        assertEquals(1, result.size());
        assertEquals(List.of("a"), result.get(0));
    }
}
