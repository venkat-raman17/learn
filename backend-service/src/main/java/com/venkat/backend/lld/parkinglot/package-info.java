/**
 * <h2>LLD Practice Problem: Parking Lot System</h2>
 *
 * <h3>Problem Statement</h3>
 * Design and implement a multi-level parking lot management system. The lot contains
 * several floors (levels), each with a fixed number of spots. Spots come in three sizes
 * (SMALL, MEDIUM, LARGE) that must be matched to the vehicle type requesting a spot.
 * When a vehicle parks, the system issues a {@code Ticket}. When the vehicle leaves,
 * the system accepts the ticket, calculates and returns the fee using a pluggable
 * {@code PricingStrategy}, and marks the spot as free for the next vehicle.
 *
 * <hr>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li><b>park(Vehicle) -&gt; Ticket</b>: Find the nearest available spot that fits the
 *       vehicle type, assign it, and return a ticket containing spot location and entry
 *       timestamp. Throw {@code ParkingLotFullException} if no compatible spot is
 *       available.</li>
 *   <li><b>unpark(Ticket) -&gt; Receipt</b>: Release the assigned spot, compute the fee
 *       via the configured {@code PricingStrategy}, and return a {@code Receipt} with
 *       duration and amount. Throw {@code InvalidTicketException} for unknown or already
 *       redeemed tickets.</li>
 *   <li><b>Spot matching rules</b>:
 *     <ul>
 *       <li>BIKE  -&gt; SMALL spot</li>
 *       <li>CAR   -&gt; MEDIUM spot</li>
 *       <li>TRUCK -&gt; LARGE spot</li>
 *     </ul>
 *   </li>
 *   <li><b>Nearest spot</b>: Prefer spots on the lowest level first, then lowest spot
 *       number within a level.</li>
 *   <li><b>Display</b>: {@code getAvailability()} returns the number of free spots per
 *       vehicle type across the whole lot.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ul>
 *   <li><b>Thread safety</b>: {@code park()} must be atomic — two threads racing for the
 *       last compatible spot must not both receive a ticket for it. Use
 *       {@code synchronized}, {@code ReentrantLock}, or {@code AtomicReference} as you
 *       see fit; justify your choice in a comment.</li>
 *   <li><b>Extensibility</b>: Adding a new vehicle type (e.g. BUS) should require only
 *       (a) a new enum constant and (b) a new spot-size rule — no changes to
 *       {@code ParkingLot}'s core loop.</li>
 *   <li><b>Testability</b>: Inject dependencies (clock, pricing strategy, lot
 *       configuration) rather than hard-coding them.</li>
 * </ul>
 *
 * <hr>
 *
 * <h3>Suggested Entities</h3>
 * <pre>
 * VehicleType     (enum)      BIKE, CAR, TRUCK
 * SpotSize        (enum)      SMALL, MEDIUM, LARGE
 * Vehicle         (value)     licensePlate, VehicleType
 * ParkingSpot     (entity)    level, spotNumber, SpotSize, available flag
 * Ticket          (value)     ticketId, Vehicle, ParkingSpot, entryTime (Instant)
 * Receipt         (value)     Ticket, exitTime, durationMinutes, fee (BigDecimal)
 * PricingStrategy (interface) BigDecimal calculate(long durationMinutes)
 * HourlyPricing   (impl)      rate per vehicle type; partial hour rounds up to full hour
 * ParkingFloor    (entity)    level number, list of ParkingSpot
 * ParkingLot      (facade)    owns floors, strategy, issues/redeems tickets
 * </pre>
 *
 * <h3>Public API (entry points the tests drive)</h3>
 * <pre>{@code
 * ParkingLot lot = ParkingLot.create(config, pricingStrategy);
 * Ticket  t = lot.park(vehicle);           // assigns nearest free spot
 * Receipt r = lot.unpark(t);               // releases spot, returns fee
 * Map<VehicleType, Integer> a = lot.getAvailability();
 * }</pre>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><b>Strategy</b> — {@code PricingStrategy} makes fee calculation swappable
 *       without changing {@code ParkingLot}. Consider passing it via constructor.</li>
 *   <li><b>Factory</b> — A {@code ParkingSpotFactory} (or builder) can create spots of
 *       the right size given a configuration map, keeping construction logic separate
 *       from business logic.</li>
 *   <li><b>Singleton</b> — A real-world lot control system might be a singleton. Think
 *       about whether Singleton belongs here and what its trade-offs are for testing.</li>
 * </ul>
 *
 * <hr>
 *
 * <h3>Follow-up Questions</h3>
 * <ol>
 *   <li>How would you support <em>reserved spots</em> (monthly pass holders) without
 *       changing the {@code ParkingLot} facade?</li>
 *   <li>If the lot had 10 000 floors × 1 000 spots, how would you make
 *       {@code park()} sub-millisecond? (Hint: think priority queues per size.)</li>
 *   <li>How would you persist state so the lot survives a JVM restart without losing
 *       active tickets?</li>
 * </ol>
 *
 * <h3>Extensibility / Concurrency Prompt</h3>
 * <p>Simulate a race condition in a test: spin up 50 threads, each calling
 * {@code park(new Vehicle("BIKE-"+i, VehicleType.BIKE))}, on a lot that has only 30
 * SMALL spots. Assert exactly 30 threads received a {@code Ticket} and exactly 20
 * received a {@code ParkingLotFullException}. What locking strategy did you choose and
 * why?</p>
 *
 * @author  practice
 * @version 1.0
 */
package com.venkat.backend.lld.parkinglot;
