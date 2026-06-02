from mcp_knowledge.embeddings import TfidfEmbedder, tokenize
from mcp_knowledge.rag import KnowledgeBase
from mcp_knowledge.vector_store import InMemoryVectorStore, Record


def test_tokenize_lowercases_and_splits():
    assert tokenize("Corrective-RAG, v2!") == ["corrective", "rag", "v2"]


def test_embedder_query_lands_near_relevant_doc():
    embedder = TfidfEmbedder()
    docs = [
        "corrective rag adds a self correction grading loop",
        "parking lots and elevators are unrelated topics",
    ]
    embedder.fit(docs)
    q = embedder.embed("what is corrective rag grading")
    # cosine similarity (vectors are L2-normalised) — relevant doc must score higher
    sims = [float(q @ embedder.embed(d)) for d in docs]
    assert sims[0] > sims[1]


def test_vector_store_topk_ordering():
    store = InMemoryVectorStore()
    import numpy as np

    store.add(Record("a", np.array([1.0, 0.0])))
    store.add(Record("b", np.array([0.0, 1.0])))
    store.add(Record("c", np.array([0.7071, 0.7071])))
    hits = store.search(np.array([1.0, 0.0]), k=2)
    assert [h.id for h in hits] == ["a", "c"]
    assert hits[0].score > hits[1].score


def test_knowledge_base_retrieves_relevant_chunk_in_topk():
    kb = KnowledgeBase.from_default_corpus()
    hits = kb.search("how does corrective RAG correct itself", k=3)
    assert hits, "expected at least one hit"
    # relevant doc is in the top-k (keyword retrievers can tie on near-synonyms)
    assert "corrective-rag" in {h.doc_id for h in hits}
    assert all(h.score >= 0 for h in hits)


def test_retrieval_ranks_unambiguous_query_first():
    kb = KnowledgeBase.from_default_corpus()
    assert kb.search("what is the model context protocol", k=1)[0].doc_id == "mcp"
    assert kb.search("langgraph state machine nodes and edges", k=1)[0].doc_id == "langgraph"
    assert kb.search("cosine similarity vector embeddings search", k=1)[0].doc_id == "embeddings"


def test_knowledge_base_lists_documents():
    kb = KnowledgeBase.from_default_corpus()
    ids = {d["doc_id"] for d in kb.documents()}
    assert {"rag", "corrective-rag", "mcp", "langgraph"} <= ids
