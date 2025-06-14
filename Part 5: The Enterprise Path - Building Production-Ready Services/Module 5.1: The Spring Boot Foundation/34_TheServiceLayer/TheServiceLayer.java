
/**
 * This lesson introduces the "Service Layer," an essential architectural pattern
 * that separates business logic from web request handling.
 *
 * ## The Problem: Overloaded Controllers
 *
 * In previous lessons, our controllers handled everything: HTTP requests, JSON
 * conversion, and business logic (like finding or creating users). This violates
 * the Single Responsibility Principle and makes the controller difficult to test and maintain.
 *
 * ## The Solution: The Service Layer
 *
 * A service layer is an intermediate layer that sits between the web controller and
 * the data repository. Its sole purpose is to contain the core business logic of the
 * application.
 *
 * The new flow of a request becomes:
 * `Controller (Web) -> Service (Business Logic) -> Repository (Data)`
 *
 * ### Benefits:
 * - **Separation of Concerns:** Each layer has a clear, distinct responsibility.
 * - **Testability:** Business logic can be unit tested independently of the web layer.
 * - **Reusability:** Business logic in the service layer can be reused by multiple
 *   controllers or other parts of the application.
 *
 * ## Core Concepts & Annotations Covered:
 * - **`@Service`**: A Spring stereotype annotation that marks a class as a business
 *   logic component. It's functionally similar to `@Repository` but used for a
 *   different architectural layer.
 * - **Architectural Refactoring**: Moving business logic from the controller to the service.
 *
 * ## PREQUISITE: Maven `pom.xml` with `spring-boot-starter-web`.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * The API endpoints and testing commands are the same as the previous lesson. The
 * difference is purely internal—the logic has been refactored into a cleaner,
 * more professional architecture.
 *
 * - `curl http://localhost:8080/api/users`
 * - `curl http://localhost:8080/api/users/1`
 * - `curl -X POST -H "Content-Type: application/json" -d '{"name":"Charlie", "email":"charlie@example.com"}' http://localhost:8080/api/users`
 * - `curl -i -X POST -H "Content-Type: application/json" -d '{"name":"Eve", "email":"alice@example.com"}' http://localhost:8080/api/users`
 *   (This last one will fail with a 409 Conflict, proving our new business logic works!)
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// --- Main Application Entry Point ---
@SpringBootApplication
public class TheServiceLayer {
    public static void main(String[] args) {
        SpringApplication.run(TheServiceLayer.class, args);
    }
}

// --- 1. The Data Model, Custom Exceptions, and Error DTO ---
record User(Integer id, String name, String email) {
}

class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String m) {
        super(m);
    }
}

class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String m) {
        super(m);
    }
}

record ErrorResponse(int status, String error, String message) {
}

// --- 2. The Data Access Layer (Repository) ---
// This layer's only job is to interact with the data source. No business logic
// here.
@Repository
class UserRepository {
    private final Map<Integer, User> db = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();

    public UserRepository() {
        save(new User(null, "Alice", "alice@example.com"));
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(db.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return db.values().stream().filter(u -> u.email().equalsIgnoreCase(email)).findFirst();
    }

    public Collection<User> findAll() {
        return db.values();
    }

    public User save(User user) {
        if (user.id() == null) {
            int id = idCounter.incrementAndGet();
            User savedUser = new User(id, user.name(), user.email());
            db.put(id, savedUser);
            return savedUser;
        }
        db.put(user.id(), user);
        return user;
    }
}

// --- 3. The Business Logic Layer (Service) ---
// @Service marks this as a business logic component.
@Service
class UserService {
    // The service layer depends on the repository layer.
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public User createUser(User user) {
        // This is business logic! A controller shouldn't know or care about this check.
        userRepository.findByEmail(user.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException("A user with email '" + user.email() + "' already exists.");
        });
        return userRepository.save(user);
    }
}

// --- 4. The Web API Layer (Controller) ---
// The controller now depends on the service layer, not the repository.
@RestController
@RequestMapping("/api/users")
class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // The controller methods are now clean, simple, one-line delegations to the
    // service.
    // Its only job is to handle HTTP concerns.
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}

// --- 5. Global Exception Handler (Updated) ---
// This class now handles the new business-level exception from the service
// layer.
@ControllerAdvice
class GlobalApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, "Not Found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailConflict(EmailAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(409, "Conflict", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}