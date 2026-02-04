package com.example.notification.service;

import com.example.notification.model.NotificationEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class CallbackService {

    private static final Logger log = LoggerFactory.getLogger(CallbackService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendCallback(NotificationEvent event) {
        if (event.getCallbackUrl() == null || event.getCallbackUrl().isEmpty()) {
            return;
        }

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("eventId", event.getEventId());
            body.put("status", event.getStatus());
            body.put("eventType", event.getEventType());
            body.put("processedAt", event.getProcessedAt());

            if (event.getErrorMessage() != null) {
                body.put("errorMessage", event.getErrorMessage());
            }

            log.info("Sending callback for event {} to {}", event.getEventId(), event.getCallbackUrl());
            // In a real system, we'd handle retries and timeouts.
            // For this assignment, we just fire and log error on failure.
            restTemplate.postForEntity(event.getCallbackUrl(), body, String.class);

        } catch (Exception e) {
            log.error("Failed to send callback for event {}: {}", event.getEventId(), e.getMessage());
        }
    }
}
