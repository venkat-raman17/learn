package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReconstructItineraryTest {

    private final ReconstructItinerary sol = new ReconstructItinerary();

    @Test
    void testExample1() {
        // LeetCode example 1
        List<List<String>> tickets = Arrays.asList(
                Arrays.asList("MUC", "LHR"),
                Arrays.asList("JFK", "MUC"),
                Arrays.asList("SFO", "SJC"),
                Arrays.asList("LHR", "SFO")
        );
        List<String> expected = Arrays.asList("JFK", "MUC", "LHR", "SFO", "SJC");
        assertEquals(expected, sol.findItinerary(tickets));
    }

    @Test
    void testExample2() {
        // LeetCode example 2: two routes from JFK, must pick lexicographically smallest
        List<List<String>> tickets = Arrays.asList(
                Arrays.asList("JFK", "SFO"),
                Arrays.asList("JFK", "ATL"),
                Arrays.asList("SFO", "ATL"),
                Arrays.asList("ATL", "JFK"),
                Arrays.asList("ATL", "SFO")
        );
        List<String> expected = Arrays.asList("JFK", "ATL", "JFK", "SFO", "ATL", "SFO");
        assertEquals(expected, sol.findItinerary(tickets));
    }

    @Test
    void testSingleTicket() {
        List<List<String>> tickets = Arrays.asList(
                Arrays.asList("JFK", "LAX")
        );
        List<String> expected = Arrays.asList("JFK", "LAX");
        assertEquals(expected, sol.findItinerary(tickets));
    }

    @Test
    void testDeadEndHandled() {
        // JFK->KUL is a dead end; must go JFK->NRT->JFK->KUL
        List<List<String>> tickets = Arrays.asList(
                Arrays.asList("JFK", "KUL"),
                Arrays.asList("JFK", "NRT"),
                Arrays.asList("NRT", "JFK")
        );
        List<String> expected = Arrays.asList("JFK", "NRT", "JFK", "KUL");
        assertEquals(expected, sol.findItinerary(tickets));
    }

    @Test
    void testLexicographicOrder() {
        // From JFK: can go to AAA or BBB; AAA must come first
        List<List<String>> tickets = Arrays.asList(
                Arrays.asList("JFK", "BBB"),
                Arrays.asList("JFK", "AAA"),
                Arrays.asList("AAA", "BBB"),
                Arrays.asList("BBB", "AAA")
        );
        // DFS from JFK: picks AAA first -> AAA->BBB->AAA... let's trace:
        // sorted neighbors of JFK: [AAA, BBB]
        // Visit AAA: neighbors [BBB] -> visit BBB: neighbors [AAA] -> visit AAA: neighbors []
        //   post-order add AAA, back to BBB: post-order add BBB, back to JFK-AAA branch: add AAA
        // back to JFK: remaining neighbor BBB: visit BBB... but BBB was already consumed
        // Actually neighbors of BBB is [AAA], AAA has no more neighbors after being consumed once
        // Let's re-trace carefully:
        // JFK neighbors (sorted): [AAA, BBB]
        // dfs(JFK): poll AAA -> dfs(AAA)
        //   dfs(AAA): poll BBB -> dfs(BBB)
        //     dfs(BBB): poll AAA -> dfs(AAA)
        //       dfs(AAA): empty neighbors -> addFirst(AAA)  result=[AAA]
        //     BBB neighbors now empty -> addFirst(BBB)  result=[BBB,AAA]
        //   AAA neighbors now empty -> addFirst(AAA)  result=[AAA,BBB,AAA]
        // JFK: poll BBB -> dfs(BBB): empty -> addFirst(BBB)  result=[BBB,AAA,BBB,AAA]
        // JFK neighbors empty -> addFirst(JFK)  result=[JFK,BBB,AAA,BBB,AAA]
        List<String> expected = Arrays.asList("JFK", "BBB", "AAA", "BBB", "AAA");
        assertEquals(expected, sol.findItinerary(tickets));
    }
}
