package com.example.assignment;

import com.example.notification.dto.EventRequest;
import com.example.notification.dto.EventResponse;
import com.example.notification.model.EventStatus;
import com.example.notification.model.EventType;
import com.example.notification.service.EventQueueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventQueueService eventQueueService;

    // A simple endpoint to catch callbacks
    @RestController
    public static class TestCallbackController {
        public static final BlockingQueue<Map<String, Object>> callbacks = new LinkedBlockingQueue<>();

        @PostMapping("/test-callback")
        public void receiveCallback(@RequestBody Map<String, Object> body) {
            callbacks.offer(body);
        }
    }

    @Test
    public void testFullFlow_PushNotification() throws InterruptedException {
        // Push is fastest (2s)
        String callbackUrl = "http://localhost:" + port + "/test-callback";

        EventRequest request = new EventRequest();
        request.setEventType(EventType.PUSH);
        request.setCallbackUrl(callbackUrl);
        Map<String, Object> payload = new HashMap<>();
        payload.put("deviceId", "123");
        request.setPayload(payload);

        // 1. Send Event
        ResponseEntity<EventResponse> response = restTemplate.postForEntity("/api/events", request,
                EventResponse.class);
        Assertions.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody().getEventId());

        // 2. Verify Queue (immediately after submit, might be picked up already, but
        // queue service logic works)
        // Since processor runs in background, we might check queue if we are fast
        // enough,
        // but robust test relies on effect (callback).

        // 3. Wait for Callback (Push takes 2s + overhead)
        Map<String, Object> callbackData = TestCallbackController.callbacks.poll(5, TimeUnit.SECONDS);
        Assertions.assertNotNull(callbackData, "Callback was not received within timeout");

        Assertions.assertEquals(response.getBody().getEventId(), callbackData.get("eventId"));
        Assertions.assertEquals("PUSH", callbackData.get("eventType"));
        // Status could be COMPLETED or FAILED (random 10%)
        String status = (String) callbackData.get("status");
        Assertions.assertTrue("COMPLETED".equals(status) || "FAILED".equals(status));
    }

    @Test
    public void testValidationFailure() {
        EventRequest request = new EventRequest();
        // Missing fields

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/events", request, Void.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
