"""A small built-in corpus so the server is useful out of the box (no external data needed).

The documents describe the very concepts this project demonstrates, so the agentic RAG demo is
self-explanatory: ask "what is corrective RAG?" and the pipeline retrieves + answers from here.
"""

from __future__ import annotations

from dataclasses import dataclass


@dataclass
class Document:
    id: str
    title: str
    text: str


DEFAULT_CORPUS: list[Document] = [
    Document(
        "rag",
        "Retrieval-Augmented Generation (RAG)",
        """RAG grounds a language model in external knowledge instead of relying only on its
parameters. At query time the system retrieves relevant chunks from a knowledge base and passes them
to the model as context, so answers cite real sources and stay current without retraining.

A RAG pipeline has three stages: index (chunk documents, embed them, store the vectors), retrieve
(embed the query, find the nearest chunks), and generate (prompt the model with the query plus the
retrieved context). Retrieval quality usually matters more than the model choice.""",
    ),
    Document(
        "corrective-rag",
        "Corrective / Agentic RAG",
        """Corrective RAG (CRAG) adds a self-correction loop on top of plain RAG. After retrieval, a
grader judges whether the retrieved chunks are actually relevant to the question. If they are, the
model generates the answer. If they are not, the agent transforms the query (rewrites it) and
retrieves again, or falls back to another source, before answering.

Agentic RAG expresses this as a graph of steps with conditional edges: retrieve, grade documents,
decide, optionally rewrite-and-retry, then generate. This avoids confidently answering from
irrelevant context, which is the most common RAG failure mode.""",
    ),
    Document(
        "mcp",
        "Model Context Protocol (MCP)",
        """The Model Context Protocol is an open standard that lets AI applications connect to tools
and data through a uniform interface. An MCP server exposes capabilities — tools (callable
functions), resources (readable data), and prompts — and any MCP-aware client (an agent, an IDE, a
chat app) can discover and call them.

MCP decouples the agent from its integrations: you write a tool once as an MCP server and every
client can use it. In this project the knowledge base is an MCP server exposing a search_knowledge
tool, and the LangGraph agent is the MCP client that calls it to retrieve context.""",
    ),
    Document(
        "langgraph",
        "LangGraph",
        """LangGraph models an agent as a state machine: a graph of nodes that read and write
a shared state object, connected by normal and conditional edges. Unlike a fixed chain, a
graph can loop, branch, and retry, which is exactly what agentic workflows need.

A Corrective RAG agent maps cleanly onto LangGraph: each step (retrieve, grade, transform query,
generate) is a node, and a conditional edge after grading decides whether to generate or to rewrite
the query and retrieve again, with a retry budget to guarantee termination.""",
    ),
    Document(
        "embeddings",
        "Embeddings and Vector Search",
        """An embedding maps text to a vector so that semantically similar text lands near in space.
Retrieval embeds the query and finds the nearest stored chunk vectors, typically by cosine
similarity. Normalising vectors to unit length makes cosine similarity a single dot product.

TF-IDF is a classic, transparent embedding: it weights terms by how often they appear in a
document versus how rare they are across the corpus. Dense neural embeddings
(sentence-transformers, OpenAI, Voyage) capture more meaning but need a model. The retrieval
interface is the same either way, so the two are interchangeable behind one seam.""",
    ),
    Document(
        "agents",
        "LLM Agents, Tools, and the ReAct Loop",
        """An agent is a language model that can take actions through tools, observe the
results, and decide what to do next. The ReAct pattern interleaves reasoning and acting:
the model thinks, calls a tool, reads the observation, and repeats until it can answer.

Guardrails matter: bound the number of steps, validate tool inputs, and judge the final answer for
groundedness against the retrieved evidence. Agentic RAG is a focused agent whose main tool is
retrieval and whose loop is the grade-and-correct cycle.""",
    ),
]
