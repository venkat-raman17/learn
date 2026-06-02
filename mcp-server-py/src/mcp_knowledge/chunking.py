"""Document chunking.

RAG retrieves *chunks*, not whole documents, so each hit is small and on-topic. This splits on
blank lines (paragraph boundaries) then packs paragraphs up to a target word budget with a small
overlap — the pragmatic default behind "semantic chunking". A production system would chunk on
token counts and respect headings.
"""

from __future__ import annotations

from dataclasses import dataclass


@dataclass
class Chunk:
    doc_id: str
    title: str
    text: str
    index: int


def chunk_document(
    doc_id: str, title: str, text: str, max_words: int = 90, overlap_words: int = 15
) -> list[Chunk]:
    paragraphs = [p.strip() for p in text.split("\n\n") if p.strip()]
    chunks: list[Chunk] = []
    current: list[str] = []
    count = 0

    def flush() -> None:
        nonlocal current, count
        if current:
            chunks.append(Chunk(doc_id, title, " ".join(current).strip(), len(chunks)))
            # carry an overlap tail for context continuity
            tail = " ".join(current).split()[-overlap_words:]
            current = tail[:]
            count = len(tail)

    for para in paragraphs:
        words = para.split()
        if count + len(words) > max_words and current:
            flush()
        current.append(para)
        count += len(words)
    # final flush without keeping an overlap tail
    if current:
        chunks.append(Chunk(doc_id, title, " ".join(current).strip(), len(chunks)))
    return chunks
