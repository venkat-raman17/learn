"""CLI: answer a question with Corrective RAG, retrieving via the MCP knowledge server.

```bash
uv run python -m agentic.main "what is corrective rag and how does it work?"
```

Runs fully **keyless** (FakeLLM + the local MCP server). Swap in real Claude with the ``claude``
extra and ``ANTHROPIC_API_KEY`` — see the README.
"""

from __future__ import annotations

import asyncio
import sys

from .llm import LLM, FakeLLM
from .rag_graph import build_graph, initial_state
from .retriever import MCPRetriever, Retriever


async def answer_question(
    question: str, retriever: Retriever, llm: LLM, *, k: int = 4, max_attempts: int = 2
) -> dict:
    """Run the Corrective-RAG graph end to end and return the final state."""
    graph = build_graph(retriever, llm, k)
    return await graph.ainvoke(initial_state(question, max_attempts))


async def _run(question: str) -> None:
    llm = FakeLLM()
    # MCPRetriever spawns the sibling knowledge server over stdio — retrieval flows through MCP.
    async with MCPRetriever() as retriever:
        result = await answer_question(question, retriever, llm)

    print(f"agentic-python · Corrective RAG over MCP\n\nQ: {question}\n")
    print(result["answer"])
    print("\n--- trace ---")
    for step in result["trace"]:
        print(f"  {step}")


def main() -> None:
    question = " ".join(sys.argv[1:]).strip() or "What is corrective RAG and how does it work?"
    asyncio.run(_run(question))


if __name__ == "__main__":
    main()
