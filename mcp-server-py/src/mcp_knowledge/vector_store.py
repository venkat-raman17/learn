"""In-memory vector store with cosine-similarity top-k search.

The concept behind FAISS / Chroma / pgvector, kept to ~30 lines of numpy. Vectors are assumed L2-
normalised (the embedder does this), so cosine similarity is a single matrix-vector dot product.
Swap this class for a real ANN index when the corpus outgrows a brute-force scan.
"""

from __future__ import annotations

from dataclasses import dataclass, field
from typing import Any

import numpy as np


@dataclass
class Record:
    id: str
    vector: np.ndarray
    payload: dict[str, Any] = field(default_factory=dict)


@dataclass
class ScoredRecord:
    id: str
    score: float
    payload: dict[str, Any]


class InMemoryVectorStore:
    def __init__(self) -> None:
        self._records: list[Record] = []
        self._matrix: np.ndarray | None = None  # cached (n, dim) stack, invalidated on add

    def add(self, record: Record) -> None:
        self._records.append(record)
        self._matrix = None

    def __len__(self) -> int:
        return len(self._records)

    def search(self, query: np.ndarray, k: int = 4) -> list[ScoredRecord]:
        if not self._records:
            return []
        if self._matrix is None:
            self._matrix = np.vstack([r.vector for r in self._records])

        scores = self._matrix @ query  # cosine, since everything is L2-normalised
        k = min(k, len(self._records))
        # argpartition for top-k, then sort just those k descending.
        top_idx = np.argpartition(-scores, k - 1)[:k]
        top_idx = top_idx[np.argsort(-scores[top_idx])]
        return [
            ScoredRecord(self._records[i].id, float(scores[i]), self._records[i].payload)
            for i in top_idx
        ]
