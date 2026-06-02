"""The LLM seam.

The Corrective-RAG graph needs three LLM-ish decisions: **grade** a chunk's relevance, **rewrite**
a query, and **generate** a grounded answer. :class:`FakeLLM` implements all three deterministically
so the whole pipeline runs and tests pass with **zero API keys**. :class:`ClaudeLLM` is the one-line
swap to real Claude (``uv sync --extra claude`` + ``ANTHROPIC_API_KEY``).
"""

from __future__ import annotations

import os
import re
from typing import Protocol, runtime_checkable

_TOKEN_RE = re.compile(r"[a-z0-9]+")
_STOPWORDS = {
    "the", "a", "an", "is", "are", "was", "were", "of", "to", "in", "on", "for", "and", "or",
    "what", "how", "does", "do", "did", "why", "when", "which", "who", "that", "this", "it",
    "its", "as", "by", "with", "about", "can", "i", "you", "we", "be", "into",
}


def _content_tokens(text: str) -> set[str]:
    return {t for t in _TOKEN_RE.findall(text.lower()) if t not in _STOPWORDS and len(t) > 1}


@runtime_checkable
class LLM(Protocol):
    def grade_relevance(self, question: str, doc_text: str) -> bool: ...

    def rewrite_query(self, question: str, previous_query: str) -> str: ...

    def generate_answer(self, question: str, contexts: list[dict]) -> str: ...


class FakeLLM:
    """Deterministic stand-in. Grading = content-word overlap; rewriting = keyword extraction;
    generation = extractive summary with citations. No network, fully reproducible."""

    def __init__(self, min_overlap: int = 2) -> None:
        self.min_overlap = min_overlap

    def grade_relevance(self, question: str, doc_text: str) -> bool:
        shared = _content_tokens(question) & _content_tokens(doc_text)
        return len(shared) >= self.min_overlap

    def rewrite_query(self, question: str, previous_query: str) -> str:
        # Drop stopwords/question words to a keyword query; if that doesn't change things, broaden
        # by adding generic intent terms. This deterministically alters retrieval on retry.
        keywords = [t for t in _TOKEN_RE.findall(question.lower()) if t not in _STOPWORDS]
        rewritten = " ".join(keywords)
        if rewritten and rewritten != previous_query.strip().lower():
            return rewritten
        return f"{rewritten} concept definition overview".strip()

    def generate_answer(self, question: str, contexts: list[dict]) -> str:
        if not contexts:
            return "I couldn't find relevant information in the knowledge base to answer that."
        # Extractive: take the first sentence of each context, cite the source title.
        bullets = []
        seen: set[str] = set()
        for ctx in contexts:
            title = ctx.get("title", "source")
            sentence = ctx.get("text", "").split(". ")[0].strip().rstrip(".")
            if sentence and sentence not in seen:
                seen.add(sentence)
                bullets.append(f"- {sentence}. [{title}]")
        sources = ", ".join(sorted({c.get("title", "source") for c in contexts}))
        body = "\n".join(bullets)
        return f"Based on the knowledge base:\n{body}\n\nSources: {sources}"


class ClaudeLLM:
    """Real Claude via the Anthropic SDK. Lazily imported so the package works without the extra.

    Set the model with ``AGENTIC_MODEL`` (no default is hardcoded) and ``ANTHROPIC_API_KEY``.
    """

    def __init__(self, model: str | None = None) -> None:
        try:
            from anthropic import Anthropic
        except ImportError as e:  # pragma: no cover - exercised only with the extra installed
            raise RuntimeError("Install the 'claude' extra: uv sync --extra claude") from e
        self.model = model or os.environ.get("AGENTIC_MODEL")
        if not self.model:
            raise RuntimeError("Set AGENTIC_MODEL (e.g. a Claude model id) to use ClaudeLLM.")
        self._client = Anthropic()

    def _complete(self, prompt: str, max_tokens: int = 512) -> str:  # pragma: no cover
        msg = self._client.messages.create(
            model=self.model,
            max_tokens=max_tokens,
            messages=[{"role": "user", "content": prompt}],
        )
        return "".join(block.text for block in msg.content if block.type == "text").strip()

    def grade_relevance(self, question: str, doc_text: str) -> bool:  # pragma: no cover
        out = self._complete(
            f"Is the following document relevant to answering the question? "
            f"Answer only 'yes' or 'no'.\n\nQuestion: {question}\n\nDocument: {doc_text}"
        )
        return out.lower().startswith("y")

    def rewrite_query(self, question: str, previous_query: str) -> str:  # pragma: no cover
        return self._complete(
            f"Rewrite this question as a better search query (keywords only, no punctuation). "
            f"Return only the query.\n\nQuestion: {question}\nPrevious query: {previous_query}"
        )

    def generate_answer(self, question: str, contexts: list[dict]) -> str:  # pragma: no cover
        if not contexts:
            return "I couldn't find relevant information in the knowledge base to answer that."
        joined = "\n\n".join(f"[{c.get('title')}] {c.get('text')}" for c in contexts)
        return self._complete(
            f"Answer the question using only the context. Cite sources as [Title].\n\n"
            f"Context:\n{joined}\n\nQuestion: {question}"
        )
