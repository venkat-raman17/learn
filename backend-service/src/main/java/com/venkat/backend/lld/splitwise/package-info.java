/**
 * <h2>LLD Practice — Splitwise</h2>
 *
 * <h3>Problem Statement</h3>
 * Design and implement a simplified expense-splitting system similar to Splitwise.
 * Users can be added to the system, form groups, and record shared expenses.
 * The system tracks who owes whom and by how much, and allows users to settle
 * their debts. The goal is a clean, extensible object-oriented design with a
 * correct balance sheet at all times.
 *
 * <hr>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li><b>User management</b> — addUser(userId, name): register a user in the system.</li>
 *   <li><b>Group management</b> — createGroup(groupId, name, memberIds): form a named group
 *       from existing users.</li>
 *   <li><b>Add expense</b> —
 *       {@code addExpense(payerId, amount, participantIds, SplitType, splitDetails)}
 *       records an expense paid by {@code payerId} on behalf of {@code participantIds}.
 *       <ul>
 *         <li>{@code EQUAL} — split the total equally; {@code splitDetails} is ignored.</li>
 *         <li>{@code EXACT} — each participant owes the exact rupee amount in
 *             {@code splitDetails} (values must sum to {@code amount}).</li>
 *         <li>{@code PERCENT} — each participant owes the given percentage of
 *             {@code amount} in {@code splitDetails} (percentages must sum to 100).</li>
 *       </ul>
 *   </li>
 *   <li><b>Balance sheet</b> — showBalances(userId): return a list of balance entries
 *       showing what the user owes others and what others owe the user.</li>
 *   <li><b>Settle</b> — settle(userId1, userId2): record that userId2 has paid back
 *       userId1 in full; clear the balance between the pair.</li>
 *   <li><b>Global balances</b> — showAllBalances(): return every non-zero balance
 *       in the system.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ul>
 *   <li>All monetary values are stored as {@code double} (or {@code BigDecimal} for bonus).</li>
 *   <li>Balance queries must run in O(U) time where U = number of users.</li>
 *   <li>The design must be easily extensible to new split strategies without modifying
 *       existing classes (Open/Closed Principle).</li>
 *   <li>Thread-safety is not required in the baseline; it is a follow-up concern.</li>
 *   <li>No persistence layer is required — in-memory storage only.</li>
 * </ul>
 *
 * <h3>Suggested Entities</h3>
 * <ul>
 *   <li>{@code User} — userId, name</li>
 *   <li>{@code Group} — groupId, name, Set&lt;User&gt; members</li>
 *   <li>{@code Expense} — expenseId, payer, amount, list of {@code Split} objects</li>
 *   <li>{@code Split} — user, share (computed by strategy)</li>
 *   <li>{@code SplitStrategy} — interface: {@code List<Split> compute(double amount,
 *       List<User> participants, Map<String,Double> details)}</li>
 *   <li>{@code EqualSplitStrategy}, {@code ExactSplitStrategy},
 *       {@code PercentSplitStrategy} — concrete strategies</li>
 *   <li>{@code BalanceSheet} — internal ledger: Map&lt;userId, Map&lt;userId, Double&gt;&gt;
 *       representing net amounts owed</li>
 * </ul>
 *
 * <h3>Public API (see {@link com.venkat.backend.lld.splitwise.ExpenseManager})</h3>
 * <pre>{@code
 * ExpenseManager mgr = ExpenseManager.create();
 *
 * mgr.addUser("u1", "Alice");
 * mgr.addUser("u2", "Bob");
 * mgr.addUser("u3", "Charlie");
 *
 * // Alice pays 300, split equally among all three
 * mgr.addExpense("u1", 300.0,
 *     List.of("u1","u2","u3"),
 *     SplitType.EQUAL,
 *     Map.of());
 *
 * // Alice pays 200, Bob owes 100, Charlie owes 100 (EXACT)
 * mgr.addExpense("u1", 200.0,
 *     List.of("u2","u3"),
 *     SplitType.EXACT,
 *     Map.of("u2", 100.0, "u3", 100.0));
 *
 * List<BalanceEntry> entries = mgr.showBalances("u2");
 * mgr.settle("u1", "u2");   // u2 settles with u1
 * }</pre>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><b>Strategy</b> — {@code SplitStrategy} interface with three concrete
 *       implementations, selected via {@code SplitType} enum. This is the primary
 *       pattern and the one you MUST implement.</li>
 *   <li><b>Factory / Factory Method</b> — {@code ExpenseManager.create()} and a
 *       {@code SplitStrategyFactory} that maps {@code SplitType} to the right strategy.</li>
 *   <li><b>Repository / Service separation</b> — separate the user/group data store
 *       from the expense service to keep single responsibility.</li>
 * </ul>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>How would you add a {@code SHARE}-based split (e.g., Alice gets 2 shares,
 *       Bob gets 1 share, so Alice owes 2/3)? What changes in your design?</li>
 *   <li>The balance sheet currently stores gross pairwise debts. How would you
 *       minimise the number of transactions to settle the whole group
 *       (minimum-cash-flow problem)?</li>
 *   <li>If concurrent threads call {@code addExpense} and {@code showBalances}
 *       simultaneously, what synchronisation strategy would you apply?
 *       Compare {@code synchronized}, {@code ReentrantReadWriteLock},
 *       and {@code ConcurrentHashMap} trade-offs.</li>
 * </ol>
 *
 * <h3>Extensibility and Concurrency Prompt</h3>
 * <p>Extend the system so that expenses can be associated with a group (not just
 * a free-form participant list). When a group expense is added, only group members
 * may be participants. Additionally, make {@code addExpense} and {@code showBalances}
 * safe for concurrent use: choose the appropriate locking granularity and justify
 * your choice in a comment.</p>
 */
package com.venkat.backend.lld.splitwise;
