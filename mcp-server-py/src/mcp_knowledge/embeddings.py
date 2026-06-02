"""Text embeddings.

The default :class:`TfidfEmbedder` is a from-scratch TF-IDF vectoriser — real, deterministic,
dependency-light retrieval that runs anywhere with no model download (the same "teach the
mechanism" approach as the backend metrics kata). It implements the :class:`Embedder` seam, so you
can swap in ``sentence-transformers`` / OpenAI / Voyage embeddings in one place without touching
the vector store or the MCP server.
"""

from __future__ import annotations

import math
import re
from collections import Counter
from typing import Protocol

import numpy as np

_TOKEN_RE = re.compile(r"[a-z0-9]+")


def tokenize(text: str) -> list[str]:
    """Lowercase + split on non-alphanumeric. Good enough for keyword-semantic retrieval."""
    return _TOKEN_RE.findall(text.lower())


class Embedder(Protocol):
    """The swap seam. A real provider would call a model/API in ``embed``."""

    dim: int

    def fit(self, documents: list[str]) -> None: ...

    def embed(self, text: str) -> np.ndarray: ...


class TfidfEmbedder:
    """TF-IDF with smoothed IDF and L2-normalised vectors (so cosine == dot product).

    ``fit`` learns the vocabulary + inverse-document-frequency from the corpus; ``embed`` projects
    any text (including a query) into that fitted vocabulary space. Out-of-vocabulary terms are
    ignored, exactly like a real fitted vectoriser.
    """

    def __init__(self) -> None:
        self.vocab: dict[str, int] = {}
        self.idf: np.ndarray = np.zeros(0)
        self.dim = 0
        self._fitted = False

    def fit(self, documents: list[str]) -> None:
        # Build vocabulary.
        vocab: dict[str, int] = {}
        doc_tokens = [tokenize(d) for d in documents]
        for tokens in doc_tokens:
            for tok in tokens:
                if tok not in vocab:
                    vocab[tok] = len(vocab)

        n_docs = max(len(documents), 1)
        df = np.zeros(len(vocab))
        for tokens in doc_tokens:
            for tok in set(tokens):
                df[vocab[tok]] += 1

        # Smoothed IDF: log((1 + N) / (1 + df)) + 1  (sklearn-style; never zero/negative).
        self.idf = np.log((1 + n_docs) / (1 + df)) + 1.0
        self.vocab = vocab
        self.dim = len(vocab)
        self._fitted = True

    def embed(self, text: str) -> np.ndarray:
        if not self._fitted:
            raise RuntimeError("TfidfEmbedder.embed called before fit()")
        vec = np.zeros(self.dim)
        counts = Counter(tok for tok in tokenize(text) if tok in self.vocab)
        if not counts:
            return vec  # all-OOV → zero vector (cosine 0 against everything)
        max_tf = max(counts.values())
        for tok, c in counts.items():
            idx = self.vocab[tok]
            tf = 0.5 + 0.5 * (c / max_tf)  # augmented term frequency
            vec[idx] = tf * self.idf[idx]
        norm = math.sqrt(float(vec @ vec))
        return vec / norm if norm > 0 else vec
