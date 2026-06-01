package com.venkat.backend.lld.notification;

import java.util.List;

// ---------------------------------------------------------------------------
// Enumerations
// ---------------------------------------------------------------------------

/**
 * Supported notification delivery channels.
 * Add new values here to extend the set of known channel types; you must also
 * register a corresponding {@link NotificationChannel} implementation with the
 * {@link NotificationService} at runtime.
 */
enum ChannelType {
    EMAIL,
    SMS,
    PUSH
}

// ---------------------------------------------------------------------------
// Value objects / DTOs
// ---------------------------------------------------------------------------

/**
 * Immutable notification payload sent by the caller.
 *
 * @param recipientUserId ID of the target user (must be registered in the service).
 * @param type            Logical notification type key (e.g. "WELCOME", "OTP").
 *                        Used to look up a registered {@link NotificationTemplate}.
 * @param subject         Short subject / title line.
 * @param body            Raw body text (may be overwritten by the template hook).
 * @param channelType     Preferred channel; the service will also fan out to all
 *                        other channels the user has subscribed to.
 */
record Notification(
        String recipientUserId,
        String type,
        String subject,
        String body,
        ChannelType channelType
) {}

/**
 * A registered user who can subscribe to notification channels.
 *
 * @param userId      Unique identifier.
 * @param name        Display name (used by templates).
 * @param email       Email address (required by the EMAIL channel).
 * @param phone       Phone / mobile number (required by the SMS channel).
 * @param deviceToken Push notification token (required by the PUSH channel).
 */
record User(
        String userId,
        String name,
        String email,
        String phone,
        String deviceToken
) {}

/**
 * Policy that governs how many times a failed channel send is retried and how
 * long to wait between attempts.
 *
 * @param maxAttempts  Total maximum attempts (1 means no retry after first failure).
 * @param delayMillis  Milliseconds to wait between consecutive attempts.
 */
record RetryPolicy(int maxAttempts, long delayMillis) {}

/**
 * Sealed result hierarchy for a single channel dispatch attempt.
 *
 * <p>Use {@code instanceof} pattern-matching (Java 21) to distinguish the two cases:
 * <pre>{@code
 * if (result instanceof DeliveryResult.Success s) { ... }
 * else if (result instanceof DeliveryResult.Failure f) { ... }
 * }</pre>
 */
sealed interface DeliveryResult permits DeliveryResult.Success, DeliveryResult.Failure {

    /** The channel over which delivery was attempted. */
    ChannelType channelType();

    /** Successful delivery. */
    record Success(ChannelType channelType, long timestampEpochMs) implements DeliveryResult {}

    /** Delivery failed after all retry attempts. */
    record Failure(ChannelType channelType, String reason, int attempts) implements DeliveryResult {}
}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

/**
 * <strong>Strategy</strong> — pluggable delivery channel.
 *
 * <p>Implement this interface to add a new channel (e.g. SLACK) without touching
 * {@link NotificationService}. Register the implementation at startup via
 * {@link NotificationService#registerChannel(NotificationChannel)}.
 *
 * <p>Implementations are expected to be <em>stateless</em> or thread-safe.
 */
interface NotificationChannel {

    /**
     * Returns the channel type this implementation handles.
     * The service uses this value as the registry key.
     */
    ChannelType type();

    /**
     * Attempts to deliver the notification to the given user.
     *
     * <p>Throw any {@link RuntimeException} to signal a transient failure that the
     * retry executor should retry. Return a {@link DeliveryResult.Failure} to signal
     * a permanent / non-retryable failure.
     *
     * @param user         resolved recipient
     * @param notification notification payload (body may already be template-rendered)
     * @return a {@link DeliveryResult} — never {@code null}
     */
    DeliveryResult send(User user, Notification notification);
}

// ---------------------------------------------------------------------------
// Template hook
// ---------------------------------------------------------------------------

/**
 * <strong>Template hook</strong> — renders a {@link Notification} body for a specific user.
 *
 * <p>Register with {@link NotificationService#registerTemplate(String, NotificationTemplate)}.
 * The {@code type} key (e.g. {@code "WELCOME"}) must match {@link Notification#type()}.
 *
 * <p>This is a {@code @FunctionalInterface} so it can be supplied as a lambda.
 */
@FunctionalInterface
interface NotificationTemplate {

    /**
     * Renders the notification body.
     *
     * @param notification original notification (use {@link Notification#body()} as a fallback)
     * @param recipient    the resolved target user (use for personalisation)
     * @return rendered body text; must not be {@code null}
     */
    String render(Notification notification, User recipient);
}

// ---------------------------------------------------------------------------
// Observer interface
// ---------------------------------------------------------------------------

/**
 * <strong>Observer</strong> — receives callbacks after each channel delivery attempt.
 *
 * <p>Register with {@link NotificationService#addObserver(DeliveryObserver)}.
 * All registered observers are invoked after every attempt (success or failure).
 * Implementations must not throw unchecked exceptions from {@code onDelivery};
 * doing so may suppress subsequent observer notifications.
 */
@FunctionalInterface
interface DeliveryObserver {

    /**
     * Called after a delivery attempt completes.
     *
     * @param result the outcome of the attempt
     */
    void onDelivery(DeliveryResult result);
}

// ---------------------------------------------------------------------------
// Main service class
// ---------------------------------------------------------------------------

/**
 * Entry point for the Notification Service.
 *
 * <h3>Usage sketch</h3>
 * <pre>{@code
 * NotificationService svc = NotificationService.create();
 *
 * // Register channels (Strategy plug-in)
 * svc.registerChannel(new EmailChannel());
 * svc.setRetryPolicy(ChannelType.EMAIL, new RetryPolicy(3, 200));
 *
 * // Register a template
 * svc.registerTemplate("WELCOME", (n, u) -> "Hi " + u.name() + "! " + n.body());
 *
 * // Attach an observer
 * svc.addObserver(result -> System.out.println("Delivery: " + result));
 *
 * // Manage subscriptions
 * svc.registerUser(user);
 * svc.subscribe(user.userId(), ChannelType.EMAIL);
 *
 * // Dispatch
 * List<DeliveryResult> results = svc.send(notification);
 * }</pre>
 *
 * <p><strong>Learner task:</strong> implement every method body. Define all internal
 * helper classes (registries, retry executor, etc.) in separate files inside this package.
 * Do NOT modify any method signature or the public API of the types defined in this file.
 */
public class NotificationService {

    // ------------------------------------------------------------------
    // Factory
    // ------------------------------------------------------------------

    /**
     * Creates a new, empty {@code NotificationService} instance with no channels,
     * no templates, no observers, and no registered users.
     *
     * @return a fresh service ready for configuration
     */
    public static NotificationService create() {
        throw new UnsupportedOperationException("implement me");
    }

    // ------------------------------------------------------------------
    // Channel registration (Strategy)
    // ------------------------------------------------------------------

    /**
     * Registers a delivery channel strategy.
     * Replaces any previously registered channel for the same {@link ChannelType}.
     *
     * @param channel the channel implementation; must not be {@code null}
     */
    public void registerChannel(NotificationChannel channel) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Associates a {@link RetryPolicy} with a channel type.
     * If no policy is set for a channel the service sends exactly once (no retry).
     *
     * @param channelType target channel type
     * @param policy      retry configuration; must not be {@code null}
     */
    public void setRetryPolicy(ChannelType channelType, RetryPolicy policy) {
        throw new UnsupportedOperationException("implement me");
    }

    // ------------------------------------------------------------------
    // Template registration
    // ------------------------------------------------------------------

    /**
     * Registers a template for a given notification type key.
     * Replaces any previously registered template for the same key.
     *
     * @param notificationType key that matches {@link Notification#type()}
     * @param template         the rendering function; must not be {@code null}
     */
    public void registerTemplate(String notificationType, NotificationTemplate template) {
        throw new UnsupportedOperationException("implement me");
    }

    // ------------------------------------------------------------------
    // Observer registration (Observer pattern)
    // ------------------------------------------------------------------

    /**
     * Adds a delivery observer that will be called after every send attempt.
     * The same observer instance may be added multiple times (each addition adds
     * an extra invocation per event — implementations may choose to prevent duplicates).
     *
     * @param observer the observer to register; must not be {@code null}
     */
    public void addObserver(DeliveryObserver observer) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Removes a previously registered observer.
     * Has no effect if the observer was not registered.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(DeliveryObserver observer) {
        throw new UnsupportedOperationException("implement me");
    }

    // ------------------------------------------------------------------
    // User management
    // ------------------------------------------------------------------

    /**
     * Registers a user with the service, making them a valid notification recipient.
     * Replaces any existing user record with the same {@link User#userId()}.
     *
     * @param user the user to register; must not be {@code null}
     */
    public void registerUser(User user) {
        throw new UnsupportedOperationException("implement me");
    }

    // ------------------------------------------------------------------
    // Subscription management (Observer — subscribe/unsubscribe)
    // ------------------------------------------------------------------

    /**
     * Subscribes a user to a notification channel.
     * Subsequent calls to {@link #send(Notification)} will also route to this channel
     * if a registered {@link NotificationChannel} exists for it.
     *
     * @param userId      the ID of a previously registered user
     * @param channelType the channel to subscribe to
     * @throws IllegalArgumentException if the userId is not registered
     */
    public void subscribe(String userId, ChannelType channelType) {
        throw new UnsupportedOperationException("implement me");
    }

    /**
     * Unsubscribes a user from a notification channel.
     * Has no effect if the user was not subscribed to that channel.
     *
     * @param userId      the ID of a previously registered user
     * @param channelType the channel to unsubscribe from
     * @throws IllegalArgumentException if the userId is not registered
     */
    public void unsubscribe(String userId, ChannelType channelType) {
        throw new UnsupportedOperationException("implement me");
    }

    // ------------------------------------------------------------------
    // Core dispatch
    // ------------------------------------------------------------------

    /**
     * Sends the notification to its recipient over all channels the user is subscribed
     * to for which a {@link NotificationChannel} is registered.
     *
     * <p>Processing order (per channel):
     * <ol>
     *   <li>Resolve the recipient {@link User}; fail immediately with a single
     *       {@link DeliveryResult.Failure} if not found.</li>
     *   <li>Apply the {@link NotificationTemplate} for {@link Notification#type()}
     *       (if registered) to produce the rendered body.</li>
     *   <li>Attempt delivery via the channel strategy, honouring the {@link RetryPolicy}.</li>
     *   <li>Notify all registered {@link DeliveryObserver}s with the result.</li>
     * </ol>
     *
     * @param notification the notification to dispatch; must not be {@code null}
     * @return a non-null, non-empty list of {@link DeliveryResult} — one per channel attempted
     * @throws IllegalArgumentException if the notification or its required fields are {@code null}
     */
    public List<DeliveryResult> send(Notification notification) {
        throw new UnsupportedOperationException("implement me");
    }
}
