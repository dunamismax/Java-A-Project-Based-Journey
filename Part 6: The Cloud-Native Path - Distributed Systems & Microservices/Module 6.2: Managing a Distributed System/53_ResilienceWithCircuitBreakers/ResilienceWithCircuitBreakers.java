
/**
 * This lesson introduces the **Circuit Breaker** pattern, a crucial technique for
 * building resilient, fault-tolerant microservices. We will use **Resilience4j**,
 * the standard resilience library for modern Java applications.
 *
 * ## The Problem: Cascading Failures
 *
 * In a microservice architecture, services often depend on each other. In our example,
 * the `product-service` calls the `user-service`. What happens if the `user-service`
 * is slow, unresponsive, or down?
 *
 * The threads in the `product-service` will block, waiting for a response. If requests
 * keep coming, all of its threads could become blocked, causing the `product-service`
 * itself to fail. This is a **cascading failure**, where one service's failure brings
 * down other services, potentially causing a system-wide outage.
 *
 * ## The Solution: The Circuit Breaker Pattern
 *
 * A circuit breaker acts like an electrical circuit breaker. It wraps a potentially
 * failing operation (like a network call) and monitors it for failures.
 *
 * ### It has three states:
 * 1.  **CLOSED:** (Normal state) Requests are allowed to pass through to the downstream service.
 * 2.  **OPEN:** If the number of failures exceeds a configured threshold, the circuit "opens."
 *     For a configured duration, it **immediately rejects** all further calls without even
 *     attempting to make the network request. This protects the calling service from being
 *     blocked and gives the downstream service time to recover.
 * 3.  **HALF-OPEN:** After the open duration, the circuit allows a limited number of "trial"
 *     requests through. If they succeed, it moves back to `CLOSED`. If they fail, it trips
 *     back to `OPEN`.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <!-- Resilience4j with Spring Boot 3 Starter -->
 * <dependency>
 *     <groupId>io.github.resilience4j</groupId>
 *     <artifactId>resilience4j-spring-boot3</artifactId>
 *     <version>2.2.0</version>
 * </dependency>
 * <!-- Spring AOP is needed for the annotations to work -->
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-aop</artifactId>
 * </dependency>
 * ```
 *
 * ## SETUP: Configuration in `application.properties` for the Product Service
 * ```properties
 * # Resilience4j Circuit Breaker configuration for an instance named "userService"
 * resilience4j.circuitbreaker.instances.userService.failure-rate-threshold=50
 * resilience4j.circuitbreaker.instances.userService.minimum-number-of-calls=5
 * resilience4j.circuitbreaker.instances.userService.sliding-window-type=COUNT_BASED
 * resilience4j.circuitbreaker.instances.userService.sliding-window-size=10
 * resilience4j.circuitbreaker.instances.userService.wait-duration-in-open-state=10s
 * ```
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Start ONLY the Product Service:** `java com.yourpackage.ProductServiceApplication`
 *     *Do NOT start the User Service. We want to simulate it being unavailable.*
 * 2.  **Repeatedly call the endpoint that depends on the User Service.**
 *     In a terminal, run this command several times: `curl -i http://localhost:8082/products/101/details`
 * 3.  **Observe the behavior:**
 *     - The **first few requests** will hang for a moment and then fail with a 500 error. This is the circuit breaker in its `CLOSED` state, trying to make the call.
 *     - After enough failures, the circuit will **OPEN**. Subsequent requests will fail **instantly** with a `CircuitBreaker 'userService' is open` error message. This is the pattern in action!
 *     - **Wait 10 seconds** for the circuit to become `HALF-OPEN`. Try the `curl` command again. It will attempt another real call.
 *     - Now, start the `UserServiceApplication` in another terminal and call the endpoint again. The request will succeed, and the circuit will move back to `CLOSED`.
 */

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// =====================================================================================
// --- USER SERVICE (Runs on Port 8081) - Keep this one stopped to test the failure ---
// =====================================================================================

@SpringBootApplication
class UserServiceApplication {
    public static void main(String[] args) {
        // This service is intentionally left simple. Its existence (or lack thereof)
        // is what we are testing in the other service.
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8081"));
        app.run(args);
    }

    record User(Integer id, String username) {
    }

    @RestController
    class UserController {
        @GetMapping("/users/{id}")
        public User getUser(@PathVariable int id) {
            return new User(id, "Alice");
        }
    }
}

// =====================================================================================
// --- PRODUCT SERVICE (Runs on Port 8082) - WITH CIRCUIT BREAKER ---
// =====================================================================================

@SpringBootApplication
class ProductServiceApplication {
    public static void main(String[] args) {
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

record User(Integer id, String username) {
}

record ProductDetailDto(Integer id, String name, User productOwner) {
}

@Service
class ProductDetailService {
    private final RestTemplate restTemplate;

    public ProductDetailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // This method is now protected by a circuit breaker named "userService".
    // The configuration for this instance is pulled from application.properties.
    @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
    public ProductDetailDto getProductDetails(int productId) {
        System.out.println("--- Attempting to call User Service ---");
        // For demonstration, we'll hard-code the product and focus on the remote call.
        Product product = new Product(productId, "Laptop Pro", 1);
        String userServiceUrl = "http://localhost:8081/users/" + product.userId();

        // This is the call that will fail if UserService is down.
        User user = restTemplate.getForObject(userServiceUrl, User.class);

        return new ProductDetailDto(product.id(), product.name(), user);
    }

    // This is the **fallback method**. It must have the same return type and
    // arguments as the original method, plus a Throwable argument.
    // It is called automatically by Resilience4j when the circuit is OPEN
    // or when an exception occurs that trips the circuit.
    public ProductDetailDto getUserFallback(int productId, Throwable t) {
        System.out.println("--- Circuit breaker engaged! Executing fallback for productId: " + productId + " ---");
        System.out.println("--- Underlying error: " + t.getMessage() + " ---");
        // We return a default/cached/partial response instead of letting the error
        // propagate.
        User fallbackUser = new User(1, "Default User (Service Unavailable)");
        Product product = new Product(productId, "Laptop Pro", 1);
        return new ProductDetailDto(product.id(), product.name(), fallbackUser);
    }
}

@RestController
class ProductController {
    private final ProductDetailService service;

    public ProductController(ProductDetailService s) {
        this.service = s;
    }

    @GetMapping("/products/{id}/details")
    public ResponseEntity<ProductDetailDto> getProductDetails(@PathVariable int id) {
        ProductDetailDto dto = service.getProductDetails(id);
        return ResponseEntity.ok(dto);
    }
}