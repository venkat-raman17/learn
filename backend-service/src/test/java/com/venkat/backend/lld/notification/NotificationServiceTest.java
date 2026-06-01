package com.venkat.backend.lld.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Definition-of-done tests for the Notification Service LLD practice.
 *
 * <p>Remove the {@code @Disabled} annotation once your implementation is complete
 * and all three tests pass without modification.
 *
 * <p>These tests use only the public API defined in {@link NotificationService}
 * plus a minimal in-process stub channel — no external dependencies needed.
 */
@Disabled("LLD practice — implement, then remove")
class NotificationServiceTest {

    // ------------------------------------------------------------------
    // Minimal stub channel (in-process, always succeeds)
    // ------------------------------------------------------------------

    /** Records every send call so tests can assert delivery occurred. */
    static class StubChannel implements NotificationChannel {

        private final ChannelType channelType;
        final List<String> deliveredBodies = new ArrayList<>();
        private boolean shouldFail;
        private int failCount;
        private int callCount;

        StubChannel(ChannelType channelType) {
            this.channelType = channelType;
        }

        /** Configure the stub to fail for the next {@code times} invocations, then succeed. */
        void failNextTimes(int times) {
            this.shouldFail = true;
            this.failCount = times;
            this.callCount = 0;
        }

        @Override
        public ChannelType type() {
            return channelType;
        }

        @Override
        public DeliveryResult send(User user, Notification notification) {
            callCount++;
            if (shouldFail && callCount <= failCount) {
                throw new RuntimeException("Transient failure #" + callCount);
            }
            deliveredBodies.add(notification.body());
            return new DeliveryResult.Success(channelType, System.currentTimeMillis());
        }
    }

    // ------------------------------------------------------------------
    // Test fixtures
    // ------------------------------------------------------------------

    private NotificationService service;
    private StubChannel emailChannel;
    private StubChannel smsChannel;

    private static final User ALICE = new User(
            "user-alice", "Alice", "alice@example.com", "+10000000001", "token-alice");

    @BeforeEach
    void setUp() {
        service = NotificationService.create();
        emailChannel = new StubChannel(ChannelType.EMAIL);
        smsChannel   = new StubChannel(ChannelType.SMS);

        service.registerChannel(emailChannel);
        service.registerChannel(smsChannel);
        service.registerUser(ALICE);
    }

    // ------------------------------------------------------------------
    // Test 1: fan-out to subscribed channels + template rendering
    // ------------------------------------------------------------------

    /**
     * When Alice is subscribed to both EMAIL and SMS, a single {@code send} call
     * must deliver to both channels. A registered template must be applied so the
     * rendered body (not the raw body) reaches the channel.
     */
    @Test
    void send_fanOutToAllSubscribedChannels_andAppliesTemplate() {
        // Subscribe Alice to both channels
        service.subscribe(ALICE.userId(), ChannelType.EMAIL);
        service.subscribe(ALICE.userId(), ChannelType.SMS);

        // Register a WELCOME template that prepends "Hello <name>: "
        service.registerTemplate("WELCOME",
                (n, u) -> "Hello " + u.name() + ": " + n.body());

        Notification notification = new Notification(
                ALICE.userId(), "WELCOME", "Welcome!", "You have joined.", ChannelType.EMAIL);

        List<DeliveryResult> results = service.send(notification);

        // Both channels attempted
        assertEquals(2, results.size(),
                "Expected one DeliveryResult per subscribed channel");

        // Both succeeded
        assertTrue(results.stream().allMatch(r -> r instanceof DeliveryResult.Success),
                "All results should be Success when channels do not fail");

        // Template was applied — raw body was "You have joined."
        // rendered should be "Hello Alice: You have joined."
        String expectedBody = "Hello Alice: You have joined.";
        assertTrue(emailChannel.deliveredBodies.contains(expectedBody),
                "EMAIL channel should have received the rendered body");
        assertTrue(smsChannel.deliveredBodies.contains(expectedBody),
                "SMS channel should have received the rendered body");
    }

    // ------------------------------------------------------------------
    // Test 2: retry policy recovers a transient failure
    // ------------------------------------------------------------------

    /**
     * When the EMAIL channel fails transiently on the first attempt but succeeds
     * on the second, the retry policy must allow recovery and the final result
     * must be {@link DeliveryResult.Success}.
     */
    @Test
    void send_retriesOnTransientFailure_andReturnsSuccess() {
        service.subscribe(ALICE.userId(), ChannelType.EMAIL);

        // Fail the first attempt, succeed on retry
        emailChannel.failNextTimes(1);
        service.setRetryPolicy(ChannelType.EMAIL, new RetryPolicy(3, 0));

        Notification notification = new Notification(
                ALICE.userId(), "OTP", "Your OTP", "123456", ChannelType.EMAIL);

        List<DeliveryResult> results = service.send(notification);

        assertEquals(1, results.size());
        assertInstanceOf(DeliveryResult.Success.class, results.get(0),
                "Should succeed after retry");

        // Channel was ultimately invoked and body was delivered
        assertFalse(emailChannel.deliveredBodies.isEmpty(),
                "Body should have been delivered after successful retry");
    }

    // ------------------------------------------------------------------
    // Test 3: observer receives callbacks + unsubscribe prevents delivery
    // ------------------------------------------------------------------

    /**
     * Observers must be invoked for each delivery attempt. Unsubscribing a channel
     * must prevent further deliveries on that channel.
     */
    @Test
    void addObserver_receivesCallbacks_andUnsubscribeStopsDelivery() {
        service.subscribe(ALICE.userId(), ChannelType.EMAIL);
        service.subscribe(ALICE.userId(), ChannelType.SMS);

        List<DeliveryResult> observedResults = new ArrayList<>();
        service.addObserver(observedResults::add);

        Notification first = new Notification(
                ALICE.userId(), "INFO", "Info", "First message", ChannelType.EMAIL);
        service.send(first);

        // Both channels attempted — observer should have seen 2 results
        assertEquals(2, observedResults.size(),
                "Observer should be called once per channel attempt");

        // Now unsubscribe Alice from SMS
        service.unsubscribe(ALICE.userId(), ChannelType.SMS);
        observedResults.clear();

        Notification second = new Notification(
                ALICE.userId(), "INFO", "Info", "Second message", ChannelType.EMAIL);
        service.send(second);

        // Only EMAIL now
        assertEquals(1, observedResults.size(),
                "After unsubscribing SMS, observer should only see 1 result");
        assertEquals(ChannelType.EMAIL, observedResults.get(0).channelType(),
                "Remaining result must be for EMAIL");

        // SMS channel must not have received the second message
        assertTrue(smsChannel.deliveredBodies.stream()
                .noneMatch(b -> b.equals("Second message")),
                "SMS channel must not deliver after user unsubscribes");
    }
}
