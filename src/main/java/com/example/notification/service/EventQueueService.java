package com.example.notification.service;

import com.example.notification.model.EventType;
import com.example.notification.model.NotificationEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EventQueueService {

    private final BlockingQueue<NotificationEvent> emailQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<NotificationEvent> smsQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<NotificationEvent> pushQueue = new LinkedBlockingQueue<>();

    public void submit(NotificationEvent event) {
        switch (event.getEventType()) {
            case EMAIL -> emailQueue.offer(event);
            case SMS -> smsQueue.offer(event);
            case PUSH -> pushQueue.offer(event);
            default -> throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        }
    }

    public BlockingQueue<NotificationEvent> getQueue(EventType type) {
        return switch (type) {
            case EMAIL -> emailQueue;
            case SMS -> smsQueue;
            case PUSH -> pushQueue;
        };
    }

    public BlockingQueue<NotificationEvent> getEmailQueue() {
        return emailQueue;
    }

    public BlockingQueue<NotificationEvent> getSmsQueue() {
        return smsQueue;
    }

    public BlockingQueue<NotificationEvent> getPushQueue() {
        return pushQueue;
    }
}
