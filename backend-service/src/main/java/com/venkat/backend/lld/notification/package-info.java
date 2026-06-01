/**
 * <h2>LLD Practice — Notification Service</h2>
 *
 * <h3>Problem Statement</h3>
 * <p>
 * Design and implement a <strong>Notification Service</strong> that can send notifications to
 * users over multiple channels: EMAIL, SMS, and PUSH. The service must support:
 * <ul>
 *   <li>A pluggable channel strategy so that new channels can be added without modifying core logic.</li>
 *   <li>A user-subscription model (Observer pattern) where users declare which channels they want to receive notifications on.</li>
 *   <li>A templating hook so that notification content can be rendered from a template before dispatch.</li>
 *   <li>A configurable retry policy (max attempts + backoff) per channel.</li>
 * </ul>
 * </p>
 *
 * <h3>Functional Requirements</h3>
 * <ol>
 *   <li><strong>FR-1</strong> — The service SHALL accept a {@code Notification} (recipient user ID, subject,
 *       body, preferred channel) and dispatch it to that user over their subscribed channels.</li>
 *   <li><strong>FR-2</strong> — A {@code User} can subscribe to or unsubscribe from one or more
 *       {@code ChannelType}s at any time.</li>
 *   <li><strong>FR-3</strong> — Before dispatch, the notification body SHALL be passed through a
 *       {@code NotificationTemplate} (if one is registered for the notification type).</li>
 *   <li><strong>FR-4</strong> — If a channel fails to deliver, the service SHALL retry up to the
 *       configured maximum attempts using a {@code RetryPolicy}. Exhausted retries must be
 *       observable (delivery result returned / event fired).</li>
 *   <li><strong>FR-5</strong> — A new channel (e.g. SLACK) MUST be registerable at runtime by
 *       providing a new {@code NotificationChannel} implementation — no existing class changes required.</li>
 *   <li><strong>FR-6</strong> — The service SHALL return a {@code DeliveryResult} per channel attempt
 *       indicating success or failure with a reason.</li>
 * </ol>
 *
 * <h3>Non-Functional Requirements</h3>
 * <ol>
 *   <li><strong>NFR-1 Thread-safety</strong> — The user subscription registry and channel registry
 *       must be safe for concurrent read/write access.</li>
 *   <li><strong>NFR-2 Extensibility</strong> — Adding a new channel, template, or retry policy
 *       must not require modifying {@code NotificationService} or any existing channel class
 *       (Open/Closed Principle).</li>
 *   <li><strong>NFR-3 Observability</strong> — Callers can attach a {@code DeliveryObserver} to
 *       receive callbacks after each send attempt (success or failure), enabling logging/metrics.</li>
 *   <li><strong>NFR-4 No frameworks</strong> — Implementation uses only {@code java.util.*},
 *       {@code java.util.concurrent.*}, and {@code java.util.function.*}. No Spring, no Lombok.</li>
 * </ol>
 *
 * <h3>Suggested Entities / Vocabulary</h3>
 * <ul>
 *   <li>{@code ChannelType} — enum: EMAIL, SMS, PUSH (and anything a learner adds).</li>
 *   <li>{@code Notification} — value object: recipientUserId, type, subject, body, channelType.</li>
 *   <li>{@code User} — entity: userId, name, email address, phone number, device token; holds a
 *       set of subscribed channel types.</li>
 *   <li>{@code NotificationChannel} — <em>Strategy</em> interface: {@code ChannelType type()},
 *       {@code DeliveryResult send(User, Notification)}.</li>
 *   <li>{@code NotificationTemplate} — functional interface: {@code String render(Notification, User)}.</li>
 *   <li>{@code RetryPolicy} — value object: maxAttempts, delayMillis; a {@code RetryExecutor}
 *       uses it to wrap a channel call.</li>
 *   <li>{@code DeliveryResult} — sealed result: {@code Success(channelType, timestamp)} /
 *       {@code Failure(channelType, reason, attempts)}.</li>
 *   <li>{@code DeliveryObserver} — <em>Observer</em> interface: {@code void onDelivery(DeliveryResult)}.</li>
 *   <li>{@code NotificationService} — orchestrator: register channels/templates, manage subscriptions,
 *       dispatch, apply retry, notify observers.</li>
 * </ul>
 *
 * <h3>Public API (what the skeleton exposes)</h3>
 * <pre>{@code
 * // Bootstrap
 * NotificationService svc = NotificationService.create();
 * svc.registerChannel(new EmailChannel());          // Strategy plug-in
 * svc.setRetryPolicy(ChannelType.EMAIL, new RetryPolicy(3, 200));
 * svc.registerTemplate("WELCOME", (n, u) -> "Hi " + u.name() + ", " + n.body());
 * svc.addObserver(result -> System.out.println(result));
 *
 * // Subscription management (Observer pattern)
 * svc.subscribe(userId, ChannelType.EMAIL);
 * svc.unsubscribe(userId, ChannelType.SMS);
 *
 * // Dispatch
 * List<DeliveryResult> results = svc.send(notification);
 * }</pre>
 *
 * <h3>Design Patterns to Consider</h3>
 * <ul>
 *   <li><strong>Strategy</strong> — {@code NotificationChannel} lets you swap or add delivery
 *       mechanisms without changing the service orchestrator.</li>
 *   <li><strong>Observer</strong> — {@code DeliveryObserver} decouples result consumers
 *       (loggers, metrics sinks) from the send path.</li>
 *   <li><strong>Template Method / Functional Interface</strong> — {@code NotificationTemplate}
 *       as a {@code @FunctionalInterface} allows lambda-based registration.</li>
 *   <li><strong>Registry / Factory</strong> — A {@code Map<ChannelType, NotificationChannel>}
 *       inside the service acts as a runtime registry; consider whether the service itself
 *       should be a singleton (static factory vs constructor).</li>
 *   <li><strong>Decorator</strong> — {@code RetryExecutor} can wrap any {@code NotificationChannel}
 *       transparently, keeping retry logic out of the channel implementations.</li>
 * </ul>
 *
 * <h3>Follow-Up Questions</h3>
 * <ol>
 *   <li>How would you handle a user who has subscribed to EMAIL but has no email address set?
 *       Should the service fail fast, skip silently, or surface an error in the result?</li>
 *   <li>If two threads call {@code subscribe} and {@code send} concurrently for the same user,
 *       what consistency guarantees does your implementation provide?</li>
 *   <li>The retry policy uses a fixed delay. How would you extend it to support exponential
 *       backoff with jitter while keeping the {@code RetryPolicy} value object simple?</li>
 * </ol>
 *
 * <h3>Extensibility / Concurrency Stretch Goal</h3>
 * <p>
 * Extend the service to dispatch to all of a user's subscribed channels <em>concurrently</em>
 * using a {@code java.util.concurrent.ExecutorService}. Ensure that:
 * (a) the observer callbacks are still invoked for each result even if one channel throws,
 * (b) the {@code send} method waits for all channel tasks to complete before returning the
 * aggregated {@code List<DeliveryResult>},
 * (c) the executor is injected (constructor or factory parameter) so it can be replaced in tests.
 * </p>
 *
 * @see com.venkat.backend.lld.notification.NotificationService
 */
package com.venkat.backend.lld.notification;
