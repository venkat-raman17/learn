# Testing Strategy — Test Types Across the Stack

A Staff engineer is expected to reason about *which kind of test* belongs where, not just write
more of them. This repo demonstrates the core types **by working sample** (backend JUnit, web
Vitest/RTL, mobile jest-expo) and the rest **by theory** below.

## The shape: pyramid vs trophy

- **Test pyramid**: many fast **unit** tests, fewer **integration**, fewest **e2e**. Optimizes for speed/cost; the classic default for services.
- **Testing trophy** (front-end leaning): bulk at the **integration/component** layer because that's where UI bugs actually live, with static analysis (types + lint) as the wide base. *"Write tests. Not too many. Mostly integration."*

Pick per layer: backend leans pyramid; UI leans trophy.

## Test types

| Type | What it verifies | Isolation | Speed |
|------|------------------|-----------|-------|
| **Static** (types, lint) | Whole classes of bugs before runtime | n/a | instant |
| **Unit** | One function/class in isolation | full (mocks/fakes) | ⚡ ms |
| **Component** | A UI component renders + behaves | DOM, mocked deps | fast |
| **Integration** | Several units across a boundary (DB, HTTP) | partial | medium |
| **Contract** | Two services agree on an API shape | consumer/provider | medium |
| **E2E** | A real user flow through the running app | none (real stack) | 🐢 slow |
| **Visual regression** | Pixels didn't change unexpectedly | rendered output | medium |
| **Property-based** | Invariants hold over generated inputs | unit-level | medium |
| **Load / perf** | Latency/throughput under concurrency | real stack | slow |
| **Mutation** | Your tests actually *catch* bugs (mutate code, expect failures) | meta | slow |
| **Accessibility** | No a11y violations (roles, contrast, labels) | component/e2e | fast |

**Test doubles**: *dummy* (filler) · *stub* (canned answers) · *spy* (records calls) · *mock* (asserts interactions) · *fake* (working lightweight impl, e.g. in-memory sink/DB). Prefer fakes + real collaborators over mocks where cheap — mocks couple tests to implementation.

---

## Mapped to this repo

### Backend — JUnit 5 (`backend-service`)
| Type | Where | Notes |
|------|-------|-------|
| Unit | every `lld/**/*Test.java` | plain assertions |
| **Parameterized** | [`ObservabilityTest.HistogramPercentiles`](../../backend-service/src/test/java/com/venkat/backend/lld/observability/ObservabilityTest.java) | `@ParameterizedTest` + `@CsvSource` |
| **Nested** | same file | `@Nested` grouping |
| **Concurrency** | `ObservabilityTest`, `RateLimiterTest` | `ExecutorService` + exact-count assertions (no lost updates) |
| **Integration** | [`OpsEndpointsTest`](../../backend-service/src/test/java/com/venkat/backend/ops/OpsEndpointsTest.java) | `@SpringBootTest(RANDOM_PORT)` + `WebTestClient` against a live server |
| Contract *(theory)* | — | Spring Cloud Contract / Pact for provider-consumer pacts |
| Integration w/ real infra *(theory)* | — | **Testcontainers** (spin a real Postgres/Kafka in Docker) |
| Mutation *(theory)* | — | **PIT** (`pitest`) to score test effectiveness |
| Load *(theory)* | — | **Gatling** / **k6** against the running service |

### Web — Vitest + Testing Library (`web/spa-react-vite`)
| Type | Where | Notes |
|------|-------|-------|
| **Unit** | [`MemorySink.test.ts`](../../web/spa-react-vite/src/observability/MemorySink.test.ts) | pure logic, no DOM |
| **Component / integration** | [`ObservabilityProvider.test.tsx`](../../web/spa-react-vite/src/observability/ObservabilityProvider.test.tsx) | RTL render + `user-event`; ErrorBoundary capture |
| E2E *(theory)* | — | **Playwright** / Cypress drives the built app |
| Visual *(theory)* | — | Playwright snapshots or **Storybook** + Chromatic |
| A11y *(theory)* | — | **jest-axe** / `@axe-core/playwright` |

### Mobile — jest-expo (`expo-app`)
| Type | Where | Notes |
|------|-------|-------|
| **Unit** | [`MemorySink.test.ts`](../../expo-app/src/observability/MemorySink.test.ts) | jest-expo preset |
| Component *(theory)* | — | **React Native Testing Library** renders RN components |
| E2E *(theory)* | — | **Detox** (gray-box) or **Maestro** (black-box) on a simulator |

---

## Principles

- **Test behavior, not implementation.** RTL queries by role/text, not internal state — refactors shouldn't break tests.
- **One reason to fail per test.** Clear name = a sentence about the behavior.
- **Determinism**: inject the clock (see the rate-limiter/observability katas) instead of `sleep`. Flaky tests get ignored, then disabled, then deleted.
- **Coverage is a floor, not a goal.** 100% line coverage with no assertions proves nothing — mutation testing is the real check.
- **Fast feedback**: keep unit/component tests in the pre-push loop; gate e2e/load in CI.
