package com.venkat.backend.lld.elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

enum Direction      { UP, DOWN }
enum ElevatorDirection { UP, DOWN, IDLE }
enum DoorState      { OPEN, CLOSED }

// ---------------------------------------------------------------------------
// Value / DTO types
// ---------------------------------------------------------------------------

record ElevatorState(int id, int currentFloor, ElevatorDirection direction, DoorState doorState) {}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

interface SchedulingStrategy {
    int selectElevator(List<ElevatorState> states, int floor, Direction direction);
}

// ---------------------------------------------------------------------------
// Built-in scheduling strategy: nearest idle or same-direction elevator
// ---------------------------------------------------------------------------

class NearestElevatorStrategy implements SchedulingStrategy {

    @Override
    public int selectElevator(List<ElevatorState> states, int floor, Direction direction) {
        int bestId   = -1;
        int bestDist = Integer.MAX_VALUE;

        for (ElevatorState s : states) {
            int dist = Math.abs(s.currentFloor() - floor);

            if (s.direction() == ElevatorDirection.IDLE) {
                if (dist < bestDist) { bestDist = dist; bestId = s.id(); }
            } else if (s.direction() == ElevatorDirection.UP && direction == Direction.UP && s.currentFloor() <= floor) {
                if (dist < bestDist) { bestDist = dist; bestId = s.id(); }
            } else if (s.direction() == ElevatorDirection.DOWN && direction == Direction.DOWN && s.currentFloor() >= floor) {
                if (dist < bestDist) { bestDist = dist; bestId = s.id(); }
            }
        }
        // Fallback: pick nearest elevator regardless of direction
        if (bestId == -1) {
            for (ElevatorState s : states) {
                int dist = Math.abs(s.currentFloor() - floor);
                if (dist < bestDist) { bestDist = dist; bestId = s.id(); }
            }
        }
        return bestId;
    }
}

// ---------------------------------------------------------------------------
// Internal Elevator model
// ---------------------------------------------------------------------------

class Elevator {

    private final int id;
    private int currentFloor;
    private ElevatorDirection direction;
    private DoorState doorState;
    // Ordered set of target floors — drives SCAN movement
    private final TreeSet<Integer> stops = new TreeSet<>();

    Elevator(int id, int startFloor) {
        this.id           = id;
        this.currentFloor = startFloor;
        this.direction    = ElevatorDirection.IDLE;
        this.doorState    = DoorState.CLOSED;
    }

    int getId()              { return id; }
    int getCurrentFloor()    { return currentFloor; }
    ElevatorDirection getDirection() { return direction; }
    DoorState getDoorState() { return doorState; }
    TreeSet<Integer> getStops()      { return stops; }

    void addStop(int floor) { stops.add(floor); }

    ElevatorState snapshot() {
        return new ElevatorState(id, currentFloor, direction, doorState);
    }

    /**
     * Advance one simulation tick.
     * <ul>
     *   <li>If door is OPEN → close it and remove the current floor from stops.</li>
     *   <li>If moving → advance one floor; open door if we've arrived at a stop.</li>
     *   <li>If IDLE and stops is non-empty → choose direction and move.</li>
     * </ul>
     */
    void tick() {
        if (doorState == DoorState.OPEN) {
            doorState = DoorState.CLOSED;
            stops.remove(currentFloor);
            updateDirection();
            return;
        }

        if (stops.isEmpty()) {
            direction = ElevatorDirection.IDLE;
            return;
        }

        // Determine next floor to move toward
        if (direction == ElevatorDirection.IDLE) {
            updateDirection();
        }

        if (direction == ElevatorDirection.UP) {
            currentFloor++;
        } else if (direction == ElevatorDirection.DOWN) {
            currentFloor--;
        }

        if (stops.contains(currentFloor)) {
            doorState = DoorState.OPEN;
        }
    }

    private void updateDirection() {
        if (stops.isEmpty()) { direction = ElevatorDirection.IDLE; return; }
        // If currently going UP and there are higher stops, keep going UP
        if (direction == ElevatorDirection.UP && stops.higher(currentFloor) != null) return;
        // If currently going DOWN and there are lower stops, keep going DOWN
        if (direction == ElevatorDirection.DOWN && stops.lower(currentFloor) != null) return;

        // Determine new direction based on remaining stops
        if (stops.higher(currentFloor) != null) {
            direction = ElevatorDirection.UP;
        } else if (stops.lower(currentFloor) != null) {
            direction = ElevatorDirection.DOWN;
        } else {
            direction = ElevatorDirection.IDLE;
        }
    }
}

// ---------------------------------------------------------------------------
// Top-level facade
// ---------------------------------------------------------------------------

public class ElevatorSystem {

    private final int numFloors;
    private final List<Elevator> elevators;
    private final SchedulingStrategy strategy;

    public ElevatorSystem(int numFloors, int numElevators, SchedulingStrategy strategy) {
        if (numFloors < 2)    throw new IllegalArgumentException("numFloors must be >= 2");
        if (numElevators < 1) throw new IllegalArgumentException("numElevators must be >= 1");
        this.numFloors = numFloors;
        this.strategy  = strategy != null ? strategy : new NearestElevatorStrategy();

        elevators = new ArrayList<>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i, 1)); // all elevators start at floor 1
        }
    }

    public void externalRequest(int floor, Direction direction) {
        validateFloor(floor);
        List<ElevatorState> states = getStates();
        int chosen = strategy.selectElevator(states, floor, direction);
        elevators.get(chosen).addStop(floor);
    }

    public void internalRequest(int elevatorId, int targetFloor) {
        if (elevatorId < 0 || elevatorId >= elevators.size())
            throw new IllegalArgumentException("Invalid elevatorId: " + elevatorId);
        validateFloor(targetFloor);
        elevators.get(elevatorId).addStop(targetFloor);
    }

    public void step() {
        for (Elevator e : elevators) {
            e.tick();
        }
    }

    public List<ElevatorState> getStates() {
        List<ElevatorState> result = new ArrayList<>(elevators.size());
        for (Elevator e : elevators) result.add(e.snapshot());
        return Collections.unmodifiableList(result);
    }

    private void validateFloor(int floor) {
        if (floor < 1 || floor > numFloors)
            throw new IllegalArgumentException("Floor must be between 1 and " + numFloors);
    }
}
