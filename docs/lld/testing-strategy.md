# Testing Strategy — A Complete Reference of Test Types

A Staff engineer reasons about *which kind of test* belongs where, not just writes more of them.
This doc is a **complete catalog of test types**: each is defined, and marked either with a
**working sample** in this repo or as **theory** (with the tool you'd reach for). The repo samples
live in JUnit 5 (`backend-service`), Vitest + Testing Library (`web/spa-react-vite`), and jest-expo
(`expo-app`).

## The shape: pyramid vs trophy

- **Test pyramid**: many fast **unit** tests, fewer **integration**, fewest **e2e**. Optimizes for speed/cost; the classic default for services.
- **Testing trophy** (front-end leaning): bulk at the **integration/component** layer because that's where UI bugs live, on a wide base of static analysis (types + lint). *"Write tests. Not too many. Mostly integration."*

Backend leans pyramid; UI leans trophy. Either way: **the slower and less isolated a test, the fewer you keep.**

---

## 1. By scope (the pyramid axis)

| Type | Verifies | In this repo |
|------|----------|--------------|
| **Static analysis** | Whole bug classes pre-runtime: type errors, lint, dead code | ✅ TypeScript `tsc` + ESLint (web/expo), `tsc -b` in build; Java compiler |
| **Unit** | One function/class in isolation, deps faked | ✅ [`MemorySink.test.ts`](../../web/spa-react-vite/src/observability/MemorySink.test.ts) (web) · [mobile](../../expo-app/src/observability/MemorySink.test.ts) · all `lld/**/*Test.java` |
| **Component** | A single UI component renders + behaves (DOM) | ✅ [`ObservabilityProvider.test.tsx`](../../web/spa-react-vite/src/observability/ObservabilityProvider.test.tsx) (RTL) · *(mobile component = RNTL, theory)* |
| **Integration** | Several units across a real boundary (HTTP, DB) | ✅ [`OpsEndpointsTest`](../../backend-service/src/test/java/com/venkat/backend/ops/OpsEndpointsTest.java) — `@SpringBootTest(RANDOM_PORT)` + `WebTestClient` |
| **Integration w/ real infra** | Code against a real Postgres/Kafka, not a mock | 📖 **Testcontainers** (spins real services in Docker) |
| **System** | The whole assembled app, all modules wired | 📖 deploy to a staging env, run the suite against it |
| **End-to-end (E2E)** | A real user journey through the running app | 📖 **Playwright**/Cypress (web) · **Detox**/**Maestro** (mobile) |

## 2. By technique

| Type | Idea | In this repo |
|------|------|--------------|
| **Black-box** | Test via public behavior, no internals | ✅ default style (RTL queries by role/text) |
| **White-box** | Exercise internal paths/branches deliberately | ✅ concurrency tests hitting specific race windows |
| **Gray-box** | Some internal knowledge (e.g. inject a clock) | ✅ injected-clock determinism in the rate-limiter/observability katas |
| **Parameterized / data-driven** | Same test over many input rows | ✅ [`ObservabilityTest.HistogramPercentiles`](../../backend-service/src/test/java/com/venkat/backend/lld/observability/ObservabilityTest.java) — `@ParameterizedTest` + `@CsvSource` |
| **Property-based** | Assert invariants over *generated* inputs | 📖 **jqwik** (Java) · **fast-check** (TS) — e.g. "encrypt→decrypt is identity for any string" |
| **Fuzz** | Throw malformed/random input to find crashes | 📖 **jazzer** (Java) · libFuzzer; great for parsers/decoders |
| **Metamorphic** | Relations between outputs when there's no oracle | 📖 e.g. `sort(shuffle(x)) == sort(x)` |
| **Snapshot** | Serialize output, diff against a stored baseline | 📖 Vitest/Jest `toMatchSnapshot()` — use sparingly (brittle) |
| **Golden master / approval** | Pin legacy output before refactor | 📖 ApprovalTests |
| **Mutation** | Mutate the code, expect tests to fail — scores test *quality* | 📖 **PIT** (Java) · **Stryker** (TS) |
| **Concurrency / race** | No lost updates under parallel load | ✅ `ExecutorService` + exact-count asserts in `ObservabilityTest`/`RateLimiterTest` |

## 3. Functional test purposes

| Type | When it runs | In this repo |
|------|-------------|--------------|
| **Smoke** | "Does it boot / can I log in?" — fast gate before deeper tests | 📖 a tiny CI job hitting `/actuator/health` |
| **Sanity** | Narrow check that one fix works after a change | 📖 manual/targeted |
| **Regression** | Old features still work after changes | ✅ the whole suite *is* the regression net |
| **Retest / confirmation** | Re-run the exact failing case after a fix | ✅ run the single failed test |
| **Acceptance (UAT)** | Meets business/user requirements | 📖 stakeholder sign-off on staging |
| **BDD / spec-by-example** | Behavior expressed in Given/When/Then | 📖 **Cucumber** (Java) · spec-style `describe/it` (already used) |
| **Exploratory / ad-hoc** | Human pokes at it without a script | 📖 manual session, time-boxed |

## 4. Non-functional test types

| Category | Specific types | In this repo |
|----------|---------------|--------------|
| **Performance** | **Load** (expected traffic), **Stress** (past breaking point), **Soak/Endurance** (memory leaks over hours), **Spike** (sudden surge), **Scalability** (does adding nodes help?), **Volume** (huge datasets) | 📖 **k6** / **Gatling** / **JMeter** against the running service; the [Prometheus metrics](../../backend-service/src/main/java/com/venkat/backend/ops/OpsController.java) feed the dashboards you'd watch |
| **Security** | **SAST** (static code scan), **DAST** (attack the running app), **IAST**, **SCA / dependency scanning**, **secret scanning**, **penetration testing** | 📖 CodeQL/Semgrep · OWASP ZAP · `npm audit`/Dependabot/Snyk · `gitleaks` |
| **Accessibility** | WCAG: roles, labels, contrast, keyboard nav | 📖 **jest-axe** / `@axe-core/playwright`; the LLD components were built a11y-first |
| **Usability** | Real users complete tasks | 📖 moderated studies |
| **Compatibility** | Cross-browser, cross-device, cross-OS-version | 📖 Playwright projects / BrowserStack; RN tested on iOS+Android+web |
| **Localization / i18n** | Translations, RTL, date/number formats | 📖 pseudo-localization pass |
| **Reliability / resilience** | Recovery, failover, graceful degradation | ✅ **graceful shutdown** verified in `backend-service`; 📖 failover drills |
| **Installation / upgrade** | Clean install + migration paths | 📖 test migration scripts |
| **Compliance** | GDPR/SOC2/PCI controls hold | 📖 audited checks |

## 5. Testing in (and toward) production

| Type | Idea | In this repo |
|------|------|--------------|
| **Synthetic monitoring** | Scripted probes hit prod continuously | 📖 ping `/actuator/health` from a monitor |
| **Canary** | Release to a small % first, watch error/latency | 📖 gate on crash-free-sessions / SLO burn |
| **Blue-green validation** | Smoke the green stack before cutover | 📖 deploy strategy |
| **Shadow / dark traffic** | Mirror real traffic to the new version, discard responses | 📖 |
| **A/B / experimentation** | Compare variants on a real metric | 📖 ties to the [analytics layer](../../web/spa-react-vite/src/observability/) |
| **Feature-flag testing** | Test both flag states; kill-switch | 📖 flag both branches |
| **Chaos engineering** | Inject faults (kill nodes, add latency) to prove resilience | 📖 **Chaos Monkey** / Litmus |

## Test doubles (reference)

*Dummy* (filler arg) · *Stub* (canned return) · *Spy* (records calls) · *Mock* (asserts interactions) ·
*Fake* (working lightweight impl — e.g. the in-memory `MemorySink`/an in-memory DB). **Prefer fakes +
real collaborators over mocks** where cheap; mocks couple tests to implementation details.

---

## Principles

- **Test behavior, not implementation.** RTL queries by role/text, not internal state — refactors shouldn't break tests.
- **One reason to fail per test.** The name is a sentence about the behavior.
- **Determinism over sleeping.** Inject the clock (rate-limiter/observability katas). Flaky tests get ignored → disabled → deleted.
- **Coverage is a floor, not a goal.** 100% lines with weak assertions proves nothing — *mutation testing* is the real check.
- **Fast feedback.** Keep static + unit + component in the pre-push loop; gate integration/e2e/load/security scans in CI.

> **Legend:** ✅ = working sample in this repo · 📖 = covered by theory (with the tool to use).
