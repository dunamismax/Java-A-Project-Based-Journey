
/**
 * This lesson builds a functional REST API using Spring Boot to perform CRUD
 * (Create, Read, Update, Delete) operations on a collection of users.
 *
 * We will see how Spring Boot's annotations make it easy to define endpoints,
 * handle URL parameters, and process JSON data, while using Dependency Injection
 * to structure our application cleanly.
 *
 * ## Core Concepts & Annotations Covered:
 * - **`@RestController`**: Marks a class as a web request handler.
 * - **`@Repository`**: Marks a class as a data-layer component (a "bean").
 * - **Dependency Injection**: How Spring automatically provides ("injects") a repository into our controller.
 * - **`@GetMapping`**, **`@PostMapping`**: Maps HTTP GET and POST requests.
 * - **`@PathVariable`**: Extracts values from the URL path (e.g., the `id` in `/api/users/{id}`).
 * - **`@RequestBody`**: Deserializes JSON from the request body into a Java object.
 *
 * PREQUISITE: Maven `pom.xml` with `spring-boot-starter-web`.
 *
 * HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method below.
 * 2.  Use `curl` or an API client to send requests to `http://localhost:8080`.
 *
 *     - **Get all users:** `curl http://localhost:8080/api/users`
 *
 *     - **Get user with ID 1:** `curl http://localhost:8080/api/users/1`
 *
 *     - **Get non-existent user:** `curl -i http://localhost:8080/api/users/99`
 *
 *     - **Create a new user:**
 *       `curl -X POST -H "Content-Type: application/json" -d '{"name":"Charlie", "email":"charlie@example.com"}' http://localhost:8080/api/users`
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// --- Main Application Entry Point ---
@SpringBootApplication
public class YourFirstAPIWithSpring {
    public static void main(String[] args) {
        SpringApplication.run(YourFirstAPIWithSpring.class, args);
    }
}

// --- 1. The Data Model ---
// A simple record to represent a User. Spring Boot handles JSON conversion
// automatically.
record User(Integer id, String name, String email) {
}

// --- 2. The Data Access Layer (Repository) ---
// The @Repository annotation tells Spring to create a single instance (a
// "bean") of this
// class and manage its lifecycle. It encapsulates our data storage logic.
@Repository
class UserRepository {
    // Using a thread-safe Map as our in-memory database.
    private final Map<Integer, User> userDatabase = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();

    public UserRepository() {
        // Initialize with some dummy data.
        save(new User(null, "Alice", "alice@example.com"));
        save(new User(null, "Bob", "bob@example.com"));
    }

    public Collection<User> findAll() {
        return userDatabase.values();
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(userDatabase.get(id));
    }

    public User save(User user) {
        int id = idCounter.incrementAndGet();
        User newUser = new User(id, user.name(), user.email());
        userDatabase.put(id, newUser);
        return newUser;
    }
}

// --- 3. The Web API Layer (Controller) ---
// @RestController combines @Controller and @ResponseBody, simplifying the
// creation of RESTful web services.
// @RequestMapping sets a base path for all endpoints in this controller.
@RestController
@RequestMapping("/api/users")
class UserController {

    // --- Dependency Injection ---
    // Instead of `new UserRepository()`, we declare the dependency.
    // Spring sees this constructor and "injects" the managed UserRepository bean.
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles GET /api/users
     * 
     * @return A collection of all users.
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Handles GET /api/users/{id}
     * 
     * @param id The ID from the URL path.
     * @return The User if found, or a 404 Not Found status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user)) // If found, return 200 OK with the user
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    /**
     * Handles POST /api/users
     * 
     * @param user The User object deserialized from the JSON request body.
     * @return The newly created user with a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        // Return a 201 Created status for successful resource creation.
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}