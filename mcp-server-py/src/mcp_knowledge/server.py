"""MCP server exposing the RAG knowledge base over stdio.

Run it directly (``uv run knowledge-server`` or ``python -m mcp_knowledge.server``); an MCP client
(the agentic-python LangGraph agent, or any MCP-aware host) connects over stdio and calls the tools
below. The server owns retrieval; clients own generation.
"""

from __future__ import annotations

from mcp.server.fastmcp import FastMCP

from .rag import KnowledgeBase

mcp = FastMCP("knowledge-base")

# One knowledge base for the process, built from the bundled corpus at startup.
_kb = KnowledgeBase.from_default_corpus()


@mcp.tool()
def search_knowledge(query: str, k: int = 4) -> list[dict]:
    """Semantic search over the knowledge base.

    Args:
        query: natural-language question or keywords.
        k: number of chunks to return (default 4).

    Returns:
        Top-k chunks, each with chunk_id, doc_id, title, text, and a relevance score (0-1).
    """
    return [hit.to_dict() for hit in _kb.search(query, k)]


@mcp.tool()
def list_documents() -> list[dict]:
    """List the documents in the knowledge base (doc_id + title)."""
    return _kb.documents()


def run() -> None:
    """Console-script entry point — serves over stdio."""
    mcp.run(transport="stdio")


if __name__ == "__main__":
    run()
