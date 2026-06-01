/**
 * <h2>LLD Practice Problem: Vending Machine</h2>
 *
 * <h3>Problem Statement</h3>
 * <p>
 * Design and implement a Vending Machine system that accepts coins, allows a user to select
 * a product by code, dispenses the product when sufficient funds have been inserted, and
 * supports refunding any remaining balance. The machine maintains an inventory of products
 * with individual stock counts and transitions through well-defined internal states using
 * the <strong>State</strong> design pattern.
 * </p>
 *
 * <hr/>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li><strong>Insert Coin</strong> — The machine must accept one or more {@code Coin} values
 *       (PENNY, NICKEL, DIME, QUARTER) and accumulate a running balance.</li>
 *   <li><strong>Select Product</strong> — The user selects a product by its alphanumeric code
 *       (e.g. "A1", "B3"). The machine validates the code, checks stock, and verifies that
 *       the inserted balance is sufficient.</li>
 *   <li><strong>Dispense</strong> — Triggers the physical dispensing of the selected product,
 *       deducts its price from the balance, decrements stock, and returns any change owed.</li>
 *   <li><strong>Refund</strong> — Returns the current accumulated balance to the user at any
 *       time and resets the machine to the idle (no-money) state.</li>
 *   <li><strong>Inventory Management</strong> — Products can be loaded into the machine with
 *       a code, name, price (in cents), and initial stock count. The machine transitions to
 *       {@code SoldOutState} when the selected product is out of stock.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ul>
 *   <li>State transitions must be encapsulated in state objects — no large {@code if/else}
 *       or {@code switch} chains in the machine class itself.</li>
 *   <li>The machine must be deterministic: given the same sequence of operations, the
 *       outcome must always be identical.</li>
 *   <li>Illegal operations in a given state (e.g., calling {@code dispense()} before
 *       selecting a product) must throw a meaningful, documented exception rather than
 *       silently failing.</li>
 *   <li>All monetary values are expressed in <em>whole cents</em> (int) to avoid
 *       floating-point issues.</li>
 * </ul>
 *
 * <hr/>
 *
 * <h3>Suggested Entities / Classes</h3>
 * <ul>
 *   <li>{@code Coin} — enum with cent values (PENNY=1, NICKEL=5, DIME=10, QUARTER=25).</li>
 *   <li>{@code Product} — value type: code, name, price in cents, mutable stock count.</li>
 *   <li>{@code DispenseResult} — DTO returned by {@code dispense()}: dispensed product name
 *       and change returned in cents.</li>
 *   <li>{@code VendingMachineState} — interface (or abstract class) declaring
 *       {@code insertCoin}, {@code selectProduct}, {@code dispense}, {@code refund}.</li>
 *   <li>{@code NoMoneyState} — idle; accepts coins, rejects other ops.</li>
 *   <li>{@code HasMoneyState} — balance present; accepts more coins, allows product
 *       selection, allows refund.</li>
 *   <li>{@code DispensingState} — product selected and funds confirmed; dispense only.</li>
 *   <li>{@code SoldOutState} — selected product has zero stock; reject dispense,
 *       allow refund.</li>
 *   <li>{@code VendingMachine} — context class holding current state and inventory map.</li>
 * </ul>
 *
 * <h3>Public API (see {@link com.venkat.backend.lld.vendingmachine.VendingMachine})</h3>
 * <pre>{@code
 * VendingMachine vm = new VendingMachine();
 * vm.loadProduct(new Product("A1", "Cola", 75, 10));
 * vm.insertCoin(Coin.QUARTER);
 * vm.insertCoin(Coin.QUARTER);
 * vm.insertCoin(Coin.QUARTER);
 * vm.selectProduct("A1");
 * DispenseResult result = vm.dispense();   // dispensed="Cola", change=0
 * }</pre>
 *
 * <hr/>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><strong>State</strong> (primary) — each state implements {@code VendingMachineState};
 *       the machine delegates all operations to the current state object and each state
 *       is responsible for triggering transitions via a back-reference to the context.</li>
 *   <li><strong>Strategy</strong> — change-making logic could be extracted as a separate
 *       strategy if multiple algorithms (greedy, exact) are needed.</li>
 *   <li><strong>Factory Method</strong> — a static factory on {@code VendingMachine} for
 *       creating pre-loaded machines in tests.</li>
 * </ul>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>How would you extend the design to support <em>note/bill</em> acceptance in addition
 *       to coins without modifying existing state classes?</li>
 *   <li>If two threads simultaneously call {@code insertCoin()} and {@code dispense()},
 *       which critical sections must be synchronized and at what granularity?</li>
 *   <li>How would you persist and restore the vending machine's inventory and balance
 *       across restarts (e.g., via JSON serialization)?</li>
 * </ol>
 *
 * <h3>Extensibility / Concurrency Prompt</h3>
 * <p>
 * Extend your implementation to be thread-safe for a scenario where multiple users can
 * insert coins from different terminals concurrently (imagine a networked machine). Consider
 * using {@code synchronized} methods, {@code ReentrantLock}, or Java's {@code AtomicInteger}
 * for the balance. Ensure that a product can never be double-dispensed even under a race
 * condition. As a bonus, add a maintenance mode that locks the machine for restocking.
 * </p>
 *
 * @author  learner
 * @version 1.0
 * @since   Java 21
 */
package com.venkat.backend.lld.vendingmachine;
