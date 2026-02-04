package com.example.notification.dto;

import com.example.notification.model.EventType;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class EventRequest {
    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotNull(message = "Payload is required")
    private Map<String, Object> payload;

    @NotNull(message = "Callback URL is required")
    private String callbackUrl;

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
}
