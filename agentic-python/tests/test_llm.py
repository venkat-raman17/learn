from agentic.llm import FakeLLM


def test_grade_relevance_overlap():
    llm = FakeLLM(min_overlap=2)
    q = "what is corrective rag grading"
    assert llm.grade_relevance(q, "corrective rag adds a grading loop") is True
    assert llm.grade_relevance(q, "parking lots and elevators are unrelated") is False


def test_rewrite_drops_stopwords_and_changes_query():
    llm = FakeLLM()
    question = "What is the model context protocol?"
    rewritten = llm.rewrite_query(question, question)
    # stopwords/question words removed
    assert "what" not in rewritten and "the" not in rewritten.split()
    assert "model" in rewritten and "protocol" in rewritten


def test_generate_cites_sources_and_handles_empty():
    llm = FakeLLM()
    ctx = [{"title": "MCP", "text": "The Model Context Protocol connects tools. Open standard."}]
    answer = llm.generate_answer("what is mcp", ctx)
    assert "[MCP]" in answer and "Sources: MCP" in answer

    assert "couldn't find" in llm.generate_answer("anything", [])
