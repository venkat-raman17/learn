"""End-to-end MCP test: spawn the server over stdio and call its tools as a real client would."""

from __future__ import annotations

import sys

from mcp import ClientSession
from mcp.client.stdio import StdioServerParameters, stdio_client


def _server_params() -> StdioServerParameters:
    # Use the same interpreter so the installed package resolves.
    return StdioServerParameters(command=sys.executable, args=["-m", "mcp_knowledge.server"])


def _all_text(result) -> str:
    return " ".join(getattr(c, "text", "") for c in result.content)


async def test_lists_and_calls_tools_over_stdio():
    async with stdio_client(_server_params()) as (read, write):
        async with ClientSession(read, write) as session:
            await session.initialize()

            tools = {t.name for t in (await session.list_tools()).tools}
            assert {"search_knowledge", "list_documents"} <= tools

            result = await session.call_tool(
                "search_knowledge", {"query": "what is the model context protocol", "k": 2}
            )
            assert not result.isError
            assert "Model Context Protocol" in _all_text(result)

            docs = await session.call_tool("list_documents", {})
            assert "langgraph" in _all_text(docs)
