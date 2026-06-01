/**
 * <h2>LLD Practice Problem: Logging Framework</h2>
 *
 * <h3>Problem Statement</h3>
 * <p>
 * Design and implement a thread-safe, extensible logging framework in plain Java.
 * The framework must support multiple severity levels, a configurable minimum level
 * per logger instance, and pluggable output destinations (appenders/sinks).
 * The design goal is to separate <em>what to log</em> (the logger) from
 * <em>where to log</em> (the appender) and <em>how to format it</em> (the formatter).
 * </p>
 *
 * <hr>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li>Support four severity levels in ascending order: DEBUG &lt; INFO &lt; WARN &lt; ERROR.</li>
 *   <li>Each {@code Logger} instance has a configurable minimum level; messages below
 *       the threshold are silently dropped.</li>
 *   <li>A logger holds one or more {@code Appender} instances. Every enabled appender
 *       receives every log record that passes the level gate.</li>
 *   <li>Built-in appenders: {@code ConsoleAppender} (writes to stdout) and
 *       {@code FileAppender} (appends to a named file).</li>
 *   <li>Log records include: timestamp (epoch ms), level, logger name, and message.</li>
 *   <li>Convenience helpers: {@code debug(msg)}, {@code info(msg)},
 *       {@code warn(msg)}, {@code error(msg)}.</li>
 *   <li>The minimum level and the appender list must be mutable at runtime.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ol>
 *   <li><strong>Thread-safety:</strong> concurrent calls to {@code log(...)} from
 *       multiple threads must not interleave or corrupt output.</li>
 *   <li><strong>Extensibility:</strong> adding a new appender (e.g., network sink,
 *       in-memory ring-buffer) requires only implementing {@code Appender} — no
 *       changes to {@code Logger}.</li>
 *   <li><strong>No external dependencies:</strong> {@code java.util} and
 *       {@code java.io} / {@code java.nio} only.</li>
 *   <li><strong>No busy-wait:</strong> thread synchronisation must use proper
 *       Java concurrency primitives.</li>
 * </ol>
 *
 * <hr>
 *
 * <h3>Suggested Entities</h3>
 * <ul>
 *   <li>{@code Level} — enum: DEBUG, INFO, WARN, ERROR (with ordinal-based comparison).</li>
 *   <li>{@code LogRecord} — immutable value object: {@code (long timestamp, Level level,
 *       String loggerName, String message)}.</li>
 *   <li>{@code Formatter} — single-method interface: {@code String format(LogRecord)}.</li>
 *   <li>{@code Appender} — interface with {@code void append(LogRecord)} and
 *       {@code void close()}.</li>
 *   <li>{@code ConsoleAppender} — implements {@code Appender}, writes to
 *       {@code System.out}.</li>
 *   <li>{@code FileAppender} — implements {@code Appender}, writes to a file path.</li>
 *   <li>{@code Logger} — central class: holds name, minimum level, list of appenders,
 *       and the log/debug/info/warn/error methods.</li>
 *   <li>{@code LoggerFactory} — optional; produces named {@code Logger} singletons
 *       (think: registry pattern).</li>
 * </ul>
 *
 * <hr>
 *
 * <h3>Public API (minimal surface — do not change signatures)</h3>
 * <pre>{@code
 * // Core log dispatch
 * void log(Level level, String message);
 *
 * // Convenience shortcuts
 * void debug(String message);
 * void info(String message);
 * void warn(String message);
 * void error(String message);
 *
 * // Configuration (runtime-mutable)
 * void setMinLevel(Level level);
 * Level getMinLevel();
 * void addAppender(Appender appender);
 * void removeAppender(Appender appender);
 * }</pre>
 *
 * <hr>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><strong>Strategy:</strong> {@code Appender} and {@code Formatter} are strategy
 *       interfaces — swap implementations without touching {@code Logger}.</li>
 *   <li><strong>Chain of Responsibility:</strong> alternatively, model each level as a
 *       handler in a chain; each handler decides whether to process the record and
 *       whether to pass it along. Consider when this is better or worse than the
 *       flat-list-of-appenders approach.</li>
 *   <li><strong>Factory / Registry:</strong> a {@code LoggerFactory} that returns the
 *       same {@code Logger} instance for the same name avoids duplicate appenders across
 *       a codebase.</li>
 *   <li><strong>Template Method:</strong> a base {@code AbstractAppender} could handle
 *       formatting and level-gating, delegating only the I/O to subclasses.</li>
 * </ul>
 *
 * <hr>
 *
 * <h3>Follow-up Questions</h3>
 * <ol>
 *   <li>How would you implement an <em>asynchronous</em> appender that queues
 *       {@code LogRecord} objects and flushes them on a dedicated background thread,
 *       without blocking the caller?</li>
 *   <li>If you used Chain of Responsibility, what is the trade-off versus the
 *       Strategy (flat-list) approach in terms of adding a new level, changing
 *       routing logic, and testability?</li>
 *   <li>How would you add hierarchical loggers (parent/child by dot-separated name,
 *       like {@code com.venkat} is the parent of {@code com.venkat.backend}) so that
 *       a child inherits its parent's appenders unless explicitly overridden?</li>
 * </ol>
 *
 * <h3>Extensibility / Concurrency Prompt</h3>
 * <p>
 * Your {@code Logger} must remain correct when 100 threads call {@code log()} simultaneously
 * and a separate thread calls {@code addAppender()} / {@code setMinLevel()} concurrently.
 * Consider whether a {@code ReadWriteLock}, a {@code synchronized} block, or a
 * {@code CopyOnWriteArrayList} is the right primitive for the appender list, and justify
 * your choice in a comment.
 * </p>
 */
package com.venkat.backend.lld.logger;
