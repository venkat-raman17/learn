package com.venkat.backend.lld.parkinglot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

enum VehicleType { BIKE, CAR, TRUCK }

enum SpotSize { SMALL, MEDIUM, LARGE }

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

record Vehicle(String licensePlate, VehicleType type) {
    Vehicle {
        if (licensePlate == null || licensePlate.isBlank())
            throw new IllegalArgumentException("licensePlate must not be blank");
        if (type == null) throw new IllegalArgumentException("type must not be null");
    }
}

record Ticket(String ticketId, Vehicle vehicle, int level, int spotNumber, SpotSize spotSize, Instant entryTime) {
    Ticket {
        if (ticketId == null || ticketId.isBlank())
            throw new IllegalArgumentException("ticketId must not be blank");
        if (vehicle == null)   throw new IllegalArgumentException("vehicle must not be null");
        if (entryTime == null) throw new IllegalArgumentException("entryTime must not be null");
    }
}

record Receipt(Ticket ticket, Instant exitTime, long durationMinutes, BigDecimal fee) {}

// ---------------------------------------------------------------------------
// Exceptions
// ---------------------------------------------------------------------------

class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException(String message) { super(message); }
}

class InvalidTicketException extends RuntimeException {
    public InvalidTicketException(String message) { super(message); }
}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

interface PricingStrategy {
    BigDecimal calculate(VehicleType vehicleType, long durationMinutes);
}

// ---------------------------------------------------------------------------
// Configuration DTO
// ---------------------------------------------------------------------------

record LotConfig(java.util.List<FloorConfig> floors) {
    LotConfig {
        if (floors == null || floors.isEmpty())
            throw new IllegalArgumentException("At least one floor is required");
    }
}

record FloorConfig(int level, Map<SpotSize, Integer> spotCounts) {
    FloorConfig {
        if (spotCounts == null || spotCounts.isEmpty())
            throw new IllegalArgumentException("spotCounts must not be empty");
    }
}

// ---------------------------------------------------------------------------
// Internal helper classes
// ---------------------------------------------------------------------------

class ParkingSpot {
    private final int level;
    private final int number;
    private final SpotSize size;
    private boolean occupied = false;

    ParkingSpot(int level, int number, SpotSize size) {
        this.level  = level;
        this.number = number;
        this.size   = size;
    }

    int getLevel()    { return level; }
    int getNumber()   { return number; }
    SpotSize getSize() { return size; }
    boolean isOccupied() { return occupied; }
    void setOccupied(boolean occupied) { this.occupied = occupied; }
}

// ---------------------------------------------------------------------------
// Main facade
// ---------------------------------------------------------------------------

public class ParkingLot {

    // Vehicle type → compatible spot size
    private static final Map<VehicleType, SpotSize> TYPE_TO_SIZE = Map.of(
        VehicleType.BIKE,  SpotSize.SMALL,
        VehicleType.CAR,   SpotSize.MEDIUM,
        VehicleType.TRUCK, SpotSize.LARGE
    );

    private final List<List<ParkingSpot>> floors; // floors.get(level) → list of spots (ordered by number)
    private final PricingStrategy strategy;
    private final Map<String, Ticket> activeTickets = new HashMap<>(); // ticketId → Ticket
    private final Map<String, ParkingSpot> ticketToSpot = new HashMap<>(); // ticketId → spot
    private final ReentrantLock lock = new ReentrantLock();

    private ParkingLot(LotConfig config, PricingStrategy strategy) {
        this.strategy = strategy;
        this.floors   = new ArrayList<>();

        for (FloorConfig fc : config.floors()) {
            List<ParkingSpot> spotsOnFloor = new ArrayList<>();
            int spotNum = 1;
            // Enumerate sizes in a deterministic order: SMALL, MEDIUM, LARGE
            for (SpotSize size : SpotSize.values()) {
                int count = fc.spotCounts().getOrDefault(size, 0);
                for (int i = 0; i < count; i++) {
                    spotsOnFloor.add(new ParkingSpot(fc.level(), spotNum++, size));
                }
            }
            floors.add(spotsOnFloor);
        }
    }

    public static ParkingLot create(LotConfig config, PricingStrategy strategy) {
        Objects.requireNonNull(config,   "config must not be null");
        Objects.requireNonNull(strategy, "strategy must not be null");
        return new ParkingLot(config, strategy);
    }

    // -------------------------------------------------------------------------
    // Core operations
    // -------------------------------------------------------------------------

    public Ticket park(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "vehicle must not be null");
        SpotSize required = TYPE_TO_SIZE.get(vehicle.type());

        lock.lock();
        try {
            // Find the nearest free spot: lowest level first, then lowest spot number
            for (List<ParkingSpot> spotsOnFloor : floors) {
                for (ParkingSpot spot : spotsOnFloor) {
                    if (!spot.isOccupied() && spot.getSize() == required) {
                        spot.setOccupied(true);
                        Ticket ticket = new Ticket(
                            UUID.randomUUID().toString(),
                            vehicle,
                            spot.getLevel(),
                            spot.getNumber(),
                            spot.getSize(),
                            Instant.now()
                        );
                        activeTickets.put(ticket.ticketId(), ticket);
                        ticketToSpot.put(ticket.ticketId(), spot);
                        return ticket;
                    }
                }
            }
            throw new ParkingLotFullException(
                "No available " + required + " spot for " + vehicle.type());
        } finally {
            lock.unlock();
        }
    }

    public Receipt unpark(Ticket ticket) {
        Objects.requireNonNull(ticket, "ticket must not be null");

        lock.lock();
        try {
            Ticket stored = activeTickets.remove(ticket.ticketId());
            if (stored == null) {
                throw new InvalidTicketException(
                    "Unknown or already redeemed ticket: " + ticket.ticketId());
            }
            ParkingSpot spot = ticketToSpot.remove(ticket.ticketId());
            spot.setOccupied(false);

            Instant exitTime = Instant.now();
            long elapsedSeconds = exitTime.getEpochSecond() - stored.entryTime().getEpochSecond();
            long durationMinutes = (long) Math.ceil(elapsedSeconds / 60.0);
            BigDecimal fee = strategy.calculate(stored.vehicle().type(), durationMinutes);

            return new Receipt(stored, exitTime, durationMinutes, fee);
        } finally {
            lock.unlock();
        }
    }

    public Map<VehicleType, Integer> getAvailability() {
        lock.lock();
        try {
            Map<VehicleType, Integer> result = new EnumMap<>(VehicleType.class);
            for (VehicleType vt : VehicleType.values()) result.put(vt, 0);

            SpotSize small  = SpotSize.SMALL;
            SpotSize medium = SpotSize.MEDIUM;
            SpotSize large  = SpotSize.LARGE;

            for (List<ParkingSpot> floor : floors) {
                for (ParkingSpot spot : floor) {
                    if (!spot.isOccupied()) {
                        VehicleType vt = switch (spot.getSize()) {
                            case SMALL  -> VehicleType.BIKE;
                            case MEDIUM -> VehicleType.CAR;
                            case LARGE  -> VehicleType.TRUCK;
                        };
                        result.merge(vt, 1, Integer::sum);
                    }
                }
            }
            return Collections.unmodifiableMap(result);
        } finally {
            lock.unlock();
        }
    }
}
