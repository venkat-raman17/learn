/**
 * <h2>LLD Practice Problem: Elevator System</h2>
 *
 * <h3>Problem Statement</h3>
 * <p>
 * Design and implement a simulation of an Elevator System for a building with
 * {@code M} floors and {@code N} elevators. The system accepts external requests
 * (a person pressing the UP or DOWN button on a floor) and internal requests
 * (a person inside an elevator pressing a destination floor button). A pluggable
 * {@link com.venkat.backend.lld.elevator.SchedulingStrategy} decides which
 * elevator should service each external request. The simulation advances
 * one logical tick at a time via a {@code step()} call.
 * </p>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li>The system is initialised with a fixed number of floors and elevators.</li>
 *   <li>Any floor can issue an external request specifying a direction (UP or DOWN).</li>
 *   <li>Any elevator can receive an internal request specifying a target floor.</li>
 *   <li>Each elevator tracks: current floor, movement direction (UP / DOWN / IDLE),
 *       door state (OPEN / CLOSED), and its own pending request queue.</li>
 *   <li>On each {@code step()} tick the system moves every elevator one floor closer
 *       to its next stop, opens/closes doors at a stop, and dispatches idle elevators
 *       to pending external requests.</li>
 *   <li>The scheduling strategy is swappable at construction time (Strategy pattern).</li>
 *   <li>It must be possible to query the current state snapshot of all elevators.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ol>
 *   <li>Thread-safe enough that a single-threaded simulation loop calling {@code step()}
 *       does not produce inconsistent state; concurrency notes in the extensibility section.</li>
 *   <li>No framework dependencies — plain Java 21, {@code java.util} only.</li>
 *   <li>O(N) per {@code step()} where N is the number of elevators.</li>
 *   <li>Floors are numbered 1..M (ground = 1).</li>
 * </ol>
 *
 * <h3>Suggested Entities</h3>
 * <ul>
 *   <li>{@code ElevatorSystem} — top-level facade; owns elevators and the strategy.</li>
 *   <li>{@code Elevator} — internal model; holds floor, direction, door state, queue.</li>
 *   <li>{@code ElevatorState} (record/DTO) — immutable snapshot for external observers.</li>
 *   <li>{@code SchedulingStrategy} — interface; {@code selectElevator(List&lt;ElevatorState&gt;, int floor, Direction)} returns an elevator id.</li>
 *   <li>{@code NearestIdleStrategy} — simplest strategy: pick the idle elevator closest to the request floor.</li>
 *   <li>{@code Direction} enum — UP, DOWN.</li>
 *   <li>{@code ElevatorDirection} enum — UP, DOWN, IDLE.</li>
 *   <li>{@code DoorState} enum — OPEN, CLOSED.</li>
 * </ul>
 *
 * <h3>Public API (implement these exactly)</h3>
 * <pre>{@code
 * ElevatorSystem system = new ElevatorSystem(numFloors, numElevators, strategy);
 * system.externalRequest(floor, Direction.UP);
 * system.internalRequest(elevatorId, targetFloor);
 * system.step();
 * List<ElevatorState> states = system.getStates();
 * // ElevatorState fields: int id, int currentFloor, ElevatorDirection direction, DoorState doorState
 * }</pre>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><strong>Strategy</strong> — {@code SchedulingStrategy} lets you swap scheduling algorithms
 *       (nearest-idle, SCAN/LOOK, round-robin) without changing {@code ElevatorSystem}.</li>
 *   <li><strong>State</strong> — each elevator's behaviour on a tick differs depending on whether
 *       it is IDLE, moving, or at a stop with doors open; a State pattern can model this cleanly.</li>
 *   <li><strong>Observer / Event</strong> — floors or UI components could subscribe to elevator
 *       arrival events; consider how you would add this without coupling the core to observers.</li>
 * </ul>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>How would you implement the SCAN (elevator) algorithm as a {@code SchedulingStrategy},
 *       and how does it differ from nearest-idle in worst-case wait time?</li>
 *   <li>If {@code step()} were called from multiple threads simultaneously, which shared state
 *       needs synchronisation and what data structure would you use for each elevator's queue?</li>
 *   <li>How would you extend the system to support express elevators that only service
 *       floors 1 and 20-30, without changing the {@code ElevatorSystem} API?</li>
 * </ol>
 *
 * <h3>Extensibility and Concurrency Prompt</h3>
 * <p>
 * Imagine the building operator wants to change the scheduling strategy at runtime
 * (e.g., switch to energy-saving mode during off-peak hours). What interface or
 * lifecycle method would you add? How would you prevent a strategy swap mid-{@code step()}
 * from causing an elevator to be dispatched twice or not at all?
 * Consider using {@code ReentrantReadWriteLock} or an immutable snapshot of pending
 * requests passed to the strategy so the core loop remains lock-free.
 * </p>
 *
 * @author  learner
 * @version 1.0
 */
package com.venkat.backend.lld.elevator;
