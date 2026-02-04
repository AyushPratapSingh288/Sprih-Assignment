package com.example.notification.service;

import com.example.notification.model.EventType;
import org.springframework.stereotype.Service;

@Service
public class EmailEventProcessor extends EventProcessor {

    @Override
    protected EventType getEventType() {
        return EventType.EMAIL;
    }

    @Override
    protected long getProcessingTimeMillis() {
        return 5000;
    }
}
