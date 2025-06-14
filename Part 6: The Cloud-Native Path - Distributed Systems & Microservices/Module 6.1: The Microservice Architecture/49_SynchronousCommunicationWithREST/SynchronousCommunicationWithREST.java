
/**
 * This lesson demonstrates synchronous, service-to-service communication
 * in a microservice architecture using a REST client.
 *
 * ## The Problem: Services Need to Collaborate
 *
 * In our previous lesson, we split our application into a `UserService` and a
 * `ProductService`. The `ProductService` knows a product belongs to a user (it has a
 * `userId`), but it doesn't know the user's name or email. How can it get this
 * information to present a complete picture to the end-user?
 *
 * The `ProductService` needs to make a network call to the `UserService`'s API.
 *
 * ## The Solution: Spring's `RestTemplate`
 *
 * Spring provides the `RestTemplate` class, a powerful and straightforward tool for
 * making HTTP requests to other services. We will use it within our `ProductService`
 * to "call" an endpoint on the `UserService`.
 *
 * This type of communication is **synchronous**: when the `ProductService` requests user
 * details, it blocks and waits for the `UserService` to respond before it can
 * continue processing its own request.
 *
 * ## Core Concepts Covered:
 * - **Service-to-Service Communication**: The fundamental pattern of one microservice
 *   calling another's API.
 * - **`RestTemplate`**: Spring's classic, synchronous HTTP client.
 * - **DTOs for Aggregated Data**: Creating a new DTO (`ProductDetailDto`) to combine
 *   data from both the `ProductService` and the `UserService`.
 * - **Configuration**: Defining a `RestTemplate` object as a Spring `@Bean` so it can be
 *   injected wherever it's needed.
 *
 * ## PREQUISITES: `spring-boot-starter-web` in your `pom.xml`.
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Compile the file:** `mvn compile`
 * 2.  **Start the User Service (Terminal 1):**
 *     `java com.yourpackage.UserServiceApplication`
 * 3.  **Start the Product Service (Terminal 2):**
 *     `java com.yourpackage.ProductServiceApplication`
 * 4.  **Test the new, aggregated endpoint with `curl`:**
 *
 *     `curl http://localhost:8082/products/101/details`
 *
 *     Observe the output. You will see a JSON object that combines product information
 *     with user information fetched from the other service!
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// =====================================================================================
// --- USER SERVICE (Runs on Port 8081) - UNCHANGED FROM PREVIOUS LESSON ---
// =====================================================================================

@SpringBootApplication
class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8081"));
        app.run(args);
    }
}

record User(Integer id, String username, String email) {
}

@Repository
class UserRepository {
    private final Map<Integer, User> db = new ConcurrentHashMap<>(Map.of(
            1, new User(1, "Alice", "alice@example.com"),
            2, new User(2, "Bob", "bob@example.com")));

    public Optional<User> findById(int id) {
        return Optional.ofNullable(db.get(id));
    }

    public Collection<User> findAll() {
        return db.values();
    }
}

@RestController
class UserController {
    private final UserRepository repo;

    public UserController(UserRepository r) {
        this.repo = r;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

// =====================================================================================
// --- PRODUCT SERVICE (Runs on Port 8082) - UPDATED FOR COMMUNICATION ---
// =====================================================================================

@SpringBootApplication
class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8082"));
        app.run(args);
    }

    // We define the RestTemplate as a @Bean. This tells Spring to create a single
    // instance of it that can be injected anywhere in our application.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

// Original Product record
record Product(Integer id, String name, double price, Integer userId) {
}

// A new DTO to hold the combined Product and User information.
record ProductDetailDto(Integer id, String name, double price, User productOwner) {
}

@Repository
class ProductRepository {
    private final Map<Integer, Product> db = new ConcurrentHashMap<>(Map.of(
            101, new Product(101, "Laptop Pro", 1499.99, 1),
            102, new Product(102, "Wireless Mouse", 49.50, 1),
            103, new Product(103, "Mechanical Keyboard", 120.00, 2)));

    public Optional<Product> findById(int id) {
        return Optional.ofNullable(db.get(id));
    }
}

@Service
class ProductService {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    // We inject both the repository and the RestTemplate.
    public ProductService(ProductRepository productRepo, RestTemplate restTemplate) {
        this.productRepository = productRepo;
        this.restTemplate = restTemplate;
    }

    public Optional<ProductDetailDto> getProductDetails(int productId) {
        // 1. Find the product in our own database.
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return Optional.empty();
        }
        Product product = productOpt.get();

        // 2. Make an HTTP GET request to the UserService to fetch the user's details.
        // The URL points to the other microservice.
        String userServiceUrl = "http://localhost:8081/users/" + product.userId();
        try {
            User user = restTemplate.getForObject(userServiceUrl, User.class);

            // 3. Combine the data into our new DTO.
            ProductDetailDto dto = new ProductDetailDto(product.id(), product.name(), product.price(), user);
            return Optional.of(dto);
        } catch (Exception e) {
            // In a real app, handle this error more gracefully (e.g., circuit breaker).
            System.err.println("Error calling UserService: " + e.getMessage());
            return Optional.empty();
        }
    }
}

@RestController
class ProductController {
    private final ProductService productService;

    public ProductController(ProductService service) {
        this.productService = service;
    }

    @GetMapping("/products/{id}/details")
    public ResponseEntity<ProductDetailDto> getProductWithDetails(@PathVariable int id) {
        // The controller calls the service, which handles the communication.
        return productService.getProductDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}