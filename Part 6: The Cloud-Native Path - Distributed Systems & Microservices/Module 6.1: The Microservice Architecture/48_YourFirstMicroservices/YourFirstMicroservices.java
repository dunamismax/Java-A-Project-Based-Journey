
/**
 * This lesson is our first practical step into the microservice architecture. We will
 * take the concepts from the previous lesson and split a single application's
 * responsibilities into two smaller, independent services.
 *
 * ## The Goal: Decomposing the Monolith
 *
 * We will create two separate, runnable Spring Boot applications within this single file:
 *
 * 1.  **`UserService`**: A microservice solely responsible for managing user data. It will
 *     know nothing about products. It will run on **port 8081**.
 *
 * 2.  **`ProductService`**: A microservice solely responsible for managing product data. It
 *     will know that a product belongs to a user (via a `userId`), but it will not
 *     have direct access to the user's details. It will run on **port 8082**.
 *
 * This separation introduces a new, fundamental challenge: **How do these services
 * communicate with each other?** If the `ProductService` needs to display a user's name,
 * how does it get that information? We will solve this problem in the next lesson.
 *
 * ## PREQUISITES:
 * The `spring-boot-starter-web` dependency in your `pom.xml`.
 *
 * ## HOW TO RUN AND TEST THIS:
 * This requires running two applications simultaneously from separate terminals.
 *
 * 1.  **Compile the file:** From your project's root directory, run:
 *     `mvn compile`
 *
 * 2.  **Open Terminal 1 and start the User Service:**
 *     Navigate to your target/classes directory and run the `UserServiceApplication`.
 *     ```sh
 *     java com.yourpackage.UserServiceApplication
 *     ```
 *
 * 3.  **Open Terminal 2 and start the Product Service:**
 *     Navigate to your target/classes directory and run the `ProductServiceApplication`.
 *     ```sh
 *     java com.yourpackage.ProductServiceApplication
 *     ```
 *
 * 4.  **Test each service independently with `curl`:**
 *
 *     - **Get all users (from port 8081):**
 *       `curl http://localhost:8081/users`
 *
 *     - **Get user with ID 1 (from port 8081):**
 *       `curl http://localhost:8081/users/1`
 *
 *     - **Get all products (from port 8082):**
 *       `curl http://localhost:8082/products`
 *
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// =====================================================================================
// --- USER SERVICE (Runs on Port 8081) ---
// =====================================================================================

@SpringBootApplication
class UserServiceApplication {
    public static void main(String[] args) {
        // We programmatically set the port to avoid conflicts with the other service.
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8081"));
        app.run(args);
    }
}

record User(Integer id, String username, String email) {
}

@Repository
class UserRepository {
    private final Map<Integer, User> userDb = new ConcurrentHashMap<>();

    public UserRepository() {
        userDb.put(1, new User(1, "Alice", "alice@example.com"));
        userDb.put(2, new User(2, "Bob", "bob@example.com"));
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(userDb.get(id));
    }

    public Collection<User> findAll() {
        return userDb.values();
    }
}

@RestController
class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository repo) {
        this.userRepository = repo;
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

// =====================================================================================
// --- PRODUCT SERVICE (Runs on Port 8082) ---
// =====================================================================================

@SpringBootApplication
class ProductServiceApplication {
    public static void main(String[] args) {
        // We set a different port for this service.
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(Map.of("server.port", "8082"));
        app.run(args);
    }
}

// This product belongs to a user, but we only store the user's ID.
record Product(Integer id, String name, double price, Integer userId) {
}

@Repository
class ProductRepository {
    private final Map<Integer, Product> productDb = new ConcurrentHashMap<>();

    public ProductRepository() {
        productDb.put(101, new Product(101, "Laptop Pro", 1499.99, 1)); // Belongs to Alice
        productDb.put(102, new Product(102, "Wireless Mouse", 49.50, 1)); // Belongs to Alice
        productDb.put(103, new Product(103, "Mechanical Keyboard", 120.00, 2)); // Belongs to Bob
    }

    public Collection<Product> findAll() {
        return productDb.values();
    }
}

@RestController
class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository repo) {
        this.productRepository = repo;
    }

    @GetMapping("/products")
    public Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }
}