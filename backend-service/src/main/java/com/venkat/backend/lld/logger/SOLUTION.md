# Logging Framework — reference design

---

## Approach

**Entity model**

The framework is built around three orthogonal concerns deliberately kept apart:

| Concern | Entity | Role |
|---|---|---|
| What happened | `LogRecord` | Immutable value object; created once, read many times |
| Where it goes | `Appender` | Output destination; many can be attached per logger |
| How it looks | `Formatter` | Converts `LogRecord` to a string; injected into each appender |
| When it matters | `Level` enum | Ordinal-ordered gate checked before any work is done |
| Who triggers it | `Logger` | Owns the gate check and fan-out loop |
| How to obtain one | `LoggerFactory` | Named singleton registry |

**Design patterns used**

- **Strategy** (primary) — `Appender` and `Formatter` are interfaces. The logger delegates to whichever strategies are registered; swapping a `ConsoleAppender` for a `FileAppender` requires zero changes to `Logger`.
- **Template Method** — `AbstractAppender` handles the format call and any appender-level level gate, delegating only raw I/O (`doAppend`) to subclasses. This avoids duplicating boilerplate across every appender.
- **Factory / Registry** — `LoggerFactory` acts as a concurrent map from name to `Logger`. Callers always get the same instance for the same name, preventing duplicate appender registration across a codebase.
- **Chain of Responsibility** (rejected for primary path) — Modelling levels as a handler chain adds complexity and makes it harder to broadcast one record to multiple appenders at different levels; the flat-list approach is simpler and more testable here. CoR is a better fit when routing logic is conditional and appenders should not all receive every record.

---

## Class design

| Class / Interface | Responsibility |
|---|---|
| `Level` (enum) | Four constants `DEBUG INFO WARN ERROR`; ordinal encodes severity |
| `LogRecord` | Immutable snapshot: `timestamp`, `level`, `loggerName`, `message` |
| `Formatter` (interface) | Single method `String format(LogRecord)` — format strategy |
| `SimpleFormatter` | Default implementation: `"[timestamp] LEVEL loggerName — message"` |
| `Appender` (interface) | `void append(LogRecord)` + `void close()` — output strategy |
| `AbstractAppender` | Holds a `Formatter`; implements `append` as format + `doAppend`; `close` is no-op by default |
| `ConsoleAppender` | Extends `AbstractAppender`; `doAppend` calls `System.out.println` |
| `FileAppender` | Extends `AbstractAppender`; owns a `BufferedWriter`; `close` flushes+closes idempotently |
| `Logger` | Gate check, `LogRecord` construction, fan-out to appenders; holds `volatile Level` and `CopyOnWriteArrayList<Appender>` |
| `LoggerFactory` | `ConcurrentHashMap<String, Logger>`; `getLogger(name)` returns or creates |

---

## Key code

**Level gate and fan-out in `Logger.log`**

```java
// Fields
private final String name;
private volatile Level minLevel;                        // volatile: single-word write is atomic
private final CopyOnWriteArrayList<Appender> appenders; // read-heavy, rare mutations

public Logger(String name, Level minLevel) {
    this.name      = Objects.requireNonNull(name);
    this.minLevel  = Objects.requireNonNull(minLevel);
    this.appenders = new CopyOnWriteArrayList<>();
}

public void log(Level level, String message) {
    if (level.ordinal() < minLevel.ordinal()) return;   // fast path — no allocation

    LogRecord record = new LogRecord(
        System.currentTimeMillis(), level, name, message
    );
    for (Appender a : appenders) {                       // snapshot iteration — no lock held
        a.append(record);
    }
}
```

**Why `CopyOnWriteArrayList`:** reads (log calls) vastly outnumber writes (add/remove appender). COWAL gives lock-free, consistent snapshot iteration in `log()` while serialising mutations. A `ReadWriteLock` would work too but requires more ceremony; `synchronized` on every `log()` call kills throughput unnecessarily.

**AbstractAppender template**

```java
public abstract class AbstractAppender implements Logger.Appender {
    protected final Logger.Formatter formatter;

    protected AbstractAppender(Logger.Formatter formatter) {
        this.formatter = (formatter != null) ? formatter : new SimpleFormatter();
    }

    @Override
    public final void append(Logger.LogRecord record) {
        String line = formatter.format(record);
        doAppend(line);
    }

    protected abstract void doAppend(String formatted);

    @Override
    public void close() { /* no-op default */ }
}
```

**ConsoleAppender**

```java
public class ConsoleAppender extends AbstractAppender {
    public ConsoleAppender(Logger.Formatter formatter) { super(formatter); }

    @Override
    protected synchronized void doAppend(String line) {
        System.out.println(line);   // PrintStream.println is already synchronized,
    }                               // but explicit sync prevents interleaving across calls

    @Override public void close() { /* System.out owned externally */ }
}
```

**FileAppender (thread-safe writes, idempotent close)**

```java
public class FileAppender extends AbstractAppender {
    private final Object lock = new Object();
    private BufferedWriter writer;          // lazily opened
    private volatile boolean closed = false;

    public FileAppender(String path, Logger.Formatter fmt) throws IOException {
        super(fmt);
        this.writer = new BufferedWriter(new FileWriter(path, /*append=*/true));
    }

    @Override
    protected void doAppend(String line) {
        if (closed) return;
        synchronized (lock) {
            if (closed) return;             // double-checked after acquiring lock
            try {
                writer.write(line);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("FileAppender write failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        if (closed) return;
        synchronized (lock) {
            if (closed) return;
            closed = true;
            try { writer.close(); } catch (IOException ignored) {}
        }
    }
}
```

**LoggerFactory (named singleton registry)**

```java
public final class LoggerFactory {
    private static final ConcurrentHashMap<String, Logger> REGISTRY = new ConcurrentHashMap<>();

    public static Logger getLogger(String name) {
        return REGISTRY.computeIfAbsent(name, n -> new Logger(n, Logger.Level.DEBUG));
    }

    private LoggerFactory() {}
}
```

**SimpleFormatter**

```java
public class SimpleFormatter implements Logger.Formatter {
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                         .withZone(ZoneId.systemDefault());

    @Override
    public String format(Logger.LogRecord r) {
        return String.format("[%s] %-5s %s — %s",
            DTF.format(Instant.ofEpochMilli(r.getTimestamp())),
            r.getLevel(), r.getLoggerName(), r.getMessage());
    }
}
```

---

## Walkthrough

Scenario from the kata spec: two threads log concurrently while a third reconfigures the logger.

```
Thread-1: logger.info("Request received")
Thread-2: logger.debug("Params: id=42")        // dropped if minLevel = INFO
Thread-3: logger.setMinLevel(Level.WARN)
Thread-4: logger.addAppender(fileAppender)
```

1. **Thread-1** calls `log(INFO, "Request received")`.
   - Reads `volatile minLevel` (INFO). `INFO.ordinal() >= INFO.ordinal()` — passes gate.
   - Allocates `LogRecord(now, INFO, "app", "Request received")`.
   - Iterates the COWAL snapshot: calls `consoleAppender.append(record)`.
   - `AbstractAppender.append` calls `formatter.format(record)` then `System.out.println`.

2. **Thread-2** calls `log(DEBUG, "Params: id=42")`.
   - Reads `volatile minLevel` (INFO). `DEBUG.ordinal() < INFO.ordinal()` — returns immediately. No allocation, no lock.

3. **Thread-3** calls `setMinLevel(WARN)`.
   - Writes `volatile minLevel = WARN`. All subsequent reads in other threads see WARN immediately (happens-before via volatile write).

4. **Thread-4** calls `addAppender(fileAppender)`.
   - COWAL's internal lock serialises the add. Any `log()` call already iterating the old snapshot is unaffected; new calls get a snapshot that includes `fileAppender`.

---

## Concurrency & edge cases

| Risk | Guard |
|---|---|
| Stale `minLevel` across threads | `volatile` field; single-word write is atomic on all JVMs |
| Appender list torn during concurrent add + log | `CopyOnWriteArrayList` — iteration always on a consistent snapshot |
| Interleaved lines in console output | `synchronized` on `doAppend` (or rely on `PrintStream` internal lock) |
| Partial write / torn record in file | `synchronized (lock)` block inside `FileAppender.doAppend` |
| Double-close of `FileAppender` | `volatile boolean closed` + double-checked locking pattern |
| Adding the same appender twice | Guard with `if (!appenders.contains(appender))` before `add` — COWAL's `addIfAbsent` does this atomically |
| Null `level` or `message` passed to `log` | Null-check at entry; throw `NullPointerException` or substitute `"null"` |
| `append` throwing inside the fan-out loop | Wrap each `a.append(record)` in try-catch; log to stderr; continue to next appender |

---

## Complexity & extensibility

**Big-O of key operations**

| Operation | Cost | Notes |
|---|---|---|
| `log()` — gate check | O(1) | Volatile read + ordinal compare |
| `log()` — fan-out | O(A) | A = number of appenders; typically 1-3 |
| `addAppender` / `removeAppender` | O(A) | COWAL copies the backing array |
| `LoggerFactory.getLogger` | O(1) amortised | `ConcurrentHashMap.computeIfAbsent` |

**Adding a new appender (e.g., HTTP sink, ring-buffer) — zero edits to existing code**

```java
public class HttpAppender extends AbstractAppender {
    private final URI endpoint;

    public HttpAppender(URI endpoint, Logger.Formatter fmt) {
        super(fmt);
        this.endpoint = endpoint;
    }

    @Override
    protected void doAppend(String line) {
        // POST line to endpoint — existing Logger, AbstractAppender untouched
    }
}
```

Register it:
```java
logger.addAppender(new HttpAppender(URI.create("https://logs.example.com/ingest"), new SimpleFormatter()));
```

The `Logger` class never needs to change — pure Open/Closed Principle.

---

## Follow-ups

1. **Async appender** — Wrap any `Appender` in `AsyncAppender`: hold a `LinkedBlockingQueue<LogRecord>`, return immediately from `append()` (enqueue only), and drain on a dedicated daemon thread using `queue.take()`. Back-pressure: bound the queue; on overflow either drop (lossy) or block caller (lossless, risks latency). `close()` must drain the queue and interrupt the thread gracefully.

2. **Chain of Responsibility vs Strategy (flat list)** — CoR excels when routing is conditional (e.g., WARN goes to file, ERROR also pages on-call). Flat-list Strategy is simpler when every appender should receive every passing record. Trade-offs: CoR makes adding a new level harder (must rewire the chain); flat Strategy makes adding a level O(1) (just compare ordinals). CoR is harder to unit-test in isolation because handlers are coupled.

3. **Hierarchical loggers** — Introduce a `parent` field on `Logger` (nullable). `log()` walks up the parent chain and delivers the record to each ancestor's appenders (unless `additivity = false` is set, short-circuiting propagation). `LoggerFactory.getLogger("com.venkat.backend")` parses the dot-separated name and looks up or creates parent `"com.venkat"` first. This mirrors Log4j / Logback's logger hierarchy exactly.
