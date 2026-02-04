package com.example.notification.service;

import com.example.notification.model.EventType;
import org.springframework.stereotype.Service;

@Service
public class SmsEventProcessor extends EventProcessor {

    @Override
    protected EventType getEventType() {
        return EventType.SMS;
    }

    @Override
    protected long getProcessingTimeMillis() {
        return 3000;
    }
}
