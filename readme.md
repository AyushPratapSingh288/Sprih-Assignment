A Java-based asynchronous event notification system that processes events through channel-specific FIFO queues.

- **Multi-Channel Support**: Handles `EMAIL`, `SMS`, and `PUSH` notifications.
- **Asynchronous Processing**: Each event type is processed in its own asynchronous FIFO queue to ensure isolation and scalability.
- **Callback Notifications**: Notifies clients of the final status (SUCCESS/FAILURE) via a provided callback URL.
- **Dockerized**: Easy deployment using Docker and Docker Compose.
- **Graceful Shutdown**: Ensures all pending events are processed or safely stored before the system shuts down.
- **Comprehensive API**: Simple REST API to submit notification events.


### Building the Project

```bash
./mvnw clean install
```

### Running Locally

```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`.

### Running with Docker

```bash
docker-compose up --build
```


{
  "eventType": "EMAIL",
  "payload": {
    "to": "user@example.com",
    "subject": "Welcome!",
    "body": "Thank you for joining our platform."
  },
  "callbackUrl": "http://here i am using a another server just to receive callbacks/callback"
}
