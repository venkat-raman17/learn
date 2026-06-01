# System-design numbers

For back-of-envelope estimates. All approximate — the point is orders of magnitude.

## Latency (every engineer should know)

| Operation | ~Time |
| --- | --- |
| L1 cache reference | ~1 ns |
| Branch mispredict | ~3 ns |
| L2 cache reference | ~4 ns |
| Mutex lock/unlock | ~17 ns |
| Main memory reference | ~100 ns |
| Compress 1 KB (zippy) | ~2 µs |
| Read 1 MB sequentially from memory | ~3 µs |
| SSD random read | ~16 µs |
| Round trip in same datacenter | ~0.5 ms |
| Read 1 MB sequentially from SSD | ~50 µs–1 ms |
| Disk seek (HDD) | ~3–10 ms |
| Read 1 MB sequentially from disk | ~5–20 ms |
| Round trip CA ↔ Netherlands | ~150 ms |

Takeaways: memory is ~100× faster than SSD, ~10⁴× faster than disk seek; cross-region is ~10⁵× a memory ref. Cache and keep data close.

## Powers of two (storage)

| 2^x | ≈ | Name |
| --- | --- | --- |
| 2^10 | 1 thousand | 1 KB |
| 2^20 | 1 million | 1 MB |
| 2^30 | 1 billion | 1 GB |
| 2^40 | 1 trillion | 1 TB |
| 2^50 | 1 quadrillion | 1 PB |

## Time & QPS math

- **Seconds/day** ≈ 86,400 ≈ **10⁵** (handy: divide daily counts by 10⁵ for avg/sec).
- **Avg QPS** = (DAU × actions/user/day) / 10⁵. **Peak** ≈ 2–3× avg.
- **Storage/day** = writes/day × bytes/write × replication. Multiply by retention (days) for total.
- 1 day ≈ 86.4k s · 1 month ≈ 2.5M s · 1 year ≈ 31.5M s ≈ **π × 10⁷**.

## Availability ("nines")

| SLA | Downtime/year |
| --- | --- |
| 99% | ~3.65 days |
| 99.9% | ~8.76 hours |
| 99.99% | ~52 minutes |
| 99.999% | ~5.26 minutes |

## Rough capacity figures (order of magnitude)

| Thing | Ballpark |
| --- | --- |
| App server throughput | ~10k–100k QPS (simple), far less for heavy work |
| Redis / in-memory cache | ~100k+ ops/s per node |
| Single SQL primary (writes) | ~thousands/s before sharding |
| Kafka partition | ~10s of MB/s; scale by partitions |
| Disk (HDD) seq read | ~100–200 MB/s · SSD ~500 MB/s–GB/s |
| Inter-DC bandwidth | plan in Gbps; don't ship TBs synchronously |

## Typical sizes
- A tweet/post text: ~300 B–1 KB · A web page: ~1–3 MB · A photo: ~200 KB–2 MB · A minute of 1080p video: ~50–100 MB · A UUID: 16 B · An int64/timestamp: 8 B.
