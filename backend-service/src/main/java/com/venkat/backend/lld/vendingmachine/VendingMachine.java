package com.venkat.backend.lld.vendingmachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

enum Coin {
    PENNY(1), NICKEL(5), DIME(10), QUARTER(25);

    private final int cents;
    Coin(int cents) { this.cents = cents; }
    public int getCents() { return cents; }
}

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

class Product {
    private final String code;
    private final String name;
    private final int priceCents;
    private int stock;

    public Product(String code, String name, int priceCents, int stock) {
        this.code       = code;
        this.name       = name;
        this.priceCents = priceCents;
        this.stock      = stock;
    }

    public String getCode()       { return code; }
    public String getName()       { return name; }
    public int getPriceCents()    { return priceCents; }
    public int getStock()         { return stock; }
    public void setStock(int s)   { this.stock = s; }
}

class DispenseResult {
    private final String productName;
    private final int changeCents;

    public DispenseResult(String productName, int changeCents) {
        this.productName  = productName;
        this.changeCents  = changeCents;
    }

    public String getProductName() { return productName; }
    public int getChangeCents()    { return changeCents; }

    @Override
    public String toString() {
        return "DispenseResult{product='" + productName + "', change=" + changeCents + "c}";
    }
}

// ---------------------------------------------------------------------------
// State interface
// ---------------------------------------------------------------------------

interface VendingMachineState {
    void insertCoin(Coin coin);
    void selectProduct(String code);
    DispenseResult dispense();
    int refund();
}

// ---------------------------------------------------------------------------
// Concrete states
// ---------------------------------------------------------------------------

class NoMoneyState implements VendingMachineState {
    private final VendingMachine ctx;
    NoMoneyState(VendingMachine ctx) { this.ctx = ctx; }

    @Override
    public void insertCoin(Coin coin) {
        ctx.addBalance(coin.getCents());
        ctx.setState(new HasMoneyState(ctx));
    }

    @Override public void selectProduct(String code) {
        throw new IllegalStateException("Insert coins first.");
    }
    @Override public DispenseResult dispense() {
        throw new IllegalStateException("Select a product first.");
    }
    @Override public int refund() {
        throw new IllegalStateException("No money to refund.");
    }
}

class HasMoneyState implements VendingMachineState {
    private final VendingMachine ctx;
    HasMoneyState(VendingMachine ctx) { this.ctx = ctx; }

    @Override
    public void insertCoin(Coin coin) {
        ctx.addBalance(coin.getCents());
        // remain in HasMoneyState
    }

    @Override
    public void selectProduct(String code) {
        Product product = ctx.findProduct(code);
        if (product == null) throw new IllegalArgumentException("Unknown product code: " + code);
        if (product.getStock() <= 0) throw new IllegalStateException("Product out of stock.");
        if (ctx.getBalanceCents() < product.getPriceCents())
            throw new IllegalStateException("Insufficient funds. Need " + product.getPriceCents() + "c, have " + ctx.getBalanceCents() + "c.");
        ctx.setSelectedProduct(product);
        ctx.setState(new DispensingState(ctx));
    }

    @Override public DispenseResult dispense() {
        throw new IllegalStateException("Select a product first.");
    }

    @Override
    public int refund() {
        int refunded = ctx.getBalanceCents();
        ctx.resetBalance();
        ctx.setState(new NoMoneyState(ctx));
        return refunded;
    }
}

class DispensingState implements VendingMachineState {
    private final VendingMachine ctx;
    DispensingState(VendingMachine ctx) { this.ctx = ctx; }

    @Override public void insertCoin(Coin coin) {
        throw new IllegalStateException("Dispensing in progress.");
    }
    @Override public void selectProduct(String code) {
        throw new IllegalStateException("Dispensing in progress.");
    }

    @Override
    public DispenseResult dispense() {
        Product product = ctx.getSelectedProduct();
        int change = ctx.getBalanceCents() - product.getPriceCents();
        product.setStock(product.getStock() - 1);
        ctx.resetBalance();
        ctx.setSelectedProduct(null);
        ctx.setState(new NoMoneyState(ctx));
        return new DispenseResult(product.getName(), change);
    }

    @Override
    public int refund() {
        // Cancel mid-dispense: full refund, no product dispensed
        int refunded = ctx.getBalanceCents();
        ctx.resetBalance();
        ctx.setSelectedProduct(null);
        ctx.setState(new NoMoneyState(ctx));
        return refunded;
    }
}

// ---------------------------------------------------------------------------
// Context
// ---------------------------------------------------------------------------

public class VendingMachine {

    private VendingMachineState currentState;
    private final Map<String, Product> inventory = new HashMap<>();
    private int balanceCents;
    private Product selectedProduct;

    public VendingMachine() {
        this.currentState = new NoMoneyState(this);
    }

    // ---- Public API ----

    public void insertCoin(Coin coin) {
        Objects.requireNonNull(coin, "coin must not be null");
        currentState.insertCoin(coin);
    }

    public void selectProduct(String code) {
        Objects.requireNonNull(code, "code must not be null");
        currentState.selectProduct(code);
    }

    public DispenseResult dispense() {
        return currentState.dispense();
    }

    public int refund() {
        return currentState.refund();
    }

    // ---- Inventory ----

    public void loadProduct(Product product) {
        Objects.requireNonNull(product, "product must not be null");
        inventory.put(product.getCode(), product);
    }

    public Map<String, Product> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    // ---- Observability ----

    public int getBalanceCents()      { return balanceCents; }
    public Product getSelectedProduct() { return selectedProduct; }

    // ---- Package-private helpers used by state objects ----

    void setState(VendingMachineState state) { this.currentState = state; }
    void addBalance(int cents)               { this.balanceCents += cents; }
    void resetBalance()                      { this.balanceCents = 0; }
    void setSelectedProduct(Product p)       { this.selectedProduct = p; }
    Product findProduct(String code)         { return inventory.get(code); }
}
