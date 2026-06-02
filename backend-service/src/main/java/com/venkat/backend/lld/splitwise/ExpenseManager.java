package com.venkat.backend.lld.splitwise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class ExpenseManager {

    // -----------------------------------------------------------------------
    // Internal state
    // -----------------------------------------------------------------------

    private final Map<String, String> users = new HashMap<>();  // userId → name
    // balances[from][to] = amount from owes to  (positive = debt)
    private final Map<String, Map<String, Double>> balances = new HashMap<>();

    // -----------------------------------------------------------------------
    // Construction
    // -----------------------------------------------------------------------

    private ExpenseManager() {}

    public static ExpenseManager create() {
        return new ExpenseManager();
    }

    // -----------------------------------------------------------------------
    // User management
    // -----------------------------------------------------------------------

    public void addUser(String userId, String name) {
        Objects.requireNonNull(userId, "userId must not be null");
        if (users.containsKey(userId))
            throw new IllegalArgumentException("User already registered: " + userId);
        users.put(userId, name);
        balances.put(userId, new HashMap<>());
    }

    // -----------------------------------------------------------------------
    // Group management
    // -----------------------------------------------------------------------

    public void createGroup(String groupId, String groupName, List<String> memberIds) {
        // Groups are informational — validation only, balances are user-level
        Objects.requireNonNull(memberIds, "memberIds must not be null");
        for (String uid : memberIds) requireRegistered(uid);
    }

    // -----------------------------------------------------------------------
    // Expense recording
    // -----------------------------------------------------------------------

    public void addExpense(String payerId,
                           double amount,
                           List<String> participantIds,
                           SplitType splitType,
                           Map<String, Double> splitDetails) {
        requireRegistered(payerId);
        if (participantIds == null || participantIds.isEmpty())
            throw new IllegalArgumentException("participantIds must not be empty");
        for (String uid : participantIds) requireRegistered(uid);
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");

        SplitStrategy strategy = strategyFor(splitType);
        Map<String, Double> shares = strategy.compute(amount, participantIds,
            splitDetails != null ? splitDetails : Collections.emptyMap());

        // Update balance graph: for each participant, payer is owed their share
        for (Map.Entry<String, Double> e : shares.entrySet()) {
            String participant = e.getKey();
            double share = e.getValue();
            if (participant.equals(payerId)) continue; // payer doesn't owe themselves

            // participant owes payerId `share`
            addBalance(participant, payerId, share);
        }
    }

    // -----------------------------------------------------------------------
    // Balance queries
    // -----------------------------------------------------------------------

    public List<BalanceEntry> showBalances(String userId) {
        requireRegistered(userId);
        List<BalanceEntry> result = new ArrayList<>();

        // What userId owes others
        Map<String, Double> owes = balances.getOrDefault(userId, Collections.emptyMap());
        for (Map.Entry<String, Double> e : owes.entrySet()) {
            if (e.getValue() > 1e-9) result.add(new BalanceEntry(userId, e.getKey(), e.getValue()));
        }
        // What others owe userId
        for (Map.Entry<String, Map<String, Double>> entry : balances.entrySet()) {
            String other = entry.getKey();
            if (other.equals(userId)) continue;
            double owed = entry.getValue().getOrDefault(userId, 0.0);
            if (owed > 1e-9) result.add(new BalanceEntry(other, userId, owed));
        }
        return Collections.unmodifiableList(result);
    }

    public List<BalanceEntry> showAllBalances() {
        List<BalanceEntry> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> outer : balances.entrySet()) {
            String from = outer.getKey();
            for (Map.Entry<String, Double> inner : outer.getValue().entrySet()) {
                if (inner.getValue() > 1e-9) {
                    result.add(new BalanceEntry(from, inner.getKey(), inner.getValue()));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    // -----------------------------------------------------------------------
    // Settlement
    // -----------------------------------------------------------------------

    public void settle(String userId1, String userId2) {
        requireRegistered(userId1);
        requireRegistered(userId2);
        // Zero out balances in both directions
        setBalance(userId1, userId2, 0.0);
        setBalance(userId2, userId1, 0.0);
    }

    // -----------------------------------------------------------------------
    // Value types and enumerations
    // -----------------------------------------------------------------------

    public enum SplitType { EQUAL, EXACT, PERCENT }

    public static final class BalanceEntry {
        private final String fromUserId;
        private final String toUserId;
        private final double amount;

        public BalanceEntry(String fromUserId, String toUserId, double amount) {
            this.fromUserId = fromUserId;
            this.toUserId   = toUserId;
            this.amount     = amount;
        }

        public String getFromUserId() { return fromUserId; }
        public String getToUserId()   { return toUserId; }
        public double getAmount()     { return amount; }

        @Override
        public String toString() { return fromUserId + " owes " + toUserId + ": " + amount; }
    }

    public interface SplitStrategy {
        Map<String, Double> compute(double amount, List<String> participants, Map<String, Double> details);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private void requireRegistered(String userId) {
        if (!users.containsKey(userId))
            throw new IllegalArgumentException("Unknown user: " + userId);
    }

    private void addBalance(String from, String to, double amount) {
        // First simplify: if 'to' already owes 'from', net them out
        double counterDebt = balances.getOrDefault(to, Collections.emptyMap()).getOrDefault(from, 0.0);
        if (counterDebt > 0) {
            double net = amount - counterDebt;
            if (net > 1e-9) {
                setBalance(to, from, 0.0);
                setBalance(from, to, net);
            } else if (net < -1e-9) {
                setBalance(to, from, -net);
                setBalance(from, to, 0.0);
            } else {
                setBalance(to, from, 0.0);
                setBalance(from, to, 0.0);
            }
        } else {
            double existing = balances.getOrDefault(from, Collections.emptyMap()).getOrDefault(to, 0.0);
            setBalance(from, to, existing + amount);
        }
    }

    private void setBalance(String from, String to, double amount) {
        balances.computeIfAbsent(from, k -> new HashMap<>()).put(to, amount);
    }

    private SplitStrategy strategyFor(SplitType type) {
        return switch (type) {
            case EQUAL   -> (amount, participants, details) -> {
                Map<String, Double> shares = new HashMap<>();
                double share = amount / participants.size();
                participants.forEach(p -> shares.put(p, share));
                return shares;
            };
            case EXACT   -> (amount, participants, details) -> {
                double sum = details.values().stream().mapToDouble(Double::doubleValue).sum();
                if (Math.abs(sum - amount) > 1e-6)
                    throw new IllegalArgumentException("EXACT splits must sum to " + amount + ", got " + sum);
                return new HashMap<>(details);
            };
            case PERCENT -> (amount, participants, details) -> {
                double pctSum = details.values().stream().mapToDouble(Double::doubleValue).sum();
                if (Math.abs(pctSum - 100.0) > 1e-6)
                    throw new IllegalArgumentException("PERCENT splits must sum to 100, got " + pctSum);
                Map<String, Double> shares = new HashMap<>();
                details.forEach((p, pct) -> shares.put(p, amount * pct / 100.0));
                return shares;
            };
        };
    }
}
