
/**
 * This lesson introduces the **API Gateway** pattern, a fundamental component for
 * managing and securing a microservice architecture.
 *
 * ## The Problem: Direct Client-to-Service Communication
 *
 * As the number of microservices grows, letting clients (like a web browser or mobile app)
 * talk directly to each service becomes problematic:
 * - **Complexity:** The client needs to know the address of every single microservice.
 * - **Security:** How do you handle authentication, authorization, and rate limiting for
 *   every service? You'd have to duplicate this logic everywhere.
 * - **Refactoring:** Renaming or splitting a service becomes a breaking change for all clients.
 *
 * ## The Solution: The API Gateway
 *
 * An API Gateway acts as a single entry point for all client requests. It's a reverse
 * proxy that sits in front of your microservices and routes requests to the appropriate
 * downstream service.
 *
 * ### Key Responsibilities of an API Gateway:
 * - **Routing:** Intelligently forwards requests based on the URL path.
 * - **Authentication & Security:** Acts as a single point for authenticating users.
 * - **Rate Limiting:** Protects your services from being overwhelmed.
 * - **Load Balancing:** Can distribute traffic across multiple instances of a service.
 * - **Request/Response Transformation:** Can modify requests and responses as they pass through.
 *
 * We will use **Spring Cloud Gateway**, the standard gateway implementation for Spring.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <!-- For the Gateway application -->
 * <dependency>
 *     <groupId>org.springframework.cloud</groupId>
 *     <artifactId>spring-cloud-starter-gateway</artifactId>
 * </dependency>
 *
 * <!-- For the User/Product services -->
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-web</artifactId>
 * </dependency>
 *
 * <!-- Make sure you have a Spring Cloud BOM (Bill of Materials) in your <dependencyManagement> -->
 * <dependencyManagement>
 *   <dependencies>
 *     <dependency>
 *       <groupId>org.springframework.cloud</groupId>
 *       <artifactId>spring-cloud-dependencies</artifactId>
 *       <version>2023.0.1</version>
 *       <type>pom</type>
 *       <scope>import</scope>
 *     </dependency>
 *   </dependencies>
 * </dependencyManagement>
 * ```
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Compile:** `mvn compile`
 * 2.  **Start the User Service (Terminal 1):** `java com.yourpackage.UserServiceApplication` (on port 8081)
 * 3.  **Start the Product Service (Terminal 2):** `java com.yourpackage.ProductServiceApplication` (on port 8082)
 * 4.  **Start the API Gateway Service (Terminal 3):** `java com.yourpackage.ApiGatewayApplication` (on port 8080)
 * 5.  **Test ALL requests through the Gateway on port 8080:**
 *
 *     - **Get users:** `curl http://localhost:8080/api/users` (Gateway routes to UserService)
 *     - **Get products:** `curl http://localhost:8080/api/products` (Gateway routes to ProductService)
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// =====================================================================================
// --- API GATEWAY SERVICE (Runs on Port 8080) ---
// =====================================================================================

@SpringBootApplication
public class TheAPIGatewayPattern {
    // This main method is a placeholder to keep the file structure consistent.
    // The actual applications to run are specified below.
    public static void main(String[] args) {
        System.out.println("This file contains three applications. Please run them as specified in the comments.");
    }
}

@SpringBootApplication
class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGatewayApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8080"));
        app.run(args);
    }

    // We define our routing rules as a Spring @Bean.
    // Spring Cloud Gateway provides a `RouteLocatorBuilder` to define these routes
    // using a fluent Java-based API.
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Define a route for the User Service
                .route("user_service_route", r -> r.path("/api/users/**") // When a request path matches this pattern...
                        .uri("http://localhost:8081")) // ...forward it to this URI.

                // Define a route for the Product Service
                .route("product_service_route", r -> r.path("/api/products/**")
                        .uri("http://localhost:8082"))

                .build();
    }
}

// =====================================================================================
// --- USER SERVICE (Runs on Port 8081) --- UNCHANGED ---
// =====================================================================================

@SpringBootApplication
class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8081"));
        app.run(args);
    }
}

record User(Integer id, String username) {
}

@RestController
class UserController {
    private final Map<Integer, User> userDb = new ConcurrentHashMap<>(Map.of(
            1, new User(1, "Alice"), 2, new User(2, "Bob")));

    @GetMapping("/api/users")
    public Collection<User> getAll() {
        return userDb.values();
    }
}

// =====================================================================================
// --- PRODUCT SERVICE (Runs on Port 8082) --- UNCHANGED ---
// =====================================================================================

@SpringBootApplication
class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8082"));
        app.run(args);
    }
}

record Product(Integer id, String name) {
}

@RestController
class ProductController {
    private final Map<Integer, Product> productDb = new ConcurrentHashMap<>(Map.of(
            101, new Product(101, "Laptop"), 102, new Product(102, "Mouse")));

    @GetMapping("/api/products")
    public Collection<Product> getAll() {
        return productDb.values();
    }
}