package com.venkat.backend.lld.parkinglot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

/** The type of vehicle requesting a parking spot. */
enum VehicleType {
    BIKE, CAR, TRUCK
}

/** The physical size of a parking spot. */
enum SpotSize {
    SMALL, MEDIUM, LARGE
}

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

/**
 * An immutable description of a vehicle.
 *
 * @param licensePlate unique plate string (non-null, non-blank)
 * @param type         the vehicle category
 */
record Vehicle(String licensePlate, VehicleType type) {
    Vehicle {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("licensePlate must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
    }
}

/**
 * Issued when a vehicle successfully parks. Carry this to {@link ParkingLot#unpark}.
 *
 * <p>Learner task: decide which fields this needs to record in order for
 * {@code unpark} to (a) locate the spot to free and (b) compute duration.
 */
record Ticket(
        String ticketId,
        Vehicle vehicle,
        int level,
        int spotNumber,
        SpotSize spotSize,
        Instant entryTime
) {
    Ticket {
        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException("ticketId must not be blank");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle must not be null");
        }
        if (entryTime == null) {
            throw new IllegalArgumentException("entryTime must not be null");
        }
    }
}

/**
 * Returned when a vehicle exits. Summarises the parking session.
 *
 * @param ticket          the original entry ticket
 * @param exitTime        when the vehicle left
 * @param durationMinutes actual minutes parked (ceiling — partial minutes count as full)
 * @param fee             total charge in the lot's currency
 */
record Receipt(
        Ticket ticket,
        Instant exitTime,
        long durationMinutes,
        BigDecimal fee
) {}

// ---------------------------------------------------------------------------
// Exceptions
// ---------------------------------------------------------------------------

/** Thrown by {@link ParkingLot#park} when no compatible spot is available. */
class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException(String message) {
        super(message);
    }
}

/** Thrown by {@link ParkingLot#unpark} for an unknown or already-redeemed ticket. */
class InvalidTicketException extends RuntimeException {
    public InvalidTicketException(String message) {
        super(message);
    }
}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

/**
 * Pluggable fee calculation strategy.
 *
 * <p>Implement this to provide different pricing models (hourly flat rate,
 * surge pricing, free first 15 minutes, etc.).
 *
 * <p>Learner note: consider parameterising by {@link VehicleType} so that
 * trucks pay more than bikes.
 */
interface PricingStrategy {

    /**
     * Calculates the parking fee.
     *
     * @param vehicleType     the type of the parked vehicle
     * @param durationMinutes actual duration in minutes (already ceiling-rounded)
     * @return non-negative fee; must not be {@code null}
     */
    BigDecimal calculate(VehicleType vehicleType, long durationMinutes);
}

// ---------------------------------------------------------------------------
// Configuration DTO
// ---------------------------------------------------------------------------

/**
 * Describes the physical layout of the lot passed to {@link ParkingLot#create}.
 *
 * <p>Example — a 2-level lot:
 * <pre>{@code
 * var floorConfigs = List.of(
 *     new FloorConfig(0, Map.of(SpotSize.SMALL, 10, SpotSize.MEDIUM, 20, SpotSize.LARGE, 5)),
 *     new FloorConfig(1, Map.of(SpotSize.SMALL, 10, SpotSize.MEDIUM, 20, SpotSize.LARGE, 5))
 * );
 * var config = new LotConfig(floorConfigs);
 * }</pre>
 *
 * @param floors ordered list of floor configurations (index 0 = ground level)
 */
record LotConfig(java.util.List<FloorConfig> floors) {
    LotConfig {
        if (floors == null || floors.isEmpty()) {
            throw new IllegalArgumentException("At least one floor is required");
        }
    }
}

/**
 * Configuration for a single floor.
 *
 * @param level      zero-based floor number
 * @param spotCounts map from {@link SpotSize} to the number of spots of that size on
 *                   this floor
 */
record FloorConfig(int level, Map<SpotSize, Integer> spotCounts) {
    FloorConfig {
        if (spotCounts == null || spotCounts.isEmpty()) {
            throw new IllegalArgumentException("spotCounts must not be empty");
        }
    }
}

// ---------------------------------------------------------------------------
// Main facade — STUB ONLY, learner implements the body
// ---------------------------------------------------------------------------

/**
 * Entry-point facade for the parking lot system.
 *
 * <p><b>Learner tasks</b> (do not change the public signatures):
 * <ol>
 *   <li>Implement {@link #park(Vehicle)} — find the nearest free compatible spot,
 *       mark it occupied, and return a {@link Ticket}. Handle the full-lot case.</li>
 *   <li>Implement {@link #unpark(Ticket)} — validate the ticket, free the spot,
 *       compute and return the {@link Receipt}. Handle invalid/redeemed tickets.</li>
 *   <li>Implement {@link #getAvailability()} — aggregate free spots by vehicle type.</li>
 *   <li>Design the internal data structures (floors, spots, active-ticket registry).</li>
 *   <li>Ensure {@code park()} is thread-safe: two threads racing for the last spot
 *       must not both succeed.</li>
 * </ol>
 *
 * <p>You may add private fields, private methods, and inner/helper classes freely.
 * Do NOT add new public methods unless they are part of the spec.
 */
public class ParkingLot {

    // TODO: add private fields (floors, active tickets, strategy, clock, lock …)

    /**
     * Private constructor — use {@link #create(LotConfig, PricingStrategy)} to
     * obtain an instance.
     */
    private ParkingLot(LotConfig config, PricingStrategy strategy) {
        // TODO: initialise internal data structures from config; store strategy
        throw new UnsupportedOperationException("implement me");
    }

    // ---------------------------------------------------------------------------
    // Factory method
    // ---------------------------------------------------------------------------

    /**
     * Creates a new {@code ParkingLot} with the given layout and pricing strategy.
     *
     * @param config   physical layout of the lot; must not be {@code null}
     * @param strategy fee calculation strategy; must not be {@code null}
     * @return a fully initialised, ready-to-use {@code ParkingLot}
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public static ParkingLot create(LotConfig config, PricingStrategy strategy) {
        // TODO: validate arguments, delegate to constructor
        throw new UnsupportedOperationException("implement me");
    }

    // ---------------------------------------------------------------------------
    // Core operations
    // ---------------------------------------------------------------------------

    /**
     * Parks a vehicle in the nearest available compatible spot.
     *
     * <p>Nearest is defined as: lowest level first, then lowest spot number within
     * the level. The spot size must match the vehicle type
     * (BIKE→SMALL, CAR→MEDIUM, TRUCK→LARGE).
     *
     * <p>This method must be <b>thread-safe</b>: concurrent calls must not assign
     * the same spot to two different vehicles.
     *
     * @param vehicle the vehicle to park; must not be {@code null}
     * @return a {@link Ticket} capturing the assigned spot and entry time
     * @throws ParkingLotFullException  if no compatible spot is currently free
     * @throws IllegalArgumentException if {@code vehicle} is {@code null}
     */
    public Ticket park(Vehicle vehicle) {
        // TODO: implement
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Releases the spot associated with the given ticket and calculates the fee.
     *
     * @param ticket the ticket issued at entry; must not be {@code null}
     * @return a {@link Receipt} with duration and fee
     * @throws InvalidTicketException   if the ticket is unknown or already redeemed
     * @throws IllegalArgumentException if {@code ticket} is {@code null}
     */
    public Receipt unpark(Ticket ticket) {
        // TODO: implement
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns the current count of free spots for each vehicle type.
     *
     * <p>The map must contain an entry for every {@link VehicleType} constant,
     * even if the count is zero.
     *
     * @return unmodifiable snapshot; never {@code null}
     */
    public Map<VehicleType, Integer> getAvailability() {
        // TODO: implement
        throw new UnsupportedOperationException("implement me");
    }
}
