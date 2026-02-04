package com.example.notification.service;

import com.example.notification.model.EventType;
import com.example.notification.model.NotificationEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventQueueServiceTest {

    @Test
    public void testSubmitAndRetrieve() {
        EventQueueService service = new EventQueueService();
        NotificationEvent event = NotificationEvent.builder()
                .eventId("1")
                .eventType(EventType.EMAIL)
                .build();

        service.submit(event);

        Assertions.assertEquals(1, service.getEmailQueue().size());
        Assertions.assertEquals(event, service.getEmailQueue().poll());

        Assertions.assertTrue(service.getSmsQueue().isEmpty());
        Assertions.assertTrue(service.getPushQueue().isEmpty());
    }
}
