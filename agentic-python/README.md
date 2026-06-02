# agentic-python

Practice app for the **modern AI-systems / agentic** track: **Corrective (Agentic) RAG** built as a
**LangGraph** state machine that retrieves through the **MCP** knowledge server
([`../mcp-server-py`](../mcp-server-py/)). Tooling: **uv** + **Ruff** + **pytest**.

Runs **fully keyless** — a deterministic `FakeLLM` + the local MCP server — and swaps to real Claude
in one line.

## The pipeline (Corrective RAG)

```
retrieve ──▶ grade ──▶ (relevant?) ──▶ generate ──▶ answer (+ citations)
   ▲                       │ no, retries left
   └──── transform_query ◀─┘   (bounded → always terminates)
```

- **retrieve** — call the MCP server's `search_knowledge` tool (`MCPRetriever`, stdio).
- **grade** — keep only chunks the LLM judges relevant.
- **decide** — relevant chunks → generate; otherwise rewrite the query and retry (retry budget).
- **generate** — grounded answer with `[Source]` citations.

## Setup

```bash
uv sync
uv run python -m agentic.main "what is corrective rag and how does it self-correct?"
uv run pytest          # 10 tests (graph logic, FakeLLM, + real MCP integration)
uv run ruff check .
```

### Use real Claude

```bash
uv sync --extra claude
export ANTHROPIC_API_KEY=sk-ant-...
export AGENTIC_MODEL=<a-claude-model-id>
# then construct ClaudeLLM() instead of FakeLLM() in agentic/main.py
```

## Layout

```
src/agentic/
  llm.py          # LLM seam: FakeLLM (deterministic, keyless) + ClaudeLLM (real, optional)
  retriever.py    # Retriever seam: MCPRetriever (spawns the MCP server) + StaticRetriever (tests)
  rag_graph.py    # the LangGraph Corrective-RAG state machine
  main.py         # CLI: question -> answer + step trace
tests/            # graph happy-path + corrective loop + LLM + real MCP roundtrip
```

The MCP server is a **path dependency** (`tool.uv.sources`), so `uv sync` wires the client→server
flow automatically within the monorepo.
