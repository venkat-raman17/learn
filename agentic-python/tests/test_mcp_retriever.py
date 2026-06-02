"""Integration: the agent retrieves through the REAL MCP server (spawned over stdio)."""

from agentic.llm import FakeLLM
from agentic.main import answer_question
from agentic.retriever import MCPRetriever


async def test_search_via_mcp_server():
    async with MCPRetriever() as retriever:
        hits = await retriever.search("what is the model context protocol", k=3)
    assert hits
    assert any(h["doc_id"] == "mcp" for h in hits)
    assert all("text" in h and "title" in h for h in hits)


async def test_end_to_end_corrective_rag_over_mcp():
    async with MCPRetriever() as retriever:
        result = await answer_question(
            "what is the model context protocol", retriever, FakeLLM()
        )
    assert result["answer"]
    assert "Model Context Protocol" in result["answer"] or "MCP" in result["answer"]
    assert any(step.startswith("retrieve") for step in result["trace"])
