package com.venkat.backend.lld.logger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Definition-of-done tests for the Logging Framework LLD practice problem.
 *
 * <p>Remove the {@code @Disabled} annotation only after you have fully implemented
 * the classes in {@code com.venkat.backend.lld.logger}.
 *
 * <p>These tests compile against the skeleton API only — no internals are assumed.
 */
@Disabled("LLD practice — implement, then remove @Disabled to verify")
class LoggerTest {

    // -------------------------------------------------------------------------
    // Helper: in-memory appender for capturing output in tests
    // -------------------------------------------------------------------------

    /**
     * A test-only appender that accumulates formatted records in memory.
     *
     * <p>You are NOT required to ship this in production code; it lives here
     * purely to make assertions possible without touching files or stdout.
     */
    static class CapturingAppender implements Logger.Appender {

        private final List<Logger.LogRecord> records =
                Collections.synchronizedList(new ArrayList<>());

        @Override
        public void append(Logger.LogRecord record) {
            records.add(record);
        }

        @Override
        public void close() {
            records.clear();
        }

        List<Logger.LogRecord> captured() {
            return Collections.unmodifiableList(records);
        }

        int size() {
            return records.size();
        }
    }

    // -------------------------------------------------------------------------
    // Test 1: level gate — messages below minLevel must be silently dropped
    // -------------------------------------------------------------------------

    /**
     * Scenario: a logger configured at WARN level must forward WARN and ERROR
     * messages to appenders and silently drop DEBUG and INFO messages.
     */
    @Test
    void levelGateDropsBelowMinLevel() {
        CapturingAppender sink = new CapturingAppender();

        Logger logger = new Logger("gateTest", Logger.Level.WARN);
        logger.addAppender(sink);

        logger.debug("this should be dropped");
        logger.info("this should also be dropped");
        logger.warn("this must reach the appender");
        logger.error("this must reach the appender too");

        List<Logger.LogRecord> captured = sink.captured();

        assertEquals(2, captured.size(),
                "Only WARN and ERROR records should pass the level gate");

        assertEquals(Logger.Level.WARN,  captured.get(0).getLevel());
        assertEquals(Logger.Level.ERROR, captured.get(1).getLevel());

        assertEquals("this must reach the appender",      captured.get(0).getMessage());
        assertEquals("this must reach the appender too",  captured.get(1).getMessage());

        // Logger name must be propagated into the record
        assertEquals("gateTest", captured.get(0).getLoggerName());
    }

    // -------------------------------------------------------------------------
    // Test 2: runtime reconfiguration — level change + adding/removing appenders
    // -------------------------------------------------------------------------

    /**
     * Scenario: dynamically raise the minimum level and swap appenders at runtime;
     * previously registered appenders must stop receiving records after removal.
     */
    @Test
    void runtimeReconfigurationChangesRouting() {
        CapturingAppender sinkA = new CapturingAppender();
        CapturingAppender sinkB = new CapturingAppender();

        Logger logger = new Logger("reconfig", Logger.Level.DEBUG);
        logger.addAppender(sinkA);

        logger.info("goes to A only");

        // Add a second appender — subsequent messages go to both
        logger.addAppender(sinkB);
        logger.warn("goes to A and B");

        // Raise the level — INFO should now be dropped
        logger.setMinLevel(Logger.Level.ERROR);
        logger.info("dropped — level raised");
        logger.error("goes to A and B again");

        // Remove sinkA — only sinkB receives from now on
        logger.removeAppender(sinkA);
        logger.error("goes to B only");

        // sinkA: "goes to A only", "goes to A and B", "goes to A and B again" => 3
        assertEquals(3, sinkA.size(),
                "sinkA should have received exactly 3 records before removal");

        // sinkB: "goes to A and B", "goes to A and B again", "goes to B only" => 3
        assertEquals(3, sinkB.size(),
                "sinkB should have received exactly 3 records");

        assertEquals(Logger.Level.ERROR, logger.getMinLevel());
    }

    // -------------------------------------------------------------------------
    // Test 3: thread-safety — concurrent log calls must not lose records
    // -------------------------------------------------------------------------

    /**
     * Scenario: 10 threads each log 100 ERROR messages concurrently; the
     * capturing appender must receive exactly 1000 records with no data races.
     */
    @Test
    void concurrentLoggingDoesNotLoseRecords() throws InterruptedException {
        CapturingAppender sink = new CapturingAppender();

        Logger logger = new Logger("concurrent", Logger.Level.ERROR);
        logger.addAppender(sink);

        int threadCount   = 10;
        int logsPerThread = 100;

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(threadCount);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            pool.submit(() -> {
                try {
                    startGate.await();           // all threads start simultaneously
                    for (int i = 0; i < logsPerThread; i++) {
                        logger.error("thread-" + threadId + "-msg-" + i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startGate.countDown();                   // release all threads at once
        assertTrue(doneLatch.await(10, TimeUnit.SECONDS),
                "Threads did not finish within 10 seconds");
        pool.shutdown();

        assertEquals(threadCount * logsPerThread, sink.size(),
                "All " + (threadCount * logsPerThread) +
                " concurrent log records must be captured without loss");
    }
}
