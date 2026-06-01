package com.venkat.backend.lld.parkinglot;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Definition-of-done tests for the Parking Lot LLD problem.
 *
 * <p>Remove the {@code @Disabled} annotation only after you have implemented
 * the full solution. All three tests must pass without modification.
 *
 * <p>Do NOT modify these tests. Add your own test class if you want extra coverage.
 */
@Disabled("LLD practice — implement, then remove @Disabled")
class ParkingLotTest {

    // ---------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------

    /** A flat hourly rate: bikes=1, cars=2, trucks=4 per hour (partial hour = full). */
    private static PricingStrategy flatRate() {
        return (vehicleType, durationMinutes) -> {
            long hours = (durationMinutes + 59) / 60; // ceiling division
            int ratePerHour = switch (vehicleType) {
                case BIKE  -> 1;
                case CAR   -> 2;
                case TRUCK -> 4;
            };
            return BigDecimal.valueOf(hours * ratePerHour);
        };
    }

    /** Builds a simple 2-level lot: each level has 5 SMALL, 5 MEDIUM, 2 LARGE spots. */
    private static LotConfig twoLevelConfig() {
        FloorConfig floor0 = new FloorConfig(0,
                Map.of(SpotSize.SMALL, 5, SpotSize.MEDIUM, 5, SpotSize.LARGE, 2));
        FloorConfig floor1 = new FloorConfig(1,
                Map.of(SpotSize.SMALL, 5, SpotSize.MEDIUM, 5, SpotSize.LARGE, 2));
        return new LotConfig(List.of(floor0, floor1));
    }

    // ---------------------------------------------------------------------------
    // Test 1 — happy path: park, availability update, unpark with correct fee
    // ---------------------------------------------------------------------------

    @Test
    void parkAndUnpark_singleVehicle_issuesTicketAndCorrectFee() {
        ParkingLot lot = ParkingLot.create(twoLevelConfig(), flatRate());

        // Confirm initial availability includes all sizes
        Map<VehicleType, Integer> before = lot.getAvailability();
        assertTrue(before.get(VehicleType.CAR) > 0, "Should have free CAR spots initially");

        Vehicle car = new Vehicle("KA-01-AB-1234", VehicleType.CAR);
        Ticket ticket = lot.park(car);

        assertNotNull(ticket, "park() must return a non-null Ticket");
        assertNotNull(ticket.ticketId(), "Ticket must have a non-null ID");
        assertEquals(car, ticket.vehicle(), "Ticket must reference the parked vehicle");
        assertEquals(SpotSize.MEDIUM, ticket.spotSize(),
                "CAR must be assigned a MEDIUM spot");

        // Availability must decrease by 1
        Map<VehicleType, Integer> after = lot.getAvailability();
        assertEquals(before.get(VehicleType.CAR) - 1, after.get(VehicleType.CAR),
                "One fewer CAR spot should be available after parking");

        // Simulate passage of ~90 minutes (fee = ceil(90/60) * 2 = 4)
        Receipt receipt = lot.unpark(ticket);
        assertNotNull(receipt, "unpark() must return a non-null Receipt");
        assertNotNull(receipt.exitTime(), "Receipt must contain an exit time");
        assertTrue(receipt.durationMinutes() >= 0, "Duration must be non-negative");
        assertNotNull(receipt.fee(), "Fee must not be null");
        assertTrue(receipt.fee().compareTo(BigDecimal.ZERO) >= 0,
                "Fee must be non-negative");

        // Spot must be freed — availability back to original
        Map<VehicleType, Integer> restored = lot.getAvailability();
        assertEquals(before.get(VehicleType.CAR), restored.get(VehicleType.CAR),
                "CAR spot count must be restored after unparking");
    }

    // ---------------------------------------------------------------------------
    // Test 2 — full lot raises ParkingLotFullException; mixed vehicle types
    // ---------------------------------------------------------------------------

    @Test
    void park_fullLot_throwsParkingLotFullException() {
        // Tiny lot: 1 SMALL, 1 MEDIUM, 1 LARGE on a single floor
        LotConfig tinyConfig = new LotConfig(List.of(
                new FloorConfig(0,
                        Map.of(SpotSize.SMALL, 1, SpotSize.MEDIUM, 1, SpotSize.LARGE, 1))));
        ParkingLot lot = ParkingLot.create(tinyConfig, flatRate());

        Ticket bikeTicket  = lot.park(new Vehicle("BIKE-1", VehicleType.BIKE));
        Ticket carTicket   = lot.park(new Vehicle("CAR-1",  VehicleType.CAR));
        Ticket truckTicket = lot.park(new Vehicle("TRUCK-1", VehicleType.TRUCK));

        assertNotNull(bikeTicket);
        assertNotNull(carTicket);
        assertNotNull(truckTicket);

        // All spots occupied — next park must throw
        assertThrows(ParkingLotFullException.class,
                () -> lot.park(new Vehicle("CAR-2", VehicleType.CAR)),
                "Parking a CAR when lot is full must throw ParkingLotFullException");

        // Unpark one car; now a CAR should fit again
        lot.unpark(carTicket);
        assertDoesNotThrow(
                () -> lot.park(new Vehicle("CAR-3", VehicleType.CAR)),
                "Parking a CAR after a spot is freed must succeed");
    }

    // ---------------------------------------------------------------------------
    // Test 3 — concurrent park: exactly N successes for N spots
    // ---------------------------------------------------------------------------

    @Test
    void park_concurrentRequests_noDoubleAssignment() throws InterruptedException {
        // 10 SMALL spots, 10 threads asking for BIKE
        int spotsAvailable = 10;
        int threadCount    = 20;

        LotConfig config = new LotConfig(List.of(
                new FloorConfig(0, Map.of(
                        SpotSize.SMALL,  spotsAvailable,
                        SpotSize.MEDIUM, 0,
                        SpotSize.LARGE,  0))));
        ParkingLot lot = ParkingLot.create(config, flatRate());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger fullCount    = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int idx = i;
            futures.add(pool.submit(() -> {
                try {
                    lot.park(new Vehicle("BIKE-" + idx, VehicleType.BIKE));
                    successCount.incrementAndGet();
                } catch (ParkingLotFullException e) {
                    fullCount.incrementAndGet();
                }
            }));
        }

        for (Future<?> f : futures) {
            try { f.get(); } catch (Exception ignored) {}
        }
        pool.shutdown();

        assertEquals(spotsAvailable, successCount.get(),
                "Exactly " + spotsAvailable + " parks must succeed (no double-assignment)");
        assertEquals(threadCount - spotsAvailable, fullCount.get(),
                "Remaining threads must receive ParkingLotFullException");
    }
}
