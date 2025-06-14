# Lesson 55: Structured Logging in a Microservice Environment

This lesson is a conceptual and practical guide to implementing **structured logging**, an essential practice for monitoring and debugging distributed systems.

## The Problem: Unsearchable, Human-Readable Logs

In a microservice architecture, a single user request can trigger a chain of events across multiple services. When an error occurs, you need to trace that request through every service it touched.

Traditional, human-readable log formats are difficult for machines to parse:

```
INFO 14:32:01.123 --- [http-nio-8081-exec-1] c.e.UserService : Fetching user with ID 101
WARN 14:32:01.456 --- [http-nio-8082-exec-5] c.e.OrderService: Inventory level for product 55 is low.
ERROR 14:32:01.789 --- [http-nio-8083-exec-2] c.e.PaymentService: Payment failed for order 9001.
```

Trying to find all logs related to a single operation (e.g., "order 9001") requires complex and slow text searches across logs from multiple services.

## The Solution: Structured, Machine-Readable Logs (JSON)

Structured logging is the practice of writing logs in a consistent, machine-readable format, most commonly **JSON**. Each log entry is a JSON object with key-value pairs.

The same logs from above would now look like this:

```json
{"@timestamp":"2025-06-12T14:32:01.123Z", "level":"INFO", "service":"user-service", "thread":"http-nio-8081-exec-1", "logger":"c.e.UserService", "message":"Fetching user", "user_id":101}
{"@timestamp":"2025-06-12T14:32:01.456Z", "level":"WARN", "service":"order-service", "thread":"http-nio-8082-exec-5", "logger":"c.e.OrderService", "message":"Inventory level low", "product_id":55}
{"@timestamp":"2025-06-12T14:32:01.789Z", "level":"ERROR", "service":"payment-service", "thread":"http-nio-8083-exec-2", "logger":"c.e.PaymentService", "message":"Payment failed", "order_id":9001}
```

### Key Benefits:

*   **Searchable:** You can easily run powerful, fast queries like `level:ERROR AND service:payment-service`.
*   **Filterable:** You can filter logs by any field, such as `user_id:101`, to see the entire history of that user's interactions across all services.
*   **Analytics & Visualization:** Structured logs can be easily ingested into log management platforms (like the ELK Stack, Splunk, or Datadog) to create dashboards and alerts.

---

## How to Implement Structured Logging with Logback

We can achieve this without changing any of our Java code (`log.info(...)`). We only need to add a new dependency and update our `logback.xml` configuration.

### 1. Add the `logstash-logback-encoder` Dependency

This library provides a powerful JSON encoder for Logback. Add it to the `pom.xml` of **all** your microservices.

**File: `pom.xml`**
```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version> <!-- Use a recent version -->
</dependency>
```

### 2. Update `logback.xml` to Use the JSON Encoder

Replace the contents of `src/main/resources/logback.xml` in each microservice with the following. This configuration tells Logback to stop printing plain text and start printing JSON to the console.

**File: `src/main/resources/logback.xml`**
```xml
<configuration>
    <appender name="STDOUT_JSON" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <!--
                This encoder produces a rich JSON object.
                You can customize the output further if needed.
                See the logstash-logback-encoder documentation for options.
            -->
        </encoder>
    </appender>

    <!--
        This adds the 'service_name' property to every single log message,
        which is crucial for distinguishing logs in a central platform.
        The 'spring.application.name' is read from application.properties.
    -->
    <springProperty scope="context" name="SERVICE_NAME" source="spring.application.name" defaultValue="unknown-service"/>
    <contextName>${SERVICE_NAME}</contextName>


    <root level="info">
        <appender-ref ref="STDOUT_JSON" />
    </root>

</configuration>
```

### 3. Add `spring.application.name`
Ensure each microservice has its name defined in its `application.properties` (or `bootstrap.properties`), as this is what populates the `service` field in the logs.

**File: `user-service/src/main/resources/application.properties`**
```properties
spring.application.name=user-service
server.port=8081
```

---

## The Result

Now, when you run any of your microservices, the console output will be a stream of JSON objects. While less readable for a human in the console, it's incredibly powerful for machines.

**Old Log:**
`INFO --- [main] c.e.UserService: Starting User Service...`

**New Structured Log:**
`{"@timestamp":"...", "level":"INFO", "thread_name":"main", "logger_name":"c.e.UserService", "message":"Starting User Service...", "context":"user-service"}`

This simple change is a foundational step for achieving **observability** in a distributed system, which we will explore further in the next lessons on distributed tracing and metrics.