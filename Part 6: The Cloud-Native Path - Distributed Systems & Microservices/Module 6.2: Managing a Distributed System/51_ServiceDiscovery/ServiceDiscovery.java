
/**
 * This lesson introduces **Service Discovery**, a crucial pattern for making a
 * microservice architecture robust and scalable.
 *
 * ## The Problem: Hard-coded URLs
 *
 * In our previous lessons, the API Gateway and services had hard-coded URLs like
 * `http://localhost:8081`. This is a major problem in a real environment:
 * - **No Scalability:** What if we want to run 5 instances of the `user-service` to
 *   handle more traffic? The gateway only knows about one.
 * - **No Resilience:** If the `user-service` instance at port 8081 crashes, the
 *   entire system fails.
 * - **Dynamic Environments:** In the cloud, services are often deployed with dynamic
 *   IP addresses and ports. We can't know their location ahead of time.
 *
 * ## The Solution: Service Discovery with Eureka
 *
 * A Service Discovery pattern involves three components:
 * 1.  **Service Registry (Eureka Server):** A central server that acts like a phone
 *     book for all your microservices.
 * 2.  **Service Provider (Eureka Client):** Each microservice, upon starting up,
 *     "registers" itself with the Eureka Server, telling it "I am the user-service,
 *     and I am available at this address." It also sends regular heartbeats to prove
 *     it's still alive.
 * 3.  **Service Consumer (Another Eureka Client):** When one service (like our API Gateway)
 *     needs to talk to another, it first asks the Eureka Server, "What are the current
 *     addresses for the user-service?" Eureka provides a list of all available instances.
 *
 * We will use **Netflix Eureka**, part of the Spring Cloud ecosystem.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <!-- For the Eureka Server application -->
 * <dependency>
 *     <groupId>org.springframework.cloud</groupId>
 *     <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
 * </dependency>
 *
 * <!-- For ALL other microservices (Gateway, User, Product) -->
 * <dependency>
 *     <groupId>org.springframework.cloud</groupId>
 *     <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
 * </dependency>
 *
 * <!-- Spring Cloud BOM is required in <dependencyManagement> -->
 * ```
 *
 * ## SETUP: Configuration via `application.properties`
 *
 * - **For Eureka Server (`src/main/resources/application-eureka.properties`):**
 *   ```properties
 *   server.port=8761
 *   # These settings tell the Eureka server not to register itself as a client.
 *   eureka.client.register-with-eureka=false
 *   eureka.client.fetch-registry=false
 *   ```
 *
 * - **For API Gateway, User Service, and Product Service (e.g., `application-users.properties`):**
 *   ```properties
 *   # The name this service will register itself with in Eureka.
 *   spring.application.name=user-service
 *   # The address of the Eureka server.
 *   eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
 *   ```
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Compile:** `mvn compile`
 * 2.  **Start the Eureka Server (Terminal 1):** `java com.yourpackage.EurekaServerApplication`
 * 3.  **Open the Eureka Dashboard:** In your browser, go to `http://localhost:8761`. You'll see a dashboard.
 * 4.  **Start User Service (Terminal 2):** `java com.yourpackage.UserServiceApplication`
 * 5.  **Start Product Service (Terminal 3):** `java com.yourpackage.ProductServiceApplication`
 *     *Refresh the Eureka dashboard. You will now see `USER-SERVICE` and `PRODUCT-SERVICE` registered!*
 * 6.  **Start API Gateway (Terminal 4):** `java com.yourpackage.ApiGatewayApplication`
 * 7.  **Test through the Gateway:** `curl http://localhost:8080/users`. It works! The gateway dynamically discovered the user service via Eureka.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// =====================================================================================
// --- EUREKA DISCOVERY SERVER (Runs on Port 8761) ---
// =====================================================================================

@SpringBootApplication
@EnableEurekaServer // This single annotation turns this app into a Eureka server.
class EurekaServerApplication {
    public static void main(String[] args) {
        // To run, create an `application-eureka.properties` file as described above
        // and activate it with --spring.config.name=application-eureka
        System.setProperty("spring.config.name", "application-eureka");
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}

// =====================================================================================
// --- API GATEWAY (Runs on Port 8080) --- UPDATED FOR DISCOVERY ---
// =====================================================================================

@SpringBootApplication
@EnableDiscoveryClient // This annotation enables the app to act as a discovery client.
class ApiGatewayApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-gateway");
        // Create an application-gateway.properties file with its port and Eureka
        // settings.
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // **THE KEY CHANGE IS HERE**
                // Instead of a hard-coded URI, we use `lb://SERVICE-NAME`.
                // `lb` stands for "load balancer". Spring Cloud Gateway will query Eureka
                // for the address of "user-service" and route the request there.
                .route("user_service_route", r -> r.path("/users/**")
                        .uri("lb://user-service"))

                .route("product_service_route", r -> r.path("/products/**")
                        .uri("lb://product-service"))
                .build();
    }
}

// =====================================================================================
// --- USER SERVICE (Dynamic Port) & PRODUCT SERVICE (Dynamic Port) ---
// =====================================================================================

// Both services now only need the @EnableDiscoveryClient annotation and the
// correct
// configuration to register with Eureka. Spring Boot handles the rest.

@SpringBootApplication
@EnableDiscoveryClient
class UserServiceApplication {
    public static void main(String[] args) {
        // Create an application-users.properties file with its app name and Eureka
        // settings.
        // `server.port=0` tells Spring Boot to start on a random available port.
        System.setProperty("spring.config.name", "application-users");
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

@SpringBootApplication
@EnableDiscoveryClient
class ProductServiceApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-products");
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}

// REST Controllers remain unchanged from the previous lesson.
@RestController
class UserController {
    private final Map<Integer, String> userDb = new ConcurrentHashMap<>(Map.of(1, "Alice", 2, "Bob"));

    @GetMapping("/users")
    public Collection<String> getAll() {
        return userDb.values();
    }
}

@RestController
class ProductController {
    private final Map<Integer, String> productDb = new ConcurrentHashMap<>(Map.of(101, "Laptop", 102, "Mouse"));

    @GetMapping("/products")
    public Collection<String> getAll() {
        return productDb.values();
    }
}