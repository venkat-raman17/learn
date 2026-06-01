package com.venkat.backend.lld.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Logger {

    // -------------------------------------------------------------------------
    // Level enum
    // -------------------------------------------------------------------------

    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    // -------------------------------------------------------------------------
    // LogRecord — immutable value object
    // -------------------------------------------------------------------------

    public static final class LogRecord {

        private final long timestamp;
        private final Level level;
        private final String loggerName;
        private final String message;

        public LogRecord(long timestamp, Level level, String loggerName, String message) {
            this.timestamp  = timestamp;
            this.level      = level;
            this.loggerName = loggerName;
            this.message    = message;
        }

        public long getTimestamp()    { return timestamp; }
        public Level getLevel()       { return level; }
        public String getLoggerName() { return loggerName; }
        public String getMessage()    { return message; }
    }

    // -------------------------------------------------------------------------
    // Formatter — Strategy interface
    // -------------------------------------------------------------------------

    public interface Formatter {
        String format(LogRecord record);
    }

    // -------------------------------------------------------------------------
    // Default formatters
    // -------------------------------------------------------------------------

    public static class PlainFormatter implements Formatter {
        private static final DateTimeFormatter DF =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

        @Override
        public String format(LogRecord record) {
            return String.format("[%s] [%s] [%s] %s",
                DF.format(Instant.ofEpochMilli(record.getTimestamp())),
                record.getLevel(),
                record.getLoggerName(),
                record.getMessage());
        }
    }

    public static class JsonFormatter implements Formatter {
        @Override
        public String format(LogRecord record) {
            return String.format(
                "{\"ts\":%d,\"level\":\"%s\",\"logger\":\"%s\",\"msg\":\"%s\"}",
                record.getTimestamp(),
                record.getLevel(),
                record.getLoggerName(),
                record.getMessage().replace("\"", "\\\""));
        }
    }

    // -------------------------------------------------------------------------
    // Appender — Strategy interface
    // -------------------------------------------------------------------------

    public interface Appender {
        void append(LogRecord record);
        void close();
    }

    // -------------------------------------------------------------------------
    // ConsoleAppender
    // -------------------------------------------------------------------------

    public static class ConsoleAppender implements Appender {

        private final Formatter formatter;

        public ConsoleAppender(Formatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public synchronized void append(LogRecord record) {
            System.out.println(formatter.format(record));
        }

        @Override
        public void close() {
            // no-op: console does not need to be closed
        }
    }

    // -------------------------------------------------------------------------
    // FileAppender
    // -------------------------------------------------------------------------

    public static class FileAppender implements Appender {

        private final String filePath;
        private final Formatter formatter;
        private BufferedWriter writer;
        private boolean closed = false;

        public FileAppender(String filePath, Formatter formatter) {
            this.filePath  = filePath;
            this.formatter = formatter;
            try {
                this.writer = new BufferedWriter(new FileWriter(filePath, true));
            } catch (IOException e) {
                throw new RuntimeException("Cannot open log file: " + filePath, e);
            }
        }

        public String getFilePath() { return filePath; }

        @Override
        public synchronized void append(LogRecord record) {
            if (closed) return;
            try {
                writer.write(formatter.format(record));
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("FileAppender write error: " + e.getMessage());
            }
        }

        @Override
        public synchronized void close() {
            if (closed) return;
            closed = true;
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("FileAppender close error: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // Logger fields
    // =========================================================================

    private final String name;
    private final AtomicReference<Level> minLevel;
    // CopyOnWriteArrayList gives thread-safe reads without locking
    private final CopyOnWriteArrayList<Appender> appenders = new CopyOnWriteArrayList<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Logger(String name, Level minLevel) {
        if (name == null) throw new IllegalArgumentException("name must not be null");
        if (minLevel == null) throw new IllegalArgumentException("minLevel must not be null");
        this.name     = name;
        this.minLevel = new AtomicReference<>(minLevel);
    }

    public Logger(String name) {
        this(name, Level.DEBUG);
    }

    // -------------------------------------------------------------------------
    // Core dispatch
    // -------------------------------------------------------------------------

    public void log(Level level, String message) {
        if (level == null || message == null) return;
        if (level.ordinal() < minLevel.get().ordinal()) return;

        LogRecord record = new LogRecord(System.currentTimeMillis(), level, name, message);
        // Snapshot the list to avoid ConcurrentModificationException under add/remove
        for (Appender appender : appenders) {
            appender.append(record);
        }
    }

    // -------------------------------------------------------------------------
    // Convenience helpers
    // -------------------------------------------------------------------------

    public void debug(String message) { log(Level.DEBUG, message); }
    public void info(String message)  { log(Level.INFO,  message); }
    public void warn(String message)  { log(Level.WARN,  message); }
    public void error(String message) { log(Level.ERROR, message); }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    public String getName()    { return name; }
    public Level getMinLevel() { return minLevel.get(); }

    public void setMinLevel(Level level) {
        if (level == null) throw new IllegalArgumentException("level must not be null");
        minLevel.set(level);
    }

    public void addAppender(Appender appender) {
        if (appender == null) throw new IllegalArgumentException("appender must not be null");
        // Identity deduplication
        for (Appender existing : appenders) {
            if (existing == appender) return;
        }
        appenders.add(appender);
    }

    public void removeAppender(Appender appender) {
        appenders.removeIf(a -> a == appender);
    }

    public List<Appender> getAppenders() {
        return Collections.unmodifiableList(new ArrayList<>(appenders));
    }
}
