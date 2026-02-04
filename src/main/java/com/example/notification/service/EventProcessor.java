package com.example.notification.service;

import com.example.notification.model.EventStatus;
import com.example.notification.model.EventType;
import com.example.notification.model.NotificationEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class EventProcessor {

    private static final Logger log = LoggerFactory.getLogger(EventProcessor.class);

    @Autowired
    private EventQueueService eventQueueService;

    @Autowired
    private CallbackService callbackService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Random random = new Random();

    protected abstract EventType getEventType();

    protected abstract long getProcessingTimeMillis();

    @PostConstruct
    public void start() {
        executorService.submit(this::processQueue);
    }

    private void processQueue() {
        BlockingQueue<NotificationEvent> queue = eventQueueService.getQueue(getEventType());
        log.info("Started processor for {}", getEventType());

        while (running.get() || !queue.isEmpty()) {
            try {
                // Poll with timeout to allow checking running flag periodically if queue is
                // empty
                NotificationEvent event = queue.poll(1, TimeUnit.SECONDS);
                if (event == null)
                    continue;

                processEvent(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Processor for {} interrupted", getEventType());
            } catch (Exception e) {
                log.error("Error in processor for {}", getEventType(), e);
            }
        }
        log.info("Processor for {} stopped", getEventType());
    }

    private void processEvent(NotificationEvent event) {
        event.setStatus(EventStatus.PROCESSING);
        log.info("Processing event: {}", event.getEventId());

        try {
            Thread.sleep(getProcessingTimeMillis());

            // Simulate 10% failure
            if (random.nextInt(100) < 10) {
                throw new RuntimeException("Simulated processing failure");
            }

            event.setStatus(EventStatus.COMPLETED);
            event.setProcessedAt(LocalDateTime.now());
            log.info("Completed event: {}", event.getEventId());

        } catch (Exception e) {
            event.setStatus(EventStatus.FAILED);
            event.setProcessedAt(LocalDateTime.now());
            event.setErrorMessage(e.getMessage());
            log.error("Failed event: {}", event.getEventId());
        } finally {
            callbackService.sendCallback(event);
        }
    }

    @PreDestroy
    public void stop() {
        running.set(false);
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
