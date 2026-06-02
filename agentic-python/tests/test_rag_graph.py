"""The corrective loop: when the first retrieval is irrelevant, the graph rewrites the query and
retries before generating — proving the conditional self-correction edge works."""

from agentic.llm import FakeLLM
from agentic.main import answer_question
from agentic.rag_graph import build_graph, initial_state
from agentic.retriever import StaticRetriever

RELEVANT = {
    "doc_id": "corrective-rag",
    "title": "Corrective RAG",
    "text": "corrective rag adds a grading and self correction loop to check relevance",
}
IRRELEVANT = {"doc_id": "misc", "title": "Unrelated", "text": "parking lots and elevators"}


class ScriptedRetriever:
    """Returns an irrelevant doc on the first call, the relevant one afterwards."""

    def __init__(self) -> None:
        self.calls = 0

    async def search(self, query: str, k: int = 4) -> list[dict]:
        self.calls += 1
        return [IRRELEVANT] if self.calls == 1 else [RELEVANT]


async def test_happy_path_no_correction():
    result = await answer_question(
        "what is corrective rag grading relevance", StaticRetriever([RELEVANT]), FakeLLM()
    )
    assert "Corrective RAG" in result["answer"]
    assert not any("transform_query" in s for s in result["trace"])  # no correction needed


async def test_corrective_loop_rewrites_then_succeeds():
    retriever = ScriptedRetriever()
    result = await answer_question(
        "corrective rag grading relevance", retriever, FakeLLM(), max_attempts=2
    )
    assert retriever.calls == 2  # retried after the first miss
    assert any("transform_query" in s for s in result["trace"])
    assert "Corrective RAG" in result["answer"]


async def test_gives_up_gracefully_after_retries():
    # Nothing relevant ever retrieved → bounded retries → graceful not-found (terminates).
    result = await answer_question(
        "completely unrelated quantum gardening",
        StaticRetriever([IRRELEVANT]),
        FakeLLM(),
        max_attempts=2,
    )
    assert "couldn't find" in result["answer"]
    assert result["attempts"] <= 2


async def test_graph_is_compilable_object():
    graph = build_graph(StaticRetriever([RELEVANT]), FakeLLM())
    state = await graph.ainvoke(initial_state("what is corrective rag grading"))
    assert state["answer"]
