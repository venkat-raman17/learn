package com.venkat.backend.lld.elevator;

import java.util.List;

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

/**
 * The direction a person presses on a floor panel (external request).
 */
enum Direction {
    UP, DOWN
}

/**
 * The current movement direction of an elevator cabin.
 */
enum ElevatorDirection {
    UP, DOWN, IDLE
}

/**
 * The state of an elevator's door.
 */
enum DoorState {
    OPEN, CLOSED
}

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

/**
 * Immutable snapshot of a single elevator's observable state.
 *
 * @param id           zero-based elevator identifier
 * @param currentFloor the floor the elevator is currently on (1-based)
 * @param direction    current movement direction
 * @param doorState    current door state
 */
record ElevatorState(int id, int currentFloor, ElevatorDirection direction, DoorState doorState) {}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

/**
 * Pluggable scheduling strategy that selects which elevator should handle an
 * external floor request.
 *
 * <p>Implement this interface to experiment with different algorithms
 * (nearest-idle, SCAN/LOOK, round-robin, etc.).</p>
 */
interface SchedulingStrategy {

    /**
     * Given a read-only snapshot of all elevator states, pick the best elevator
     * to service a request at {@code floor} in {@code direction}.
     *
     * @param states    current state of every elevator (index == elevator id)
     * @param floor     the floor from which the external request originated (1-based)
     * @param direction the direction the waiting passenger intends to travel
     * @return the id of the chosen elevator (must be a valid index into {@code states})
     */
    int selectElevator(List<ElevatorState> states, int floor, Direction direction);
}

// ---------------------------------------------------------------------------
// Top-level facade
// ---------------------------------------------------------------------------

/**
 * Entry point for the Elevator System simulation.
 *
 * <p><strong>Practice goal:</strong> implement all methods whose bodies currently
 * throw {@link UnsupportedOperationException}. Define internal helper classes
 * (e.g. {@code Elevator}) in separate files inside this package — do not change
 * the public API signatures below.</p>
 *
 * <h4>Simulation contract</h4>
 * <ul>
 *   <li>One call to {@link #step()} represents one tick of simulated time.</li>
 *   <li>Each tick: idle elevators are dispatched, moving elevators advance one
 *       floor, elevators at a target floor open their doors (and close them the
 *       following tick so the floor is de-queued).</li>
 *   <li>Floors are 1-based ({@code 1..numFloors}).</li>
 *   <li>Elevator ids are 0-based ({@code 0..numElevators-1}).</li>
 * </ul>
 */
public class ElevatorSystem {

    /**
     * Constructs the system.
     *
     * @param numFloors     total number of floors in the building (>= 2)
     * @param numElevators  total number of elevator cabins (>= 1)
     * @param strategy      the scheduling strategy used to assign external requests
     * @throws IllegalArgumentException if numFloors < 2 or numElevators < 1
     */
    public ElevatorSystem(int numFloors, int numElevators, SchedulingStrategy strategy) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Records an external hall-call request: someone on {@code floor} wants to
     * travel in {@code direction}.
     *
     * <p>The system must use the {@link SchedulingStrategy} to assign an elevator
     * to this request (or queue it if no suitable elevator is free).</p>
     *
     * @param floor     the floor where the request originates (1-based)
     * @param direction the direction the passenger wishes to travel
     * @throws IllegalArgumentException if floor is out of range
     */
    public void externalRequest(int floor, Direction direction) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Records an internal cabin request: the passenger inside elevator
     * {@code elevatorId} pressed the button for {@code targetFloor}.
     *
     * @param elevatorId  the id of the elevator (0-based)
     * @param targetFloor the destination floor (1-based)
     * @throws IllegalArgumentException if elevatorId or targetFloor is out of range
     */
    public void internalRequest(int elevatorId, int targetFloor) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Advances the simulation by one tick.
     *
     * <p>Per-tick behaviour (suggested order):</p>
     * <ol>
     *   <li>For each elevator with an open door: close the door and remove the
     *       serviced floor from the queue.</li>
     *   <li>For each moving elevator: move one floor toward the next target.
     *       If it has arrived, open the door.</li>
     *   <li>For each idle elevator: if there are unassigned external requests,
     *       dispatch it via the strategy.</li>
     * </ol>
     */
    public void step() {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns an immutable snapshot of the current state of all elevators,
     * ordered by elevator id (ascending).
     *
     * @return unmodifiable list of {@link ElevatorState}, one per elevator
     */
    public List<ElevatorState> getStates() {
        throw new UnsupportedOperationException("implement me");
    }
}
