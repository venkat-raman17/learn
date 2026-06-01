# agentic-python

Practice app for the **modern AI-systems / agentic** track (Phase 4), built on the
**Claude Agent SDK**. Tooling: **uv** + **Ruff** + **pytest**.

## Setup

```powershell
uv sync                                   # venv + deps (Python 3.12+)
$env:ANTHROPIC_API_KEY = "sk-ant-..."     # for live agent calls
uv run python -m agentic.main
uv run pytest
uv run ruff check .
```

## Layout
- `src/agentic/` — package code (your agents/workflows)
- `tests/` — pytest

> If `claude-agent-sdk` doesn't resolve in your environment yet, adjust the dependency in
> `pyproject.toml` (pin a version, or start from the `anthropic` SDK) — this is a learning scaffold.
