package com.example.notification.service;

import com.example.notification.model.EventType;
import org.springframework.stereotype.Service;

@Service
public class PushEventProcessor extends EventProcessor {

    @Override
    protected EventType getEventType() {
        return EventType.PUSH;
    }

    @Override
    protected long getProcessingTimeMillis() {
        return 2000; // 2 seconds
    }
}
