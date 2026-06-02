package com.venkat.backend.lld.splitwise;

import com.venkat.backend.lld.splitwise.ExpenseManager.BalanceEntry;
import com.venkat.backend.lld.splitwise.ExpenseManager.SplitType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LLD practice tests for {@link ExpenseManager}.
 *
 * <p>These tests define the expected behaviour — they are your definition of done.
 * Remove the {@code @Disabled} annotation only after you have fully implemented
 * the class. Do NOT modify the test logic; only the implementation classes change.
 */
class ExpenseManagerTest {

    private ExpenseManager mgr;

    @BeforeEach
    void setUp() {
        mgr = ExpenseManager.create();
        mgr.addUser("u1", "Alice");
        mgr.addUser("u2", "Bob");
        mgr.addUser("u3", "Charlie");
    }

    /**
     * Scenario: Alice pays 300 for dinner shared equally by all three.
     * Bob and Charlie each owe Alice 100. Alice owes nothing.
     */
    @Test
    void equalSplit_balancesAreCorrect() {
        mgr.addExpense("u1", 300.0,
                List.of("u1", "u2", "u3"),
                SplitType.EQUAL,
                Map.of());

        List<BalanceEntry> aliceBalances = mgr.showBalances("u1");
        List<BalanceEntry> bobBalances   = mgr.showBalances("u2");

        // Alice is owed money — two entries (from Bob and Charlie)
        assertEquals(2, aliceBalances.size(),
                "Alice should have exactly two balance entries (Bob + Charlie owe her)");

        aliceBalances.forEach(e ->
                assertEquals("u1", e.getToUserId(), "All debts should be TO Alice"));

        aliceBalances.forEach(e ->
                assertEquals(100.0, e.getAmount(), 0.001,
                        "Each person owes Alice exactly 100"));

        // Bob owes Alice 100
        assertEquals(1, bobBalances.size());
        BalanceEntry bobEntry = bobBalances.get(0);
        assertEquals("u2", bobEntry.getFromUserId());
        assertEquals("u1", bobEntry.getToUserId());
        assertEquals(100.0, bobEntry.getAmount(), 0.001);
    }

    /**
     * Scenario: Alice pays 200 for a taxi (EXACT split).
     * Bob owes 120, Charlie owes 80.
     * After Bob settles with Alice, only Charlie's debt remains.
     */
    @Test
    void exactSplit_thenSettle_balanceCleared() {
        mgr.addExpense("u1", 200.0,
                List.of("u2", "u3"),
                SplitType.EXACT,
                Map.of("u2", 120.0, "u3", 80.0));

        // Verify exact debts before settlement
        List<BalanceEntry> before = mgr.showAllBalances();
        assertTrue(before.stream().anyMatch(
                e -> "u2".equals(e.getFromUserId()) && "u1".equals(e.getToUserId())
                        && Math.abs(e.getAmount() - 120.0) < 0.001),
                "Bob should owe Alice 120");
        assertTrue(before.stream().anyMatch(
                e -> "u3".equals(e.getFromUserId()) && "u1".equals(e.getToUserId())
                        && Math.abs(e.getAmount() - 80.0) < 0.001),
                "Charlie should owe Alice 80");

        // Bob settles with Alice
        mgr.settle("u1", "u2");

        List<BalanceEntry> after = mgr.showAllBalances();
        // Bob-Alice balance must be gone
        boolean bobAliceDebtExists = after.stream().anyMatch(
                e -> (("u2".equals(e.getFromUserId()) && "u1".equals(e.getToUserId()))
                        || ("u1".equals(e.getFromUserId()) && "u2".equals(e.getToUserId())))
                        && e.getAmount() > 0.001);
        assertFalse(bobAliceDebtExists, "Bob-Alice balance should be zero after settlement");

        // Charlie still owes Alice
        assertEquals(1, after.size(),
                "Only Charlie's outstanding debt should remain");
        assertEquals("u3", after.get(0).getFromUserId());
        assertEquals("u1", after.get(0).getToUserId());
        assertEquals(80.0, after.get(0).getAmount(), 0.001);
    }

    /**
     * Scenario: Bob pays 1000 for a trip (PERCENT split).
     * Alice gets 50%, Charlie gets 30%, Bob keeps 20% (i.e. pays his own share).
     * Alice owes Bob 500, Charlie owes Bob 300.
     */
    @Test
    void percentSplit_sharesConvertToCorrectAmounts() {
        mgr.addExpense("u2", 1000.0,
                List.of("u1", "u2", "u3"),
                SplitType.PERCENT,
                Map.of("u1", 50.0, "u2", 20.0, "u3", 30.0));

        List<BalanceEntry> bobBalances = mgr.showBalances("u2");

        // Bob is owed 500 by Alice and 300 by Charlie
        assertEquals(2, bobBalances.size(),
                "Bob should have 2 entries (Alice and Charlie owe him)");

        bobBalances.forEach(e ->
                assertEquals("u2", e.getToUserId(), "All debts should be TO Bob"));

        double totalOwed = bobBalances.stream().mapToDouble(BalanceEntry::getAmount).sum();
        assertEquals(800.0, totalOwed, 0.001,
                "Total owed to Bob should be 800 (50% + 30% of 1000)");

        boolean aliceOwesBob500 = bobBalances.stream().anyMatch(
                e -> "u1".equals(e.getFromUserId()) && Math.abs(e.getAmount() - 500.0) < 0.001);
        assertTrue(aliceOwesBob500, "Alice should owe Bob 500");

        boolean charlieOwesBob300 = bobBalances.stream().anyMatch(
                e -> "u3".equals(e.getFromUserId()) && Math.abs(e.getAmount() - 300.0) < 0.001);
        assertTrue(charlieOwesBob300, "Charlie should owe Bob 300");
    }
}
