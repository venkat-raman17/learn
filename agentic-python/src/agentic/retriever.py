"""Retrievers — the "R" of RAG.

:class:`MCPRetriever` is the real one: it spawns the sibling ``mcp-server-py`` over stdio and calls
its ``search_knowledge`` tool, so retrieval genuinely flows through the **Model Context Protocol**.
:class:`StaticRetriever` is an offline in-memory fake for fast graph tests.
"""

from __future__ import annotations

import json
import re
import sys
from typing import Any, Protocol, runtime_checkable

from mcp import ClientSession
from mcp.client.stdio import StdioServerParameters, stdio_client

_TOKEN_RE = re.compile(r"[a-z0-9]+")


@runtime_checkable
class Retriever(Protocol):
    async def search(self, query: str, k: int = 4) -> list[dict[str, Any]]: ...


class StaticRetriever:
    """In-memory keyword retriever over canned docs — no server, for tests/offline."""

    def __init__(self, documents: list[dict[str, Any]]) -> None:
        self._docs = documents

    async def search(self, query: str, k: int = 4) -> list[dict[str, Any]]:
        q = set(_TOKEN_RE.findall(query.lower()))
        scored = []
        for d in self._docs:
            tokens = set(_TOKEN_RE.findall(d.get("text", "").lower()))
            overlap = len(q & tokens)
            if overlap:
                scored.append((overlap, d))
        scored.sort(key=lambda x: x[0], reverse=True)
        return [{**d, "score": float(s)} for s, d in scored[:k]]


def _parse_tool_result(result: Any) -> list[dict[str, Any]]:
    """FastMCP wraps a list return as structuredContent={'result': [...]}; else parse JSON text."""
    sc = getattr(result, "structuredContent", None)
    if isinstance(sc, dict) and "result" in sc:
        return sc["result"]
    if isinstance(sc, list):
        return sc
    text = "".join(getattr(c, "text", "") for c in getattr(result, "content", []))
    return json.loads(text) if text else []


class MCPRetriever:
    """Async context manager holding a live MCP client session to the knowledge server.

    ```python
    async with MCPRetriever() as r:
        hits = await r.search("what is corrective rag?", k=4)
    ```
    """

    def __init__(
        self,
        command: str = sys.executable,
        args: tuple[str, ...] = ("-m", "mcp_knowledge.server"),
    ) -> None:
        self._params = StdioServerParameters(command=command, args=list(args))
        self._stdio_cm: Any = None
        self._session_cm: Any = None
        self.session: ClientSession | None = None

    async def __aenter__(self) -> MCPRetriever:
        self._stdio_cm = stdio_client(self._params)
        read, write = await self._stdio_cm.__aenter__()
        self._session_cm = ClientSession(read, write)
        self.session = await self._session_cm.__aenter__()
        await self.session.initialize()
        return self

    async def __aexit__(self, *exc: object) -> None:
        if self._session_cm is not None:
            await self._session_cm.__aexit__(*exc)
        if self._stdio_cm is not None:
            await self._stdio_cm.__aexit__(*exc)

    async def search(self, query: str, k: int = 4) -> list[dict[str, Any]]:
        if self.session is None:
            raise RuntimeError("MCPRetriever must be used as an async context manager")
        result = await self.session.call_tool("search_knowledge", {"query": query, "k": k})
        return _parse_tool_result(result)
