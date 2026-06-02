package com.venkat.backend.lld.vendingmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Definition-of-done tests for the Vending Machine LLD practice problem.
 *
 * <p>Remove the {@code @Disabled} annotation only after you have fully implemented
 * all classes and confirmed that all tests pass.</p>
 *
 * <p>These tests exercise the public API only — no reflection, no internal casts.</p>
 */
class VendingMachineTest {

    private VendingMachine vm;

    @BeforeEach
    void setUp() {
        vm = new VendingMachine();
        // Load two products:
        //   A1 — Cola  @ 75 cents, stock 2
        //   B1 — Chips @ 50 cents, stock 1
        vm.loadProduct(new Product("A1", "Cola",  75, 2));
        vm.loadProduct(new Product("B1", "Chips", 50, 1));
    }

    /**
     * Happy-path scenario: insert exact change, select, dispense.
     * Verifies correct product name, zero change, and decremented stock.
     */
    @Test
    void exactChange_dispensesProductAndReturnsNoChange() {
        // 25 + 25 + 25 = 75 cents (exact price of Cola)
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);

        vm.selectProduct("A1");
        DispenseResult result = vm.dispense();

        assertNotNull(result, "dispense() must return a non-null DispenseResult");
        assertEquals("Cola", result.getProductName(),
                "Dispensed product name must match the selected product");
        assertEquals(0, result.getChangeCents(),
                "No change should be returned when exact coins were inserted");

        // Stock of A1 should now be 1
        assertEquals(1, vm.getInventory().get("A1").getStock(),
                "Stock of A1 must decrement from 2 to 1 after one dispense");
    }

    /**
     * Overpayment scenario: insert more than the product price, verify correct change.
     */
    @Test
    void overpayment_dispensesProductAndReturnsCorrectChange() {
        // Insert 4 quarters = 100 cents; Chips cost 50 cents -> change = 50 cents
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);

        vm.selectProduct("B1");
        DispenseResult result = vm.dispense();

        assertEquals("Chips", result.getProductName(),
                "Dispensed product name must be Chips");
        assertEquals(50, result.getChangeCents(),
                "Machine must return 50 cents change (100 inserted - 50 price)");
        assertEquals(0, vm.getBalanceCents(),
                "Balance must be zero after a successful dispense");
    }

    /**
     * Refund scenario and out-of-stock guard:
     * 1. Dispense the only Chips (stock -> 0).
     * 2. Insert coins and select Chips again; expect the machine to
     *    refuse dispensing and allow a refund.
     */
    @Test
    void soldOut_refundReturnsFundsAndPreventsDispense() {
        // First, buy the only Chips
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.selectProduct("B1");
        vm.dispense();   // stock of B1 is now 0

        // Reset machine by starting a new transaction
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);

        // Selecting a sold-out product should transition to SoldOutState
        // (selectProduct may throw or the subsequent dispense should throw)
        assertThrows(IllegalStateException.class, () -> {
            vm.selectProduct("B1");  // sold out
            vm.dispense();           // must not succeed
        }, "Attempting to dispense a sold-out product must throw IllegalStateException");

        // Refund must return whatever was inserted
        int refunded = vm.refund();
        assertEquals(50, refunded,
                "Refund must return the 50 cents inserted before the failed selection");
        assertEquals(0, vm.getBalanceCents(),
                "Balance must be zero after refund");
    }
}
