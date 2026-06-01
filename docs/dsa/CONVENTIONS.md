# dsa-java conventions

The rules every file in `dsa-java` follows. When starting something new, copy the matching
template from [`templates/`](templates/).

## One concept per file
Each data structure, algorithm, and coding problem is its own `.java` file named for the concept
(`DynamicArray.java`, `MergeSort.java`, `TwoSum.java`). Its test sits in the mirror package under
`src/test/...` with a `Test` suffix.

## Packages (`com.venkat.dsa`)
- `linear/ hashing/ trees/ graphs/` — data structures.
- `algorithms/{sorting,searching,graph}/` — techniques. Graph *structures* live in `graphs/`;
  graph *algorithms* live in `algorithms/graph/`.
- `coding/{easy,medium,hard}/` — NeetCode 150, foldered by difficulty, pattern noted in Javadoc.

## Data structure (reference) — fully worked, you read to learn
- Generic where it makes sense; **no external dependencies** (pure Java).
- Class Javadoc carries: one-line definition, backing representation, invariant(s), an
  **operations table with time & space Big-O**, "when to use", and interview notes.
- Each public method's Javadoc states its Big-O.
- A sibling test exercises every operation + edge cases (empty, single, resize, duplicates,
  boundaries) and doubles as runnable usage documentation. Tests must pass.

## Coding problem (practice) — a stub you solve
- Class Javadoc: id + title, **Difficulty**, **Pattern**, link, restated problem, constraints,
  2–3 examples, target time/space, progressive **hints**, follow-ups.
- The public method body is `throw new UnsupportedOperationException("implement me")`.
- Sibling test holds official + edge cases. **You implement until green.** A fresh, unsolved
  practice test is marked `@Disabled("start me")` so the build stays green; remove it when you begin.
- Reference solutions are added **on request**, after you attempt (sibling `*_Solution.java`).

## Complexity annotations
Always state **time and space**, amortized vs. worst-case where they differ (mark amortized `*`
and explain). Define symbols (`n`, `k`, `V`, `E`).

## Tests
JUnit 5 (Jupiter). One test class per concept, mirror package, `…Test` suffix. Plain unit tests
(no framework). Name tests by behavior (`popOnEmptyThrows`, `growsBeyondInitialCapacity`).
