package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@Disabled("practice — delete when you start")
public class WordLadderTest {

    private final WordLadder solution = new WordLadder();

    @Test
    public void testExample1() {
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log","cog");
        assertEquals(5, solution.ladderLength("hit", "cog", wordList));
    }

    @Test
    public void testNoPath() {
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log");
        assertEquals(0, solution.ladderLength("hit", "cog", wordList));
    }

    @Test
    public void testDirectTransformation() {
        List<String> wordList = Arrays.asList("hot");
        assertEquals(2, solution.ladderLength("hit", "hot", wordList));
    }
}
