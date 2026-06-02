from agentic.llm import FakeLLM
from agentic.main import answer_question
from agentic.retriever import StaticRetriever

DOCS = [
    {"doc_id": "corrective-rag", "title": "Corrective RAG",
     "text": "corrective rag adds a grading and self correction loop to check relevance"},
    {"doc_id": "misc", "title": "Unrelated", "text": "parking lots and elevators"},
]


async def test_graph_answers_with_static_retriever():
    result = await answer_question(
        "what is corrective rag grading", StaticRetriever(DOCS), FakeLLM()
    )
    assert result["answer"]
    assert "Corrective RAG" in result["answer"]  # cites the relevant source
    assert any(step.startswith("generate") for step in result["trace"])
