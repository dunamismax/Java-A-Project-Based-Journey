
/**
 * This lesson introduces **Distributed Tracing**, a technique used to monitor and
 * profile requests as they flow through a distributed system.
 *
 * ## The Problem: Lost in a Sea of Services
 *
 * In a microservice architecture, a single user action can trigger a complex chain of
 * calls across multiple services. If a request is slow or fails, how do you determine
 * which service is the bottleneck or the source of the error?
 *
 * With structured logging alone, you can find all logs for a specific `orderId`, but
 * you can't easily see the causal relationships, timing, and hierarchy between them.
 *
 * ## The Solution: Distributed Tracing
 *
 * Distributed Tracing solves this by assigning a unique **Trace ID** to every incoming
 * external request. This Trace ID is then passed along in the headers of every subsequent
 * service-to-service call.
 *
 * Each individual operation within a service (like an HTTP call or a database query) is
 * recorded as a **Span**, which also contains the shared Trace ID.
 *
 * ### The Components:
 * 1.  **Instrumentation Library (Micrometer Tracing):** Spring Boot integrates with Micrometer
 *     Tracing, which automatically instruments your application. It intercepts incoming
 *     requests to start a new trace and outgoing requests to propagate the trace context.
 * 2.  **Tracer (OpenZipkin Brave):** The underlying library that implements the tracing logic.
 * 3.  **Exporter:** A component that sends the collected trace data to a tracing backend.
 * 4.  **Tracing Backend (Zipkin):** A dedicated server that receives trace data, stores it,
 *     and provides a UI to visualize the entire request as a "flame graph."
 *
 * ## PREQUISITES:
 * 1.  **Docker Desktop** must be running to start Zipkin.
 * 2.  **Maven `pom.xml` Dependencies** for ALL microservices (Gateway, User, Product):
 *     ```xml
 *     <!-- Micrometer Tracing with the OpenZipkin Brave Bridge -->
 *     <dependency>
 *         <groupId>io.micrometer</groupId>
 *         <artifactId>micrometer-tracing-bridge-brave</artifactId>
 *     </dependency>
 *
 *     <!-- Exporter to send traces to Zipkin -->
 *     <dependency>
 *         <groupId>io.zipkin.reporter2</groupId>
 *         <artifactId>zipkin-reporter-brave</artifactId>
 *     </dependency>
 *     ```
 *
 * 3.  **Configuration in `application.properties`** for ALL microservices:
 *     ```properties
 *     # The probability of a trace being sampled and sent. 1.0 means trace every request.
 *     spring.management.tracing.sampling.probability=1.0
 *     # The endpoint of the Zipkin server running in Docker.
 *     spring.zipkin.base-url=http://localhost:9411/
 *     ```
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Start Zipkin using Docker (Terminal 1):**
 *     `docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin`
 * 2.  **Start User Service (Terminal 2):** `java com.yourpackage.UserServiceApplication`
 * 3.  **Start Product Service (Terminal 3):** `java com.yourpackage.ProductServiceApplication`
 * 4.  **Start API Gateway (Terminal 4):** `java com.yourpackage.ApiGatewayApplication`
 *     *Check the logs of all three services. You will now see log messages prefixed with
 *     `[service-name,TRACE_ID,SPAN_ID]`! Micrometer automatically adds this context.*
 * 5.  **Make a request that goes through the whole system:**
 *     `curl http://localhost:8080/products/101/details`
 * 6.  **Visualize the Trace:**
 *     - Open the Zipkin UI in your browser: `http://localhost:9411`
 *     - Click the "Run Query" button. You will see your trace!
 *     - Click on the trace to see a detailed flame graph showing the timing and relationship
 *       of the calls from the Gateway to the Product Service, and from the Product Service
 *       to the User Service.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// =====================================================================================
// --- API GATEWAY, USER SERVICE, and PRODUCT SERVICE ---
// No Java code changes are needed! The magic is handled by the dependencies and
// configuration. We just need to define the main application classes.
// =====================================================================================

// --- API GATEWAY ---
@SpringBootApplication
class ApiGatewayApplication {
    public static void main(String[] args) {
        // Needs application.properties with a port, app name, and Zipkin URL.
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r.path("/products/**").uri("http://localhost:8082"))
                .build();
    }
}

// --- USER SERVICE ---
@SpringBootApplication
class UserServiceApplication {
    public static void main(String[] args) {
        // Needs application.properties with a port, app name, and Zipkin URL.
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8081"));
        app.run(args);
    }

    record User(Integer id, String username) {
    }

    @RestController
    class UserController {
        private static final Logger log = LoggerFactory.getLogger(UserController.class);

        @GetMapping("/users/{id}")
        public User getUser(@PathVariable int id) {
            log.info("Fetching user with ID: {}", id);
            return new User(id, "Alice");
        }
    }
}

// --- PRODUCT SERVICE ---
@SpringBootApplication
class ProductServiceApplication {
    public static void main(String[] args) {
        // Needs application.properties with a port, app name, and Zipkin URL.
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8082"));
        app.run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

record Product(Integer id, String name, Integer userId) {
}

record ProductDetailDto(Integer id, String name, UserServiceApplication.User productOwner) {
}

@Service
class ProductDetailService {
    private static final Logger log = LoggerFactory.getLogger(ProductDetailService.class);
    private final RestTemplate restTemplate;

    public ProductDetailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDetailDto getProductDetails(int productId) {
        log.info("Fetching details for product ID: {}", productId);
        Product product = new Product(productId, "Laptop Pro", 1);
        String userServiceUrl = "http://localhost:8081/users/" + product.userId();

        // Micrometer Tracing automatically adds the Trace ID headers to this outgoing
        // request.
        var user = restTemplate.getForObject(userServiceUrl, UserServiceApplication.User.class);
        log.info("Successfully fetched user details for product ID: {}", productId);
        return new ProductDetailDto(product.id(), product.name(), user);
    }
}

@RestController
class ProductController {
    private final ProductDetailService service;

    public ProductController(ProductDetailService s) {
        this.service = s;
    }

    @GetMapping("/products/{id}/details")
    public ResponseEntity<ProductDetailDto> getDetails(@PathVariable int id) {
        return ResponseEntity.ok(service.getProductDetails(id));
    }
}