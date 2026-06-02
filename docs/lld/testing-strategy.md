# Testing Strategy — Test Types Across the Stack

A Staff engineer reasons about *which kind of test* belongs where. This doc leads with the
**practical test types you name in practice** (unit, smoke, integration, e2e, regression, …), each
marked with a **✅ working sample** in this repo or **📖 theory** (with the tool to use). Repo
samples live in JUnit 5 (`backend-service`), Vitest + Testing Library (`web/spa-react-vite`), and
jest-expo (`expo-app`).

## The shape: pyramid vs trophy

- **Test pyramid**: many fast **unit**, fewer **integration**, fewest **e2e**. The default for services.
- **Testing trophy** (front-end): bulk at **component/integration** (where UI bugs live), on a wide base of static analysis (types + lint).

Backend leans pyramid; UI leans trophy. Rule of thumb: **the slower and less isolated a test, the fewer you keep.**

---

## The practical test types

| Type | What it is / when it runs | In this repo |
|------|---------------------------|--------------|
| **Static analysis** | Types + lint catch whole bug classes before runtime | ✅ `tsc` + ESLint (web/expo), `tsc -b` in build; Java compiler |
| **Unit** | One function/class in isolation, deps faked. Fast, the bulk | ✅ [`MemorySink.test.ts`](../../web/spa-react-vite/src/observability/MemorySink.test.ts) (web) · [mobile](../../expo-app/src/observability/MemorySink.test.ts) · every `lld/**/*Test.java` |
| **Smoke** | Fast "is it fundamentally alive?" gate — run before the deep suite and post-deploy | ✅ [`SmokeTest`](../../backend-service/src/test/java/com/venkat/backend/SmokeTest.java) — `@Tag("smoke")`, run via `./mvnw test -Dgroups=smoke` |
| **Component** | A single UI component renders + behaves (DOM) | ✅ [`ObservabilityProvider.test.tsx`](../../web/spa-react-vite/src/observability/ObservabilityProvider.test.tsx) (RTL) · *(mobile = RNTL, 📖)* |
| **Integration** | Several units across a real boundary (HTTP, DB) | ✅ [`OpsEndpointsTest`](../../backend-service/src/test/java/com/venkat/backend/ops/OpsEndpointsTest.java) — `@SpringBootTest(RANDOM_PORT)` + `WebTestClient` |
| **Integration w/ real infra** | Against a real Postgres/Kafka, not a mock | 📖 **Testcontainers** (real services in Docker) |
| **Contract** | Two services agree on an API shape | 📖 **Spring Cloud Contract** / **Pact** |
| **System** | The whole assembled app, all modules wired, on staging | 📖 run the suite against a deployed env |
| **End-to-end (E2E)** | A real user journey through the running app | 📖 **Playwright**/Cypress (web) · **Detox**/**Maestro** (mobile) |
| **Regression** | Old features still work after a change | ✅ the whole CI suite *is* the regression net |
| **Sanity / retest** | Narrow check that one specific fix works | ✅ re-run the single previously-failing test |
| **Acceptance (UAT) / BDD** | Meets business needs; Given/When/Then | 📖 **Cucumber**; stakeholder sign-off on staging |
| **Performance** | **Load** (expected), **Stress** (past breaking), **Soak** (leaks over hours), **Spike** (surge), **Scalability**, **Volume** | 📖 **k6** / **Gatling** / **JMeter**; the [Prometheus metrics](../../backend-service/src/main/java/com/venkat/backend/ops/OpsController.java) feed the dashboards you watch |
| **Security** | **SAST**, **DAST**, **SCA/dependency scan**, **secret scan**, **pen test** | 📖 CodeQL/Semgrep · OWASP ZAP · `npm audit`/Dependabot/Snyk · `gitleaks` |
| **Accessibility** | WCAG: roles, labels, contrast, keyboard nav | 📖 **jest-axe** / `@axe-core/playwright` (LLD components built a11y-first) |
| **Compatibility** | Cross-browser, cross-device, cross-OS-version | 📖 Playwright projects / BrowserStack; RN on iOS+Android+web |
| **Reliability / resilience** | Recovery, failover, graceful degradation | ✅ **graceful shutdown** verified in `backend-service`; 📖 failover/chaos drills |
| **Localization (i18n)** | Translations, RTL, date/number formats | 📖 pseudo-localization pass |

## Testing in (and toward) production

| Type | Idea | In this repo |
|------|------|--------------|
| **Synthetic monitoring** | Scripted probes hit prod continuously | 📖 ping `/actuator/health` from a monitor |
| **Canary** | Release to a small %, watch error/latency, then ramp | 📖 gate on crash-free-sessions / SLO burn |
| **Blue-green validation** | Smoke the green stack before cutover | 📖 deploy strategy |
| **Shadow / dark traffic** | Mirror real traffic to the new version, discard responses | 📖 |
| **A/B / experimentation** | Compare variants on a real metric | 📖 ties to the [analytics layer](../../web/spa-react-vite/src/observability/) |
| **Feature-flag testing** | Test both flag states; kill-switch | 📖 flag both branches |
| **Chaos engineering** | Inject faults (kill nodes, add latency) to prove resilience | 📖 **Chaos Monkey** / Litmus |

---

## Advanced techniques (the *how*, applied within the types above)

| Technique | Idea | In this repo |
|-----------|------|--------------|
| **Black / white / gray-box** | Test via behavior / internal paths / partial knowledge | ✅ RTL (black) · concurrency races (white) · injected clock (gray) |
| **Parameterized / data-driven** | Same test over many input rows | ✅ [`ObservabilityTest.HistogramPercentiles`](../../backend-service/src/test/java/com/venkat/backend/lld/observability/ObservabilityTest.java) — `@ParameterizedTest` + `@CsvSource` |
| **Concurrency / race** | No lost updates under parallel load | ✅ `ExecutorService` + exact-count asserts (`ObservabilityTest`, `RateLimiterTest`) |
| **Property-based** | Invariants over *generated* inputs | 📖 **jqwik** (Java) · **fast-check** (TS) |
| **Fuzz** | Malformed/random input to find crashes | 📖 **jazzer** / libFuzzer |
| **Metamorphic** | Output relations when there's no oracle (`sort(shuffle x)==sort x`) | 📖 |
| **Snapshot / golden master** | Diff serialized output vs a stored baseline | 📖 Vitest/Jest `toMatchSnapshot()` · ApprovalTests (use sparingly) |
| **Mutation** | Mutate the code, expect tests to fail — scores test *quality* | 📖 **PIT** (Java) · **Stryker** (TS) |

## Test doubles (reference)

*Dummy* (filler) · *Stub* (canned return) · *Spy* (records calls) · *Mock* (asserts interactions) ·
*Fake* (working lightweight impl — e.g. the in-memory `MemorySink`). **Prefer fakes + real
collaborators over mocks** where cheap; mocks couple tests to implementation.

---

## Principles

- **Test behavior, not implementation.** Query by role/text, not internal state — refactors shouldn't break tests.
- **One reason to fail per test.** The name is a sentence about the behavior.
- **Determinism over sleeping.** Inject the clock (rate-limiter/observability katas). Flaky tests get ignored → disabled → deleted.
- **Coverage is a floor, not a goal.** 100% lines with weak assertions proves nothing — mutation testing is the real check.
- **Fast feedback.** Static + unit + component + smoke in the pre-push loop; integration/e2e/load/security scans gated in CI.

> **Legend:** ✅ = working sample in this repo · 📖 = covered by theory (with the tool to use).
