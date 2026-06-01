package com.venkat.backend.lld.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

// ---------------------------------------------------------------------------
// Enumerations
// ---------------------------------------------------------------------------

enum ChannelType { EMAIL, SMS, PUSH }

// ---------------------------------------------------------------------------
// Value objects / DTOs
// ---------------------------------------------------------------------------

record Notification(String recipientUserId, String type, String subject, String body, ChannelType channelType) {}

record User(String userId, String name, String email, String phone, String deviceToken) {}

record RetryPolicy(int maxAttempts, long delayMillis) {}

sealed interface DeliveryResult permits DeliveryResult.Success, DeliveryResult.Failure {
    ChannelType channelType();
    record Success(ChannelType channelType, long timestampEpochMs) implements DeliveryResult {}
    record Failure(ChannelType channelType, String reason, int attempts) implements DeliveryResult {}
}

// ---------------------------------------------------------------------------
// Strategy interface
// ---------------------------------------------------------------------------

interface NotificationChannel {
    ChannelType type();
    DeliveryResult send(User user, Notification notification);
}

// ---------------------------------------------------------------------------
// Template hook
// ---------------------------------------------------------------------------

@FunctionalInterface
interface NotificationTemplate {
    String render(Notification notification, User recipient);
}

// ---------------------------------------------------------------------------
// Observer interface
// ---------------------------------------------------------------------------

@FunctionalInterface
interface DeliveryObserver {
    void onDelivery(DeliveryResult result);
}

// ---------------------------------------------------------------------------
// Main service class
// ---------------------------------------------------------------------------

public class NotificationService {

    // Registries
    private final Map<ChannelType, NotificationChannel> channels = new EnumMap<>(ChannelType.class);
    private final Map<ChannelType, RetryPolicy>         retryPolicies = new EnumMap<>(ChannelType.class);
    private final Map<String, NotificationTemplate>     templates = new HashMap<>();
    private final CopyOnWriteArrayList<DeliveryObserver> observers = new CopyOnWriteArrayList<>();
    private final Map<String, User>                     users = new HashMap<>();
    // userId → set of subscribed channels
    private final Map<String, Set<ChannelType>>         subscriptions = new HashMap<>();

    private NotificationService() {}

    public static NotificationService create() {
        return new NotificationService();
    }

    // ---- Channel registration ----

    public void registerChannel(NotificationChannel channel) {
        Objects.requireNonNull(channel);
        channels.put(channel.type(), channel);
    }

    public void setRetryPolicy(ChannelType channelType, RetryPolicy policy) {
        Objects.requireNonNull(policy);
        retryPolicies.put(channelType, policy);
    }

    // ---- Template registration ----

    public void registerTemplate(String notificationType, NotificationTemplate template) {
        Objects.requireNonNull(notificationType);
        Objects.requireNonNull(template);
        templates.put(notificationType, template);
    }

    // ---- Observer registration ----

    public void addObserver(DeliveryObserver observer) {
        Objects.requireNonNull(observer);
        observers.add(observer);
    }

    public void removeObserver(DeliveryObserver observer) {
        observers.remove(observer);
    }

    // ---- User management ----

    public void registerUser(User user) {
        Objects.requireNonNull(user);
        users.put(user.userId(), user);
        subscriptions.putIfAbsent(user.userId(), EnumSet.noneOf(ChannelType.class));
    }

    // ---- Subscription management ----

    public void subscribe(String userId, ChannelType channelType) {
        requireRegistered(userId);
        subscriptions.get(userId).add(channelType);
    }

    public void unsubscribe(String userId, ChannelType channelType) {
        requireRegistered(userId);
        subscriptions.get(userId).remove(channelType);
    }

    // ---- Core dispatch ----

    public List<DeliveryResult> send(Notification notification) {
        Objects.requireNonNull(notification);

        User recipient = users.get(notification.recipientUserId());
        if (recipient == null) {
            DeliveryResult.Failure failure = new DeliveryResult.Failure(
                notification.channelType(), "Unknown user: " + notification.recipientUserId(), 1);
            notifyObservers(failure);
            return List.of(failure);
        }

        // Render body via template if registered
        String renderedBody = notification.body();
        NotificationTemplate template = templates.get(notification.type());
        if (template != null) renderedBody = template.render(notification, recipient);

        Notification rendered = new Notification(
            notification.recipientUserId(),
            notification.type(),
            notification.subject(),
            renderedBody,
            notification.channelType()
        );

        // Collect channels: the requested channel + all subscribed channels
        Set<ChannelType> targetChannels = EnumSet.noneOf(ChannelType.class);
        targetChannels.add(notification.channelType());
        Set<ChannelType> subs = subscriptions.getOrDefault(recipient.userId(), Collections.emptySet());
        targetChannels.addAll(subs);

        List<DeliveryResult> results = new ArrayList<>();
        for (ChannelType ct : targetChannels) {
            NotificationChannel channel = channels.get(ct);
            if (channel == null) continue; // no registered implementation; skip

            DeliveryResult result = dispatchWithRetry(channel, recipient, rendered);
            results.add(result);
            notifyObservers(result);
        }

        if (results.isEmpty()) {
            DeliveryResult.Failure failure = new DeliveryResult.Failure(
                notification.channelType(), "No registered channel for " + notification.channelType(), 1);
            notifyObservers(failure);
            results.add(failure);
        }
        return Collections.unmodifiableList(results);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private DeliveryResult dispatchWithRetry(NotificationChannel channel, User user, Notification notification) {
        RetryPolicy policy = retryPolicies.getOrDefault(channel.type(), new RetryPolicy(1, 0));
        int attempts = 0;
        while (attempts < policy.maxAttempts()) {
            attempts++;
            try {
                DeliveryResult result = channel.send(user, notification);
                if (result instanceof DeliveryResult.Success) return result;
                // Permanent failure — don't retry
                return new DeliveryResult.Failure(channel.type(), ((DeliveryResult.Failure) result).reason(), attempts);
            } catch (RuntimeException e) {
                if (attempts >= policy.maxAttempts()) {
                    return new DeliveryResult.Failure(channel.type(), e.getMessage(), attempts);
                }
                if (policy.delayMillis() > 0) {
                    try { Thread.sleep(policy.delayMillis()); } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return new DeliveryResult.Failure(channel.type(), "Interrupted", attempts);
                    }
                }
            }
        }
        return new DeliveryResult.Failure(channel.type(), "Max attempts reached", attempts);
    }

    private void requireRegistered(String userId) {
        if (!users.containsKey(userId))
            throw new IllegalArgumentException("Unknown user: " + userId);
    }

    private void notifyObservers(DeliveryResult result) {
        for (DeliveryObserver observer : observers) {
            try { observer.onDelivery(result); } catch (Exception ignored) {}
        }
    }
}
