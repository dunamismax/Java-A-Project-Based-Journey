
/**
 * This lesson introduces **asynchronous communication** using a **message queue**,
 * a fundamental pattern for building decoupled, resilient, and scalable microservices.
 *
 * ## The Problem with Synchronous Communication
 *
 * Synchronous REST calls (from the last few lessons) create **tight coupling**. The
 * `product-service` is directly dependent on the `notification-service` being available
 * and fast. If the `notification-service` is down, the product creation process fails.
 * This is inefficient and not resilient.
 *
 * ## The Solution: Event-Driven Architecture with a Message Queue
 *
 * Instead of making a direct API call, services communicate indirectly by publishing
 * events (messages) to a central **message broker** (like RabbitMQ, Kafka, etc.).
 *
 * ### The New Flow:
 * 1.  The `ProductService` creates a product and then publishes a `ProductCreatedEvent`
 *     message to a "queue" in the message broker. Its job is now done. It returns
 *     a response to the user immediately.
 * 2.  The `NotificationService`, completely independent of the `ProductService`, is a
 *     "listener" or "consumer" of that queue.
 * 3.  The message broker delivers the event to the `NotificationService`, which then
 *     processes it (e.g., sends an email) on its own time.
 *
 * ### Key Benefits:
 * - **Decoupling:** Services don't need to know about each other's location or availability.
 * - **Resilience:** If the `NotificationService` is down, the messages simply wait in
 *   the queue until it comes back online. The product creation process is unaffected.
 * - **Scalability:** You can add more instances of the `NotificationService` to process
 *   messages from the queue in parallel, handling high loads easily.
 *
 * ## PREQUISITES:
 * 1.  **Docker Desktop** must be running to start a RabbitMQ container.
 * 2.  **Maven `pom.xml` Dependencies** for both services:
 *     ```xml
 *     <!-- Spring AMQP for RabbitMQ -->
 *     <dependency>
 *         <groupId>org.springframework.boot</groupId>
 *         <artifactId>spring-boot-starter-amqp</artifactId>
 *     </dependency>
 *     ```
 *
 * 3.  **Configuration in `application.properties`** for both services:
 *     ```properties
 *     # Connect to the RabbitMQ instance running in Docker
 *     spring.rabbitmq.host=localhost
 *     spring.rabbitmq.port=5672
 *     spring.rabbitmq.username=guest
 *     spring.rabbitmq.password=guest
 *     ```
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Start RabbitMQ using Docker (Terminal 1):**
 *     `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management`
 *     (The management UI will be available at http://localhost:15672, user/pass: guest/guest)
 * 2.  **Start the Notification Service (Terminal 2):** `java com.yourpackage.NotificationServiceApplication`
 * 3.  **Start the Product Service (Terminal 3):** `java com.yourpackage.ProductServiceApplication`
 * 4.  **Create a new product using `curl`:**
 *     `curl -X POST -H "Content-Type: application/json" -d '{"name":"New Smart TV", "price":899.99}' http://localhost:8082/products`
 * 5.  **Observe the logs:**
 *     - The `ProductService` will log that it has PUBLISHED an event.
 *     - Almost immediately, the `NotificationService` will log that it has RECEIVED and processed the event. This demonstrates the asynchronous, decoupled communication.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

// =====================================================================================
// --- A Shared "Event" DTO ---
// =====================================================================================
// This record represents the message that will be sent over the queue.
// It must implement Serializable for the message converter.
record ProductCreatedEvent(Integer productId, String productName) implements Serializable {
}

// =====================================================================================
// --- NOTIFICATION SERVICE (The Message Consumer) ---
// =====================================================================================

@SpringBootApplication
class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotificationServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8083"));
        app.run(args);
    }

    // This Bean defines the queue that our listener will consume from.
    // Spring AMQP will create this queue in RabbitMQ if it doesn't already exist.
    @Bean
    public Queue productEventsQueue() {
        return new Queue("q.product-created-events");
    }
}

@Component
class ProductEventListener {
    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    // @RabbitListener marks this method as a message consumer.
    // It will automatically be invoked whenever a message appears on the specified
    // queue.
    @RabbitListener(queues = "q.product-created-events")
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        log.info("RECEIVED EVENT: A new product was created: ID={}, Name='{}'", event.productId(), event.productName());
        // In a real application, you would send an email, a push notification, etc.
        System.out.println("-> Sending confirmation email for product: " + event.productName());
    }
}

// =====================================================================================
// --- PRODUCT SERVICE (The Message Publisher) ---
// =====================================================================================

@SpringBootApplication
class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8082"));
        app.run(args);
    }
}

record Product(Integer id, String name, double price) {
}

record CreateProductRequest(String name, double price) {
}

@RestController
class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final RabbitTemplate rabbitTemplate;

    // Spring Boot auto-configures RabbitTemplate, so we can just inject it.
    public ProductController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody CreateProductRequest request) {
        // 1. Create the product (in a real app, this would be saved to a database).
        Product newProduct = new Product(101, request.name(), request.price());
        log.info("Product created with ID: {}", newProduct.id());

        // 2. Create the event DTO.
        ProductCreatedEvent event = new ProductCreatedEvent(newProduct.id(), newProduct.name());

        // 3. Publish the event to the message queue.
        // `convertAndSend` serializes the Java object and sends it to the specified
        // queue.
        rabbitTemplate.convertAndSend("q.product-created-events", event);
        log.info("PUBLISHED EVENT to queue 'q.product-created-events' for product ID: {}", newProduct.id());

        // 4. Return the response to the client immediately.
        return newProduct;
    }
}