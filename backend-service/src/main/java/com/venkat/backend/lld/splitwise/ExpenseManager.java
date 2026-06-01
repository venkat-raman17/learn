package com.venkat.backend.lld.splitwise;

import java.util.List;
import java.util.Map;

/**
 * Public entry-point for the Splitwise LLD practice.
 *
 * <p>Obtain an instance via {@link #create()}. All identifiers (userId, groupId)
 * are plain {@code String} values chosen by the caller.
 *
 * <p><b>Do not modify the method signatures.</b> Implement the internals by
 * adding package-private or private helper classes / internal data structures
 * inside this package. Remove each {@code throw new UnsupportedOperationException}
 * as you implement the corresponding method.
 */
public class ExpenseManager {

    // -----------------------------------------------------------------------
    // Construction
    // -----------------------------------------------------------------------

    /** Private constructor — use {@link #create()} factory method. */
    private ExpenseManager() { }

    /**
     * Factory method — returns a fresh, empty {@code ExpenseManager}.
     *
     * @return new instance
     */
    public static ExpenseManager create() {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // User management
    // -----------------------------------------------------------------------

    /**
     * Register a new user.
     *
     * @param userId unique user identifier
     * @param name   display name
     * @throws IllegalArgumentException if userId is already registered
     */
    public void addUser(String userId, String name) {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Group management
    // -----------------------------------------------------------------------

    /**
     * Create a named group with an initial set of members.
     *
     * @param groupId   unique group identifier
     * @param groupName display name for the group
     * @param memberIds list of already-registered user IDs to include
     * @throws IllegalArgumentException if groupId already exists or any
     *                                  memberId is not a registered user
     */
    public void createGroup(String groupId, String groupName, List<String> memberIds) {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Expense recording
    // -----------------------------------------------------------------------

    /**
     * Record a new shared expense.
     *
     * <p>The payer has already paid the full {@code amount}. The system
     * computes each participant's share via the chosen {@link SplitType}
     * strategy and updates the internal balance sheet accordingly.
     *
     * <p><b>Contract per SplitType:</b>
     * <ul>
     *   <li>{@code EQUAL} — share = amount / participantIds.size();
     *       {@code splitDetails} is ignored.</li>
     *   <li>{@code EXACT} — {@code splitDetails} maps each participantId to
     *       their exact amount; values must sum to {@code amount}.</li>
     *   <li>{@code PERCENT} — {@code splitDetails} maps each participantId to
     *       their percentage (0–100); values must sum to 100.</li>
     * </ul>
     *
     * <p>If the payer is included in {@code participantIds}, their own share
     * is deducted from what others owe them (they do not owe themselves).
     *
     * @param payerId        userId of the person who paid
     * @param amount         total amount paid (must be &gt; 0)
     * @param participantIds list of userIds who share this expense
     * @param splitType      strategy to use for dividing the amount
     * @param splitDetails   per-user values (amounts or percentages);
     *                       pass an empty map for {@code EQUAL}
     * @throws IllegalArgumentException if validation fails (unknown user,
     *         amounts/percentages do not add up, empty participant list, etc.)
     */
    public void addExpense(String payerId,
                           double amount,
                           List<String> participantIds,
                           SplitType splitType,
                           Map<String, Double> splitDetails) {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Balance queries
    // -----------------------------------------------------------------------

    /**
     * Return all non-zero balance entries involving {@code userId}.
     *
     * <p>Each {@link BalanceEntry} in the returned list represents either:
     * <ul>
     *   <li>"{@code userId} owes {@code otherUserId} X" — a positive debt, or</li>
     *   <li>"{@code otherUserId} owes {@code userId} X" — money owed to the user.</li>
     * </ul>
     *
     * @param userId the user whose balances are requested
     * @return immutable list of balance entries; empty list if all settled
     * @throws IllegalArgumentException if userId is not registered
     */
    public List<BalanceEntry> showBalances(String userId) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Return every non-zero pairwise balance in the entire system.
     *
     * @return immutable list of all outstanding balance entries
     */
    public List<BalanceEntry> showAllBalances() {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Settlement
    // -----------------------------------------------------------------------

    /**
     * Settle the outstanding balance between two users in full.
     *
     * <p>After this call the net balance between {@code userId1} and
     * {@code userId2} must be zero in both directions.
     *
     * @param userId1 one party in the settlement
     * @param userId2 the other party
     * @throws IllegalArgumentException if either userId is not registered
     */
    public void settle(String userId1, String userId2) {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Value types and enumerations
    // -----------------------------------------------------------------------

    /**
     * Determines how an expense amount is divided among participants.
     */
    public enum SplitType {
        /** Divide total equally among all participants. */
        EQUAL,
        /** Each participant owes an exact rupee amount provided in splitDetails. */
        EXACT,
        /** Each participant owes a percentage of the total provided in splitDetails. */
        PERCENT
    }

    /**
     * Immutable snapshot of a single directional debt.
     *
     * <p>{@code fromUserId} owes {@code toUserId} the given {@code amount}.
     */
    public static final class BalanceEntry {

        private final String fromUserId;
        private final String toUserId;
        private final double amount;

        /**
         * Construct a balance entry.
         *
         * @param fromUserId the debtor
         * @param toUserId   the creditor
         * @param amount     amount owed (must be &gt; 0)
         */
        public BalanceEntry(String fromUserId, String toUserId, double amount) {
            this.fromUserId = fromUserId;
            this.toUserId   = toUserId;
            this.amount     = amount;
        }

        /** @return the user who owes money */
        public String getFromUserId() { return fromUserId; }

        /** @return the user who is owed money */
        public String getToUserId() { return toUserId; }

        /** @return the outstanding amount */
        public double getAmount() { return amount; }

        @Override
        public String toString() {
            return fromUserId + " owes " + toUserId + ": " + amount;
        }
    }

    /**
     * Strategy interface — compute how a given amount is divided.
     *
     * <p>Implement one concrete class per {@link SplitType}.
     * You are expected to create these classes as part of the exercise.
     */
    public interface SplitStrategy {

        /**
         * Compute per-participant shares.
         *
         * @param amount       total expense amount
         * @param participants ordered list of participant user IDs
         * @param details      auxiliary data (exact amounts or percentages);
         *                     may be empty for {@code EQUAL}
         * @return map of userId -&gt; share amount (values sum to {@code amount})
         * @throws IllegalArgumentException if {@code details} is invalid for
         *         this strategy
         */
        Map<String, Double> compute(double amount,
                                    List<String> participants,
                                    Map<String, Double> details);
    }
}
