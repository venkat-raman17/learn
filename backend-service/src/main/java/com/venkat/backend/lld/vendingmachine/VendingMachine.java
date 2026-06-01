package com.venkat.backend.lld.vendingmachine;

import java.util.Map;

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

/**
 * Coins accepted by the vending machine.
 * Each constant carries its value in whole cents.
 */
enum Coin {
    PENNY(1), NICKEL(5), DIME(10), QUARTER(25);

    private final int cents;

    Coin(int cents) {
        this.cents = cents;
    }

    /** Returns the monetary value of this coin in whole cents. */
    public int getCents() {
        return cents;
    }
}

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

/**
 * Represents a single product stocked in the vending machine.
 * <p>All prices are expressed in whole cents.</p>
 */
class Product {

    private final String code;
    private final String name;
    private final int priceCents;
    private int stock;

    /**
     * @param code       unique slot code (e.g. "A1")
     * @param name       human-readable product name
     * @param priceCents price in whole cents (must be &gt; 0)
     * @param stock      initial stock count (must be &gt;= 0)
     */
    public Product(String code, String name, int priceCents, int stock) {
        this.code = code;
        this.name = name;
        this.priceCents = priceCents;
        this.stock = stock;
    }

    /** Returns the slot code for this product. */
    public String getCode() { return code; }

    /** Returns the display name of this product. */
    public String getName() { return name; }

    /** Returns the price of this product in whole cents. */
    public int getPriceCents() { return priceCents; }

    /** Returns the current stock level. */
    public int getStock() { return stock; }

    /** Sets the stock level (used internally by the machine after dispensing). */
    public void setStock(int stock) { this.stock = stock; }
}

/**
 * Immutable result returned by {@link VendingMachine#dispense()}.
 */
class DispenseResult {

    private final String productName;
    private final int changeCents;

    /**
     * @param productName name of the dispensed product
     * @param changeCents change returned to the user in whole cents
     */
    public DispenseResult(String productName, int changeCents) {
        this.productName = productName;
        this.changeCents = changeCents;
    }

    /** Returns the name of the dispensed product. */
    public String getProductName() { return productName; }

    /** Returns the change given back to the user in whole cents. */
    public int getChangeCents() { return changeCents; }

    @Override
    public String toString() {
        return "DispenseResult{product='" + productName + "', change=" + changeCents + "c}";
    }
}

// ---------------------------------------------------------------------------
// State interface
// ---------------------------------------------------------------------------

/**
 * Strategy/State interface that every concrete vending machine state must implement.
 * <p>
 * Each method corresponds to a user action. Implementations should either carry out
 * the action and transition the machine context to the next state, or throw
 * {@link IllegalStateException} with a descriptive message when the action is not
 * permitted in the current state.
 * </p>
 *
 * <p><strong>Learner task:</strong> create concrete implementations:
 * {@code NoMoneyState}, {@code HasMoneyState}, {@code DispensingState},
 * {@code SoldOutState} (each in its own file or as package-private classes).</p>
 */
interface VendingMachineState {

    /**
     * Handles a coin insertion.
     *
     * @param coin the coin inserted by the user
     * @throws IllegalStateException if the current state does not permit coin insertion
     */
    void insertCoin(Coin coin);

    /**
     * Handles a product selection by slot code.
     *
     * @param code the slot code chosen by the user (e.g. "A1")
     * @throws IllegalStateException    if the current state does not permit selection
     * @throws IllegalArgumentException if the code does not exist in inventory
     */
    void selectProduct(String code);

    /**
     * Triggers dispensing of the previously selected product.
     *
     * @return a {@link DispenseResult} describing what was dispensed and any change
     * @throws IllegalStateException if the current state does not permit dispensing
     */
    DispenseResult dispense();

    /**
     * Refunds the currently accumulated balance.
     *
     * @return the amount refunded in whole cents
     * @throws IllegalStateException if the current state does not permit a refund
     */
    int refund();
}

// ---------------------------------------------------------------------------
// Context — public entry point
// ---------------------------------------------------------------------------

/**
 * <strong>Context</strong> class for the Vending Machine State pattern.
 *
 * <p>This class forms the public API surface. It holds:
 * <ul>
 *   <li>the current {@link VendingMachineState} (delegate for all operations),</li>
 *   <li>the product inventory ({@code Map<String, Product>}),</li>
 *   <li>the running balance in whole cents,</li>
 *   <li>the currently selected product (may be {@code null}).</li>
 * </ul>
 * </p>
 *
 * <p><strong>Learner task:</strong> implement the constructor, all public methods,
 * and all internal state helper methods. The state objects you create will call back
 * into the package-private helpers below (e.g. {@code addBalance}, {@code setState})
 * to drive transitions.</p>
 *
 * <p>Do <em>not</em> put transition logic directly in this class — keep it thin.</p>
 */
public class VendingMachine {

    // -----------------------------------------------------------------------
    // Internal fields — define and initialise these in your implementation.
    // -----------------------------------------------------------------------

    /** Current state of the machine. Initialise to NoMoneyState in the constructor. */
    private VendingMachineState currentState;

    /** Inventory: slot code -> Product. */
    private final Map<String, Product> inventory = new java.util.HashMap<>();

    /** Accumulated balance in whole cents. */
    private int balanceCents;

    /** The product selected by the user (null when none selected). */
    private Product selectedProduct;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Creates a new, empty vending machine in the {@code NoMoneyState}.
     * <p><strong>TODO:</strong> initialise {@code currentState} to {@code new NoMoneyState(this)}.</p>
     */
    public VendingMachine() {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Public API — delegate to currentState
    // -----------------------------------------------------------------------

    /**
     * Inserts a coin, accumulating its value in the balance.
     * Delegates to {@link VendingMachineState#insertCoin(Coin)}.
     *
     * @param coin the coin to insert; must not be {@code null}
     */
    public void insertCoin(Coin coin) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Selects a product by its slot code.
     * Delegates to {@link VendingMachineState#selectProduct(String)}.
     *
     * @param code slot code such as "A1"; must not be {@code null}
     */
    public void selectProduct(String code) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Dispenses the selected product.
     * Delegates to {@link VendingMachineState#dispense()}.
     *
     * @return {@link DispenseResult} with the product name and any change
     */
    public DispenseResult dispense() {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Cancels the current transaction and returns the balance.
     * Delegates to {@link VendingMachineState#refund()}.
     *
     * @return the refunded amount in whole cents
     */
    public int refund() {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Inventory management
    // -----------------------------------------------------------------------

    /**
     * Loads a product into the machine's inventory.
     * If a product with the same code already exists, it is replaced.
     *
     * @param product the product to add; must not be {@code null}
     */
    public void loadProduct(Product product) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns a read-only snapshot of the current inventory.
     *
     * @return unmodifiable map of code to {@link Product}
     */
    public Map<String, Product> getInventory() {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Observability helpers (useful for tests and state objects)
    // -----------------------------------------------------------------------

    /**
     * Returns the current balance in whole cents.
     *
     * @return balance &gt;= 0
     */
    public int getBalanceCents() {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns the currently selected product, or {@code null} if none is selected.
     *
     * @return selected {@link Product} or {@code null}
     */
    public Product getSelectedProduct() {
        throw new UnsupportedOperationException("implement me");
    }

    // -----------------------------------------------------------------------
    // Package-private helpers called by state objects
    // -----------------------------------------------------------------------

    /**
     * Transitions to the given state. Called by state objects.
     *
     * @param state the next state; must not be {@code null}
     */
    void setState(VendingMachineState state) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Adds the given amount to the balance. Called by state objects on coin insertion.
     *
     * @param cents amount to add (must be &gt; 0)
     */
    void addBalance(int cents) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Resets the balance to zero. Called by state objects after dispense or refund.
     */
    void resetBalance() {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Sets the currently selected product. Pass {@code null} to clear the selection.
     *
     * @param product selected product or {@code null}
     */
    void setSelectedProduct(Product product) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns the product for the given code, or {@code null} if not found.
     *
     * @param code slot code to look up
     * @return matching {@link Product} or {@code null}
     */
    Product findProduct(String code) {
        throw new UnsupportedOperationException("implement me");
    }
}
