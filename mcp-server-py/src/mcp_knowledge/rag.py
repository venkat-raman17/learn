"""The knowledge base: ties chunking + embeddings + vector store into a search API.

This is the *retrieval* half of RAG (the "R"). Generation (the "A"+"G") lives in the agentic-python
client, which reaches this through the MCP ``search_knowledge`` tool.
"""

from __future__ import annotations

from dataclasses import dataclass
from typing import Any

from .chunking import chunk_document
from .corpus import DEFAULT_CORPUS, Document
from .embeddings import Embedder, TfidfEmbedder
from .vector_store import InMemoryVectorStore, Record


@dataclass
class SearchHit:
    chunk_id: str
    doc_id: str
    title: str
    text: str
    score: float

    def to_dict(self) -> dict[str, Any]:
        return {
            "chunk_id": self.chunk_id,
            "doc_id": self.doc_id,
            "title": self.title,
            "text": self.text,
            "score": round(self.score, 4),
        }


class KnowledgeBase:
    def __init__(self, embedder: Embedder | None = None) -> None:
        self.embedder: Embedder = embedder or TfidfEmbedder()
        self.store = InMemoryVectorStore()
        self._titles: dict[str, str] = {}

    @classmethod
    def from_default_corpus(cls, embedder: Embedder | None = None) -> KnowledgeBase:
        kb = cls(embedder)
        kb.ingest(DEFAULT_CORPUS)
        return kb

    def ingest(self, documents: list[Document]) -> None:
        # Chunk everything first so the embedder is fitted on the chunk corpus it will serve.
        chunks = [
            c for doc in documents for c in chunk_document(doc.id, doc.title, doc.text)
        ]
        self.embedder.fit([c.text for c in chunks])
        for c in chunks:
            chunk_id = f"{c.doc_id}#{c.index}"
            self._titles[c.doc_id] = c.title
            self.store.add(
                Record(
                    id=chunk_id,
                    vector=self.embedder.embed(c.text),
                    payload={"doc_id": c.doc_id, "title": c.title, "text": c.text},
                )
            )

    def search(self, query: str, k: int = 4) -> list[SearchHit]:
        results = self.store.search(self.embedder.embed(query), k)
        return [
            SearchHit(
                chunk_id=r.id,
                doc_id=r.payload["doc_id"],
                title=r.payload["title"],
                text=r.payload["text"],
                score=r.score,
            )
            for r in results
        ]

    def documents(self) -> list[dict[str, str]]:
        return [{"doc_id": d, "title": t} for d, t in sorted(self._titles.items())]
