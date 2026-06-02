# CI/CD — Pipelines, Deployments & DevOps

Reference for Staff-level CI/CD design discussions. The `.github/workflows/` in this repo are
working examples you can trace alongside this doc.

---

## What CI/CD solves

| Without CI/CD | With CI/CD |
|---|---|
| "Works on my machine" | Every commit verified on a clean, identical environment |
| Manual test runs (skipped under deadline) | Tests are mandatory — the merge gate blocks skips |
| Big-bang deployments (risky, infrequent) | Small, frequent, automated deployments (lower risk per deploy) |
| "Who broke the build?" archaeology | Broken commit identified within minutes, author notified |
| Security holes found in prod | Vulnerability scans run on every PR |

---

## CI vs CD vs CD

| Acronym | Full name | What it does |
|---|---|---|
| **CI** | Continuous Integration | Merge code frequently; automatically build + test every commit |
| **CD** | Continuous Delivery | Every passing build is *deployable* to production (human triggers the deploy) |
| **CD** | Continuous Deployment | Every passing build is *automatically deployed* to production (no human gate) |

Continuous Deployment requires very high test confidence and strong observability (so you can roll back quickly). Most teams practice Continuous Delivery.

---

## GitHub Actions — key concepts

```
Event (push, PR, schedule)
  └─▶ Workflow (.github/workflows/*.yml)
         └─▶ Job (runs on a runner)
                └─▶ Step (shell command or action)
```

**Key terms:**
- **Runner**: ephemeral VM (Ubuntu, macOS, Windows) or self-hosted. Each job gets a fresh runner.
- **Action**: reusable step published to the Marketplace (`actions/checkout@v4`).
- **Secret**: encrypted value injected at runtime (`${{ secrets.MY_SECRET }}`).
- **Environment**: named deployment target (staging, production) with protection rules (required reviewers).
- **Concurrency group**: prevents two runs of the same workflow on the same branch from racing.

---

## This repo's CI structure

```
.github/workflows/
  ci-java.yml         # dsa-java + backend-service tests (Java 21 / Maven)
  ci-python.yml       # mcp-server-py + agentic-python (uv / pytest / Ruff)
  ci-web.yml          # spa-react-vite (Vitest) + graphql-layer (tsc) + web apps (build)
  ci-security.yml     # npm audit + pip-audit + OWASP dep-check + Gitleaks + CodeQL
```

### Path filtering — only run what changed

```yaml
on:
  push:
    paths:
      - "backend-service/**"
      - ".github/workflows/ci-java.yml"
```

Without path filtering, every commit would trigger all four workflows — slow and wasteful.
With path filtering, a Python change runs only `ci-python.yml`.

### Caching — fast rebuilds

```yaml
- uses: actions/setup-java@v4
  with:
    cache: maven       # restores ~/.m2 between runs — saves 60–90s on Maven downloads

- uses: actions/setup-node@v4
  with:
    cache: npm
    cache-dependency-path: web/spa-react-vite/package-lock.json
```

Maven cache key is based on `pom.xml` hash; npm cache key on `package-lock.json` hash.
Cache misses are not failures — the job runs slower but still succeeds.

### Fail-fast vs matrix continuation

```yaml
strategy:
  fail-fast: false    # all matrix jobs complete even if one fails
  matrix:
    app: [nextjs-app, react-router-app, astro-app]
```

`fail-fast: true` (default) cancels remaining matrix jobs on first failure — faster feedback,
but you lose visibility into how many apps are broken. Use `false` for independent targets.

### Concurrency — cancel stale runs

```yaml
concurrency:
  group: ci-java-${{ github.ref }}
  cancel-in-progress: true
```

If you push twice quickly, the first run is cancelled when the second starts. Prevents a queue
of stale runs clogging runners on a busy branch.

---

## Docker in CI

### GitHub-provided service containers

For integration tests that need a real database (used in `ci-java.yml` for the `main` branch job):

```yaml
services:
  postgres:
    image: postgres:16-alpine
    env:
      POSTGRES_USER: app
      POSTGRES_PASSWORD: app_password
      POSTGRES_DB: appdb
    ports: ["5432:5432"]
    options: >-
      --health-cmd "pg_isready -U app -d appdb"
      --health-interval 10s
      --health-retries 5
```

The service starts before the job steps; the health check ensures it's ready before tests run.

### Multi-stage Docker builds (already in `web/react-router-app/Dockerfile`)

```dockerfile
# Stage 1: build
FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Stage 2: runtime — small image, no build tools
FROM node:22-alpine
WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
EXPOSE 3000
CMD ["node", "dist/server.js"]
```

Multi-stage: the final image contains only runtime artefacts (no TypeScript compiler, no
source maps with secrets, no `devDependencies`). Typical result: 800 MB → 120 MB.

---

## Deployment strategies

### Blue-green deployment

```
Router ──▶ Blue (current prod, v1.0)
           Green (new version v1.1 — deployed but not receiving traffic)

1. Deploy v1.1 to Green.
2. Run smoke tests against Green.
3. Flip router: Blue gets 0%, Green gets 100%.
4. Blue stays warm for instant rollback.
```

Zero downtime. Rollback = flip router back. Requires 2× infra cost.

### Canary release

```
Router ──▶ v1.0 (95% of traffic)
           v1.1 (5% of traffic — canary)

Gradually increase: 5% → 10% → 25% → 50% → 100%
Monitor error rate + latency at each step. Rollback if metrics degrade.
```

Limits blast radius. Used by Netflix, Google. Requires feature flag infrastructure or
weighted routing (ALB target group weights, Istio, NGINX upstream).

### Feature flags

```typescript
// Decouple deploy from release — code ships dark, enabled via config
if (featureFlags.isEnabled('new-checkout-flow', userId)) {
  return <NewCheckout />;
}
return <OldCheckout />;
```

Providers: LaunchDarkly, Unleash (OSS), PostHog. Flags allow:
- A/B testing (different groups see different code).
- Gradual rollout without separate deployments.
- Kill switch — disable a feature without a redeploy.

### Rolling update (Kubernetes)

```yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxSurge: 1        # create 1 new pod before terminating an old one
    maxUnavailable: 0  # never take a pod down before a new one is ready
```

Kubernetes terminates old pods one-by-one as new pods pass readiness probes. No downtime
if readiness probes are correctly configured (`/actuator/health/readiness` — already wired).

---

## CI/CD security

### Secrets in Actions

```yaml
# ✅ Reference, never print
env:
  JWT_SECRET: ${{ secrets.JWT_SECRET }}

# ❌ Never echo secrets — GitHub masks known secrets but you might expose unknowns
- run: echo "Secret is $JWT_SECRET"
```

### Pinning action versions by SHA

```yaml
# ❌ Tag can be moved (supply-chain attack)
- uses: actions/checkout@v4

# ✅ Pin to the SHA of the v4 tag — immutable
- uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683  # v4.2.2
```

### Minimum permissions

```yaml
permissions:
  contents: read        # most jobs only need to read source
  security-events: write  # only CodeQL needs this
```

Default `GITHUB_TOKEN` has broad write permissions. Restrict globally in repo settings and override per-job.

### `pull_request_target` danger

```yaml
# ⚠️ pull_request_target runs in the context of the BASE branch — it has secrets access
# A malicious PR could exfiltrate secrets if you run its code here
on: pull_request_target

# ✅ Use pull_request (no secrets access) for untrusted forks
on: pull_request
```

---

## Key metrics (DORA)

The four DORA metrics measure delivery health:

| Metric | Elite performers | What it measures |
|---|---|---|
| **Deployment frequency** | Multiple per day | How often you ship |
| **Lead time for changes** | < 1 hour | Commit → prod time |
| **Change failure rate** | < 5% | % of deploys causing incidents |
| **Time to restore** | < 1 hour | How fast you recover from failure |

Short lead time requires small commits, automated testing, and no manual approval gates in the hot path.

---

## Interview talking points

1. **Trunk-based development vs long-lived branches**: trunk-based (commit to main daily, feature flags for in-progress work) enables high deployment frequency; long-lived feature branches create merge debt and slow CI.

2. **Shift-left testing**: expensive tests (integration, E2E) should still run in CI, but slow ones can run only on `main` (not every PR). The key is keeping the PR feedback loop under 10 minutes.

3. **Immutable artefacts**: build once, promote the same Docker image / JAR through staging → production. Never rebuild for prod — the build is the known quantity, not the source.

4. **Rollback vs roll-forward**: rollback is fast but loses the fix; roll-forward (hotfix commit to main, redeploy) is safer for small fixes. Blue-green makes rollback instant — the old version is still running.

5. **GitOps**: the deployment state is declared in a Git repo (Helm charts, Kustomize manifests); ArgoCD/Flux reconciles live state to declared state. Git history = audit log; PR review = change approval.
