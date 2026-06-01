package com.venkat.backend.lld.logger;

import java.util.List;

/**
 * Skeleton API for the Logging Framework LLD practice problem.
 *
 * <p><strong>Rules for the learner:</strong>
 * <ul>
 *   <li>Do NOT change any method or type signature in this file.</li>
 *   <li>Implement all internal logic; you may add helper classes in separate files.</li>
 *   <li>Replace every {@code throw new UnsupportedOperationException("implement me")}
 *       with a real implementation.</li>
 * </ul>
 */
public class Logger {

    // -------------------------------------------------------------------------
    // Level enum
    // -------------------------------------------------------------------------

    /**
     * Severity levels in ascending order: DEBUG &lt; INFO &lt; WARN &lt; ERROR.
     *
     * <p>The natural {@link Enum#ordinal()} ordering matches the severity order,
     * so {@code level.ordinal() >= minLevel.ordinal()} is a valid gate check.
     */
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    // -------------------------------------------------------------------------
    // LogRecord — immutable value object
    // -------------------------------------------------------------------------

    /**
     * An immutable snapshot of a single log event.
     *
     * <p>Construct one inside {@link Logger#log(Level, String)} and pass it to
     * every enabled appender.
     */
    public static final class LogRecord {

        private final long timestamp;
        private final Level level;
        private final String loggerName;
        private final String message;

        /**
         * Creates a log record.
         *
         * @param timestamp  epoch-milliseconds ({@code System.currentTimeMillis()})
         * @param level      severity of the event
         * @param loggerName name of the issuing logger
         * @param message    human-readable log message
         */
        public LogRecord(long timestamp, Level level, String loggerName, String message) {
            this.timestamp  = timestamp;
            this.level      = level;
            this.loggerName = loggerName;
            this.message    = message;
        }

        /** Returns the epoch-millisecond timestamp. */
        public long getTimestamp()  { return timestamp; }

        /** Returns the severity level. */
        public Level getLevel()     { return level; }

        /** Returns the name of the logger that produced this record. */
        public String getLoggerName() { return loggerName; }

        /** Returns the log message. */
        public String getMessage()  { return message; }
    }

    // -------------------------------------------------------------------------
    // Formatter — Strategy interface
    // -------------------------------------------------------------------------

    /**
     * Strategy interface for converting a {@link LogRecord} to a printable string.
     *
     * <p>Implement this to customise the output format (plain-text, JSON, etc.).
     */
    public interface Formatter {
        /**
         * Formats the given record into a string ready for output.
         *
         * @param record the log record to format
         * @return a non-null formatted string
         */
        String format(LogRecord record);
    }

    // -------------------------------------------------------------------------
    // Appender — Strategy interface
    // -------------------------------------------------------------------------

    /**
     * Strategy interface for log output destinations.
     *
     * <p>Implementations must be thread-safe: multiple threads may call
     * {@link #append(LogRecord)} concurrently.
     */
    public interface Appender {

        /**
         * Writes the given record to this appender's destination.
         *
         * @param record the log record to write; never {@code null}
         */
        void append(LogRecord record);

        /**
         * Releases any resources held by this appender (e.g., file handles).
         *
         * <p>Called when the appender is removed from a logger or when the
         * application shuts down. Must be idempotent.
         */
        void close();
    }

    // -------------------------------------------------------------------------
    // ConsoleAppender — built-in implementation stub
    // -------------------------------------------------------------------------

    /**
     * An {@link Appender} that writes formatted log records to {@code System.out}.
     *
     * <p>Uses the supplied {@link Formatter}; if none is provided use a sensible default.
     */
    public static class ConsoleAppender implements Appender {

        private final Formatter formatter;

        /**
         * Creates a console appender with the given formatter.
         *
         * @param formatter strategy used to convert records to strings
         */
        public ConsoleAppender(Formatter formatter) {
            this.formatter = formatter;
        }

        /**
         * {@inheritDoc}
         *
         * <p>Writes the formatted record to {@code System.out} in a thread-safe manner.
         */
        @Override
        public void append(LogRecord record) {
            throw new UnsupportedOperationException("implement me");
        }

        /** No-op for console; provided for API symmetry. */
        @Override
        public void close() {
            throw new UnsupportedOperationException("implement me");
        }
    }

    // -------------------------------------------------------------------------
    // FileAppender — built-in implementation stub
    // -------------------------------------------------------------------------

    /**
     * An {@link Appender} that appends formatted log records to a file.
     *
     * <p>The file is created (or opened for append) lazily or eagerly — your choice.
     * Writes must be thread-safe.
     */
    public static class FileAppender implements Appender {

        private final String filePath;
        private final Formatter formatter;

        /**
         * Creates a file appender targeting the given path.
         *
         * @param filePath  path of the target log file
         * @param formatter strategy used to convert records to strings
         */
        public FileAppender(String filePath, Formatter formatter) {
            this.filePath  = filePath;
            this.formatter = formatter;
        }

        /** Returns the target file path. */
        public String getFilePath() { return filePath; }

        /**
         * {@inheritDoc}
         *
         * <p>Appends the formatted record to the file. Must be thread-safe.
         */
        @Override
        public void append(LogRecord record) {
            throw new UnsupportedOperationException("implement me");
        }

        /**
         * Flushes and closes the underlying file writer.
         * Must be idempotent and thread-safe.
         */
        @Override
        public void close() {
            throw new UnsupportedOperationException("implement me");
        }
    }

    // =========================================================================
    // Logger fields — intentionally left blank for the learner to choose
    // =========================================================================

    // TODO: declare fields — e.g. name, minLevel, appender collection, lock

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Creates a logger with the given name and minimum level, with no appenders.
     *
     * @param name     logical name for this logger (e.g. class name or module)
     * @param minLevel messages below this level are discarded
     */
    public Logger(String name, Level minLevel) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Creates a logger with {@link Level#DEBUG} as the default minimum level.
     *
     * @param name logical name for this logger
     */
    public Logger(String name) {
        this(name, Level.DEBUG);
    }

    // -------------------------------------------------------------------------
    // Core dispatch
    // -------------------------------------------------------------------------

    /**
     * Logs a message at the specified level if {@code level >= minLevel}.
     *
     * <p>Thread-safe: may be called concurrently by multiple threads.
     *
     * @param level   severity of the event
     * @param message human-readable description
     */
    public void log(Level level, String message) {
        throw new UnsupportedOperationException("implement me");
    }

    // -------------------------------------------------------------------------
    // Convenience helpers
    // -------------------------------------------------------------------------

    /**
     * Logs a DEBUG-level message. Equivalent to {@code log(Level.DEBUG, message)}.
     *
     * @param message the debug message
     */
    public void debug(String message) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Logs an INFO-level message. Equivalent to {@code log(Level.INFO, message)}.
     *
     * @param message the info message
     */
    public void info(String message) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Logs a WARN-level message. Equivalent to {@code log(Level.WARN, message)}.
     *
     * @param message the warning message
     */
    public void warn(String message) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Logs an ERROR-level message. Equivalent to {@code log(Level.ERROR, message)}.
     *
     * @param message the error message
     */
    public void error(String message) {
        throw new UnsupportedOperationException("implement me");
    }

    // -------------------------------------------------------------------------
    // Configuration (runtime-mutable)
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this logger.
     *
     * @return logger name; never {@code null}
     */
    public String getName() {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns the current minimum level gate.
     *
     * @return current minimum {@link Level}
     */
    public Level getMinLevel() {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Updates the minimum level gate at runtime.
     *
     * <p>After this call, any subsequent {@code log()} invocation uses the new level.
     * Thread-safe.
     *
     * @param level the new minimum level; must not be {@code null}
     */
    public void setMinLevel(Level level) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Adds an appender to this logger's sink list.
     *
     * <p>The same appender instance may not be added twice (guard with identity check).
     * Thread-safe.
     *
     * @param appender the appender to add; must not be {@code null}
     */
    public void addAppender(Appender appender) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Removes an appender from this logger's sink list.
     *
     * <p>Does nothing if the appender is not present. Does NOT call {@link Appender#close()};
     * the caller is responsible for lifecycle management. Thread-safe.
     *
     * @param appender the appender to remove
     */
    public void removeAppender(Appender appender) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Returns an unmodifiable snapshot of the current appender list.
     *
     * @return immutable list of appenders registered at the time of the call
     */
    public List<Appender> getAppenders() {
        throw new UnsupportedOperationException("implement me");
    }
}
