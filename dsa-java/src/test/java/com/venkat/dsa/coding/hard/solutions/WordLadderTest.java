package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class WordLadderTest {

    private final WordLadder sol = new WordLadder();

    @Test
    void example1_length5() {
        // beginWord="hit", endWord="cog", wordList=["hot","dot","dog","lot","log","cog"]
        // hit->hot->dot->dog->cog = 5
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log","cog");
        assertEquals(5, sol.ladderLength("hit", "cog", wordList));
    }

    @Test
    void example2_endWordNotInList() {
        // beginWord="hit", endWord="cog", wordList=["hot","dot","dog","lot","log"]
        // "cog" not in list -> 0
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log");
        assertEquals(0, sol.ladderLength("hit", "cog", wordList));
    }

    @Test
    void directOneStep() {
        // "a" -> "b" with wordList=["b"]: one transformation, sequence length = 2
        List<String> wordList = Arrays.asList("b");
        assertEquals(2, sol.ladderLength("a", "b", wordList));
    }

    @Test
    void noPathExists_unreachable() {
        // "abc" -> "xyz": none of the words bridge abc to xyz within 1-char changes
        // adc differs from abc in pos 1 (b->d), but none of adc/ddd/ccc lead to xyz
        List<String> wordList = Arrays.asList("adc","ddd","ccc");
        assertEquals(0, sol.ladderLength("abc", "xyz", wordList));
    }

    @Test
    void twoStepPath() {
        // "red"->"tax": red->ted->tad->tax... let's use simpler
        // "ab"->"cd": ab->cb->cd
        List<String> wordList = Arrays.asList("cb","cd");
        assertEquals(3, sol.ladderLength("ab", "cd", wordList));
    }

    @Test
    void shortestAmongMultiplePaths() {
        // Both "hot"->"dot"->"dog"->"cog" (4) and "hot"->"lot"->"log"->"cog" (4) exist
        // Both are length 4 from "hot" to "cog"; answer is 4
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log","cog");
        assertEquals(4, sol.ladderLength("hot", "cog", wordList));
    }

    @Test
    void endWordNotReachable_emptyWordList() {
        assertEquals(0, sol.ladderLength("hit", "cog", new ArrayList<>()));
    }
}
