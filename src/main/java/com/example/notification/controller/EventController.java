package com.example.notification.controller;

import com.example.notification.dto.EventRequest;
import com.example.notification.dto.EventResponse;
import com.example.notification.model.EventStatus;
import com.example.notification.model.NotificationEvent;
import com.example.notification.service.EventQueueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventQueueService eventQueueService;

    @Autowired
    public EventController(EventQueueService eventQueueService) {
        this.eventQueueService = eventQueueService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> acceptEvent(@Valid @RequestBody EventRequest request) {
        String eventId = UUID.randomUUID().toString();

        NotificationEvent event = NotificationEvent.builder()
                .eventId(eventId)
                .eventType(request.getEventType())
                .payload(request.getPayload())
                .callbackUrl(request.getCallbackUrl())
                .status(EventStatus.PENDING)
                .build();

        eventQueueService.submit(event);

        return ResponseEntity.accepted().body(new EventResponse(eventId, "Event accepted for processing."));
    }
}
