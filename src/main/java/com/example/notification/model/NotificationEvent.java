package com.example.notification.model;

import java.time.LocalDateTime;
import java.util.Map;

public class NotificationEvent {
    private String eventId;
    private EventType eventType;
    private Map<String, Object> payload;
    private String callbackUrl;
    private EventStatus status;
    private LocalDateTime processedAt;
    private String errorMessage;

    public NotificationEvent() {
    }

    public NotificationEvent(String eventId, EventType eventType, Map<String, Object> payload, String callbackUrl,
            EventStatus status, LocalDateTime processedAt, String errorMessage) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
        this.callbackUrl = callbackUrl;
        this.status = status;
        this.processedAt = processedAt;
        this.errorMessage = errorMessage;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String eventId;
        private EventType eventType;
        private Map<String, Object> payload;
        private String callbackUrl;
        private EventStatus status;
        private LocalDateTime processedAt;
        private String errorMessage;

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder payload(Map<String, Object> payload) {
            this.payload = payload;
            return this;
        }

        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        public Builder status(EventStatus status) {
            this.status = status;
            return this;
        }

        public Builder processedAt(LocalDateTime processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public NotificationEvent build() {
            return new NotificationEvent(eventId, eventType, payload, callbackUrl, status, processedAt, errorMessage);
        }
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
