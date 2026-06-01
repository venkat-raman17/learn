package com.venkat.backend.lld.elevator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ElevatorSystemTest {

    /**
     * Simplest possible strategy: always pick elevator 0.
     * Replace with your own NearestIdleStrategy once implemented.
     */
    private static final SchedulingStrategy ALWAYS_ZERO =
            (states, floor, direction) -> 0;

    /**
     * Scenario: single elevator, 5 floors.
     * An external UP request from floor 3 should cause the elevator to travel
     * from floor 1 to floor 3, arriving with doors open after enough ticks.
     */
    @Test
    void elevatorTravelsToExternalRequestFloor() {
        ElevatorSystem system = new ElevatorSystem(5, 1, ALWAYS_ZERO);

        // Elevator 0 starts at floor 1, IDLE, CLOSED
        List<ElevatorState> initial = system.getStates();
        assertEquals(1, initial.get(0).currentFloor());
        assertEquals(ElevatorDirection.IDLE, initial.get(0).direction());
        assertEquals(DoorState.CLOSED, initial.get(0).doorState());

        // Issue an external UP request from floor 3
        system.externalRequest(3, Direction.UP);

        // Step until the elevator reaches floor 3 (at most 5 ticks)
        ElevatorState arrived = null;
        for (int tick = 0; tick < 5; tick++) {
            system.step();
            ElevatorState s = system.getStates().get(0);
            if (s.currentFloor() == 3 && s.doorState() == DoorState.OPEN) {
                arrived = s;
                break;
            }
        }

        // The elevator must have arrived at floor 3 with doors open
        assertEquals(3, arrived.currentFloor(),
                "Elevator should have reached floor 3");
        assertEquals(DoorState.OPEN, arrived.doorState(),
                "Doors should be open on arrival");
    }

    /**
     * Scenario: two elevators, internal request.
     * An internal request to floor 5 from elevator 1 (starting on floor 1)
     * should move elevator 1 upward, leaving elevator 0 unaffected.
     */
    @Test
    void internalRequestMovesCorrectElevator() {
        ElevatorSystem system = new ElevatorSystem(10, 2, ALWAYS_ZERO);

        // Internal request: elevator 1 should go to floor 5
        system.internalRequest(1, 5);

        // After one step elevator 1 should have moved UP (floor 2) and elevator 0 stays
        system.step();
        List<ElevatorState> states = system.getStates();

        assertEquals(1, states.get(0).currentFloor(),
                "Elevator 0 must not move — it received no request");
        assertNotEquals(1, states.get(1).currentFloor(),
                "Elevator 1 must have started moving toward floor 5");
        assertEquals(ElevatorDirection.UP, states.get(1).direction(),
                "Elevator 1 should be travelling UP");
    }

    /**
     * Scenario: after servicing a request the elevator returns to IDLE.
     * Single elevator, 3 floors, request floor 2 from floor 1.
     * After arriving and doors cycling, the elevator should be IDLE again.
     */
    @Test
    void elevatorBecomesIdleAfterServicingRequest() {
        ElevatorSystem system = new ElevatorSystem(3, 1, ALWAYS_ZERO);

        system.externalRequest(2, Direction.UP);

        // Run enough ticks: 1 tick to start moving, 1 to arrive + open, 1 to close
        for (int tick = 0; tick < 5; tick++) {
            system.step();
        }

        ElevatorState state = system.getStates().get(0);
        assertEquals(2, state.currentFloor(),
                "Elevator should be resting at floor 2");
        assertEquals(ElevatorDirection.IDLE, state.direction(),
                "Elevator should be IDLE after serving the request");
        assertEquals(DoorState.CLOSED, state.doorState(),
                "Doors should be closed after the stop cycle completes");
    }
}
