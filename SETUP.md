# Setup

One-time setup per project. **First-time installs need internet** (Maven Central, npm, PyPI); after
that, builds/tests run offline from the local caches. You don't push anything — just clone/unzip,
install, and work locally.

## Prerequisites

| Tool | Version | Check |
| --- | --- | --- |
| JDK | 21+ | `java -version` |
| Node.js | 22.12+ (or 20.19+) | `node -v` |
| npm | 10+ | `npm -v` |
| uv (Python) | latest | `uv --version` |
| Docker | latest (for `infra/`) | `docker --version` |

Each Java project ships the Maven wrapper (`mvnw` / `mvnw.cmd`) — you don't need a system Maven.
Recommended IDEs: **IntelliJ IDEA** (Java) and **VS Code** (web/Python).

## Per project

```powershell
# DSA — data structures + coding (run the tests; first run downloads deps)
cd dsa-java;            .\mvnw.cmd test

# Backend service (LLD katas, later Kafka/DB)
cd backend-service;    .\mvnw.cmd test

# Frontend apps — one install each (node_modules is git-ignored, not in the zip)
cd web\spa-react-vite;     npm install;  npm run dev      # http://localhost:5173
cd web\nextjs-app;         npm install;  npm run dev
cd web\react-router-app;   npm install;  npm run dev
cd web\astro-app;          npm install;  npm run dev
cd web\ssr-tanstack-start; npm install;  npx prisma generate;  npm run dev   # needs a DB URL in .env.local

# Native + web (Expo)
cd expo-app;           npm install;  npm run web          # or: npm run android / ios

# Agentic (Python)
cd agentic-python;     uv sync;  $env:ANTHROPIC_API_KEY="sk-ant-...";  uv run pytest

# Infra (databases + Kafka) — needs Docker running
cd infra;              docker compose up -d                # Kafka UI: http://localhost:8081
```

## Self-check that everything works

- `dsa-java`: `.\mvnw.cmd test` → **all green** (data-structure tests + verified coding solutions).
- `backend-service`: `.\mvnw.cmd test` → green (LLD practice tests stay `@Disabled` until you solve them).
- Each web app: `npm run build` → succeeds.

## Notes for a restricted / corporate laptop

- You only need **outbound** access to the package registries for the first install. No pushing,
  no inbound, no accounts required.
- If a registry is blocked, point the tool at your corporate mirror: npm (`.npmrc` `registry=`),
  Maven (`~/.m2/settings.xml` mirror), uv (`UV_INDEX_URL`).
- Markdown study material (`docs/`, all the case studies and reading) needs **no tooling at all** —
  read it in any editor or the GitHub web view.
