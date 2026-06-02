# mcp-knowledge-server

An **MCP server** that exposes a lightweight **RAG knowledge base** as tools. It owns the *retrieval*
half of RAG; the [`agentic-python`](../agentic-python/) LangGraph agent is the MCP client that
retrieves from it and generates answers.

- **Retrieval**: from-scratch **TF-IDF** embeddings + an in-memory **cosine** vector store — real,
  deterministic, **no model download, no API keys**. Swappable for `sentence-transformers` / OpenAI /
  Voyage behind the `Embedder` seam.
- **Protocol**: [Model Context Protocol](https://modelcontextprotocol.io) over stdio via the official
  `mcp` Python SDK (FastMCP).
- **Corpus**: a small built-in set of docs about RAG / Corrective RAG / MCP / LangGraph / embeddings /
  agents, so the demo is self-explanatory.

## Tools

| Tool | Signature | Returns |
|------|-----------|---------|
| `search_knowledge` | `(query: str, k: int = 4)` | top-k chunks: `{chunk_id, doc_id, title, text, score}` |
| `list_documents` | `()` | `[{doc_id, title}]` |

## Run

```bash
uv sync
uv run knowledge-server          # serves over stdio (for an MCP client to spawn)
uv run pytest                    # 7 tests: retrieval engine + end-to-end stdio MCP roundtrip
uv run ruff check .
```

The server is normally **spawned by a client** (see `agentic-python`), not run by hand. To wire it
into an MCP host (Claude Desktop, an IDE), point the host at the `knowledge-server` command over
stdio.

## Layout

```
src/mcp_knowledge/
  embeddings.py     # Embedder seam + from-scratch TfidfEmbedder
  vector_store.py   # in-memory cosine top-k (the idea behind FAISS/Chroma)
  chunking.py       # paragraph-packing chunker
  corpus.py         # built-in sample documents
  rag.py            # KnowledgeBase: ingest + search
  server.py         # FastMCP server exposing the tools
tests/
  test_rag.py       # retrieval engine
  test_server.py    # spawns the server over stdio and calls the tools
```
