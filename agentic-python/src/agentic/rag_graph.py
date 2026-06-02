"""Corrective / Agentic RAG as a LangGraph state machine.

```
        ┌────────────┐
START ─▶│  retrieve   │◀────────────┐
        └─────┬──────┘             │
              ▼                    │ (rewrite & retry, bounded)
        ┌────────────┐   no relevant│
        │   grade     ├─────────────┘
        └─────┬──────┘
   relevant   │ or out of retries
              ▼
        ┌────────────┐
        │  generate   │─▶ END
        └────────────┘
```

The conditional edge after **grade** is the corrective loop: if no retrieved chunk is relevant and
we still have retries, **transform the query** and retrieve again; otherwise **generate** (with the
relevant chunks, or a graceful "not found"). The retry budget guarantees termination.
"""

from __future__ import annotations

from typing import Any, TypedDict

from langgraph.graph import END, START, StateGraph

from .llm import LLM
from .retriever import Retriever


class RagState(TypedDict, total=False):
    question: str
    query: str                      # current (possibly rewritten) search query
    documents: list[dict[str, Any]]  # last retrieval
    relevant: list[dict[str, Any]]   # graded-relevant subset
    answer: str
    attempts: int
    max_attempts: int
    trace: list[str]                 # human-readable step log (lightweight observability)


def initial_state(question: str, max_attempts: int = 2) -> RagState:
    return {
        "question": question,
        "query": question,
        "documents": [],
        "relevant": [],
        "answer": "",
        "attempts": 0,
        "max_attempts": max_attempts,
        "trace": [],
    }


def build_graph(retriever: Retriever, llm: LLM, k: int = 4):
    """Compile a Corrective-RAG graph bound to a retriever + LLM."""

    async def retrieve(state: RagState) -> RagState:
        docs = await retriever.search(state["query"], k)
        return {
            "documents": docs,
            "trace": state["trace"] + [f"retrieve(query={state['query']!r}) -> {len(docs)} docs"],
        }

    async def grade(state: RagState) -> RagState:
        relevant = [
            d
            for d in state["documents"]
            if llm.grade_relevance(state["question"], d.get("text", ""))
        ]
        attempts = state["attempts"] + 1
        return {
            "relevant": relevant,
            "attempts": attempts,
            "trace": state["trace"]
            + [f"grade -> {len(relevant)}/{len(state['documents'])} relevant (attempt {attempts})"],
        }

    async def transform_query(state: RagState) -> RagState:
        new_query = llm.rewrite_query(state["question"], state["query"])
        return {
            "query": new_query,
            "trace": state["trace"] + [f"transform_query -> {new_query!r}"],
        }

    async def generate(state: RagState) -> RagState:
        answer = llm.generate_answer(state["question"], state["relevant"])
        return {"answer": answer, "trace": state["trace"] + ["generate"]}

    def decide(state: RagState) -> str:
        if state["relevant"]:
            return "generate"
        if state["attempts"] < state["max_attempts"]:
            return "transform_query"
        return "generate"  # out of retries → best-effort (empty relevant → graceful not-found)

    graph = StateGraph(RagState)
    graph.add_node("retrieve", retrieve)
    graph.add_node("grade", grade)
    graph.add_node("transform_query", transform_query)
    graph.add_node("generate", generate)

    graph.add_edge(START, "retrieve")
    graph.add_edge("retrieve", "grade")
    graph.add_conditional_edges(
        "grade", decide, {"generate": "generate", "transform_query": "transform_query"}
    )
    graph.add_edge("transform_query", "retrieve")
    graph.add_edge("generate", END)
    return graph.compile()
