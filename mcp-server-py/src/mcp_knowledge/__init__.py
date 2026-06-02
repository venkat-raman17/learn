"""MCP knowledge server — a lightweight RAG retrieval engine exposed over MCP."""

from .rag import KnowledgeBase, SearchHit

__all__ = ["KnowledgeBase", "SearchHit"]
