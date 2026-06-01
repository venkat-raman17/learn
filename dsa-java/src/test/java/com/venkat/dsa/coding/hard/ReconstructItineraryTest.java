package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ReconstructItineraryTest {

    private final ReconstructItinerary solution = new ReconstructItinerary();

    @Test
    public void testExample1() {
        List<List<String>> tickets = Arrays.asList(
            Arrays.asList("MUC", "LHR"),
            Arrays.asList("JFK", "MUC"),
            Arrays.asList("SFO", "SJC"),
            Arrays.asList("LHR", "SFO")
        );
        List<String> expected = Arrays.asList("JFK", "MUC", "LHR", "SFO", "SJC");
        assertEquals(expected, solution.findItinerary(tickets));
    }

    @Test
    public void testExample2() {
        List<List<String>> tickets = Arrays.asList(
            Arrays.asList("JFK", "SFO"),
            Arrays.asList("JFK", "ATL"),
            Arrays.asList("SFO", "ATL"),
            Arrays.asList("ATL", "JFK"),
            Arrays.asList("ATL", "SFO")
        );
        List<String> expected = Arrays.asList("JFK", "ATL", "JFK", "SFO", "ATL", "SFO");
        assertEquals(expected, solution.findItinerary(tickets));
    }

    @Test
    public void testSingleTicket() {
        List<List<String>> tickets = Arrays.asList(
            Arrays.asList("JFK", "ATL")
        );
        List<String> expected = Arrays.asList("JFK", "ATL");
        assertEquals(expected, solution.findItinerary(tickets));
    }
}
