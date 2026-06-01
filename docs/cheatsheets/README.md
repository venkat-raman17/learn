# Cheat sheets

Quick reference for interview day. *Fuller sheets filled per phase; starters below.*

## Contents
- [ ] `big-o.md` — complexity per data-structure operation (filled in Phase 1)
- [ ] `pattern-triggers.md` — "if you see X, reach for pattern Y" (filled in Phase 2)
- [ ] `system-design-numbers.md` — latency/throughput/capacity numbers (filled in Phase 4)
- [ ] `design-checklists.md` — LLD & HLD round checklists (filled in Phases 3–4)

## Big-O quick table (starter)
| Structure | Access | Search | Insert | Delete |
| --- | --- | --- | --- | --- |
| Dynamic array | O(1) | O(n) | O(1)* | O(n) |
| Linked list | O(n) | O(n) | O(1) | O(1) |
| Hash map | — | O(1)* | O(1)* | O(1)* |
| BST (balanced) | O(log n) | O(log n) | O(log n) | O(log n) |
| Heap | — | O(n) | O(log n) | O(log n) |

`*` amortized / average.

## Latency numbers every engineer should know (starter, order-of-magnitude)
| Operation | ~Time |
| --- | --- |
| L1 cache reference | ~1 ns |
| Main memory reference | ~100 ns |
| SSD random read | ~16 µs |
| Read 1 MB sequentially from memory | ~3 µs |
| Round trip within same datacenter | ~0.5 ms |
| Read 1 MB from SSD | ~50 µs–1 ms |
| Disk seek (HDD) | ~3–10 ms |
| Round trip CA ↔ Netherlands | ~150 ms |

Use these for back-of-envelope estimates in HLD. (Numbers are approximate, for reasoning — not exact.)
